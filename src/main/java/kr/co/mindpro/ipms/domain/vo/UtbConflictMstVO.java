package kr.co.mindpro.ipms.domain.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class UtbConflictMstVO extends BaseVO {
    private String conflictSeq;

    private String participantSeq;

    private String userMstSeq;

    private String userInfo;

    private String officeSeq;

    private String litigationCaseNo;

    private String litigationOffice;

    private String litigationAppNo;

    private String litigationCorpStaff;

    private String litigationStaff;

    private String courtCategoryCode;

    private String caseCategoryCode;

    private String state;
}