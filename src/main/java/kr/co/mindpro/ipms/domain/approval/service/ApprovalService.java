package kr.co.mindpro.ipms.domain.approval.service;

import java.util.List;

import kr.co.mindpro.ipms.domain.approval.vo.ApprDocVO;

public interface ApprovalService {

    /** 기안 저장 (임시저장 / 상신) */
    ApprDocVO saveDoc(ApprDocVO vo);

    /** 기안 문서 상세 조회 (결재선 + 대상 포함) */
    ApprDocVO getDoc(String docSeq);

    /** 내 기안함 목록 */
    List<ApprDocVO> getMyDraftList(String drafterSeq, String docStatus, String keyword);

    /** 결재 대기함 */
    List<ApprDocVO> getPendingList(String approverSeq, String keyword);

    /** 결재 참조/열람 */
    List<ApprDocVO> getReferenceList(String userMstSeq, String officeSeq, String keyword);

    /** 부서 수신함 */
    List<ApprDocVO> getDeptInboxList(String userMstSeq, String keyword);

    /** 부서 참조/열람 */
    List<ApprDocVO> getDeptReferenceList(String userMstSeq, String officeSeq, String keyword);

    /** 문서 수신함 */
    List<ApprDocVO> getInboxList(String userMstSeq, String keyword);

    /** 결재 처리 (승인 / 반려) */
    void processApproval(String docSeq, String lineSeq, String action, String comment, String actorSeq);

    /** 상신 취소 (회수) - 기안자 본인만 가능 */
    void withdrawDoc(String docSeq, String drafterSeq);

    /** 기안 삭제 (DRAFT 상태만) */
    void deleteDoc(String docSeq, String drafterSeq);
}
