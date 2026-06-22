package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaPctAppRequest.java
 * @since : 2026. 3. 11.
 */
public class OverseaPctAppRequest {
    public record CreatePctAppRequest(
            @Schema(description = "출원 식별자")
            String appSeq,

            @Schema(description = "해외 기본 식별자", example = "EXTMST20260000014")
            String appExtSeq,

            @Schema(description = "출원 사건 관리")
            PctAppCaseMng appCaseMng,

            @Schema(description = "출원 기본 정보")
            PctAppBaseInfo appBaseInfo,

            @Schema(description = "담당 정보")
            PctAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            PctAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭 정보")
            PctAppNameInfo appNameInfo,

            @Schema(description = "출원 전략설정")
            PctAppStrategy appStrategy,

            @Schema(description = "비고")
            AppNote appNote,

            @Schema(description = "출원 행정관리")
            PctAppManagement appManagement,

            @Schema(description = "등록 및 권리유지 관리")
            PctAppMaintenance appMaintenance,

            @Schema(description = "요약/청구 탭 정보")
            ClaimSummaryInfo claimSummaryInfo
    ) {}

    @Builder
    public record PctAppCaseMng(
            @Schema(description = "출원루트")
            CommonRecordResponse.CodeInfo appRoute,        // 공통코드 컬럼명 = IP_PROC_TYPE

            @Schema(description = "구분(내국/외국)")
            CommonRecordResponse.CodeInfo category,

            @Schema(description = "권리")
            CommonRecordResponse.CodeInfo rightType,

            @Schema(description = "출원구분")
            CommonRecordResponse.CodeInfo appCategory,

            @Schema(description = "OurRef", example = "REF-2026-001")
            String ourRef,

            @Schema(description = "출원인관리번호", example = "APPMNG-001")
            String clientRef,

            @Schema(description = "접수일", example = "20260130")
            String receiptDate
    ) {}

    @Builder
    public record PctAppBaseInfo(
            @Schema(description = "출원지시일", example = "20260110")
            String appOrderDate,

            @Schema(description = "출원마감일", example = "20260210")
            String appDeadline,

            @Schema(description = "공지예외적용")
            CommonRecordResponse.CodeInfo noticeExceptionApply,

            @Schema(description = "출원일", example = "20260130")
            String appDate,

            @Schema(description = "출원번호", example = "10-2026-1234567")
            String appNo
    ) {}

    @Builder
    public record PctAppManagerInfo(
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

    @Builder
    public record PctAppCounterPartyInfo(
            @Schema(description = "의뢰인")
            List<CommonRecordResponse.CounterPartyInfo> clientInfo,

            @Schema(description = "출원인")
            List<CommonRecordResponse.CounterPartyInfo> applicantInfo,

            @Schema(description = "발명자/고안자")
            CommonRecordResponse.PersonInfo inventorInfo
    ) {}

    @Schema(description = "명칭_정보")
    public record PctAppNameInfo(
            @Schema(description = "국문 명칭", example = "차세대 반도체 제조 장치")
            String titleKo,

            @Schema(description = "영문 명칭", example = "Next-gen Semiconductor Manufacturing Device")
            String titleEn
    ) {}

    @Schema(description = "20개월 마감 정보")
    public record Deadline20Info(

            @Schema(description = "완료여부", example = "N")
            String complete20Yn,

            @Schema(description = "국내진입마감일 - NPE(National Phase Entry)", example = "20270830")
            String npe20Deadline,

            @Schema(description = "진입완료일", example = "20270801")
            String entry20CompleteDate,

            @Schema(description = "출원국가", example = "[\"US\", \"JP\"]")
            List<String> app20Country

    ) {}

    @Schema(description = "30개월 마감 정보")
    public record Deadline30Info(

            @Schema(description = "완료여부", example = "N")
            String complete30Yn,

            @Schema(description = "국내진입마감일 - NPE(National Phase Entry)", example = "20280630")
            String npe30Deadline,

            @Schema(description = "진입완료일", example = "20280601")
            String entry30CompleteDate,

            @Schema(description = "출원국가", example = "[\"EP\", \"DE\"]")
            List<String> app30Country

    ) {}

    @Builder
    public record PctAppStrategy(
            @Schema(description = "지정국가(kr 지정 유무)", example = "N")
            String krDesignationYn,

            Deadline20Info deadline20Info,

            Deadline30Info deadline30Info
    ) {}

    @Schema(description = "비고")
    public record AppNote(
            @Schema(description = "내용", example = "빠른 심사 요청 요망")
            String note
    ) {}

    public record PctFilingFeeInfo(
            @Schema(description = "마감일", example = "20260301")
            String filingFeeDeadline,

            @Schema(description = "제출일", example = "20260228")
            String filingFeePayDate
    ) {}

    public record InternationalSearchInfo(
            @Schema(description = "접수일", example = "20260401")
            String isaReceiptDate,

            @Schema(description = "보고일", example = "20260701")
            String isrReportDate,

            @Schema(description = "결과", example = "긍정적")
            String searchResult
    ) {}

    @Builder
    public record PctAppManagement(
            @Schema(description = "포기_지시일", example = "20290101")
            String abandonOrderDate,

            @Schema(description = "포기_일자", example = "20290105")
            String abandonDate,

            @Schema(description = "포기_내용", example = "사업성 결여로 인한 포기")
            String abandonNote,

            @Schema(description = "수수료 납부 정보")
            PctFilingFeeInfo pctFilingFeeInfo,

            @Schema(description = "국제 조사 정보")
            InternationalSearchInfo internationalSearchInfo
    ) {}

    public record PctIpeInfo(
            @Schema(description = "마감일", example = "20271001")
            String ipeDeadline,

            @Schema(description = "청구일", example = "20270901")
            String ipeRequestDate,

            @Schema(description = "보고일", example = "20280101")
            String ipeReportDate
    ) {}

    public record IntlPubInfo(
            @Schema(description = "접수일", example = "20270501")
            String intlReceiptDate,

            @Schema(description = "일자", example = "20271101")
            String intlPubDate,

            @Schema(description = "번호", example = "WO2027/123456")
            String intlPubNo
    ) {}

    @Builder
    public record PctAppMaintenance(
            @Schema(description = "예비심사 정보(International Preliminary Examination)")
            PctIpeInfo pctIpeInfo,

            @Schema(description = "국제공개")
            IntlPubInfo intlPubInfo
    ) {}

    @Schema(description = "요약/청구 탭")
    public record ClaimSummaryInfo(

            @Schema(description = "요약내용", example = "요약내용입니다.")
            String summary,

            @Schema(description = "청구범위", example = "청구범위 내용입니다.")
            String claimScope
    ) {}
}
