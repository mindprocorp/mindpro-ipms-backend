package kr.co.mindpro.ipms.domain.memo.service.impl;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.memo.dto.request.MemoRequest;
import kr.co.mindpro.ipms.domain.memo.dto.response.MemoResponse;
import kr.co.mindpro.ipms.domain.memo.repository.db1.MemoMapper;
import kr.co.mindpro.ipms.domain.memo.service.MemoService;
import kr.co.mindpro.ipms.domain.memo.vo.MemoVO;
import kr.co.mindpro.ipms.domain.paper.repository.db1.PaperMapper;
import kr.co.mindpro.ipms.domain.paper.service.PaperService;
import kr.co.mindpro.ipms.domain.paper.vo.PaperRequestVO;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 청구서 비즈니스 로직 구현체
 * 
 * @author   : min
 * @fileName : DuedateServiceImpl.java
 * @since    : 2026. 01. 07.
 */
@Service
@RequiredArgsConstructor
public class MemoServiceImpl implements MemoService {
    private final MemoMapper memoMapper;

    private final PaperService paperService;
    private final PaperMapper paperMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllMemos(List<MemoVO> list) {
        if (list == null || list.isEmpty()) return;

//        // 1. 기준 정보 추출
//        MemoVO firstVo = list.get(0);
//        String tblSeq = firstVo.getTblSeq();
//        String officeSeq = firstVo.getOfficeSeq();
//        String userId = SecurityUtil.getUserInfoSeq();
//
//        // 2. DueDate 방식대로 삭제 먼저 실행
//        memoMapper.deleteMstByWork(tblSeq, officeSeq);
//        int deletedRows = memoMapper.deleteMappByWork(tblSeq, officeSeq);
//
//        // 3. 루프 돌며 인서트
//        if (deletedRows >= 0) {
//            list.forEach(vo -> {
//                vo.setTblSeq(tblSeq);
//                vo.setOfficeSeq(officeSeq);
//                vo.setCreateUser(userId);
//                vo.setDelYn("N");
//                memoMapper.insertMemo(vo);
//            });
//        }
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<MemoResponse.MemoDetail> getMemoListByWork(String tblSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        List<MemoResponse.MemoDetail> list =  memoMapper.findAllByWork(tblSeq, officeSeq);

        List<MemoResponse.MemoDetail> listWithFiles = list.stream().map(memo -> {
            List<PaperResponseVO> fileList = paperMapper.findAllByWork(memo.memoSeq(), officeSeq);
            return memo.toBuilder()
                    .fileInfo(CommonRecordResponse.FileInfo.from(fileList))
                    .build();
        }).collect(java.util.stream.Collectors.toList());

        return  BaseSearchResponse.of(listWithFiles, 1, 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemoResponse.MemoDetail registerMemo(MemoRequest.MemoDetail memo, List<MultipartFile> files) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        String memoSeq = memo.memoSeq();

        int result = 0;

        MemoVO memoVO =  MemoVO.builder()
                // 매핑 정보 (Mapp 테이블용)
                .officeSeq(officeSeq)
                .tblSeq(memo.tblSeq())
                .tblCode("MEMO")
                // 메모 본체 정보 (Mst 테이블용)
                .mustReadYn(memo.mustReadYn())
                .memoTitle(memo.memoTitle())
                .memoRegDate(memo.memoRegDate())

                .customerName(memo.customerName())
                .note(memo.note())

                // 공통 관리 필드
                .delYn("N")
                .build();

        if (StringUtils.hasText(memoSeq)) {
            memoVO.setMemoSeq(memoSeq);

            result = memoMapper.getDuplicateMemo(memoVO);
        }

        if (result > 0) {
            memoVO.setUpdateUser(userSeq);

            result = memoMapper.updateMemo(memoVO);

            if (result <= 0) {
                throw new RuntimeException("Failed to update memo");
            }

            // 수정 모드: 프론트에서 삭제 요청한 첨부파일 논리 삭제
            if (memo.deletedFileSeqList() != null && !memo.deletedFileSeqList().isEmpty()) {
                paperService.softDeleteFilesByFileSeqList(memoVO.getMemoSeq(), memo.deletedFileSeqList());
            }
        } else {
            memoVO.setCreateUser(userSeq);

            result = memoMapper.insertMemo(memoVO);

            if (result <= 0) {
                throw new RuntimeException("Failed to insert memo");
            }

        }

        String tempSeq =  memoVO.getMemoSeq();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                fileUpload(memoVO.getMemoSeq(), officeSeq, userSeq, "393", file, "memoFile");
            }
        }

        return getMemoDetail(tempSeq);
    }

    @Override
    public MemoResponse.MemoDetail getMemoDetail(String memoSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        MemoResponse.MemoDetail memo = memoMapper.findById(memoSeq, officeSeq);

        if (memo == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        List<PaperResponseVO> fileList = paperMapper.findAllByWork(memo.memoSeq(), officeSeq);

        return memo.toBuilder()
                .fileInfo(
                        CommonRecordResponse.FileInfo.from(
                            fileList
                        ))
                .build();
    }

    @Override
    public void softDeleteMemo(String tblSeq, String memoSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = 0;

        result = memoMapper.softDeleteMstByWork(tblSeq, officeSeq, userSeq, memoSeq);

        if (result <= 0) {
            throw new RuntimeException("Failed to delete memo");
        }

        result = memoMapper.softDeleteMappByWork(tblSeq, officeSeq, userSeq, memoSeq);

        if (result <= 0) {
            throw new RuntimeException("Failed to delete memo mapper");
        }
    }

    @Override
    public void softDeleteMemoByList(String tblSeq, List<String> memoSeqList) {
        if (memoSeqList == null || memoSeqList.isEmpty()) {
            throw new RuntimeException("maintenanceFeeSeq is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = 0;

        result = memoMapper.softDeleteMstByWorkList(tblSeq, officeSeq, userSeq, memoSeqList);

        if (result != memoSeqList.size()) {
            throw new RuntimeException("Failed to soft delete memo list");
        }

        // 현재 테이블 구조에 따라 메핑 개수도 메모마스터와 같은 상황입니다.
        result = memoMapper.softDeleteMappByWorkList(tblSeq, officeSeq, userSeq, memoSeqList);

        if (result != memoSeqList.size()) {
            throw new RuntimeException("Failed to soft delete memo list mapper");
        }
    }

    @Override
    public void hardDeleteMemo(String tblSeq, String memoSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = 0;

        result = memoMapper.hardDeleteMstByWork(tblSeq, officeSeq, memoSeq);

        if (result <= 0) {
            throw new RuntimeException("Failed to hard delete memo");
        }

        result = memoMapper.hardDeleteMappByWork(tblSeq, officeSeq, memoSeq);

        if (result <= 0) {
            throw new RuntimeException("Failed to hard delete memo mapper");
        }
    }

    @Override
    public void hardDeleteMemoByList(String tblSeq, List<String> memoSeqList) {
        if (memoSeqList == null || memoSeqList.isEmpty()) {
            throw new RuntimeException("maintenanceFeeSeq is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = 0;

        result = memoMapper.hardDeleteMstByWorkList(tblSeq, officeSeq, memoSeqList);

        if (result != memoSeqList.size()) {
            throw new RuntimeException("Failed to soft delete memo list");
        }

        // 현재 테이블 구조에 따라 메핑 개수도 메모마스터와 같은 상황입니다.
        result = memoMapper.hardDeleteMappByWorkList(tblSeq, officeSeq, memoSeqList);

        if (result != memoSeqList.size()) {
            throw new RuntimeException("Failed to soft delete memo list mapper");
        }
    }

    private void fileUpload(String tblSeq, String officeSeq, String user, String docSeq, MultipartFile file, String category) {
        PaperRequestVO paperVO = PaperRequestVO.builder()
                .officeSeq(officeSeq)
                .tblSeq(tblSeq)
                .file(file)
                .docSeq(docSeq)
                .fileCategoryCode(category)
                .createUser(user)
                .build();

        paperService.saveFileMapping(paperVO);
    }
}