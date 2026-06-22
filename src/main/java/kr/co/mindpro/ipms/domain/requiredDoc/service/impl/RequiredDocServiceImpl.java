package kr.co.mindpro.ipms.domain.requiredDoc.service.impl;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.ai.service.RagService;
import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import kr.co.mindpro.ipms.domain.requiredDoc.dto.request.RequiredDocRequest;
import kr.co.mindpro.ipms.domain.requiredDoc.dto.response.RequiredDocResponse;
import kr.co.mindpro.ipms.domain.requiredDoc.repository.db1.RequiredDocMapper;
import kr.co.mindpro.ipms.domain.requiredDoc.service.RequiredDocService;
import kr.co.mindpro.ipms.domain.requiredDoc.vo.RequiredDocVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;
import static kr.co.mindpro.ipms.common.util.DataConvertUtil.parseToOffsetDateTime;

/**
 * @author : seokho
 * @fileName : RequiredDocServiceImpl.java
 * @since : 2026. 4. 1.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequiredDocServiceImpl implements RequiredDocService {

    private final DueDateService dueDateService;
    private final RagService ragService;

    private final RequiredDocMapper requiredDocMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRequiredDoc(RequiredDocRequest.createRequiredDocRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        String requiredDocSeq = request.requiredDocSeq();

        int result = 0;

        RequiredDocVO requiredDocVO = RequiredDocVO.builder()
                .officeSeq(officeSeq)
                .appSeq(request.appSeq())
                .requiredDocSeq(requiredDocSeq)
                .requiredDocName(request.requiredDocName())
                .submitDeadline(request.submitDeadline())
                .signReqDate(request.signReqDate())
                .receiptDate(request.receiptDate())
                .sendDate(request.sendDate())
                .submitDate(request.submitDate())
                .build();

        if (StringUtils.hasText(requiredDocSeq)) {
            result = requiredDocMapper.getDuplicateRequiredDoc(requiredDocVO);
        }

        if (result > 0) {
            requiredDocVO.setUpdateUser(userSeq);

            result = requiredDocMapper.updateRequiredDoc(requiredDocVO);

            if (result <= 0) {
                throw new RuntimeException(">>> requiredDoc update failed.");
            }
        } else {
            requiredDocVO.setCreateUser(userSeq);

            result = requiredDocMapper.insertRequiredDoc(requiredDocVO);

            if (result <= 0) {
                throw new RuntimeException(">>> requiredDoc insert failed.");
            }
        }

        List<DueDateVO> dueDates = getDueDateList(requiredDocVO, officeSeq);

        if (!dueDates.isEmpty()) {
            dueDateService.saveAllDueDates(dueDates);
        }

        // AI Vector Sync
        ragService.syncVectorData(officeSeq, "REQUIRED_DOC", requiredDocVO.getRequiredDocSeq(),"필요서류", this.getRequiredDocDetail(requiredDocVO.getAppSeq(), requiredDocVO.getRequiredDocSeq()));
    }

    @Override
    public RequiredDocResponse.RequiredDocDetailResponse getRequiredDocDetail(String appSeq, String requiredDocSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        RequiredDocVO reqDocVO = requiredDocMapper.getRequiredDocDetail(officeSeq, requiredDocSeq, appSeq, userSeq);

        return RequiredDocResponse.RequiredDocDetailResponse.builder()
                .requiredDocSeq(reqDocVO.getRequiredDocSeq())
                .appSeq(reqDocVO.getAppSeq())
                .requiredDocName(reqDocVO.getRequiredDocName())
                .submitDeadline(formatMinusHoursString8(reqDocVO.getSubmitDeadline()))
                .signReqDate(formatMinusHoursString8(reqDocVO.getSignReqDate()))
                .receiptDate(formatMinusHoursString8(reqDocVO.getReceiptDate()))
                .sendDate(formatMinusHoursString8(reqDocVO.getSendDate()))
                .submitDate(formatMinusHoursString8(reqDocVO.getSubmitDate()))
                .build();
    }

    @Override
    public BaseSearchResponse<RequiredDocResponse.RequiredDocListResponse> getRequiredDocListByAppSeq(BaseSearchRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int totalCnt = requiredDocMapper.getRequiredDocListCnt(officeSeq, request.getTblSeq());


        if (totalCnt > 0) {
            List<RequiredDocVO> listVO = requiredDocMapper.getRequiredDocListByAppSeq(request.getTblSeq(), officeSeq);

            List<RequiredDocResponse.RequiredDocListResponse> res = listVO.stream()
                    .map(reqDocVO -> RequiredDocResponse.RequiredDocListResponse.builder()
                            .requiredDocSeq(reqDocVO.getRequiredDocSeq())
                            .requiredDocName(reqDocVO.getRequiredDocName())
                            .submitDeadline(formatMinusHoursString8(reqDocVO.getSubmitDeadline()))
                            .signReqDate(formatMinusHoursString8(reqDocVO.getSignReqDate()))
                            .receiptDate(formatMinusHoursString8(reqDocVO.getReceiptDate()))
                            .sendDate(formatMinusHoursString8(reqDocVO.getSendDate()))
                            .submitDate(formatMinusHoursString8(reqDocVO.getSubmitDate()))
                            .build()
                    ).toList();

            return BaseSearchResponse.of(res, totalCnt, request.getPage(), request.getPageSize());
        }

        return new BaseSearchResponse<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteRequiredDoc(String appSeq, String requiredDocSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = 0;

        result = requiredDocMapper.softDeleteRequiredDoc(appSeq, requiredDocSeq, officeSeq, userSeq);

        if (result <= 0) {
            throw new RuntimeException(">>> requiredDoc delete failed.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteRequiredDocByList(String appSeq, List<String> requiredDocSeqList) {
        if (requiredDocSeqList == null || requiredDocSeqList.isEmpty()) {
            throw new RuntimeException("구비서류 시퀀스 목록이 비어 있습니다.");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = requiredDocMapper.softDeleteRequiredDocList(appSeq, requiredDocSeqList, officeSeq, userSeq);

        if (result != requiredDocSeqList.size()) {
            throw new RuntimeException(">>> requiredDoc list soft delete failed.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteRequiredDoc(String appSeq, String requiredDocSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = requiredDocMapper.hardDeleteRequiredDoc(appSeq, requiredDocSeq, officeSeq);

        if (result <= 0) {
            throw new RuntimeException(">>> requiredDoc hard delete failed.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteRequiredDocByList(String appSeq, List<String> requiredDocSeqList) {
        if (requiredDocSeqList == null || requiredDocSeqList.isEmpty()) {
            throw new RuntimeException("구비서류 시퀀스 목록이 비어 있습니다.");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = requiredDocMapper.hardDeleteRequiredDocList(appSeq, requiredDocSeqList, officeSeq);

        if (result != requiredDocSeqList.size()) {
            throw new RuntimeException(">>> requiredDoc list hard delete failed.");
        }
    }

    public List<DueDateVO> getDueDateList(RequiredDocVO requiredDocVO, String officeSeq) {

        String reqDocSeq = requiredDocVO.getRequiredDocSeq();

        List<DueDateVO> duedateVOList = new ArrayList<>();

        addDueDateIfPresent(duedateVOList, "submitDeadline", requiredDocVO.getSubmitDeadline(), reqDocSeq, officeSeq);
        addDueDateIfPresent(duedateVOList, "signReqDate", requiredDocVO.getSignReqDate(), reqDocSeq, officeSeq);
        addDueDateIfPresent(duedateVOList, "receiptDate", requiredDocVO.getReceiptDate(), reqDocSeq, officeSeq);
        addDueDateIfPresent(duedateVOList, "sendDate", requiredDocVO.getSendDate(), reqDocSeq, officeSeq);
        addDueDateIfPresent(duedateVOList, "submitDate", requiredDocVO.getSubmitDate(), reqDocSeq, officeSeq);

        return duedateVOList;
    }

    // =================================================================
    // 기일 추가 (Null Safe & 날짜 파싱 통합)
    // =================================================================
    public void addDueDateIfPresent(List<DueDateVO> list, String code, String dateStr, String tblSeq, String officeSeq) {
        // 날짜 문자열이 존재할 때만 파싱해서 리스트에 추가
        list.add(DueDateVO.builder()
                .duedateCategoryCode(code)
                .duedateDate(parseToOffsetDateTime(dateStr))
                .officeSeq(officeSeq)
                .tblSeq(tblSeq)
                .build());
    }
}
