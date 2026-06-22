package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaEpAppRequest.java
 * @since : 2026. 3. 11.
 */
public class OverseaEpAppRequest {
    public record CreateEpAppRequest(
            @Schema(description = "출원 식별자")
            String appSeq,

            @Schema(description = "해외 기본 식별자", example = "EXTMST20260000014")
            String appExtSeq,

            @Schema(description = "출원 사건관리")
            EpAppCaseMng appCaseMng,

            @Schema(description = "출원기본정보")
            EpAppBaseInfo appBaseInfo,

            @Schema(description = "담당 정보")
            EpAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            EpAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭 정보")
            EpAppNameInfo appNameInfo,

            @Schema(description = "류(class)")
            EpAppIpcClass appIpcClass,

            @Schema(description = "명세서 구성요소")
            EpAppSpecificElement appSpecificElement,

            @Schema(description = "출원 전략설정")
            EpAppStrategy appStrategy,

            @Schema(description = "비고")
            AppNote appNote,

            @Schema(description = "지정국가")
            DesignatedStateInfo designatedStateInfo,

            @Schema(description = "등록국가")
            RegisteredStates registeredStates,

            @Schema(description = "출원 행정관리")
            EpAppManagement appManagement,

            @Schema(description = "등록/권리유지 관리")
            EpAppMaintenance appMaintenance,

            @Schema(description = "요약/청구 탭 정보")
            ClaimSummaryInfo claimSummaryInfo
    ) {}

    @Builder
    public record EpAppCaseMng(
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

    @Schema(description = "분할출원정보")
    public record DivAppInfo(
            @Schema(description = "분할출원마감일", example = "20270501")
            String divDeadline,

            @Schema(description = "분할 출원일", example = "20270420")
            String divAppDate,

            @Schema(description = "분할출원번호", example = "10-2027-0000002")
            String divAppNo
    ) {}

    @Builder
    public record EpAppBaseInfo(
            @Schema(description = "공지예외적용")
            CommonRecordResponse.CodeInfo noticeExceptionApply,

            @Schema(description = "출원마감일", example = "20260210")
            String appDeadline,

            @Schema(description = "오더발송일", example = "20260115")
            String oaDeliveryDate,

            @Schema(description = "출원일", example = "20260130")
            String appDate,

            @Schema(description = "출원번호", example = "10-2026-1234567")
            String appNo,

            @Schema(description = "분할출원정보")
            DivAppInfo divAppInfo
    ) {}

    @Builder
    public record EpAppManagerInfo(
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
    public record EpAppCounterPartyInfo(
            @Schema(description = "해외대리인")
            List<CommonRecordResponse.CounterPartyInfo> foreignAgentInfo,

            @Schema(description = "의뢰인")
            List<CommonRecordResponse.CounterPartyInfo> clientInfo,

            @Schema(description = "출원인")
            List<CommonRecordResponse.CounterPartyInfo> applicantInfo,

            @Schema(description = "발명자/고안자")
            CommonRecordResponse.PersonInfo inventorInfo
    ) {}

    @Schema(description = "명칭_정보")
    public record EpAppNameInfo(
            @Schema(description = "국문 명칭", example = "차세대 반도체 제조 장치")
            String titleKo,

            @Schema(description = "영문 명칭", example = "Next-gen Semiconductor Manufacturing Device")
            String titleEn
    ) {}

    // 레퍼런스 시스템에서는 상표 지정상품에 대한 '상품류' 항목이지만 ep는 hardIp 이므로 의미가 명확하지 않음.
    // 그래서 ipc 분류 코드로 지정 함.
    @Schema(description = "ipc 분류 코드 - 류(class)")
    public record EpAppIpcClass(
            @Schema(description = "IPC분류", example = "G06Q 50/10")
            String ipcClassification
    ) {}

    @Builder
    public record EpAppSpecificElement(
            @Schema(description = "등급")
            CommonRecordResponse.CodeInfo grade,

            @Schema(description = "독립항", example = "5")
            String independentClaims,

            @Schema(description = "종속항", example = "15")
            String dependentClaims,

            @Schema(description = "국내명세서 페이지수", example = "20")
            String specPage,

            @Schema(description = "도면 수", example = "10")
            String drawingCount,

            @Schema(description = "해외명세서 페이지수", example = "30")
            String overseaSpecPage
    ) {}

    @Schema(description = "국제출원정보")
    public record GlobalAppInfo(
            @Schema(description = "국제출원일", example = "20251120")
            String globalAppDate,

            @Schema(description = "국제출원번호", example = "PCT/KR2025/001234")
            String globalAppNo
    ) {}

    @Schema(description = "원출원_정보")
    public record OriginalAppInfo(
            @Schema(description = "원출원일(상표는 출원 기본정보에 사용)", example = "20251201")
            String originalAppDate,

            @Schema(description = "원출원번호(상표는 출원 기본정보에 사용)", example = "10-2025-0012345")
            String originalAppNo
    ) {}

    @Builder
    public record EpAppStrategy(
            @Schema(description = "국제출원정보")
            GlobalAppInfo globalAppInfo,

            @Schema(description = "원출원_정보")
            OriginalAppInfo originalAppInfo
    ) {}

    @Schema(description = "비고")
    public record AppNote(
            @Schema(description = "내용", example = "빠른 심사 요청 요망")
            String note
    ) {}

    @Schema(description = "지정국가")
    public record DesignatedStateInfo(
            @Schema(description = "지정국가 (공통)", example = "[\"HAGUE\"]")
            List<String> designated
    ) {}

    @Schema(description = "등록국가 (실제 권리를 확보한 나라)")
    public record RegisteredStates(
            @Schema(description = "등록국가", example = "[\"DE\", \"FR\", \"GB\"]")
            List<String> registeredStates
    ) {}

    @Builder
    public record EpAppManagement(
            @Schema(description = "청구보정일", example = "20270501")
            String claimAmendDate,

            @Schema(description = "출원공고_일자", example = "20280115")
            String announcementDate,

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

            @Schema(description = "서치(EP) - 접수일", example = "20260801")
            String searchReceiptDate,

            @Schema(description = "서치(EP) - 보고일", example = "20260901")
            String searchReportDate,

            @Schema(description = "서치(EP) - 결과", example = "등록가능")
            String epSearchResult
    ) {}

    @Builder
    public record EpAppMaintenance(
            // 등록결정 그룹 (EP - 유럽 특화 속성)
            // 한국에서는 등록결정이 나고 수수료를 지불하면 권한이 발효되지만 유럽은 검사가 종료되면
            // 수수료를 일정기간내 납부하고 나서 등록 및 공고가 진행된다고 합니다. 그래서 따로 만들어 줬습니다.
//            @Schema(description = "등록결정 - 결정일", example = "20280520")
//            String epRegDecisionDate,
//
//            @Schema(description = "등록결정 - 마감일", example = "20280820")
//            String epRegNormalDeadline,
//
//            @Schema(description = "등록결정 - 과태일", example = "20290220")
//            String epRegGraceDeadline,
//
//            @Schema(description = "등록결정 - 지시일", example = "20280601")
//            String epRegOrderDate,
//
//            @Schema(description = "등록결정 - 납부일", example = "20280610")
//            String epRegPaymentDate,

            @Schema(description = "등록_결정일", example = "20280520")
            String regDecisionDate,

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

            @Schema(description = "등록공고_일자", example = "20280715")
            String regAnnounceDate,

            @Schema(description = "등록공고_번호", example = "US-9876543-B2")
            String regAnnounceNo,

            @Schema(description = "관리위임_일자", example = "20280801")
            String annuityOrderDate,

            @Schema(description = "관리위임_업체", example = "Dennemeyer")
            String annuityAgency,

            @Schema(description = "포기취하 - 접수일", example = "20290501")
            String deemedWithdrawalReceiptDate,

            @Schema(description = "포기취하 - 일자", example = "20290505")
            String deemedWithdrawalDate,

            @Schema(description = "포기취하 - 내용", example = "대응 없음으로 인한 취하 간주")
            String deemedWithdrawalContent
    ) {}

    @Schema(description = "요약/청구 탭")
    public record ClaimSummaryInfo(

            @Schema(description = "요약내용", example = "요약내용입니다.")
            String summary,

            @Schema(description = "청구범위", example = "청구범위 내용입니다.")
            String claimScope
    ) {}
}
