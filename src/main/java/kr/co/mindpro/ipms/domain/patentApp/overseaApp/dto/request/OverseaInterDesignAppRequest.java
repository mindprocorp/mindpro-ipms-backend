package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaInterDesignAppRequest.java
 * @since : 2026. 3. 12.
 */
public class OverseaInterDesignAppRequest {
    public record CreateInterDesignAppRequest(
            @Schema(description = "출원 식별자")
            String appSeq,

            @Schema(description = "해외 기본 식별자", example = "EXTMST20260000014")
            String appExtSeq,

            @Schema(description = "출원 사건관리")
            InterDesignAppCaseMng appCaseMng,

            @Schema(description = "출원기본정보")
            InterDesignAppBaseInfo appBaseInfo,

            @Schema(description = "담당 정보")
            InterDesignAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            InterDesignAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭_정보")
            InterDesignAppNameInfo appNameInfo,

            @Schema(description = "출원 전략설정")
            InterDesignAppStrategy appStrategy,

            @Schema(description = "설명/요점")
            InterDesignDescription designDescription,

            @Schema(description = "비고")
            AppNote appNote,

            @Schema(description = "출원 행정관리")
            InterDesignAppManagement appManagement,

            @Schema(description = "등록/권리유지 관리")
            InterDesignAppMaintenance appMaintenance
    ) {}

    @Builder
    public record InterDesignAppCaseMng(
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
    public record InterDesignAppBaseInfo(
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

            @Schema(description = "특허청제출일", example = "20260201")
            String authoritySubmissionDate,

            @Schema(description = "헤이그발송일", example = "20260205")
            String hagueDeliveryDate,

            @Schema(description = "WIPO 참조번호", example = "WIPO-12345")
            String wipoRefNo,

            @Schema(description = "등록_등록일", example = "20280701")
            String regDate,

            @Schema(description = "등록_등록번호", example = "US-9876543")
            String regNo
    ) {}

    @Builder
    public record InterDesignAppManagerInfo(
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
    public record InterDesignAppCounterPartyInfo(
            @Schema(description = "의뢰인")
            List<CommonRecordResponse.CounterPartyInfo> clientInfo,

            @Schema(description = "출원인")
            List<CommonRecordResponse.CounterPartyInfo> applicantInfo,

            @Schema(description = "창작자 정보")
            CommonRecordResponse.PersonInfo inventorInfo,

            @Schema(description = "등록권리자 정보")
            List<CommonRecordResponse.CounterPartyInfo> regMgrInfo
    ) {}


    public record InterDesignAppNameInfo(
            @Schema(description = "국문 명칭", example = "차세대 반도체 제조 장치")
            String titleKo,

            @Schema(description = "영문 명칭", example = "Next-gen Semiconductor Manufacturing Device")
            String titleEn
    ) {}

    @Builder
    public record InterDesignAppStrategy(
            @Schema(description = "지정국가", example = "[\"HAGUE\"]")
            List<String> designated,

            @Schema(description = "등록국가", example = "[\"DE\", \"FR\", \"GB\"]")
            List<String> registeredStates
    ) {}

    @Schema(description = "비고")
    public record AppNote(
            @Schema(description = "내용", example = "빠른 심사 요청 요망")
            String note
    ) {}

    @Builder
    public record InterDesignAppManagement(
            @Schema(description = "출원공개_일자", example = "20270730")
            String pubDate,

            @Schema(description = "출원공개_번호", example = "US-2027-0123456-A1")
            String pubNo,

            @Schema(description = "보정 - 통지일", example = "20270110")
            String amendNoticeDate,

            @Schema(description = "보정 - 마감일", example = "20270310")
            String amendDeadline,

            @Schema(description = "보정 - 제출일", example = "20270305")
            String amendSubmitDate,

            @Schema(description = "공개 선택", example = "N")
            String publicYn,

            @Schema(description = "연기월수", example = "12")
            String defermentMonthCount,

            @Schema(description = "포기_접수일", example = "20290102")
            String abandonReceiptDate,

            @Schema(description = "포기_일자", example = "20290105")
            String abandonDate,

            @Schema(description = "포기_내용", example = "사업성 결여로 인한 포기")
            String abandonNote
    ) {}

    @Builder
    public record InterDesignAppMaintenance(
            @Schema(description = "권리존속기간", example = "20460130")
            String rightPeriod,

            @Schema(description = "갱신차수", example = "3")
            String paymentInstallment,

            @Schema(description = "정상마감일", example = "20290701")
            String standardDeadline,

            @Schema(description = "과태마감일", example = "20300101")
            String penaltyDeadline,

            @Schema(description = "보호시작일", example = "20260130")
            String protectionStartDate
    ) {}

    @Schema(description = "설명/요점")
    public record InterDesignDescription(
            @Schema(description = "디자인 설명", example = "본 디자인은 ~형상이다.")
            String designDescription,

            @Schema(description = "디자인 요약", example = "디자인 창작 내용 요약")
            String designSummary
    ) {}
}
