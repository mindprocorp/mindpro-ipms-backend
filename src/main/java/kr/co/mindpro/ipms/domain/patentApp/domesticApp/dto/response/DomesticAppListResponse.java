package kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

/**
 * @author : seokho
 * @fileName : DomesticAppListResponse.java
 * @since : 2026. 3. 3.
 */
public class DomesticAppListResponse {
    @Schema(description = "국내 출원 리스트 데이터")
    @Builder(toBuilder = true)
    public record AppListResponse(
            // ==========================================
            // [0] 기본 식별 및 상태 (Grid No, Status)
            // ==========================================
            @Schema(description = "APP PK")
            String appSeq,

//            @Schema(description = "사무소 PK")
//            String officeSeq,

            @Schema(description = "구분")
            CommonRecordResponse.CodeInfo category,

            @Schema(description = "권리")
            CommonRecordResponse.CodeInfo rightType,

            @Schema(description = "사건마감일 (계산 필드)")
            String caseDeadline,

            @Schema(description = "현재상태")
            CommonRecordResponse.CodeInfo status,

            @Schema(description = "접수일")
            String receiptDate,


            // ==========================================
            // [1] 관리 번호 (Ref)
            // ==========================================
            @Schema(description = "OurRef")
            String ourRef,

            @Schema(description = "YourRef")
            String yourRef,

            @Schema(description = "출원인관리번호")
            String clientRef,

            @Schema(description = "접근코드")
            String accessCode,

            // ==========================================
            // [2] 당사자 정보 (CounterParty & Manager)
            // ==========================================
            @Schema(description = "부서", example = "test")
            String deptName,

            @Schema(description = "의뢰인 이름")
            String clientNm,

            @Schema(description = "의뢰인 이름")
            String applicantNm,

            @Schema(description = "발명자 정보")
            CommonRecordResponse.PersonInfo inventorInfo,

            @Schema(description = "의뢰인 이름")
            String regMgrNm,

            @Schema(description = "출원인담당자 정보")
            CommonRecordResponse.PersonInfo clientContactInfo,

            @Schema(description = "관리담당자 정보")
            CommonRecordResponse.PersonInfo adminMgrInfo,

            @Schema(description = "사건담당자 정보")
            CommonRecordResponse.PersonInfo caseMgrInfo,

            @Schema(description = "담당변리사 정보")
            CommonRecordResponse.PersonInfo attorneyInfo,

            // ==========================================
            // [3] 기본 출원 정보 (Base Info)
            // ==========================================
            @Schema(description = "출원일")
            String appDate,

            @Schema(description = "출원번호")
            String appNo,

            @Schema(description = "등록일")
            String regDate,

            @Schema(description = "등록번호")
            String regNo,

            @Schema(description = "국문명칭")
            String titleKo,

            @Schema(description = "영문명칭")
            String titleEn,

            @Schema(description = "제안명칭")
            String proposal,

            @Schema(description = "비고")
            String note,

            @Schema(description = "류(Class)")
            String goodsClass,

            // ==========================================
            // [4] 기일 관리 (Date Management)
            // ==========================================
            @Schema(description = "초안발송일")
            String draftSendDate,

            @Schema(description = "신청마감일")
            String reqDeadline,

            @Schema(description = "출원마감일")
            String appDeadline,

            @Schema(description = "출원지시일")
            String appOrderDate,

            @Schema(description = "심사청구일")
            String examRequestDate,

            @Schema(description = "해외출원일")
            String foreignAppDate,

            @Schema(description = "위임일")
            String annuityOrderDate,

            @Schema(description = "위임업체")
            String annuityAgency,

            @Schema(description = "포기취하일")
            String abandonDate,

            @Schema(description = "포기내용")
            String abandonNote,


            // ==========================================
            // [5] 상세 속성 및 명세서 (Specific Elements)
            // ==========================================
            @Schema(description = "출원종류")
            CommonRecordResponse.CodeInfo appType,

            @Schema(description = "출원구분")
            CommonRecordResponse.CodeInfo appCategory,

            @Schema(description = "등급")
            CommonRecordResponse.CodeInfo grade,

            @Schema(description = "독립항")
            String independentClaims,

            @Schema(description = "종속항")
            String dependentClaims,

            @Schema(description = "최종항수")
            String finalClaimsCount,

            @Schema(description = "명세서")
            String specPage,

            @Schema(description = "도면수")
            String drawingCount,

            @Schema(description = "도수")
            String figureCount,


            // ==========================================
            // [6] 전략 및 가족 특허 (Strategy & Family)
            // ==========================================
            @Schema(description = "분류출원번호")
            String classificAppNo,

            @Schema(description = "최초출원일")
            String firstAppDate,

            @Schema(description = "최초출원번호")
            String firstAppNo,

            @Schema(description = "원출원일")
            String originalAppDate,

            @Schema(description = "원출원번호")
            String originalAppNo,

            @Schema(description = "재출원일")
            String reAppDate,

            @Schema(description = "재출원번호")
            String reAppNo,

            @Schema(description = "이중출원일")
            String dualAppDate,

            @Schema(description = "이중출원번호")
            String dualAppNo,

            @Schema(description = "국제출원일")
            String globalAppDate,

            @Schema(description = "국제출원번호")
            String globalAppNo,

            @Schema(description = "최초우선권주장일")
            String priorityDate,


            // ==========================================
            // [7] 해외 전략 (Foreign Strategy)
            // ==========================================
            @Schema(description = "상표조사여부")
            String isTrademarkResearch,

            @Schema(description = "해외진행여부")
            String isForeignApp,

            @Schema(description = "해외진행방법")
            CommonRecordResponse.CodeInfo foreignAppTiming,

            @Schema(description = "해외출원마감일 (6개월)")
            String foreign6mDeadline,

            @Schema(description = "해외출원마감일2 (1년)")
            String foreign1yDeadline,


            // ==========================================
            // [8] 심사 및 청구범위 (Examination & Claims)
            // ==========================================
            @Schema(description = "청구범위통지일")
            String claimsNoticeDate,

            @Schema(description = "청구범위마감일")
            String claimsDeadline,

            @Schema(description = "청구범위제출일")
            String claimsSubmitDate,

            @Schema(description = "우심사청구일")
            String priorExamReqDate,

            @Schema(description = "우심사결정일")
            String priorExamDecDate,

            @Schema(description = "공개신청일")
            String earlyPubRequestDate,

            @Schema(description = "공개일")
            String pubDate,

            @Schema(description = "공개번호")
            String pubNo,

            @Schema(description = "공고결정일")
            String announcementDecisionDate,

            @Schema(description = "공고일")
            String announcementDate,

            @Schema(description = "공고번호")
            String announcementNo,


            // ==========================================
            // [9] 등록 관리 (Registration)
            // ==========================================
            @Schema(description = "등록결정일")
            String regDecisionDate,

            @Schema(description = "등록접수일")
            String regReceiptDate,

            @Schema(description = "등록정상마감")
            String regNormalDeadline,

            @Schema(description = "등록과태마감일")
            String regGraceDeadline,

            @Schema(description = "원등록일")
            String originalRegDate,

            @Schema(description = "원등록번호")
            String originalRegNo,

            @Schema(description = "등록공고일")
            String regAnnounceDate,

            @Schema(description = "등록공고번호")
            String regAnnounceNo,

            @Schema(description = "국제등록일MD")
            String madridAppDate,

            @Schema(description = "국제등록번호MD")
            String madridAppNo,


            // ==========================================
            // [10] 연차 및 갱신 (Annuity & Renewal)
            // ==========================================
            @Schema(description = "IPC분류")
            String ipcClassification,

            @Schema(description = "권리존속기간")
            String rightPeriod,

            @Schema(description = "연차관리여부")
            String isAnnuityManaged,

            @Schema(description = "연차과태일")
            String penaltyDeadline,

            @Schema(description = "납부차수")
            String annuityYear,

            @Schema(description = "연차마감일")
            String standardDeadline,

            @Schema(description = "갱신관리여부")
            String isRenewalManaged,

            @Schema(description = "갱신마감일")
            String renewalDeadline,

            @Schema(description = "갱신등록료")
            String trademarkRenewalFee,

            @Schema(description = "국내우선권주장여부")
            String hasDomesticPriority,

            @Schema(description = "국내우선권주장마감일")
            String domesticPriorDeadline,

            @Schema(description = "국내우선권주장일")
            String domesticPriorDate,


            // ==========================================
            // [11] BaseVO 필드 (Record는 상속 불가하므로 직접 선언)
            // ==========================================
            @Schema(description = "생성자")
            String createUser,

            @Schema(description = "생성일시")
            String createAt,

            @Schema(description = "수정자")
            String updateUser,

            @Schema(description = "수정일시")
            String updateAt
    ) {
        public static AppListResponse fromVO(
                CommonAppVO vo
        ) {
                return new AppListResponse(
                        // [0] 기본 식별 및 상태
                        vo.getAppSeq(),
                        new CommonRecordResponse.CodeInfo(vo.getCategoryCode(), vo.getCategoryName()),
                        new CommonRecordResponse.CodeInfo(vo.getRightTypeCode(), vo.getRightTypeName()),
                        "20261231"/*formatMinusHoursString8(vo.getCaseDeadline())*/, // todo 계산 필드 추후 수정 필
                        new CommonRecordResponse.CodeInfo(vo.getStatusCode(), vo.getStatusName()),
                        formatMinusHoursString8(vo.getReceiptDate()),

                        // [1] 관리 번호
                        vo.getOurRef(),
                        vo.getYourRef(),
                        vo.getClientRef(),
                        vo.getAccessCode(),

                        // [2] 당사자 정보
                        vo.getDeptName(),
                        vo.getClientNm(),
                        vo.getApplicantNm(),
                        new CommonRecordResponse.PersonInfo(vo.getInventor(), vo.getInventorNm()),
                        vo.getRegMgrNm(),
                        new CommonRecordResponse.PersonInfo(vo.getClientContact(), vo.getClientContactNm()),
                        new CommonRecordResponse.PersonInfo(vo.getAdminMgr(), vo.getAdminMgrNm()),
                        new CommonRecordResponse.PersonInfo(vo.getCaseMgr(), vo.getCaseMgrNm()),
                        new CommonRecordResponse.PersonInfo(vo.getAttorney(), vo.getAttorneyNm()),

                        // [3] 기본 출원 정보
                        formatMinusHoursString8(vo.getAppDate()),
                        vo.getAppNo(),
                        formatMinusHoursString8(vo.getRegDate()),
                        vo.getRegNo(),
                        vo.getTitleKo(),
                        vo.getTitleEn(),
                        vo.getProposal(),
                        vo.getNote(),
                        vo.getGoodsClass(),

                        // [4] 기일 관리
                        formatMinusHoursString8(vo.getDraftSendDate()),
                        ""/*formatMinusHoursString8(vo.getReqDeadline())*/,  // 신청마감일
                        formatMinusHoursString8(vo.getAppDeadline()),
                        formatMinusHoursString8(vo.getAppOrderDate()),
                        formatMinusHoursString8(vo.getExamRequestDate()),
                        formatMinusHoursString8(vo.getForeignAppDate()),
                        formatMinusHoursString8(vo.getAnnuityOrderDate()),
                        vo.getAnnuityAgency(),
                        formatMinusHoursString8(vo.getAbandonDate()),
                        vo.getAbandonNote(),

                        // [5] 상세 속성 및 명세서
                        new CommonRecordResponse.CodeInfo(vo.getAppTypeCode(), vo.getAppTypeName()),
                        new CommonRecordResponse.CodeInfo(vo.getAppCategoryCode(), vo.getAppCategoryName()),
                        new CommonRecordResponse.CodeInfo(vo.getGradeCode(), vo.getGradeName()),
                        vo.getIndependentClaims(),
                        vo.getDependentClaims(),
                        vo.getFinalClaimsCount() == null ? "0" : String.valueOf(vo.getFinalClaimsCount()),
                        vo.getSpecPage(),
                        vo.getDrawingCount(),
                        vo.getFigureCount(),

                        // [6] 전략 및 가족 특허
                        vo.getClassificAppNo(),
                        formatMinusHoursString8(vo.getFirstAppDate()),
                        vo.getFirstAppNo(),
                        formatMinusHoursString8(vo.getOriginalAppDate()),
                        vo.getOriginalAppNo(),
                        formatMinusHoursString8(vo.getReAppDate()),
                        vo.getReAppNo(),
                        formatMinusHoursString8(vo.getDualAppDate()),
                        vo.getDualAppNo(),
                        formatMinusHoursString8(vo.getGlobalAppDate()),
                        vo.getGlobalAppNo(),
                        formatMinusHoursString8(vo.getPriorityDate()),

                        // [7] 해외 전략
                        vo.getIsTrademarkResearch(),
                        vo.getIsForeignApp(),
                        new CommonRecordResponse.CodeInfo(vo.getForeignAppTimingCode(), vo.getForeignAppTimingName()),
                        formatMinusHoursString8(vo.getForeign6mDeadline()),
                        formatMinusHoursString8(vo.getForeign1yDeadline()),

                        // [8] 심사 및 청구범위
                        formatMinusHoursString8(vo.getClaimsNoticeDate()),
                        formatMinusHoursString8(vo.getClaimsDeadline()),
                        formatMinusHoursString8(vo.getClaimsSubmitDate()),
                        formatMinusHoursString8(vo.getPriorExamReqDate()),
                        formatMinusHoursString8(vo.getPriorExamDecDate()),
                        formatMinusHoursString8(vo.getEarlyPubRequestDate()),
                        formatMinusHoursString8(vo.getPubDate()),
                        vo.getPubNo(),
                        formatMinusHoursString8(vo.getAnnouncementDecisionDate()),
                        formatMinusHoursString8(vo.getAnnouncementDate()),
                        vo.getAnnouncementNo(),

                        // [9] 등록 관리
                        formatMinusHoursString8(vo.getRegDecisionDate()),
                        formatMinusHoursString8(vo.getRegReceiptDate()),
                        formatMinusHoursString8(vo.getRegNormalDeadline()),
                        formatMinusHoursString8(vo.getRegGraceDeadline()),
                        formatMinusHoursString8(vo.getOriginalRegDate()),
                        vo.getOriginalRegNo(),
                        formatMinusHoursString8(vo.getRegAnnounceDate()),
                        vo.getRegAnnounceNo(),
                        formatMinusHoursString8(vo.getMadridAppDate()),
                        vo.getMadridAppNo(),

                        // [10] 연차 및 갱신
                        vo.getIpcClassification(),
                        formatMinusHoursString8(vo.getRightPeriod()),
                        vo.getIsAnnuityManaged(),
                        formatMinusHoursString8(vo.getPenaltyDeadline()),
                        vo.getNextPaymentInstallment(), // annuityYear에 매핑
                        formatMinusHoursString8(vo.getStandardDeadline()),
                        vo.getIsRenewalManaged(),
                        ""/*formatMinusHoursString8(vo.getRenewalDeadline())*/,  // 갱신마감일
                        vo.getTrademarkRenewalFee(),
                        vo.getHasDomesticPriority(),
                        formatMinusHoursString8(vo.getDomesticPriorDeadline()),
                        formatMinusHoursString8(vo.getDomesticPriorDate()),

                        // [11] BaseVO 필드
                        vo.getCreateUser(),
                        formatMinusHoursString8(vo.getCreateAt()),
                        vo.getUpdateUser(),
                        formatMinusHoursString8(vo.getUpdateAt())
                );
        }
    }
}
