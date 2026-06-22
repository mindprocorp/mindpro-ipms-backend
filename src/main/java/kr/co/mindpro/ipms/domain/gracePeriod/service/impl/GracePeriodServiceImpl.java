package kr.co.mindpro.ipms.domain.gracePeriod.service.impl;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import kr.co.mindpro.ipms.domain.gracePeriod.dto.request.GracePeriodRequest;
import kr.co.mindpro.ipms.domain.gracePeriod.dto.response.GracePeriodResponse;
import kr.co.mindpro.ipms.domain.gracePeriod.repository.db1.GracePeriodMapper;
import kr.co.mindpro.ipms.domain.gracePeriod.service.GracePeriodService;
import kr.co.mindpro.ipms.domain.gracePeriod.vo.GracePeriodVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;
import static kr.co.mindpro.ipms.common.util.DataConvertUtil.parseToOffsetDateTime;

/**
 * @author : seokho
 * @fileName : GracePeriodServiceImpl.java
 * @since : 2026. 2. 3.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GracePeriodServiceImpl implements GracePeriodService {

    private final GracePeriodMapper gracePeriodMapper;
    private final DueDateService dueDateService;

    // 공지예외 정보 생성
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerGracePeriod(GracePeriodRequest.SaveRequest request) {
        int result = 0;

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        String gracePeriodSeq = request.gracePeriodSeq();

        GracePeriodVO vo = GracePeriodVO.builder()
                .appSeq(request.appSeq())
                .officeSeq(officeSeq)
                .gracePeriodContentCode(request.gracePeriodContent().code())
                .note(request.note())
                .build();

        if (gracePeriodSeq != null && !gracePeriodSeq.isEmpty()) {
            vo.setGracePeriodSeq(gracePeriodSeq);

            result = gracePeriodMapper.getDuplicateGracePeriodCnt(vo);
        }

        if (result > 0) {
            vo.setUpdateUser(userSeq);
            result = gracePeriodMapper.updateGracePeriod(vo);

            if (result <= 0) {
                log.error(">>> [ERROR] 공지예외 tbl 정보 업데이트 실패! (Return: 0)");
                throw new RuntimeException("공지예외 정보 업데이트에 실패했습니다.");
            }
        } else {
            vo.setCreateUser(userSeq);

            result = gracePeriodMapper.insertGracePeriod(vo);

            if (result <= 0) {
                log.error(">>> [ERROR] 공지예외 tbl 정보 저장 실패! (Return: 0)");
                throw new RuntimeException("비용 마스터 정보 저장에 실패했습니다.");
            }
        }

        List<DueDateVO> dueDateVOList = new ArrayList<>();

        addDueDateIfPresent(dueDateVOList, "submitDeadLineDate", request.submitDeadLineDate(), vo.getGracePeriodSeq(), officeSeq);
        addDueDateIfPresent(dueDateVOList, "submitClosingDate", request.submitClosingDate(), vo.getGracePeriodSeq(), officeSeq);
        addDueDateIfPresent(dueDateVOList, "gracePeriodDate", request.gracePeriodDate(), vo.getGracePeriodSeq(), officeSeq);

        if (!dueDateVOList.isEmpty()) {
            dueDateService.saveAllDueDates(dueDateVOList);
        }
    }

    @Override
    public BaseSearchResponse<GracePeriodResponse.DetailResponse> getGracePeriodListByWork(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        List<GracePeriodVO> list = gracePeriodMapper.findAllByWork(officeSeq, appSeq);

        // 리스트가 비었을 경우 빈 값 반환.
        if (list.isEmpty()) return new BaseSearchResponse<>();

        List<GracePeriodResponse.DetailResponse> res = list.stream()
                .map(vo ->
                        GracePeriodResponse.DetailResponse.builder()
                                .appSeq(vo.getAppSeq())
                                .gracePeriodSeq(vo.getGracePeriodSeq())
                                .gracePeriodContent(
                                        new CommonRecordResponse.CodeInfo(
                                                vo.getGracePeriodContentCode(),
                                                vo.getGracePeriodContentName()
                                        )
                                )
                                .submitDeadLineDate(formatMinusHoursString8(vo.getSubmitDeadLineDate()))
                                .submitClosingDate(formatMinusHoursString8(vo.getSubmitClosingDate()))
                                .gracePeriodDate(formatMinusHoursString8(vo.getGracePeriodDate()))
                                .note(vo.getNote())
                                .build()
                ).toList();

        return BaseSearchResponse.of(res, 1, 99);
    }

    @Override
    public GracePeriodResponse.DetailResponse getGracePeriodDetail(String appSeq, String gracePeriodSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        GracePeriodVO vo = gracePeriodMapper.getGracePeriodDetail(officeSeq, appSeq, gracePeriodSeq);

        if (vo == null) {
            throw new RuntimeException("Grace period not found");
        }

        return GracePeriodResponse.DetailResponse.builder()
                .appSeq(vo.getAppSeq())
                .gracePeriodSeq(vo.getGracePeriodSeq())
                .gracePeriodContent(
                        new CommonRecordResponse.CodeInfo(
                                vo.getGracePeriodContentCode(),
                                vo.getGracePeriodContentName()
                        )
                )
                .submitDeadLineDate(formatMinusHoursString8(vo.getSubmitDeadLineDate()))
                .submitClosingDate(formatMinusHoursString8(vo.getSubmitClosingDate()))
                .gracePeriodDate(formatMinusHoursString8(vo.getGracePeriodDate()))
                .note(vo.getNote())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteGracePeriod(String appSeq, String gracePeriodSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = gracePeriodMapper.softDeleteGracePeriod(officeSeq, appSeq, gracePeriodSeq, userSeq);

        if (result <= 0) {
            throw new RuntimeException("Grace period not found or already soft deleted");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteGracePeriodByList(String appSeq, List<String> gracePeriodSeqList) {
        if (gracePeriodSeqList == null || gracePeriodSeqList.isEmpty()) {
            throw new RuntimeException("Grace period sequence list is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = gracePeriodMapper.softDeleteGracePeriodByList(officeSeq, userSeq, appSeq, gracePeriodSeqList);

        if (result != gracePeriodSeqList.size()) {
            throw new RuntimeException("Grace period soft deletion failed");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteGracePeriod(String appSeq, String gracePeriodSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = gracePeriodMapper.hardDeleteGracePeriod(officeSeq, appSeq, gracePeriodSeq);

        if (result <= 0) {
            throw new RuntimeException("Grace period not found or already hard deleted");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteGracePeriodByList(String appSeq, List<String> gracePeriodSeqList) {
        if (gracePeriodSeqList == null || gracePeriodSeqList.isEmpty()) {
            throw new RuntimeException("Grace period sequence list is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = gracePeriodMapper.hardDeleteGracePeriodByList(officeSeq, appSeq, gracePeriodSeqList);

        if (result != gracePeriodSeqList.size()) {
            throw new RuntimeException("Grace period hard deletion failed");
        }
    }

    // =================================================================
    // 기일 추가 (Null Safe & 날짜 파싱 통합)
    // =================================================================
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
