package kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;

import java.util.List;

/**
 * @author : seokho
 * @fileName : DomesticDesignAppRequest.java
 * @since : 2026. 2. 27.
 * description : 국내출원 디자인 등록 요청 레코드
 */
public class DomesticDesignAppRequest {

    public record CreateDomesticDesignAppRequest(
            @Schema(description = "출원 식별키(update 시 필요.)")
            String appSeq,

            @Schema(description = "출원_사건관리")
            DesignAppCaseMng appCaseMng,

            @Schema(description = "출원기본정보")
            DesignAppBaseInfo appBaseInfo,

            @Schema(description = "담당 정보")
            DesignAppManagerInfo appManagerInfo,

            @Schema(description = "당사자_정보")
            DesignAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭_정보")
            DesignAppNameInfo appNameInfo,

            @Schema(description = "출원_전략설정")
            DesignAppStrategy appStrategy,

            @Schema(description = "출원_행정관리")
            DesignAppManagement appManagement,

            @Schema(description = "등록/권리유지_관리")
            DesignAppMaintenance appMaintenance,

            @Schema(description = "물품류")
            GoodsClass goodsClass,

            @Schema(description = "디자인 설명/요점")
            DesignDescription designDescription,

            @Schema(description = "비고")
            AppNote appNote
    ) {}

    public record DesignAppBaseInfo(
            @Schema(description = "출원지시일", example = "20260109")
            String appOrderDate,

            @Schema(description = "출원마감일", example = "20260109")
            String appDeadline,

            @Schema(description = "출원일", example = "20260109")
            String appDate,

            @Schema(description = "출원번호", example = "123456")
            String appNo,

            @Schema(description = "접근코드", example = "test")
            String accessCode
    ) {}

    public record DesignAppCaseMng(
//            @Schema(description = "출원루트")
//            CommonRecordResponse.CodeInfo appRoute,

            @Schema(description = "구분(내국/외국)")
            CommonRecordResponse.CodeInfo category,

            @Schema(description = "권리")
            CommonRecordResponse.CodeInfo rightType,

            @Schema(description = "출원종류")
            CommonRecordResponse.CodeInfo appType,

            @Schema(description = "출원구분(등록,분할,분리 등)")
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

    public record DesignAppManagerInfo(
            @Schema(description = "부서", example = "test")
            String deptCode,

            @Schema(description = "관리담당자")
            CommonRecordResponse.PersonInfo adminMgrInfo,

            @Schema(description = "사건담당자")
            CommonRecordResponse.PersonInfo caseMgrInfo,

            @Schema(description = "담당변리사")
            CommonRecordResponse.PersonInfo attorneyInfo
    ) {}

    public record DesignAppCounterPartyInfo(
            @Schema(description = "의뢰인")
            List<CommonRecordResponse.CounterPartyInfo> clientInfo,

            @Schema(description = "의뢰인 담당자")
            CommonRecordResponse.PersonInfo clientContactInfo,

            @Schema(description = "출원인")
            List<CommonRecordResponse.CounterPartyInfo> applicantInfo,

            @Schema(description = "발명자")
            CommonRecordResponse.PersonInfo inventorInfo,

            @Schema(description = "등록권리자")
            List<CommonRecordResponse.CounterPartyInfo> regMgrInfo
    ) {}

    public record DesignAppNameInfo(
            @Schema(description = "제안명칭", example = "제안명칭")
            String proposal,

            @Schema(description = "국문명칭", example = "국문명칭")
            String titleKo,

            @Schema(description = "영문명칭", example = "영문명칭")
            String titleEn,

            @Schema(description = "기타_표기_명칭", example = "기타표기명칭")
            String etcTitle
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

    public record DesignAppStrategy(
            @Schema(description = "원출원_정보")
            OriginalAppInfo originalAppInfo,

            @Schema(description = "재출원_정보")
            ReAppInfo reAppInfo,

            @Schema(description = "원등록정보")
            OriginalRegInfo originalRegInfo,

            @Schema(description = "해외출원_여부", example = "Y")
            String isForeignApp,

            @Schema(description = "해외출원_동시_추후")
            CommonRecordResponse.CodeInfo foreignAppTiming,

            @Schema(description = "해외출원_6월마감", example = "20260109")
            String foreign6mDeadline,

            @Schema(description = "해외출원_출원일", example = "20260109")
            String foreignAppDate
    ) {}

    public record DesignAppManagement(
            @Schema(description = "위임장_제출_여부", example = "Y")
            String isPoaSubmitted,

            @Schema(description = "IPC_분류", example = "testVal")
            String ipcClassification,

            @Schema(description = "조기공개신청일", example = "20260109")
            String earlyPubRequestDate,

            @Schema(description = "부분_디자인_여부", example = "Y")
            String isPartialDesign,

            @Schema(description = "우선심사_청구일", example = "20260109")
            String priorExamReqDate,

            @Schema(description = "우선심사_결정일", example = "20260109")
            String priorExamDecDate,

            @Schema(description = "출원공개_일자", example = "20260109")
            String pubDate,

            @Schema(description = "출원공개_번호", example = "123456")
            String pubNo,

            @Schema(description = "포기_지시일", example = "20260109")
            String abandonOrderDate,

            @Schema(description = "포기_일자", example = "20260109")
            String abandonDate,

            @Schema(description = "포기_내용", example = "testVal")
            String abandonNote
    ) {}

    public record DesignAppMaintenance(
            @Schema(description = "등록/권리유지_다의장(물품수, 복수디자인 수)", example = "432")
            Integer multiDesign,

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

            @Schema(description = "등록_감면율")
            CommonRecordResponse.CodeInfo regReductionRate,

            @Schema(description = "등록_등록일", example = "20260109")
            String regDate,

            @Schema(description = "등록_등록번호", example = "123456")
            String regNo,

            @Schema(description = "등록공고_일자", example = "20260109")
            String regAnnounceDate,

            @Schema(description = "등록공고_번호", example = "123456")
            String regAnnounceNo,

            @Schema(description = "연차관리_감면율")
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

    @Schema(description = "물품류(Class)")
    public record GoodsClass(
            @Schema(description = "물품류", example = "제9류")
            String goodsClass
    ) {}

    @Schema(description = "설명/요점")
    public record DesignDescription(

            @Schema(description = "디자인 설명", example = "디자인 설명입니다.")
            String designDescription,

            @Schema(description = "디자인 요약", example = "디자인 요약입니다.")
            String designSummary
    ) {}

    @Schema(description = "비고")
    public record AppNote(
            @Schema(description = "비고", example = "testVal")
            String note
    ) {}
}
