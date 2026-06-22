package kr.co.mindpro.ipms.common.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * 모든 VO에서 공통으로 사용하는 컬럼 정의
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseSearchRequest {
    @JsonIgnore
    private String officeSeq;
    @JsonIgnore
    private int offSet;
    @JsonIgnore
    private String userDeptSeq;

    @Schema(description = "현재 로그인 사용자의 userInfoSeq (내 게시글 DRAFT 포함 조회 시 사용)")
    private String userInfoSeq;

    @Schema( description = "각 업무 식별자, 필요 시 사용.")
    private String tblSeq;

    // UI에서 공통으로 넘어오는 필터 구조들
    @Schema(hidden = true)
    private List<Map<String, String>> searchCondition; // 상단 검색바
    @Schema(hidden = true)
    private List<Map<String, String>> textFilters;     // 그리드 텍스트 필터
    @Schema(hidden = true)
    private List<Map<String, String>> dateFilters;     // 그리드 날짜 필터

    // 페이징 공통
    @Schema(example = "1")
    @Builder.Default private int page = 1;
    @Schema(example = "20")
    @Builder.Default private int pageSize = 50;

    @JsonIgnore
    public int getOffSet() {
        return page > 1 ? (page - 1) * pageSize : 0;
    }
}