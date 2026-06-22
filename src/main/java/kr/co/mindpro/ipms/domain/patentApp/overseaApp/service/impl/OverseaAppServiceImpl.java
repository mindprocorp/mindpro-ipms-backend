package kr.co.mindpro.ipms.domain.patentApp.overseaApp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
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
import kr.co.mindpro.ipms.domain.history.service.HistoryService;
import kr.co.mindpro.ipms.domain.paper.repository.db1.PaperMapper;
import kr.co.mindpro.ipms.domain.paper.service.PaperService;
import kr.co.mindpro.ipms.domain.paper.vo.PaperRequestVO;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.participant.service.ParticipantService;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.repository.db1.AppCommonMapper;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.util.AppStatusUtil;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.repository.db1.DomesticAppMapper;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.*;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.*;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.*;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.repository.db1.OverseaAppMapper;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.service.OverseaAppService;
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
 * @fileName : OverseaAppServiceImpl.java
 * @since : 2026. 2. 11.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OverseaAppServiceImpl implements OverseaAppService {

    private final OverseaAppMapper overseaAppMapper;
    private final DomesticAppMapper domesticAppMapper;
    private final ParticipantService participantService;
    private final DueDateService dueDateService;
    private final PaperService paperService;
    private final CustomerService customerService;
    private final HistoryService historyService;
    private final RagService ragService;

    private final PaperMapper paperMapper;
    private final AppCommonMapper appCommonMapper;
    private final CustomerMapper customerMapper;

    private final ObjectMapper objectMapper;


    @Override
    public BaseSearchResponse<OverseaAppListResponse.AppListDetailResponse> getOverseaList(BaseSearchRequest request) {

        String officeSeq = SecurityUtil.getOfficeSeq();
        request.setOfficeSeq(officeSeq);

        // 1. 전체 카운트 조회
        int totalCount = overseaAppMapper.getOverseaAppCount(request);

        // [개선] 데이터가 있을 때만 루프를 돌도록 방어 코드 추가
        if (totalCount > 0) {
            List<CommonAppVO> list = overseaAppMapper.getOverseaAppList(request);

            // 3. [변경] Builder 수동 호출 대신 깔끔하게 fromVO 메서드로 매핑
            List<OverseaAppListResponse.AppListDetailResponse> listResponse = list.stream()
                    .map(OverseaAppListResponse.AppListDetailResponse::fromVO)
                    .toList();

            return BaseSearchResponse.of(listResponse, totalCount, request.getPage(), request.getPageSize());
        }

        // 데이터 없을 시 빈 리스트 반환
        return BaseSearchResponse.of(new ArrayList<>(), 0, request.getPage(), request.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String savePct(OverseaPctAppRequest.CreatePctAppRequest request, MultipartFile mainDrawingFile) {

        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        String appSeq = request.appSeq();
        int result;

        // 🌟 [변경] MergeVO 없이 바로 CommonAppVO 빌드
        CommonAppVO newAppVO = buildCommonAppVO(request, officeSeq, loginUser);

        // todo app_route 강제 세팅 (test 데이터일때 변환해줌 추후 지울 것.)
        newAppVO.setCategoryCode("30"); // 해외로 세팅
        newAppVO.setAppRouteCode("30");  // PCT로 세팅

        String rightType = newAppVO.getRightTypeCode();

        if ("10".equals(rightType) || "20".equals(rightType)) {
            newAppVO.setRightTypeCode(rightType);
        } else {
            log.error(">>> [ERROR] PCT 권리에 유효하지 않은 권리 구분 코드 유입: {}", rightType);
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

            // 1. 기존데이터 조회
            oldAppVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            // 2. 권리별 json 스냅샷 생성 및 업데이트 (🌟 mergeVO 대신 appVO 전달)
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

            // 5. 대표도 파일 업로드 (수정 시 새 파일이 있는 경우)
            if (mainDrawingFile != null && !mainDrawingFile.isEmpty()) {
                String patentSeq = oldAppVO.getPatentSeq();
                if (patentSeq != null && !patentSeq.isBlank()) {
                    // PCT 특허 docSeq = 142
                    fileUpload(patentSeq, officeSeq, loginUser, "142", mainDrawingFile, "appPctPatentFile");
                }
            }

            log.info(">>> 해외출원 PCT 정보가 성공적으로 업데이트되었습니다. appSeq: {}", appSeq);

        } else {
            // ==========================================
            // [인서트 로직] 중복된 출원키가 없을 때
            // ==========================================

            result = overseaAppMapper.insertOverseaApp(newAppVO);

            if (result <= 0) {
                log.error(">>> [ERROR] 해외출원 pct 마스터 정보 저장 실패! (Return: 0)");
                throw new RuntimeException("해외출원 pct 정보(Master) 저장에 실패했습니다.");
            }

            // 🌟 해외출원 전용 맵핑 로직 (appVO 필드 활용)
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

            // 🌟 [변경] 하위 테이블(PCT 등) 인서트 (mergeVO 대신 appVO 전달)
            insertPatentApp(appSeq, newAppVO, mainDrawingFile);

            log.info(">>> 해외출원 PCT 정보가 신규 생성되었습니다. appSeq: {}", appSeq);
        }

        // [공통] 관계자 정보 처리 (고객 맵핑)
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();

            if (party.clientInfo() != null && !party.clientInfo().isEmpty()) {
                customerMappToWork(party.clientInfo(), appSeq, "client");
            }
            if (party.applicantInfo() != null && !party.applicantInfo().isEmpty()) {
                customerMappToWork(party.applicantInfo(), appSeq, "applicant");
            }
            // [추가] 발명자 — PCT 발명자 매핑
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

        // [변경] 관계자 정보 저장 (appVO 전달)
        List<ParticipantVO> participants = getParticipantList(appSeq, newAppVO);
        if (!participants.isEmpty()) {
            participantService.saveAllParticipants(participants);
        }

        // [변경] 기일 정보 저장 (appVO 전달)
        List<DueDateVO> dueDates = getDueDateList(appSeq, newAppVO);
        if (!dueDates.isEmpty()) {
            dueDateService.saveAllDueDates(dueDates);
        }

        if (oldAppVO != null) {
            CommonAppVO latestVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
            historyService.compareAndLog(appSeq, "해외출원 PCT 정보 수정", oldAppVO, latestVO);
        }

        // AI Vector Sync
        ragService.syncVectorData(SecurityUtil.getOfficeSeq(), "OVERSEA_APP", appSeq, "해외 PCT 출원", this.getPctDetail(appSeq));


        return appSeq;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveEp(OverseaEpAppRequest.CreateEpAppRequest request, MultipartFile mainDrawingFile) {

        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 수정 시 프론트에서 appSeq 전달 예정
        String appSeq = request.appSeq();
        int result;

        // [변경] MergeVO를 걷어내고 바로 CommonAppVO 빌드!
        CommonAppVO newAppVO = buildCommonAppVO(request, officeSeq, loginUser);

        // todo app_route 강제 세팅 (추후 데이터 정제 후 삭제)
        newAppVO.setCategoryCode("30"); // 해외로 세팅
        newAppVO.setAppRouteCode("40");  // EP로 세팅

        String rightType = newAppVO.getRightTypeCode();

        // EP는 보통 특허(10) 중심이므로 유효성 체크
        if (!"10".equals(rightType)) {
            log.error(">>> [ERROR] EP 권리에 유효하지 않은 권리 구분 코드 유입: {}", rightType);
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

            // 2. 권리별 json 스냅샷 생성 및 하부 테이블 업데이트 (appVO 전달)
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

            // 5. 대표도 파일 업로드 (수정 시 새 파일이 있는 경우)
            if (mainDrawingFile != null && !mainDrawingFile.isEmpty()) {
                String patentSeq = oldAppVO.getPatentSeq();
                if (patentSeq != null && !patentSeq.isBlank()) {
                    // EP 특허 docSeq = 190
                    fileUpload(patentSeq, officeSeq, loginUser, "190", mainDrawingFile, "appEpPatentFile");
                }
            }

            log.info(">>> 해외출원 EP 정보가 성공적으로 업데이트되었습니다. appSeq: {}", appSeq);

        } else {
            // ==========================================
            // [인서트 로직] 중복된 출원키가 없을 때
            // ==========================================

            result = overseaAppMapper.insertOverseaApp(newAppVO);

            if (result <= 0) {
                log.error(">>> [ERROR] 해외출원 EP 마스터 정보 저장 실패! (Return: 0)");
                throw new RuntimeException("해외출원 EP 정보(Master) 저장에 실패했습니다.");
            }

            // 해외출원 전용 맵핑 로직 (appExtSeq가 있을 경우 체인 연결)
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

            // [변경] 하위 테이블(EP 상세 등) 인서트 (appVO 전달)
            insertPatentApp(appSeq, newAppVO, mainDrawingFile);

            log.info(">>> 해외출원 EP 정보가 신규 생성되었습니다. appSeq: {}", appSeq);
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
            // EP는 해외 대리인(foreignAgent) 정보가 매우 중요!
            if (party.foreignAgentInfo() != null && !party.foreignAgentInfo().isEmpty()) {
                customerMappToWork(party.foreignAgentInfo(), appSeq, "foreignAgent");
            }
            // [추가] 발명자 — EP 발명자 매핑
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

        // 관계자 정보 저장 (mergeVO -> newAppVO)
        List<ParticipantVO> participants = getParticipantList(appSeq, newAppVO);
        if (!participants.isEmpty()) {
            participantService.saveAllParticipants(participants);
        }

        // 기일 정보 저장 (mergeVO -> newAppVO)
        List<DueDateVO> dueDates = getDueDateList(appSeq, newAppVO);
        if (!dueDates.isEmpty()) {
            dueDateService.saveAllDueDates(dueDates);
        }

        if (oldAppVO != null) {
            CommonAppVO latestVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
            historyService.compareAndLog(appSeq, "해외출원 EP 정보 수정", oldAppVO, latestVO);
        }

        // AI Vector Sync
        ragService.syncVectorData(SecurityUtil.getOfficeSeq(), "OVERSEA_APP", appSeq, "해외 EP 출원", this.getEpDetail(appSeq));

        return appSeq;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveMadrid(OverseaMadridAppRequest.CreateMadridRequest request, MultipartFile trademarkImage) {

        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 수정 시 프론트에서 appSeq 전달
        String appSeq = request.appSeq();
        int result;

        // 🌟 [변경] MergeVO 없이 바로 CommonAppVO 빌드
        CommonAppVO newAppVO = buildCommonAppVO(request, officeSeq, loginUser);

        // todo app_route 강제 세팅 (test 데이터일때 변환해줌 추후 지울 것.)
        newAppVO.setCategoryCode("30"); // 해외로 세팅
        newAppVO.setAppRouteCode("50"); // madrid로 세팅.

        String rightType = newAppVO.getRightTypeCode();

        // 마드리드는 상표(40) 권리 전용
        if (!"40".equals(rightType)) {
            log.error(">>> [ERROR] madrid 권리에 유효하지 않은 권리 구분 코드 유입: {}", rightType);
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 요청된 값에 따라 상태값 지정.
        AppStatusUtil.calculateAndSetAppState(newAppVO);

        // 중복되는 출원키가 있는지 확인하는 로직 (업데이트/인서트 분기)
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

            // 2. 권리별 json 스냅샷 생성 (🌟 newAppVO의 최신 정보를 반영하여 oldAppVO 업데이트)
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

            // 5. 상표 이미지 파일 업로드 (수정 시 새 파일이 있는 경우)
            if (trademarkImage != null && !trademarkImage.isEmpty()) {
                String trademarkSeq = oldAppVO.getTrademarkSeq();
                if (trademarkSeq != null && !trademarkSeq.isBlank()) {
                    fileUpload(trademarkSeq, officeSeq, loginUser, "202", trademarkImage, "appMadridPatentFile");
                }
            }

            log.info(">>> 해외출원 Madrid 정보가 성공적으로 업데이트되었습니다. appSeq: {}", appSeq);

        } else {
            // ==========================================
            // [인서트 로직] 중복된 출원키가 없을 때
            // ==========================================

            result = overseaAppMapper.insertOverseaApp(newAppVO);

            if (result <= 0) {
                log.error(">>> [ERROR] 해외출원 Madrid 마스터 정보 저장 실패! (Return: 0)");
                throw new RuntimeException("해외출원 Madrid 정보(Master) 저장에 실패했습니다.");
            }

            // 🌟 해외출원 전용 맵핑 로직 (appExtSeq 연동)
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

            // 🌟 [변경] 하위 테이블(상표 권리 등) 인서트 (newAppVO 전달)
            insertTrademarkApp(appSeq, newAppVO, trademarkImage);

            log.info(">>> 해외출원 Madrid 정보가 신규 생성되었습니다. appSeq: {}", appSeq);
        }

        // ==========================================
        // [공통] 관계자 및 기일 정보 저장
        // ==========================================
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();

            if (party.clientInfo() != null && !party.clientInfo().isEmpty()) {
                customerMappToWork(party.clientInfo(), appSeq, "client");
            }
            if (party.applicantInfo() != null && !party.applicantInfo().isEmpty()) {
                customerMappToWork(party.applicantInfo(), appSeq, "applicant");
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
            historyService.compareAndLog(appSeq, "해외출원 마드리드 정보 수정", oldAppVO, latestVO);
        }

        // AI Vector Sync
        ragService.syncVectorData(SecurityUtil.getOfficeSeq(), "OVERSEA_APP", appSeq, "해외 MADRID 출원", this.getMadridDetail(appSeq));

        return appSeq;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveInterDesign(OverseaInterDesignAppRequest.CreateInterDesignAppRequest request, MultipartFile mainImageFile) {

        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 수정 시 프론트에서 appSeq 전달
        String appSeq = request.appSeq();
        int result;

        // 🌟 [변경] MergeVO 없이 바로 CommonAppVO 빌드
        CommonAppVO newAppVO = buildCommonAppVO(request, officeSeq, loginUser);

        // todo app_route 강제 세팅 (추후 삭제 예정)
        newAppVO.setCategoryCode("30"); // 해외로 세팅
        newAppVO.setAppRouteCode("60");  // 국제디자인(헤이그)으로 세팅

        String rightType = newAppVO.getRightTypeCode();

        // 국제 디자인은 권리구분이 디자인(30)이어야 함
        if (!"30".equals(rightType)) {
            log.error(">>> [ERROR] 국제디자인(헤이그) 권리에 유효하지 않은 권리 구분 코드 유입: {}", rightType);
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

            // 2. 권리별 json 스냅샷 생성 및 하부 테이블 업데이트
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

            // 5. 대표도 파일 업로드 (수정 시 새 파일이 있는 경우)
            if (mainImageFile != null && !mainImageFile.isEmpty()) {
                String designSeq = oldAppVO.getDesignSeq();
                if (designSeq != null && !designSeq.isBlank()) {
                    fileUpload(designSeq, officeSeq, loginUser, "393", mainImageFile, "appInterDesignPatentFile");
                }
            }

            log.info(">>> 해외출원 국제디자인 정보가 성공적으로 업데이트되었습니다. appSeq: {}", appSeq);

        } else {
            // ==========================================
            // [인서트 로직] 중복된 출원키가 없을 때
            // ==========================================

            result = overseaAppMapper.insertOverseaApp(newAppVO);

            if (result <= 0) {
                log.error(">>> [ERROR] 해외출원 국제디자인 마스터 정보 저장 실패! (Return: 0)");
                throw new RuntimeException("해외출원 국제디자인 정보(Master) 저장에 실패했습니다.");
            }

            // 🌟 해외출원 전용 맵핑 로직 (appExtSeq 체인 연결)
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

            // 🌟 [변경] 하위 디자인 테이블 인서트 (newAppVO 전달)
            insertDesignApp(appSeq, newAppVO, mainImageFile);

            log.info(">>> 해외출원 국제디자인 정보가 신규 생성되었습니다. appSeq: {}", appSeq);
        }

        // ==========================================
        // [공통] 관계자 및 기일 정보 저장
        // ==========================================
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
            // [추가] 발명자 — 국제디자인 발명자 매핑
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
            historyService.compareAndLog(appSeq, "해외출원 국제디자인 정보 수정", oldAppVO, latestVO);
        }

        // AI Vector Sync
        ragService.syncVectorData(SecurityUtil.getOfficeSeq(), "OVERSEA_APP", appSeq, "해외 INTER DESIGN 출원", this.getInterDesignDetail(appSeq));

        return appSeq;
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

    // 1. PCT 상세 조회
    @Override
    public OverseaPctAppResponse.PctAppDetailResponse getPctDetail(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // 마스터 정보 조회 (CommonAppVO)
        CommonAppVO mstVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 🌟 [변경] MergeVO 생성 및 fillMaster... 로직 제거
        // 당사자 정보 직접 조회
        List<CommonRecordResponse.CounterPartyInfo> clientList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "client");
        List<CommonRecordResponse.CounterPartyInfo> applicantList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "applicant");
        // [추가] 발명자 — PCT 발명자 조회 + mstVO 주입
        List<CommonRecordResponse.CounterPartyInfo> inventorListPct = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "inventor");
        if (!inventorListPct.isEmpty()) {
            var inv = inventorListPct.get(0);
            mstVO.setInventor(inv.counterPartySeq());
            mstVO.setInventorNm(inv.counterPartyName());
        }

        // 파일 리스트 조회
        List<PaperResponseVO> fileList = paperMapper.findAllByWork(mstVO.getPatentSeq(), officeSeq);

        // 🌟 [변경] fromVOViewPct 메서드 호출 (mstVO와 리스트들을 직접 전달)
        return OverseaPctAppResponse.PctAppDetailResponse.fromVOViewPct(mstVO, clientList, applicantList, fileList);
    }

    // 2. EP(유럽 특허) 상세 조회
    @Override
    public OverseaEpAppResponse.EpAppDetailResponse getEpDetail(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        CommonAppVO mstVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 당사자 정보 직접 조회 (EP는 해외대리인이 중요!)
        List<CommonRecordResponse.CounterPartyInfo> clientList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "client");
        List<CommonRecordResponse.CounterPartyInfo> applicantList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "applicant");
        List<CommonRecordResponse.CounterPartyInfo> foreignAgentList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "foreignAgent");
        // [추가] 발명자 — EP 발명자 조회 + mstVO 주입
        List<CommonRecordResponse.CounterPartyInfo> inventorListEp = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "inventor");
        if (!inventorListEp.isEmpty()) {
            var inv = inventorListEp.get(0);
            mstVO.setInventor(inv.counterPartySeq());
            mstVO.setInventorNm(inv.counterPartyName());
        }

        List<PaperResponseVO> fileList = paperMapper.findAllByWork(mstVO.getPatentSeq(), officeSeq);

        // 🌟 [변경] fromVOViewEp 호출
        return OverseaEpAppResponse.EpAppDetailResponse.fromVOViewEp(mstVO, clientList, applicantList, foreignAgentList, fileList);
    }

    // 3. Madrid(국제 상표) 상세 조회
    @Override
    public OverseaMadridAppResponse.MadridAppDetailResponse getMadridDetail(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        CommonAppVO mstVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 당사자 정보 조회
        List<CommonRecordResponse.CounterPartyInfo> clientList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "client");
        List<CommonRecordResponse.CounterPartyInfo> applicantList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "applicant");

        // 🌟 상표이므로 trademarkSeq 사용
        List<PaperResponseVO> fileList = paperMapper.findAllByWork(mstVO.getTrademarkSeq(), officeSeq);

        // 🌟 [변경] fromVOViewMadrid 호출
        return OverseaMadridAppResponse.MadridAppDetailResponse.fromVOViewMadrid(mstVO, clientList, applicantList, fileList);
    }

    // 4. International Design(국제 디자인) 상세 조회
    @Override
    public OverseaInterDesignAppResponse.InterDesignAppDetailResponse getInterDesignDetail(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        CommonAppVO mstVO = overseaAppMapper.getOverseaAppDetail(officeSeq, appSeq)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 당사자 정보 조회 (디자인은 등록권리자 regMgr 포함 가능성 높음)
        List<CommonRecordResponse.CounterPartyInfo> clientList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "client");
        List<CommonRecordResponse.CounterPartyInfo> applicantList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "applicant");
        List<CommonRecordResponse.CounterPartyInfo> regMgrList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "regMgr");
        // [추가] 발명자 — 국제디자인 발명자 조회 + mstVO 주입
        List<CommonRecordResponse.CounterPartyInfo> inventorListId = customerMapper.selectCounterPartyListByTblSeq(officeSeq, appSeq, "inventor");
        if (!inventorListId.isEmpty()) {
            var inv = inventorListId.get(0);
            mstVO.setInventor(inv.counterPartySeq());
            mstVO.setInventorNm(inv.counterPartyName());
        }

        // 🌟 디자인이므로 designSeq 사용
        List<PaperResponseVO> fileList = paperMapper.findAllByWork(mstVO.getDesignSeq(), officeSeq);

        // 🌟 [변경] fromVOViewInterDesign 호출
        return OverseaInterDesignAppResponse.InterDesignAppDetailResponse.fromVOViewInterDesign(mstVO, clientList, applicantList, regMgrList, fileList);
    }

    /*public void setCounterPartyInfo(OverseaAppMergeVO mergeVO, String tblSeq, String officeSeq) {

        List<CommonRecordResponse.CounterPartyInfo> clientList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, tblSeq, "client");
        List<CommonRecordResponse.CounterPartyInfo> applicantList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, tblSeq, "applicant");
        List<CommonRecordResponse.CounterPartyInfo> regMgrList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, tblSeq, "regMgr");
        List<CommonRecordResponse.CounterPartyInfo> foreignAgentList = customerMapper.selectCounterPartyListByTblSeq(officeSeq, tblSeq, "foreignAgent");

        mergeVO.setClientInfo(clientList);
        mergeVO.setApplicantInfo(applicantList);
        mergeVO.setRegMgrInfo(regMgrList);
        mergeVO.setForeignAgentInfo(foreignAgentList);
    }*/

    // =================================================================
    // DB 저장 관련 (Insert Logic)
    // =================================================================

    // 1. 특허/실용신안 상세 저장
    public void insertPatentApp(String appSeq, CommonAppVO appVO, MultipartFile mainDrawingFile) {
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

        // todo 특/실 구분값 필요해보임. 일단 특허 고정. pct와 ep 구분도 필요 pct 특허 docSeq = 142, ep 특허 docSeq = 190
        // 파일 업로드
        fileUpload(patentVO.getPatentSeq(), SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq(), "142", mainDrawingFile, "appPctPatentFile");
    }

    // 2. 디자인 상세 저장
    public void insertDesignApp(String appSeq, CommonAppVO appVO, MultipartFile mainImageFile) {
        AppDesignVO designVO = AppDesignVO.builder()
                .appSeq(appSeq)
                .officeSeq(appVO.getOfficeSeq())
                .designDescription(appVO.getDesignDescription())
                .designSummary(appVO.getDesignSummary())
                .createUser(appVO.getCreateUser())
                .updateUser(appVO.getCreateUser())
                .note(appVO.getNote())
                .build();

        domesticAppMapper.insertDesignApp(designVO);

        // 파일 업로드
        // todo 국제디자인 docSeq 공통 코드 없음으로 일단 393 (기타) 지정.
        fileUpload(designVO.getDesignSeq(), SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq(), "393", mainImageFile, "appPctPatentFile");
    }

    // 3. 상표 상세 저장
    public void insertTrademarkApp(String appSeq, CommonAppVO appVO, MultipartFile trademarkImage) {
        AppTrademarkVO trademarkVO = AppTrademarkVO.builder()
                .appSeq(appSeq)
                .officeSeq(appVO.getOfficeSeq())
                .createUser(appVO.getCreateUser())
                .updateUser(appVO.getCreateUser())
                .note(appVO.getNote())
                .build();

        domesticAppMapper.insertTrademarkApp(trademarkVO);

        // 파일 업로드
        fileUpload(trademarkVO.getTrademarkSeq(), SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq(), "202", trademarkImage, "appPctPatentFile");
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
     * MergeVO에서 모든 날짜/기한 필드를 추출하여 DueDateVO 리스트로 반환
     */
    public List<DueDateVO> getDueDateList(String appSeq, CommonAppVO appVO) {
        String officeSeq = appVO.getOfficeSeq();
        List<DueDateVO> list = new ArrayList<>();

        // ---------------------------------------------------------
        // 1. 사건 관리 (Case Management)
        // ---------------------------------------------------------
        addDueDateIfPresent(list, "receiptDate", appVO.getReceiptDate(), appSeq, officeSeq);           // 접수일
        addDueDateIfPresent(list, "appCompleteDate", appVO.getAppCompleteDate(), appSeq, officeSeq);   // 출원 완료일
        addDueDateIfPresent(list, "appOrderDate", appVO.getAppOrderDate(), appSeq, officeSeq);         // 출원 지시일
        addDueDateIfPresent(list, "appDeadline", appVO.getAppDeadline(), appSeq, officeSeq);           // 출원 마감일
        addDueDateIfPresent(list, "oaDeliveryDate", appVO.getOaDeliveryDate(), appSeq, officeSeq);     // OA 발송일
        addDueDateIfPresent(list, "appDate", appVO.getAppDate(), appSeq, officeSeq);                   // 출원일

        // 분할출원 관련
        addDueDateIfPresent(list, "divDeadline", appVO.getDivDeadline(), appSeq, officeSeq);           // 분할출원마감일
        addDueDateIfPresent(list, "divAppDate", appVO.getDivAppDate(), appSeq, officeSeq);             // 분할 출원일

        // 특허청/WIPO/헤이그 관련
        addDueDateIfPresent(list, "autoProtectionDate", appVO.getAutoProtectionDate(), appSeq, officeSeq);       // 자동보호 결정일
        addDueDateIfPresent(list, "authoritySubmissionDate", appVO.getAuthoritySubmissionDate(), appSeq, officeSeq); // 특허청제출일
        addDueDateIfPresent(list, "hagueDeliveryDate", appVO.getHagueDeliveryDate(), appSeq, officeSeq);         // 헤이그발송일

        // ---------------------------------------------------------
        // 2. 전략 및 관계 (Strategy & Relations)
        // ---------------------------------------------------------
        addDueDateIfPresent(list, "provisionalAppDate", appVO.getProvisionalAppDate(), appSeq, officeSeq); // 가출원일
        addDueDateIfPresent(list, "firstAppDate", appVO.getFirstAppDate(), appSeq, officeSeq);             // 최초출원일
        addDueDateIfPresent(list, "originalAppDate", appVO.getOriginalAppDate(), appSeq, officeSeq);       // 원출원일
        addDueDateIfPresent(list, "originalRegDate", appVO.getOriginalRegDate(), appSeq, officeSeq);       // 원등록일
        addDueDateIfPresent(list, "reAppDate", appVO.getReAppDate(), appSeq, officeSeq);                   // 재출원일
        addDueDateIfPresent(list, "globalAppDate", appVO.getGlobalAppDate(), appSeq, officeSeq);           // 국제출원일
        addDueDateIfPresent(list, "parentRegAppDate", appVO.getParentRegAppDate(), appSeq, officeSeq);     // 모등록일

        // PCT 진입 마감/완료
        addDueDateIfPresent(list, "npe20Deadline", appVO.getNpe20Deadline(), appSeq, officeSeq);               // 20개월 진입마감
        addDueDateIfPresent(list, "entry20CompleteDate", appVO.getEntry20CompleteDate(), appSeq, officeSeq);   // 20개월 진입완료
        addDueDateIfPresent(list, "npe30Deadline", appVO.getNpe30Deadline(), appSeq, officeSeq);               // 30개월 진입마감
        addDueDateIfPresent(list, "entry30CompleteDate", appVO.getEntry30CompleteDate(), appSeq, officeSeq);   // 30개월 진입완료

        // PCT 수수료/조사/심사
        addDueDateIfPresent(list, "filingFeeDeadline", appVO.getFilingFeeDeadline(), appSeq, officeSeq); // 수수료 마감
        addDueDateIfPresent(list, "filingFeePayDate", appVO.getFilingFeePayDate(), appSeq, officeSeq);   // 수수료 납부
        addDueDateIfPresent(list, "isaReceiptDate", appVO.getIsaReceiptDate(), appSeq, officeSeq);       // 국제조사 접수
        addDueDateIfPresent(list, "isrReportDate", appVO.getIsrReportDate(), appSeq, officeSeq);         // 국제조사 보고
        addDueDateIfPresent(list, "ipeDeadline", appVO.getIpeDeadline(), appSeq, officeSeq);             // 예비심사 마감
        addDueDateIfPresent(list, "ipeRequestDate", appVO.getIpeRequestDate(), appSeq, officeSeq);       // 예비심사 청구
        addDueDateIfPresent(list, "ipeReportDate", appVO.getIpeReportDate(), appSeq, officeSeq);         // 예비심사 보고
        addDueDateIfPresent(list, "intlReceiptDate", appVO.getIntlReceiptDate(), appSeq, officeSeq);     // 국제공개 접수
        addDueDateIfPresent(list, "intlPubDate", appVO.getIntlPubDate(), appSeq, officeSeq);             // 국제공개일

        // ---------------------------------------------------------
        // 3. 행정 관리 (Management)
        // ---------------------------------------------------------
        addDueDateIfPresent(list, "examRequestDeadline", appVO.getExamRequestDeadline(), appSeq, officeSeq);     // 심사청구마감
        addDueDateIfPresent(list, "examRequestOrderDate", appVO.getExamRequestOrderDate(), appSeq, officeSeq);   // 심사청구지시
        addDueDateIfPresent(list, "examRequestDate", appVO.getExamRequestDate(), appSeq, officeSeq);             // 심사청구일

        addDueDateIfPresent(list, "pubDate", appVO.getPubDate(), appSeq, officeSeq);                             // 공개일
        addDueDateIfPresent(list, "announcementDate", appVO.getAnnouncementDate(), appSeq, officeSeq);           // 공고일
        addDueDateIfPresent(list, "announcementDecisionDate", appVO.getAnnouncementDecisionDate(), appSeq, officeSeq); // 공고결정일

        // 보정 관련
        addDueDateIfPresent(list, "amendNoticeDate", appVO.getAmendNoticeDate(), appSeq, officeSeq);     // 보정 통지일
        addDueDateIfPresent(list, "amendDeadline", appVO.getAmendDeadline(), appSeq, officeSeq);         // 보정 마감일
        addDueDateIfPresent(list, "amendSubmitDate", appVO.getAmendSubmitDate(), appSeq, officeSeq);     // 보정 제출일
        addDueDateIfPresent(list, "claimAmendDate", appVO.getClaimAmendDate(), appSeq, officeSeq);       // 청구 보정일

        // 포기/취하 관련
        addDueDateIfPresent(list, "abandonOrderDate", appVO.getAbandonOrderDate(), appSeq, officeSeq);     // 포기 지시일
        addDueDateIfPresent(list, "abandonReceiptDate", appVO.getAbandonReceiptDate(), appSeq, officeSeq); // 포기 접수일
        addDueDateIfPresent(list, "abandonDate", appVO.getAbandonDate(), appSeq, officeSeq);               // 포기일
        addDueDateIfPresent(list, "deemedWithdrawalReceiptDate", appVO.getDeemedWithdrawalReceiptDate(), appSeq, officeSeq); // 취하간주 접수
        addDueDateIfPresent(list, "deemedWithdrawalDate", appVO.getDeemedWithdrawalDate(), appSeq, officeSeq);               // 취하간주 일자

        // ---------------------------------------------------------
        // 4. 등록 및 권리 유지 (Registration & Maintenance)
        // ---------------------------------------------------------
//        addDueDateIfPresent(list, "kipoDelayDays", appVO.getKipoDelayDays(), appSeq, officeSeq);     // 특허청 지연일 (날짜형식 아닐 수 있음 주의)
        addDueDateIfPresent(list, "rightPeriod", appVO.getRightPeriod(), appSeq, officeSeq);         // 존속기간 만료일

        addDueDateIfPresent(list, "regDecisionDate", appVO.getRegDecisionDate(), appSeq, officeSeq);     // 등록결정일
        addDueDateIfPresent(list, "regReceiptDate", appVO.getRegReceiptDate(), appSeq, officeSeq);       // 등록접수일
        addDueDateIfPresent(list, "regNormalDeadline", appVO.getRegNormalDeadline(), appSeq, officeSeq); // 등록 정상마감
        addDueDateIfPresent(list, "regGraceDeadline", appVO.getRegGraceDeadline(), appSeq, officeSeq);   // 등록 과태마감
        addDueDateIfPresent(list, "regOrderDate", appVO.getRegOrderDate(), appSeq, officeSeq);           // 등록 지시일
        addDueDateIfPresent(list, "regPaymentDate", appVO.getRegPaymentDate(), appSeq, officeSeq);       // 등록 납부일
        addDueDateIfPresent(list, "regDate", appVO.getRegDate(), appSeq, officeSeq);                     // 등록일
        addDueDateIfPresent(list, "regAnnounceDate", appVO.getRegAnnounceDate(), appSeq, officeSeq);     // 등록 공고일
        addDueDateIfPresent(list, "renewalDeadline", appVO.getRenewalDeadline(), appSeq, officeSeq);     // 갱신 마감일

        // EP(유럽) 등록결정 관련
        addDueDateIfPresent(list, "epAnnouncementDate", appVO.getEpAnnouncementDate(), appSeq, officeSeq);   // EP 공고일
        addDueDateIfPresent(list, "searchReceiptDate", appVO.getSearchReceiptDate(), appSeq, officeSeq);     // EP 서치 접수
        addDueDateIfPresent(list, "searchReportDate", appVO.getSearchReportDate(), appSeq, officeSeq);       // EP 서치 보고


        // 연차료/국내등록/기타
        addDueDateIfPresent(list, "annuityOrderDate", appVO.getAnnuityOrderDate(), appSeq, officeSeq);   // 연차 관리위임일
        addDueDateIfPresent(list, "standardDeadline", appVO.getStandardDeadline(), appSeq, officeSeq);   // 연차 정상마감
        addDueDateIfPresent(list, "penaltyDeadline", appVO.getPenaltyDeadline(), appSeq, officeSeq);     // 연차 과태마감
        addDueDateIfPresent(list, "domesticRegDate", appVO.getDomesticRegDate(), appSeq, officeSeq);     // 국내 등록일
        addDueDateIfPresent(list, "protectionStartDate", appVO.getProtectionStartDate(), appSeq, officeSeq); // 보호 시작일

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

    // 각 권리별 데이터 snapShot 생성 및 신규 데이터 softDelete & 인서트
    public String processSnapshotAndUpdate(CommonAppVO mstVO, CommonAppVO appVO, String officeSeq, String appSeq, String loginUser) {
        int result;
        String snapShot;

        try {
            switch (mstVO.getRightTypeCode()) {
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
//                    newDesignVO.setMultiDesign(String.valueOf(mergeVO.getMultiDesign()));
//                    newDesignVO.setIsPartialDesign(mergeVO.getIsPartialDesign());

                    newDesignVO.setUpdateUser(loginUser);

                    result = domesticAppMapper.updateDesignApp(newDesignVO);

                    if (result == 0) {
                        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                }
                case "40" -> {
                    AppTrademarkVO trademarkVO = new AppTrademarkVO();

                    trademarkVO.setTrademarkSeq(mstVO.getTrademarkSeq());
                    trademarkVO.setMadridAppNo(mstVO.getMadridNo());
                    trademarkVO.setClassificAppNo(mstVO.getProductClassAppNo());

                    snapShot = objectMapper.writeValueAsString(trademarkVO);

                    // 상표에는 유효한 데이터가 없음으로 일단 업데이트 로직 제외.
                }
                default -> throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(">>> 이력 데이터 JSON 변환 중 오류 발생", e);
        }

        return snapShot;
    }

    /**
     * [리팩토링] PCT(국제출원) Request -> CommonAppVO 다이렉트 변환 (오버로딩)
     */
    private CommonAppVO buildCommonAppVO(OverseaPctAppRequest.CreatePctAppRequest request, String officeSeq, String loginUser) {

        // 1. 기본 식별자 및 해외 체인 정보 세팅
        CommonAppVO.CommonAppVOBuilder<?, ?> builder = CommonAppVO.builder()
                .officeSeq(officeSeq)
                .createUser(loginUser)
                .updateUser(loginUser)
                .appSeq(request.appSeq() != null && !request.appSeq().isBlank() ? request.appSeq() : "utb_app_mst")
                .appExtSeq(request.appExtSeq()); // 해외 마스터 연결용

        // 2. 출원 사건 관리 (PctAppCaseMng)
        if (request.appCaseMng() != null) {
            var mng = request.appCaseMng();
            if (mng.appRoute() != null) {
                builder.appRouteCode(mng.appRoute().code());
                builder.appRouteName(mng.appRoute().codeName());
            }
            if (mng.category() != null) builder.categoryCode(mng.category().code());
            if (mng.rightType() != null) builder.rightTypeCode(mng.rightType().code());
            if (mng.appCategory() != null) builder.appCategoryCode(mng.appCategory().code());

            builder.ourRef(mng.ourRef())
                    .clientRef(mng.clientRef())
                    .receiptDate(mng.receiptDate());
        }

        // 3. 출원 기본 정보 (PctAppBaseInfo)
        if (request.appBaseInfo() != null) {
            var base = request.appBaseInfo();
            builder.appOrderDate(base.appOrderDate())
                    .appDeadline(base.appDeadline())
                    .appDate(base.appDate())
                    .appNo(base.appNo());

            if (base.noticeExceptionApply() != null) {
                builder.noticeExceptionApplyCode(base.noticeExceptionApply().code());
            }
        }

        // 4. 담당 정보 (PctAppManagerInfo)
        if (request.appManagerInfo() != null) {
            var mgr = request.appManagerInfo();
            builder.deptName(mgr.deptCode());

            if (mgr.applicantContactInfo() != null) {
                builder.applicantContact(mgr.applicantContactInfo().userSeq());
                builder.applicantContactNm(mgr.applicantContactInfo().userName());
            }
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

        // 5. 당사자 정보 (PctAppCounterPartyInfo)
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();
            if (party.inventorInfo() != null) {
                builder.inventor(party.inventorInfo().userSeq());
                builder.inventorNm(party.inventorInfo().userName());
            }
        }

        // 6. 명칭 정보 (PctAppNameInfo)
        if (request.appNameInfo() != null) {
            builder.titleKo(request.appNameInfo().titleKo())
                    .titleEn(request.appNameInfo().titleEn());
        }

        // 7. 출원 전략설정 (PctAppStrategy) - 🌟 PCT 핵심 (20/30개월 마감)
        if (request.appStrategy() != null) {
            var strat = request.appStrategy();
            builder.krDesignationYn(strat.krDesignationYn());

            if (strat.deadline20Info() != null) {
                var d20 = strat.deadline20Info();
                builder.complete20Yn(d20.complete20Yn())
                        .npe20Deadline(d20.npe20Deadline())
                        .entry20CompleteDate(d20.entry20CompleteDate())
                        .app20Country(listToString(d20.app20Country()));
            }
            if (strat.deadline30Info() != null) {
                var d30 = strat.deadline30Info();
                builder.complete30Yn(d30.complete30Yn())
                        .npe30Deadline(d30.npe30Deadline())
                        .entry30CompleteDate(d30.entry30CompleteDate())
                        .app30Country(listToString(d30.app30Country()));
            }
        }

        // 8. 출원 행정관리 (PctAppManagement) - 🌟 수수료 및 국제 조사
        if (request.appManagement() != null) {
            var mgmt = request.appManagement();
            builder.abandonOrderDate(mgmt.abandonOrderDate())
                    .abandonDate(mgmt.abandonDate())
                    .abandonNote(mgmt.abandonNote());

            if (mgmt.pctFilingFeeInfo() != null) {
                builder.filingFeeDeadline(mgmt.pctFilingFeeInfo().filingFeeDeadline())
                        .filingFeePayDate(mgmt.pctFilingFeeInfo().filingFeePayDate());
            }
            if (mgmt.internationalSearchInfo() != null) {
                var isr = mgmt.internationalSearchInfo();
                builder.isaReceiptDate(isr.isaReceiptDate())
                        .isrReportDate(isr.isrReportDate())
                        .searchResult(isr.searchResult());
            }
        }

        // 9. 등록 및 권리유지 관리 (PctAppMaintenance) - 🌟 예비심사 및 국제공개
        if (request.appMaintenance() != null) {
            var maint = request.appMaintenance();
            if (maint.pctIpeInfo() != null) {
                var ipe = maint.pctIpeInfo();
                builder.ipeDeadline(ipe.ipeDeadline())
                        .ipeRequestDate(ipe.ipeRequestDate())
                        .ipeReportDate(ipe.ipeReportDate());
            }
            if (maint.intlPubInfo() != null) {
                var pub = maint.intlPubInfo();
                builder.intlReceiptDate(pub.intlReceiptDate())
                        .intlPubDate(pub.intlPubDate())
                        .intlPubNo(pub.intlPubNo());
            }
        }

        // 10. 요약/청구 및 비고
        if (request.claimSummaryInfo() != null) {
            builder.summary(request.claimSummaryInfo().summary())
                    .claimScope(request.claimSummaryInfo().claimScope());
        }
        if (request.appNote() != null) builder.note(request.appNote().note());

        return builder.build();
    }

    /**
     * [리팩토링] EP(유럽특허) Request -> CommonAppVO 다이렉트 변환 (Full Version)
     */
    private CommonAppVO buildCommonAppVO(OverseaEpAppRequest.CreateEpAppRequest request, String officeSeq, String loginUser) {

        // 1. 기본 식별자 및 공통 정보 세팅
        CommonAppVO.CommonAppVOBuilder<?, ?> builder = CommonAppVO.builder()
                .officeSeq(officeSeq)
                .createUser(loginUser)
                .updateUser(loginUser)
                .appSeq(request.appSeq() != null && !request.appSeq().isBlank() ? request.appSeq() : "utb_app_mst")
                .appExtSeq(request.appExtSeq()); // 해외 마스터 연동 식별자

        // 2. 출원 사건관리 (EpAppCaseMng)
        if (request.appCaseMng() != null) {
            var mng = request.appCaseMng();
            if (mng.appRoute() != null) {
                builder.appRouteCode(mng.appRoute().code());
                builder.appRouteName(mng.appRoute().codeName());
            }
            if (mng.category() != null) {
                builder.categoryCode(mng.category().code());
                builder.categoryName(mng.category().codeName());
            }
            if (mng.rightType() != null) {
                builder.rightTypeCode(mng.rightType().code());
                builder.rightTypeName(mng.rightType().codeName());
            }
            if (mng.appCategory() != null) {
                builder.appCategoryCode(mng.appCategory().code());
                builder.appCategoryName(mng.appCategory().codeName());
            }
            builder.ourRef(mng.ourRef())
                    .yourRef(mng.yourRef())
                    .clientRef(mng.clientRef())
                    .receiptDate(mng.receiptDate());
        }

        // 3. 출원기본정보 (EpAppBaseInfo) + 분할출원 정보
        if (request.appBaseInfo() != null) {
            var base = request.appBaseInfo();
            builder.appDeadline(base.appDeadline())
                    .oaDeliveryDate(base.oaDeliveryDate())
                    .appDate(base.appDate())
                    .appNo(base.appNo());

            if (base.noticeExceptionApply() != null) {
                builder.noticeExceptionApplyCode(base.noticeExceptionApply().code());
                builder.noticeExceptionApplyName(base.noticeExceptionApply().codeName());
            }

            if (base.divAppInfo() != null) {
                builder.divDeadline(base.divAppInfo().divDeadline())
                        .divAppDate(base.divAppInfo().divAppDate())
                        .divAppNo(base.divAppInfo().divAppNo());
            }
        }

        // 4. 담당 정보 (EpAppManagerInfo)
        if (request.appManagerInfo() != null) {
            var mgr = request.appManagerInfo();
            builder.deptName(mgr.deptCode()); // 부서코드 매핑

            if (mgr.applicantContactInfo() != null) {
                builder.applicantContact(mgr.applicantContactInfo().userSeq());
                builder.applicantContactNm(mgr.applicantContactInfo().userName());
            }
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

        // 5. 당사자 정보 (EpAppCounterPartyInfo) - 마스터에는 발명자 정보만 세팅
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();
            if (party.inventorInfo() != null) {
                builder.inventor(party.inventorInfo().userSeq());
                builder.inventorNm(party.inventorInfo().userName());
            }
        }

        // 6. 명칭 정보 (EpAppNameInfo)
        if (request.appNameInfo() != null) {
            builder.titleKo(request.appNameInfo().titleKo())
                    .titleEn(request.appNameInfo().titleEn());
        }

        // 7. 류(class) / IPC 분류 (EpAppIpcClass)
        if (request.appIpcClass() != null) {
            builder.ipcClassification(request.appIpcClass().ipcClassification());
        }

        // 8. 명세서 구성요소 (EpAppSpecificElement)
        if (request.appSpecificElement() != null) {
            var spec = request.appSpecificElement();
            builder.independentClaims(spec.independentClaims())
                    .dependentClaims(spec.dependentClaims())
                    .specPage(spec.specPage())
                    .drawingCount(spec.drawingCount())
                    .overseaSpecPage(spec.overseaSpecPage());

            if (spec.grade() != null) {
                builder.gradeCode(spec.grade().code());
                builder.gradeName(spec.grade().codeName());
            }
        }

        // 9. 출원 전략설정 (EpAppStrategy)
        if (request.appStrategy() != null) {
            var strat = request.appStrategy();
            if (strat.globalAppInfo() != null) {
                builder.globalAppDate(strat.globalAppInfo().globalAppDate())
                        .globalAppNo(strat.globalAppInfo().globalAppNo());
            }
            if (strat.originalAppInfo() != null) {
                builder.originalAppDate(strat.originalAppInfo().originalAppDate())
                        .originalAppNo(strat.originalAppInfo().originalAppNo());
            }
        }

        // 10. 지정국가 및 등록국가 (지정국가 리스트는 콤마 구분자로 변환)
        if (request.designatedStateInfo() != null && request.designatedStateInfo().designated() != null) {
            builder.designated(String.join(",", request.designatedStateInfo().designated()));
        }
        if (request.registeredStates() != null && request.registeredStates().registeredStates() != null) {
            builder.registeredStates(String.join(",", request.registeredStates().registeredStates()));
        }

        // 11. 출원 행정관리 (EpAppManagement) - 🌟 EP 전용 서치 정보 포함
        if (request.appManagement() != null) {
            var mgmt = request.appManagement();
            builder.claimAmendDate(mgmt.claimAmendDate())
                    .announcementDate(mgmt.announcementDate())
                    .examRequestDeadline(mgmt.examRequestDeadline())
                    .examRequestOrderDate(mgmt.examRequestOrderDate())
                    .examRequestDate(mgmt.examRequestDate())
                    .pubDate(mgmt.pubDate())
                    .pubNo(mgmt.pubNo())
                    .searchReceiptDate(mgmt.searchReceiptDate()) // EP 서치 접수일
                    .searchReportDate(mgmt.searchReportDate())   // EP 서치 보고일
                    .epSearchResult(mgmt.epSearchResult());      // EP 서치 결과
        }

        // 12. 등록/권리유지 관리 (EpAppMaintenance) - 🌟 EP 전용 취하 간주 정보 포함
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
                    .annuityOrderDate(maint.annuityOrderDate())
                    .annuityAgency(maint.annuityAgency())
                    .deemedWithdrawalReceiptDate(maint.deemedWithdrawalReceiptDate()) // 취하간주 접수일
                    .deemedWithdrawalDate(maint.deemedWithdrawalDate())               // 취하간주 일자
                    .deemedWithdrawalContent(maint.deemedWithdrawalContent());       // 취하간주 내용
        }

        // 13. 요약/청구 탭 정보 (ClaimSummaryInfo)
        if (request.claimSummaryInfo() != null) {
            builder.summary(request.claimSummaryInfo().summary())
                    .claimScope(request.claimSummaryInfo().claimScope());
        }

        // 14. 비고 (AppNote)
        if (request.appNote() != null) {
            builder.note(request.appNote().note());
        }

        return builder.build();
    }

    /**
     * [리팩토링] 마드리드 Request -> CommonAppVO 다이렉트 변환 (Full Version)
     */
    private CommonAppVO buildCommonAppVO(OverseaMadridAppRequest.CreateMadridRequest request, String officeSeq, String loginUser) {

        // 1. 기본 식별자 및 공통 정보
        CommonAppVO.CommonAppVOBuilder<?, ?> builder = CommonAppVO.builder()
                .officeSeq(officeSeq)
                .createUser(loginUser)
                .updateUser(loginUser)
                .appSeq(request.appSeq() != null && !request.appSeq().isBlank() ? request.appSeq() : "utb_app_mst")
                .appExtSeq(request.appExtSeq());

        // 2. 출원 사건관리 (MadridAppCaseMng)
        if (request.appCaseMng() != null) {
            var mng = request.appCaseMng();
            if (mng.appRoute() != null) {
                builder.appRouteCode(mng.appRoute().code());
                builder.appRouteName(mng.appRoute().codeName());
            }
            if (mng.category() != null) {
                builder.categoryCode(mng.category().code());
                builder.categoryName(mng.category().codeName());
            }
            if (mng.rightType() != null) {
                builder.rightTypeCode(mng.rightType().code());
                builder.rightTypeName(mng.rightType().codeName());
            }
            if (mng.appCategory() != null) {
                builder.appCategoryCode(mng.appCategory().code());
                builder.appCategoryName(mng.appCategory().codeName());
            }
            builder.ourRef(mng.ourRef())
                    .yourRef(mng.yourRef())
                    .clientRef(mng.clientRef())
                    .receiptDate(mng.receiptDate());
        }

        // 3. 출원기본정보 (MadridAppBaseInfo)
        if (request.appBaseInfo() != null) {
            var base = request.appBaseInfo();
            builder.appDeadline(base.appDeadline())
                    .appDate(base.appDate())
                    .appNo(base.appNo())
                    .authorityRefNo(base.authorityRefNo()) // 특허청 참조번호
                    .autoProtectionDate(base.autoProtectionDate()) // 자동보호 결정일
                    .announcementDate(base.announcementDate())
                    .announcementNo(base.announcementNo());

            if (base.noticeExceptionApply() != null) {
                builder.noticeExceptionApplyCode(base.noticeExceptionApply().code());
                builder.noticeExceptionApplyName(base.noticeExceptionApply().codeName());
            }
        }

        // 4. 담당 정보 (MadridAppManagerInfo)
        if (request.appManagerInfo() != null) {
            var mgr = request.appManagerInfo();
            builder.deptName(mgr.deptCode());

            if (mgr.applicantContactInfo() != null) {
                builder.applicantContact(mgr.applicantContactInfo().userSeq());
                builder.applicantContactNm(mgr.applicantContactInfo().userName());
            }
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

        // 5. 명칭 및 물품류
        if (request.appNameInfo() != null) {
            builder.titleKo(request.appNameInfo().titleKo())
                    .titleEn(request.appNameInfo().titleEn());
        }
        if (request.goodsClass() != null) {
            builder.goodsClass(request.goodsClass().goodsClass());
        }

        // 6. 출원 전략설정 (MadridAppStrategy) - 🌟 리스트 데이터 처리
        if (request.appStrategy() != null) {
            var strat = request.appStrategy();

            // 원등록 정보
            if (strat.originalRegInfo() != null) {
                builder.originalRegDate(strat.originalRegInfo().originalRegDate())
                        .originalRegNo(strat.originalRegInfo().originalRegNo());
            }

            // 지정국가 / 사후지정국 / 등록국가 (콤마 구분자 문자열 변환)
            if (strat.designated() != null) {
                builder.designated(String.join(",", strat.designated()));
            }
            if (strat.subsequent() != null) {
                builder.subsequent(String.join(",", strat.subsequent()));
            }
            if (strat.registeredStates() != null) {
                builder.registeredStates(String.join(",", strat.registeredStates()));
            }
        }

        // 7. 행정관리 (MadridAppManagement)
        if (request.appManagement() != null) {
            var mgmt = request.appManagement();
            builder.abandonReceiptDate(mgmt.abandonReceiptDate())
                    .abandonDate(mgmt.abandonDate())
                    .abandonNote(mgmt.abandonNote());
        }

        // 8. 등록/유지관리 (MadridAppMaintenance)
        if (request.appMaintenance() != null) {
            var maint = request.appMaintenance();
            builder.regDate(maint.regDate())
                    .regNo(maint.regNo())
                    .paymentInstallment(maint.paymentInstallment()) // 갱신차수
                    .annuityOrderDate(maint.annuityOrderDate())
                    .annuityAgency(maint.annuityAgency())
                    .standardDeadline(maint.standardDeadline())
                    .penaltyDeadline(maint.penaltyDeadline());

            // 국내등록 정보 연동
            if (maint.domesticRegInfo() != null) {
                builder.domesticRegDate(maint.domesticRegInfo().domesticRegDate())
                        .domesticRegNo(maint.domesticRegInfo().domesticRegNo());
            }
        }

        // 9. 비고 (AppNote)
        if (request.appNote() != null) {
            builder.note(request.appNote().note());
        }

        return builder.build();
    }

    /**
     * [리팩토링] 국제디자인(헤이그) Request -> CommonAppVO 다이렉트 변환 (Full Version)
     */
    private CommonAppVO buildCommonAppVO(OverseaInterDesignAppRequest.CreateInterDesignAppRequest request, String officeSeq, String loginUser) {

        // 1. 기본 식별자 및 공통 정보
        CommonAppVO.CommonAppVOBuilder<?, ?> builder = CommonAppVO.builder()
                .officeSeq(officeSeq)
                .createUser(loginUser)
                .updateUser(loginUser)
                .appSeq(request.appSeq() != null && !request.appSeq().isBlank() ? request.appSeq() : "utb_app_mst")
                .appExtSeq(request.appExtSeq());

        // 2. 출원 사건관리 (InterDesignAppCaseMng)
        if (request.appCaseMng() != null) {
            var mng = request.appCaseMng();
            if (mng.appRoute() != null) {
                builder.appRouteCode(mng.appRoute().code());
                builder.appRouteName(mng.appRoute().codeName());
            }
            if (mng.category() != null) {
                builder.categoryCode(mng.category().code());
                builder.categoryName(mng.category().codeName());
            }
            if (mng.rightType() != null) {
                builder.rightTypeCode(mng.rightType().code());
                builder.rightTypeName(mng.rightType().codeName());
            }
            if (mng.appCategory() != null) {
                builder.appCategoryCode(mng.appCategory().code());
                builder.appCategoryName(mng.appCategory().codeName());
            }
            builder.ourRef(mng.ourRef())
                    .clientRef(mng.clientRef())
                    .receiptDate(mng.receiptDate());
        }

        // 3. 출원기본정보 (InterDesignAppBaseInfo) - 🌟 헤이그/WIPO 특화 필드
        if (request.appBaseInfo() != null) {
            var base = request.appBaseInfo();
            builder.appDeadline(base.appDeadline())
                    .appDate(base.appDate())
                    .appNo(base.appNo())
                    .authorityRefNo(base.authorityRefNo())           // 특허청 참조번호
                    .authoritySubmissionDate(base.authoritySubmissionDate()) // 특허청 제출일
                    .hagueDeliveryDate(base.hagueDeliveryDate())     // 헤이그 발송일
                    .wipoRefNo(base.wipoRefNo())                     // WIPO 참조번호
                    .regDate(base.regDate())
                    .regNo(base.regNo());

            if (base.noticeExceptionApply() != null) {
                builder.noticeExceptionApplyCode(base.noticeExceptionApply().code());
                builder.noticeExceptionApplyName(base.noticeExceptionApply().codeName());
            }
        }

        // 4. 담당 정보 (InterDesignAppManagerInfo)
        if (request.appManagerInfo() != null) {
            var mgr = request.appManagerInfo();
            builder.deptName(mgr.deptCode());

            if (mgr.applicantContactInfo() != null) {
                builder.applicantContact(mgr.applicantContactInfo().userSeq());
                builder.applicantContactNm(mgr.applicantContactInfo().userName());
            }
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

        // 5. 당사자 정보 (InterDesignAppCounterPartyInfo) - 마스터에는 창작자(발명자)만 세팅
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();
            if (party.inventorInfo() != null) {
                builder.inventor(party.inventorInfo().userSeq());
                builder.inventorNm(party.inventorInfo().userName());
            }
        }

        // 6. 명칭 정보 (InterDesignAppNameInfo)
        if (request.appNameInfo() != null) {
            builder.titleKo(request.appNameInfo().titleKo())
                    .titleEn(request.appNameInfo().titleEn());
        }

        // 7. 출원 전략설정 (InterDesignAppStrategy) - 🌟 국가 리스트 처리
        if (request.appStrategy() != null) {
            var strat = request.appStrategy();
            if (strat.designated() != null) {
                builder.designated(String.join(",", strat.designated()));
            }
            if (strat.registeredStates() != null) {
                builder.registeredStates(String.join(",", strat.registeredStates()));
            }
        }

        // 8. 설명/요점 (InterDesignDescription) - 🌟 디자인 특화
        if (request.designDescription() != null) {
            builder.designDescription(request.designDescription().designDescription())
                    .designSummary(request.designDescription().designSummary());
        }

        // 9. 출원 행정관리 (InterDesignAppManagement) - 🌟 보정 및 연기 정보
        if (request.appManagement() != null) {
            var mgmt = request.appManagement();
            builder.pubDate(mgmt.pubDate())
                    .pubNo(mgmt.pubNo())
                    .amendNoticeDate(mgmt.amendNoticeDate())
                    .amendDeadline(mgmt.amendDeadline())
                    .amendSubmitDate(mgmt.amendSubmitDate())
                    .publicYn(mgmt.publicYn())                      // 공개 여부
                    .defermentMonthCount(mgmt.defermentMonthCount()) // 연기 월수
                    .abandonReceiptDate(mgmt.abandonReceiptDate())
                    .abandonDate(mgmt.abandonDate())
                    .abandonNote(mgmt.abandonNote());
        }

        // 10. 등록/권리유지 관리 (InterDesignAppMaintenance)
        if (request.appMaintenance() != null) {
            var maint = request.appMaintenance();
            builder.rightPeriod(maint.rightPeriod())
                    .paymentInstallment(maint.paymentInstallment())
                    .standardDeadline(maint.standardDeadline())
                    .penaltyDeadline(maint.penaltyDeadline())
                    .protectionStartDate(maint.protectionStartDate());
        }

        // 11. 비고 (AppNote)
        if (request.appNote() != null) {
            builder.note(request.appNote().note());
        }

        return builder.build();
    }

    /**
     * MasterVO -> MergeVO (DB 조회 결과 -> 통합 VO 역변환)
     * 주: 날짜(DueDate)는 AppMstVO에 없으므로, 별도 로직(getDueDateList)으로 채워야 합니다.
     *//*
    public void fillMasterToMergeVO(OverseaAppMergeVO mergeVO, CommonAppVO appVO) {
        if (appVO == null) return;

        // ---------------------------------------------------------
        // 1. 식별자 및 기본 분류
        // ---------------------------------------------------------
        mergeVO.setAppSeq(appVO.getAppSeq());
        mergeVO.setOfficeSeq(appVO.getOfficeSeq());
        mergeVO.setAppRouteCode(appVO.getAppRouteCode());           // 출원 루트
        mergeVO.setAppRouteName(appVO.getAppRouteName());           // 출원 루트
        mergeVO.setCategoryCode(appVO.getCategoryCode());  // 구분 (내국/외국/해외)
        mergeVO.setCategoryName(appVO.getCategoryName());  // 구분 (내국/외국/해외)
        mergeVO.setRightTypeCode(appVO.getRightTypeCode());     // 권리 구분
        mergeVO.setRightTypeName(appVO.getRightTypeName());     // 권리 구분
        mergeVO.setAppCategoryCode(appVO.getAppCategoryCode());     // 출원 구분
        mergeVO.setAppCategoryName(appVO.getAppCategoryName());     // 출원 구분
        mergeVO.setAppCountry(appVO.getCountryName());      // 국가명
        mergeVO.setAppCountryCode(appVO.getCountryCode());  // 국가코드
        mergeVO.setAppTypeCode(appVO.getAppTypeCode());             // 출원 종류 (신규/진입 등)
        mergeVO.setAppTypeName(appVO.getAppTypeName());             // 출원 종류 (신규/진입 등)
//        mergeVO.setIsOversea(appVO.getIsOversea());         // 해외출원 여부

        // ---------------------------------------------------------
        // 2. 관리 번호 (Ref No)
        // ---------------------------------------------------------
        mergeVO.setOurRef(appVO.getOurRef());              // OurRef
        mergeVO.setYourRef(appVO.getYourRef());            // YourRef
        mergeVO.setClientRef(appVO.getClientRef());         // ClientRef
        mergeVO.setWipoRefNo(appVO.getWipoRefNo());         // WIPO 참조번호
        mergeVO.setAuthorityRefNo(appVO.getAuthorityRefNo()); // 특허청참조번호
        mergeVO.setAccessCode(appVO.getAccessCode());       // 접근 코드

        // ---------------------------------------------------------
        // 3. 명칭 및 물품
        // ---------------------------------------------------------
        mergeVO.setTitleKo(appVO.getTitleKo());
        mergeVO.setTitleEn(appVO.getTitleEn());
        mergeVO.setGoodsClass(appVO.getGoodsClass());     // 물품류

        // ---------------------------------------------------------
        // 4. 출원/공개/등록 번호 (Numbers)
        // ---------------------------------------------------------
        mergeVO.setAppNo(appVO.getAppNo());                 // 출원번호
        mergeVO.setPubNo(appVO.getPubNo());                // 공개번호
        mergeVO.setAnnouncementNo(appVO.getAnnouncementNo());     // 공고번호
        mergeVO.setRegNo(appVO.getRegNo());                 // 등록번호
        mergeVO.setRegAnnounceNo(appVO.getRegAnnounceNo());   // 등록공고번호

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
        mergeVO.setGradeName(appVO.getGradeName());
        mergeVO.setIndependentClaims(appVO.getIndependentClaims());
        mergeVO.setDependentClaims(appVO.getDependentClaims());
        mergeVO.setFinalClaimCount(appVO.getFinalClaimsCount()); // 최종항수

        mergeVO.setSpecPage(appVO.getSpecPage());           // 국내 명세서 페이지
        mergeVO.setOverseaSpecPage(appVO.getOverseaSpecPage()); // 해외 명세서 페이지
        mergeVO.setDrawingCount(appVO.getDrawingCount());  // 도면 수
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
        mergeVO.setIpcClassification(appVO.getIpcClassification()); // IPC
        mergeVO.setAnnuityAgency(appVO.getAnnuityAgency()); // 위임 업체
        mergeVO.setAbandonNote(appVO.getAbandonNote());         // 포기 내용
        mergeVO.setDeemedWithdrawalContent(appVO.getDeemedWithdrawalContent()); // 취하간주 내용

        mergeVO.setDeptName(appVO.getDeptName());           // 부서
        mergeVO.setNote(appVO.getNote());                   // 비고
        mergeVO.setNoticeExceptionApplyCode(appVO.getNoticeExceptionApplyCode()); // 공지예외
        mergeVO.setNoticeExceptionApplyName(appVO.getNoticeExceptionApplyName()); // 공지예외

        mergeVO.setPublicYn(appVO.getPublicYn());
        mergeVO.setDefermentMonthCount(appVO.getDefermentMonthCount());
        mergeVO.setPubDate(appVO.getPubDate());
        mergeVO.setPubNo(appVO.getPubNo());

        // ---------------------------------------------------------
        // 9. 관리 여부 및 비용
        // ---------------------------------------------------------
        mergeVO.setIsAnnuityManaged(appVO.getIsAnnuityManaged());  // 연차관리여부
        mergeVO.setIsRenewalManaged(appVO.getIsRenewalManaged());  // 갱신관리여부
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
        mergeVO.setClientName(appVO.getClientName());

//        mergeVO.setApplicantSeq(appVO.getApplicant());
        mergeVO.setApplicantName(appVO.getApplicantName());

        mergeVO.setApplicantContactSeq(appVO.getApplicantContact());
        mergeVO.setApplicantContactName(appVO.getApplicantContactName());

        mergeVO.setInventorSeq(appVO.getInventor());
        mergeVO.setInventorName(appVO.getInventorName());

//        mergeVO.setRegMgrSeq(appVO.getRegMgr());
        mergeVO.setRegMgrName(appVO.getRegMgrName());

        mergeVO.setAppManagerSeq(appVO.getAppManager());
        mergeVO.setAppManagerName(appVO.getAppManagerName());

        mergeVO.setClientContactSeq(appVO.getClient());
        mergeVO.setClientContactName(appVO.getClientName());

        mergeVO.setAdminMgrSeq(appVO.getAdminMgr());
        mergeVO.setAdminMgrName(appVO.getAdminMgrName());

        mergeVO.setCaseMgrSeq(appVO.getCaseMgr());
        mergeVO.setCaseMgrName(appVO.getCaseMgrName());

        mergeVO.setAttorneySeq(appVO.getAttorney());
        mergeVO.setAttorneyName(appVO.getAttorneyName());

//        mergeVO.setForeignAgentSeq(appVO.getForeignAgent());
        mergeVO.setForeignAgentName(appVO.getForeignAgentName());

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
     * (해외 출원 통합 VO를 마스터 테이블 VO로 변환)
     */
    /*public CommonAppVO buildAppMstVOFromMerge(OverseaAppMergeVO mergeVO) {
        return CommonAppVO.builder()
                // ---------------------------------------------------------
                // 1. 식별자 및 시스템 정보
                // ---------------------------------------------------------
                .officeSeq(mergeVO.getOfficeSeq())
                .appExtSeq(mergeVO.getAppExtSeq())
                .appSeq(mergeVO.getAppSeq()) // PK
                .createUser(mergeVO.getCreateUser())
                .updateUser(mergeVO.getCreateUser())

                // ---------------------------------------------------------
                // 2. 출원 기본 분류 및 상태
                // ---------------------------------------------------------
                .categoryCode("30")                // 구분 (고정값)
//                .isOversea("Y")                           // 해외출원 YN (고정값)
                .rightCategoryCode(mergeVO.getRightTypeCode())    // 권리_구분
                .rightCategoryName(mergeVO.getRightTypeName())    // 권리_구분
                .appRouteCode(mergeVO.getAppRouteCode())          // 해외 출원 루트
                .appRouteName(mergeVO.getAppRouteName())          // 해외 출원 루트
                .appClassificationCode(mergeVO.getCategoryCode()) // 구분 (내국/외국/해외)
                .appClassificationName(mergeVO.getCategoryName()) // 구분 (내국/외국/해외)
                .appCategoryCode(mergeVO.getAppCategoryCode())    // 출원_구분
                .appCategoryName(mergeVO.getAppCategoryName())    // 출원_구분
                .appKindCode(mergeVO.getAppTypeCode())            // 출원 종류
                .appKindName(mergeVO.getAppTypeName())            // 출원 종류
                .countryName(mergeVO.getAppCountry())     // 국가명
                .countryCode(mergeVO.getAppCountryCode()) // 국가코드

                // ---------------------------------------------------------
                // 3. 의뢰 및 참조 정보
                // ---------------------------------------------------------
                .retainSeq(mergeVO.getClientRef())        // 의뢰식별자(ClientRef)
                .assetNo(mergeVO.getOurRef())             // ourRef
                .agentRef(mergeVO.getYourRef())           // yourRef
                .wipoRefNo(mergeVO.getWipoRefNo())        // WIPO 참조번호
                .authorityRefNo(mergeVO.getAuthorityRefNo()) // 특허청 참조번호

                // ---------------------------------------------------------
                // 4. 명칭 및 물품
                // ---------------------------------------------------------
                .appNameKo(mergeVO.getTitleKo())          // 국문 명칭
                .appNameEn(mergeVO.getTitleEn())          // 영문 명칭
                .productClass(mergeVO.getGoodsClass())    // 물품류

                // ---------------------------------------------------------
                // 5. 번호 정보 (Numbers)
                // ---------------------------------------------------------
                .appNo(mergeVO.getAppNo())                      // 출원번호
                .openNo(mergeVO.getPubNo())                     // 공개번호
                .publicNo(mergeVO.getAnnouncementNo())          // 공고번호
                .regNo(mergeVO.getRegNo())                      // 등록번호
                .regPublicNo(mergeVO.getRegAnnounceNo())        // 등록공고번호
                .provisionalAppNo(mergeVO.getProvisionalAppNo())// 가출원번호
                .divAppNo(mergeVO.getDivAppNo())                // 분할출원번호
                .intlPubNo(mergeVO.getIntlPubNo())              // 국제공개번호
                .domesticRegNo(mergeVO.getDomesticRegNo())      // 국내등록번호

                // ---------------------------------------------------------
                // 6. 전략 및 관계 번호 (Family)
                // ---------------------------------------------------------
                .firstAppNo(mergeVO.getFirstAppNo())            // 최초출원번호
                .originalAppNo(mergeVO.getOriginalAppNo())      // 원출원번호
                .originalRegNo(mergeVO.getOriginalRegNo())      // 원등록번호
                .reAppNo(mergeVO.getReAppNo())                  // 재출원번호
                .globalAppNo(mergeVO.getGlobalAppNo())          // 국제출원번호
                .parentRegAppNo(mergeVO.getParentRegAppNo())    // 모등록번호 (MergeVO 신규반영)

                // ---------------------------------------------------------
                // 7. 명세서 및 도면 (이미지 포함)
                // ---------------------------------------------------------
                .gradeCode(mergeVO.getGradeCode())                        // 등급
                .gradeName(mergeVO.getGradeName())                        // 등급
                .independentClaim(mergeVO.getIndependentClaims()) // 독립항
                .dependentClaim(mergeVO.getDependentClaims())     // 종속항
                .ultiDependentClaimCount(mergeVO.getFinalClaimCount()) // 최종항수
                .specPage(mergeVO.getSpecPage())                  // 국내 명세서 페이지
                .overseaSpecPage(mergeVO.getOverseaSpecPage())    // 해외 명세서 페이지
                .drawingPaperCount(mergeVO.getDrawingCount())     // 도면 수
                .mainDrawingFile(mergeVO.getMainImgFile())        // [신규] 대표도/이미지 파일

                // ---------------------------------------------------------
                // 8. 텍스트 상세 (요약, 청구범위, 디자인 등)
                // ---------------------------------------------------------
                .summary(mergeVO.getSummary())                    // [신규] 요약
                .claimScope(mergeVO.getClaimScope())              // [신규] 청구범위
                .designDescription(mergeVO.getDesignDescription())// [신규] 디자인 설명
                .designSummary(mergeVO.getDesignSummary())        // [신규] 디자인 요약

                // ---------------------------------------------------------
                // 9. 행정 및 관리
                // ---------------------------------------------------------
                .ipcCategoryCode(mergeVO.getIpcClassification())  // IPC 코드
                .outsourcingCorpName(mergeVO.getAnnuityAgency())  // 위임 업체
                // 위임 업체가 있으면 Y, 없으면 N 자동 설정
                .outsourcingYn(mergeVO.getAnnuityAgency() != null && !mergeVO.getAnnuityAgency().isEmpty() ? "Y" : "N")

                .deptCode(mergeVO.getDeptName())                  // 부서
                .note(mergeVO.getNote())                          // 비고
                .noticeExceptionApplyCode(mergeVO.getNoticeExceptionApplyCode()) // 공지예외적용
                .noticeExceptionApplyName(mergeVO.getNoticeExceptionApplyName()) // 공지예외적용
                .giveUpContent(mergeVO.getAbandonNote())          // 포기내용
                .deemedWithdrawalContent(mergeVO.getDeemedWithdrawalContent()) // 취하내용
                .accessCode(mergeVO.getAccessCode())              // [신규] 접근코드

                .publicYn(mergeVO.getPublicYn())
                .defermentMonthCount(mergeVO.getDefermentMonthCount())
                .pubNo(mergeVO.getPubNo())

                // ---------------------------------------------------------
                // 10. 관리 여부 및 비용/마감일
                // ---------------------------------------------------------
                .yearCntManagementYn(mergeVO.getIsAnnuityManaged()) // 연차관리 YN
                .renewalManagementYn(mergeVO.getIsRenewalManaged()) // 갱신관리 YN
                .nextPaymentInstallment(mergeVO.getNextPaymentInstallment()) // 차기납부차수
                .paymentInstallment(mergeVO.getPaymentInstallment())

                .renewalDeadline(mergeVO.getRenewalDeadline())    // [신규] 갱신등록마감
                .trademarkRenewalFee(mergeVO.getTrademarkRenewalFee()) // [신규] 상표갱신료
                .renewalLateFee(mergeVO.getRenewalLateFee())           // [신규] 갱신과태료

                // ---------------------------------------------------------
                // 11. 해외 전용 상세 정보 (PCT/EP)
                // ---------------------------------------------------------
                .krDesignationYn(mergeVO.getKrDesignationYn())      // KR 지정 여부
                .searchResult(mergeVO.getSearchResult())            // 국제조사 결과
                .epSearchResult(mergeVO.getEpSearchResult())        // EP 서치 결과

                // PCT 20/30개월
                .complete20Yn(mergeVO.getComplete20Yn())
                .app20Country(mergeVO.getApp20Country())
                .complete30Yn(mergeVO.getComplete30Yn())
                .app30Country(mergeVO.getApp30Country())

                // 국가 리스트 (List -> String 변환)
                .designated(listToString(mergeVO.getDesignated()))   // 지정국가
                .registeredStates(listToString(mergeVO.getRegisteredStates())) // 등록국가
                .subsequent(listToString(mergeVO.getSubsequent()))             // 사후지정

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMadridImage(String fileSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        paperMapper.softDeleteByFileSeq(officeSeq, fileSeq, loginUser);
    }
}
