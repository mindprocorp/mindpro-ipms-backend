package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaIndividualAppRequest.java
 * @since : 2026. 3. 9.
 */
public class OverseaIndividualHardIpAppRequest {
    public record CreateHardIpRequest(
            @Schema(description = "출원 식별키(update 시 필요.)")
            String appSeq,

            @Schema(description = "해외 기본 식별키")
            String appExtSeq,

            @Schema(description = "출원기본정보")
            HardIpAppBaseInfo appBaseInfo,

            @Schema(description = "출원 사건관리")
            HardIpAppCaseMng appCaseMng,

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

            @Schema(description = "비고")
            HardIpAppNote appNote,

            @Schema(description = "출원 행정관리")
            HardIpAppManagement appManagement,

            @Schema(description = "등록/권리유지 관리")
            HardIpAppMaintenance appMaintenance,

            @Schema(description = "요약/청구 탭")
            ClaimSummaryInfo claimSummaryInfo

    ) {}

    @Builder
    public record HardIpAppCaseMng(
            @Schema(description = "출원루트")
            CommonRecordResponse.CodeInfo appRoute,

//            @Schema(description = "구분(내국/외국)", example = "20")
//            CommonRecordResponse.CodeInfo category,

            @Schema(description = "권리")
            CommonRecordResponse.CodeInfo rightType,

            @Schema(description = "출원구분")
            CommonRecordResponse.CodeInfo appCategory,

            @Schema(description = "출원국정보")
            CommonRecordResponse.CodeInfo appCountryInfo,

            @Schema(description = "출원국명")
            String appCountry,

//            @Schema(description = "출원종류", example = "10")
//            CommonRecordResponse.CodeInfo appType,

            @Schema(description = "OurRef", example = "REF-2026-001")
            String ourRef,

            @Schema(description = "YourRef", example = "YOUR-2026-US-01")
            String yourRef,

            @Schema(description = "출원인관리번호", example = "APPMNG-001")
            String clientRef,

            @Schema(description = "접수일", example = "20260130")
            String receiptDate
    ) {}

    @Builder
    public record HardIpAppBaseInfo(
            @Schema(description = "출원지시일", example = "20260109")
            String appOrderDate,

            @Schema(description = "출원마감일", example = "20260109")
            String appDeadline,

            @Schema(description = "오더발송일", example = "20260115")
            String oaDeliveryDate,

            @Schema(description = "출원일", example = "20260109")
            String appDate,

            @Schema(description = "출원번호", example = "123456")
            String appNo
    ) {}

    @Builder
    @Schema(description = "담당 정보")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record HardIpAppManagerInfo(
            @Schema(description = "부서", example = "해외관리팀")
            String deptCode,

            @Schema(description = "출원인담당")
            CommonRecordResponse.PersonInfo applicantContactInfo,

            @Schema(description = "관리담당자")
            CommonRecordResponse.PersonInfo adminMgrInfo,

            @Schema(description = "사건담당자")
            CommonRecordResponse.PersonInfo caseMgrInfo,

            @Schema(description = "담당변리사")
            CommonRecordResponse.PersonInfo attorneyInfo
    ) {}

    /* --- 당사자 정보 --- */
    @Builder
    @Schema(description = "당사자_정보")
    public record HardIpAppCounterPartyInfo(
            @Schema(description = "해외대리인")
            List<CommonRecordResponse.CounterPartyInfo> foreignAgentInfo,

            @Schema(description = "의뢰인")
            List<CommonRecordResponse.CounterPartyInfo> clientInfo,

            @Schema(description = "출원인")
            List<CommonRecordResponse.CounterPartyInfo> applicantInfo,

            @Schema(description = "발명자/고안자")
            CommonRecordResponse.PersonInfo inventorInfo,

            @Schema(description = "등록권리자")
            List<CommonRecordResponse.CounterPartyInfo> regMgrInfo
    ) {}

    @Schema(description = "명칭_정보")
    public record HardIpAppNameInfo(
            @Schema(description = "국문 명칭", example = "차세대 반도체 제조 장치")
            String titleKo,

            @Schema(description = "영문 명칭", example = "Next-gen Semiconductor Manufacturing Device")
            String titleEn
    ) {}

    @Builder
    public record HardIpAppSpecificElement(
            @Schema(description = "등급")
            CommonRecordResponse.CodeInfo grade,

            @Schema(description = "독립항", example = "5")
            String independentClaims,

            @Schema(description = "종속항", example = "15")
            String dependentClaims,

            @Schema(description = "국내명세서(국내명세서 페이지수)", example = "20")
            String specPage,

            @Schema(description = "도면 수", example = "10")
            String drawingCount,

            @Schema(description = "명세서(해외명세서 페이지수)", example = "30")
            String overseaSpecPage
    ) {}

    @Schema(description = "가출원_정보")
    public record ProvisionalAppInfo(
            @Schema(description = "가출원일", example = "20250601")
            String provisionalAppDate,

            @Schema(description = "가출원번호", example = "63/123,456")
            String provisionalAppNo
    ) {}

    @Schema(description = "최초출원_정보")
    public record FirstAppInfo(
            @Schema(description = "최초출원일", example = "20250109")
            String firstAppDate,

            @Schema(description = "최초출원번호", example = "10-2025-0000001")
            String firstAppNo
    ) {}

    @Schema(description = "원출원_정보")
    public record OriginalAppInfo(
            @Schema(description = "원출원일(상표는 출원 기본정보에 사용)", example = "20251201")
            String originalAppDate,

            @Schema(description = "원출원번호(상표는 출원 기본정보에 사용)", example = "10-2025-0012345")
            String originalAppNo
    ) {}

    @Schema(description = "재출원_정보")
    public record ReAppInfo(
            @Schema(description = "재출원일", example = "20260215")
            String reAppDate,

            @Schema(description = "재출원번호", example = "10-2026-0054321")
            String reAppNo
    ) {}

    @Schema(description = "국제출원정보")
    public record GlobalAppInfo(
            @Schema(description = "국제출원일", example = "20251120")
            String globalAppDate,

            @Schema(description = "국제출원번호", example = "PCT/KR2025/001234")
            String globalAppNo
    ) {}

    @Builder
    public record HardIpAppStrategy(

            @Schema(description = "국제출원정보")
            GlobalAppInfo globalAppInfo,

            @Schema(description = "가출원_정보")
            ProvisionalAppInfo provisionalAppInfo,

            @Schema(description = "최초출원정보")
            FirstAppInfo firstAppInfo,

            @Schema(description = "원출원_정보")
            OriginalAppInfo originalAppInfo,

            @Schema(description = "재출원_정보")
            ReAppInfo reAppInfo
    ) {}

    @Schema(description = "비고")
    public record HardIpAppNote(
            @Schema(description = "내용", example = "빠른 심사 요청 요망")
            String note
    ) {}

    @Builder
    @Schema(description = "출원_행정관리")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record HardIpAppManagement(

            @Schema(description = "IPC분류", example = "G06Q 50/10")
            String ipcClassification,

            @Schema(description = "모등록일(해외 개국 > 특/실)", example = "20251010")
            String parentRegAppDate,

            // 심사청구 그룹
            @Schema(description = "심사청구_마감일", example = "20280130")
            String examRequestDeadline,

            @Schema(description = "심사청구_지시일", example = "20260520")
            String examRequestOrderDate,

            @Schema(description = "심사청구_청구일", example = "20260601")
            String examRequestDate,

            @Schema(description = "출원공개_일자", example = "20270730")
            String pubDate,

            @Schema(description = "출원공개_번호", example = "US-2027-0123456-A1")
            String pubNo,

            // 출원공고 그룹
            @Schema(description = "출원공고_일자", example = "20280115")
            String announcementDate,

            @Schema(description = "출원공고_번호", example = "US-11223344")
            String announcementNo,

            // 포기 그룹 (기존 별도 레코드에서 여기로 통합)
            @Schema(description = "포기_지시일", example = "20290101") String abandonOrderDate,
            @Schema(description = "포기_일자", example = "20290105") String abandonDate,
            @Schema(description = "포기_내용", example = "사업성 결여로 인한 포기") String abandonNote
    ) {}

    @Builder
    @Schema(description = "등록_권리유지_관리")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record HardIpAppMaintenance(

            @Schema(description = "최종항수", example = "15")
            Integer finalClaimCount,

            @Schema(description = "특허청지연일", example = "0")
            Integer kipoDelayDays,

            @Schema(description = "권리존속기간", example = "20460130")
            String rightPeriod,

            @Schema(description = "연차관리여부", example = "Y")
            String isAnnuityManaged,

            // 등록 그룹
            @Schema(description = "등록_결정일", example = "20280520")
            String regDecisionDate,

            @Schema(description = "등록_접수일", example = "20280525")
            String regReceiptDate,

            @Schema(description = "등록_정상마감", example = "20280820")
            String regNormalDeadline,

            @Schema(description = "등록_과태마감", example = "20290220")
            String regGraceDeadline,

            @Schema(description = "등록_지시일", example = "20280601")
            String regOrderDate,

            @Schema(description = "등록_납부일", example = "20280610")
            String regPaymentDate,

            @Schema(description = "등록_등록일", example = "20280701")
            String regDate,

            @Schema(description = "등록_등록번호", example = "US-9876543")
            String regNo,

            // 등록 공고 그룹
            @Schema(description = "등록공고_일자", example = "20280715")
            String regAnnounceDate,

            @Schema(description = "등록공고_번호", example = "US-9876543-B2")
            String regAnnounceNo,

            // 연차관리 그룹
            @Schema(description = "차기납부차수", example = "4")
            String nextPaymentInstallment,

            @Schema(description = "관리위임_일자", example = "20280801")
            String annuityOrderDate,

            @Schema(description = "관리위임_업체", example = "Dennemeyer")
            String annuityAgency,

            @Schema(description = "정상마감일", example = "20290701")
            String standardDeadline,

            @Schema(description = "과태마감일", example = "20300101")
            String penaltyDeadline
    ) {}

    public record ClaimSummaryInfo(

            @Schema(description = "요약내용", example = "요약내용입니다.")
            String summary,

            @Schema(description = "청구범위", example = "청구범위 내용입니다.")
            String claimScope
    ) {}
}
