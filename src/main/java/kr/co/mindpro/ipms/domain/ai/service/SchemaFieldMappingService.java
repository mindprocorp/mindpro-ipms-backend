package kr.co.mindpro.ipms.domain.ai.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class SchemaFieldMappingService {
    
    private static final Logger log = LoggerFactory.getLogger(SchemaFieldMappingService.class);

    private static final Map<String, List<String>> DOMAIN_VO_MAPPING = Map.of(
            "DOMESTIC_APP", List.of(
                    "kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO",
                    "kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppMstVO",
                    "kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppPatentVO",
                    "kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppDesignVO",
                    "kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppTrademarkVO",
                    "kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.AppBasicInfoVO"
            ),
            "OVERSEA_APP", List.of(
                    "kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO",
                    "kr.co.mindpro.ipms.domain.patentApp.overseaApp.vo.AppExtMstVO"
            ),
            "CUSTOMER", List.of(
                    "kr.co.mindpro.ipms.domain.customer.vo.CustomerVO",
                    "kr.co.mindpro.ipms.domain.customer.vo.ModifiedHistVO"
            ),
            "CONFLICT", List.of(
                    "kr.co.mindpro.ipms.domain.conflict.vo.ConflictMergeVO",
                    "kr.co.mindpro.ipms.domain.conflict.vo.ConflictMstVO"
            ),
            "INVOICE_DOMESTIC", List.of(
                    "kr.co.mindpro.ipms.domain.invoice.vo.InvoiceMstVO",
                    "kr.co.mindpro.ipms.domain.invoice.vo.InvoiceClaimVO",
                    "kr.co.mindpro.ipms.domain.invoice.vo.InvoiceBankingVO"
            ),
            "BIZ_INFO", List.of(
                    "kr.co.mindpro.ipms.domain.bizinfo.vo.BizInfoVO"
            )
    );

    private static final Map<String, String> DOMAIN_ALIAS = Map.of(
            "이의심판", "CONFLICT",
            "이민", "CONFLICT",
            "분쟁", "CONFLICT",
            "출원", "DOMESTIC_APP",
            "특허출원", "DOMESTIC_APP",
            "고객", "CUSTOMER",
            "청구서", "INVOICE_DOMESTIC",
            "해외출원", "OVERSEA_APP"
    );

    private final Map<String, Map<String, FieldInfo>> domainFieldCache = new HashMap<>();

    @PostConstruct
    public void init() {
        log.info(">>>> [AI] SchemaFieldMappingService 초기화 시작");
        loadAllDomainFields();
        log.info(">>>> [AI] SchemaFieldMappingService 초기화 완료 - 도메인 수: {}", domainFieldCache.size());
    }

    private void loadAllDomainFields() {
        for (Map.Entry<String, List<String>> entry : DOMAIN_VO_MAPPING.entrySet()) {
            String domain = entry.getKey();
            List<String> voClassNames = entry.getValue();
            Map<String, FieldInfo> fieldMap = new LinkedHashMap<>();

            for (String voClassName : voClassNames) {
                try {
                    Class<?> clazz = Class.forName(voClassName);
                    extractFieldsFromClass(clazz, fieldMap);
                    log.debug(">>>> [AI] {} -> {} 필드 추출 완료", domain, voClassName);
                } catch (ClassNotFoundException e) {
                    log.warn(">>>> [AI] 클래스 찾을 수 없음: {}", voClassName);
                }
            }

            domainFieldCache.put(domain, fieldMap);
        }
    }

    private void extractFieldsFromClass(Class<?> clazz, Map<String, FieldInfo> fieldMap) {
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                io.swagger.v3.oas.annotations.media.Schema schemaAnn = field.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
                
                String fieldName = field.getName();
                String description = "";
                String example = "";

                if (schemaAnn != null) {
                    if (StringUtils.hasText(schemaAnn.description())) {
                        description = schemaAnn.description();
                    }
                    if (StringUtils.hasText(schemaAnn.example())) {
                        example = schemaAnn.example();
                    }
                }

                if (!fieldMap.containsKey(fieldName)) {
                    fieldMap.put(fieldName, new FieldInfo(fieldName, field.getType().getSimpleName(), description, example));
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    @Tool(name = "getDomainFields", description = "도메인별 사용 가능한 필드명 목록을 조회합니다. AI가 검색 시 사용할 수 있는 정확한 필드명을 확인하려면 이 도구를 사용하세요. 'domainType'을 지정하면 해당 도메인의 모든 필드명, 설명, 타입 정보를 반환합니다. 도메인 별칭(이의심판→CONFLICT, 분쟁→CONFLICT 등)도 사용 가능합니다.")
    public String getAvailableFields(
            @ToolParam(description = "도메인 타입 (CUSTOMER, DOMESTIC_APP, CONFLICT, INVOICE_DOMESTIC, BIZ_INFO, OVERSEA_APP) 또는 별칭 (이의심판, 분쟁, 고객 등)") String domainType) {
        
        String domain = resolveDomain(domainType);
        Map<String, FieldInfo> fields = domainFieldCache.get(domain);

        if (fields == null || fields.isEmpty()) {
            return "도메인 '" + domain + "'에 대한 필드 정보를 찾을 수 없습니다. 사용 가능한 도메인: " + String.join(", ", DOMAIN_VO_MAPPING.keySet());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(domain).append(" 도메인 필드 목록 ===\n");
        sb.append("총 ").append(fields.size()).append("개 필드 (상위 ").append(Math.min(fields.size(), 50)).append("개 표시)\n\n");

        int count = 0;
        for (FieldInfo info : fields.values()) {
            if (count >= 50) break; // 50개로 제한
            sb.append(String.format("- %s", info.fieldName));
            if (StringUtils.hasText(info.description)) {
                sb.append(String.format(": %s", info.description));
            }
            sb.append("\n");
            count++;
        }

        if (fields.size() > 50) {
            sb.append("\n※ 더 많은 필드는 'searchFieldNames' 도구로 키워드 검색하세요.\n");
        }

        return sb.toString();
    }

    @Tool(name = "searchFieldNames", description = "도메인에서 특정 키워드(설명, 필드명)와 관련된 필드명을 검색합니다. AI가 어떤 필드를 사용해야 할지 막혔을 때 사용하세요. 'keyword'를 기반으로 필드명을 추천해줍니다.")
    public String searchFields(
            @ToolParam(description = "도메인 타입 또는 별칭") String domainType,
            @ToolParam(description = "검색 키워드 (예: 출원인, 사건담당자, 청구일)") String keyword) {
        
        String domain = resolveDomain(domainType);
        Map<String, FieldInfo> fields = domainFieldCache.get(domain);

        if (fields == null) {
            return "도메인 '" + domain + "'을 찾을 수 없습니다.";
        }

        String lowerKeyword = keyword.toLowerCase();
        List<FieldInfo> matched = new ArrayList<>();

        for (FieldInfo info : fields.values()) {
            if (info.fieldName.toLowerCase().contains(lowerKeyword) ||
                (info.description != null && info.description.toLowerCase().contains(lowerKeyword))) {
                matched.add(info);
            }
        }

        // 공통 별칭(Alias)에 대한 하드코딩된 발견 로직 추가 (사건마감일 등)
        if (lowerKeyword.contains("마감") || lowerKeyword.contains("기일")) {
            if (fields.containsKey("dueLimitDate") && !matched.contains(fields.get("dueLimitDate"))) {
                matched.add(fields.get("dueLimitDate"));
            }
        }
        if (lowerKeyword.contains("상태") || lowerKeyword.contains("단계")) {
            if (fields.containsKey("statusCodeName") && !matched.contains(fields.get("statusCodeName"))) {
                matched.add(fields.get("statusCodeName"));
            }
        }

        if (matched.isEmpty()) {
            return "키워드 '" + keyword + "'와 관련된 필드를 찾을 수 없습니다.";
        }

        int displayCount = Math.min(matched.size(), 20);
        StringBuilder sb = new StringBuilder();
        sb.append("=== '").append(keyword).append("' 관련 필드 (총 ").append(matched.size()).append("개, 상위 ").append(displayCount).append("개 표시) ===\n\n");
        
        for (int i = 0; i < displayCount; i++) {
            FieldInfo info = matched.get(i);
            sb.append(String.format("- %s", info.fieldName));
            if (StringUtils.hasText(info.description)) {
                sb.append(String.format(": %s", info.description));
            }
            sb.append("\n");
        }

        if (matched.size() > 20) {
            sb.append("\n※ 더 많은 결과는 필드명을 구체적으로 입력하여 검색하세요.\n");
        }

        return sb.toString();
    }

    public String resolveDomain(String input) {
        if (!StringUtils.hasText(input)) return "";
        String upper = input.toUpperCase();
        if (DOMAIN_VO_MAPPING.containsKey(upper)) {
            return upper;
        }
        String alias = DOMAIN_ALIAS.get(input);
        if (alias != null) {
            return alias;
        }
        return upper;
    }

    /**
     * AI가 전달한 필드명(한글 또는 영문)을 실제 시스템의 영문 필드명으로 변환합니다. (Self-Healing)
     */
    public String resolveTechnicalField(String domainType, String queryField) {
        if (!StringUtils.hasText(queryField)) return queryField;

        String domain = resolveDomain(domainType);
        Map<String, FieldInfo> fields = domainFieldCache.get(domain);

        if (fields == null || fields.isEmpty()) return queryField;

        // 1. 이미 정확한 영문 필드명인 경우 (Case-Sensitive 체크)
        if (fields.containsKey(queryField)) {
            return queryField;
        }

        // 2. 대소문자 무시 체크
        for (String fieldName : fields.keySet()) {
            if (fieldName.equalsIgnoreCase(queryField)) {
                return fieldName;
            }
        }

        // 3. [핵심] 한글 설명(Description)으로 매핑 찾기 (AI 환청 교정)
        for (FieldInfo info : fields.values()) {
            if (StringUtils.hasText(info.description)) {
                // 설명이 완전히 일치하거나, 설명 내에 검색어가 포함된 경우 (가장 가까운 것 선택)
                if (info.description.replace(" ", "").equals(queryField.replace(" ", "")) ||
                    info.description.contains(queryField)) {
                    log.info(">>>> [AI Self-Healing] 필드명 교정: '{}' -> '{}' (도메인: {})", queryField, info.fieldName, domain);
                    return info.fieldName;
                }
            }
        }

        // 4. 관습적 별칭 매핑 (도메인 정규화 외의 세부 필드 하드코딩은 제거 - 벡터 DB 검색 권장)
        if (queryField.equals("심판유형") || queryField.equals("사건구분")) return "caseTypeCodeName";
        if (queryField.equals("상태") || queryField.equals("진행상태") || queryField.equals("status")) return "statusCodeName";
        if (queryField.equals("사건명") || queryField.equals("심판명")) return "caseTitleKo";
        if (queryField.equals("사건마감일") || queryField.equals("마감일")) return "dueLimitDate";

        return queryField;
    }

    public static class FieldInfo {
        public String fieldName;
        public String type;
        public String description;
        public String example;

        public FieldInfo(String fieldName, String type, String description, String example) {
            this.fieldName = fieldName;
            this.type = type;
            this.description = description;
            this.example = example;
        }
    }
}