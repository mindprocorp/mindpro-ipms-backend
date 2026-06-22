package kr.co.mindpro.ipms.domain.searchcondition.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SearchConditionRequest {

    @Schema(description = "메뉴 코드", example = "CONFLICT", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuCode;

    @Schema(description = "검색 조건 명칭", example = "2026 상반기 특허 건", requiredMode = Schema.RequiredMode.REQUIRED)
    private String conditionName;

    @Schema(description = "기본 검색 옵션 리스트",
            example = "{\"caseCategory\": \"TRIAL\"}")
    private Map<String, Object> searchOptions;

    @Schema(description = "일자 필터 리스트",
            example = "[{\"type\":\"CREATE\", \"startDate\":\"20260101\", \"endDate\":\"20260630\", \"andOrNOT\":\"AND\"}]")
    private List<Map<String, Object>> dateFilters;

    @Schema(description = "문자 필터 리스트",
            example = "[{\"type\":\"TITLE\", \"value\":\"특허\", \"andOrNOT\":\"AND\"}]")
    private List<Map<String, Object>> textFilters;

    @Schema(description = "활성화 여부", example = "Y", allowableValues = {"Y", "N"})
    private String useYn;
}
