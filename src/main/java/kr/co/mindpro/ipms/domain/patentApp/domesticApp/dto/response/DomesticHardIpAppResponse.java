package kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;

import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

/**
 * @author : seokho
 * @fileName : DomesticHardIpAppResponse.java
 * @since : 2026. 3. 3.
 */
public class DomesticHardIpAppResponse {
    @Schema(description = "국내 특허/실용신안 출원 상세 응답")
    public record HardIpAppDetailResponse(

            @Schema(description = "출원 식별자")
            String appSeq,

            @Schema(description = "출원상태")
            CommonRecordResponse.CodeInfo appStatus,

            @Schema(description = "출원 사건관리")
            HardIpAppCaseMng appCaseMng,

            @Schema(description = "출원기본정보")
            HardIpAppBaseInfo appBaseInfo,

            @Schema(description = "담당 정보")
            HardIpAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            HardIpAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭 정보")
            HardIpAppNameInfo appNameInfo,

            @Schema(description = "명세서 구성요소")
            HardIpAppSpecificElement appSpecificElement,

            @Schema(description = "출원 전략설정")
            HardIpAppStrategy appStrategy,

            @Schema(description = "요약/청구 탭 정보")
            ClaimSummaryInfo claimSummaryInfo,

            @Schema(description = "출원 행정관리")
            HardIpAppManagement appManagement,

            @Schema(description = "등록/권리유지 관리")
            HardIpAppMaintenance appMaintenance,

            @Schema(description = "첨부파일정보")
            List<CommonRecordResponse.FileInfo> fileInfo,

            @Schema(description = "비고")
            AppNote appNote

    ) implements DomesticAppDetailResponse {
        /**
         * [MergeVO -> Response 변환]
         * 평탄화된 MergeVO 데이터를 계층형 Response 구조로 변환
         */
        public static HardIpAppDetailResponse fromVOViewPatent(
                CommonAppVO vo,
                List<CommonRecordResponse.CounterPartyInfo> clientList,
                List<CommonRecordResponse.CounterPartyInfo> applicantList,
                List<CommonRecordResponse.CounterPartyInfo> regMgrList,
                List<PaperResponseVO> fileList
                ) {
            return new HardIpAppDetailResponse(
                    vo.getAppSeq(),
                    new CommonRecordResponse.CodeInfo(vo.getStatusCode(), vo.getStatusName()),
                    // 1. appCaseMng (HardIpAppCaseMng)
                    new HardIpAppCaseMng(
                            new CommonRecordResponse.CodeInfo(vo.getAppRouteCode(), vo.getAppRouteName()),
                            new CommonRecordResponse.CodeInfo(vo.getCategoryCode(), vo.getCategoryName()),
                            new CommonRecordResponse.CodeInfo(vo.getRightTypeCode(), vo.getRightTypeName()),
                            new CommonRecordResponse.CodeInfo(vo.getAppTypeCode(), vo.getAppTypeName()),
                            new CommonRecordResponse.CodeInfo(vo.getAppCategoryCode(), vo.getAppCategoryName()),
                            formatMinusHoursString8(vo.getInventionReportDate()),
                            formatMinusHoursString8(vo.getReceiptDate()),
                            vo.getOurRef(),
                            vo.getYourRef(),
                            vo.getClientRef(),
                            formatMinusHoursString8(vo.getDraftDeadline()),
                            formatMinusHoursString8(vo.getDraftSendDate())
                    ),

                    // 2. appBaseInfo (HardIpAppBaseInfo)
                    new HardIpAppBaseInfo(
                            formatMinusHoursString8(vo.getAppOrderDate()),
                            formatMinusHoursString8(vo.getAppDeadline()),
                            formatMinusHoursString8(vo.getAppDate()),
                            vo.getAppNo(),
                            vo.getAccessCode(),
                            new CommonRecordResponse.CodeInfo(vo.getAppLanguageCode(), vo.getAppLanguageName()),
                            formatMinusHoursString8(vo.getTransDeadline()),
                            formatMinusHoursString8(vo.getTransSubmitDate())
                    ),

                    // 3. appManagerInfo (HardIpAppManagerInfo)
                    new HardIpAppManagerInfo(
                            vo.getDeptName(),
                            new CommonRecordResponse.PersonInfo(vo.getAdminMgr(), vo.getAdminMgrNm()),
                            new CommonRecordResponse.PersonInfo(vo.getCaseMgr(), vo.getCaseMgrNm()),
                            new CommonRecordResponse.PersonInfo(vo.getAttorney(), vo.getAttorneyNm())
                    ),

                    // 4. appCounterPartyInfo (HardIpAppCounterPartyInfo)
                    new HardIpAppCounterPartyInfo(
                            clientList,
                            new CommonRecordResponse.PersonInfo(vo.getClientContact(), vo.getClientContactNm()),
                            applicantList,
                            new CommonRecordResponse.PersonInfo(vo.getInventor(), vo.getInventorNm()),
                            regMgrList
                    ),

                    // 5. appNameInfo (HardIpAppNameInfo)
                    new HardIpAppNameInfo(
                            vo.getProposal(),
                            vo.getTitleKo(),
                            vo.getTitleEn(),
                            vo.getEtcTitle()
                    ),

                    // 6. appSpecificElement (HardIpAppSpecificElement)
                    new HardIpAppSpecificElement(
                            new CommonRecordResponse.CodeInfo(vo.getGradeCode(), vo.getGradeName()),
                            vo.getIndependentClaims(),
                            vo.getDependentClaims(),
                            vo.getSpecPage(),
                            vo.getFigureCount(),
                            vo.getDrawingCount()
                    ),

                    // 7. appStrategy (HardIpAppStrategy)
                    new HardIpAppStrategy(
                            new FirstAppInfo(formatMinusHoursString8(vo.getFirstAppDate()), vo.getFirstAppNo()),
                            new OriginalAppInfo(formatMinusHoursString8(vo.getOriginalAppDate()), vo.getOriginalAppNo()),
                            new ReAppInfo(formatMinusHoursString8(vo.getReAppDate()), vo.getReAppNo()),
                            new DualAppInfo(formatMinusHoursString8(vo.getDualAppDate()), vo.getDualAppNo()),
                            new GlobalAppInfo(formatMinusHoursString8(vo.getGlobalAppDate()), vo.getGlobalAppNo()),
                            vo.getIsForeignApp(),
                            new CommonRecordResponse.CodeInfo(vo.getForeignAppTimingCode(), vo.getForeignAppTimingName()),
                            formatMinusHoursString8(vo.getForeign6mDeadline()),
                            formatMinusHoursString8(vo.getForeign1yDeadline()),
                            formatMinusHoursString8(vo.getForeignAppDate()),
                            formatMinusHoursString8(vo.getClaimsNoticeDate()),
                            formatMinusHoursString8(vo.getClaimsDeadline()),
                            formatMinusHoursString8(vo.getClaimsSubmitDate())
                    ),

                    // 8. claimSummaryInfo (ClaimSummaryInfo)
                    new ClaimSummaryInfo(
                            vo.getSummary(),
                            vo.getClaimScope()
                    ),

                    // 9. appManagement (HardIpAppManagement)
                    new HardIpAppManagement(
                            vo.getIsPoaSubmitted(),
                            vo.getIpcClassification(),
                            formatMinusHoursString8(vo.getEarlyPubRequestDate()),
                            vo.getHasDomesticPriority(),
                            formatMinusHoursString8(vo.getDomesticPriorDeadline()),
                            formatMinusHoursString8(vo.getDomesticPriorDate()),
                            formatMinusHoursString8(vo.getExamRequestDeadline()),
                            formatMinusHoursString8(vo.getExamRequestDate()),
                            formatMinusHoursString8(vo.getPriorExamReqDate()),
                            formatMinusHoursString8(vo.getPriorExamDecDate()),
                            formatMinusHoursString8(vo.getPubDate()),
                            vo.getPubNo(),
                            formatMinusHoursString8(vo.getAnnouncementDate()),
                            vo.getAnnouncementNo(),
                            formatMinusHoursString8(vo.getAbandonOrderDate()),
                            formatMinusHoursString8(vo.getAbandonDate()),
                            vo.getAbandonNote()
                    ),

                    // 10. appMaintenance (HardIpAppMaintenance)
                    new HardIpAppMaintenance(
                            vo.getFinalClaimsCount(),
                            vo.getKipoDelayDays(),
                            formatMinusHoursString8(vo.getRightPeriod()),
                            vo.getIsAnnuityManaged(),
                            formatMinusHoursString8(vo.getRegDecisionDate()),
                            formatMinusHoursString8(vo.getRegReceiptDate()),
                            formatMinusHoursString8(vo.getRegNormalDeadline()),
                            formatMinusHoursString8(vo.getRegGraceDeadline()),
                            new CommonRecordResponse.CodeInfo(vo.getRegReductionRateCode(), vo.getRegReductionRateName()),
                            formatMinusHoursString8(vo.getRegDate()),
                            vo.getRegNo(),
                            formatMinusHoursString8(vo.getRegAnnounceDate()),
                            vo.getRegAnnounceNo(),
                            new CommonRecordResponse.CodeInfo(vo.getAnnuityReducRateCode(), vo.getAnnuityReducRateName()),
                            vo.getAnnuityYear(),
                            formatMinusHoursString8(vo.getStandardDeadline()),
                            formatMinusHoursString8(vo.getPenaltyDeadline()),
                            formatMinusHoursString8(vo.getRecoveryDeadline()),
                            formatMinusHoursString8(vo.getAnnuityOrderDate()),
                            vo.getAnnuityAgency()
                    ),
                    // 11. fileInfo
                    CommonRecordResponse.FileInfo.from(
                            fileList
                    ),
                    // 12. appNote (AppNote)
                    new AppNote(
                            vo.getNote()
                    )
            );
        }
    }

    public record HardIpAppBaseInfo(
            @Schema(description = "출원지시일", example = "20260109")
            String appOrderDate,

            @Schema(description = "출원마감일", example = "20260109")
            String appDeadline,

            @Schema(description = "출원일", example = "20260109")
            String appDate,

            @Schema(description = "출원번호", example = "123456")
            String appNo,

            @Schema(description = "접근코드", example = "test")
            String accessCode,

            @Schema(description = "출원언어정보")
            CommonRecordResponse.CodeInfo appLanguage,

            @Schema(description = "번역문마감일", example = "20260109")
            String transDeadline,

            @Schema(description = "번역문제출일", example = "20260109")
            String transSubmitDate
    ) {}

    public record HardIpAppCaseMng(
            @Schema(description = "출원루트")
            CommonRecordResponse.CodeInfo appRoute,

            @Schema(description = "구분(내국/외국)", example = "10")
            CommonRecordResponse.CodeInfo category,

            @Schema(description = "권리", example = "10")
            CommonRecordResponse.CodeInfo rightType,

            @Schema(description = "출원종류", example = "10")
            CommonRecordResponse.CodeInfo appType,

            @Schema(description = "출원구분(등록,분할,분리 등)", example = "10")
            CommonRecordResponse.CodeInfo appCategory,

            @Schema(description = "발명신고일", example = "20260109")
            String inventionReportDate,

            @Schema(description = "접수일", example = "20260109")
            String receiptDate,

            @Schema(description = "OurRef", example = "OUR123456")
            String ourRef,

            @Schema(description = "YourRef", example = "YOUR123456")
            String yourRef,

            @Schema(description = "출원인관리번호", example = "APPMNG123456")
            String clientRef,

            @Schema(description = "초안마감일", example = "20260109")
            String draftDeadline,

            @Schema(description = "초안발송일", example = "20260109")
            String draftSendDate
    ) {}

    public record HardIpAppManagerInfo(
            @Schema(description = "부서", example = "test")
            String deptCode,

            @Schema(description = "관리담당자", example = "USERIF20260000001")
            CommonRecordResponse.PersonInfo adminMgrInfo,

            @Schema(description = "사건담당자", example = "USERIF20260000001")
            CommonRecordResponse.PersonInfo caseMgrInfo,

            @Schema(description = "담당변리사", example = "USERIF20260000001")
            CommonRecordResponse.PersonInfo attorneyInfo
    ) {}

    public record HardIpAppCounterPartyInfo(
            @Schema(description = "의뢰인 이름")
            List<CommonRecordResponse.CounterPartyInfo> clientInfo,

            @Schema(description = "의뢰인 담당자", example = "USERIF20260000001")
            CommonRecordResponse.PersonInfo clientContactInfo,

            @Schema(description = "출원인 이름")
            List<CommonRecordResponse.CounterPartyInfo> applicantInfo,

            @Schema(description = "발명자", example = "USERIF20260000001")
            CommonRecordResponse.PersonInfo inventorInfo,

            @Schema(description = "등록권리자 이름(마스터 테이블 저장용)")
            List<CommonRecordResponse.CounterPartyInfo> regMgrInfo
    ) {}

    public record HardIpAppNameInfo(
            @Schema(description = "제안명칭", example = "testVal")
            String proposal,

            @Schema(description = "국문명칭", example = "testVal")
            String titleKo,

            @Schema(description = "영문명칭", example = "testVal")
            String titleEn,

            @Schema(description = "기타_표기_명칭", example = "testVal")
            String etcTitle
    ) {}

    public record HardIpAppSpecificElement(
            @Schema(description = "등급", example = "A")
            CommonRecordResponse.CodeInfo grade,

            @Schema(description = "독립항", example = "5")
            String independentClaims,

            @Schema(description = "종속항", example = "6")
            String dependentClaims,

            @Schema(description = "명세서", example = "7")
            String specPage,

            @Schema(description = "도수", example = "5")
            String figureCount,

            @Schema(description = "도면수", example = "5")
            String drawingCount
    ) {}

    public record HardIpAppStrategy(
            @Schema(description = "최초출원_정보")
            FirstAppInfo firstAppInfo,

            @Schema(description = "원출원_정보")
            OriginalAppInfo originalAppInfo,

            @Schema(description = "재출원_정보")
            ReAppInfo reAppInfo,

            @Schema(description = "이중출원_정보")
            DualAppInfo dualAppInfo,

            @Schema(description = "국제_출원_정보")
            GlobalAppInfo globalAppInfo,

            @Schema(description = "해외출원_여부", example = "Y")
            String isForeignApp,

            @Schema(description = "해외출원_동시_추후", example = "10")
            CommonRecordResponse.CodeInfo foreignAppTiming,

            @Schema(description = "해외출원_6월마감", example = "20260109")
            String foreign6mDeadline,

            @Schema(description = "해외출원_1년마감", example = "20260109")
            String foreign1yDeadline,

            @Schema(description = "해외출원_출원일", example = "20260109")
            String foreignAppDate,

            @Schema(description = "청구범위제출_통지일", example = "20260109")
            String claimsNoticeDate,

            @Schema(description = "청구범위제출_마감일", example = "20260109")
            String claimsDeadline,

            @Schema(description = "청구범위제출_제출일", example = "20260109")
            String claimsSubmitDate
    ) {}

    @Schema(description = "최초출원_정보")
    public record FirstAppInfo(
            @Schema(description = "최초출원일", example = "20260109")
            String firstAppDate,

            @Schema(description = "최초출원번호", example = "first123456")
            String firstAppNo
    ) {}

    @Schema(description = "원출원_정보")
    public record OriginalAppInfo(
            @Schema(description = "원출원일", example = "20251201")
            String originalAppDate,

            @Schema(description = "원출원번호", example = "parent123456")
            String originalAppNo
    ) {}

    @Schema(description = "재출원_정보")
    public record ReAppInfo(
            @Schema(description = "재출원일", example = "20260215")
            String reAppDate,

            @Schema(description = "재출원번호", example = "re123456")
            String reAppNo
    ) {}

    @Schema(description = "이중출원_정보")
    public record DualAppInfo(
            @Schema(description = "이중출원일", example = "20260301")
            String dualAppDate,

            @Schema(description = "이중출원번호", example = "dual123456")
            String dualAppNo
    ) {}

    @Schema(description = "국제_출원_정보")
    public record GlobalAppInfo(
            @Schema(description = "국제_출원일", example = "20251201")
            String globalAppDate,

            @Schema(description = "국제_출원_번호", example = "parent123456")
            String globalAppNo
    ) {}

    public record HardIpAppManagement(
            @Schema(description = "위임장_제출_여부", example = "Y")
            String isPoaSubmitted,

            @Schema(description = "IPC_분류", example = "testVal")
            String ipcClassification,

            @Schema(description = "조기공개신청일", example = "20260109")
            String earlyPubRequestDate,

            @Schema(description = "국내_우선권_여부", example = "Y")
            String hasDomesticPriority,

            @Schema(description = "국내_우선권_마감일", example = "20260109")
            String domesticPriorDeadline,

            @Schema(description = "국내_우선권_주장일", example = "20260109")
            String domesticPriorDate,

            @Schema(description = "심사청구_마감일", example = "20260109")
            String examRequestDeadline,

            @Schema(description = "심사청구_청구일", example = "20260109")
            String examRequestDate,

            @Schema(description = "우선심사_청구일", example = "20260109")
            String priorExamReqDate,

            @Schema(description = "우선심사_결정일", example = "20260109")
            String priorExamDecDate,

            @Schema(description = "출원공개_일자", example = "20260109")
            String pubDate,

            @Schema(description = "출원공개_번호", example = "123456")
            String pubNo,

            @Schema(description = "출원공고_일자", example = "20260109")
            String announcementDate,

            @Schema(description = "출원공고_번호", example = "123456")
            String announcementNo,

            @Schema(description = "포기_지시일", example = "20260109")
            String abandonOrderDate,

            @Schema(description = "포기_일자", example = "20260109")
            String abandonDate,

            @Schema(description = "포기_내용", example = "testVal")
            String abandonNote
    ) {}

    public record HardIpAppMaintenance(
            @Schema(description = "등록/권리유지_최종항수(독립/종속)", example = "432")
            Integer finalClaimsCount,

            @Schema(description = "등록/권리유지_특허청지연일(PAT)", example = "0")
            Integer kipoDelayDays,

            @Schema(description = "등록/권리유지_권리종속기간", example = "20260109")
            String rightPeriod,

            @Schema(description = "등록/권리유지_연차관리 여부", example = "Y")
            String isAnnuityManaged,

            @Schema(description = "등록_결정일", example = "20260109")
            String regDecisionDate,

            @Schema(description = "등록_접수일", example = "20260109")
            String regReceiptDate,

            @Schema(description = "등록_정상_마감", example = "20260109")
            String regNormalDeadline,

            @Schema(description = "등록_과태_마감", example = "20260109")
            String regGraceDeadline,

            @Schema(description = "등록_감면율", example = "50")
            CommonRecordResponse.CodeInfo regReductionRate,

            @Schema(description = "등록_등록일", example = "20260109")
            String regDate,

            @Schema(description = "등록_등록번호", example = "123456")
            String regNo,

            @Schema(description = "등록공고_일자", example = "20260109")
            String regAnnounceDate,

            @Schema(description = "등록공고_번호", example = "123456")
            String regAnnounceNo,

            @Schema(description = "연차관리_감면율", example = "50")
            CommonRecordResponse.CodeInfo annuityReducRate,

            @Schema(description = "연차관리_차수", example = "5")
            String annuityYear,

            @Schema(description = "연차관리_정상마감일", example = "20260109")
            String standardDeadline,

            @Schema(description = "연차관리_과태마감일", example = "20260109")
            String penaltyDeadline,

            @Schema(description = "연차관리_회복", example = "20260109")
            String recoveryDeadline,

            @Schema(description = "연차위임_일자", example = "20260109")
            String annuityOrderDate,

            @Schema(description = "연차위임_업체", example = "mindpro")
            String annuityAgency
    ) {}

    @Schema(description = "요약/청구 탭")
    public record ClaimSummaryInfo(

            @Schema(description = "요약내용", example = "요약내용입니다.")
            String summary,

            @Schema(description = "청구범위", example = "청구범위 내용입니다.")
            String claimScope
    ) {}

    @Schema(description = "비고")
    public record AppNote(

            @Schema(description = "비고", example = "testVal")
            String note
    ) {}
}
