package kr.co.mindpro.ipms.domain.ai.tool;

import kr.co.mindpro.ipms.domain.ai.util.AiToolSecurityHelper;
import kr.co.mindpro.ipms.domain.ai.util.VectorDocumentParser;
import kr.co.mindpro.ipms.domain.bizinfo.service.BizInfoService;
import kr.co.mindpro.ipms.domain.conflict.service.ConflictService;
import kr.co.mindpro.ipms.domain.customer.service.CustomerService;
import kr.co.mindpro.ipms.domain.invoice.service.InvoiceService;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.service.DomesticAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchRealtimeDatabaseRecordTool {

    private final CustomerService customerService;
    private final BizInfoService bizInfoService;
    private final DomesticAppService domesticAppService;
    private final ConflictService conflictService;
    private final InvoiceService invoiceService;
    private final AiToolSecurityHelper securityHelper;

    @Tool(description = """
        특정 식별자(appSeq, conflictSeq 등)를 사용하여 개별 레코드의 실시간 상세 정보를 조회합니다.
        목록 검색(`searchDatabaseList`)을 통해 얻은 PK를 사용하여 상세 내용을 확인할 때 호출하십시오.
        결과 규격: '한글설명(영문필드명): 값' 형식의 여러 줄로 반환됩니다.
        """)
    public String fetchRealtimeDatabaseRecord(String domainType, String uniqueId, String officeSeq, String loginUser) {
        Authentication originalAuth = SecurityContextHolder.getContext().getAuthentication();
        try {
            securityHelper.setupAiSecurityContext(officeSeq, loginUser);
            Object result = switch (domainType.toUpperCase()) {
                case "CUSTOMER" -> customerService.getCustomerDetail(uniqueId);
                case "BIZ_INFO" -> bizInfoService.getBizInfoDetail(uniqueId);
                case "PATENT_APP" -> domesticAppService.getDomesticAppDetail(uniqueId);
                case "CONFLICT" -> conflictService.getConflictDetail(uniqueId);
                case "INVOICE_DOMESTIC" -> invoiceService.getDomesticDetail(uniqueId);
                default -> null;
            };
            return result != null ? VectorDocumentParser.parseToText(result) : "해당 데이터를 사내 DB에서 찾을 수 없습니다.";
        } catch (Exception e) {
            log.error("실시간 상세조회 실패: {}", domainType, e);
            return "조회 중 오류 발생: " + e.getMessage();
        } finally {
            SecurityContextHolder.getContext().setAuthentication(originalAuth);
        }
    }
}
