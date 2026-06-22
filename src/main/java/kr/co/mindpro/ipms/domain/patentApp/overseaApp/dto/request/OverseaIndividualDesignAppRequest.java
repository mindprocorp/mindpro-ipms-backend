package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaIndividualDesignAppRequest.java
 * @since : 2026. 3. 10.
 */
public class OverseaIndividualDesignAppRequest {
    public record CreateDesignAppRequest(
            @Schema(description = "출원 식별키(update 시 필요.)")
            String appSeq,

            @Schema(description = "해외 기본 식별키")
            String appExtSeq,

            @Schema(description = "출원사건정보")
            DesignAppCaseMng appCaseMng,

            @Schema(description = "출원기본정보")
            DesignAppBaseInfo appBaseInfo,

            @Schema(description = "담당자정보")
            DesignAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            DesignAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭 정보")
            DesignAppNameInfo appNameInfo,

            @Schema(description = "전략 설정")
            DesignAppStrategy appStrategy,

            @Schema(description = "이미지 파일")
            DesignAppImageFile appImageFile,

            @Schema(description = "비고")
            HardIpAppNote appNote,

            @Schema(description = "출원 행정관리")
            DesignAppManagement appManagement,

            @Schema(description = "등록/권리유지 관리")
            DesignAppMaintenance appMaintenance,

            @Schema(description = "디자인 설명/요점")
            DesignDescription designDescription
    ) {}

    @Builder
    public record DesignAppCaseMng(
            @Schema(description = "출원루트")
            CommonRecordResponse.CodeInfo appRoute,

            @Schema(description = "권리")
            CommonRecordResponse.CodeInfo rightType,

            @Schema(description = "출원구분")
            CommonRecordResponse.CodeInfo appCategory,

            @Schema(description = "출원국정보")
            CommonRecordResponse.CodeInfo appCountryInfo,

            @Schema(description = "출원국명")
            String appCountry,

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
    public record DesignAppBaseInfo(
            @Schema(description = "출원마감일", example = "20260109")
            String appDeadline,

            @Schema(description = "오더발송일", example = "20260115")
            String oaDeliveryDate,

            @Schema(description = "출원일", example = "20260109")
            String appDate,

            @Schema(description = "출원번호", example = "123456")
            String appNo,

            @Schema(description = "공지예외적용")
            CommonRecordResponse.CodeInfo noticeExceptionApply
    ) {}

    @Builder
    public record DesignAppManagerInfo(
            @Schema(description = "부서", example = "해외관리팀")
            String deptCode,

            @Schema(description = "관리담당자")
            CommonRecordResponse.PersonInfo adminMgrInfo,

            @Schema(description = "사건담당자")
            CommonRecordResponse.PersonInfo caseMgrInfo,

            @Schema(description = "담당변리사")
            CommonRecordResponse.PersonInfo attorneyInfo
    ) {}

    @Builder
    public record DesignAppCounterPartyInfo(
            @Schema(description = "해외대리인")
            List<CommonRecordResponse.CounterPartyInfo> foreignAgentInfo,

            @Schema(description = "의뢰인")
            List<CommonRecordResponse.CounterPartyInfo> clientInfo,

            @Schema(description = "출원인")
            List<CommonRecordResponse.CounterPartyInfo> applicantInfo,

            @Schema(description = "발명자/고안자")
            CommonRecordResponse.PersonInfo inventorInfo,

            @Schema(description = "출원담당자")
            CommonRecordResponse.PersonInfo appManagerInfo,

            @Schema(description = "등록권리자")
            List<CommonRecordResponse.CounterPartyInfo> regMgrInfo
    ) {}

    @Schema(description = "명칭_정보")
    public record DesignAppNameInfo(
            @Schema(description = "국문 명칭", example = "차세대 반도체 제조 장치")
            String titleKo,

            @Schema(description = "영문 명칭", example = "Next-gen Semiconductor Manufacturing Device")
            String titleEn
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

    @Schema(description = "원등록_정보")
    public record OriginalRegInfo(
            @Schema(description = "원등록일", example = "20251201")
            String originalRegDate,

            @Schema(description = "원등록번호", example = "parent123456")
            String originalRegNo
    ) {}

    @Builder
    public record DesignAppStrategy(
            @Schema(description = "최초출원정보")
            FirstAppInfo firstAppInfo,

            @Schema(description = "원출원_정보")
            OriginalAppInfo originalAppInfo,

            @Schema(description = "재출원_정보")
            ReAppInfo reAppInfo,

            @Schema(description = "모등록일(해외 개국 > 디자인)", example = "20251010")
            String parentRegAppDate,

            @Schema(description = "모등록번호(해외 개국 > 디자인)", example = "parent123456")
            String parentRegAppNo,

            @Schema(description = "원등록_정보")
            OriginalRegInfo originalRegInfo
    ) {}

    @Schema(description = "이미지_파일")
    public record DesignAppImageFile(
            @Schema(description = "대표도/이미지 파일", example = "drawing_main.jpg")
            String mainImgFile
    ) {}

    @Schema(description = "비고")
    public record HardIpAppNote(
            @Schema(description = "내용", example = "빠른 심사 요청 요망")
            String note
    ) {}

    @Builder
    public record DesignAppManagement(
            @Schema(description = "출원공개_일자", example = "20270730")
            String pubDate,

            @Schema(description = "출원공개_번호", example = "US-2027-0123456-A1")
            String pubNo,

            @Schema(description = "포기_지시일", example = "20290101")
            String abandonOrderDate,

            @Schema(description = "포기_일자", example = "20290105")
            String abandonDate,

            @Schema(description = "포기_내용", example = "사업성 결여로 인한 포기")
            String abandonNote
    ) {}

    @Builder
    public record DesignAppMaintenance(
            @Schema(description = "특허청지연일", example = "0")
            Integer kipoDelayDays,

            @Schema(description = "권리존속기간", example = "20460130")
            String rightPeriod,

            @Schema(description = "연차관리여부", example = "Y")
            String isAnnuityManaged,

            @Schema(description = "등록_등록일", example = "20280701")
            String regDate,

            @Schema(description = "등록_등록번호", example = "US-9876543")
            String regNo,

            @Schema(description = "물품류")
            GoodsClass goodsClass,

            // 등록 공고 그룹
            @Schema(description = "등록공고_일자", example = "20280715")
            String regAnnounceDate,

            @Schema(description = "등록공고_번호", example = "US-9876543-B2")
            String regAnnounceNo,

            // 연차관리 그룹
            @Schema(description = "차기납부차수/차기갱신차수", example = "4")
            String nextPaymentInstallment,

            @Schema(description = "관리위임_일자", example = "20280801")
            String annuityOrderDate,

            @Schema(description = "관리위임_업체", example = "Dennemeyer")
            String annuityAgency,

            @Schema(description = "정상마감일", example = "20290701")
            String standardDeadline,

            @Schema(description = "과태마감일", example = "20300101")
            String penaltyDeadline,

            // 등록 그룹
            @Schema(description = "등록_결정일", example = "20280520")
            String regDecisionDate,

            @Schema(description = "등록_정상마감", example = "20280820")
            String regNormalDeadline,

            @Schema(description = "등록_과태마감", example = "20290220")
            String regGraceDeadline,

            @Schema(description = "등록_지시일", example = "20280601")
            String regOrderDate,

            @Schema(description = "등록_납부일", example = "20280610")
            String regPaymentDate
    ) {}

    @Schema(description = "설명/요점")
    public record DesignDescription(
            @Schema(description = "디자인 설명", example = "본 디자인은 ~형상이다.")
            String designDescription,

            @Schema(description = "디자인 요약", example = "디자인 창작 내용 요약")
            String designSummary
    ) {}

    @Schema(description = "물품류")
    public record GoodsClass(
            @Schema(description = "물품류", example = "09류")
            String goodsClass
    ) {}
}
