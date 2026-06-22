package kr.co.mindpro.ipms.domain.searchcondition.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.List;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SearchConditionListResponse {
    @Schema(description = "검색 조건 목록")
    private List<SearchConditionResponse> list; // 파싱된 DTO들이 담김

    @Schema(description = "총 개수", example = "5")
    private int totalCount;

    public static SearchConditionListResponse of(List<SearchConditionResponse> list) {
        return SearchConditionListResponse.builder()
                .list(list)
                .totalCount(list != null ? list.size() : 0)
                .build();
    }

}
