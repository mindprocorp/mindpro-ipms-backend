package kr.co.mindpro.ipms.domain.ids.service.impl;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import kr.co.mindpro.ipms.domain.ids.controller.IdsController;
import kr.co.mindpro.ipms.domain.ids.dto.request.IdsRequest;
import kr.co.mindpro.ipms.domain.ids.repository.db1.IdsMapper;
import kr.co.mindpro.ipms.domain.ids.service.IdsService;
import kr.co.mindpro.ipms.domain.ids.vo.IdsVO;
import kr.co.mindpro.ipms.domain.participant.service.ParticipantService;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;
import static kr.co.mindpro.ipms.common.util.DataConvertUtil.parseToOffsetDateTime;

/**
 * @author : seokho
 * @fileName : IdsServiceImpl.java
 * @since : 2026. 3. 12.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IdsServiceImpl implements IdsService {

    private final ParticipantService participantService;
    private final DueDateService dueDateService;

    private final IdsMapper idsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveIds(IdsRequest.SaveIdsRequest request) {
        String submitMng = request.idsSubmitMng();

        if (submitMng == null || !submitMng.matches("^USERIF\\d{11}$")) {
            throw new RuntimeException("제출담당자 값이 유효하지 않습니다.");
        }

        int result = 0;

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        String appSeq = request.appSeq();
        String idsSeq = request.idsSeq();

        int duplicateSeq = 0;

        if (idsSeq != null && !idsSeq.isBlank()) {
            duplicateSeq = idsMapper.getDuplicateIdsSeqCnt(officeSeq, appSeq, idsSeq);
        }

        IdsVO vo = IdsVO.builder()
                .officeSeq(officeSeq)
                .appSeq(appSeq)
                .idsSeq(idsSeq)
                .occurCountryCode(request.occurCountryCode())
                .occurCountryName(request.occurCountryName())
                .occurNo(request.occurNo())
                .familyNoEn(request.familyNoEn())
                .isIdsSubmitted(request.isIdsSubmitted())
                .occurDate(request.occurDate())
                .idsPubDate(request.idsPubDate())
                .idsReceiptDate(request.idsReceiptDate())
                .idsSendDate(request.idsSendDate())
                .idsDeadline(request.idsDeadline())
                .idsSubmitDate(request.idsSubmitDate())
                .idsSubmitMng(request.idsSubmitMng())
                .idsSubmitMngNm(request.idsSubmitMngNm())
                .note(request.note())
                .build();

        if (duplicateSeq > 0) {
            vo.setIdsSeq(idsSeq);
            vo.setUpdateUser(userSeq);

            result = idsMapper.updateIds(vo);

            if (result <= 0) {
                log.error(">>> [ERROR] IDS tbl 정보 업데이트 실패! (Return: 0)");
                throw new RuntimeException("IDS 정보 업데이트에 실패했습니다.");
            }
        } else {
            vo.setCreateUser(userSeq);

            result = idsMapper.insertIds(vo);

            if (result <= 0) {
                log.error(">>> [ERROR] IDS tbl 정보 저장 실패! (Return: 0)");
                throw new RuntimeException("비용 마스터 정보 저장에 실패했습니다.");
            }
        }

        List<ParticipantVO> participants = getParticipantList(vo);

        if (!participants.isEmpty()) {
            participantService.saveAllParticipants(participants);
        }

        // [공통] 기일 정보 저장
        List<DueDateVO> dueDates = getDueDateList(vo);

        if (!dueDates.isEmpty()) {
            dueDateService.saveAllDueDates(dueDates);
        }

    }

    @Override
    public BaseSearchResponse<IdsVO> getIdsList(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        List<IdsVO> idsVOList = idsMapper.getIdsList(officeSeq, appSeq);


        for (IdsVO idsVO : idsVOList) {
            idsVO.setOccurDate(formatMinusHoursString8(idsVO.getOccurDate()));
            idsVO.setIdsPubDate(formatMinusHoursString8(idsVO.getIdsPubDate()));
            idsVO.setIdsReceiptDate(formatMinusHoursString8(idsVO.getIdsReceiptDate()));
            idsVO.setIdsSendDate(formatMinusHoursString8(idsVO.getIdsSendDate()));
            idsVO.setIdsDeadline(formatMinusHoursString8(idsVO.getIdsDeadline()));
            idsVO.setIdsSubmitDate(formatMinusHoursString8(idsVO.getIdsSubmitDate()));
        }

        return BaseSearchResponse.of(idsVOList, 1, 99);
    }

    @Override
    public IdsVO getIdsDetail(String appSeq, String idsSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        IdsVO vo = idsMapper.getIdsDetail(officeSeq, appSeq, idsSeq);

        if (vo != null) {
            vo.setOccurDate(formatMinusHoursString8(vo.getOccurDate()));
            vo.setIdsPubDate(formatMinusHoursString8(vo.getIdsPubDate()));
            vo.setIdsReceiptDate(formatMinusHoursString8(vo.getIdsReceiptDate()));
            vo.setIdsSendDate(formatMinusHoursString8(vo.getIdsSendDate()));
            vo.setIdsDeadline(formatMinusHoursString8(vo.getIdsDeadline()));
            vo.setIdsSubmitDate(formatMinusHoursString8(vo.getIdsSubmitDate()));
        } else {
            throw new RuntimeException("해당하는 IDS 데이터가 유효하지 않습니다.");
        }

        return vo;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteByIdsSeq(String appSeq, String idsSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result;

        result = idsMapper.softDeleteByIdsSeq(officeSeq, appSeq, idsSeq, userSeq);

        if (result <= 0) {
            throw new RuntimeException("IDS 논리적 삭제 실패");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteByIdsSeqList(String appSeq, List<String> idsSeqList) {
        if (idsSeqList == null || idsSeqList.isEmpty()) {
            throw new RuntimeException("IDS sequence list is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = idsMapper.softDeleteByIdsSeqList(officeSeq, userSeq, appSeq, idsSeqList);

        if (result != idsSeqList.size()) {
            throw new RuntimeException("IDS multi delete is failed");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteByIdsSeq(String appSeq, String idsSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = idsMapper.hardDeleteByIdsSeq(officeSeq, appSeq, idsSeq);

        if (result <= 0) {
            throw new RuntimeException("IDS 물리적 삭제 실패");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteByIdsSeqList(String appSeq, List<String> idsSeqList) {
        if (idsSeqList == null || idsSeqList.isEmpty()) {
            throw new RuntimeException("IDS sequence list is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = idsMapper.hardDeleteByIdsSeqList(officeSeq, appSeq, idsSeqList);

        if (result != idsSeqList.size()) {
            throw new RuntimeException("IDS multi hard delete is failed");
        }
    }

    public List<ParticipantVO> getParticipantList(IdsVO vo) {

        String officeSeq = vo.getOfficeSeq();

        List<ParticipantVO> participantVOList = new ArrayList<>();

        addParticipantIfPresent(participantVOList, "idsSubmitMng", vo.getIdsSubmitMng(), vo.getIdsSeq(), officeSeq);

        return participantVOList;
    }

    public void addParticipantIfPresent(List<ParticipantVO> list, String code, String userSeq, String appSeq, String officeSeq) {
        if (userSeq != null && !userSeq.isEmpty()) {
            list.add(ParticipantVO.builder()
                    .participantCode(code)
                    .userInfoSeq(userSeq)
                    .officeSeq(officeSeq)
                    .tblSeq(appSeq)
                    .mainYn("Y")
                    .build());
        }
    }

    public List<DueDateVO> getDueDateList(IdsVO vo) {

        String officeSeq = vo.getOfficeSeq();

        List<DueDateVO> dueDateVOList = new ArrayList<>();

        addDueDateIfPresent(dueDateVOList, "occurDate", vo.getOccurDate(), vo.getIdsSeq(), officeSeq);
        addDueDateIfPresent(dueDateVOList, "idsPubDate", vo.getIdsPubDate(), vo.getIdsSeq(), officeSeq);
        addDueDateIfPresent(dueDateVOList, "idsReceiptDate", vo.getIdsReceiptDate(), vo.getIdsSeq(), officeSeq);
        addDueDateIfPresent(dueDateVOList, "idsSendDate", vo.getIdsSendDate(), vo.getIdsSeq(), officeSeq);
        addDueDateIfPresent(dueDateVOList, "idsDeadline", vo.getIdsDeadline(), vo.getIdsSeq(), officeSeq);
        addDueDateIfPresent(dueDateVOList, "idsSubmitDate", vo.getIdsSubmitDate(), vo.getIdsSeq(), officeSeq);

        return dueDateVOList;
    }

    public void addDueDateIfPresent(List<DueDateVO> list, String code, String dateStr, String appSeq, String officeSeq) {
        // 날짜 문자열이 존재할 때만 파싱해서 리스트에 추가
        if (dateStr != null && !dateStr.isEmpty()) {
            list.add(DueDateVO.builder()
                    .duedateCategoryCode(code)
                    .duedateDate(parseToOffsetDateTime(dateStr))
                    .officeSeq(officeSeq)
                    .tblSeq(appSeq)
                    .build());
        }
    }
}
