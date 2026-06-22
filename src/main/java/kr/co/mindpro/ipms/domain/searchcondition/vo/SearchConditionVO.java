package kr.co.mindpro.ipms.domain.searchcondition.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 청구서 테이블 매핑 객체
 * DB 테이블 ipms_user 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : ParticipantVO.java
 * @since	 : 2026. 01. 07.
 */
  /**
   * 사용자 검색 조건 저장 VO
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    @Schema(description = "사용자 검색 조건 정보")
    public class SearchConditionVO extends BaseVO {

      @Schema(description = "검색 조건 시퀀스 (PK)", example = "SCON0000000001")
      private String conditionSeq;

      @Schema(description = "사용자 정보 시퀀스", example = "USER0000000001")
      private String userInfoSeq;

      @Schema(description = "메뉴 코드 (예: CONFLICT, APP, REG)", example = "CONFLICT")
      private String menuCode;

      @Schema(description = "검색 조건 명칭", example = "2026 상반기 특허 심판건")
      private String conditionName;

      @Schema(description = "핵심 검색 옵션 (JSON) - 구분, 권리 등 메뉴별 가변 항목", example = "{\"caseCategory\":\"TRIAL\", \"rightType\":\"PATENT\"}")
      private String searchOptions;

      @Schema(description = "일자 필터 리스트 (JSON)", example = "[{\"type\":\"CREATE\", \"startDate\":\"2026-01-01\", \"endDate\":\"2026-06-30\"}]")
      private String dateFilters;

      @Schema(description = "문자 필터 리스트 (JSON)", example = "[{\"type\":\"TITLE\", \"value\":\"특허\"}]")
      private String textFilters;

      @Schema(description = "활성화 여부", example = "Y")
      @Builder.Default
      private String useYn = "Y";

      @Schema(description = "삭제 여부", example = "N")
      @Builder.Default
      private String delYn = "N";
    }
