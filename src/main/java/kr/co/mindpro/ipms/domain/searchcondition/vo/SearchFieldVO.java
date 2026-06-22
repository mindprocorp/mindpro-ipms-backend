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
  @Schema(description = "사용자 검색 조건 정보")
  public class SearchFieldVO {

    private String searchFieldSeq;
    private String menuCode;
    private String fieldKey;         // UI의 type (예: receiptDate)
    private String targetTable;      // MST, DUEDATE, PARTI
    private String dbColumn;         // 실제 데이터가 있는 컬럼 (예: duedate_date)
    private String searchCodeColumn; // 코드를 비교할 컬럼 (예: duedate_category_code)
    private String searchCodeValue;  // 비교할 코드 값 (예: claimDate)
    private String dataType;         // TEXT, DATE, PERSON
    private String remark;

  }
