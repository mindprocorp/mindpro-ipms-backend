package kr.co.mindpro.ipms.domain.searchcondition.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchConditionVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SearchConditionResponse {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private String conditionSeq;
    private String menuCode;
    private String conditionName;

    @Schema(description = "기본 검색 옵션 리스트 {key: value}")
    private Map<String, Object> searchOptions;

    @Schema(description = "일자 필터 리스트 [{type, startDate, endDate, andOrNOT}]")
    private List<Map<String, Object>> dateFilters;

    @Schema(description = "문자 필터 리스트 [{type, value, andOrNOT}]")
    private List<Map<String, Object>> textFilters;

    private String useYn;
    private String createAt;

    public static SearchConditionResponse of(SearchConditionVO vo, ObjectMapper objectMapper) {
        if (vo == null) return null;
        try {
            Map<String, Object> options = parseJsonMap(vo.getSearchOptions(), objectMapper);
            List<Map<String, Object>> dates   = parseJsonList(vo.getDateFilters(), objectMapper);
            List<Map<String, Object>> texts   = parseJsonList(vo.getTextFilters(), objectMapper);

            return SearchConditionResponse.builder()
                    .conditionSeq(vo.getConditionSeq())
                    .menuCode(vo.getMenuCode())
                    .conditionName(vo.getConditionName())
                    .useYn(vo.getUseYn())
                    .searchOptions(options)
                    .dateFilters(dates)
                    .textFilters(texts)
                    .createAt(vo.getCreateAt() != null ? vo.getCreateAt().format(DATE_FORMATTER) : null)
                    .build();
        } catch (Exception e) {
            log.error("SearchConditionResponse 변환 오류: {}", e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> parseJsonList(String json, ObjectMapper mapper) {
        try {
            if (json == null || json.isBlank()) return null;
            return mapper.readValue(json, List.class);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> parseJsonMap(String json, ObjectMapper mapper) {
        try {
            if (json == null || json.isBlank()) return null;
            com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(json);
            if (node.isObject()) {
                return mapper.treeToValue(node, Map.class);
            }
            // 레거시: [{key, value}] 배열 포맷 → {key: value} Map 변환
            if (node.isArray()) {
                Map<String, Object> result = new java.util.LinkedHashMap<>();
                for (com.fasterxml.jackson.databind.JsonNode item : node) {
                    if (item.has("key")) {
                        result.put(item.get("key").asText(), item.has("value") ? item.get("value").asText() : "");
                    }
                }
                return result.isEmpty() ? null : result;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
