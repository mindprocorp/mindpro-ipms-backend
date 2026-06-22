package kr.co.mindpro.ipms.domain.ai.util;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import org.springframework.util.StringUtils;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * 객체를 AI RAG용 텍스트로 변환하는 파서
 */
public class VectorDocumentParser {

    /**
     * VO 객체를 '요약' 텍스트로 변환 (목록 검색용)
     * 하위 객체(List, Custom Object)를 재귀적으로 탐색하지 않고 최상위 필드만 추출합니다.
     */
    public static String parseToSummaryText(Object data, String domainNm) {
        if (data == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("=== [요약 데이터 유형]: ").append(domainNm).append(" ===\n");
        sb.append("[안내]: 목록 조회를 위한 요약 정보입니다. 상세 정보는 개별 조회가 필요합니다.\n");
        sb.append("--------------------------------------------------\n");

        parseRecursive(data, sb, 0, false); // shallow=false
        return sb.toString();
    }

    public static String parseToSummaryText(Object data) {
        return parseToSummaryText(data, "일반 데이터 요약");
    }

    /**
     * VO 객체를 '전체' 텍스트로 변환 (상세 단건 조회용)
     */
    public static String parseToText(Object data, String domainNm) {
        if (data == null) return "";
        StringBuilder sb = new StringBuilder();

        sb.append("=== [상세 데이터 유형]: ").append(domainNm).append(" ===\n");
        sb.append("[데이터 개요]: 본 내용은 시스템에 등록된 '").append(domainNm).append("'의 상세 레코드 정보입니다.\n");
        sb.append("--------------------------------------------------\n");

        parseRecursive(data, sb, 0, true); // recursive=true
        return sb.toString();
    }

    public static String parseToText(Object data) {
        return parseToText(data, "일반 데이터 상세");
    }

    private static void parseRecursive(Object obj, StringBuilder sb, int depth, boolean isRecursive) {
        if (obj == null) return;

        // 공통 코드 정보 처리
        if (obj instanceof CommonRecordResponse.CodeInfo ci) {
            sb.append(StringUtils.hasText(ci.codeName()) ? ci.codeName() : ci.code()).append("\n");
            return;
        }

        // 사용자 정보 처리
        if (obj instanceof CommonRecordResponse.PersonInfo pi) {
            sb.append(StringUtils.hasText(pi.userName()) ? pi.userName() : pi.userSeq()).append("\n");
            return;
        }

        // 기본 타입 처리
        if (obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
            sb.append(obj).append("\n");
            return;
        }

        // 컬렉션 처리
        if (obj instanceof Collection<?> col) {
            for (Object item : col) parseRecursive(item, sb, depth, isRecursive);
            return;
        }

        // 객체 필드 탐색 (Reflection)
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                // 불필요 필드 제외
                if (value == null || field.getName().startsWith("$") || field.getName().equals("serialVersionUID")) continue;

                String fieldName = field.getName();
                String label = fieldName;

                // Swagger @Schema 어노테이션의 description을 라벨로 활용
                Schema schema = field.getAnnotation(Schema.class);
                if (schema != null && StringUtils.hasText(schema.description())) {
                    label = schema.description();
                }

                String technicalLabel = label; // [변경] AI 렌더링 오염 방지를 위해 영문 필드명 제거

                if (isCustomDomain(value) || value instanceof Collection<?>) {
                    if (isRecursive) {
                        // 하위 객체나 리스트인 경우 그룹화 표시
                        sb.append("\n").append("  ".repeat(depth)).append("[").append(technicalLabel).append("]\n");
                        parseRecursive(value, sb, depth + 1, true);
                    } else {
                        // 요약 모드인 경우 하위 객체는 존재 여부만 간단히 표시 (메모리 절약)
                        sb.append("  ".repeat(depth)).append(technicalLabel).append(": [상세 데이터 존재 - 개별 조회 필요]\n");
                    }
                } else {
                    // 일반 필드 값 출력
                    String strVal = value.toString().replace("\n", " ").trim();
                    if (!strVal.isEmpty()) {
                        sb.append("  ".repeat(depth)).append(technicalLabel).append(": ").append(strVal).append("\n");
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    private static boolean isCustomDomain(Object o) {
        return o != null && o.getClass().getPackageName().startsWith("kr.co.mindpro.ipms");
    }
}