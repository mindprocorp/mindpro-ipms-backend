package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaMadridAppRequest.java
 * @since : 2026. 3. 11.
 */
public class OverseaMadridAppRequest {
    public record CreateMadridRequest(
            @Schema(description = "출원 식별자")
            String appSeq,

            @Schema(description = "해외 기본 식별자", example = "EXTMST20260000014")
            String appExtSeq,

            @Schema(description = "출원 사건관리")
            MadridAppCaseMng appCaseMng,

            @Schema(description = "출원기본정보")
            MadridAppBaseInfo appBaseInfo,

            @Schema(description = "담당 정보")
            MadridAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            MadridAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭 정보")
            MadridAppNameInfo appNameInfo,

            @Schema(description = "물품류(class)")
            GoodsClass goodsClass,

            @Schema(description = "출원 전략설정")
            MadridAppStrategy appStrategy,

            @Schema(description = "비고")
            AppNote appNote,

            @Schema(description = "출원 행정관리")
            MadridAppManagement appManagement,

            @Schema(description = "등록/권리유지 관리")
            MadridAppMaintenance appMaintenance
    ) {}

    @Builder
    public record MadridAppCaseMng(
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

            @Schema(description = "YourRef", example = "YOUR-2026-US-01")
            String yourRef,

            @Schema(description = "출원인관리번호", example = "APPMNG-001")
            String clientRef,

            @Schema(description = "접수일", example = "20260130")
            String receiptDate
    ) {}

    @Builder
    public record MadridAppBaseInfo(
            @Schema(description = "공지예외적용")
            CommonRecordResponse.CodeInfo noticeExceptionApply,

            @Schema(description = "출원마감일", example = "20260210")
            String appDeadline,

            @Schema(description = "출원일", example = "20260130")
            String appDate,

            @Schema(description = "출원번호", example = "10-2026-1234567")
            String appNo,

            @Schema(description = "특허청참조번호", example = "AUTH-KR-2026-001")
            String authorityRefNo,

            @Schema(description = "자동보호 결정일", example = "20270101")
            String autoProtectionDate,

            @Schema(description = "출원공고_일자", example = "20280115")
            String announcementDate,

            @Schema(description = "출원공고_번호", example = "US-11223344")
            String announcementNo
    ) {}

    @Builder
    public record MadridAppManagerInfo(
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
    public record MadridAppCounterPartyInfo(
            @Schema(description = "의뢰인")
            List<CommonRecordResponse.CounterPartyInfo> clientInfo,

            @Schema(description = "출원인")
            List<CommonRecordResponse.CounterPartyInfo> applicantInfo
    ) {}

    @Schema(description = "명칭_정보")
    public record MadridAppNameInfo(
            @Schema(description = "국문 명칭", example = "차세대 반도체 제조 장치")
            String titleKo,

            @Schema(description = "영문 명칭", example = "Next-gen Semiconductor Manufacturing Device")
            String titleEn
    ) {}

    public record GoodsClass(
            @Schema(description = "물품류", example = "09류")
            String goodsClass
    ) {}

    public record OriginalRegInfo(
            @Schema(description = "원등록일", example = "20251201")
            String originalRegDate,

            @Schema(description = "원등록번호", example = "parent123456")
            String originalRegNo
    ) {}

    @Builder
    public record MadridAppStrategy(
            @Schema(description = "원등록_정보")
            OriginalRegInfo originalRegInfo,

            @Schema(description = "지정국가", example = "[\"HAGUE\"]")
            List<String> designated,

            @Schema(description = "사후 지정국가", example = "[\"MADRID\"]")
            List<String> subsequent,

            @Schema(description = "등록국가", example = "[\"DE\", \"FR\", \"GB\"]")
            List<String> registeredStates
    ) {}

    @Schema(description = "비고")
    public record AppNote(
            @Schema(description = "내용", example = "빠른 심사 요청 요망")
            String note
    ) {}

    @Builder
    public record MadridAppManagement(
            @Schema(description = "포기_접수일", example = "20290102")
            String abandonReceiptDate,

            @Schema(description = "포기_일자", example = "20290105")
            String abandonDate,

            @Schema(description = "포기_내용", example = "사업성 결여로 인한 포기")
            String abandonNote
    ) {}

    @Schema(description = "국내등록")
    public record DomesticRegInfo(
            @Schema(description = "일자", example = "20251231")
            String domesticRegDate,

            @Schema(description = "번호", example = "10-1234567-0000")
            String domesticRegNo
    ) {}

    @Builder
    public record MadridAppMaintenance(
            @Schema(description = "등록_등록일", example = "20280701")
            String regDate,

            @Schema(description = "등록_등록번호", example = "US-9876543")
            String regNo,

            @Schema(description = "갱신차수", example = "3")
            String paymentInstallment,

            @Schema(description = "관리위임_일자", example = "20280801")
            String annuityOrderDate,

            @Schema(description = "관리위임_업체", example = "Dennemeyer")
            String annuityAgency,

            @Schema(description = "정상마감일", example = "20290701")
            String standardDeadline,

            @Schema(description = "과태마감일", example = "20300101")
            String penaltyDeadline,

            @Schema(description = "국내등록")
            DomesticRegInfo domesticRegInfo
    ) {}
}
