package kr.co.mindpro.ipms.domain.searchcondition.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

/**
 * 사용자 검색 조건 저장 VO
   */
  @Data
  @SuperBuilder
  @NoArgsConstructor
  @Schema(description = "사용자 검색 조건 정보")
  public class SearchParamVO {



    @Schema(description = "구분 코드를 체크할 DB 컬럼명", example = "participant_code")
    private String codeCol;

    @Schema(description = "필터링할 구분 코드 값", example = "attorney")
    private String codeVal;

    @Schema(description = "실제 데이터(검색어/날짜)가 들어있는 DB 컬럼명", example = "user_info_seq")
    private String dataCol;

    @Schema(description = "일반 문자열 검색값 (TEXT 필터용)", example = "김변리")
    private String value;

    @Schema(description = "검색 시작일 (DATE 필터용, YYYYMMDD)", example = "20260101")
    private OffsetDateTime startDate;

    @Schema(description = "검색 종료일 (DATE 필터용, YYYYMMDD)", example = "20261231")
    private OffsetDateTime endDate;
    @Schema(description = "텍스트 필터 여부 (true: LIKE 이름검색, false: = ID검색)", example = "true")
    private boolean isTextFilter; // ★ 이 필드를 추가하세요!
  }
