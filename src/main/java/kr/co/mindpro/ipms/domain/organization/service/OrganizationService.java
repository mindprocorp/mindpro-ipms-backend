package kr.co.mindpro.ipms.domain.organization.service;

import java.util.List;

import kr.co.mindpro.ipms.domain.organization.vo.ApprTemplateVO;
import kr.co.mindpro.ipms.domain.organization.vo.DeptVO;
import kr.co.mindpro.ipms.domain.organization.vo.FormTemplateVO;
import kr.co.mindpro.ipms.domain.organization.vo.OfficeCodeVO;

/**
 * [Service Interface] 조직관리 서비스
 *
 * @author   :
 * @since    : 2026. 03. 20.
 */
public interface OrganizationService {

    // 부서 트리 조회
    List<DeptVO> getDeptTree(String officeSeq);

    // 부서 등록
    void createDept(DeptVO vo);

    // 부서 수정
    void updateDept(DeptVO vo);

    // 부서 삭제
    void deleteDept(String deptSeq);

    // 사무소별 코드 조회
    List<OfficeCodeVO> getOfficeCodeList(String codeClass, String officeSeq);

    // 사무소별 코드 등록 (자동 등록 포함)
    OfficeCodeVO getOrCreateOfficeCode(String officeSeq, String codeClass, String codeName);

    // 사무소별 코드 수정
    void updateOfficeCode(OfficeCodeVO vo);

    // 사무소별 코드 삭제
    void deleteOfficeCode(String officeCodeSeq);

    // 결재선 템플릿 목록 조회
    List<ApprTemplateVO> getApprTemplateList(String officeSeq, String templateName);

    // 결재선 템플릿 상세 조회 (단계 포함)
    ApprTemplateVO getApprTemplateDetail(String apprTemplateSeq);

    // 결재선 템플릿 등록/수정 (단계 포함)
    void saveApprTemplate(ApprTemplateVO vo);

    // 결재선 템플릿 삭제
    void deleteApprTemplate(String apprTemplateSeq);

    // 서식 템플릿 목록 조회 (hasWriteAuth/hasReadAuth 포함)
    List<FormTemplateVO> getFormTemplateList(String officeSeq, String categoryCode, String templateName, String userMstSeq);

    // 서식 템플릿 상세 조회 (targets 포함)
    FormTemplateVO getFormTemplateDetail(String formTemplateSeq);

    // 서식 템플릿 등록/수정 (targets 포함)
    void saveFormTemplate(FormTemplateVO vo);

    // 서식 템플릿 삭제
    void deleteFormTemplate(String formTemplateSeq);

    // 사무소 초기 데이터 세팅 (시드 코드 복사 + 루트 부서 생성)
    void initializeDefaultCodes(String officeSeq, String officeName);
}
