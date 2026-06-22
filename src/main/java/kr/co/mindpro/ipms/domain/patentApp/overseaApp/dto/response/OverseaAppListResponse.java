package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import lombok.Builder;

import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

/**
 * @author : seokho
 * @fileName : OverseaAppListResponse.java
 * @since : 2026. 3. 12.
 */
public class OverseaAppListResponse {
    @Schema(description = "해외 출원 리스트 데이터 (Grid Row)")
    @Builder(toBuilder = true)
    public record AppListDetailResponse(

            // ==========================================
            // [0] 기본 식별 및 상태 (Grid No, Status)
            // ==========================================

            @Schema(description = "해외 기본 식별자")
            String appExtSeq,

            @Schema(description = "APP 식별자")
            String appSeq,

            @Schema(description = "구분")
            CommonRecordResponse.CodeInfo category,

            @Schema(description = "출원 루트")
            CommonRecordResponse.CodeInfo appRoute,

            @Schema(description = "권리")
            CommonRecordResponse.CodeInfo rightType,

            @Schema(description = "국가코드")
            CommonRecordResponse.CodeInfo countryCode,

            @Schema(description = "현재상태")
            CommonRecordResponse.CodeInfo status,


            // ==========================================
            // [1] 관리 번호 및 참조 (Ref)
            // ==========================================
            @Schema(description = "OurRef")
            String ourRef,

            @Schema(description = "YourRef")
            String yourRef,

            @Schema(description = "출원인관리번호")
            String clientRef,

            @Schema(description = "특허청참조번호")
            String authorityRefNo,

            @Schema(description = "WIPO참조번호")
            String wipoRefNo,


            // ==========================================
            // [2] 당사자 정보 (PersonInfo 객체 활용)
            // ==========================================
            @Schema(description = "의뢰인 이름 모음")
            String clientNm,

            @Schema(description = "출원인 이름 모음")
            String applicantNm,

            @Schema(description = "발명자 정보")
            CommonRecordResponse.PersonInfo inventorInfo,

            @Schema(description = "등록권리자 이름 모음")
            String regMgrNm,

            @Schema(description = "관리담당자 정보")
            CommonRecordResponse.PersonInfo adminMgrInfo,

            @Schema(description = "사건담당자 정보")
            CommonRecordResponse.PersonInfo caseMgrInfo,

            @Schema(description = "담당변리사 정보")
            CommonRecordResponse.PersonInfo attorneyInfo,

            @Schema(description = "해외대리인 이름 모음")
            String foreignAgentNm,


            // ==========================================
            // [3] 기본 출원/등록 정보 (Base Info)
            // ==========================================
            @Schema(description = "국문명칭")
            String titleKo,

            @Schema(description = "영문명칭")
            String titleEn,

            @Schema(description = "류(Class)")
            String goodsClass,

            @Schema(description = "출원일")
            String appDate,

            @Schema(description = "출원번호")
            String appNo,

            @Schema(description = "출원공개일자")
            String pubDate,

            @Schema(description = "출원공개번호")
            String pubNo,

            @Schema(description = "출원공고일자")
            String announcementDate,

            @Schema(description = "출원공고번호")
            String announcementNo,

            @Schema(description = "등록결정일")
            String regDecisionDate,

            @Schema(description = "등록일")
            String regDate,

            @Schema(description = "등록번호")
            String regNo,

            @Schema(description = "비고")
            String note,


            // ==========================================
            // [4] 기일 관리 (Date Management)
            // ==========================================
            @Schema(description = "접수일")
            String receiptDate,

            @Schema(description = "사건마감일 (계산 필드)")
            String caseDeadline,

            @Schema(description = "출원마감일")
            String appDeadline,

            @Schema(description = "20개월마감일")
            String npe20Deadline,

            @Schema(description = "30개월마감일")
            String npe30Deadline,

            @Schema(description = "연차마감일")
            String standardDeadline, // (기존 국내출원 네이밍 재사용)

            @Schema(description = "갱신마감일")
            String renewalDeadline,

            @Schema(description = "위임일")
            String annuityOrderDate, // (기존 위임일 네이밍 재사용)

            @Schema(description = "위임업체")
            String annuityAgency,

            @Schema(description = "포기취하일")
            String abandonDate,

            @Schema(description = "포기내용")
            String abandonNote
    ) {
        public static OverseaAppListResponse.AppListDetailResponse fromVO(
                CommonAppVO vo
        ) {
            if (vo == null) return null;

            return OverseaAppListResponse.AppListDetailResponse.builder()
                    // ==========================================
                    // [0] 기본 식별 및 상태
                    // ==========================================
                    .appExtSeq(vo.getAppExtSeq())
                    .appSeq(vo.getAppSeq())
                    .category(
                            CommonRecordResponse.CodeInfo.builder()
                                    .code(vo.getAppCategoryCode())
                                    .codeName(vo.getAppCategoryName())
                                    .build()
                    )
                    .appRoute(
                            CommonRecordResponse.CodeInfo.builder()
                                    .code(vo.getAppRouteCode())
                                    .codeName(vo.getAppRouteName())
                                    .build()
                    )
                    .rightType(
                            CommonRecordResponse.CodeInfo.builder()
                                    .code(vo.getRightTypeCode())
                                    .codeName(vo.getRightTypeName())
                                    .build()
                    )
                    .countryCode(
                            CommonRecordResponse.CodeInfo.builder()
                                    .code(vo.getCountryCode())
                                    .codeName(vo.getCountryName())
                                    .build()
                    )
                    .status(
                            CommonRecordResponse.CodeInfo.builder()
                                    .code(vo.getStatusCode())
                                    .codeName(vo.getStatusName())
                                    .build()
                    )

                    // ==========================================
                    // [1] 관리 번호 및 참조 (Ref)
                    // ==========================================
                    .caseDeadline("20261231")           // todo 계산 필드 추후 수정 필
                    .ourRef(vo.getOurRef())         // 보통 IPMS에서 ourRef는 assetNo를 사용
                    .yourRef(vo.getYourRef())       // 보통 yourRef는 agentRef
                    .clientRef(vo.getClientRef())                 // AppMstVO에 없음 (필요시 추가 필요)
                    .authorityRefNo(vo.getAuthorityRefNo())
                    .wipoRefNo(vo.getWipoRefNo())

                    // ==========================================
                    // [2] 당사자 정보 (PersonInfo)
                    // ==========================================
                    .clientNm(vo.getClientNm())
                    .applicantNm(vo.getApplicantNm())
                    .inventorInfo(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getInventor()).userName(vo.getInventorNm()).build())
                    .regMgrNm(vo.getRegMgrNm())
                    .adminMgrInfo(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getAdminMgr()).userName(vo.getAdminMgrNm()).build())
                    .caseMgrInfo(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getCaseMgr()).userName(vo.getCaseMgrNm()).build())
                    .attorneyInfo(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getAttorney()).userName(vo.getAttorneyNm()).build())
                    .foreignAgentNm(vo.getForeignAgentNm())

                    // ==========================================
                    // [3] 기본 출원/등록 정보
                    // ==========================================
                    .titleKo(vo.getTitleKo())
                    .titleEn(vo.getTitleEn())
                    .goodsClass(vo.getGoodsClass())
                    .appNo(vo.getAppNo())
                    .pubDate(formatMinusHoursString8(vo.getPubDate()))
                    .pubNo(vo.getPubNo())
                    .announcementNo(vo.getAnnouncementNo())
                    .regNo(vo.getRegNo())
                    .note(vo.getNote())

                    // ==========================================
                    // [4] 기일 관리 (마감일 및 포기 내역 등)
                    // ==========================================
                    .renewalDeadline(formatMinusHoursString8(vo.getRenewalDeadline()))
                    .annuityAgency(vo.getAnnuityAgency())
                    // 포기/취하 내용은 deemedWithdrawalContent나 giveUpContent 활용
                    .abandonNote(vo.getAbandonNote() != null ? vo.getAbandonNote() : vo.getDeemedWithdrawalContent())
                    .appDate(formatMinusHoursString8(vo.getAppDate()))
                    .announcementDate(formatMinusHoursString8(vo.getAnnouncementDate()))
                    .regDecisionDate(formatMinusHoursString8(vo.getRegDecisionDate()))
                    .regDate(formatMinusHoursString8(vo.getRegDate()))
                    .receiptDate(formatMinusHoursString8(vo.getReceiptDate()))
                    .appDeadline(formatMinusHoursString8(vo.getAppDeadline()))
                    .npe20Deadline(formatMinusHoursString8(vo.getNpe20Deadline()))
                    .npe30Deadline(formatMinusHoursString8(vo.getNpe30Deadline()))
                    .standardDeadline(formatMinusHoursString8(vo.getStandardDeadline()))
                    .annuityOrderDate(formatMinusHoursString8(vo.getAnnuityOrderDate()))
                    .abandonDate(formatMinusHoursString8(vo.getAbandonDate()))

                    .build();
        }
    }
}
