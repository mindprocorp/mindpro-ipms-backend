package kr.co.mindpro.ipms.domain.searchcondition.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 검색 조건 저장 VO
   */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "사용자 검색 조건 정보")
  public class SearchQueryParamVO  {


    @Builder.Default
    private List<SearchParamVO> mstEqFilters = new ArrayList<>();

    @Builder.Default
    private List<SearchParamVO> mstLikeFilters = new ArrayList<>();

    @Builder.Default
    private List<SearchParamVO> pSearchList = new ArrayList<>();

    @Builder.Default
    private List<SearchParamVO> dSearchList = new ArrayList<>();


    private String officeSeq;

    private List<String> combinedIds;

  }
