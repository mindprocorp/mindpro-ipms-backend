package kr.co.mindpro.ipms.domain.approval.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class ApprDocVO extends BaseVO {
    private String docSeq;
    private String officeSeq;
    private String formTemplateSeq;
    private String formTemplateName;  // JOIN
    private String docNo;
    private String docTitle;
    private String docContent;        // JSON (서식 필드값)
    /** DRAFT(임시저장) | PENDING(결재중) | APPROVED(승인) | REJECTED(반려) | WITHDRAWN(회수) */
    private String docStatus;
    private String drafterSeq;
    private String drafterName;       // JOIN
    private String draftDeptSeq;
    private String draftDeptName;     // JOIN
    private OffsetDateTime submitAt;
    private OffsetDateTime completeAt;

    private String templateData;      // JOIN (서식 구조 JSON, 단건 조회 시)

    private List<ApprDocLineVO> lines;
    private List<ApprDocTargetVO> targets;
}
