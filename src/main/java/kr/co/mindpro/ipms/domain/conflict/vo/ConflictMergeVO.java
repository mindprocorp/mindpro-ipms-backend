package kr.co.mindpro.ipms.domain.conflict.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.util.CommonMapping;


import kr.co.mindpro.ipms.domain.conflict.dto.request.ConflictRequest;

import kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppMstVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 분쟁/심판(Conflict) 통합 매핑 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "분쟁/심판 통합 등록 및 수정 데이터 객체")
public class ConflictMergeVO {



    @Schema(description = "업무 일련번호", example = "CFT202600001")
    private String conflictSeq;

    @Schema(description = "사무소 일련번호", example = "OFFICE_001")
    private String officeSeq;

    @Schema(description = "등록자 일련번호", example = "USERIF20260000001")
    private String createUser;

    // --- 1. 사건관리 (CftCaseMng - CFT) ---
    @Schema(description = "상태코드", example = "특허법원")
    private String statusCode;
    @Schema(description = "상태이름", example = "특허법원")
    private String statusCodeName;

    @Schema(description = "계류법정코드", example = "특허법원")
    private String courtCategoryCode;
    @Schema(description = "계류법정이름", example = "특허법원")
    private String courtCategoryCodeName;
    @Schema(description = "구분", example = "특허법원")
    private String caseCategoryCode;
    @Schema(description = "구분", example = "특허법원")
    private String caseCategoryCodeName;

    @Schema(description = "대리인구분코드", example = "내자")
    private String agentCategoryCode;
    @Schema(description = "대리인구분", example = "내자")
    private String agentCategoryCodeName;

    @Schema(description = "국내외구분코드", example = "내국")
    private String appClassificationCode;
    @Schema(description = "국내외구분", example = "내국")
    private String appClassificationCodeName;


    @Schema(description = "권리코드", example = "특허")
    private String rightTypeCode;
    @Schema(description = "권리", example = "특허")
    private String rightTypeCodeName;

    @Schema(description = "사건종류코드", example = "무효심판")
    private String caseTypeCode;
    @Schema(description = "사건종류", example = "무효심판")
    private String caseTypeCodeName;

    @CommonMapping(type="DATE", group="CFT", code="", description="접수일")
    @Schema(description = "접수일", example = "20260116")
    private String receiptDate;

    @Schema(description = "OurRef (출원연결키)", example = "APP202600001")
    private String ourRef;

    @Schema(description = "YourRef", example = "Y-12345")
    private String yourRef;

    @Schema(description = "출원인관리번호", example = "C-98765")
    private String clientRef;

    // --- 2. 출원 기본정보 (AppBaseInfo - APP) ---
    @Schema(description = "출원키", example = "APPMST20260000128")
    private String appSeq;

    @Schema(description = "출원국", example = "KR")
    private String appCountry;

    @Schema(description = "국가코드", example = "KR")
    private String countryCode;
    @Schema(description = "국가코드이름", example = "KR")
    private String countryCodeName;

    @CommonMapping(type="DATE", group="APP", code="", description="출원일")
    @Schema(description = "출원일", example = "20251201")
    private String appDate;

    @Schema(description = "출원번호", example = "10-2025-1234567")
    private String appNo;

    @CommonMapping(type="DATE", group="APP", code="", description="출원/등록공고일")
    @Schema(description = "출원/등록공고일", example = "20260101")
    private String announcementDate;

    @CommonMapping(type="DATE", group="APP", code="", description="등록일")
    @Schema(description = "등록일", example = "20260601")
    private String regDate;

    @Schema(description = "등록번호", example = "10-1234567-0000")
    private String regNo;

    @CommonMapping(type="DATE", group="CFT", code="", description="청구마감일")
    @Schema(description = "청구마감일", example = "20261231")
    private String dueLimitDate;

    @CommonMapping(type="DATE", group="CFT", code="", description="청구일")
    @Schema(description = "청구일", example = "20260110")
    private String claimDate;

    @Schema(description = "사건번호", example = "2026허1234")
    private String caseNo;

    // --- 3. 담당 정보 (CftManagerInfo - CFT) ---
    @Schema(description = "부서", example = "특허1팀")
    private String deptName;

    @CommonMapping(type="PERSON", group="CFT", code="", description="관리담당자")
    @Schema(description = "관리담당자")
    private String adminMgr;

    @Schema(description = "관리담당자이름")
    private String adminMgrName;

    @CommonMapping(type="PERSON", group="CFT", code="", description="사건담당자")
    @Schema(description = "사건담당자")
    private String caseMgr;
    @Schema(description = "사건담당자이름")
    private String caseMgrName;

    @CommonMapping(type="PERSON", group="CFT", code="", description="담당변리사")
    @Schema(description = "담당변리사")
    private String attorney;
    @Schema(description = "담당변리사이름")
    private String attorneyName;
    // --- 4. 당사자 정보 (AppPartyInfo - APP) ---
    @CommonMapping(type="PERSON", group="CFT", code="", description="해외대리인")
    @Schema(description = "해외대리인")
    private String foreignAgent;


    @Schema(description = "해외대리인이름")
    private String foreignAgentName;

    @CommonMapping(type="PERSON", group="CFT", code="", description="")
    @Schema(description = "의뢰인")
    private String client;


    @Schema(description = "의뢰인이름")
    private String clientName;

    @CommonMapping(type="PERSON", group="APP", code="", description="출원인")
    @Schema(description = "출원인")
    private String applicant;


    @Schema(description = "출원인이름")
    private String applicantName;

    // --- 5. 명칭정보 & 6. 물품류 ---
    @Schema(description = "국문명칭")
    private String titleKo;

    @Schema(description = "영문명칭")
    private String titleEn;

    @Schema(description = "물품류")
    private String goodsClass;

    // --- 7. 청구/피청구인 정보 (CftLitigantInfo - CFT) ---
    @CommonMapping(type="PERSON", group="CFT", code="", description="소개자")
    @Schema(description = "소개자")
    private String introducer;

    @Schema(description = "소개자이름")
    private String introducerName;

    @Schema(description = "청구인 타입", example = "원고")
    private String petitionerType;

    @CommonMapping(type="PERSON", group="CFT", code="", description="청구인명")
    @Schema(description = "청구인")
    private String petitioner;

    @Schema(description = "청구인명")
    private String petitionerName;

    @Schema(description = "청구인 메모")
    private String petitionerMemo;

    @Schema(description = "피청구인 타입", example = "피고")
    private String respondentType;

    @CommonMapping(type="PERSON", group="CFT", code="", description="피청구인명")
    @Schema(description = "피청구인명")
    private String respondent;

    @Schema(description = "피청구인명")
    private String respondentName;

    @Schema(description = "피청구인 메모")
    private String respondentMemo;

    // --- 8. 비고 ---
    @Schema(description = "비고")
    private String note;

    // --- 9. 판결·결과 관리 (CftJudgmentInfo - CFT) ---
    @CommonMapping(type="DATE", group="CFT", code="", description="심사전치일")
    @Schema(description = "심사전치일")
    private String preExamDate;

    @Schema(description = "심사전치결과")
    private String preExamResult;
    @Schema(description = "최종결과")
    private String finalResult;

    @CommonMapping(type="DATE", group="CFT", code="", description="보정서 마감일")
    @Schema(description = "보정서 마감일")
    private String amendLimitDate;

    @CommonMapping(type="DATE", group="CFT", code="", description="보정서 제출일")
    @Schema(description = "보정서 제출일")
    private String amendSubmitDate;

    @CommonMapping(type="DATE", group="CFT", code="", description="판결 송달일")
    @Schema(description = "판결 송달일")
    private String judgmentServedDate;

    @CommonMapping(type="DATE", group="CFT", code="", description="판결 결정일")
    @Schema(description = "판결 결정일")
    private String judgmentDate;

    @Schema(description = "결정내용")
    private String decisionContent;

    @Schema(description = "불복제기 내용")
    private String appealContent;

    @CommonMapping(type="DATE", group="CFT", code="", description="불복제기 마감일")
    @Schema(description = "불복제기 마감일")
    private String appealLimitDate;

    @CommonMapping(type="DATE", group="CFT", code="", description="불복제기 청구일")
    @Schema(description = "불복제기 청구일")
    private String appealDate;

    @CommonMapping(type="DATE", group="CFT", code="", description="포기 지시일자")
    @Schema(description = "포기 지시일자")
    private String abandonInstructDate;

    @CommonMapping(type="DATE", group="CFT", code="", description="포기 일자")
    @Schema(description = "포기 일자")
    private String abandonDate;

    @Schema(description = "포기건 여부", example = "Y")
    private String isAbandoned;

    @Schema(description = "포기 내용")
    private String abandonContent;

    @Schema(    description = "분쟁 결과 리스트",   example = "[{ \"conflictResultSeq\": \"CONFRES20260000001\", \"judgmentResult\": \"인용\" }]"    )
    List<ConflictResultViewVO> ConflictResultList;

    @CommonMapping(type="PAPER", group="PAP", code="", description="불복제기 마감일")
    @Schema(   description = "출원 상표 이미지 파일 시퀀스", example = "FILE20260000001"    )
    private String appTrademarkImage;

    @CommonMapping(type="PAPER", group="PAP", code="", description="불복제기 마감일")
    @Schema(   description = "인용 상표 이미지 파일 시퀀스", example = "FILE20260000002"    )
    private String citedTrademarkImage;



    @CommonMapping(type="DATE", group="APP", code="")
    @Schema(description = "국내등록결정일")
    private String domesticRegDecisionDate;
    @CommonMapping(type="DATE", group="APP", code="")
    @Schema(description = "국내등록일")
    private String domesticRegDate;
    @Schema(description = "국내등록번호")
    private String domesticRegNo;
    @CommonMapping(type="DATE", group="APP", code="")
    @Schema(description = "국제등록일")
    private String intlRegDate;
    @Schema(description = "기타사건여부")
    private String etcYn;



    @Schema(description = "사건명")
    private String caseTitleKo;

    @CommonMapping(type="DATE", group="CFT", code="", description="포기 일자")
    @Schema(description = "처리일")
    private String processDate;


    /**
     * Request DTO의 섹션별 데이터를 VO 필드로 평탄화 매핑
     * ServiceImpl에서 vo.fillFromRequest(request) 로 호출
     */
    // 1. 사건관리 조각
    public void fillCaseMng(ConflictRequest.ConflictCftCaseMng mng) {
        if (mng == null) return;
        courtCategoryCode = mng.courtCategory().code();
        agentCategoryCode = mng.agentCategory().code();
        caseTypeCode      = mng.caseType().code();
        //caseCategoryCode = mng.caseCategory().code();
        appClassificationCode = mng.appClassification().code();
        rightTypeCode         = mng.rightType().code();
        //statusCode            = mng.status().code();
        receiptDate       = mng.receiptDate();
        ourRef            = mng.ourRef();
        yourRef           = mng.yourRef();
        clientRef         = mng.clientRef();
    }
    public void fillEtcCaseMng(ConflictRequest.ConflictEtcCftCaseMng mng) {
        if (mng == null) return;
        //courtCategoryCode = mng.courtCategory().code();
        //agentCategoryCode = mng.agentCategory().code();
        appSeq            = mng.appSeq();
        caseTypeCode      = mng.caseType().code();
        //caseCategoryCode = mng.caseCategory().code();
        appClassificationCode= mng.appClassification().code();
        rightTypeCode         = mng.rightType().code();
        //statusCode            = mng.status().code();
        receiptDate       = mng.receiptDate();
        ourRef            = mng.ourRef();
        yourRef           = mng.yourRef();
        clientRef         = mng.clientRef();
    }

    // 2. 출원 기본정보 조각
    public void fillAppBaseInfo(ConflictRequest.ConflictAppBaseInfo base) {
        if (base == null) return;



        // [공통 필드]
        appSeq      = base.appSeq();
        countryCode = base.countryCode().code();
        appDate     = base.appDate();
        appNo       = base.appNo();
        announcementDate     = base.announcementDate();
        regDate     = base.regDate();
        regNo       = base.regNo();
        dueLimitDate = base.dueLimitDate();
        claimDate   = base.claimDate();
        caseNo      = base.caseNo();
        domesticRegDecisionDate = base.domesticRegDecisionDate();
        domesticRegDate         = base.domesticRegDate();
        domesticRegNo           = base.domesticRegNo();
        intlRegDate             = base.intlRegDate();

    }

    public void fillEtcAppBaseInfo(ConflictRequest.EtcAppBaseInfo base) {
        if (base == null) return;

        // 공통 필드 매핑
        // appSeq: fillEtcCaseMng에서 이미 세팅된 경우 덮어쓰지 않음 (null이 아닌 경우만 적용)
        if (org.springframework.util.StringUtils.hasText(base.appSeq())) {
            this.appSeq = base.appSeq();
        }
        this.countryCode = base.countryCode().code();
        this.appDate     = base.appDate();
        this.appNo       = base.appNo();

        //this.announcementDate     = base.announcementDate();
        this.regDate     = base.regDate();
        this.regNo       = base.regNo();
        this.dueLimitDate = base.dueLimitDate();
        this.claimDate   = base.claimDate();
       //   //이동

        // 기타사건 전용 필드 매핑
        this.domesticRegDecisionDate = base.domesticRegDecisionDate();
        this.domesticRegDate         = base.domesticRegDate();
        this.domesticRegNo           = base.domesticRegNo();
        this.intlRegDate             = base.intlRegDate();
        this.etcYn = "Y"; // 기타사건임을 명시
        this.processDate    = base.processDate();
    }
    // 3. 담당 정보 조각
    public void fillManagerInfo(ConflictRequest.ConflictCftManagerInfo manager) {
        if (manager == null) return;
        deptName = manager.deptName();
        adminMgr = manager.adminMgr().userSeq();
        caseMgr  = manager.caseMgr().userSeq();
        attorney = manager.attorney().userSeq();
    }

    // 4. 당사자 정보 조각
    public void fillPartyInfo(ConflictRequest.ConflictAppPartyInfo party) {
        if (party == null) return;
        foreignAgent  = party.foreignAgent().userSeq();
        foreignAgentName = party.foreignAgent().userName();
        client    = party.client().userSeq();
        clientName = party.client().userName();
        applicant = party.applicant().userSeq();
        applicantName = party.applicant().userName();
    }
    // 4.기타사건 당사자 정보 조각
    public void fillEtcPartyInfo(ConflictRequest.ConflictEtcAppPartyInfo party) {
        if (party == null) return;
        foreignAgent  = party.foreignAgent().userSeq();
        foreignAgentName = party.foreignAgent().userName();
        client    = party.client().userSeq();
        clientName = party.client().userName();
        applicant = party.applicant().userSeq();
        applicantName = party.applicant().userName();
    }

    // 5. 명칭 정보 조각
    public void fillTitleInfo(ConflictRequest.ConflictAppTitleInfo title) {
        if (title == null) return;
        titleKo = title.titleKo();
        titleEn = title.titleEn();
    }

    // 6. 물품 정보 조각
    public void fillGoodsInfo(ConflictRequest.ConflictAppGoodsInfo goods) {
        if (goods == null) return;
        goodsClass = goods.goodsClass();
    }

    // 7. 관계자 정보 조각
    public void fillLitigantInfo(ConflictRequest.ConflictCftLitigantInfo litigant) {
        if (litigant == null) return;
        introducer     = litigant.introducer();
        petitionerType = litigant.petitionerType();
        petitioner = litigant.petitioner().userSeq();
        petitionerMemo = litigant.petitionerMemo();
        respondentType = litigant.respondentType();
        respondent = litigant.respondent();
        respondentMemo = litigant.respondentMemo();
    }
    public void fillEtcLitigantInfo(ConflictRequest.ConflictEtcLitigantInfo litigant) {
        if (litigant == null) return;
        introducer     = litigant.introducer();
        caseTitleKo    = litigant.caseTitleKo();
        //caseNo        = litigant.caseNo();
        petitionerType = litigant.petitionerType();
        petitioner = litigant.petitioner().userSeq();
        petitionerMemo = litigant.petitionerMemo();
        respondentType = litigant.respondentType();
        respondent = litigant.respondent();
        respondentMemo = litigant.respondentMemo();
    }

    // 8. 판결/결과 정보 조각
    public void fillJudgmentInfo(ConflictRequest.ConflictCftJudgmentInfo judge) {
        if (judge == null) return;
        preExamDate         = judge.preExamDate();
        preExamResult       = judge.preExamResult();
        finalResult         = judge.finalResult();
        amendLimitDate      = judge.amendLimitDate();
        amendSubmitDate     = judge.amendSubmitDate();
        judgmentServedDate  = judge.judgmentServedDate();
        judgmentDate        = judge.judgmentDate();
        decisionContent     = judge.decisionContent();
        appealLimitDate     = judge.appealLimitDate();
        appealDate          = judge.appealDate();
        appealContent       = judge.appealContent();
        isAbandoned         = judge.isAbandoned();
        abandonInstructDate = judge.abandonInstructDate();
        abandonDate         = judge.abandonDate();
        abandonContent      = judge.abandonContent();
    }

    // 9. 비고 정보 조각
    public void fillNoteInfo(ConflictRequest.ConflictCftNoteInfo noteInfo) {
        if (noteInfo == null) return;
        note = noteInfo.note();
    }
    public void fillFromAppBasicInfo(ConflictMergeVO app) {
        if (app == null) return;

        // [사건/출원 식별 정보]

        this.ourRef    = app.getOurRef();    // 쿼리의 AS ourRef와 매칭
        this.yourRef   = app.getYourRef();   // 쿼리의 AS yourRef와 매칭
        this.clientRef = app.getClientRef(); // 쿼리의 AS clientRef와 매칭

        // [권리 및 국가]
        this.rightTypeCode   = app.getRightTypeCode();   // 쿼리의 AS rightType
        this.rightTypeCodeName   = app.getRightTypeCodeName();   // 쿼리의 AS rightType
        this.countryCode = app.getCountryCode(); // 쿼리의 AS countryCode
        this.countryCodeName = app.getCountryCodeName(); // 쿼리의 AS countryCode
        this.appCountry  = app.getAppCountry();  // 쿼리의 AS appCountry

        this.appClassificationCode = app.getAppClassificationCode();
        this.appClassificationCodeName = app.getAppClassificationCodeName();
        // [번호 및 일자]
        this.appNo   = app.getAppNo();
        this.regNo   = app.getRegNo();
        this.appDate = app.getAppDate();
        this.announcementDate = app.getAnnouncementDate();
        this.regDate = app.getRegDate();

        // [명칭 및 물품류]
        this.titleKo    = app.getTitleKo();
        this.titleEn    = app.getTitleEn();
        this.goodsClass = app.getGoodsClass();

        // [담당자/당사자 정보] - 쿼리에서 가져온 결과 매핑
//        this.client    = app.getClientName();    // 쿼리의 AS client
//        this.client    = app.getClientName();    // 쿼리의 AS client
        this.applicant = app.getApplicant(); // 쿼리의 AS applicant
        this.applicantName = app.getApplicantName(); // 쿼리의 AS applicant
        this.foreignAgent = app.getForeignAgent();
        this.foreignAgentName = app.getForeignAgentName();

        //기타사건
        domesticRegNo    =  app.getDomesticRegNo();

    }
    /**
     * [마스터 통합 매핑]
     * ConflictMstVO와 AppMstVO의 데이터를 MergeVO 필드에 1:1로 매핑합니다.
     */
    public void fillFromMaster(Object sourceVo) {
        if (sourceVo == null) return;

        // 1. [분쟁 마스터] utb_conflict_mst 매핑
        if (sourceVo instanceof ConflictMstVO mst) {
            this.conflictSeq = mst.getConflictSeq();
            this.officeSeq = mst.getOfficeSeq();
            this.appSeq = mst.getAppSeq();
            // 심판 사건 정보
            this.caseNo = mst.getLitigationCaseNo();       // 2026-당-1234
            this.courtCategoryCode = mst.getCourtCategoryCode();  // 특허심판원
            this.courtCategoryCodeName = mst.getCourtCategoryCodeName();
            this.agentCategoryCode = mst.getAgentCategoryCode();
            this.agentCategoryCodeName = mst.getAgentCategoryCodeName();
            this.caseTypeCode = mst.getCaseTypeCode();
            this.caseTypeCodeName = mst.getCaseTypeCodeName();

            this.deptName = mst.getDeptName();
            // 청구/피청구인 정보
            this.petitionerType = mst.getPetitionerType();
            this.petitionerMemo = mst.getPetitionerMemo();
            this.respondentType = mst.getRespondentType();
            this.respondentMemo = mst.getRespondentMemo();

            // 판결 및 결과
            this.preExamResult = mst.getPreExamResult();
            this.finalResult = mst.getFinalResult();
            this.decisionContent = mst.getDecisionContent();
            this.appealContent = mst.getAppealContent();
            this.isAbandoned = mst.getAbandonYn();
            this.abandonContent = mst.getAbandonContent();
            this.statusCode = mst.getStatus();
            this.statusCodeName = mst.getStatusCodeName();
            this.appealContent = mst.getAppealContent();

            //비고
            note = mst.getNote();

        }

        // 2. [출원 마스터] utb_app_mst 매핑
        else if (sourceVo instanceof AppMstVO app) {
            // 분쟁 마스터에서 안 채워진 정보나 출원 고유 정보 채움
            this.countryCode = (this.countryCode == null) ? app.getCountryCode() : this.countryCode;
            this.appNo = app.getAppNo();
            this.regNo = app.getRegNo();
            // 명칭 정보
            this.titleKo = app.getAppNameKo();    // app_name_ko
            this.titleEn = app.getAppNameEn();    // app_name_en
            this.goodsClass = app.getProductClass(); // product_class

            // 기타 출원 마스터 필드
            this.rightTypeCode = app.getRightCategoryCode(); // right_category
            this.rightTypeCodeName = app.getRightCategoryName(); // right_category

            this.yourRef = app.getAgentRef();   // 필요시 매핑 규칙에 따라 조정
            this.ourRef = app.getAssetNo();
        }
    }
    // --- [Record DTO 변환 메서드 추가] ---

//    @Schema(description = "사건관리 Record 변환")
//    public ConflictRequest.ConflictCftCaseMng toCftCaseMng() {
//        return new ConflictRequest.ConflictCftCaseMng(
//                this.courtCategoryCode, this.agentCategoryCode, this.caseTypeCode, this.appClassification,
//                this.rightTypeCode,  this.receiptDate,this.statusCode,
//                this.ourRef, this.yourRef, this.clientRef
//        );
//    }

//    @Schema(description = "출원 기본정보 Record 변환")
//    public ConflictRequest.ConflictAppBaseInfo toAppBaseInfo() {
//        return new ConflictRequest.ConflictAppBaseInfo(
//                this.appSeq , this.countryCode, this.appDate, this.appNo,
//                this.pubDate, this.regDate, this.regNo, this.dueLimitDate,
//                this.claimDate, this.caseNo
//        );
//    }
//    @Schema(description = "기타사건 확장 출원정보 Record 변환")
//    public ConflictRequest.EtcAppBaseInfo toEtcAppBaseInfo() {
//        return new ConflictRequest.EtcAppBaseInfo(
//                this.appSeq, this.countryCode, this.appDate, this.appNo,
//                this.pubDate, this.regDate, this.regNo, this.dueLimitDate,
//                this.claimDate, this.caseNo,
//                // 확장 필드 추가
//                this.domesticRegDecisionDate,
//                this.domesticRegDate,
//                this.domesticRegNo
//        );
//    }


//    @Schema(description = "담당 정보 Record 변환")
//    public ConflictRequest.ConflictCftManagerInfo toCftManagerInfo() {
//        return new ConflictRequest.ConflictCftManagerInfo(
//                this.deptName, this.adminMgr, this.caseMgr, this.attorney
//        );
//    }

//    @Schema(description = "당사자 정보 Record 변환")
//    public ConflictRequest.ConflictAppPartyInfo toAppPartyInfo() {
//        return new ConflictRequest.ConflictAppPartyInfo(
//                this.foreignAgent, this.client, this.applicant
//        );
//    }

    @Schema(description = "명칭 정보 Record 변환")
    public ConflictRequest.ConflictAppTitleInfo toAppTitleInfo() {
        return new ConflictRequest.ConflictAppTitleInfo(
                this.titleKo, this.titleEn
        );
    }

    @Schema(description = "물품 정보 Record 변환")
    public ConflictRequest.ConflictAppGoodsInfo toAppGoodsInfo() {
        return new ConflictRequest.ConflictAppGoodsInfo(this.goodsClass);
    }

//    @Schema(description = "청구/피청구인 정보 Record 변환")
//    public ConflictRequest.ConflictCftLitigantInfo toCftLitigantInfo() {
//        return new ConflictRequest.ConflictCftLitigantInfo(
//                this.introducer, this.petitionerType, this.petitionerName,
//                this.petitionerMemo, this.respondentType, this.respondentName, this.respondentMemo
//        );
//    }

    @Schema(description = "판결 정보 Record 변환")
    public ConflictRequest.ConflictCftJudgmentInfo toCftJudgmentInfo() {
        return new ConflictRequest.ConflictCftJudgmentInfo(
                this.preExamDate, this.preExamResult, this.finalResult,
                this.amendLimitDate, this.amendSubmitDate, this.judgmentServedDate,
                this.judgmentDate, this.decisionContent, this.appealContent,
                this.appealLimitDate, this.appealDate, this.isAbandoned,
                this.abandonInstructDate, this.abandonDate, this.abandonContent
        );
    }

    @Schema(description = "비고 정보 Record 변환")
    public ConflictRequest.ConflictCftNoteInfo toCftNoteInfo() {
        return new ConflictRequest.ConflictCftNoteInfo(this.note);
    }
}