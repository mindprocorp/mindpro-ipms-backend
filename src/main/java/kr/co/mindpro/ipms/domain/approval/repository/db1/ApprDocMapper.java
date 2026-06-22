package kr.co.mindpro.ipms.domain.approval.repository.db1;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.approval.vo.ApprDocLineVO;
import kr.co.mindpro.ipms.domain.approval.vo.ApprDocTargetVO;
import kr.co.mindpro.ipms.domain.approval.vo.ApprDocVO;

@Mapper
public interface ApprDocMapper {

    // ── 기안 문서 ─────────────────────────────────────────
    int insertDoc(ApprDocVO vo);
    int updateDoc(ApprDocVO vo);
    int deleteDoc(@Param("docSeq") String docSeq);
    Optional<ApprDocVO> selectDocBySeq(@Param("docSeq") String docSeq);

    // ── 목록 조회 ─────────────────────────────────────────
    /** 내 기안함 */
    List<ApprDocVO> selectMyDraftList(@Param("drafterSeq") String drafterSeq,
                                      @Param("docStatus") String docStatus,
                                      @Param("keyword") String keyword);

    /** 결재 대기함 (내 차례인 문서) */
    List<ApprDocVO> selectPendingList(@Param("approverSeq") String approverSeq,
                                      @Param("keyword") String keyword);

    /** 결재 참조/열람 (개인 공유 대상 + 전체 공유 포함) */
    List<ApprDocVO> selectReferenceList(@Param("userMstSeq") String userMstSeq,
                                        @Param("officeSeq") String officeSeq,
                                        @Param("keyword") String keyword);

    /** 부서 수신함 */
    List<ApprDocVO> selectDeptInboxList(@Param("userMstSeq") String userMstSeq,
                                        @Param("keyword") String keyword);

    /** 부서 참조/열람 (부서 공유 대상 + 전체 공유 포함) */
    List<ApprDocVO> selectDeptReferenceList(@Param("userMstSeq") String userMstSeq,
                                            @Param("officeSeq") String officeSeq,
                                            @Param("keyword") String keyword);

    /** 문서 수신함 (개인 수신 대상) */
    List<ApprDocVO> selectInboxList(@Param("userMstSeq") String userMstSeq,
                                    @Param("keyword") String keyword);

    // ── 결재선 ────────────────────────────────────────────
    int insertDocLine(ApprDocLineVO vo);
    int deleteDocLines(@Param("docSeq") String docSeq);
    List<ApprDocLineVO> selectDocLines(@Param("docSeq") String docSeq);
    int updateDocLine(ApprDocLineVO vo);

    // ── 수신/공유 대상 ────────────────────────────────────
    int insertDocTarget(ApprDocTargetVO vo);
    int deleteDocTargets(@Param("docSeq") String docSeq);
    List<ApprDocTargetVO> selectDocTargets(@Param("docSeq") String docSeq);
}
