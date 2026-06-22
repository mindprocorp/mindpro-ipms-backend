package kr.co.mindpro.ipms.domain.ai.tool;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.ai.service.SchemaFieldMappingService;
import kr.co.mindpro.ipms.domain.ai.util.AiToolSecurityHelper;
import kr.co.mindpro.ipms.domain.ai.util.VectorDocumentParser;
import kr.co.mindpro.ipms.domain.conflict.service.ConflictService;
import kr.co.mindpro.ipms.domain.customer.service.CustomerService;
import kr.co.mindpro.ipms.domain.invoice.service.InvoiceService;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.service.DomesticAppService;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.service.OverseaAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchDatabaseListTool {

    private final CustomerService customerService;
    private final DomesticAppService domesticAppService;
    private final OverseaAppService overseaAppService;
    private final ConflictService conflictService;
    private final InvoiceService invoiceService;
    private final AiToolSecurityHelper securityHelper;
    private final SchemaFieldMappingService mappingService;

    @Tool(description = """
        [MANDATORY FOR SEARCHES] 사내 데이터를 조회할 때 반드시 실행하십시오. 
        날짜 검색(마감일, 출원일 등)은 반드시 'dateFilters'를 사용하여 실시간 SQL 조회를 수행합니다.
        유효 도메인(domainType): [DOMESTIC_APP, OVERSEA_APP, CONFLICT, CUSTOMER, INVOICE_DOMESTIC]
        지식(RAG) 검색 결과보다 이 도구의 검색 결과를 최우선하여 답변하십시오.
        """)
    public String searchDatabaseList(String domainType, String officeSeq, String loginUser,
                                     List<Map<String, String>> searchCondition,
                                     List<Map<String, String>> textFilters,
                                     List<Map<String, String>> dateFilters,
                                     Integer page, Integer pageSize) {
        
        log.info("[AI Tool Input - searchDatabaseList] domain: {}, condition: {}, text: {}, date: {}", 
                domainType, searchCondition, textFilters, dateFilters);
        
        Authentication originalAuth = SecurityContextHolder.getContext().getAuthentication();
        try {
            securityHelper.setupAiSecurityContext(officeSeq, loginUser);
            
            // 1. 도메인 정규화 (Self-Healing)
            String resolvedDomain = mappingService.resolveDomain(domainType);
            
            // 2. 파라미터 지능형 재라우팅 (Intelligence Layer)
            // AI가 실수로 searchCondition에 넣은 날짜 필터를 dateFilters로 이동
            List<Map<String, String>> finalDateFilters = (dateFilters != null) ? new ArrayList<>(dateFilters) : new ArrayList<>();
            List<Map<String, String>> finalSearchCondition = (searchCondition != null) ? new ArrayList<>(searchCondition) : new ArrayList<>();
            
            preprocessParameters(resolvedDomain, finalSearchCondition, finalDateFilters);

            // 3. AI가 보낸 필드명을 기술적 필드명으로 변환 및 검증 (Strict Validation Layer)
            List<String> invalidFields = new ArrayList<>();
            for (Map<String, String> cond : finalSearchCondition) {
                String originalField = cond.get("codeName");
                String technicalField = mappingService.resolveTechnicalField(resolvedDomain, originalField);
                if (technicalField.equals(originalField)) {
                    // 필드가 변환되지 않은 경우 (가이드나 사전에 없는 경우)
                    invalidFields.add(originalField);
                }
                cond.put("codeName", technicalField);
            }

            if (!invalidFields.isEmpty()) {
                String errorMsg = String.format("검색 실패: 유효하지 않은 필드명(%s)이 포함되어 있습니다. " +
                        "필드명을 정확히 알 수 없는 경우 반드시 'SearchVectorDatabaseTool'을 먼저 호출하여 기술 명칭을 사전에서 확인하십시오.", invalidFields);
                log.warn(">>>> [AI VALIDATION ERROR] Domain: {}, InvalidFields: {}", resolvedDomain, invalidFields);
                return errorMsg;
            }

            int finalPage = (page != null) ? page : 1;
            int finalSize = (pageSize != null) ? pageSize : 10;

            BaseSearchRequest req = BaseSearchRequest.builder()
                    .page(finalPage).pageSize(finalSize).officeSeq(officeSeq).build();
            req.setSearchCondition(finalSearchCondition);
            if (textFilters != null) req.setTextFilters(textFilters);
            req.setDateFilters(finalDateFilters);

            Object result = switch (resolvedDomain.toUpperCase()) {
                case "CUSTOMER" -> customerService.getCustomerList(req);
                case "DOMESTIC_APP" -> domesticAppService.getDomesticAppSearchList(req);
                case "OVERSEA_APP" -> overseaAppService.getOverseaList(req);
                case "CONFLICT" -> conflictService.getConflictList(req);
                case "INVOICE_DOMESTIC" -> invoiceService.getDomesticList(req);
                default -> null;
            };
            
            if (result == null && !"DOMESTIC_APP,OVERSEA_APP,CUSTOMER,CONFLICT,INVOICE_DOMESTIC".contains(resolvedDomain.toUpperCase())) {
                return String.format("유효하지 않은 도메인('%s')입니다. [DOMESTIC_APP, CONFLICT, CUSTOMER, INVOICE_DOMESTIC, OVERSEA_APP] 중 하나를 선택하여 정확한 쿼리를 다시 생성하십시오.", domainType);
            }

            return result != null ? VectorDocumentParser.parseToText(result, resolvedDomain) : "검색 조건에 맞는 데이터가 없습니다.";
        } catch (Exception e) {
            log.error("데이터 목록 검색 실패: {}", domainType, e);
            return "검색 실패: 시스템 오류가 발생했습니다. (사유: " + e.getMessage() + ")";
        } finally {
            SecurityContextHolder.getContext().setAuthentication(originalAuth);
        }
    }

    /**
     * AI의 파라미터 실수를 자동으로 보정합니다.
     * searchCondition에 들어있는 날짜 관련 데이터를 dateFilters로 이동시킵니다.
     */
    private void preprocessParameters(String domain, List<Map<String, String>> conditions, List<Map<String, String>> dateFilters) {
        if (conditions == null || conditions.isEmpty()) return;

        Iterator<Map<String, String>> iterator = conditions.iterator();
        while (iterator.hasNext()) {
            Map<String, String> cond = iterator.next();
            String field = cond.get("codeName");
            String value = cond.get("value");

            if (field == null || value == null) continue;

            // 날짜 형식 감지 (YYYY-MM-DD 또는 YYYYMMDD) 또는 필드명에 '일', '마감' 포함 시
            boolean isDateValue = value.matches("^\\d{4}-\\d{2}-\\d{2}$") || value.matches("^\\d{8}$");
            boolean isDateField = field.contains("일") || field.contains("마감") || field.contains("Date") || field.contains("Deadline");

            if (isDateValue || isDateField) {
                log.info(">>>> [AI Parameter Rerouting] Field '{}' moved to dateFilters", field);
                // dateFilters 형식으로 변환 (startDate, endDate 모두 동일하게 설정하여 특정일 검색 지원)
                cond.put("startDate", value.replace("-", ""));
                cond.put("endDate", value.replace("-", ""));
                dateFilters.add(cond);
                iterator.remove();
            }
        }
    }
}
