package kr.co.mindpro.ipms.domain.patentApp.appCommon.service.impl;

import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.repository.db1.AppCommonMapper;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.repository.db1.DomesticAppMapper;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.service.AppCommonService;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.AppBasicInfoVO;
import kr.co.mindpro.ipms.domain.paper.repository.db1.PaperMapper;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.AppBasicInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : seokho
 * @fileName : AppCommonServiceImpl.java
 * @since : 2026. 1. 21.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 트랜잭션 기본 설정 (성능 최적화)
public class AppCommonServiceImpl implements AppCommonService {

    private final DomesticAppMapper domesticAppMapper;
    private final AppCommonMapper appCommonMapper;
    private final PaperMapper paperMapper;

    @Override
    public AppBasicInfoVO getBasicInfoDetail(String officeSeq, String appSeq) {

        List<String> dueDateCateCodeList = List.of(
                "inventionReportDate", "receiptDate", "draftDeadline", "draftSendDate",
                "appOrderDate", "appDeadline", "appDate", "transDeadline", "transSubmitDate"
        );

        List<String> participantCateCodeList = List.of(
                "caseMgr", "adminMgr", "attorney", "clientName",
                "clientContact", "applicantName", "inventorName", "regMgr"
        );

        return domesticAppMapper.getBasicInfoDetail(officeSeq, appSeq, dueDateCateCodeList, participantCateCodeList).orElse(null);
    }

    // 출원 공통(국내/해외 통합) 삭제(물리적) 메서드
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteAppCommon(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // todo 지금 단계에서는 FK 제약조건이 없음으로 마스터만 물리적 삭제 진행 중.
        int result = appCommonMapper.hardDeleteAppCommon(officeSeq, appSeq);

        if (result <= 0) {
            throw new BusinessException("출원 정보 삭제(물리적) 실패", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 출원 공통(국내/해외 통합) 삭제(논리적) 메서드
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteAppCommon(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        // todo 연관성이 있는 다른 데이터들에 대한 처분도 고민되어야 함.
        int result = appCommonMapper.softDeleteAppCommon(officeSeq, appSeq, userSeq);

        if (result <= 0) {
            throw new BusinessException("출원 정보 삭제(논리적) 실패", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteAppImageFile(String appSeq, String fileSeq) {
        log.info(">>> [공통출원] 이미지 논리 삭제 요청 - appSeq={}, fileSeq={}", appSeq, fileSeq);
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();
        // 파일은 designSeq/trademarkSeq/patentSeq에 연결되어 있으므로 tblSeq 없이 fileSeq만으로 삭제
        paperMapper.softDeleteByFileSeq(officeSeq, fileSeq, loginUser);
    }
}
