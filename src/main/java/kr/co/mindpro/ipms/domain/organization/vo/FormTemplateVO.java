package kr.co.mindpro.ipms.domain.organization.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class FormTemplateVO extends BaseVO {
    private String formTemplateSeq;
    private String officeSeq;
    private String categoryCode;
    private String templateName;
    private String useYn;
    private String docModifyYn;
    private String templateData;
    private String sortOrd;

    // 1단계: 기본 설정
    private String docNumYn;
    private String docNumFormat;
    private String footerYn;
    private String footerContent;
    private String externalYn;
    private String redirectUrl;

    // 3단계: 결재선 설정
    private String apprTemplateSeq;
    private String apprRequiredYn;
    private String apprAdminSetYn;
    private String apprDefaultLineYn;
    private String apprCondLineYn;
    private String apprChangeAllowYn;
    private String apprSkipUpperYn;
    private String fullyApproveYn;

    // 4단계: 수신·공유 설정
    private String receiveYn;
    private String receiveTiming;
    private String receiveChangeYn;
    private String shareScope;
    private String shareTiming;
    private String shareChangeYn;

    // 공유그룹 + 권한 대상 목록
    private List<FormTemplateTargetVO> targets;

    // 권한 체크 (목록 조회 시 현재 사용자 기준으로 계산)
    private Boolean hasWriteAuth;
    private Boolean hasReadAuth;
}
