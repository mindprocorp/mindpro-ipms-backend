package kr.co.mindpro.ipms.domain.approval.service.impl;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mindpro.ipms.domain.approval.repository.db1.ApprDocMapper;
import kr.co.mindpro.ipms.domain.approval.service.ApprovalService;
import kr.co.mindpro.ipms.domain.approval.vo.ApprDocLineVO;
import kr.co.mindpro.ipms.domain.approval.vo.ApprDocTargetVO;
import kr.co.mindpro.ipms.domain.approval.vo.ApprDocVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprDocMapper apprDocMapper;

    @Override
    @Transactional
    public ApprDocVO saveDoc(ApprDocVO vo) {
        boolean isNew = vo.getDocSeq() == null || vo.getDocSeq().isBlank();
        if (isNew) {
            // SELF 타입 결재자 → 기안자로 치환
            if (vo.getLines() != null) {
                for (ApprDocLineVO line : vo.getLines()) {
                    if ("SELF".equals(line.getApproverType())) {
                        line.setApproverSeq(vo.getDrafterSeq());
                        line.setApproverName(null); // DB JOIN으로 표시
                    }
                }
            }
            if ("PENDING".equals(vo.getDocStatus())) {
                vo.setSubmitAt(OffsetDateTime.now());
                // 문서번호 자동 생성: 서식코드-연도-순번 형태
                if (vo.getDocNo() == null || vo.getDocNo().isBlank()) {
                    String year = String.valueOf(java.time.LocalDate.now().getYear());
                    String timeSeq = String.valueOf(System.currentTimeMillis()).substring(7); // 6자리
                    vo.setDocNo("DOC-" + year + "-" + timeSeq);
                }
            }
            apprDocMapper.insertDoc(vo);
        } else {
            // 상신 시점 기록
            if ("PENDING".equals(vo.getDocStatus())) {
                ApprDocVO existing = apprDocMapper.selectDocBySeq(vo.getDocSeq())
                        .orElseThrow(() -> new IllegalArgumentException("문서를 찾을 수 없습니다."));
                if (!"PENDING".equals(existing.getDocStatus())) {
                    vo.setSubmitAt(OffsetDateTime.now());
                }
            }
            apprDocMapper.updateDoc(vo);
            apprDocMapper.deleteDocLines(vo.getDocSeq());
            apprDocMapper.deleteDocTargets(vo.getDocSeq());

            if (vo.getLines() != null) {
                for (ApprDocLineVO line : vo.getLines()) {
                    if ("SELF".equals(line.getApproverType())) {
                        line.setApproverSeq(vo.getDrafterSeq());
                    }
                }
            }
        }

        if (vo.getLines() != null) {
            for (ApprDocLineVO line : vo.getLines()) {
                line.setDocSeq(vo.getDocSeq());
                line.setCreateUser(vo.getCreateUser());
                line.setUpdateUser(vo.getUpdateUser());
                apprDocMapper.insertDocLine(line);
            }
        }

        if (vo.getTargets() != null) {
            for (ApprDocTargetVO target : vo.getTargets()) {
                target.setDocSeq(vo.getDocSeq());
                target.setCreateUser(vo.getCreateUser());
                target.setUpdateUser(vo.getUpdateUser());
                apprDocMapper.insertDocTarget(target);
            }
        }

        return apprDocMapper.selectDocBySeq(vo.getDocSeq())
                .orElseThrow(() -> new IllegalStateException("저장된 문서를 조회할 수 없습니다."));
    }

    @Override
    public ApprDocVO getDoc(String docSeq) {
        ApprDocVO doc = apprDocMapper.selectDocBySeq(docSeq)
                .orElseThrow(() -> new IllegalArgumentException("문서를 찾을 수 없습니다."));
        doc.setLines(apprDocMapper.selectDocLines(docSeq));
        doc.setTargets(apprDocMapper.selectDocTargets(docSeq));
        return doc;
    }

    @Override
    public List<ApprDocVO> getMyDraftList(String drafterSeq, String docStatus, String keyword) {
        return apprDocMapper.selectMyDraftList(drafterSeq, docStatus, keyword);
    }

    @Override
    public List<ApprDocVO> getPendingList(String approverSeq, String keyword) {
        return apprDocMapper.selectPendingList(approverSeq, keyword);
    }

    @Override
    public List<ApprDocVO> getReferenceList(String userMstSeq, String officeSeq, String keyword) {
        return apprDocMapper.selectReferenceList(userMstSeq, officeSeq, keyword);
    }

    @Override
    public List<ApprDocVO> getDeptInboxList(String userMstSeq, String keyword) {
        return apprDocMapper.selectDeptInboxList(userMstSeq, keyword);
    }

    @Override
    public List<ApprDocVO> getDeptReferenceList(String userMstSeq, String officeSeq, String keyword) {
        return apprDocMapper.selectDeptReferenceList(userMstSeq, officeSeq, keyword);
    }

    @Override
    public List<ApprDocVO> getInboxList(String userMstSeq, String keyword) {
        return apprDocMapper.selectInboxList(userMstSeq, keyword);
    }

    @Override
    @Transactional
    public void processApproval(String docSeq, String lineSeq, String action, String comment, String actorSeq) {
        ApprDocVO doc = apprDocMapper.selectDocBySeq(docSeq)
                .orElseThrow(() -> new IllegalArgumentException("문서를 찾을 수 없습니다."));

        if (!"PENDING".equals(doc.getDocStatus())) {
            throw new IllegalStateException("결재 진행 중인 문서가 아닙니다.");
        }

        ApprDocLineVO line = new ApprDocLineVO();
        line.setLineSeq(lineSeq);
        line.setLineStatus(action);  // APPROVED or REJECTED
        line.setActionComment(comment);
        line.setUpdateUser(actorSeq);
        apprDocMapper.updateDocLine(line);

        if ("REJECTED".equals(action)) {
            // 반려 → 문서 상태 REJECTED
            ApprDocVO update = new ApprDocVO();
            update.setDocSeq(docSeq);
            update.setDocStatus("REJECTED");
            update.setCompleteAt(OffsetDateTime.now());
            update.setUpdateUser(actorSeq);
            apprDocMapper.updateDoc(update);
        } else {
            // 승인 → 모든 단계가 APPROVED인지 확인
            List<ApprDocLineVO> lines = apprDocMapper.selectDocLines(docSeq);
            boolean allApproved = lines.stream().allMatch(l -> "APPROVED".equals(l.getLineStatus()));
            if (allApproved) {
                ApprDocVO update = new ApprDocVO();
                update.setDocSeq(docSeq);
                update.setDocStatus("APPROVED");
                update.setCompleteAt(OffsetDateTime.now());
                update.setUpdateUser(actorSeq);
                apprDocMapper.updateDoc(update);
            }
        }
    }

    @Override
    @Transactional
    public void withdrawDoc(String docSeq, String drafterSeq) {
        ApprDocVO doc = apprDocMapper.selectDocBySeq(docSeq)
                .orElseThrow(() -> new IllegalArgumentException("문서를 찾을 수 없습니다."));
        if (!drafterSeq.equals(doc.getDrafterSeq())) {
            throw new IllegalStateException("기안자 본인만 회수할 수 있습니다.");
        }
        if (!"PENDING".equals(doc.getDocStatus())) {
            throw new IllegalStateException("결재 진행 중인 문서만 회수할 수 있습니다.");
        }
        ApprDocVO update = new ApprDocVO();
        update.setDocSeq(docSeq);
        update.setDocStatus("WITHDRAWN");
        update.setUpdateUser(drafterSeq);
        apprDocMapper.updateDoc(update);

        // 결재선 전체 PENDING 상태 초기화
        apprDocMapper.deleteDocLines(docSeq);
    }

    @Override
    @Transactional
    public void deleteDoc(String docSeq, String drafterSeq) {
        ApprDocVO doc = apprDocMapper.selectDocBySeq(docSeq)
                .orElseThrow(() -> new IllegalArgumentException("문서를 찾을 수 없습니다."));
        if (!drafterSeq.equals(doc.getDrafterSeq())) {
            throw new IllegalStateException("기안자 본인만 삭제할 수 있습니다.");
        }
        if (!"DRAFT".equals(doc.getDocStatus()) && !"WITHDRAWN".equals(doc.getDocStatus())) {
            throw new IllegalStateException("임시저장 또는 회수된 문서만 삭제할 수 있습니다.");
        }
        apprDocMapper.deleteDocLines(docSeq);
        apprDocMapper.deleteDocTargets(docSeq);
        apprDocMapper.deleteDoc(docSeq);
    }
}
