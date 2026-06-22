package kr.co.mindpro.ipms.domain.patentApp.overseaApp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.ai.service.RagService;
import kr.co.mindpro.ipms.domain.customer.repository.db1.CustomerMapper;
import kr.co.mindpro.ipms.domain.customer.service.CustomerService;
import kr.co.mindpro.ipms.domain.customer.vo.CustomerMappVO;
import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import kr.co.mindpro.ipms.domain.paper.repository.db1.PaperMapper;
import kr.co.mindpro.ipms.domain.paper.service.PaperService;
import kr.co.mindpro.ipms.domain.history.service.HistoryService;
import kr.co.mindpro.ipms.domain.paper.vo.PaperRequestVO;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.participant.service.ParticipantService;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.repository.db1.AppCommonMapper;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.util.AppStatusUtil;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.repository.db1.DomesticAppMapper;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.*;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaIndividualDesignAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaIndividualHardIpAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaIndividualTrademarkAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaIndividualDesignAppResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaIndividualHardIpAppResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaIndividualTrademarkAppResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.repository.db1.OverseaAppMapper;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.service.OverseaIndividualService;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.*;

/**
 * @author : seokho
 * @fileName : IndividualAppServiceImpl.java
 * @description : 해외 출원 서비스 구현체 (국내 출원 서비스 구조와 동일하게 리팩토링)
 * @since : 2026. 1. 29.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndividualAppServiceImpl implements OverseaIndividualService {

    private final DomesticAppMapper domesticAppMapper;
    private final OverseaAppMapper overseaAppMapper;

    private final ParticipantService participantService;
    private final DueDateService dueDateService;
    private final PaperMapper paperMapper;
    private final PaperService paperService;
    private final CustomerService customerService;
    private final HistoryService historyService;
    private final RagService ragService;

    private final AppCommonMapper appCommonMapper;
    private final CustomerMapper customerMapper;

    private final ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOverseaHardIpApp(OverseaIndividualHardIpAppRequest.CreateHardIpRequest request, MultipartFile file) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 수정 시 프론트에서 appSeq 전달
        String appSeq = request.appSeq();
        int result;

        // 🌟 [변경] MergeVO 걷어내고 바로 CommonAppVO 빌드
        CommonAppVO newAppVO = buildCommonAppVO(request, officeSeq, loginUser);

        // todo app_route 강제 세팅 (추후 데이터 정제 후 삭제)
        newAppVO.setCategoryCode("30"); // 해외로 세팅
        newAppVO.setAppRouteCode("20");          // 개별국으로 세팅

        // 특허(10), 실용신안(20) 값이 유효하게 들어왔는지 확인하는 로직.
        String rightType = newAppVO.getRightTypeCode();

        if ("10".equals(rightType) || "20".equals(rightType)) {
            newAppVO.setRightTypeCode(rightType);
        } else {
            log.error(">>> [ERROR] 개국출원 특허/실용신안에 유효하지 않은 권리 구분 코드 유입: {}", rightType);
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 요청된 값에 따라 상태값 지정.
        AppStatusUtil.calculateAndSetAppState(newAppVO);

        // 중복되는 출원키가 있는지 확인 (업데이트/인서트 분기)
        if (appSeq == null || appSeq.isBlank()) {
            result = 0;
        } else {
            result = appCommonMapper.checkDuplicateSeq(officeSeq, appSeq);
        }

        CommonAppVO oldAppVO = null;
        if (result > 0) {
            // ==========================================
            // [업데이트 로직] 출원키 중복 시
            // ==========================================

            // 1. 기존데이터 조회 (oldAppVO)
            oldAppVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            // 2. 권리별 json 스냅샷 생성 및 하부 테이블 업데이트 (🌟 newAppVO 전달)
            String snapShot = processSnapshotAndUpdate(oldAppVO, newAppVO, officeSeq, appSeq, loginUser);
            oldAppVO.setRightSnapshot(snapShot);

            // 3. 히스토리 테이블에 기존데이터 인서트
            result = appCommonMapper.insertAppHistory(oldAppVO);
            if (result == 0) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 4. 마스터 정보 업데이트 (최신 정보인 newAppVO로 업데이트)
            result = overseaAppMapper.updateOverseaAppMst(newAppVO, appSeq, officeSeq, loginUser);
            if (result == 0) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 파일 업로드 (수정 시)
            if (file != null && !file.isEmpty()) {
                fileUpload(oldAppVO.getPatentSeq(), officeSeq, loginUser, "105", file, "appPatentFile");
            }

            log.info(">>> 해외출원 개별국가 정보가 성공적으로 업데이트되었습니다. appSeq: {}", appSeq);

        } else {
            // ==========================================
            // [인서트 로직] 중복된 출원키가 없을 때
            // ==========================================

            result = overseaAppMapper.insertOverseaApp(newAppVO);

            if (result <= 0) {
                log.error(">>> [ERROR] 해외출원 개별국가 마스터 정보 저장 실패! (Return: 0)");
                throw new RuntimeException("해외출원 개별국가 정보(Master) 저장에 실패했습니다.");
            }

            // 🌟 해외출원 전용 맵핑 로직 (appExtSeq 체인 연동)
            if (newAppVO.getAppExtSeq() != null) {
                result = overseaAppMapper.insertBasicChainMap(newAppVO);
                if (result <= 0) {
                    log.error(">>> [ERROR] 해외기본 멥핑 정보 저장 실패! (Return: 0)");
                    throw new RuntimeException("해외기본 맵핑 정보 저장에 실패했습니다.");
                } else {
                    log.info(">>> 해외 기본에 연결되었습니다. : {}", newAppVO.getExtMappSeq());
                }
            }

            appSeq = newAppVO.getAppSeq();

            // 🌟 [변경] 하위 테이블(특허 상세 등) 인서트 (newAppVO 전달)
            insertPatentApp(appSeq, newAppVO, file);

            log.info(">>> 해외출원 개별국가 정보가 신규 생성되었습니다. appSeq: {}", appSeq);
        }

        // ==========================================
        // [공통] 당사자, 관계자 및 기일 정보 저장
        // ==========================================
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();

            if (party.clientInfo() != null && !party.clientInfo().isEmpty()) {
                customerMappToWork(party.clientInfo(), appSeq, "client");
            }
            if (party.applicantInfo() != null && !party.applicantInfo().isEmpty()) {
                customerMappToWork(party.applicantInfo(), appSeq, "applicant");
            }
            // 🌟 해외 개별국은 해외대리인(foreignAgent) 정보가 핵심!
            if (party.foreignAgentInfo() != null && !party.foreignAgentInfo().isEmpty()) {
                customerMappToWork(party.foreignAgentInfo(), appSeq, "foreignAgent");
            }
            if (party.regMgrInfo() != null && !party.regMgrInfo().isEmpty()) {
                customerMappToWork(party.regMgrInfo(), appSeq, "regMgr");
            }
            // [추가] 발명자 — 단일 PersonInfo → List<CounterPartyInfo> 변환 후 utb_customer_mapp 저장
            if (party.inventorInfo() != null
                    && party.inventorInfo().userSeq() != null
                    && !party.inventorInfo().userSeq().isBlank()) {
                customerMappToWork(
                        java.util.List.of(kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse.CounterPartyInfo.builder()
                                .counterPartySeq(party.inventorInfo().userSeq())
                                .counterPartyName(party.inventorInfo().userName())
                                .build()),
                        appSeq, "inventor"
                );
            }
        }

        // 관계자 정보 저장 (newAppVO 전달)
        List<ParticipantVO> participants = getParticipantList(appSeq, newAppVO);
        if (!participants.isEmpty()) {
            participantService.saveAllParticipants(participants);
        }

        // 기일 정보 저장 (newAppVO 전달)
        List<DueDateVO> dueDates = getDueDateList(appSeq, newAppVO);
        if (!dueDates.isEmpty()) {
            dueDateService.saveAllDueDates(dueDates);
        }

        if (oldAppVO != null) {
            CommonAppVO latestVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
            historyService.compareAndLog(appSeq, "해외출원 개별국/직접출원 특허 정보 수정", oldAppVO, latestVO);
        }

        // AI Vector Sync
        ragService.syncVectorData(SecurityUtil.getOfficeSeq(), "OVERSEA_APP", appSeq, "해외 개국 - 특허/실용신안 출원", this.getOverseaHardIpAppDetail(appSeq));

        return appSeq;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOverseaDesignApp(OverseaIndividualDesignAppRequest.CreateDesignAppRequest request, MultipartFile file) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 수정 시 프론트에서 appSeq 전달
        String appSeq = request.appSeq();
        int result;

        // [변경] MergeVO 걷어내고 바로 CommonAppVO 빌드
        CommonAppVO newAppVO = buildCommonAppVO(request, officeSeq, loginUser);

        // todo app_route 강제 세팅 (추후 삭제 예정)
        newAppVO.setCategoryCode("30"); // 해외로 세팅
        newAppVO.setAppRouteCode("20");          // 개별국으로 세팅

        String rightType = newAppVO.getRightTypeCode();

        if ("30".equals(rightType)) {
            newAppVO.setRightTypeCode(rightType);
        } else {
            log.error(">>> [ERROR] 개국출원 디자인에 유효하지 않은 권리 구분 코드 유입: {}", rightType);
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 요청된 값에 따라 상태값 지정.
        AppStatusUtil.calculateAndSetAppState(newAppVO);

        // 중복되는 출원키가 있는지 확인 (업데이트 vs 인서트 분기)
        if (appSeq == null || appSeq.isBlank()) {
            result = 0;
        } else {
            result = appCommonMapper.checkDuplicateSeq(officeSeq, appSeq);
        }

        CommonAppVO oldAppVO = null;
        if (result > 0) {
            // ==========================================
            // [업데이트 로직] 출원키 중복 시
            // ==========================================

            // 1. 기존데이터 조회 (oldAppVO)
            oldAppVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            // 2. 권리별 json 스냅샷 생성 및 하부 테이블 업데이트 (🌟 newAppVO 전달)
            String snapShot = processSnapshotAndUpdate(oldAppVO, newAppVO, officeSeq, appSeq, loginUser);
            oldAppVO.setRightSnapshot(snapShot);

            // 3. 히스토리 테이블에 기존데이터 인서트
            result = appCommonMapper.insertAppHistory(oldAppVO);
            if (result == 0) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 4. 마스터 정보 업데이트
            result = overseaAppMapper.updateOverseaAppMst(newAppVO, appSeq, officeSeq, loginUser);
            if (result == 0) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 파일 업로드 (수정 시)
            if (file != null && !file.isEmpty()) {
                fileUpload(oldAppVO.getDesignSeq(), officeSeq, loginUser, "112", file, "appPatentFile");
            }

            log.info(">>> 해외출원 개별국가(디자인) 정보가 성공적으로 업데이트되었습니다. appSeq: {}", appSeq);

        } else {
            // ==========================================
            // [인서트 로직] 중복된 출원키가 없을 때
            // ==========================================

            result = overseaAppMapper.insertOverseaApp(newAppVO);

            if (result <= 0) {
                log.error(">>> [ERROR] 해외출원 개별국가(디자인) 마스터 정보 저장 실패! (Return: 0)");
                throw new RuntimeException("해외출원 개별국가(디자인) 정보(Master) 저장에 실패했습니다.");
            }
            // 해외출원 전용 맵핑 로직 (appExtSeq 체인 연동)
            if (newAppVO.getAppExtSeq() != null) {
                result = overseaAppMapper.insertBasicChainMap(newAppVO);
                if (result <= 0) {
                    log.error(">>> [ERROR] 해외기본 멥핑 정보 저장 실패! (Return: 0)");
                    throw new RuntimeException("해외기본 맵핑 정보 저장에 실패했습니다.");
                } else {
                    log.info(">>> 해외 기본에 연결되었습니다. : {}", newAppVO.getExtMappSeq());
                }
            }

            appSeq = newAppVO.getAppSeq();

            // [변경] 하위 디자인 테이블 인서트 (newAppVO 전달)
            insertDesignApp(appSeq, newAppVO, file);

            log.info(">>> 해외출원 개별국가(디자인) 정보가 신규 생성되었습니다. appSeq: {}", appSeq);
        }

        // ==========================================
        // [공통] 관계자 및 기일 정보 저장
        // ==========================================
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();

            if (party.foreignAgentInfo() != null && !party.foreignAgentInfo().isEmpty()) {
                customerMappToWork(party.foreignAgentInfo(), appSeq, "foreignAgent");
            }
            if (party.clientInfo() != null && !party.clientInfo().isEmpty()) {
                customerMappToWork(party.clientInfo(), appSeq, "client");
            }
            if (party.applicantInfo() != null && !party.applicantInfo().isEmpty()) {
                customerMappToWork(party.applicantInfo(), appSeq, "applicant");
            }
            if (party.regMgrInfo() != null && !party.regMgrInfo().isEmpty()) {
                customerMappToWork(party.regMgrInfo(), appSeq, "regMgr");
            }
            // [추가] 발명자 — 디자인 도메인도 발명자 매핑 추가
            if (party.inventorInfo() != null
                    && party.inventorInfo().userSeq() != null
                    && !party.inventorInfo().userSeq().isBlank()) {
                customerMappToWork(
                        java.util.List.of(kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse.CounterPartyInfo.builder()
                                .counterPartySeq(party.inventorInfo().userSeq())
                                .counterPartyName(party.inventorInfo().userName())
                                .build()),
                        appSeq, "inventor"
                );
            }
        }

        // 관계자 정보 저장 (newAppVO 전달)
        List<ParticipantVO> participants = getParticipantList(appSeq, newAppVO);
        if (!participants.isEmpty()) {
            participantService.saveAllParticipants(participants);
        }

        // 기일 정보 저장 (newAppVO 전달)
        List<DueDateVO> dueDates = getDueDateList(appSeq, newAppVO);
        if (!dueDates.isEmpty()) {
            dueDateService.saveAllDueDates(dueDates);
        }

        if (oldAppVO != null) {
            CommonAppVO latestVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
            historyService.compareAndLog(appSeq, "해외출원 개별국/직접출원 디자인 정보 수정", oldAppVO, latestVO);
        }

        // AI Vector Sync
        ragService.syncVectorData(SecurityUtil.getOfficeSeq(), "OVERSEA_APP", appSeq, "해외 개국 - 디자인 출원", this.getOverseaDesignAppDetail(appSeq));

        return appSeq;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOverseaTrademarkApp(OverseaIndividualTrademarkAppRequest.CreateTrademarkAppRequest request, MultipartFile file) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 수정 시 프론트에서 appSeq 전달
        String appSeq = request.appSeq();
        int result;

        // [변경] MergeVO 걷어내고 바로 CommonAppVO 빌드
        CommonAppVO newAppVO = buildCommonAppVO(request, officeSeq, loginUser);

        // todo app_route 강제 세팅 (추후 삭제 예정)
        newAppVO.setCategoryCode("30"); // 해외으로 세팅
        newAppVO.setAppRouteCode("20");          // 개별국으로 세팅

        String rightType = newAppVO.getRightTypeCode();

        if ("40".equals(rightType)) {
            newAppVO.setRightTypeCode(rightType);
        } else {
            log.error(">>> [ERROR] 개국출원 상표에 유효하지 않은 권리 구분 코드 유입: {}", rightType);
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 요청된 값에 따라 상태값 지정.
        AppStatusUtil.calculateAndSetAppState(newAppVO);

        // 중복되는 출원키가 있는지 확인 (업데이트 vs 인서트 분기)
        if (appSeq == null || appSeq.isBlank()) {
            result = 0;
        } else {
            result = appCommonMapper.checkDuplicateSeq(officeSeq, appSeq);
        }

        CommonAppVO oldAppVO = null;
        if (result > 0) {
            // ==========================================
            // [업데이트 로직] 출원키 중복 시
            // ==========================================

            // 1. 기존데이터 조회 (oldAppVO)
            oldAppVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            // 2. 권리별 json 스냅샷 생성 및 하부 테이블 업데이트 (🌟 newAppVO 전달)
            String snapShot = processSnapshotAndUpdate(oldAppVO, newAppVO, officeSeq, appSeq, loginUser);
            oldAppVO.setRightSnapshot(snapShot);

            // 3. 히스토리 테이블에 기존데이터 인서트
            result = appCommonMapper.insertAppHistory(oldAppVO);
            if (result == 0) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 4. 마스터 정보 업데이트
            result = overseaAppMapper.updateOverseaAppMst(newAppVO, appSeq, officeSeq, loginUser);
            if (result == 0) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 파일 업로드 (수정 시)
            if (file != null && !file.isEmpty()) {
                fileUpload(oldAppVO.getTrademarkSeq(), officeSeq, loginUser, "129", file, "appPatentFile");
            }

            log.info(">>> 해외출원 개별국가(상표) 정보가 성공적으로 업데이트되었습니다. appSeq: {}", appSeq);

        } else {
            // ==========================================
            // [인서트 로직] 중복된 출원키가 없을 때
            // ==========================================

            result = overseaAppMapper.insertOverseaApp(newAppVO);

            if (result <= 0) {
                log.error(">>> [ERROR] 해외출원 개별국가(상표) 마스터 정보 저장 실패! (Return: 0)");
                throw new RuntimeException("해외출원 개별국가(상표) 정보(Master) 저장에 실패했습니다.");
            }

            // 해외출원 전용 맵핑 로직 (appExtSeq 체인 연동)
            if (newAppVO.getAppExtSeq() != null) {
                result = overseaAppMapper.insertBasicChainMap(newAppVO);
                if (result <= 0) {
                    log.error(">>> [ERROR] 해외기본 멥핑 정보 저장 실패! (Return: 0)");
                    throw new RuntimeException("해외기본 맵핑 정보 저장에 실패했습니다.");
                } else {
                    log.info(">>> 해외 기본에 연결되었습니다. : {}", newAppVO.getExtMappSeq());
                }
            }

            appSeq = newAppVO.getAppSeq();

            // [변경] 하위 상표 테이블 인서트 (newAppVO 전달)
            insertTrademarkApp(appSeq, newAppVO, file);

            log.info(">>> 해외출원 개별국가(상표) 정보가 신규 생성되었습니다. appSeq: {}", appSeq);
        }

        // ==========================================
        // [공통] 당사자, 관계자 및 기일 정보 저장
        // ==========================================
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();

            if (party.foreignAgentInfo() != null && !party.foreignAgentInfo().isEmpty()) {
                customerMappToWork(party.foreignAgentInfo(), appSeq, "foreignAgent");
            }
            if (party.clientInfo() != null && !party.clientInfo().isEmpty()) {
                customerMappToWork(party.clientInfo(), appSeq, "client");
            }
            if (party.applicantInfo() != null && !party.applicantInfo().isEmpty()) {
                customerMappToWork(party.applicantInfo(), appSeq, "applicant");
            }
            if (party.regMgrInfo() != null && !party.regMgrInfo().isEmpty()) {
                customerMappToWork(party.regMgrInfo(), appSeq, "regMgr");
            }
        }

        // 관계자 정보 저장 (newAppVO 전달)
        List<ParticipantVO> participants = getParticipantList(appSeq, newAppVO);
        if (!participants.isEmpty()) {
            participantService.saveAllParticipants(participants);
        }

        // 기일 정보 저장 (newAppVO 전달)
        List<DueDateVO> dueDates = getDueDateList(appSeq, newAppVO);
        if (!dueDates.isEmpty()) {
            dueDateService.saveAllDueDates(dueDates);
        }

        if (oldAppVO != null) {
            CommonAppVO latestVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
            historyService.compareAndLog(appSeq, "해외출원 개별국/직접출원 상표 정보 수정", oldAppVO, latestVO);
        }

        // AI Vector Sync
        ragService.syncVectorData(SecurityUtil.getOfficeSeq(), "OVERSEA_APP", appSeq, "해외 개국 - 상표 출원", this.getOverseaTrademarkAppDetail(appSeq));

        return appSeq;
    }

    // 각 권리별 데이터 snapShot 생성 및 신규 데이터 softDelete & 인서트
    public String processSnapshotAndUpdate(CommonAppVO oldAppVO, CommonAppVO newAppVO, String officeSeq, String appSeq, String loginUser) {
        int result;
        String snapShot;

        try {
            switch (oldAppVO.getRightTypeCode()) {
                case "10", "20" -> {
                    AppPatentVO historyPatentVO = new AppPatentVO();

                    historyPatentVO.setOfficeSeq(officeSeq);
                    historyPatentVO.setAppSeq(appSeq);
                    historyPatentVO.setPatentSeq(oldAppVO.getPatentSeq());

                    historyPatentVO.setSummary(oldAppVO.getSummary());
                    historyPatentVO.setClaimScope(oldAppVO.getClaimScope());

                    historyPatentVO.setCreateUser(oldAppVO.getCreateUser());
                    historyPatentVO.setCreateAt(oldAppVO.getCreateAt());
                    historyPatentVO.setUpdateUser(oldAppVO.getUpdateUser());

                    snapShot = objectMapper.writeValueAsString(historyPatentVO);

                    // 새 정보 저장 로직
                    AppPatentVO newPatentVO = new AppPatentVO();
                    newPatentVO.setOfficeSeq(officeSeq);
                    newPatentVO.setAppSeq(appSeq);
                    newPatentVO.setPatentSeq(oldAppVO.getPatentSeq()); // 시퀀스는 기존꺼 유지

                    newPatentVO.setSummary(newAppVO.getSummary());       // 새 데이터!
                    newPatentVO.setClaimScope(newAppVO.getClaimScope()); // 새 데이터!

                    newPatentVO.setUpdateUser(loginUser);

                    result = domesticAppMapper.updateHardIpApp(newPatentVO);

                    if (result == 0) {
                        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                }
                case "30" -> {
                    AppDesignVO historyDesignVO = new AppDesignVO();

                    historyDesignVO.setDesignSeq(oldAppVO.getDesignSeq());
                    historyDesignVO.setDesignSummary(oldAppVO.getDesignSummary());
                    historyDesignVO.setDesignDescription(oldAppVO.getDesignDescription());
                    historyDesignVO.setMultiDesign(oldAppVO.getMultiDesign());
                    historyDesignVO.setIsPartialDesign(oldAppVO.getIsPartialDesign());

                    snapShot = objectMapper.writeValueAsString(historyDesignVO);

                    // 새 정보 저장 로직
                    AppDesignVO newDesignVO = new AppDesignVO();

                    newDesignVO.setOfficeSeq(officeSeq);
                    newDesignVO.setAppSeq(appSeq);
                    newDesignVO.setDesignSeq(oldAppVO.getDesignSeq());

                    newDesignVO.setDesignSummary(newAppVO.getDesignSummary());
                    newDesignVO.setDesignDescription(newAppVO.getDesignDescription());
                    newDesignVO.setMultiDesign(newAppVO.getMultiDesign());
                    newDesignVO.setIsPartialDesign(newAppVO.getIsPartialDesign());

                    newDesignVO.setUpdateUser(loginUser);

                    result = domesticAppMapper.updateDesignApp(newDesignVO);

                    if (result == 0) {
                        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                }
                case "40" -> {
                    AppTrademarkVO historyTrademarkVO = new AppTrademarkVO();

                    historyTrademarkVO.setTrademarkSeq(oldAppVO.getTrademarkSeq());
                    historyTrademarkVO.setMadridAppNo(oldAppVO.getMadridAppNo());
                    historyTrademarkVO.setClassificAppNo(oldAppVO.getClassificAppNo());

                    snapShot = objectMapper.writeValueAsString(historyTrademarkVO);

                    // 새 정보 저장 로직
                    AppTrademarkVO newTradeVO = AppTrademarkVO.builder()
                            .officeSeq(officeSeq)
                            .appSeq(appSeq)
                            .trademarkSeq(oldAppVO.getTrademarkSeq())
                            .madridAppNo(newAppVO.getMadridAppNo())
                            .classificAppNo(newAppVO.getClassificAppNo())
                            .updateUser(loginUser)
                            .build();

                    result = domesticAppMapper.updateTrademarkApp(newTradeVO);

                    if (result == 0) {
                        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
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

    // 1. 해외 개별국 특허/실용신안 상세 조회
    @Override
    public OverseaIndividualHardIpAppResponse.HardIpAppDetailResponse getOverseaHardIpAppDetail(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // 마스터 정보 조회 (CommonAppVO)
        CommonAppVO mstVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 🌟 [변경] MergeVO 생성 및 fillMaster... 로직 제거
        // 당사자 정보 직접 조회 (개별국은 해외대리인 foreignAgent가 핵심)
        List<CommonRecordResponse.CounterPartyInfo> clientList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "client");
        List<CommonRecordResponse.CounterPartyInfo> applicantList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "applicant");
        List<CommonRecordResponse.CounterPartyInfo> foreignAgentList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "foreignAgent");
        List<CommonRecordResponse.CounterPartyInfo> regMgrList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "regMgr");
        // [추가] 발명자도 utb_customer_mapp 의 'inventor' 카테고리에서 조회 — mstVO 에 임시 주입 (단일)
        List<CommonRecordResponse.CounterPartyInfo> inventorListHi = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "inventor");
        if (!inventorListHi.isEmpty()) {
            var inv = inventorListHi.get(0);
            mstVO.setInventor(inv.counterPartySeq());
            mstVO.setInventorNm(inv.counterPartyName());
        }

        // 파일 리스트 조회 (특허/실용신안)
        List<PaperResponseVO> fileList = paperMapper.findAllByWork(mstVO.getPatentSeq(), officeSeq);

        // 🌟 [변경] fromVOViewPatent 호출 (mstVO와 리스트들을 직접 전달)
        return OverseaIndividualHardIpAppResponse.HardIpAppDetailResponse.fromVOViewPatent(mstVO, clientList, applicantList, foreignAgentList, regMgrList, fileList);
    }

    // 2. 해외 개별국 디자인 상세 조회
    @Override
    public OverseaIndividualDesignAppResponse.DesignAppDetailResponse getOverseaDesignAppDetail(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // 마스터 정보 조회
        CommonAppVO mstVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 당사자 정보 직접 조회
        List<CommonRecordResponse.CounterPartyInfo> clientList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "client");
        List<CommonRecordResponse.CounterPartyInfo> applicantList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "applicant");
        List<CommonRecordResponse.CounterPartyInfo> foreignAgentList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "foreignAgent");
        List<CommonRecordResponse.CounterPartyInfo> regMgrList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "regMgr");
        // [추가] 발명자 — utb_customer_mapp 'inventor' 카테고리에서 조회 + mstVO 주입
        List<CommonRecordResponse.CounterPartyInfo> inventorListDsg = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "inventor");
        if (!inventorListDsg.isEmpty()) {
            var inv = inventorListDsg.get(0);
            mstVO.setInventor(inv.counterPartySeq());
            mstVO.setInventorNm(inv.counterPartyName());
        }

        // 파일 리스트 조회 (디자인)
        List<PaperResponseVO> fileList = paperMapper.findAllByWork(mstVO.getDesignSeq(), officeSeq);

        // 🌟 [변경] fromVOViewDesign 호출
        return OverseaIndividualDesignAppResponse.DesignAppDetailResponse.fromVOViewDesign(mstVO, clientList, applicantList, foreignAgentList, regMgrList, fileList);
    }

    // 3. 해외 개별국 상표 상세 조회
    @Override
    public OverseaIndividualTrademarkAppResponse.TrademarkAppDetailResponse getOverseaTrademarkAppDetail(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // 마스터 정보 조회
        CommonAppVO mstVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 당사자 정보 직접 조회
        List<CommonRecordResponse.CounterPartyInfo> clientList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "client");
        List<CommonRecordResponse.CounterPartyInfo> applicantList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "applicant");
        List<CommonRecordResponse.CounterPartyInfo> foreignAgentList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "foreignAgent");
        List<CommonRecordResponse.CounterPartyInfo> regMgrList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "regMgr");

        // 파일 리스트 조회 (상표)
        List<PaperResponseVO> fileList = paperMapper.findAllByWork(mstVO.getTrademarkSeq(), officeSeq);

        // 🌟 [변경] fromVOViewTrademark 호출
        return OverseaIndividualTrademarkAppResponse.TrademarkAppDetailResponse.fromVOViewTrademark(mstVO, clientList, applicantList, foreignAgentList, regMgrList, fileList);
    }

//    public void setCounterPartyInfo(OverseaAppMergeVO mergeVO, String tblSeq, String officeSeq) {
//
//        List<CommonRecordResponse.CounterPartyInfo> clientList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, tblSeq, "client");
//        List<CommonRecordResponse.CounterPartyInfo> applicantList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, tblSeq, "applicant");
//        List<CommonRecordResponse.CounterPartyInfo> regMgrList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, tblSeq, "regMgr");
//        List<CommonRecordResponse.CounterPartyInfo> foreignAgentList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, tblSeq, "foreignAgent");
//
//        mergeVO.setClientInfo(clientList);
//        mergeVO.setApplicantInfo(applicantList);
//        mergeVO.setRegMgrInfo(regMgrList);
//        mergeVO.setForeignAgentInfo(foreignAgentList);
//    }


    // =================================================================
    // DB 저장 관련 (Insert Logic)
    // =================================================================

    // 1. 특허/실용신안 상세 저장
    public void insertPatentApp(String appSeq, CommonAppVO appVO, MultipartFile file) {
        AppPatentVO patentVO = AppPatentVO.builder()
                .appSeq(appSeq)
                .officeSeq(appVO.getOfficeSeq())
                .appCategoryCode(appVO.getAppCategoryCode())
                .summary(appVO.getSummary())
                .claimScope(appVO.getClaimScope())
                .createUser(appVO.getCreateUser())
                .updateUser(appVO.getCreateUser())
                .note(appVO.getNote())
                .build();

        domesticAppMapper.insertPatentApp(patentVO);

        // 파일 업로드
        fileUpload(patentVO.getPatentSeq(), SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq(), "105", file, "appPatentFile");
    }

    // 2. 디자인 상세 저장
    public void insertDesignApp(String appSeq, CommonAppVO appVO, MultipartFile file) {
        AppDesignVO designVO = AppDesignVO.builder()
                .appSeq(appSeq)
                .officeSeq(appVO.getOfficeSeq())
                .createUser(appVO.getCreateUser())
                .updateUser(appVO.getCreateUser())
                .note(appVO.getNote())
                .build();

        domesticAppMapper.insertDesignApp(designVO);

        // 파일 업로드
        fileUpload(designVO.getDesignSeq(), SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq(), "112", file, "appPatentFile");
    }

    // 3. 상표 상세 저장
    public void insertTrademarkApp(String appSeq, CommonAppVO appVO, MultipartFile file) {
        AppTrademarkVO trademarkVO = AppTrademarkVO.builder()
                .appSeq(appSeq)
                .officeSeq(appVO.getOfficeSeq())
                .createUser(appVO.getCreateUser())
                .updateUser(appVO.getCreateUser())
                .note(appVO.getNote())
                .build();

        domesticAppMapper.insertTrademarkApp(trademarkVO);

        // 파일 업로드
        fileUpload(trademarkVO.getTrademarkSeq(), SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq(), "129", file, "appPatentFile");
    }

    /**
     * [Insert용] 관계자 리스트 생성
     */
    public List<ParticipantVO> getParticipantList(String appSeq, CommonAppVO appVO) {
        String officeSeq = appVO.getOfficeSeq();
        List<ParticipantVO> list = new ArrayList<>();

        // 내부 담당자
        addParticipantIfPresent(list, "appManager", appVO.getAppManager(), appSeq, officeSeq);
        addParticipantIfPresent(list, "adminMgr", appVO.getAdminMgr(), appSeq, officeSeq);
        addParticipantIfPresent(list, "caseMgr", appVO.getCaseMgr(), appSeq, officeSeq);
        addParticipantIfPresent(list, "attorney", appVO.getAttorney(), appSeq, officeSeq);

        // 외부 당사자
        addParticipantIfPresent(list, "clientContact", appVO.getClientContact(), appSeq, officeSeq);
        addParticipantIfPresent(list, "applicantContact", appVO.getApplicantContact(), appSeq, officeSeq);
        addParticipantIfPresent(list, "inventor", appVO.getInventor(), appSeq, officeSeq);

        // 해외 전용
//        addParticipantIfPresent(list, "foreignClient", mergeVO.getForeignClientSeq(), appSeq, officeSeq);

        return list;
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

    /**
     * [Insert용] 기일 리스트 생성
     */
    public List<DueDateVO> getDueDateList(String appSeq, CommonAppVO appVO) {
        String officeSeq = appVO.getOfficeSeq();
        List<DueDateVO> list = new ArrayList<>();

        // 사건 관리
        // 접수일, 출원완료일, 출원지시일, 출원마감일, 오더발송일, 출원일
        addDueDateIfPresent(list, "receiptDate", appVO.getReceiptDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "appCompleteDate", appVO.getAppCompleteDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "appOrderDate", appVO.getAppOrderDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "appDeadline", appVO.getAppDeadline(), appSeq, officeSeq);
        addDueDateIfPresent(list, "oaDeliveryDate", appVO.getOaDeliveryDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "appDate", appVO.getAppDate(), appSeq, officeSeq);

        // 전략/관계
        // 가출원일, 최초출원일, 원출원일, 원등록일, 재출원일, 국제출원일
        addDueDateIfPresent(list, "provisionalAppDate", appVO.getProvisionalAppDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "firstAppDate", appVO.getFirstAppDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "originalAppDate", appVO.getOriginalAppDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "originalRegDate", appVO.getOriginalRegDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "reAppDate", appVO.getReAppDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "globalAppDate", appVO.getGlobalAppDate(), appSeq, officeSeq);

        // 행정/심사
        // 심사청구마감일, 심사청구지시일, 심사청구일, 출원공개일, 출원공고일, 포기지시일, 포기일
        addDueDateIfPresent(list, "examRequestDeadline", appVO.getExamRequestDeadline(), appSeq, officeSeq);
        addDueDateIfPresent(list, "examRequestOrderDate", appVO.getExamRequestOrderDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "examRequestDate", appVO.getExamRequestDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "pubDate", appVO.getPubDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "announcementDate", appVO.getAnnouncementDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "announcementDecisionDate", appVO.getAnnouncementDecisionDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "abandonOrderDate", appVO.getAbandonOrderDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "abandonDate", appVO.getAbandonDate(), appSeq, officeSeq);

        // 등록/권리
        // 특허청지연일, 권리존속기간, 등록결정일, 등록접수일, 등록-정상마감
//        addDueDateIfPresent(list, "kipoDelayDays", appVO.getKipoDelayDays(), appSeq, officeSeq);
        addDueDateIfPresent(list, "rightPeriod", appVO.getRightPeriod(), appSeq, officeSeq);
        addDueDateIfPresent(list, "regDecisionDate", appVO.getRegDecisionDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "regReceiptDate", appVO.getRegReceiptDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "regNormalDeadline", appVO.getRegNormalDeadline(), appSeq, officeSeq);
        // 등록-과태마감, 등록지시일, 등록납부일, 등록일, 등록 공고일
        addDueDateIfPresent(list, "regGraceDeadline", appVO.getRegGraceDeadline(), appSeq, officeSeq);
        addDueDateIfPresent(list, "regOrderDate", appVO.getRegOrderDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "regPaymentDate", appVO.getRegPaymentDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "regDate", appVO.getRegDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "regAnnounceDate", appVO.getRegAnnounceDate(), appSeq, officeSeq);
        // 관리위임일, 마감관리 - 정상마감일, 마감관리 - 과태마감일
        addDueDateIfPresent(list, "annuityOrderDate", appVO.getAnnuityOrderDate(), appSeq, officeSeq);
        addDueDateIfPresent(list, "standardDeadline", appVO.getStandardDeadline(), appSeq, officeSeq);
        addDueDateIfPresent(list, "penaltyDeadline", appVO.getPenaltyDeadline(), appSeq, officeSeq);
        addDueDateIfPresent(list, "renewalDeadline", appVO.getRenewalDeadline(), appSeq, officeSeq);

        // 기타 - 모등록일
        addDueDateIfPresent(list, "parentRegAppDate", appVO.getParentRegAppDate(), appSeq, officeSeq);

        return list;
    }

    public void addDueDateIfPresent(List<DueDateVO> list, String code, String dateStr, String appSeq, String officeSeq) {
        if (dateStr != null && !dateStr.isEmpty()) {
            list.add(DueDateVO.builder()
                    .duedateCategoryCode(code)
                    .duedateDate(parseToOffsetDateTime(dateStr))
                    .officeSeq(officeSeq)
                    .tblSeq(appSeq)
                    .build());
        }
    }





    /**
     * [리팩토링] 해외 개별국 특허 Request -> CommonAppVO 다이렉트 변환 (Full Version)
     */
    private CommonAppVO buildCommonAppVO(OverseaIndividualHardIpAppRequest.CreateHardIpRequest request, String officeSeq, String loginUser) {

        CommonAppVO.CommonAppVOBuilder<?, ?> builder = CommonAppVO.builder()
                .officeSeq(officeSeq)
                .createUser(loginUser)
                .updateUser(loginUser)
                .appSeq(request.appSeq() != null && !request.appSeq().isBlank() ? request.appSeq() : "utb_app_mst")
                .appExtSeq(request.appExtSeq());

        // 1. 사건관리 (HardIpAppCaseMng)
        if (request.appCaseMng() != null) {
            var mng = request.appCaseMng();
            if (mng.appRoute() != null) builder.appRouteCode(mng.appRoute().code());
            if (mng.rightType() != null) builder.rightTypeCode(mng.rightType().code());
            if (mng.appCategory() != null) builder.appCategoryCode(mng.appCategory().code());
            if (mng.appCountryInfo() != null) {
                builder.countryCode(mng.appCountryInfo().code());
                builder.countryName(mng.appCountryInfo().codeName());
            }
            builder.ourRef(mng.ourRef()).yourRef(mng.yourRef()).clientRef(mng.clientRef()).receiptDate(mng.receiptDate());
        }

        // 2. 기본정보 (HardIpAppBaseInfo)
        if (request.appBaseInfo() != null) {
            var base = request.appBaseInfo();
            builder.appOrderDate(base.appOrderDate()).appDeadline(base.appDeadline())
                    .oaDeliveryDate(base.oaDeliveryDate()).appDate(base.appDate()).appNo(base.appNo());
        }

        // 3. 담당 정보 (HardIpAppManagerInfo)
        if (request.appManagerInfo() != null) {
            var mgr = request.appManagerInfo();
            builder.deptName(mgr.deptCode());
            if (mgr.applicantContactInfo() != null) builder.applicantContact(mgr.applicantContactInfo().userSeq());
            if (mgr.adminMgrInfo() != null) builder.adminMgr(mgr.adminMgrInfo().userSeq());
            if (mgr.caseMgrInfo() != null) builder.caseMgr(mgr.caseMgrInfo().userSeq());
            if (mgr.attorneyInfo() != null) builder.attorney(mgr.attorneyInfo().userSeq());
        }

        // 4. 당사자 정보 (inventor)
        if (request.appCounterPartyInfo() != null && request.appCounterPartyInfo().inventorInfo() != null) {
            builder.inventor(request.appCounterPartyInfo().inventorInfo().userSeq())
                    .inventorNm(request.appCounterPartyInfo().inventorInfo().userName());
        }

        // 5. 명칭 정보
        if (request.appNameInfo() != null) {
            builder.titleKo(request.appNameInfo().titleKo()).titleEn(request.appNameInfo().titleEn());
        }

        // 6. 명세서 구성요소
        if (request.appSpecificElement() != null) {
            var spec = request.appSpecificElement();
            builder.independentClaims(spec.independentClaims()).dependentClaims(spec.dependentClaims())
                    .specPage(spec.specPage()).drawingCount(spec.drawingCount()).overseaSpecPage(spec.overseaSpecPage());
            if (spec.grade() != null) builder.gradeCode(spec.grade().code());
        }

        // 7. 전략설정 (Global, Provisional, First, Original, Re-App)
        if (request.appStrategy() != null) {
            var strat = request.appStrategy();
            if (strat.globalAppInfo() != null) {
                builder.globalAppDate(strat.globalAppInfo().globalAppDate()).globalAppNo(strat.globalAppInfo().globalAppNo());
            }
            if (strat.provisionalAppInfo() != null) {
                builder.provisionalAppDate(strat.provisionalAppInfo().provisionalAppDate())
                        .provisionalAppNo(strat.provisionalAppInfo().provisionalAppNo());
            }
            if (strat.firstAppInfo() != null) {
                builder.firstAppDate(strat.firstAppInfo().firstAppDate()).firstAppNo(strat.firstAppInfo().firstAppNo());
            }
            if (strat.originalAppInfo() != null) {
                builder.originalAppDate(strat.originalAppInfo().originalAppDate())
                        .originalAppNo(strat.originalAppInfo().originalAppNo());
            }
            if (strat.reAppInfo() != null) {
                builder.reAppDate(strat.reAppInfo().reAppDate()).reAppNo(strat.reAppInfo().reAppNo());
            }
        }

        // 8. 행정관리 (IPC, Parent, Exam Request, Pub/Announce, Abandon)
        if (request.appManagement() != null) {
            var mgmt = request.appManagement();
            builder.ipcClassification(mgmt.ipcClassification()).parentRegAppDate(mgmt.parentRegAppDate())
                    .examRequestDeadline(mgmt.examRequestDeadline()).examRequestOrderDate(mgmt.examRequestOrderDate())
                    .examRequestDate(mgmt.examRequestDate()).pubDate(mgmt.pubDate()).pubNo(mgmt.pubNo())
                    .announcementDate(mgmt.announcementDate()).announcementNo(mgmt.announcementNo())
                    .abandonOrderDate(mgmt.abandonOrderDate()).abandonDate(mgmt.abandonDate()).abandonNote(mgmt.abandonNote());
        }

        // 9. 유지관리 (Claim, Period, Reg Info, Annuity Info)
        if (request.appMaintenance() != null) {
            var maint = request.appMaintenance();
            builder.finalClaimsCount(maint.finalClaimCount())
                    .kipoDelayDays(maint.kipoDelayDays() != null ? maint.kipoDelayDays() : 0).rightPeriod(maint.rightPeriod()).isAnnuityManaged(maint.isAnnuityManaged())
                    .regDecisionDate(maint.regDecisionDate()).regReceiptDate(maint.regReceiptDate())
                    .regNormalDeadline(maint.regNormalDeadline()).regGraceDeadline(maint.regGraceDeadline())
                    .regOrderDate(maint.regOrderDate()).regPaymentDate(maint.regPaymentDate())
                    .regDate(maint.regDate()).regNo(maint.regNo())
                    .regAnnounceDate(maint.regAnnounceDate()).regAnnounceNo(maint.regAnnounceNo())
                    .nextPaymentInstallment(maint.nextPaymentInstallment())
                    .annuityOrderDate(maint.annuityOrderDate()).annuityAgency(maint.annuityAgency())
                    .standardDeadline(maint.standardDeadline()).penaltyDeadline(maint.penaltyDeadline());
        }

        // 10. 요약/청구 및 비고
        if (request.claimSummaryInfo() != null) {
            builder.summary(request.claimSummaryInfo().summary()).claimScope(request.claimSummaryInfo().claimScope());
        }
        if (request.appNote() != null) builder.note(request.appNote().note());

        return builder.build();
    }


    /**
     * [리팩토링] 해외 개별국 디자인 Request -> CommonAppVO 다이렉트 변환 (Full Version)
     */
    private CommonAppVO buildCommonAppVO(OverseaIndividualDesignAppRequest.CreateDesignAppRequest request, String officeSeq, String loginUser) {

        CommonAppVO.CommonAppVOBuilder<?, ?> builder = CommonAppVO.builder()
                .officeSeq(officeSeq)
                .createUser(loginUser)
                .updateUser(loginUser)
                .appSeq(request.appSeq() != null && !request.appSeq().isBlank() ? request.appSeq() : "utb_app_mst")
                .appExtSeq(request.appExtSeq());

        // 1. 사건관리 (DesignAppCaseMng)
        if (request.appCaseMng() != null) {
            var mng = request.appCaseMng();
            if (mng.appRoute() != null) builder.appRouteCode(mng.appRoute().code());
            if (mng.rightType() != null) builder.rightTypeCode(mng.rightType().code());
            if (mng.appCategory() != null) builder.appCategoryCode(mng.appCategory().code());
            if (mng.appCountryInfo() != null) {
                builder.countryCode(mng.appCountryInfo().code());
                builder.countryName(mng.appCountryInfo().codeName());
            }
            builder.ourRef(mng.ourRef()).yourRef(mng.yourRef()).clientRef(mng.clientRef()).receiptDate(mng.receiptDate());
        }

        // 2. 기본정보 (DesignAppBaseInfo)
        if (request.appBaseInfo() != null) {
            var base = request.appBaseInfo();
            builder.appDeadline(base.appDeadline())
                    .oaDeliveryDate(base.oaDeliveryDate())
                    .appDate(base.appDate())
                    .appNo(base.appNo());
            if (base.noticeExceptionApply() != null) {
                builder.noticeExceptionApplyCode(base.noticeExceptionApply().code());
            }
        }

        // 3. 담당 정보 (DesignAppManagerInfo)
        if (request.appManagerInfo() != null) {
            var mgr = request.appManagerInfo();
            builder.deptName(mgr.deptCode());
            if (mgr.adminMgrInfo() != null) builder.adminMgr(mgr.adminMgrInfo().userSeq());
            if (mgr.caseMgrInfo() != null) builder.caseMgr(mgr.caseMgrInfo().userSeq());
            if (mgr.attorneyInfo() != null) builder.attorney(mgr.attorneyInfo().userSeq());
        }

        // 4. 당사자 정보 (CounterPartyInfo)
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();
            // 발명자(창작자)
            if (party.inventorInfo() != null) {
                builder.inventor(party.inventorInfo().userSeq())
                        .inventorNm(party.inventorInfo().userName());
            }
            // 출원담당자 (applicantContact에 매핑)
            if (party.appManagerInfo() != null) {
                builder.appManager(party.appManagerInfo().userSeq())
                        .appManagerNm(party.appManagerInfo().userName());
            }
        }

        // 5. 명칭 정보
        if (request.appNameInfo() != null) {
            builder.titleKo(request.appNameInfo().titleKo())
                    .titleEn(request.appNameInfo().titleEn());
        }

        // 6. 전략 설정 (DesignAppStrategy)
        if (request.appStrategy() != null) {
            var strat = request.appStrategy();
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
            if (strat.originalRegInfo() != null) {
                builder.originalRegDate(strat.originalRegInfo().originalRegDate())
                        .originalRegNo(strat.originalRegInfo().originalRegNo());
            }
            builder.parentRegAppDate(strat.parentRegAppDate());
            builder.parentRegAppNo(strat.parentRegAppNo());
        }

        // 7. 행정관리 (DesignAppManagement)
        if (request.appManagement() != null) {
            var mgmt = request.appManagement();
            builder.pubDate(mgmt.pubDate())
                    .pubNo(mgmt.pubNo())
                    .abandonOrderDate(mgmt.abandonOrderDate())
                    .abandonDate(mgmt.abandonDate())
                    .abandonNote(mgmt.abandonNote());
        }

        // 8. 등록/유지관리 (DesignAppMaintenance)
        if (request.appMaintenance() != null) {
            var maint = request.appMaintenance();
            builder.kipoDelayDays(maint.kipoDelayDays() != null ? maint.kipoDelayDays() : 0)
                    .rightPeriod(maint.rightPeriod())
                    .isAnnuityManaged(maint.isAnnuityManaged())
                    .regDate(maint.regDate())
                    .regNo(maint.regNo())
                    .regAnnounceDate(maint.regAnnounceDate())
                    .regAnnounceNo(maint.regAnnounceNo())
                    .nextPaymentInstallment(maint.nextPaymentInstallment())
                    .annuityOrderDate(maint.annuityOrderDate())
                    .annuityAgency(maint.annuityAgency())
                    .standardDeadline(maint.standardDeadline())
                    .penaltyDeadline(maint.penaltyDeadline())
                    .goodsClass(maint.goodsClass() != null ? maint.goodsClass().goodsClass() : null)
                    .regDecisionDate(maint.regDecisionDate())
                    .regNormalDeadline(maint.regNormalDeadline())
                    .regGraceDeadline(maint.regGraceDeadline())
                    .regOrderDate(maint.regOrderDate())
                    .regPaymentDate(maint.regPaymentDate());
        }

        // 9. 디자인 설명/요점 (DesignDescription)
        if (request.designDescription() != null) {
            builder.designDescription(request.designDescription().designDescription())
                    .designSummary(request.designDescription().designSummary());
        }

        // 10. 비고
        if (request.appNote() != null) {
            builder.note(request.appNote().note());
        }

        return builder.build();
    }


    /**
     * [리팩토링] 해외 개별국 상표 Request -> CommonAppVO 다이렉트 변환 (Full Version)
     */
    private CommonAppVO buildCommonAppVO(OverseaIndividualTrademarkAppRequest.CreateTrademarkAppRequest request, String officeSeq, String loginUser) {

        CommonAppVO.CommonAppVOBuilder<?, ?> builder = CommonAppVO.builder()
                .officeSeq(officeSeq)
                .createUser(loginUser)
                .updateUser(loginUser)
                .appSeq(request.appSeq() != null && !request.appSeq().isBlank() ? request.appSeq() : "utb_app_mst")
                .appExtSeq(request.appExtSeq());

        // 1. 사건관리 (TrademarkAppCaseMng)
        if (request.appCaseMng() != null) {
            var mng = request.appCaseMng();
            if (mng.appRoute() != null) builder.appRouteCode(mng.appRoute().code());
            if (mng.rightType() != null) {
                builder.rightTypeCode(mng.rightType().code());
                builder.rightTypeName(mng.rightType().codeName());
            }
            if (mng.appCategory() != null) builder.appCategoryCode(mng.appCategory().code());
            if (mng.appCountryInfo() != null) {
                builder.countryCode(mng.appCountryInfo().code());
                builder.countryName(mng.appCountryInfo().codeName());
            }
            builder.ourRef(mng.ourRef()).yourRef(mng.yourRef()).clientRef(mng.clientRef()).receiptDate(mng.receiptDate());
        }

        // 2. 기본정보 (TrademarkAppBaseInfo) 및 원출원 정보
        if (request.appBaseInfo() != null) {
            var base = request.appBaseInfo();
            builder.appDeadline(base.appDeadline())
                    .oaDeliveryDate(base.oaDeliveryDate())
                    .appDate(base.appDate())
                    .appNo(base.appNo());

            if (base.noticeExceptionApply() != null) {
                builder.noticeExceptionApplyCode(base.noticeExceptionApply().code());
            }
            if (base.originalAppInfo() != null) {
                builder.originalAppDate(base.originalAppInfo().originalAppDate())
                        .originalAppNo(base.originalAppInfo().originalAppNo());
            }
        }

        // 3. 담당 정보 (TrademarkAppManagerInfo)
        if (request.appManagerInfo() != null) {
            var mgr = request.appManagerInfo();
            builder.deptName(mgr.deptCode());
            if (mgr.adminMgrInfo() != null) builder.adminMgr(mgr.adminMgrInfo().userSeq());
            if (mgr.caseMgrInfo() != null) builder.caseMgr(mgr.caseMgrInfo().userSeq());
            if (mgr.attorneyInfo() != null) builder.attorney(mgr.attorneyInfo().userSeq());
        }

        // 4. 당사자 정보 (CounterPartyInfo) - 출원담당자 매핑
        if (request.appCounterPartyInfo() != null && request.appCounterPartyInfo().appManagerInfo() != null) {
            builder.applicantContact(request.appCounterPartyInfo().appManagerInfo().userSeq())
                    .applicantContactNm(request.appCounterPartyInfo().appManagerInfo().userName());
            builder.appManager(request.appCounterPartyInfo().appManagerInfo().userSeq())
                    .appManagerNm(request.appCounterPartyInfo().appManagerInfo().userName());
        }

        // 5. 명칭 및 물품류
        if (request.appNameInfo() != null) {
            builder.titleKo(request.appNameInfo().titleKo())
                    .titleEn(request.appNameInfo().titleEn());
        }
        if (request.goodsClass() != null) {
            builder.goodsClass(request.goodsClass().goodsClass());
        }

        // 6. 전략 설정 (TrademarkAppStrategy)
        if (request.appStrategy() != null) {
            var strat = request.appStrategy();
            if (strat.originalAppInfo() != null) {
                builder.originalAppDate(strat.originalAppInfo().originalAppDate())
                        .originalAppNo(strat.originalAppInfo().originalAppNo());
            }
            if (strat.reAppInfo() != null) {
                builder.reAppDate(strat.reAppInfo().reAppDate())
                        .reAppNo(strat.reAppInfo().reAppNo());
            }
        }

        // 7. 행정관리 (TrademarkAppManagement) - 공고결정일 포함
        if (request.appManagement() != null) {
            var mgmt = request.appManagement();
            builder.announcementDate(mgmt.announcementDate())
                    .announcementNo(mgmt.announcementNo())
                    .announcementDecisionDate(mgmt.announcementDecisionDate()) // 공고결정일
                    .abandonOrderDate(mgmt.abandonOrderDate())
                    .abandonDate(mgmt.abandonDate())
                    .abandonNote(mgmt.abandonNote());
        }

        // 8. 등록/유지관리 (TrademarkAppMaintenance) - 갱신관리 여부 포함
        if (request.appMaintenance() != null) {
            var maint = request.appMaintenance();
            builder.regDecisionDate(maint.regDecisionDate())
                    .regNormalDeadline(maint.regNormalDeadline())
                    .regGraceDeadline(maint.regGraceDeadline())
                    .regOrderDate(maint.regOrderDate())
                    .regPaymentDate(maint.regPaymentDate())
                    .regDate(maint.regDate())
                    .regNo(maint.regNo())
                    .regAnnounceDate(maint.regAnnounceDate())
                    .regAnnounceNo(maint.regAnnounceNo())
                    .nextPaymentInstallment(maint.nextPaymentInstallment())
                    .annuityOrderDate(maint.annuityOrderDate())
                    .annuityAgency(maint.annuityAgency())
                    .standardDeadline(maint.standardDeadline())
                    .penaltyDeadline(maint.penaltyDeadline())
                    .isAnnuityManaged(maint.isRenewalManaged()) // 상표는 갱신관리 여부
                    .isRenewalManaged(maint.isRenewalManaged())
                    .renewalDeadline(maint.renewalDeadline());   // 갱신등록마감
        }

        // 9. 비고
        if (request.appNote() != null) {
            builder.note(request.appNote().note());
        }

        return builder.build();
    }


    // =================================================================
    // [Private Helper Methods] DB 조회 결과 -> MergeVO 매핑 (Select Logic)
    // =================================================================

    /**
     * MasterVO -> MergeVO (국내의 fillFromMaster 역할)
     *//*
    public void fillMasterToMergeVO(OverseaAppMergeVO mergeVO, CommonAppVO appVO) {
        if (appVO == null) return;

        // ---------------------------------------------------------
        // 1. 식별자 및 기본 분류
        // ---------------------------------------------------------
        mergeVO.setAppSeq(appVO.getAppSeq());
        mergeVO.setOfficeSeq(appVO.getOfficeSeq());
        mergeVO.setAppRouteCode(appVO.getAppRouteCode());           // 출원 루트
        mergeVO.setAppRouteName(appVO.getAppRouteName());
        mergeVO.setRightTypeCode(appVO.getRightCategoryCode());     // 권리 구분
        mergeVO.setRightTypeName(appVO.getRightCategoryName());
        mergeVO.setAppCategoryCode(appVO.getAppCategoryCode());     // 출원 구분
        mergeVO.setAppCategoryName(appVO.getAppCategoryName());
        mergeVO.setAppCountry(appVO.getCountryName());      // 국가명
        mergeVO.setAppCountryCode(appVO.getCountryCode());  // 국가코드
        mergeVO.setAppTypeCode(appVO.getAppKindCode());             // 출원 종류 (신규/진입 등)
//        mergeVO.setIsOversea(appVO.getIsOversea());         // 해외출원 여부

        // ---------------------------------------------------------
        // 2. 관리 번호 (Ref No)
        // ---------------------------------------------------------
        mergeVO.setOurRef(appVO.getAssetNo());              // OurRef
        mergeVO.setYourRef(appVO.getAgentRef());            // YourRef
        mergeVO.setClientRef(appVO.getRetainSeq());         // ClientRef
        mergeVO.setWipoRefNo(appVO.getWipoRefNo());         // WIPO 참조번호
        mergeVO.setAuthorityRefNo(appVO.getAuthorityRefNo()); // 특허청참조번호
        mergeVO.setAccessCode(appVO.getAccessCode());       // 접근 코드

        // ---------------------------------------------------------
        // 3. 명칭 및 물품
        // ---------------------------------------------------------
        mergeVO.setTitleKo(appVO.getAppNameKo());
        mergeVO.setTitleEn(appVO.getAppNameEn());
        mergeVO.setGoodsClass(appVO.getProductClass());     // 물품류

        // ---------------------------------------------------------
        // 4. 출원/공개/등록 번호 (Numbers)
        // ---------------------------------------------------------
        mergeVO.setAppNo(appVO.getAppNo());                 // 출원번호
        mergeVO.setPubNo(appVO.getOpenNo());                // 공개번호
        mergeVO.setAnnouncementNo(appVO.getPublicNo());     // 공고번호
        mergeVO.setRegNo(appVO.getRegNo());                 // 등록번호
        mergeVO.setRegAnnounceNo(appVO.getRegPublicNo());   // 등록공고번호

        mergeVO.setProvisionalAppNo(appVO.getProvisionalAppNo()); // 가출원번호
        mergeVO.setDivAppNo(appVO.getDivAppNo());           // 분할출원번호
        mergeVO.setIntlPubNo(appVO.getIntlPubNo());         // 국제공개번호
        mergeVO.setDomesticRegNo(appVO.getDomesticRegNo()); // 국내등록번호

        // ---------------------------------------------------------
        // 5. 패밀리/전략 번호 (Family)
        // ---------------------------------------------------------
        mergeVO.setFirstAppNo(appVO.getFirstAppNo());       // 최초출원번호
        mergeVO.setOriginalAppNo(appVO.getOriginalAppNo()); // 원출원번호
        mergeVO.setOriginalRegNo(appVO.getOriginalRegNo()); // 원등록번호
        mergeVO.setReAppNo(appVO.getReAppNo());             // 재출원번호
        mergeVO.setGlobalAppNo(appVO.getGlobalAppNo());     // 국제출원번호
        mergeVO.setParentRegAppNo(appVO.getParentRegAppNo()); // 모등록번호

        // ---------------------------------------------------------
        // 6. 명세서 및 도면 (이미지 포함)
        // ---------------------------------------------------------
        mergeVO.setGradeCode(appVO.getGradeCode());
        mergeVO.setIndependentClaims(appVO.getIndependentClaim());
        mergeVO.setDependentClaims(appVO.getDependentClaim());
        mergeVO.setFinalClaimCount(appVO.getUltiDependentClaimCount()); // 최종항수

        mergeVO.setSpecPage(appVO.getSpecPage());           // 국내 명세서 페이지
        mergeVO.setOverseaSpecPage(appVO.getOverseaSpecPage()); // 해외 명세서 페이지
        mergeVO.setDrawingCount(appVO.getDrawingPaperCount());  // 도면 수
        mergeVO.setMainImgFile(appVO.getMainDrawingFile());     // 대표도면 파일

        // ---------------------------------------------------------
        // 7. 텍스트 상세 (요약, 청구, 디자인 등)
        // ---------------------------------------------------------
        mergeVO.setSummary(appVO.getSummary());             // 요약
        mergeVO.setClaimScope(appVO.getClaimScope());       // 청구범위
        mergeVO.setDesignDescription(appVO.getDesignDescription()); // 디자인 설명
        mergeVO.setDesignSummary(appVO.getDesignSummary()); // 디자인 요약

        // ---------------------------------------------------------
        // 8. 행정 및 관리
        // ---------------------------------------------------------
        mergeVO.setIpcClassification(appVO.getIpcCategoryCode()); // IPC
        mergeVO.setAnnuityAgency(appVO.getOutsourcingCorpName()); // 위임 업체
        mergeVO.setAbandonNote(appVO.getGiveUpContent());         // 포기 내용
        mergeVO.setDeemedWithdrawalContent(appVO.getDeemedWithdrawalContent()); // 취하간주 내용

        mergeVO.setDeptName(appVO.getDeptCode());           // 부서
        mergeVO.setNote(appVO.getNote());                   // 비고
        mergeVO.setNoticeExceptionApplyCode(appVO.getNoticeExceptionApplyCode()); // 공지예외

        mergeVO.setPublicYn(appVO.getPublicYn());
        mergeVO.setDefermentMonthCount(appVO.getDefermentMonthCount());
        mergeVO.setPubDate(appVO.getPubDate());
        mergeVO.setPubNo(appVO.getPubNo());

        // ---------------------------------------------------------
        // 9. 관리 여부 및 비용
        // ---------------------------------------------------------
        mergeVO.setIsAnnuityManaged(appVO.getYearCntManagementYn());  // 연차관리여부
        mergeVO.setIsRenewalManaged(appVO.getRenewalManagementYn());  // 갱신관리여부
        mergeVO.setNextPaymentInstallment(appVO.getNextPaymentInstallment()); // 차기납부차수
        mergeVO.setPaymentInstallment(appVO.getPaymentInstallment());     // 갱신차수

        mergeVO.setRenewalDeadline(appVO.getRenewalDeadline());       // 갱신마감일 (AppappVO에 있는 유일한 마감일)
        mergeVO.setTrademarkRenewalFee(appVO.getTrademarkRenewalFee()); // 상표 갱신료
        mergeVO.setRenewalLateFee(appVO.getRenewalLateFee());         // 갱신 과태료

        mergeVO.setPublicYn(appVO.getPublicYn());
        mergeVO.setDefermentMonthCount(appVO.getDefermentMonthCount());
        mergeVO.setPubNo(appVO.getPubNo());

        // ---------------------------------------------------------
        // 10. 해외 전용 상세 (PCT, EP 등)
        // ---------------------------------------------------------
        mergeVO.setKrDesignationYn(appVO.getKrDesignationYn()); // KR 지정
        mergeVO.setSearchResult(appVO.getSearchResult());       // 국제조사결과
        mergeVO.setEpSearchResult(appVO.getEpSearchResult());   // EP 서치결과

        mergeVO.setComplete20Yn(appVO.getComplete20Yn());
        mergeVO.setApp20Country(appVO.getApp20Country());
        mergeVO.setComplete30Yn(appVO.getComplete30Yn());
        mergeVO.setApp30Country(appVO.getApp30Country());

        // ---------------------------------------------------------
        // 11. 국가 리스트 (String -> List 변환)
        // ---------------------------------------------------------
        mergeVO.setDesignated(stringToList(appVO.getDesignated())); // 지정국가
        mergeVO.setRegisteredStates(stringToList(appVO.getRegisteredStates())); // 등록국가
        mergeVO.setSubsequent(stringToList(appVO.getSubsequent()));           // 사후지정

        // ---------------------------------------------------------
        // 12. 시스템 정보
        // ---------------------------------------------------------
        mergeVO.setCreateUser(appVO.getCreateUser());
        mergeVO.setCreateAt(appVO.getCreateAt());
        mergeVO.setUpdateUser(appVO.getUpdateUser());
        mergeVO.setUpdateAt(appVO.getUpdateAt());

        // ---------------------------------------------------------
        // 13. 관계자 정보
        // ---------------------------------------------------------
//        mergeVO.setClientSeq(appVO.getClient());
        mergeVO.setClientName(appVO.getClientNm());

//        mergeVO.setApplicantSeq(appVO.getApplicant());
        mergeVO.setApplicantName(appVO.getApplicantNm());

        mergeVO.setApplicantContactSeq(appVO.getApplicantContact());
        mergeVO.setApplicantContactName(appVO.getApplicantContactNm());

        mergeVO.setInventorSeq(appVO.getInventor());
        mergeVO.setInventorName(appVO.getInventorNm());

//        mergeVO.setRegMgrSeq(appVO.getRegMgr());
        mergeVO.setRegMgrName(appVO.getRegMgrNm());

        mergeVO.setAppManagerSeq(appVO.getAppManager());
        mergeVO.setAppManagerName(appVO.getAppManagerNm());

        mergeVO.setClientContactSeq(appVO.getClient());
        mergeVO.setClientContactName(appVO.getClientNm());

        mergeVO.setAdminMgrSeq(appVO.getAdminMgr());
        mergeVO.setAdminMgrName(appVO.getAdminMgrNm());

        mergeVO.setCaseMgrSeq(appVO.getCaseMgr());
        mergeVO.setCaseMgrName(appVO.getCaseMgrNm());

        mergeVO.setAttorneySeq(appVO.getAttorney());
        mergeVO.setAttorneyName(appVO.getAttorneyNm());

//        mergeVO.setForeignAgentSeq(appVO.getForeignAgent());
        mergeVO.setForeignAgentName(appVO.getForeignAgentNm());

//        mergeVO.setForeignClientSeq(appVO.getForeignClient());
//        mergeVO.setForeignClientName(appVO.getForeignClientNm());


        // ---------------------------------------------------------
        // 14. 기일 정보
        // ---------------------------------------------------------

        // ==========================================
        // [기본]
        // ==========================================
        mergeVO.setAppOrderDate(formatMinusHoursString8(appVO.getAppOrderDate()));
        mergeVO.setAppDeadline(formatMinusHoursString8(appVO.getAppDeadline()));
        mergeVO.setOaDeliveryDate(formatMinusHoursString8(appVO.getOaDeliveryDate()));
        mergeVO.setAppDate(formatMinusHoursString8(appVO.getAppDate()));
        mergeVO.setReceiptDate(formatMinusHoursString8(appVO.getReceiptDate()));
        mergeVO.setAppCompleteDate(formatMinusHoursString8(appVO.getAppCompleteDate()));

        // ==========================================
        // [분할출원 관련]
        // ==========================================
        mergeVO.setDivDeadline(formatMinusHoursString8(appVO.getDivDeadline()));
        mergeVO.setDivAppDate(formatMinusHoursString8(appVO.getDivAppDate()));

        // ==========================================
        // [특허청/WIPO/헤이그 관련]
        // ==========================================
        mergeVO.setAutoProtectionDate(formatMinusHoursString8(appVO.getAutoProtectionDate()));
        mergeVO.setAuthoritySubmissionDate(formatMinusHoursString8(appVO.getAuthoritySubmissionDate()));
        mergeVO.setHagueDeliveryDate(formatMinusHoursString8(appVO.getHagueDeliveryDate()));

        // ==========================================
        // [전략]
        // ==========================================
        mergeVO.setProvisionalAppDate(formatMinusHoursString8(appVO.getProvisionalAppDate()));
        mergeVO.setFirstAppDate(formatMinusHoursString8(appVO.getFirstAppDate()));
        mergeVO.setOriginalAppDate(formatMinusHoursString8(appVO.getOriginalAppDate()));
        mergeVO.setOriginalRegDate(formatMinusHoursString8(appVO.getOriginalRegDate()));
        mergeVO.setReAppDate(formatMinusHoursString8(appVO.getReAppDate()));
        mergeVO.setGlobalAppDate(formatMinusHoursString8(appVO.getGlobalAppDate()));
        mergeVO.setParentRegAppDate(formatMinusHoursString8(appVO.getParentRegAppDate()));

        // ==========================================
        // [PCT 진입 마감/완료]
        // ==========================================
        mergeVO.setNpe20Deadline(formatMinusHoursString8(appVO.getNpe20Deadline()));
        mergeVO.setEntry20CompleteDate(formatMinusHoursString8(appVO.getEntry20CompleteDate()));
        mergeVO.setNpe30Deadline(formatMinusHoursString8(appVO.getNpe30Deadline()));
        mergeVO.setEntry30CompleteDate(formatMinusHoursString8(appVO.getEntry30CompleteDate()));

        // ==========================================
        // [PCT 수수료/조사/심사]
        // ==========================================
        mergeVO.setFilingFeeDeadline(formatMinusHoursString8(appVO.getFilingFeeDeadline()));
        mergeVO.setFilingFeePayDate(formatMinusHoursString8(appVO.getFilingFeePayDate()));
        mergeVO.setIsaReceiptDate(formatMinusHoursString8(appVO.getIsaReceiptDate()));
        mergeVO.setIsrReportDate(formatMinusHoursString8(appVO.getIsrReportDate()));
        mergeVO.setIpeDeadline(formatMinusHoursString8(appVO.getIpeDeadline()));
        mergeVO.setIpeRequestDate(formatMinusHoursString8(appVO.getIpeRequestDate()));
        mergeVO.setIpeReportDate(formatMinusHoursString8(appVO.getIpeReportDate()));
        mergeVO.setIntlReceiptDate(formatMinusHoursString8(appVO.getIntlReceiptDate()));
        mergeVO.setIntlPubDate(formatMinusHoursString8(appVO.getIntlPubDate()));

        // ==========================================
        // [행정]
        // ==========================================
        mergeVO.setExamRequestDeadline(formatMinusHoursString8(appVO.getExamRequestDeadline()));
        mergeVO.setExamRequestOrderDate(formatMinusHoursString8(appVO.getExamRequestOrderDate()));
        mergeVO.setExamRequestDate(formatMinusHoursString8(appVO.getExamRequestDate()));
        mergeVO.setPubDate(formatMinusHoursString8(appVO.getPubDate()));
        mergeVO.setAnnouncementDate(formatMinusHoursString8(appVO.getAnnouncementDate()));
        mergeVO.setAnnouncementDecisionDate(formatMinusHoursString8(appVO.getAnnouncementDecisionDate()));

        // ==========================================
        // [보정관련]
        // ==========================================
        mergeVO.setAmendNoticeDate(formatMinusHoursString8(appVO.getAmendNoticeDate()));
        mergeVO.setAmendDeadline(formatMinusHoursString8(appVO.getAmendDeadline()));
        mergeVO.setAmendSubmitDate(formatMinusHoursString8(appVO.getAmendSubmitDate()));
        mergeVO.setClaimAmendDate(formatMinusHoursString8(appVO.getClaimAmendDate()));

        // ==========================================
        // [포기/취하 관련]
        // ==========================================
        mergeVO.setAbandonOrderDate(formatMinusHoursString8(appVO.getAbandonOrderDate()));
        mergeVO.setAbandonReceiptDate(formatMinusHoursString8(appVO.getAbandonReceiptDate()));
        mergeVO.setAbandonDate(formatMinusHoursString8(appVO.getAbandonDate()));
        mergeVO.setDeemedWithdrawalReceiptDate(formatMinusHoursString8(appVO.getDeemedWithdrawalReceiptDate()));
        mergeVO.setDeemedWithdrawalDate(formatMinusHoursString8(appVO.getDeemedWithdrawalDate()));

        // ==========================================
        // [등록]
        // ==========================================
        mergeVO.setKipoDelayDays(formatMinusHoursString8(appVO.getKipoDelayDays()));
        mergeVO.setRightPeriod(formatMinusHoursString8(appVO.getRightPeriod()));
        mergeVO.setRegDecisionDate(formatMinusHoursString8(appVO.getRegDecisionDate()));
        mergeVO.setRegReceiptDate(formatMinusHoursString8(appVO.getRegReceiptDate()));
        mergeVO.setRegNormalDeadline(formatMinusHoursString8(appVO.getRegNormalDeadline()));
        mergeVO.setRegGraceDeadline(formatMinusHoursString8(appVO.getRegGraceDeadline()));
        mergeVO.setRegOrderDate(formatMinusHoursString8(appVO.getRegOrderDate()));
        mergeVO.setRegPaymentDate(formatMinusHoursString8(appVO.getRegPaymentDate()));
        mergeVO.setRegDate(formatMinusHoursString8(appVO.getRegDate()));
        mergeVO.setRegAnnounceDate(formatMinusHoursString8(appVO.getRegAnnounceDate()));
        mergeVO.setRenewalDeadline(formatMinusHoursString8(appVO.getRenewalDeadline()));

        // ==========================================
        // [EP(유럽) 등록결정 관련]
        // ==========================================
        mergeVO.setEpAnnouncementDate(formatMinusHoursString8(appVO.getEpAnnouncementDate()));
        mergeVO.setSearchReceiptDate(formatMinusHoursString8(appVO.getSearchReceiptDate()));
        mergeVO.setSearchReportDate(formatMinusHoursString8(appVO.getSearchReportDate()));

        // ==========================================
        // [연차료/국내등록/기타]
        // ==========================================
        mergeVO.setAnnuityOrderDate(formatMinusHoursString8(appVO.getAnnuityOrderDate()));
        mergeVO.setStandardDeadline(formatMinusHoursString8(appVO.getStandardDeadline()));
        mergeVO.setPenaltyDeadline(formatMinusHoursString8(appVO.getPenaltyDeadline()));
        mergeVO.setDomesticRegDate(formatMinusHoursString8(appVO.getDomesticRegDate()));
        mergeVO.setProtectionStartDate(formatMinusHoursString8(appVO.getProtectionStartDate()));
    }*/

    /**
     * MergeVO -> AppMstVO 변환
     *//*
    public CommonAppVO buildAppMstVOFromMerge(OverseaAppMergeVO mergeVO) {
        return CommonAppVO.builder()
                // 사무소_식별자, 생성자, 수정자
                .officeSeq(mergeVO.getOfficeSeq())
                .appExtSeq(mergeVO.getAppExtSeq())
                .createUser(mergeVO.getCreateUser())
                .updateUser(mergeVO.getCreateUser())
                // .appSeq("utb_app_mst") // Mapper에서 selectKey로 생성하므로 주석 or null

                // 의뢰_식별자(출원인 관리번호)
                .retainSeq(mergeVO.getClientRef())

                // [기본 정보]
                // 권리_구분, 해외 출원 루트, 국가코드, 출원구분, 출원종류
                .rightCategoryCode(mergeVO.getRightTypeCode())
                .appClassificationCode("20")
                .appRouteCode(mergeVO.getAppRouteCode())
                .countryCode(mergeVO.getAppCountryCode())
                .appCategoryCode(mergeVO.getAppCategoryCode())
                .appKindCode(mergeVO.getAppTypeCode())
                .noticeExceptionApplyCode(mergeVO.getNoticeExceptionApplyCode())

                // [명칭 및 물품]
                // 출원_이름_한글, 출원_이름_영어, 물품류
                .appNameKo(mergeVO.getTitleKo())
                .appNameEn(mergeVO.getTitleEn())
                .productClass(mergeVO.getGoodsClass())

                // [번호 정보]
                // ourRef, yourRef
                .assetNo(mergeVO.getOurRef())
                .agentRef(mergeVO.getYourRef())

                // 출원번호, 공개번호, 공고번호, 등록번호, 등록공고번호
                .appNo(mergeVO.getAppNo())
                .openNo(mergeVO.getPubNo())
                .publicNo(mergeVO.getAnnouncementNo())
                .regNo(mergeVO.getRegNo())
                .regPublicNo(mergeVO.getRegAnnounceNo())
                .provisionalAppNo(mergeVO.getProvisionalAppNo())

                // [전략 및 관계 번호]
                // 최초출원번호, 원출원번호, 원등록번호, 재출원번호, 국제출원번호
                .firstAppNo(mergeVO.getFirstAppNo())
                .originalAppNo(mergeVO.getOriginalAppNo())
                .originalRegNo(mergeVO.getOriginalRegNo())
                .reAppNo(mergeVO.getReAppNo())
                .globalAppNo(mergeVO.getGlobalAppNo())

                .foreignAppTimingCode(null) // 국내 출원 전용 값. 해외출원의 시점을 나타냄. 필요시 사용해도됨.

                // [명세서 정보]
                // 등급, 독립항, 종속항
                .gradeCode(mergeVO.getGradeCode())
                .independentClaim(mergeVO.getIndependentClaims())
                .dependentClaim(mergeVO.getDependentClaims())

                // 국내명세서, 해외명세서, 도면수, 최종항수(Null 체크)
                .specPage(mergeVO.getSpecPage())
                .overseaSpecPage(mergeVO.getOverseaSpecPage())
                .drawingPaperCount(mergeVO.getDrawingCount())
                .ultiDependentClaimCount(mergeVO.getFinalClaimCount() != null ? mergeVO.getFinalClaimCount() : null)

                // [행정 및 관리]
                // IPC_구분_코드, 관리위임업체, 포기내용
                .ipcCategoryCode(mergeVO.getIpcClassification())
                .outsourcingCorpName(mergeVO.getAnnuityAgency())
                .giveUpContent(mergeVO.getAbandonNote())

                // [관리 여부]
                // 연차관리여부
                .yearCntManagementYn(mergeVO.getIsAnnuityManaged())
                .renewalManagementYn(mergeVO.getIsRenewalManaged())
                .nextPaymentInstallment(mergeVO.getNextPaymentInstallment())

                // [기타]
                // 부서코드, 비고
                .deptCode(mergeVO.getDeptName())
                .note(mergeVO.getNote())

                .clientNm(mergeVO.getClientName())
                .applicantNm(mergeVO.getApplicantName())
                .regMgrNm(mergeVO.getRegMgrName())
                .foreignAgentNm(mergeVO.getForeignAgentName())

                .build();
    }*/

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

