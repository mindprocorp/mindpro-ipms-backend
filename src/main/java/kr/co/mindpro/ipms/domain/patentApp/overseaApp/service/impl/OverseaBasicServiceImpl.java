package kr.co.mindpro.ipms.domain.patentApp.overseaApp.service.impl;

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
import kr.co.mindpro.ipms.domain.paper.repository.db1.PaperMapper;
import kr.co.mindpro.ipms.domain.paper.service.PaperService;
import kr.co.mindpro.ipms.domain.paper.vo.PaperRequestVO;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.participant.service.ParticipantService;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import kr.co.mindpro.ipms.domain.history.service.HistoryService;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaBasicAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaAppListResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaBasicAppListResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaBasicAppResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.repository.db1.OverseaAppMapper;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.service.OverseaBasicService;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.*;

/**
 * @author : seokho
 * @fileName : OverseaBasicServiceImpl.java
 * @description : 해외 기초 출원(PCT/EP/Madrid 등) 서비스 - AppExtMstVO 적용
 * @since : 2026. 2. 01.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OverseaBasicServiceImpl implements OverseaBasicService {

    private final OverseaAppMapper overseaAppMapper;
    private final ParticipantService participantService;
    private final DueDateService dueDateService;
    private final CustomerService customerService;
    private final HistoryService historyService;
    private final RagService ragService;

    private final CustomerMapper customerMapper;

    private final PaperMapper paperMapper;
    private final PaperService paperService;

    @Override
    public BaseSearchResponse<OverseaBasicAppListResponse.BasicListDetailResponse> getBasicList(BaseSearchRequest request) {

        String officeSeq = SecurityUtil.getOfficeSeq();

        request.setOfficeSeq(officeSeq);
        request.setOffSet(request.getOffSet());

        int totalCnt = overseaAppMapper.getOverseaBasicListCnt(request);

        if (totalCnt == 0) {
            return BaseSearchResponse.of(new ArrayList<>(), request.getPage(), request.getPageSize());
        }

        List<CommonAppVO> appExtMstVOList = overseaAppMapper.getOverseaBasicList(request);

        List<OverseaBasicAppListResponse.BasicListDetailResponse> resList = appExtMstVOList.stream()
                .map(OverseaBasicAppListResponse.BasicListDetailResponse::from)
                .toList();

        return BaseSearchResponse.of(resList, totalCnt, request.getPage(), request.getPageSize());
    }

    /**
     * 해외 기본 상세 해외출원 탭 리스트 조회
     * */
    @Override
    public BaseSearchResponse<OverseaAppListResponse.AppListDetailResponse> getBasicChainOverseaAppList(BaseSearchRequest request) {

        if (request.getTblSeq() == null || request.getTblSeq().isEmpty()) {
            throw new RuntimeException("tblSeq 값이 들어오지 않았습니다.");
        }

        String officeSeq = SecurityUtil. getOfficeSeq();

        request.setOfficeSeq(officeSeq);
        request.setOffSet(request.getOffSet());

        int totalCnt = overseaAppMapper.cntBasicChainOverseaAppList(request);

        if (totalCnt == 0) {
            return BaseSearchResponse.of(new ArrayList<>(), request.getPage(), request.getPageSize());
        }

        List<CommonAppVO> appMstVOList = overseaAppMapper.getBasicChainOverseaAppList(request);

        List<OverseaAppListResponse.AppListDetailResponse> list = appMstVOList.stream()
                .map(OverseaAppListResponse.AppListDetailResponse::fromVO)
                .toList();

        return BaseSearchResponse.of(list, totalCnt, request.getPage(), request.getPageSize());
    }


    // =================================================================
    // [Create] 해외 기초 출원 등록
    // =================================================================
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createBasicApp(OverseaBasicAppRequest.CreateOverseaBasicApp request, MultipartFile file) {

        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();
        String appExtSeq = request.appExtSeq();

        // 1. Request -> CommonAppVO 매핑
        CommonAppVO vo = mapToCommonVO(request, officeSeq);

        int result = 0;
        if (StringUtils.hasText(appExtSeq)) {
            result = overseaAppMapper.getDuplicateAppExtCnt(officeSeq, appExtSeq);
        }

        CommonAppVO oldVO = null;
        // 2. 마스터 테이블(Ext) 저장/수정
        if (result > 0) {
            // [추가] 수정 전 데이터 조회
            oldVO = overseaAppMapper.getOverseaBasicDetail(officeSeq, appExtSeq);

            vo.setUpdateUser(loginUser);
            result = overseaAppMapper.updateBasicApp(vo);
            if (result <= 0) throw new RuntimeException("해외기본 정보 수정 실패");
        } else {
            vo.setCreateUser(loginUser);
            // insert 후 selectKey 등으로 appExtSeq가 vo에 담긴다고 가정
            result = overseaAppMapper.insertOverseaBasicApp(vo);
            if (result <= 0) throw new RuntimeException("해외기본 정보 저장 실패");
        }

        String currentExtSeq = vo.getAppExtSeq();

        // 3. 파일 업로드 (236: 해외기초 파일 분류코드)
        if (file != null && !file.isEmpty()) {
            fileUpload(currentExtSeq, officeSeq, loginUser, "236", file, "overseaBasicAppFile");
        }

        // 4. 당사자(Customer) 맵핑
        if (request.appCounterPartyInfo() != null) {
            var party = request.appCounterPartyInfo();

            if (party.clientInfo() != null) customerMappToWork(party.clientInfo(), currentExtSeq, "client");
            if (party.applicantInfo() != null) customerMappToWork(party.applicantInfo(), currentExtSeq, "applicant");
            if (party.regMgrInfo() != null) customerMappToWork(party.regMgrInfo(), currentExtSeq, "regMgr");
            // [추가] 발명자 — utb_customer_mapp 통합 저장
            if (party.inventorInfo() != null
                    && party.inventorInfo().userSeq() != null
                    && !party.inventorInfo().userSeq().isBlank()) {
                customerMappToWork(
                        java.util.List.of(kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse.CounterPartyInfo.builder()
                                .counterPartySeq(party.inventorInfo().userSeq())
                                .counterPartyName(party.inventorInfo().userName())
                                .build()),
                        currentExtSeq, "inventor"
                );
            }
        }

        // 5. 관계자(Participant) 저장 (기존 build 로직 활용하되 VO 전달)
        List<ParticipantVO> participants = buildParticipantList(vo);
        if (!participants.isEmpty()) {
            participantService.saveAllParticipants(participants);
        }

        // 6. 기일(DueDate) 저장
        List<DueDateVO> dueDates = buildDueDateList(vo);
        if (!dueDates.isEmpty()) {
            dueDateService.saveAllDueDates(dueDates);
        }

        if (oldVO != null) {
            // [추가] 모든 정보(당사자, 관계자 등)가 업데이트된 후 최신 데이터 조회 및 이력 기록
            CommonAppVO latestVO = overseaAppMapper.getOverseaBasicDetail(officeSeq, currentExtSeq);
            historyService.compareAndLog(currentExtSeq, "해외기본 정보 수정", oldVO, latestVO);
        }

        // AI Vector Sync
        ragService.syncVectorData(SecurityUtil.getOfficeSeq(), "OVERSEA_APP", vo.getAppExtSeq(), "해외 기본 출원", this.getOverseaBasicAppDetail(vo.getAppExtSeq()));

        return currentExtSeq;
    }

    private CommonAppVO mapToCommonVO(OverseaBasicAppRequest.CreateOverseaBasicApp request, String officeSeq) {
        var caseMng = request.appCaseMng();
        var manager = request.appManagerInfo();
        var party = request.appCounterPartyInfo();
        var name = request.appNameInfo();
        var element = request.appSpecificElement();
        var strategy = request.appStrategy();
        var states = request.designatedStateInfo();

        return CommonAppVO.builder()
                .officeSeq(officeSeq)
                .appExtSeq(request.appExtSeq())
                // 1. 사건관리
                .rightTypeCode(caseMng != null && caseMng.rightType() != null ? caseMng.rightType().code() : null)
                .appTypeCode(caseMng != null && caseMng.appType() != null ? caseMng.appType().code() : null)
                .ourRef(caseMng != null ? caseMng.ourRef() : null)
                .receiptDate(caseMng != null ? caseMng.receiptDate() : null)
                .appCompleteDate(caseMng != null ? caseMng.appCompleteDate() : null)
                .appManager(caseMng != null && caseMng.appManagerInfo() != null ? caseMng.appManagerInfo().userSeq() : null)
                .caseNo(caseMng != null ? caseMng.caseNo() : null)
                // 2. 담당정보
                .deptName(manager != null ? manager.deptCode() : null)
                .adminMgr(manager != null && manager.adminMgrInfo() != null ? manager.adminMgrInfo().userSeq() : null)
                .caseMgr(manager != null && manager.caseMgrInfo() != null ? manager.caseMgrInfo().userSeq() : null)
                .attorney(manager != null && manager.attorneyInfo() != null ? manager.attorneyInfo().userSeq() : null)
                // 3. 당사자
                .clientContact(party != null && party.clientContactInfo() != null ? party.clientContactInfo().userSeq() : null)
                .inventor(party != null && party.inventorInfo() != null ? party.inventorInfo().userSeq() : null)
                // 4. 명칭 및 물품
                .titleKo(name != null ? name.titleKo() : null)
                .titleEn(name != null ? name.titleEn() : null)
                .goodsClass(request.goodsClass() != null ? request.goodsClass().goodsClass() : null)
                // 5. 명세서 구성요소
                .gradeCode(element != null && element.grade() != null ? element.grade().code() : null)
                .independentClaims(element != null ? element.independentClaims() : null)
                .dependentClaims(element != null ? element.dependentClaims() : null)
                .overseaSpecPage(element != null ? element.overseaSpecPage() : null)
                .drawingCount(element != null ? element.drawingCount() : null)
                // 6. 비고
                .note(request.appNote() != null ? request.appNote().note() : null)
                // 7. 전략 (국제출원)
                .globalAppDate(strategy != null && strategy.globalAppInfo() != null ? strategy.globalAppInfo().globalAppDate() : null)
                .globalAppNo(strategy != null && strategy.globalAppInfo() != null ? strategy.globalAppInfo().globalAppNo() : null)
                // 8. 지정국가
                .individualCountryCnt(states != null && states.designatedIndividual() != null ? states.designatedIndividual().size() : 0)
                .designatedIndividual(states != null && states.designatedIndividual() != null ? String.join(",", states.designatedIndividual()) : null)
                .pctCnt(states != null && states.designatedPct() != null ? states.designatedPct().size() : 0)
                .designatedPct(states != null && states.designatedPct() != null ? String.join(",", states.designatedPct()) : null)
                .epCnt(states != null && states.designatedEp() != null ? states.designatedEp().size() : 0)
                .designatedEp(states != null && states.designatedEp() != null ? String.join(",", states.designatedEp()) : null)
                .madridCnt(states != null && states.designatedMadrid() != null ? states.designatedMadrid().size() : 0)
                .designatedMadrid(states != null && states.designatedMadrid() != null ? String.join(",", states.designatedMadrid()) : null)
                .internationalDesignCnt(states != null && states.designatedIntlDesign() != null ? states.designatedIntlDesign().size() : 0)
                .designatedIntlDesign(states != null && states.designatedIntlDesign() != null ? String.join(",", states.designatedIntlDesign()) : null)
                .abandonDate(states != null ? states.abandonDate() : null)
                .abandonNote(states != null ? states.abandonContent() : null)
                .build();
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

    // =================================================================
// [Read] 해외 기초 출원 상세 조회
// =================================================================
    @Override
    public OverseaBasicAppResponse.OverseaBasicAppDetailResponse getOverseaBasicAppDetail(String extSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // 1. 확장 마스터 정보 조회 (이제 CommonAppVO로 한 번에 가져옴!)
        CommonAppVO vo = overseaAppMapper.getOverseaBasicDetail(officeSeq, extSeq);

        if (vo == null) {
            // [참고] 에러 코드는 상황에 맞게 수정 (예: DATA_NOT_FOUND)
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 2. 다대다 당사자(CounterParty) 정보 조회 (List 형태)
        List<CommonRecordResponse.CounterPartyInfo> clientList =
                customerMapper.selectCounterPartyListByTblSeq(officeSeq, extSeq, "client");
        List<CommonRecordResponse.CounterPartyInfo> applicantList =
                customerMapper.selectCounterPartyListByTblSeq(officeSeq, extSeq, "applicant");
        List<CommonRecordResponse.CounterPartyInfo> regMgrList =
                customerMapper.selectCounterPartyListByTblSeq(officeSeq, extSeq, "regMgr");
        // [추가] 발명자 — utb_customer_mapp 'inventor' → mstVO 주입 (단일)
        List<CommonRecordResponse.CounterPartyInfo> inventorListBasic =
                customerMapper.selectCounterPartyListByTblSeq(officeSeq, extSeq, "inventor");
        if (!inventorListBasic.isEmpty()) {
            var inv = inventorListBasic.get(0);
            vo.setInventor(inv.counterPartySeq());
            vo.setInventorNm(inv.counterPartyName());
        }

        // 3. 첨부파일 정보 조회
        List<PaperResponseVO> fileList = paperMapper.findAllByWork(extSeq, officeSeq);

        // 4. VO + List 데이터들을 조합해서 바로 Response로 변환
        return OverseaBasicAppResponse.OverseaBasicAppDetailResponse.from(vo, clientList, applicantList, regMgrList, fileList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteBasicApp(String extSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        int result = overseaAppMapper.softDeleteAppExt(loginUser, extSeq, officeSeq);

        if (result > 0) {
            return;
        }

        throw new RuntimeException("Failed to delete basic app with extSeq: " + extSeq);
    }

    // =================================================================
    // [Builder Helpers] MergeVO -> AppExtMstVO (저장용)
    // =================================================================

//    /*private AppExtMstVO buildAppExtMstVOFromMerge(OverseaBasicMergeVO mergeVO) {
//        return AppExtMstVO.builder()
//                .officeSeq(mergeVO.getOfficeSeq())
//                .createUser(mergeVO.getCreateUser())
//                .updateUser(mergeVO.getCreateUser())
//                .appExtSeq("utb_app_ext_mst") // Mapper SelectKey 사용 시 무시됨
//
//                // [기본 식별]
//                .rightCategoryCode(mergeVO.getRightTypeCode())
//                .appKindCode(mergeVO.getAppTypeCode())
//                .assetNo(mergeVO.getOurRef())
//                .caseCode(mergeVO.getCaseNo())
//                .deptCode(mergeVO.getDeptName())
//
//                // [명칭/물품]
//                .appNameKo(mergeVO.getTitleKo())
//                .appNameEn(mergeVO.getTitleEn())
//                .productClass(mergeVO.getGoodsClass())
//
//                // [지정국가 정보] (List -> Count/String 변환)
//                .individualCountryCnt(getListSize(mergeVO.getDesignatedIndividual()))
//                .individualCountryContent(listToString(mergeVO.getDesignatedIndividual()))
//
//                .pctCnt(getListSize(mergeVO.getDesignatedPct()))
//                .pctContent(listToString(mergeVO.getDesignatedPct()))
//
//                .epCnt(getListSize(mergeVO.getDesignatedEp()))
//                .epContent(listToString(mergeVO.getDesignatedEp()))
//
//                .madridCnt(getListSize(mergeVO.getDesignatedMadrid()))
//                .madridContent(listToString(mergeVO.getDesignatedMadrid()))
//
//                .internationalDesignCnt(getListSize(mergeVO.getDesignatedIntlDesign()))
//                .internationalDesignContent(listToString(mergeVO.getDesignatedIntlDesign()))
//
//                // [명세서 및 도면] (String -> Integer 변환)
//                .gradeCode(mergeVO.getGradeCode())
//                .independentClaimCnt(parseIntSafe(mergeVO.getIndependentClaims()))
//                .dependentClaimCnt(parseIntSafe(mergeVO.getDependentClaims()))
//                .overseaSpecCnt(parseIntSafe(mergeVO.getOverseaSpecPage()))
//                .drawingCnt(parseIntSafe(mergeVO.getDrawingCount()))
//
//                // [Global / 포기]
//                .globalAppNo(mergeVO.getGlobalAppNo())
//                .giveUpContent(mergeVO.getAbandonContent()) // AppExtMstVO 필드명에 맞춤
//                .note(mergeVO.getNote())
//
//                .build();
//    }*/

    // =================================================================
    // 관계자 & 기일 생성 (MergeVO 기반)
    // =================================================================

    private List<ParticipantVO> buildParticipantList(CommonAppVO vo) {
        List<ParticipantVO> list = new ArrayList<>();
        String appExtSeq = vo.getAppExtSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();
        String user = SecurityUtil.getUserInfoSeq();

        addPart(list, appExtSeq, officeSeq, "appManager", vo.getAppManager(), vo.getAppManagerNm(), user);
        addPart(list, appExtSeq, officeSeq, "adminMgr", vo.getAdminMgr(), vo.getAdminMgrNm(), user);
        addPart(list, appExtSeq, officeSeq, "caseMgr", vo.getCaseMgr(), vo.getCaseMgrNm(), user);
        addPart(list, appExtSeq, officeSeq, "attorney", vo.getAttorney(), vo.getAttorneyNm(), user);
//        addPart(list, appExtSeq, officeSeq, "client", vo.getClientSeq(), vo.getClientName(), user);
        addPart(list, appExtSeq, officeSeq, "clientContact", vo.getClientContact(), vo.getClientContactNm(), user);
//        addPart(list, appExtSeq, officeSeq, "applicant", vo.getApplicantSeq(), vo.getApplicantName(), user);
        addPart(list, appExtSeq, officeSeq, "applicantContact", vo.getApplicantContact(), vo.getApplicantContactNm(), user);
        addPart(list, appExtSeq, officeSeq, "inventor", vo.getInventor(), vo.getInventorNm(), user);
//        addPart(list, appExtSeq, officeSeq, "regMgr", vo.getRegMgrSeq(), vo.getRegMgrName(), user);

        return list;
    }

    private void addPart(List<ParticipantVO> list, String tblSeq, String officeSeq, String code, String userSeq, String userName, String regUser) {
        if (userSeq != null && !userSeq.isEmpty()) {
            list.add(ParticipantVO.builder()
                    .tblSeq(tblSeq).officeSeq(officeSeq).participantCode(code)
                    .userInfoSeq(userSeq).userNameKo(userName).mainYn("Y")
                    .createUser(regUser).updateUser(regUser).build());
        }
    }

    private List<DueDateVO> buildDueDateList(CommonAppVO vo) {
        List<DueDateVO> list = new ArrayList<>();
        String appExtSeq = vo.getAppExtSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();
        String user = SecurityUtil.getUserInfoSeq();

        addDate(list, appExtSeq, officeSeq, "receiptDate", vo.getReceiptDate(), user);
        addDate(list, appExtSeq, officeSeq, "appCompleteDate", vo.getAppCompleteDate(), user);
        addDate(list, appExtSeq, officeSeq, "globalAppDate", vo.getGlobalAppDate(), user);
        addDate(list, appExtSeq, officeSeq, "abandonDate", vo.getAbandonDate(), user);
        // (필요 시 추가 기일 매핑)

        return list;
    }

    private void addDate(List<DueDateVO> list, String tblSeq, String officeSeq, String code, String date, String regUser) {
        if (date != null && !date.isEmpty()) {
            list.add(DueDateVO.builder()
                    .tblSeq(tblSeq).officeSeq(officeSeq).duedateCategoryCode(code)
                    .duedateDate(parseToOffsetDateTime(date))
                    .createUser(regUser).updateUser(regUser).build());
        }
    }

    // =================================================================
    // [Query Helpers] AppExtMstVO -> MergeVO (조회용)
    // =================================================================

    /*private void fillExtMasterToMergeVO(OverseaBasicMergeVO mergeVO, AppExtMstVO mst) {
        if (mst == null) return;

        // PK
        mergeVO.setAppExtSeq(mst.getAppExtSeq());
//        mergeVO.setOfficeSeq(mst.getOfficeSeq());

        // 기본 정보
        mergeVO.setRightTypeCode(mst.getRightCategoryCode());
        mergeVO.setRightTypeName(mst.getRightCategoryName());
        mergeVO.setAppTypeCode(mst.getAppKindCode());
        mergeVO.setAppTypeName(mst.getAppKindName());
        mergeVO.setOurRef(mst.getAssetNo());
        mergeVO.setCaseNo(mst.getCaseCode());
        mergeVO.setDeptName(mst.getDeptCode());

        // 명칭/물품
        mergeVO.setTitleKo(mst.getAppNameKo());
        mergeVO.setTitleEn(mst.getAppNameEn());
        mergeVO.setGoodsClass(mst.getProductClass());

        // 지정국가 (String -> List 변환 로직이 필요하다면 여기서 split 수행)
        // DB에 저장된 "KR, US, JP" 형태의 문자열을 잘라서 List로 넣어줌
        mergeVO.setDesignatedIndividual(stringToList(mst.getIndividualCountryContent()));
        mergeVO.setDesignatedPct(stringToList(mst.getPctContent()));
        mergeVO.setDesignatedEp(stringToList(mst.getEpContent()));
        mergeVO.setDesignatedMadrid(stringToList(mst.getMadridContent()));
        mergeVO.setDesignatedIntlDesign(stringToList(mst.getInternationalDesignContent()));

        // 명세서 (Integer -> String)
        mergeVO.setGradeCode(mst.getGradeCode());
        mergeVO.setGradeName(mst.getGradeName());
        mergeVO.setIndependentClaims(mst.getIndependentClaimCnt() != null ? String.valueOf(mst.getIndependentClaimCnt()) : "0");
        mergeVO.setDependentClaims(mst.getDependentClaimCnt() != null ? String.valueOf(mst.getDependentClaimCnt()) : "0");
        mergeVO.setOverseaSpecPage(mst.getOverseaSpecCnt() != null ? String.valueOf(mst.getOverseaSpecCnt()) : "0");
        mergeVO.setDrawingCount(mst.getDrawingCnt() != null ? String.valueOf(mst.getDrawingCnt()) : "0");

        // Global / 포기
        mergeVO.setGlobalAppNo(mst.getGlobalAppNo());
        mergeVO.setAbandonContent(mst.getGiveUpContent());

        mergeVO.setCreateUser(mst.getCreateUser());
        mergeVO.setCreateAt(mst.getCreateAt());
        mergeVO.setUpdateUser(mst.getUpdateUser());
        mergeVO.setUpdateAt(mst.getUpdateAt());
        mergeVO.setNote(mst.getNote());

        // 관계자 정보
        mergeVO.setAppManagerSeq(mst.getAppManager());
        mergeVO.setAppManagerName(mst.getAppManagerNm());

        mergeVO.setAdminMgrSeq(mst.getAdminMgr());
        mergeVO.setAdminMgrName(mst.getAdminMgrNm());

        mergeVO.setCaseMgrSeq(mst.getCaseMgr());
        mergeVO.setCaseMgrName(mst.getCaseMgrNm());

        mergeVO.setAttorneySeq(mst.getAttorney());
        mergeVO.setAttorneyName(mst.getAttorneyNm());

//        mergeVO.setClientSeq(mst.getClient());
//        mergeVO.setClientName(mst.getClientNm());

        mergeVO.setClientContactSeq(mst.getClientContact());
        mergeVO.setClientContactName(mst.getClientContactNm());

//        mergeVO.setApplicantSeq(mst.getApplicant());
//        mergeVO.setApplicantName(mst.getApplicantNm());

        mergeVO.setInventorSeq(mst.getInventor());
        mergeVO.setInventorName(mst.getInventorNm());

//        mergeVO.setRegMgrSeq(mst.getRegMgr());
//        mergeVO.setRegMgrName(mst.getRegMgrNm());

        // 기일정보
        mergeVO.setReceiptDate(formatMinusHoursString8(mst.getReceiptDate()));
        mergeVO.setAppCompleteDate(formatMinusHoursString8(mst.getAppCompleteDate()));
        mergeVO.setGlobalAppDate(formatMinusHoursString8(mst.getGlobalAppDate()));
        mergeVO.setAbandonDate(formatMinusHoursString8(mst.getAbandonDate()));

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