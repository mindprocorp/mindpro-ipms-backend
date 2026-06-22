package kr.co.mindpro.ipms.domain.example.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import kr.co.mindpro.ipms.domain.example.dto.enums.ExampleCourtCategoryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 의뢰심판 테이블 매핑 객체
 * DB 테이블 ipms_user 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : OppoVO.java
 * @since	 : 2026. 01. 07.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExampleMstVO extends BaseVO {
    private Long conflictSeq;
    private Long participantSeq;
    private Long userMstSeq;
    private String userInfo;
    private Long officeSeq;
    private String litigationCaseNo;
    private String litigationOffice;
    private String litigationAppNo;
    private String litigationCorpStaff;
    private String litigationStaff;
    private ExampleCourtCategoryCode courtCategoryCode;
    private String caseCategoryCode;
    private String state;
}