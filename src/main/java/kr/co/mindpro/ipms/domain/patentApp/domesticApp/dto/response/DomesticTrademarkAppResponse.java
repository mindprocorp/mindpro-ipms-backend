package kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;

import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

/**
 * @author : seokho
 * @fileName : DomesticTrademarkAppResponse.java
 * @since : 2026. 3. 3.
 */
public class DomesticTrademarkAppResponse {
    public record TrademarkAppDetailResponse(
            @Schema(description = "출원 식별자")
            String appSeq,

            @Schema(description = "출원상태")
            CommonRecordResponse.CodeInfo appStatus,

            @Schema(description = "출원 사건관리")
            TrademarkAppCaseInfo appCaseMng,

            @Schema(description = "출원기본정보")
            TrademarkAppBaseInfo appBaseInfo,

            @Schema(description = "담당 정보")
            TrademarkAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            TrademarkAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭 정보")
            TrademarkAppNameInfo appNameInfo,

            @Schema(description = "물품류")
            GoodsClass goodsClass,

            @Schema(description = "출원 전략설정")
            TrademarkAppStrategy appStrategy,

            @Schema(description = "출원 행정관리")
            TrademarkAppManagement appManagement,

            @Schema(description = "등록/권리유지 관리")
            TrademarkAppMaintenance appMaintenance,

            @Schema(description = "첨부파일정보")
            List<CommonRecordResponse.FileInfo> fileInfo,

            @Schema(description = "비고")
            AppNote appNote

    ) implements DomesticAppDetailResponse {
        public static TrademarkAppDetailResponse fromVOViewTrademark(
                CommonAppVO vo,
                List<CommonRecordResponse.CounterPartyInfo> clientList,
                List<CommonRecordResponse.CounterPartyInfo> applicantList,
                List<CommonRecordResponse.CounterPartyInfo> regMgrList,
                List<PaperResponseVO> fileList
        ) {
            return new TrademarkAppDetailResponse(
                    // 1. appSeq (String)
                    vo.getAppSeq(),
                    new CommonRecordResponse.CodeInfo(vo.getStatusCode(), vo.getStatusName()),

                    // 2. appCaseMng (TrademarkAppCaseInfo)
                    new TrademarkAppCaseInfo(
                            new CommonRecordResponse.CodeInfo(vo.getAppRouteCode(), vo.getAppRouteName()),
                            new CommonRecordResponse.CodeInfo(vo.getCategoryCode(), vo.getCategoryName()),
                            new CommonRecordResponse.CodeInfo(vo.getRightTypeCode(), vo.getRightTypeName()),
                            new CommonRecordResponse.CodeInfo(vo.getAppTypeCode(), vo.getAppTypeName()),
                            new CommonRecordResponse.CodeInfo(vo.getAppCategoryCode(), vo.getAppCategoryName()),
                            formatMinusHoursString8(vo.getReceiptDate()),
                            vo.getOurRef(),
                            vo.getYourRef(),
                            vo.getClientRef(),
                            formatMinusHoursString8(vo.getDraftDeadline()),
                            formatMinusHoursString8(vo.getDraftSendDate())
                    ),

                    // 3. appBaseInfo (TrademarkAppBaseInfo)
                    new TrademarkAppBaseInfo(
                            formatMinusHoursString8(vo.getAppOrderDate()),
                            formatMinusHoursString8(vo.getAppDeadline()),
                            formatMinusHoursString8(vo.getAppDate()),
                            vo.getAppNo()
                    ),

                    // 4. appManagerInfo (TrademarkAppManagerInfo)
                    new TrademarkAppManagerInfo(
                            vo.getDeptName(),
                            new CommonRecordResponse.PersonInfo(vo.getAdminMgr(), vo.getAdminMgrNm()),
                            new CommonRecordResponse.PersonInfo(vo.getCaseMgr(), vo.getCaseMgrNm()),
                            new CommonRecordResponse.PersonInfo(vo.getAttorney(), vo.getAttorneyNm())
                    ),

                    // 5. appCounterPartyInfo (TrademarkAppCounterPartyInfo)
                    new TrademarkAppCounterPartyInfo(
                            clientList,
                            new CommonRecordResponse.PersonInfo(vo.getClientContact(), vo.getClientContactNm()),
                            applicantList,
                            regMgrList
                    ),

                    // 6. appNameInfo (TrademarkAppNameInfo)
                    new TrademarkAppNameInfo(
                            vo.getTitleKo(),
                            vo.getTitleEn()
                    ),

                    new GoodsClass(
                            vo.getGoodsClass()
                    ),

                    // 7. appStrategy (TrademarkAppStrategy)
                    new TrademarkAppStrategy(
                            new OriginalAppInfo(formatMinusHoursString8(vo.getOriginalAppDate()), vo.getOriginalAppNo()),
                            new ReAppInfo(formatMinusHoursString8(vo.getReAppDate()), vo.getReAppNo()),
                            new OriginalRegInfo(formatMinusHoursString8(vo.getOriginalRegDate()), vo.getOriginalRegNo()),
                            new MadridAppInfo(formatMinusHoursString8(vo.getMadridAppDate()), vo.getMadridAppNo()),
                            vo.getIsForeignApp(),
                            new CommonRecordResponse.CodeInfo(vo.getForeignAppTimingCode(), vo.getForeignAppTimingName()),
                            formatMinusHoursString8(vo.getForeign6mDeadline()),
                            formatMinusHoursString8(vo.getForeignAppDate()),
                            vo.getClassificAppNo()
                    ),

                    // 8. appManagement (TrademarkAppManagement)
                    new TrademarkAppManagement(
                            vo.getIsPoaSubmitted(),
                            vo.getIsTrademarkResearch(),
                            formatMinusHoursString8(vo.getPriorExamReqDate()),
                            formatMinusHoursString8(vo.getPriorExamDecDate()),
                            formatMinusHoursString8(vo.getAnnouncementDecisionDate()),
                            formatMinusHoursString8(vo.getAnnouncementDate()),
                            vo.getAnnouncementNo(),
                            formatMinusHoursString8(vo.getAbandonOrderDate()),
                            formatMinusHoursString8(vo.getAbandonDate()),
                            vo.getAbandonNote()
                    ),

                    // 9. appMaintenance (TrademarkAppMaintenance)
                    new TrademarkAppMaintenance(
                            formatMinusHoursString8(vo.getRegDecisionDate()),
                            formatMinusHoursString8(vo.getRegReceiptDate()),
                            formatMinusHoursString8(vo.getRegDate()),
                            vo.getRegNo(),
                            formatMinusHoursString8(vo.getRegAnnounceDate()),
                            vo.getRegAnnounceNo(),
                            formatMinusHoursString8(vo.getStandardDeadline()),
                            formatMinusHoursString8(vo.getPenaltyDeadline()),
                            formatMinusHoursString8(vo.getAnnuityOrderDate()),
                            vo.getAnnuityAgency(),
                            formatMinusHoursString8(vo.getPriorityDate()),
                            formatMinusHoursString8(vo.getRegNormalDeadline()),
                            vo.getAnnuityYear(),
                            vo.getIsRenewalManaged(),
                            vo.getNextPaymentInstallment(),
                            vo.getTrademarkRenewalFee(),
                            vo.getRenewalLateFee()
                    ),

                    // 10. 첨부파일
                    CommonRecordResponse.FileInfo.from(
                            fileList
                    ),

                    // 11. appNote (AppNote)
                    new AppNote(
                            vo.getNote()
                    )
            );
        }
    }

    public record TrademarkAppBaseInfo(
            @Schema(description = "출원지시일", example = "20260109")
            String appOrderDate,

            @Schema(description = "출원마감일", example = "20260109")
            String appDeadline,

            @Schema(description = "출원일", example = "20260109")
            String appDate,

            @Schema(description = "출원번호", example = "123456")
            String appNo
    ) {}

    public record TrademarkAppCaseInfo(
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

    public record TrademarkAppManagerInfo(
            @Schema(description = "부서", example = "test")
            String deptCode,

            @Schema(description = "관리담당자", example = "USERIF20260000001")
            CommonRecordResponse.PersonInfo adminMgrInfo,

            @Schema(description = "사건담당자", example = "USERIF20260000001")
            CommonRecordResponse.PersonInfo caseMgrInfo,

            @Schema(description = "담당변리사", example = "USERIF20260000001")
            CommonRecordResponse.PersonInfo attorneyInfo
    ) {}

    public record TrademarkAppCounterPartyInfo(
            @Schema(description = "의뢰인 이름")
            List<CommonRecordResponse.CounterPartyInfo> clientInfo,

            @Schema(description = "의뢰인 담당자", example = "USERIF20260000001")
            CommonRecordResponse.PersonInfo clientContactInfo,

            @Schema(description = "출원인 이름")
            List<CommonRecordResponse.CounterPartyInfo> applicantInfo,

            @Schema(description = "등록권리자 이름")
            List<CommonRecordResponse.CounterPartyInfo> regMgrInfo
    ) {}

    public record TrademarkAppNameInfo(
            @Schema(description = "국문명칭", example = "testVal")
            String titleKo,

            @Schema(description = "영문명칭", example = "testVal")
            String titleEn
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

    @Schema(description = "원등록_정보")
    public record OriginalRegInfo(
            @Schema(description = "원등록일", example = "20251201")
            String originalRegDate,

            @Schema(description = "원등록번호", example = "parent123456")
            String originalRegNo
    ) {}

    @Schema(description = "마드리드_출원_정보")
    public record MadridAppInfo(
            @Schema(description = "국제등록일", example = "20260301")
            String madridAppDate,

            @Schema(description = "국제등록번호", example = "madrid123456")
            String madridAppNo
    ) {}

    public record TrademarkAppStrategy(
            @Schema(description = "원출원_정보")
            OriginalAppInfo originalAppInfo,

            @Schema(description = "재출원_정보")
            ReAppInfo reAppInfo,

            @Schema(description = "원등록정보")
            OriginalRegInfo originalRegInfo,

            @Schema(description = "마드리드_출원_정보")
            MadridAppInfo madridAppInfo,

            @Schema(description = "해외출원_여부", example = "Y")
            String isForeignApp,

            @Schema(description = "해외출원_동시_추후", example = "10")
            CommonRecordResponse.CodeInfo foreignAppTiming,

            @Schema(description = "해외출원_6월마감", example = "20260109")
            String foreign6mDeadline,

            @Schema(description = "해외출원_출원일", example = "20260109")
            String foreignAppDate,

            @Schema(description = "분류출원번호", example = "classificAppNo123456")
            String classificAppNo
    ) {}

    public record TrademarkAppManagement(
            @Schema(description = "위임장_제출_여부", example = "Y")
            String isPoaSubmitted,

            @Schema(description = "상표_조사_여부", example = "Y")
            String isTrademarkResearch,

            @Schema(description = "우선심사_청구일", example = "20260109")
            String priorExamReqDate,

            @Schema(description = "우선심사_결정일", example = "20260109")
            String priorExamDecDate,

            @Schema(description = "출원공고_결정일", example = "20280115")
            String announcementDecisionDate,

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

    public record TrademarkAppMaintenance(
            @Schema(description = "등록_결정일", example = "20260109")
            String regDecisionDate,

            @Schema(description = "등록_접수일", example = "20260109")
            String regReceiptDate,

            @Schema(description = "등록_등록일", example = "20260109")
            String regDate,

            @Schema(description = "등록_등록번호", example = "123456")
            String regNo,

            @Schema(description = "등록공고_일자", example = "20260109")
            String regAnnounceDate,

            @Schema(description = "등록공고_번호", example = "123456")
            String regAnnounceNo,

            @Schema(description = "상표 - 마감관리 > 정상마감일", example = "20260109")
            String standardDeadline,

            @Schema(description = "상표 - 마감관리 > 과태마감일", example = "20260109")
            String penaltyDeadline,

            @Schema(description = "연차위임_일자", example = "20260109")
            String annuityOrderDate,

            @Schema(description = "연차위임_업체", example = "mindpro")
            String annuityAgency,

            @Schema(description = "기연일(기간연장된날짜 - 상표전용)", example = "20260109")
            String priorityDate,

            @Schema(description = "등록_정상_마감", example = "20260109")
            String regNormalDeadline,

            @Schema(description = "연차관리_차수", example = "5")
            String annuityYear,

            @Schema(description = "갱신관리여부", example = "Y")
            String isRenewalManaged,

            @Schema(description = "차기납부차수/차기갱신차수", example = "4")
            String nextPaymentInstallment,

            @Schema(description = "갱신등록료", example = "500000")
            String trademarkRenewalFee,

            @Schema(description = "갱신과태료", example = "500000")
            String renewalLateFee
    ) {}

    @Schema(description = "물품류(Class)")
    public record GoodsClass(
            @Schema(description = "물품류", example = "제9류")
            String goodsClass
    ) {}

    @Schema(description = "비고")
    public record AppNote(

            @Schema(description = "비고", example = "testVal")
            String note
    ) {}
}
