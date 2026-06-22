package kr.co.mindpro.ipms.domain.preference.service.impl;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.util.DataConvertUtil;
import kr.co.mindpro.ipms.common.util.SecurityUtil;

import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.preference.dto.request.PreferenceRequest;
import kr.co.mindpro.ipms.domain.preference.dto.response.PreferenceResponse;
import kr.co.mindpro.ipms.domain.preference.repository.db1.PreferenceMapper;
import kr.co.mindpro.ipms.domain.preference.service.PreferenceService;
import kr.co.mindpro.ipms.domain.preference.vo.PreferenceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 청구서 비즈니스 로직 구현체
 * 
 * @author   : min
 * @fileName : DuedateServiceImpl.java
 * @since    : 2026. 01. 07.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PreferenceServiceImpl implements PreferenceService {
    private final PreferenceMapper preferenceMapper;
    private final DueDateService dueDateService;

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<PreferenceResponse.PreferenceDetail> getPreferenceList(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        List<PreferenceResponse.PreferenceDetail> list =preferenceMapper.findAllByApp(appSeq, officeSeq);

        return BaseSearchResponse.of(list,1 ,1 );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllPreferences(String appSeq, List<PreferenceVO> list) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();

        // 1. 기존 데이터 삭제
        preferenceMapper.deleteByApp(appSeq, officeSeq, userId);

        // 2. 새로운 데이터 인서트
        if (list != null && !list.isEmpty()) {
            list.forEach(vo -> {
                vo.setAppSeq(appSeq);
                vo.setOfficeSeq(officeSeq);
                vo.setCreateUser(userId);
                preferenceMapper.insertPreference(vo);

                // [날짜 저장] DataConvertUtil을 사용하여 VO의 @CommonMapping 필드에서 기일 추출 후 저장
                // workCategory는 "PREFER" (VO의 group 설정값) 또는 테이블 구분자로 세팅
                dueDateService.saveAllDueDates(
                        DataConvertUtil.extractDueDates(vo, vo.getPreferenceSeq(), officeSeq, "PREFER")
                );
            });
        }
    }

    public void registerPreference(PreferenceRequest.PreferenceDetail request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();

        PreferenceVO vo = PreferenceVO.builder()
                .officeSeq(officeSeq)
                .appSeq(request.appSeq())
                .preferenceSeq(request.preferenceSeq()) // 수정 시 존재
                .priorCountryCode(request.priorCountryCode())
                .preferenceNo(request.preferenceNo())
                .wipoCategoryCode(request.wipoCategoryCode())
                .preferenceSearch(request.preferenceSearch())
                .fullContentUrl(request.fullContentUrl())

                //날짜
                .preferenceAssertDate(request.preferenceAssertDate())
                .submitDeadLineDate(request.submitDeadLineDate())
                .submitClosingDate(request.submitClosingDate())
                .preferenceRegDate(request.preferenceRegDate())

                .note(request.note())
                .createUser(userId)
                .updateUser(userId)
                .build();


        // [핵심] 기존 Seq가 있다면 해당 단건만 논리 삭제
        if (vo.getPreferenceSeq() != null && !vo.getPreferenceSeq().isEmpty()) {
            // 단건 수정이므로 appSeq 전체를 지우는 deleteByApp보다는
            // 특정 Seq만 지우는 쿼리를 호출하는 것이 안전합니다.
            preferenceMapper.deletePreference(vo.getAppSeq(), vo.getPreferenceSeq(), officeSeq, userId);
        }

        preferenceMapper.insertPreference(vo);

        dueDateService.saveAllDueDates(
                DataConvertUtil.extractDueDates(vo, vo.getPreferenceSeq(), officeSeq, "PREFER")
        );
    }

    @Override
    public PreferenceResponse.PreferenceDetail getPreference(String preferenceSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();



        return preferenceMapper.findById(preferenceSeq, officeSeq);
    }

    @Override
    public void softDeletePreference(String appSeq, String preferenceSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();

        int result = preferenceMapper.deletePreference(appSeq, preferenceSeq, officeSeq, userId);

        if (result < 0) {
            log.error(">>> [ERROR] 우선권 논리적 삭제 실패! (Return: 0)");
            throw new RuntimeException("우선권 논리적 삭제에 실패했습니다.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeletePreferenceByList(String appSeq, List<String> preferenceSeqList) {
        if (preferenceSeqList == null || preferenceSeqList.isEmpty()) {
            throw new RuntimeException("우선권 시퀀스 목록이 비어 있습니다.");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();

        int result = preferenceMapper.softDeletePreferenceList(appSeq, preferenceSeqList, officeSeq, userId);

        if (result != preferenceSeqList.size()) {
            log.error(">>> [ERROR] 우선권 다건 논리 삭제 실패! (Expected: {}, Actual: {})", preferenceSeqList.size(), result);
            throw new RuntimeException("우선권 다건 논리 삭제에 실패했습니다.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeletePreference(String appSeq, String preferenceSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = preferenceMapper.hardDeletePreference(appSeq, preferenceSeq, officeSeq);

        if (result <= 0) {
            log.error(">>> [ERROR] 우선권 단건 물리 삭제 실패! (Return: 0)");
            throw new RuntimeException("우선권 단건 물리 삭제에 실패했습니다.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeletePreferenceByList(String appSeq, List<String> preferenceSeqList) {
        if (preferenceSeqList == null || preferenceSeqList.isEmpty()) {
            throw new RuntimeException("우선권 시퀀스 목록이 비어 있습니다.");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = preferenceMapper.hardDeletePreferenceList(appSeq, preferenceSeqList, officeSeq);

        if (result != preferenceSeqList.size()) {
            log.error(">>> [ERROR] 우선권 다건 물리 삭제 실패! (Expected: {}, Actual: {})", preferenceSeqList.size(), result);
            throw new RuntimeException("우선권 다건 물리 삭제에 실패했습니다.");
        }
    }
}