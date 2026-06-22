package kr.co.mindpro.ipms.domain.patentApp.domesticApp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.customer.repository.db1.CustomerMapper;
import kr.co.mindpro.ipms.domain.customer.service.CustomerService;
import kr.co.mindpro.ipms.domain.customer.vo.CustomerMappVO;
import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import kr.co.mindpro.ipms.domain.paper.repository.db1.PaperMapper;
import kr.co.mindpro.ipms.domain.history.service.HistoryService;
import kr.co.mindpro.ipms.domain.paper.service.PaperService;
import kr.co.mindpro.ipms.domain.paper.vo.PaperRequestVO;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.participant.service.ParticipantService;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.repository.db1.AppCommonMapper;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.util.AppStatusUtil;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.request.DomesticDesignAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.request.DomesticHardIpAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.request.DomesticTrademarkRequest;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.response.*;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.repository.db1.DomesticAppMapper;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.service.DomesticAppService;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.*;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;
import static kr.co.mindpro.ipms.common.util.DataConvertUtil.parseToOffsetDateTime;

import kr.co.mindpro.ipms.domain.ai.service.RagService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DomesticAppServiceImpl implements DomesticAppService {

    private final ParticipantService participantService;
    private final DueDateService dueDateService;
    private final PaperService paperService;
    private final CustomerService customerService;
    private final HistoryService historyService;

    private final RagService ragService;
    private final ObjectMapper objectMapper;
    private final DomesticAppMapper domesticAppMapper;
    private final PaperMapper paperMapper;
    private final AppCommonMapper appCommonMapper;
    private final CustomerMapper customerMapper;

    @Override
    public BaseSearchResponse<DomesticAppListResponse.AppListResponse> getDomesticAppSearchList(BaseSearchRequest request) {

        String officeSeq = SecurityUtil.getOfficeSeq();

        request.setOfficeSeq(officeSeq);

        int totalCount = domesticAppMapper.getDomesticAppSearchListTotalCount(request);

        if (totalCount > 0) {
            List<CommonAppVO> appMstVOList = domesticAppMapper.getDomesticAppSearchList(request);

            List<DomesticAppListResponse.AppListResponse> list = appMstVOList.stream()
                    .map(DomesticAppListResponse.AppListResponse::fromVO)
                    .toList();

            return BaseSearchResponse.of(list, totalCount, request.getPage(), request.getPageSize());
        } else {
            return BaseSearchResponse.of(new ArrayList<>(), 0, request.getPage(), request.getPageSize());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createHardIpApp(DomesticHardIpAppRequest.CreateHardIpAppRequest request, MultipartFile file) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();
        // 수정 시 프론트에서 appSeq 전달 예정.
        String appSeq = request.appSeq();
        int result;

        CommonAppVO appVO = buildCommonAppVO(request, officeSeq, loginUser);

        // todo 해외 구분 강제 세팅 추후 삭제
        if (Objects.equals(appVO.getAppTypeCode(), "30")) {
            appVO.setCategoryCode("20"); // 외국으로 세팅
        } else {
            appVO.setCategoryCode("10"); // 내국으로 세팅
        }

        appVO.setAppRouteCode("10");        // 국내로 세팅

        // 특허(10), 실용신안(20) 값이 유효하게 들어왔는지 확인하는 로직.
        String rightType = appVO.getRightTypeCode();

        if (!"10".equals(rightType) && !"20".equals(rightType)) {
            log.error(">>> [ERROR] 특허/실용신안에 유효하지 않은 권리 구분 코드 유입: {}", rightType);
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 요청된 값에 따라 상태값 지정.
        AppStatusUtil.calculateAndSetAppState(appVO);

        // 중복되는 출원키가 있는지 확인하는 로직 필요. 있다면 업데이트 로직, 없다면 인서트 로직.
        if (appSeq == null || appSeq.isBlank()) {
            result = 0;
        } else {
            result = appCommonMapper.checkDuplicateSeq(officeSeq, appSeq);
        }

        CommonAppVO mstVO = null;
        if (result > 0) {
            // 출원키 중복 시 업데이트 로직 시작.

            // 1. 기존데이터 조회.
            // 마스터 정보 조회 (AppMstVO)
            mstVO = domesticAppMapper.getDomesticAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            // 2. 권리별 json 스냅샷 생성.
            String snapShot = processSnapshotAndUpdate(mstVO, appVO, officeSeq, appSeq, loginUser);

            mstVO.setRightSnapshot(snapShot);

            // 3. 히스토리 테이블에 기존데이터 인서트.
            result = appCommonMapper.insertAppHistory(mstVO);
            if (result == 0) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            result = domesticAppMapper.updateDomesticAppMst(appVO, appSeq, officeSeq, loginUser);
            if (result == 0) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            if (file != null && !file.isEmpty()) {
                // 주의: 기준 PK가 appSeq가 아니라 patentSeq
                paperService.softDeleteFilesByTblSeq(mstVO.getPatentSeq(), "appPatentFile");
                fileUpload(mstVO.getPatentSeq(), officeSeq, loginUser, "18", file, "appPatentFile");
            }

            // 업데이트 로직 종료.
        } else {

            // 중복된 출원키가 없을 때 로직 시작.
            // 출원마스터 인서트
            result = domesticAppMapper.insertDomesticApp(appVO);

            if (result <= 0) {
                // 에러 로그 남기기
                log.error(">>> [ERROR] 출원 마스터 정보 저장 실패! (Return: 0)");
                throw new RuntimeException("출원 정보(Master) 저장에 실패했습니다.");
            }

            appSeq = appVO.getAppSeq();

            insertPatentApp(appSeq, appVO, file);

            // 새로운 인서트 로직 종료.
        }

        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();

            if (party.clientInfo() != null && !party.clientInfo().isEmpty()) {
                customerMappToWork(party.clientInfo(), appSeq, "client");
            }
            if (party.applicantInfo() != null && !party.applicantInfo().isEmpty()) {
                customerMappToWork(party.applicantInfo(), appSeq, "applicant");
            }
            if (party.regMgrInfo() != null && !party.regMgrInfo().isEmpty()) {
                customerMappToWork(party.regMgrInfo(), appSeq, "regMgr");
            }
            // [추가] 발명자 — utb_app_mst 에 발명자 저장 컬럼이 없으므로 utb_customer_mapp 으로 통합 저장.
            // PersonInfo(단일) → CounterPartyInfo(List) 변환. CLIENT_DIV='20' 카테고리로 등록된 customer 가 발명자.
            if (party.inventorInfo() != null
                    && party.inventorInfo().userSeq() != null
                    && !party.inventorInfo().userSeq().isBlank()) {
                customerMappToWork(
                        java.util.List.of(kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse.CounterPartyInfo.builder()
                                .counterPartySeq(party.inventorInfo().userSeq())
                                .counterPartyName(party.inventorInfo().userName())
                                .build()),
                        appSeq,
                        "inventor"
                );
            }
        }

        // [공통] 관계자 정보 저장
        List<ParticipantVO> participants = getParticipantList(appSeq, appVO);

        if (!participants.isEmpty()) {
            participantService.saveAllParticipants(participants);
        }

        // [공통] 기일 정보 저장
        List<DueDateVO> dueDates = getDueDateList(appSeq, appVO);

        if (!dueDates.isEmpty()) {
            dueDateService.saveAllDueDates(dueDates);
        }

        // AI Vector Sync
        ragService.syncVectorData(kr.co.mindpro.ipms.common.util.SecurityUtil.getOfficeSeq(), "PATENT_APP", appSeq,"국내출원", this.getDomesticAppDetail(appSeq));
        
        if (mstVO != null) {
            CommonAppVO newVO = domesticAppMapper.getDomesticAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
            historyService.compareAndLog(appSeq, "국내출원 정보 수정", mstVO, newVO);
        }

        return appSeq;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createDesignApp(DomesticDesignAppRequest.CreateDomesticDesignAppRequest request, MultipartFile file) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();
        // 수정 시 프론트에서 appSeq 전달 예정.
        String appSeq = request.appSeq();
        int result;

        // 🌟 [변경] MergeVO를 거치지 않고 바로 CommonAppVO 빌드
        CommonAppVO appVO = buildCommonAppVO(request, officeSeq, loginUser);

        // todo 해외 구분 강제 세팅 추후 삭제
        if (Objects.equals(appVO.getAppTypeCode(), "30")) {
            appVO.setCategoryCode("20"); // 외국으로 세팅
        } else {
            appVO.setCategoryCode("10"); // 내국으로 세팅
        }
        appVO.setAppRouteCode("10");  // 국내로 세팅

        // 🌟 [변경] 디자인(30) 권리 구분 코드 유효성 검사
        String rightType = appVO.getRightTypeCode();

        if (!"30".equals(rightType)) {
            log.error(">>> [ERROR] 디자인 권리에 유효하지 않은 권리 구분 코드 유입: {}", rightType);
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 요청된 값에 따라 상태값 지정.
        AppStatusUtil.calculateAndSetAppState(appVO);

        // 중복되는 출원키가 있는지 확인하는 로직 (업데이트 vs 인서트 분기)
        if (appSeq == null || appSeq.isBlank()) {
            result = 0;
        } else {
            result = appCommonMapper.checkDuplicateSeq(officeSeq, appSeq);
        }

        CommonAppVO mstVO = null;
        if (result > 0) {
            // ==========================================
            // [수정 (UPDATE) 로직 시작]
            // ==========================================

            // 1. 기존데이터 조회 (마스터 정보 조회)
            mstVO = domesticAppMapper.getDomesticAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            // 2. 권리별 json 스냅샷 생성 및 업데이트 (🌟 mergeVO 대신 appVO 전달)
            String snapShot = processSnapshotAndUpdate(mstVO, appVO, officeSeq, appSeq, loginUser);
            mstVO.setRightSnapshot(snapShot);

            // 3. 히스토리 테이블에 기존데이터 인서트
            result = appCommonMapper.insertAppHistory(mstVO);
            if (result == 0) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 4. 마스터 테이블 업데이트
            result = domesticAppMapper.updateDomesticAppMst(appVO, appSeq, officeSeq, loginUser);
            if (result == 0) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            if (file != null && !file.isEmpty()) {
                // 주의: 기준 PK가 designSeq임
                paperService.softDeleteFilesByTblSeq(mstVO.getDesignSeq(), "appDesignFile");
                fileUpload(mstVO.getDesignSeq(), officeSeq, loginUser, "56", file, "appDesignFile");
            }

        } else {
            // ==========================================
            // [신규 (INSERT) 로직 시작]
            // ==========================================

            // 출원마스터 인서트
            result = domesticAppMapper.insertDomesticApp(appVO);

            if (result <= 0) {
                log.error(">>> [ERROR] 출원 마스터 정보 저장 실패! (Return: 0)");
                throw new RuntimeException("출원 정보(Master) 저장에 실패했습니다.");
            }

            appSeq = appVO.getAppSeq();

            // 🌟 [변경] 디자인용 하위 테이블 인서트 (mergeVO 대신 appVO 전달)
            insertDesignApp(appSeq, appVO, file);
        }

        // [공통] 관계사 정보 처리 (기존 로직 유지)
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();

            if (party.clientInfo() != null && !party.clientInfo().isEmpty()) {
                customerMappToWork(party.clientInfo(), appSeq, "client");
            }
            if (party.applicantInfo() != null && !party.applicantInfo().isEmpty()) {
                customerMappToWork(party.applicantInfo(), appSeq, "applicant");
            }
            if (party.regMgrInfo() != null && !party.regMgrInfo().isEmpty()) {
                customerMappToWork(party.regMgrInfo(), appSeq, "regMgr");
            }
            // [추가] 발명자 — utb_app_mst 에 발명자 저장 컬럼이 없으므로 utb_customer_mapp 으로 통합 저장.
            // PersonInfo(단일) → CounterPartyInfo(List) 변환. CLIENT_DIV='20' 카테고리로 등록된 customer 가 발명자.
            if (party.inventorInfo() != null
                    && party.inventorInfo().userSeq() != null
                    && !party.inventorInfo().userSeq().isBlank()) {
                customerMappToWork(
                        java.util.List.of(kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse.CounterPartyInfo.builder()
                                .counterPartySeq(party.inventorInfo().userSeq())
                                .counterPartyName(party.inventorInfo().userName())
                                .build()),
                        appSeq,
                        "inventor"
                );
            }
        }

        // 🌟 [변경] 관계자 정보 저장 (mergeVO -> appVO)
        List<ParticipantVO> participants = getParticipantList(appSeq, appVO);
        if (!participants.isEmpty()) {
            participantService.saveAllParticipants(participants);
        }

        // 🌟 [변경] 기일 정보 저장 (mergeVO -> appVO)
        List<DueDateVO> dueDates = getDueDateList(appSeq, appVO);
        if (!dueDates.isEmpty()) {
            dueDateService.saveAllDueDates(dueDates);
        }


        // AI Vector Sync
        ragService.syncVectorData(SecurityUtil.getOfficeSeq(), "PATENT_APP", appSeq, "국내 디자인 출원", this.getDomesticAppDetail(appSeq));
        
        if (mstVO != null) {
            CommonAppVO newVO = domesticAppMapper.getDomesticAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
            historyService.compareAndLog(appSeq, "국내출원 디자인 정보 수정", mstVO, newVO);
        }
        
        return appSeq;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createTrademarkApp(DomesticTrademarkRequest.CreateTrademarkAppRequest request, MultipartFile file) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();
        // 수정 시 프론트에서 appSeq 전달 예정.
        String appSeq = request.appSeq();
        int result;

        // 🌟 [변경] MergeVO 없이 바로 CommonAppVO 빌드 (오버로딩 메서드 호출)
        CommonAppVO appVO = buildCommonAppVO(request, officeSeq, loginUser);

        // todo 해외 구분 강제 세팅 추후 삭제
        if (Objects.equals(appVO.getAppTypeCode(), "30")) {
            appVO.setCategoryCode("20"); // 외국으로 세팅
        } else {
            appVO.setCategoryCode("10"); // 내국으로 세팅
        }
        appVO.setAppRouteCode("10");  // 국내로 세팅

        // 🌟 [변경] 상표(40) 권리 구분 코드 유효성 검사
        String rightType = appVO.getRightTypeCode();

        if (!"40".equals(rightType)) {
            log.error(">>> [ERROR] 상표 권리에 유효하지 않은 권리 구분 코드 유입: {}", rightType);
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 요청된 값에 따라 상태값 지정.
        AppStatusUtil.calculateAndSetAppState(appVO);

        // 중복되는 출원키가 있는지 확인 (업데이트 vs 인서트 분기)
        if (appSeq == null || appSeq.isBlank()) {
            result = 0;
        } else {
            result = appCommonMapper.checkDuplicateSeq(officeSeq, appSeq);
        }

        CommonAppVO mstVO = null;
        if (result > 0) {
            // ==========================================
            // [수정 (UPDATE) 로직 시작]
            // ==========================================

            // 1. 기존데이터 조회 (마스터 정보 조회)
            mstVO = domesticAppMapper.getDomesticAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            // 2. 권리별 json 스냅샷 생성 및 업데이트 (🌟 appVO 전달)
            String snapShot = processSnapshotAndUpdate(mstVO, appVO, officeSeq, appSeq, loginUser);
            mstVO.setRightSnapshot(snapShot);

            // 3. 히스토리 테이블에 기존데이터 인서트
            result = appCommonMapper.insertAppHistory(mstVO);
            if (result == 0) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 4. 마스터 테이블 업데이트
            result = domesticAppMapper.updateDomesticAppMst(appVO, appSeq, officeSeq, loginUser);
            if (result == 0) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            if (file != null && !file.isEmpty()) {
                // 주의: 기준 PK가 trademarkSeq
                paperService.softDeleteFilesByTblSeq(mstVO.getTrademarkSeq(), "appTrademarkFile");
                fileUpload(mstVO.getTrademarkSeq(), officeSeq, loginUser, "78", file, "appTrademarkFile");
            }

        } else {
            // ==========================================
            // [신규 (INSERT) 로직 시작]
            // ==========================================

            // 출원마스터 인서트
            result = domesticAppMapper.insertDomesticApp(appVO);

            if (result <= 0) {
                log.error(">>> [ERROR] 출원 마스터 정보 저장 실패! (Return: 0)");
                throw new RuntimeException("출원 정보(Master) 저장에 실패했습니다.");
            }

            appSeq = appVO.getAppSeq();

            // 🌟 [변경] 상표용 하위 테이블 인서트 (appVO 전달)
            insertTrademarkApp(appSeq, appVO, file);
        }

        // [공통] 관계자 정보 처리
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();

            if (party.clientInfo() != null && !party.clientInfo().isEmpty()) {
                customerMappToWork(party.clientInfo(), appSeq, "client");
            }
            if (party.applicantInfo() != null && !party.applicantInfo().isEmpty()) {
                customerMappToWork(party.applicantInfo(), appSeq, "applicant");
            }
            if (party.regMgrInfo() != null && !party.regMgrInfo().isEmpty()) {
                customerMappToWork(party.regMgrInfo(), appSeq, "regMgr");
            }
            // [상표] 발명자 개념 없음 — TrademarkAppCounterPartyInfo 에 inventorInfo 자체가 없으므로 분기 생략.
        }

        // 🌟 [변경] 관계자 정보 저장 (appVO 전달)
        List<ParticipantVO> participants = getParticipantList(appSeq, appVO);
        if (!participants.isEmpty()) {
            participantService.saveAllParticipants(participants);
        }

        // 🌟 [변경] 기일 정보 저장 (appVO 전달)
        List<DueDateVO> dueDates = getDueDateList(appSeq, appVO);
        if (!dueDates.isEmpty()) {
            dueDateService.saveAllDueDates(dueDates);
        }


        // AI Vector Sync
        ragService.syncVectorData(SecurityUtil.getOfficeSeq(), "PATENT_APP", appSeq, "국내 상표 출원", this.getDomesticAppDetail(appSeq));
        
        if (mstVO != null) {
            CommonAppVO newVO = domesticAppMapper.getDomesticAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
            historyService.compareAndLog(appSeq, "국내출원 상표 정보 수정", mstVO, newVO);
        }

        return appSeq;
    }

    @Override
    public DomesticAppDetailResponse getDomesticAppDetail(String appSeq) {

        String officeSeq = SecurityUtil.getOfficeSeq();

        // 1. 마스터 정보 조회 (CommonAppVO)
        CommonAppVO mstVO = domesticAppMapper.getDomesticAppDetail(officeSeq, appSeq)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. 당사자(CounterParty) 정보 조회
        List<CommonRecordResponse.CounterPartyInfo> clientList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "client");
        List<CommonRecordResponse.CounterPartyInfo> applicantList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "applicant");
        List<CommonRecordResponse.CounterPartyInfo> regMgrList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "regMgr");

        // [신규] 발명자도 utb_customer_mapp 의 'inventor' 카테고리에서 조회 — fromVOView 시그니처 변경 없이 mstVO 에 주입.
        // utb_app_mst 에 발명자 컬럼이 없어 mstVO.inventor 가 항상 NULL이라, 매핑에서 가져온 첫 행을 임시 주입.
        List<CommonRecordResponse.CounterPartyInfo> inventorList =
                customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "inventor");
        if (!inventorList.isEmpty()) {
            var inv = inventorList.get(0);
            mstVO.setInventor(inv.counterPartySeq());
            mstVO.setInventorNm(inv.counterPartyName());
        }

        // 파일 리스트 담을 변수
        List<PaperResponseVO> fileList;

        // 3. 권리구분코드에 따른 Response 변환 및 반환
        return switch (mstVO.getRightTypeCode()) {
            case "10", "20" -> { // 특허, 실용신안
                fileList = paperMapper.findAllByWork(mstVO.getPatentSeq(), officeSeq);
                yield DomesticHardIpAppResponse.HardIpAppDetailResponse.fromVOViewPatent(mstVO, clientList, applicantList, regMgrList, fileList);
            }
            case "30" -> { // 디자인
                fileList = paperMapper.findAllByWork(mstVO.getDesignSeq(), officeSeq);
                yield DomesticDesignAppResponse.DesignAppDetailResponse.fromVOViewDesign(mstVO, clientList, applicantList, regMgrList, fileList);
            }
            case "40" -> { // 상표
                fileList = paperMapper.findAllByWork(mstVO.getTrademarkSeq(), officeSeq);
                yield DomesticTrademarkAppResponse.TrademarkAppDetailResponse.fromVOViewTrademark(mstVO, clientList, applicantList, regMgrList, fileList);
            }
            default -> throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        };
    }

    /**
     * [MergeVO -> MasterVO 변환]
     * 평탄화된 DomesticAppMergeVO 데이터를 기반으로 AppMstVO 객체를 생성합니다.
     *//*
    public CommonAppVO buildAppMstVOFromMerge(DomesticAppMergeVO mergeVO) {
        return CommonAppVO.builder()
                // 1. 기본 식별자 (MergeVO에 이미 세팅된 값 사용)
                .officeSeq(mergeVO.getOfficeSeq())
                .createUser(mergeVO.getCreateUser())
                .updateUser(mergeVO.getCreateUser())
                .appSeq("utb_app_mst")
                .retainSeq(mergeVO.getClientRef()) // 의뢰인 관리번호

                // 2. 상태값 (기본값 설정)
//                .appRouteCode(mergeVO.getAppRouteCode())
                .appStateCode("없음")
                .stateCode("없음")
//                .countryCode("없음") // 기본 "없음" (필요시 mergeVO.getCountryCode()로 변경)

                // 3. 분류 및 명칭 정보
                .rightCategoryCode(mergeVO.getRightTypeCode())
                .productClass(mergeVO.getGoodsClass())
                .appNameKo(mergeVO.getTitleKo())
                .appNameEn(mergeVO.getTitleEn())
//                .appName("없음") // 통합 명칭 (필요시 로직 추가)
                .etcTitle(mergeVO.getEtcTitle())
                .proposalName(mergeVO.getProposal())

                // 4. 출원 관리 및 업체 정보
                .outsourcingCorpName(mergeVO.getAnnuityAgency()) // 연차위임업체 -> 외주업체명
//                .outsourcingYn("X")
                .giveUpContent(mergeVO.getAbandonNote())
                .appCategoryCode(mergeVO.getAppCategoryCode())
                .appClassificationCode(mergeVO.getCategoryCode())
                .appKindCode(mergeVO.getAppTypeCode())
                .appLanguageCode(mergeVO.getAppLanguageCode())

                // 5. 명세서 및 청구항 정보
                .gradeCode(mergeVO.getGradeCode())
                .independentClaim(mergeVO.getIndependentClaims())
                .dependentClaim(mergeVO.getDependentClaims())
                .drawingPaperCount(mergeVO.getDrawingCount())
                .ultiDependentClaimCount(mergeVO.getFinalClaimsCount())
                .specPage(mergeVO.getSpecPage())
                .figureCount(mergeVO.getFigureCount())

                // 6. 출원 번호 및 연관 번호 정보
                .goodsAppNo(mergeVO.getClassificAppNo())
                .appNo(mergeVO.getAppNo())
                .originalAppNo(mergeVO.getOriginalAppNo())
                .originalRegNo(mergeVO.getOriginalRegNo())
                .doubleAppNo(mergeVO.getDualAppNo())
                .globalAppNo(mergeVO.getGlobalAppNo())
//                .globalRegNo("없음")
                .regNo(mergeVO.getRegNo())
                .openNo(mergeVO.getPubNo())
//                .productClassAppNo("없음")
                .reAppNo(mergeVO.getReAppNo())
                .publicNo(mergeVO.getAnnouncementNo())
                .regPublicNo(mergeVO.getRegAnnounceNo())
                .madridNo(mergeVO.getMadridAppNo())
                .firstAppNo(mergeVO.getFirstAppNo())
                .originalRegNo(mergeVO.getOriginalRegNo())

                // 7. 행정 및 관리 여부
                .ipcCategoryCode(mergeVO.getIpcClassification())
//                .externalAppApproach("없음")
                .yearCntManagementYn(mergeVO.getIsAnnuityManaged())
                .externalAppYn(mergeVO.getIsForeignApp())
                .trademarkResearchYn(mergeVO.getIsTrademarkResearch())
                .renewalManagementYn(mergeVO.getIsRenewalManaged())
                .interiorPreferenceAssertYn(mergeVO.getHasDomesticPriority())
                .mandatePaperSubmitYn(mergeVO.getIsPoaSubmitted())

                // 8. 기한 및 비용 정보
                .foreignAppTimingCode(mergeVO.getForeignAppTimingCode())
                .accessCode(mergeVO.getAccessCode())
                .annuityYear(mergeVO.getAnnuityYear())
                .annuityReducRateCode(mergeVO.getAnnuityReducRateCode())
                .regReductionRateCode(mergeVO.getRegReductionRateCode())
                .trademarkRenewalFee(mergeVO.getTrademarkRenewalFee())
                .renewalLateFee(mergeVO.getRenewalLateFee())
                .nextPaymentInstallment(mergeVO.getNextPaymentInstallment())

                // 9. 관리자 및 참조 정보
                .assetNo(mergeVO.getOurRef())
                .agentRef(mergeVO.getYourRef())
                .deptCode(mergeVO.getDeptName()) // 부서명 -> 부서코드 매핑
                .note(mergeVO.getNote())

                // 10. 파일 및 디자인 특화 정보
//                .mainDrawingFile("없음")
                .multiViewDrawingFile(mergeVO.getMultiViewDrawingFile())
                .isPartialDesign(mergeVO.getIsPartialDesign())
                .multiDesign(mergeVO.getMultiDesign() == null ? 0 : mergeVO.getMultiDesign())
                .trademarkImageFile(mergeVO.getTrademarkImageFile())

                // 11. counter party 정보
                .clientNm(mergeVO.getClientName())
                .applicantNm(mergeVO.getApplicantName())
                .regMgrNm(mergeVO.getRegMgrName())

                .build();
    }*/

    // =================================================================
    // [Private Helper Methods] 각 권리별 상세 저장 로직
    // =================================================================

    // 특허/실용신안 상세 저장
    public void insertPatentApp(String appSeq, CommonAppVO appVO, MultipartFile file) {
        AppPatentVO patentVO = AppPatentVO.builder()
                .appSeq(appSeq)
                .officeSeq(appVO.getOfficeSeq())
                .delYn("N")
                .appCategoryCode(appVO.getRightTypeCode())
                .summary(appVO.getSummary())
                .claimScope(appVO.getClaimScope())

                .createUser(appVO.getCreateUser())
                .updateUser(appVO.getCreateUser())
                .build();

        domesticAppMapper.insertPatentApp(patentVO);

        // 파일 업로드
        fileUpload(patentVO.getPatentSeq(), SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq(), "18", file, "appPatentFile");
    }

    // 디자인 상세 저장
    public void insertDesignApp(String appSeq, CommonAppVO appVO, MultipartFile file) {
        // null safe 처리 (숫자형 변환 시 주의)
        String multiDesignVal = (appVO.getMultiDesign() != null)
                ? String.valueOf(appVO.getMultiDesign())
                : "0";

        AppDesignVO designVO = AppDesignVO.builder()
                .appSeq(appSeq)
                .officeSeq(appVO.getOfficeSeq())
                .delYn("N")
                .multiViewDrawingFile(appVO.getMultiViewDrawingFile())
                .multiDesign(multiDesignVal)
                .isPartialDesign(appVO.getIsPartialDesign())
                .designDescription(appVO.getDesignDescription())
                .designSummary(appVO.getDesignSummary())

                .createUser(appVO.getCreateUser())
                .updateUser(appVO.getCreateUser())
                .build();

        domesticAppMapper.insertDesignApp(designVO);

        // 파일 업로드
        fileUpload(designVO.getDesignSeq(), SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq(), "56", file, "appDesignFile");
    }

    // 상표 상세 저장
    public void insertTrademarkApp(String appSeq, CommonAppVO appVO, MultipartFile file) {
        AppTrademarkVO trademarkVO = AppTrademarkVO.builder()
                .appSeq(appSeq)
                .officeSeq(appVO.getOfficeSeq())
                .delYn("N")
                .trademarkImageFile(appVO.getTrademarkImageFile())
                .createUser(appVO.getCreateUser())
                .updateUser(appVO.getCreateUser())
                .build();

        domesticAppMapper.insertTrademarkApp(trademarkVO);

        // 파일 업로드
        fileUpload(trademarkVO.getTrademarkSeq(), SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq(), "78", file, "appTrademarkFile");
    }

    // 각 권리별 데이터 snapShot 생성 및 신규 데이터 softDelete & 인서트
    public String processSnapshotAndUpdate(CommonAppVO mstVO, CommonAppVO appVO, String officeSeq, String appSeq, String loginUser) {
        int result;
        String snapShot;

        try {
            switch (appVO.getRightTypeCode()) {
                case "10", "20" -> {
                    AppPatentVO historyPatentVO = new AppPatentVO();

                    historyPatentVO.setOfficeSeq(officeSeq);
                    historyPatentVO.setAppSeq(appSeq);
                    historyPatentVO.setPatentSeq(mstVO.getPatentSeq());

                    historyPatentVO.setSummary(mstVO.getSummary());
                    historyPatentVO.setClaimScope(mstVO.getClaimScope());

                    historyPatentVO.setCreateUser(mstVO.getCreateUser());
                    historyPatentVO.setCreateAt(mstVO.getCreateAt());
                    historyPatentVO.setUpdateUser(mstVO.getUpdateUser());

                    snapShot = objectMapper.writeValueAsString(historyPatentVO);

                    // 새 정보 저장 로직
                    AppPatentVO newPatentVO = new AppPatentVO();
                    newPatentVO.setOfficeSeq(officeSeq);
                    newPatentVO.setAppSeq(appSeq);
                    newPatentVO.setPatentSeq(mstVO.getPatentSeq()); // 시퀀스는 기존꺼 유지

                    newPatentVO.setSummary(appVO.getSummary());       // 새 데이터!
                    newPatentVO.setClaimScope(appVO.getClaimScope()); // 새 데이터!

                    newPatentVO.setUpdateUser(loginUser);

                    result = domesticAppMapper.updateHardIpApp(newPatentVO);

                    if (result == 0) {
                        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                }
                case "30" -> {
                    AppDesignVO historyDesignVO = new AppDesignVO();

                    historyDesignVO.setDesignSeq(mstVO.getDesignSeq());
                    historyDesignVO.setDesignSummary(mstVO.getDesignSummary());
                    historyDesignVO.setDesignDescription(mstVO.getDesignDescription());
                    historyDesignVO.setMultiDesign(String.valueOf(mstVO.getMultiDesign()));
                    historyDesignVO.setIsPartialDesign(mstVO.getIsPartialDesign());

                    snapShot = objectMapper.writeValueAsString(historyDesignVO);

                    // 새 정보 저장 로직
                    AppDesignVO newDesignVO = new AppDesignVO();

                    newDesignVO.setOfficeSeq(officeSeq);
                    newDesignVO.setAppSeq(appSeq);
                    newDesignVO.setDesignSeq(mstVO.getDesignSeq());

                    newDesignVO.setDesignSummary(appVO.getDesignSummary());
                    newDesignVO.setDesignDescription(appVO.getDesignDescription());
                    newDesignVO.setMultiDesign(String.valueOf(appVO.getMultiDesign()));
                    newDesignVO.setIsPartialDesign(appVO.getIsPartialDesign());

                    newDesignVO.setUpdateUser(loginUser);

                    result = domesticAppMapper.updateDesignApp(newDesignVO);

                    if (result == 0) {
                        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                }
                case "40" -> {
                    AppTrademarkVO historyTrademarkVO = new AppTrademarkVO();

                    historyTrademarkVO.setTrademarkSeq(mstVO.getTrademarkSeq());
                    historyTrademarkVO.setMadridAppNo(mstVO.getMadridNo());
                    historyTrademarkVO.setClassificAppNo(mstVO.getProductClassAppNo());

                    snapShot = objectMapper.writeValueAsString(historyTrademarkVO);

                }
                default -> throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(">>> 이력 데이터 JSON 변환 중 오류 발생", e);
        }

        return snapShot;
    }

    public void customerMappToWork(List<CommonRecordResponse.CounterPartyInfo> counterParties, String tblSeq, String category) {

        if (counterParties == null || counterParties.isEmpty()) {
            return;
        }

        List<CustomerMappVO> customerMappVOList = new ArrayList<>();

        for (CommonRecordResponse.CounterPartyInfo party : counterParties) {
            customerMappVOList.add(
                    CustomerMappVO.builder()
                            .tblSeq(tblSeq)
                            .customerSeq(party.counterPartySeq())
                            // 각 업무 테이블 코드
                            .relationCode("APPMST")
                            .customerCategoryCode(category)
                            .note(party.counterPartyNote())
                            .orderNo(party.counterPartyOrderNo())
                            .build()
            );
        }

        customerService.insertCustomerMappToWork(customerMappVOList);
    }

    /**
     * [통합 관계자 리스트 생성]
     * 디자인, 특허/실용신안, 상표 공통
     * MergeVO의 Seq 유무를 체크하여 ParticipantVO 리스트를 생성합니다.
     */
    public List<ParticipantVO> getParticipantList(String appSeq, CommonAppVO appVO) {

        String officeSeq = appVO.getOfficeSeq();

        List<ParticipantVO> participantVOList = new ArrayList<>();

        // 1. 담당자 그룹 (Manager)
        addParticipantIfPresent(participantVOList, "caseMgr",     appVO.getCaseMgr(),     appSeq, officeSeq);
        addParticipantIfPresent(participantVOList, "adminMgr",    appVO.getAdminMgr(),    appSeq, officeSeq);
        addParticipantIfPresent(participantVOList, "attorney",    appVO.getAttorney(),    appSeq, officeSeq);

        // 2. 당사자 그룹 (CounterParty)
//        addParticipantIfPresent(participantVOList, "client",  mergeVO.getClientSeq(),      appSeq, officeSeq);
        addParticipantIfPresent(participantVOList, "clientContact", appVO.getClientContact(), appSeq, officeSeq);
//        addParticipantIfPresent(participantVOList, "applicant", mergeVO.getApplicantSeq(),     appSeq, officeSeq);
        addParticipantIfPresent(participantVOList, "inventor",  appVO.getInventor(),      appSeq, officeSeq);
//        addParticipantIfPresent(participantVOList, "regMgr",      mergeVO.getRegMgrSeq(),      appSeq, officeSeq);

        return participantVOList;
    }

    // =================================================================
    // 관계자 추가 (Null Safe & 중복 코드 제거)
    // =================================================================
    public void addParticipantIfPresent(List<ParticipantVO> list, String code, String userSeq, String appSeq, String officeSeq) {
        // Seq가 존재할 때만 리스트에 추가 (상표라서 발명자가 없으면 자동으로 skip됨)
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

    /**
     * [통합 기일정보 리스트 생성]
     * 디자인, 특허/실용신안, 상표 공통
     * MergeVO의 날짜 값 유무를 체크하여 DueDateVO 리스트를 생성합니다.
     */
    public List<DueDateVO> getDueDateList(String appSeq, CommonAppVO appVO) {

        String officeSeq = appVO.getOfficeSeq();

        List<DueDateVO> dueDateVOList = new ArrayList<>();

        // =================================================================
        // [0] 출원 기본 정보 (공통)
        // =================================================================
        addDueDateIfPresent(dueDateVOList, "appOrderDate",    appVO.getAppOrderDate(),    appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "appDeadline",     appVO.getAppDeadline(),     appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "appDate",         appVO.getAppDate(),         appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "transDeadline",   appVO.getTransDeadline(),   appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "transSubmitDate", appVO.getTransSubmitDate(), appSeq, officeSeq);

        // =================================================================
        // [1] 사건관리 기일 정보 (공통)
        // =================================================================
        addDueDateIfPresent(dueDateVOList, "inventionReportDate", appVO.getInventionReportDate(), appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "receiptDate",         appVO.getReceiptDate(),         appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "draftDeadline",       appVO.getDraftDeadline(),       appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "draftSendDate",       appVO.getDraftSendDate(),       appSeq, officeSeq);

        // =================================================================
        // [2] 전략설정 기일 정보 (권리별 상이 -> 자동 필터링)
        // =================================================================
        addDueDateIfPresent(dueDateVOList, "firstAppDate",      appVO.getFirstAppDate(),      appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "originalAppDate",     appVO.getOriginalAppDate(),     appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "originalRegDate",  appVO.getOriginalRegDate(),  appSeq, officeSeq); // 디자인 등
        addDueDateIfPresent(dueDateVOList, "reAppDate",         appVO.getReAppDate(),         appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "dualAppDate",       appVO.getDualAppDate(),       appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "globalAppDate",     appVO.getGlobalAppDate(),     appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "madridAppDate",     appVO.getMadridAppDate(),     appSeq, officeSeq); // 상표 전용

        // 해외출원 관련
        addDueDateIfPresent(dueDateVOList, "foreign6mDeadline", appVO.getForeign6mDeadline(), appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "foreign1yDeadline", appVO.getForeign1yDeadline(), appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "foreignAppDate",    appVO.getForeignAppDate(),    appSeq, officeSeq);

        // 청구항 관련 (특허 전용)
        addDueDateIfPresent(dueDateVOList, "claimsNoticeDate",  appVO.getClaimsNoticeDate(),  appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "claimsDeadline",    appVO.getClaimsDeadline(),    appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "claimsSubmitDate",  appVO.getClaimsSubmitDate(),  appSeq, officeSeq);

        // =================================================================
        // [3] 행정관리 기일 정보
        // =================================================================
        addDueDateIfPresent(dueDateVOList, "earlyPubRequestDate",   appVO.getEarlyPubRequestDate(),   appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "domesticPriorDeadline", appVO.getDomesticPriorDeadline(), appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "domesticPriorDate",     appVO.getDomesticPriorDate(),     appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "examRequestDeadline",   appVO.getExamRequestDeadline(),   appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "examRequestDate",       appVO.getExamRequestDate(),       appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "priorExamReqDate",      appVO.getPriorExamReqDate(),      appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "priorExamDecDate",      appVO.getPriorExamDecDate(),      appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "announcementDecisionDate",    appVO.getAnnouncementDecisionDate(),    appSeq, officeSeq); // 상표 공고결정일
        addDueDateIfPresent(dueDateVOList, "pubDate",               appVO.getPubDate(),               appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "announcementDate",      appVO.getAnnouncementDate(),      appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "abandonOrderDate",      appVO.getAbandonOrderDate(),      appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "abandonDate",           appVO.getAbandonDate(),           appSeq, officeSeq);

        // =================================================================
        // [4] 등록/유지관리 기일 정보
        // =================================================================
//        addDueDateIfPresent(dueDateVOList, "kipoDelayDays",         appVO.getKipoDelayDays(),         appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "rightPeriod",           appVO.getRightPeriod(),           appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "regDecisionDate",       appVO.getRegDecisionDate(),       appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "regReceiptDate",        appVO.getRegReceiptDate(),        appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "priorityDate",          appVO.getPriorityDate(),          appSeq, officeSeq); // 상표 기연일
        addDueDateIfPresent(dueDateVOList, "regNormalDeadline",     appVO.getRegNormalDeadline(),     appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "regGraceDeadline",      appVO.getRegGraceDeadline(),      appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "regDate",               appVO.getRegDate(),               appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "regAnnounceDate",       appVO.getRegAnnounceDate(),       appSeq, officeSeq);

        // 상표 갱신 관련
//        addDueDateIfPresent(dueDateVOList, "standardDeadline", mergeVO.getStandardDeadline(), appSeq, officeSeq);
//        addDueDateIfPresent(dueDateVOList, "penaltyDeadline",  mergeVO.getPenaltyDeadline(),  appSeq, officeSeq);

        // 연차료 관련
        addDueDateIfPresent(dueDateVOList, "standardDeadline",   appVO.getStandardDeadline(),   appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "penaltyDeadline",    appVO.getPenaltyDeadline(),    appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "recoveryDeadline", appVO.getRecoveryDeadline(), appSeq, officeSeq);
        addDueDateIfPresent(dueDateVOList, "annuityOrderDate",        appVO.getAnnuityOrderDate(),        appSeq, officeSeq);

        return dueDateVOList;
    }

    // =================================================================
    // 기일 추가 (Null Safe & 날짜 파싱 통합)
    // =================================================================
    public void addDueDateIfPresent(List<DueDateVO> list, String code, String dateStr, String appSeq, String officeSeq) {
        // 날짜 문자열이 존재할 때만 파싱해서 리스트에 추가
        list.add(DueDateVO.builder()
                .duedateCategoryCode(code)
                .duedateDate(parseToOffsetDateTime(dateStr))
                .officeSeq(officeSeq)
                .tblSeq(appSeq)
                .build());
    }

    /**
     * [리팩토링] 특허/실용신안 Request -> CommonAppVO 다이렉트 변환
     */
    private CommonAppVO buildCommonAppVO(DomesticHardIpAppRequest.CreateHardIpAppRequest request, String officeSeq, String loginUser) {

        // 1. 공통 및 기본 식별자 세팅
        CommonAppVO.CommonAppVOBuilder<?, ?> builder = CommonAppVO.builder()
                .officeSeq(officeSeq)
                .createUser(loginUser)
                .updateUser(loginUser)
                // 넘어온 appSeq가 있으면 (수정) 세팅, 없으면 (신규) DB 채번용 함수 파라미터 세팅
                .appSeq(request.appSeq() != null && !request.appSeq().isBlank() ? request.appSeq() : "utb_app_mst");

        // 2. 사건 관리 (appCaseMng)
        if (request.appCaseMng() != null) {
            var mng = request.appCaseMng();
            if (mng.category() != null) {
                builder.categoryCode(mng.category().code());
                builder.categoryName(mng.category().codeName());
            }
            if (mng.rightType() != null) {
                builder.rightTypeCode(mng.rightType().code());
                builder.rightTypeName(mng.rightType().codeName());
            }
            if (mng.appType() != null) {
                builder.appTypeCode(mng.appType().code());
                builder.appTypeName(mng.appType().codeName());
            }
            if (mng.appCategory() != null) {
                builder.appCategoryCode(mng.appCategory().code());
                builder.appCategoryName(mng.appCategory().codeName());
            }
            builder.inventionReportDate(mng.inventionReportDate())
                    .receiptDate(mng.receiptDate())
                    .ourRef(mng.ourRef())
                    .yourRef(mng.yourRef())
                    .clientRef(mng.clientRef())
                    .draftDeadline(mng.draftDeadline())
                    .draftSendDate(mng.draftSendDate());
        }

        // 3. 출원 기본정보 (appBaseInfo)
        if (request.appBaseInfo() != null) {
            var base = request.appBaseInfo();
            builder.appOrderDate(base.appOrderDate())
                    .appDeadline(base.appDeadline())
                    .appDate(base.appDate())
                    .appNo(base.appNo())
                    .accessCode(base.accessCode())
                    .transDeadline(base.transDeadline())
                    .transSubmitDate(base.transSubmitDate());

            if (base.appLanguage() != null) {
                builder.appLanguageCode(base.appLanguage().code());
                builder.appLanguageName(base.appLanguage().codeName());
            }
        }

        // 4. 담당 정보 (appManagerInfo)
        if (request.appManagerInfo() != null) {
            var mgr = request.appManagerInfo();
            builder.deptName(mgr.deptCode()); // deptCode 필드를 CommonAppVO의 deptName에 매핑

            if (mgr.adminMgrInfo() != null) {
                builder.adminMgr(mgr.adminMgrInfo().userSeq());
                builder.adminMgrNm(mgr.adminMgrInfo().userName());
            }
            if (mgr.caseMgrInfo() != null) {
                builder.caseMgr(mgr.caseMgrInfo().userSeq());
                builder.caseMgrNm(mgr.caseMgrInfo().userName());
            }
            if (mgr.attorneyInfo() != null) {
                builder.attorney(mgr.attorneyInfo().userSeq());
                builder.attorneyNm(mgr.attorneyInfo().userName());
            }
        }

        // 5. 당사자 정보 (appCounterPartyInfo)
        // (List 형태인 의뢰인, 출원인 등은 DB 맵핑 테이블로 들어가므로, 단일 객체만 마스터에 세팅)
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();
            if (party.clientContactInfo() != null) {
                builder.clientContact(party.clientContactInfo().userSeq());
                builder.applicantContactNm(party.clientContactInfo().userName());
            }
            if (party.inventorInfo() != null) {
                builder.inventor(party.inventorInfo().userSeq());
                builder.inventorNm(party.inventorInfo().userName());
            }
        }

        // 6. 명칭 정보 (appNameInfo)
        if (request.appNameInfo() != null) {
            var nameInfo = request.appNameInfo();
            builder.proposal(nameInfo.proposal())
                    .titleKo(nameInfo.titleKo())
                    .titleEn(nameInfo.titleEn())
                    .etcTitle(nameInfo.etcTitle());
        }

        // 7. 명세서 구성요소 (appSpecificElement)
        if (request.appSpecificElement() != null) {
            var spec = request.appSpecificElement();
            builder.independentClaims(spec.independentClaims())
                    .dependentClaims(spec.dependentClaims())
                    .specPage(spec.specPage())
                    .figureCount(spec.figureCount())
                    .drawingCount(spec.drawingCount());

            if (spec.grade() != null) {
                builder.gradeCode(spec.grade().code());
                builder.gradeName(spec.grade().codeName());
            }
        }

        // 8. 전략설정 (appStrategy)
        if (request.appStrategy() != null) {
            var strat = request.appStrategy();
            builder.isForeignApp(strat.isForeignApp())
                    .foreign6mDeadline(strat.foreign6mDeadline())
                    .foreign1yDeadline(strat.foreign1yDeadline())
                    .foreignAppDate(strat.foreignAppDate())
                    .claimsNoticeDate(strat.claimsNoticeDate())
                    .claimsDeadline(strat.claimsDeadline())
                    .claimsSubmitDate(strat.claimsSubmitDate());

            if (strat.foreignAppTiming() != null) {
                builder.foreignAppTimingCode(strat.foreignAppTiming().code());
                builder.foreignAppTimingName(strat.foreignAppTiming().codeName());
            }
            if (strat.firstAppInfo() != null) {
                builder.firstAppDate(strat.firstAppInfo().firstAppDate())
                        .firstAppNo(strat.firstAppInfo().firstAppNo());
            }
            if (strat.originalAppInfo() != null) {
                builder.originalAppDate(strat.originalAppInfo().originalAppDate())
                        .originalAppNo(strat.originalAppInfo().originalAppNo());
            }
            if (strat.reAppInfo() != null) {
                builder.reAppDate(strat.reAppInfo().reAppDate())
                        .reAppNo(strat.reAppInfo().reAppNo());
            }
            if (strat.dualAppInfo() != null) {
                builder.dualAppDate(strat.dualAppInfo().dualAppDate())
                        .dualAppNo(strat.dualAppInfo().dualAppNo());
            }
            if (strat.globalAppInfo() != null) {
                builder.globalAppDate(strat.globalAppInfo().globalAppDate())
                        .globalAppNo(strat.globalAppInfo().globalAppNo());
            }
        }

        // 9. 요약/청구 (claimSummaryInfo)
        if (request.claimSummaryInfo() != null) {
            builder.summary(request.claimSummaryInfo().summary())
                    .claimScope(request.claimSummaryInfo().claimScope());
        }

        // 10. 행정관리 (appManagement)
        if (request.appManagement() != null) {
            var mgmt = request.appManagement();
            builder.isPoaSubmitted(mgmt.isPoaSubmitted())
                    .ipcClassification(mgmt.ipcClassification())
                    .earlyPubRequestDate(mgmt.earlyPubRequestDate())
                    .hasDomesticPriority(mgmt.hasDomesticPriority())
                    .domesticPriorDeadline(mgmt.domesticPriorDeadline())
                    .domesticPriorDate(mgmt.domesticPriorDate())
                    .examRequestDeadline(mgmt.examRequestDeadline())
                    .examRequestDate(mgmt.examRequestDate())
                    .priorExamReqDate(mgmt.priorExamReqDate())
                    .priorExamDecDate(mgmt.priorExamDecDate())
                    .pubDate(mgmt.pubDate())
                    .pubNo(mgmt.pubNo())
                    .announcementDate(mgmt.announcementDate())
                    .announcementNo(mgmt.announcementNo())
                    .abandonOrderDate(mgmt.abandonOrderDate())
                    .abandonDate(mgmt.abandonDate())
                    .abandonNote(mgmt.abandonNote());
        }

        // 11. 등록/유지관리 (appMaintenance)
        if (request.appMaintenance() != null) {
            var maint = request.appMaintenance();
            builder.finalClaimsCount(maint.finalClaimsCount())
                    .kipoDelayDays(maint.kipoDelayDays() != null ? maint.kipoDelayDays() : 0)
                    .rightPeriod(maint.rightPeriod())
                    .isAnnuityManaged(maint.isAnnuityManaged())
                    .regDecisionDate(maint.regDecisionDate())
                    .regReceiptDate(maint.regReceiptDate())
                    .regNormalDeadline(maint.regNormalDeadline())
                    .regGraceDeadline(maint.regGraceDeadline())
                    .regDate(maint.regDate())
                    .regNo(maint.regNo())
                    .regAnnounceDate(maint.regAnnounceDate())
                    .regAnnounceNo(maint.regAnnounceNo())
                    .annuityYear(maint.annuityYear())
                    .standardDeadline(maint.standardDeadline())
                    .penaltyDeadline(maint.penaltyDeadline())
                    .recoveryDeadline(maint.recoveryDeadline())
                    .annuityOrderDate(maint.annuityOrderDate())
                    .annuityAgency(maint.annuityAgency());

            if (maint.regReductionRate() != null) {
                builder.regReductionRateCode(maint.regReductionRate().code());
                builder.regReductionRateName(maint.regReductionRate().codeName());
            }
            if (maint.annuityReducRate() != null) {
                builder.annuityReducRateCode(maint.annuityReducRate().code());
                builder.annuityReducRateName(maint.annuityReducRate().codeName());
            }
        }

        // 12. 비고 (appNote)
        if (request.appNote() != null) {
            builder.note(request.appNote().note());
        }

        return builder.build();
    }

    /**
     * [리팩토링] 디자인 Request -> CommonAppVO 다이렉트 변환 (오버로딩)
     */
    private CommonAppVO buildCommonAppVO(DomesticDesignAppRequest.CreateDomesticDesignAppRequest request, String officeSeq, String loginUser) {

        // 1. 공통 및 기본 식별자 세팅
        CommonAppVO.CommonAppVOBuilder<?, ?> builder = CommonAppVO.builder()
                .officeSeq(officeSeq)
                .createUser(loginUser)
                .updateUser(loginUser)
                .appSeq(request.appSeq() != null && !request.appSeq().isBlank() ? request.appSeq() : "utb_app_mst");

        // 2. 출원_사건관리 (DesignAppCaseMng)
        if (request.appCaseMng() != null) {
            var mng = request.appCaseMng();
            if (mng.category() != null) {
                builder.categoryCode(mng.category().code());
                builder.categoryName(mng.category().codeName());
            }
            if (mng.rightType() != null) {
                builder.rightTypeCode(mng.rightType().code());
                builder.rightTypeName(mng.rightType().codeName());
            }
            if (mng.appType() != null) {
                builder.appTypeCode(mng.appType().code());
                builder.appTypeName(mng.appType().codeName());
            }
            if (mng.appCategory() != null) {
                builder.appCategoryCode(mng.appCategory().code());
                builder.appCategoryName(mng.appCategory().codeName());
            }
            builder.receiptDate(mng.receiptDate())
                    .ourRef(mng.ourRef())
                    .yourRef(mng.yourRef())
                    .clientRef(mng.clientRef())
                    .draftDeadline(mng.draftDeadline())
                    .draftSendDate(mng.draftSendDate());
        }

        // 3. 출원기본정보 (DesignAppBaseInfo)
        if (request.appBaseInfo() != null) {
            var base = request.appBaseInfo();
            builder.appOrderDate(base.appOrderDate())
                    .appDeadline(base.appDeadline())
                    .appDate(base.appDate())
                    .appNo(base.appNo())
                    .accessCode(base.accessCode());
        }

        // 4. 담당 정보 (DesignAppManagerInfo)
        if (request.appManagerInfo() != null) {
            var mgr = request.appManagerInfo();
            builder.deptName(mgr.deptCode());
            if (mgr.adminMgrInfo() != null) {
                builder.adminMgr(mgr.adminMgrInfo().userSeq());
                builder.adminMgrNm(mgr.adminMgrInfo().userName());
            }
            if (mgr.caseMgrInfo() != null) {
                builder.caseMgr(mgr.caseMgrInfo().userSeq());
                builder.caseMgrNm(mgr.caseMgrInfo().userName());
            }
            if (mgr.attorneyInfo() != null) {
                builder.attorney(mgr.attorneyInfo().userSeq());
                builder.attorneyNm(mgr.attorneyInfo().userName());
            }
        }

        // 5. 당사자 정보 (DesignAppCounterPartyInfo)
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();
            if (party.clientContactInfo() != null) {
                builder.clientContact(party.clientContactInfo().userSeq());
                builder.clientContactNm(party.clientContactInfo().userName());
            }
            if (party.inventorInfo() != null) {
                builder.inventor(party.inventorInfo().userSeq());
                builder.inventorNm(party.inventorInfo().userName());
            }
        }

        // 6. 명칭 정보 (DesignAppNameInfo)
        if (request.appNameInfo() != null) {
            var nameInfo = request.appNameInfo();
            builder.proposal(nameInfo.proposal())
                    .titleKo(nameInfo.titleKo())
                    .titleEn(nameInfo.titleEn())
                    .etcTitle(nameInfo.etcTitle());
        }

        // 7. 출원_전략설정 (DesignAppStrategy)
        if (request.appStrategy() != null) {
            var strat = request.appStrategy();
            builder.isForeignApp(strat.isForeignApp())
                    .foreign6mDeadline(strat.foreign6mDeadline())
                    .foreignAppDate(strat.foreignAppDate());

            if (strat.foreignAppTiming() != null) {
                builder.foreignAppTimingCode(strat.foreignAppTiming().code());
                builder.foreignAppTimingName(strat.foreignAppTiming().codeName());
            }
            // 원출원 정보
            if (strat.originalAppInfo() != null) {
                builder.originalAppDate(strat.originalAppInfo().originalAppDate())
                        .originalAppNo(strat.originalAppInfo().originalAppNo());
            }
            // 재출원 정보
            if (strat.reAppInfo() != null) {
                builder.reAppDate(strat.reAppInfo().reAppDate())
                        .reAppNo(strat.reAppInfo().reAppNo());
            }
            // 원등록 정보
            if (strat.originalRegInfo() != null) {
                builder.originalRegDate(strat.originalRegInfo().originalRegDate())
                        .originalRegNo(strat.originalRegInfo().originalRegNo());
            }
        }

        // 8. 출원_행정관리 (DesignAppManagement)
        if (request.appManagement() != null) {
            var mgmt = request.appManagement();
            builder.isPoaSubmitted(mgmt.isPoaSubmitted())
                    .ipcClassification(mgmt.ipcClassification())
                    .earlyPubRequestDate(mgmt.earlyPubRequestDate())
                    .isPartialDesign(mgmt.isPartialDesign()) // 디자인 전용 필드
                    .priorExamReqDate(mgmt.priorExamReqDate())
                    .priorExamDecDate(mgmt.priorExamDecDate())
                    .pubDate(mgmt.pubDate())
                    .pubNo(mgmt.pubNo())
                    .abandonOrderDate(mgmt.abandonOrderDate())
                    .abandonDate(mgmt.abandonDate())
                    .abandonNote(mgmt.abandonNote());
        }

        // 9. 등록/권리유지_관리 (DesignAppMaintenance)
        if (request.appMaintenance() != null) {
            var maint = request.appMaintenance();
            builder.multiDesign(String.valueOf(maint.multiDesign())) // 다의장(복수디자인) 정보
                    .kipoDelayDays(maint.kipoDelayDays() != null ? maint.kipoDelayDays() : 0)
                    .rightPeriod(maint.rightPeriod())
                    .isAnnuityManaged(maint.isAnnuityManaged())
                    .regDecisionDate(maint.regDecisionDate())
                    .regReceiptDate(maint.regReceiptDate())
                    .regNormalDeadline(maint.regNormalDeadline())
                    .regGraceDeadline(maint.regGraceDeadline())
                    .regDate(maint.regDate())
                    .regNo(maint.regNo())
                    .regAnnounceDate(maint.regAnnounceDate())
                    .regAnnounceNo(maint.regAnnounceNo())
                    .annuityYear(maint.annuityYear())
                    .standardDeadline(maint.standardDeadline())
                    .penaltyDeadline(maint.penaltyDeadline())
                    .recoveryDeadline(maint.recoveryDeadline())
                    .annuityOrderDate(maint.annuityOrderDate())
                    .annuityAgency(maint.annuityAgency());

            if (maint.regReductionRate() != null) {
                builder.regReductionRateCode(maint.regReductionRate().code());
                builder.regReductionRateName(maint.regReductionRate().codeName());
            }
            if (maint.annuityReducRate() != null) {
                builder.annuityReducRateCode(maint.annuityReducRate().code());
                builder.annuityReducRateName(maint.annuityReducRate().codeName());
            }
        }

        // 10. 물품류 (GoodsClass) - 🌟 디자인 전용
        if (request.goodsClass() != null) {
            builder.goodsClass(request.goodsClass().goodsClass());
        }

        // 11. 디자인 설명/요약 (DesignDescription) - 🌟 디자인 전용
        if (request.designDescription() != null) {
            builder.designDescription(request.designDescription().designDescription())
                    .designSummary(request.designDescription().designSummary());
        }

        // 12. 비고 (AppNote)
        if (request.appNote() != null) {
            builder.note(request.appNote().note());
        }

        return builder.build();
    }

    private CommonAppVO buildCommonAppVO(DomesticTrademarkRequest.CreateTrademarkAppRequest request, String officeSeq, String loginUser) {

        // 1. 공통 및 기본 식별자 세팅
        CommonAppVO.CommonAppVOBuilder<?, ?> builder = CommonAppVO.builder()
                .officeSeq(officeSeq)
                .createUser(loginUser)
                .updateUser(loginUser)
                // 수정 시 appSeq 사용, 신규 시 채번용 문자열 세팅
                .appSeq(request.appSeq() != null && !request.appSeq().isBlank() ? request.appSeq() : "utb_app_mst");

        // 2. 출원 사건관리 (TrademarkAppCaseInfo)
        if (request.appCaseMng() != null) {
            var mng = request.appCaseMng();
            if (mng.category() != null) {
                builder.categoryCode(mng.category().code());
                builder.categoryName(mng.category().codeName());
            }
            if (mng.rightType() != null) {
                builder.rightTypeCode(mng.rightType().code());
                builder.rightTypeName(mng.rightType().codeName());
            }
            if (mng.appType() != null) {
                builder.appTypeCode(mng.appType().code());
                builder.appTypeName(mng.appType().codeName());
            }
            if (mng.appCategory() != null) {
                builder.appCategoryCode(mng.appCategory().code());
                builder.appCategoryName(mng.appCategory().codeName());
            }
            builder.receiptDate(mng.receiptDate())
                    .ourRef(mng.ourRef())
                    .yourRef(mng.yourRef())
                    .clientRef(mng.clientRef())
                    .draftDeadline(mng.draftDeadline())
                    .draftSendDate(mng.draftSendDate());
        }

        // 3. 출원기본정보 (TrademarkAppBaseInfo)
        if (request.appBaseInfo() != null) {
            var base = request.appBaseInfo();
            builder.appOrderDate(base.appOrderDate())
                    .appDeadline(base.appDeadline())
                    .appDate(base.appDate())
                    .appNo(base.appNo());
        }

        // 4. 담당 정보 (TrademarkAppManagerInfo)
        if (request.appManagerInfo() != null) {
            var mgr = request.appManagerInfo();
            builder.deptName(mgr.deptCode());
            if (mgr.adminMgrInfo() != null) {
                builder.adminMgr(mgr.adminMgrInfo().userSeq());
                builder.adminMgrNm(mgr.adminMgrInfo().userName());
            }
            if (mgr.caseMgrInfo() != null) {
                builder.caseMgr(mgr.caseMgrInfo().userSeq());
                builder.caseMgrNm(mgr.caseMgrInfo().userName());
            }
            if (mgr.attorneyInfo() != null) {
                builder.attorney(mgr.attorneyInfo().userSeq());
                builder.attorneyNm(mgr.attorneyInfo().userName());
            }
        }

        // 5. 당사자 정보 (TrademarkAppCounterPartyInfo)
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();
            if (party.clientContactInfo() != null) {
                builder.clientContact(party.clientContactInfo().userSeq());
                builder.clientContactNm(party.clientContactInfo().userName());
            }
        }

        // 6. 물품류 (GoodsClass) - 🌟 상표/디자인 핵심
        if (request.goodsClass() != null) {
            builder.goodsClass(request.goodsClass().goodsClass());
        }

        // 7. 명칭 정보 (TrademarkAppNameInfo)
        if (request.appNameInfo() != null) {
            var nameInfo = request.appNameInfo();
            builder.titleKo(nameInfo.titleKo())
                    .titleEn(nameInfo.titleEn());
        }

        // 8. 출원 전략설정 (TrademarkAppStrategy)
        if (request.appStrategy() != null) {
            var strat = request.appStrategy();
            builder.isForeignApp(strat.isForeignApp())
                    .foreign6mDeadline(strat.foreign6mDeadline())
                    .foreignAppDate(strat.foreignAppDate())
                    .productClassAppNo(strat.classificAppNo()); // DB 저장 컬럼(goods_app_no/product_class_app_no) 매핑용

            if (strat.foreignAppTiming() != null) {
                builder.foreignAppTimingCode(strat.foreignAppTiming().code());
                builder.foreignAppTimingName(strat.foreignAppTiming().codeName());
            }
            if (strat.originalAppInfo() != null) {
                builder.originalAppDate(strat.originalAppInfo().originalAppDate())
                        .originalAppNo(strat.originalAppInfo().originalAppNo());
            }
            if (strat.reAppInfo() != null) {
                builder.reAppDate(strat.reAppInfo().reAppDate())
                        .reAppNo(strat.reAppInfo().reAppNo());
            }
            if (strat.originalRegInfo() != null) {
                builder.originalRegDate(strat.originalRegInfo().originalRegDate())
                        .originalRegNo(strat.originalRegInfo().originalRegNo());
            }
            // 🌟 상표 전용: 마드리드 국제등록 정보
            if (strat.madridAppInfo() != null) {
                builder.madridAppDate(strat.madridAppInfo().madridAppDate())
                        .madridAppNo(strat.madridAppInfo().madridAppNo());
            }
        }

        // 9. 출원 행정관리 (TrademarkAppManagement)
        if (request.appManagement() != null) {
            var mgmt = request.appManagement();
            builder.isPoaSubmitted(mgmt.isPoaSubmitted())
                    .isTrademarkResearch(mgmt.isTrademarkResearch()) // 상표 전용: 조사 여부
                    .priorExamReqDate(mgmt.priorExamReqDate())
                    .priorExamDecDate(mgmt.priorExamDecDate())
                    .announcementDecisionDate(mgmt.announcementDecisionDate()) // 상표 전용: 공고결정일
                    .announcementDate(mgmt.announcementDate())
                    .announcementNo(mgmt.announcementNo())
                    .abandonOrderDate(mgmt.abandonOrderDate())
                    .abandonDate(mgmt.abandonDate())
                    .abandonNote(mgmt.abandonNote());
        }

        // 10. 등록/권리유지 관리 (TrademarkAppMaintenance)
        if (request.appMaintenance() != null) {
            var maint = request.appMaintenance();
            builder.regDecisionDate(maint.regDecisionDate())
                    .regReceiptDate(maint.regReceiptDate())
                    .regDate(maint.regDate())
                    .regNo(maint.regNo())
                    .regAnnounceDate(maint.regAnnounceDate())
                    .regAnnounceNo(maint.regAnnounceNo())
                    .standardDeadline(maint.standardDeadline())
                    .penaltyDeadline(maint.penaltyDeadline())
                    .annuityOrderDate(maint.annuityOrderDate())
                    .annuityAgency(maint.annuityAgency())
                    .priorityDate(maint.priorityDate()) // 상표 전용: 기연일
                    .regNormalDeadline(maint.regNormalDeadline())
                    .annuityYear(maint.annuityYear())
                    .isRenewalManaged(maint.isRenewalManaged()) // 상표 전용: 갱신관리여부
                    .nextPaymentInstallment(maint.nextPaymentInstallment()) // 상표 전용: 차기납부차수
                    .trademarkRenewalFee(maint.trademarkRenewalFee())     // 상표 전용: 갱신료
                    .renewalLateFee(maint.renewalLateFee());               // 상표 전용: 갱신과태료
        }

        // 11. 비고 (AppNote)
        if (request.appNote() != null) {
            builder.note(request.appNote().note());
        }

        return builder.build();
    }









    /**
     * AppMstVO(DB) -> DomesticAppMergeVO(화면) 변환
     *//*
    public void fillMasterToMergeVO(DomesticAppMergeVO mergeVO, CommonAppVO mst) {
        if (mst == null) return;

        // 1. 기본 식별자
        mergeVO.setAppSeq(mst.getAppSeq());
        mergeVO.setOfficeSeq(mst.getOfficeSeq());
        mergeVO.setAppNo(mst.getAppNo());
        mergeVO.setAccessCode(mst.getAccessCode());
        mergeVO.setStatusCode(mst.getStateCode());

        // 2. 사건 관리 정보
        mergeVO.setOurRef(mst.getAssetNo());
        mergeVO.setYourRef(mst.getAgentRef());
        mergeVO.setClientRef(mst.getRetainSeq());
        mergeVO.setAppLanguageCode(mst.getAppLanguageCode());
        mergeVO.setAppLanguageName(mst.getAppLanguageName());

        mergeVO.setAppRouteCode(mst.getAppRouteCode());
        mergeVO.setAppRouteName(mst.getAppRouteName());
        mergeVO.setCategoryCode(mst.getAppClassificationCode());
        mergeVO.setCategoryName(mst.getAppClassificationName());
        mergeVO.setRightTypeCode(mst.getRightCategoryCode());
        mergeVO.setRightTypeName(mst.getRightCategoryName());
        mergeVO.setAppTypeCode(mst.getAppKindCode());
        mergeVO.setAppTypeName(mst.getAppKindName());
        mergeVO.setAppCategoryCode(mst.getAppCategoryCode());
        mergeVO.setAppCategoryName(mst.getAppCategoryName());

        mergeVO.setDeptName(mst.getDeptCode());

        // 3. 명칭 및 물품 정보
        mergeVO.setTitleKo(mst.getAppNameKo());
        mergeVO.setTitleEn(mst.getAppNameEn());
        mergeVO.setProposal(mst.getProposalName());
        mergeVO.setGoodsClass(mst.getProductClass());
        mergeVO.setEtcTitle(mst.getEtcTitle());

        // 4. 명세서 정보
        mergeVO.setGradeCode(mst.getGradeCode());
        mergeVO.setGradeName(mst.getGradeName());
        mergeVO.setIndependentClaims(mst.getIndependentClaim());
        mergeVO.setDependentClaims(mst.getDependentClaim());
        mergeVO.setSpecPage(mst.getSpecPage());
        mergeVO.setFigureCount(mst.getFigureCount());
        mergeVO.setDrawingCount(mst.getDrawingPaperCount());

        // 5. 출원 전략/관계 정보
        mergeVO.setClassificAppNo(mst.getGoodsAppNo());
        mergeVO.setFirstAppNo(mst.getFirstAppNo());
        mergeVO.setOriginalAppNo(mst.getOriginalAppNo());
        mergeVO.setOriginalRegNo(mst.getOriginalRegNo());
        mergeVO.setReAppNo(mst.getReAppNo());
        mergeVO.setDualAppNo(mst.getDoubleAppNo());
        mergeVO.setGlobalAppNo(mst.getGlobalAppNo());
        mergeVO.setMadridAppNo(mst.getMadridNo());
        mergeVO.setIsForeignApp(mst.getExternalAppYn());
        mergeVO.setForeignAppTimingCode(mst.getForeignAppTimingCode());
        mergeVO.setForeignAppTimingName(mst.getForeignAppTimingName());

        // 6. 행정 및 공개/공고 정보
        mergeVO.setIpcClassification(mst.getIpcCategoryCode());
        mergeVO.setPubNo(mst.getOpenNo());             // pubNo <- openNo
        mergeVO.setAnnouncementNo(mst.getPublicNo());  // announcementNo <- publicNo
        mergeVO.setAbandonNote(mst.getGiveUpContent());
        mergeVO.setIsPartialDesign(mst.getIsPartialDesign());
        mergeVO.setMadridAppNo(mst.getMadridNo());

        // Y/N 여부 필드
        mergeVO.setIsTrademarkResearch(mst.getTrademarkResearchYn());
        mergeVO.setHasDomesticPriority(mst.getInteriorPreferenceAssertYn());
        mergeVO.setIsPoaSubmitted(mst.getMandatePaperSubmitYn());

        // 7. 등록 및 유지관리 정보
        mergeVO.setRegNo(mst.getRegNo());
        mergeVO.setRegAnnounceNo(mst.getRegPublicNo());
        mergeVO.setFinalClaimsCount(mst.getUltiDependentClaimCount());
        mergeVO.setRegReductionRateCode(mst.getRegReductionRateCode());
        mergeVO.setRegReductionRateName(mst.getRegReductionRateName());
        mergeVO.setAnnuityReducRateCode(mst.getAnnuityReducRateCode());
        mergeVO.setAnnuityReducRateName(mst.getAnnuityReducRateName());
        mergeVO.setMultiDesign(mst.getMultiDesign());

        // 연차/갱신 관련
        mergeVO.setNextPaymentInstallment(mst.getNextPaymentInstallment());
        mergeVO.setAnnuityYear(mst.getAnnuityYear());
        mergeVO.setAnnuityReducRateCode(mst.getAnnuityReducRateCode());
        mergeVO.setIsAnnuityManaged(mst.getYearCntManagementYn());
        mergeVO.setIsRenewalManaged(mst.getRenewalManagementYn());
        mergeVO.setTrademarkRenewalFee(mst.getTrademarkRenewalFee());
        mergeVO.setRenewalLateFee(mst.getRenewalLateFee());
        mergeVO.setAnnuityAgency(mst.getOutsourcingCorpName());

        // 8. 기타
        mergeVO.setGoodsClass(mst.getProductClass());
        mergeVO.setNote(mst.getNote());

        // 9. 탭 내용
        // 특허/실용신안 정보
        mergeVO.setSummary(mst.getSummary());
        mergeVO.setClaimScope(mst.getClaimScope());
        // 디자인 정보
        mergeVO.setDesignDescription(mst.getDesignDescription());
        mergeVO.setDesignSummary(mst.getDesignSummary());

        // 10. baseVO
        mergeVO.setCreateUser(mst.getCreateUser());
        mergeVO.setCreateAt(mst.getCreateAt());
        mergeVO.setUpdateUser(mst.getUpdateUser());
        mergeVO.setUpdateAt(mst.getUpdateAt());

        // 11. 관계자 정보
        mergeVO.setClientName(mst.getClientNm());

        mergeVO.setApplicantName(mst.getApplicantNm());

//        mergeVO.setApplicantContactSeq(mst.getApplicantContact());
//        mergeVO.setApplicantContactName(mst.getApplicantContactNm());

        mergeVO.setInventorSeq(mst.getInventor());
        mergeVO.setInventorName(mst.getInventorNm());

        mergeVO.setRegMgrName(mst.getRegMgrNm());

//        mergeVO.setAppManagerSeq(mst.getAppManager());
//        mergeVO.setAppManagerName(mst.getAppManagerNm());

        mergeVO.setClientContactSeq(mst.getClientContact());
        mergeVO.setClientContactName(mst.getClientContactNm());

        mergeVO.setAdminMgrSeq(mst.getAdminMgr());
        mergeVO.setAdminMgrName(mst.getAdminMgrNm());

        mergeVO.setCaseMgrSeq(mst.getCaseMgr());
        mergeVO.setCaseMgrName(mst.getCaseMgrNm());

        mergeVO.setAttorneySeq(mst.getAttorney());
        mergeVO.setAttorneyName(mst.getAttorneyNm());

//        mergeVO.setForeignAgentSeq(mst.getForeignAgent());
//        mergeVO.setForeignAgentName(mst.getForeignAgentNm());

//        mergeVO.setForeignClientSeq(mst.getForeignClient());
//        mergeVO.setForeignClientName(mst.getForeignClientNm());


        // 기일정보
        // ==========================================
        // [기본]
        // ==========================================
        mergeVO.setAppOrderDate(formatMinusHoursString8(mst.getAppOrderDate()));
        mergeVO.setAppDeadline(formatMinusHoursString8(mst.getAppDeadline()));
        mergeVO.setAppDate(formatMinusHoursString8(mst.getAppDate()));
        mergeVO.setReceiptDate(formatMinusHoursString8(mst.getReceiptDate()));
        mergeVO.setTransDeadline(formatMinusHoursString8(mst.getTransDeadline()));
        mergeVO.setTransSubmitDate(formatMinusHoursString8(mst.getTransSubmitDate()));

        // ==========================================
        // [사건]
        // ==========================================
        mergeVO.setInventionReportDate(formatMinusHoursString8(mst.getInventionReportDate()));
        mergeVO.setDraftDeadline(formatMinusHoursString8(mst.getDraftDeadline()));
        mergeVO.setDraftSendDate(formatMinusHoursString8(mst.getDraftSendDate()));


        // ==========================================
        // [전략]
        // ==========================================
        mergeVO.setFirstAppDate(formatMinusHoursString8(mst.getFirstAppDate()));
        mergeVO.setOriginalAppDate(formatMinusHoursString8(mst.getOriginalAppDate()));
        mergeVO.setOriginalRegDate(formatMinusHoursString8(mst.getOriginalRegDate()));
        mergeVO.setReAppDate(formatMinusHoursString8(mst.getReAppDate()));
        mergeVO.setGlobalAppDate(formatMinusHoursString8(mst.getGlobalAppDate()));
        // --- 추가 항목 ---
        mergeVO.setDualAppDate(formatMinusHoursString8(mst.getDualAppDate()));
        mergeVO.setForeign6mDeadline(formatMinusHoursString8(mst.getForeign6mDeadline()));
        mergeVO.setForeign1yDeadline(formatMinusHoursString8(mst.getForeign1yDeadline()));
        mergeVO.setClaimsNoticeDate(formatMinusHoursString8(mst.getClaimsNoticeDate()));
        mergeVO.setClaimsDeadline(formatMinusHoursString8(mst.getClaimsDeadline()));
        mergeVO.setClaimsSubmitDate(formatMinusHoursString8(mst.getClaimsSubmitDate()));
        mergeVO.setForeignAppDate(formatMinusHoursString8(mst.getForeignAppDate()));
        mergeVO.setMadridAppDate(formatMinusHoursString8(mst.getMadridAppDate()));


        // ==========================================
        // [행정]
        // ==========================================
        mergeVO.setExamRequestDeadline(formatMinusHoursString8(mst.getExamRequestDeadline()));
        mergeVO.setExamRequestDate(formatMinusHoursString8(mst.getExamRequestDate()));
        mergeVO.setPubDate(formatMinusHoursString8(mst.getPubDate()));
        mergeVO.setAnnouncementDate(formatMinusHoursString8(mst.getAnnouncementDate()));
        mergeVO.setAnnouncementDecisionDate(formatMinusHoursString8(mst.getAnnouncementDecisionDate()));
        // --- 추가 항목 ---
        mergeVO.setEarlyPubRequestDate(formatMinusHoursString8(mst.getEarlyPubRequestDate()));
        mergeVO.setDomesticPriorDeadline(formatMinusHoursString8(mst.getDomesticPriorDeadline()));
        mergeVO.setDomesticPriorDate(formatMinusHoursString8(mst.getDomesticPriorDate()));
        mergeVO.setPriorExamReqDate(formatMinusHoursString8(mst.getPriorExamReqDate()));
        mergeVO.setPriorExamDecDate(formatMinusHoursString8(mst.getPriorExamDecDate()));


        // ==========================================
        // [포기/취하 관련]
        // ==========================================
        mergeVO.setAbandonOrderDate(formatMinusHoursString8(mst.getAbandonOrderDate()));
        mergeVO.setAbandonDate(formatMinusHoursString8(mst.getAbandonDate()));


        // ==========================================
        // [등록]
        // ==========================================
        mergeVO.setKipoDelayDays(formatMinusHoursString8(mst.getKipoDelayDays()));
        mergeVO.setRightPeriod(formatMinusHoursString8(mst.getRightPeriod()));
        mergeVO.setRegDecisionDate(formatMinusHoursString8(mst.getRegDecisionDate()));
        mergeVO.setRegReceiptDate(formatMinusHoursString8(mst.getRegReceiptDate()));
        mergeVO.setRegNormalDeadline(formatMinusHoursString8(mst.getRegNormalDeadline()));
        mergeVO.setRegGraceDeadline(formatMinusHoursString8(mst.getRegGraceDeadline()));
        mergeVO.setRegDate(formatMinusHoursString8(mst.getRegDate()));
        mergeVO.setRegAnnounceDate(formatMinusHoursString8(mst.getRegAnnounceDate()));
        // --- 추가 항목 ---
        mergeVO.setPriorityDate(formatMinusHoursString8(mst.getPriorityDate()));


        // ==========================================
        // [연차료/국내등록/기타]
        // ==========================================
        mergeVO.setAnnuityOrderDate(formatMinusHoursString8(mst.getAnnuityOrderDate()));
        mergeVO.setStandardDeadline(formatMinusHoursString8(mst.getStandardDeadline()));
        mergeVO.setPenaltyDeadline(formatMinusHoursString8(mst.getPenaltyDeadline()));
        mergeVO.setRecoveryDeadline(formatMinusHoursString8(mst.getRecoveryDeadline()));
    }*/

    @Override
    @Transactional
    public void deleteAppImageFile(String appSeq, String fileSeq) {
        log.info(">>> [국내출원] 이미지 논리 삭제 요청 - appSeq={}, fileSeq={}", appSeq, fileSeq);
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();
        // 파일은 designSeq/trademarkSeq/patentSeq에 연결되어 있으므로 tblSeq 없이 fileSeq만으로 삭제
        paperMapper.softDeleteByFileSeq(officeSeq, fileSeq, loginUser);
    }

    private void fileUpload(String appSeq, String officeSeq, String user, String docSeq, MultipartFile file, String category) {
        PaperRequestVO paperVO = PaperRequestVO.builder()
                .officeSeq(officeSeq)
                .tblSeq(appSeq)
                .file(file)
                .docSeq(docSeq)
                .fileCategoryCode(category)
                .createUser(user)
                .build();

        paperService.saveFileMapping(paperVO);
    }
}
