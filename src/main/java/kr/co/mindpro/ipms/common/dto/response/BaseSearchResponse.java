package kr.co.mindpro.ipms.common.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@Schema(description = "페이징 응답 공통 래퍼")
@NoArgsConstructor
@AllArgsConstructor
public class BaseSearchResponse<T> {

    @Schema(description = "전체 데이터 개수", example = "150")
    private int totalCount;

    @Schema(description = "현재 페이지 번호 (옵션)", example = "1")
    private int page;

    @Schema(description = "페이지당 개수 (옵션)", example = "20")
    private int size;

    @Schema(description = "전체 페이지 수 (옵션 - 계산 편의용)", example = "7")
    private int totalPage;

    @Schema(description = "데이터 목록")
    private List<T> list;

    /**
     * 데이터를 받아서 응답 객체 생성 (계산 로직 포함)
     */
    public static <T> BaseSearchResponse<T> of(List<T> list, int page, int size) {
        // 전체 페이지 수 계산 (나머지가 있으면 +1)
        if (size <= 0) {
            size = 20; // 기본값으로 강제 세팅하거나 예외 처리
        }
        int totalPage = (int) Math.ceil((double) list.size() / size);

        return BaseSearchResponse.<T>builder()
                .list(list)
                .totalCount(list.size())
                .page(page)
                .size(size)
                .totalPage(totalPage)
                .build();
    }

    /**
     * 데이터를 받아서 응답 객체 생성 (계산 로직 포함)
     */
    public static <T> BaseSearchResponse<T> of(List<T> list, int totalCount, int page, int size) {
        // 전체 페이지 수 계산 (나머지가 있으면 +1)
        if (size <= 0) {
            size = 20; // 기본값으로 강제 세팅하거나 예외 처리
        }
        int totalPage = (int) Math.ceil((double) totalCount / size);

        return BaseSearchResponse.<T>builder()
                .list(list)
                .totalCount(totalCount)
                .page(page)
                .size(size)
                .totalPage(totalPage)
                .build();
    }
}