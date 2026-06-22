package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaMadridAppRequest;
import lombok.Builder;

import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.stringToList;

/**
 * @author : seokho
 * @fileName : OverseaMadridAppResponse.java
 * @since : 2026. 3. 11.
 */
public class OverseaMadridAppResponse {
    @Builder
    public record MadridAppDetailResponse(
            @Schema(description = "출원 식별자")
            String appSeq,

            @Schema(description = "해외 기본 식별자")
            String appExtSeq,

            @Schema(description = "출원상태")
            CommonRecordResponse.CodeInfo appStatus,

            @Schema(description = "출원 사건관리")
            OverseaMadridAppRequest.MadridAppCaseMng appCaseMng,

            @Schema(description = "출원기본정보")
            OverseaMadridAppRequest.MadridAppBaseInfo appBaseInfo,

            @Schema(description = "담당 정보")
            OverseaMadridAppRequest.MadridAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            OverseaMadridAppRequest.MadridAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭 정보")
            OverseaMadridAppRequest.MadridAppNameInfo appNameInfo,

            @Schema(description = "물품류(class)")
            OverseaMadridAppRequest.GoodsClass goodsClass,

            @Schema(description = "출원 전략설정")
            OverseaMadridAppRequest.MadridAppStrategy appStrategy,

            @Schema(description = "비고")
            OverseaMadridAppRequest.AppNote appNote,

            @Schema(description = "출원 행정관리")
            OverseaMadridAppRequest.MadridAppManagement appManagement,

            @Schema(description = "등록/권리유지 관리")
            OverseaMadridAppRequest.MadridAppMaintenance appMaintenance,

            @Schema(description = "첨부파일정보")
            List<CommonRecordResponse.FileInfo> fileInfo
    ) {
        public static OverseaMadridAppResponse.MadridAppDetailResponse fromVOViewMadrid(
                CommonAppVO vo,
                List<CommonRecordResponse.CounterPartyInfo> clientList,
                List<CommonRecordResponse.CounterPartyInfo> applicantList,
                List<PaperResponseVO> fileList
        ) {
            return OverseaMadridAppResponse.MadridAppDetailResponse.builder()
                    .appSeq(vo.getAppSeq())
                    .appExtSeq(vo.getAppExtSeq())
                    .appStatus(new CommonRecordResponse.CodeInfo(vo.getStatusCode(), vo.getStatusName()))

                    .appCaseMng(
                            OverseaMadridAppRequest.MadridAppCaseMng.builder()
                                    .appRoute(
                                            new CommonRecordResponse.CodeInfo(
                                                    vo.getAppRouteCode(),
                                                    vo.getAppRouteName()
                                            )
                                    )
                                    .category(
                                            new CommonRecordResponse.CodeInfo(
                                                    vo.getCategoryCode(),
                                                    vo.getCategoryName()
                                            )
                                    )
                                    .rightType(
                                            new CommonRecordResponse.CodeInfo(
                                                    vo.getRightTypeCode(),
                                                    vo.getRightTypeName()
                                            )
                                    )
                                    .appCategory(
                                            new CommonRecordResponse.CodeInfo(
                                                    vo.getAppCategoryCode(),
                                                    vo.getAppCategoryName()
                                            )
                                    )
                                    .receiptDate(formatMinusHoursString8(vo.getReceiptDate()))
                                    .ourRef(vo.getOurRef())
                                    .yourRef(vo.getYourRef())
                                    .clientRef(vo.getClientRef())
                                    .build()
                    )
                    .appBaseInfo(
                            OverseaMadridAppRequest.MadridAppBaseInfo.builder()
                                    .noticeExceptionApply(
                                            new CommonRecordResponse.CodeInfo(
                                                    vo.getNoticeExceptionApplyCode(),
                                                    vo.getNoticeExceptionApplyName()
                                            )
                                    )
                                    .appDeadline(formatMinusHoursString8(vo.getAppDeadline()))
                                    .appDate(formatMinusHoursString8(vo.getAppDate()))
                                    .appNo(vo.getAppNo())
                                    .authorityRefNo(vo.getAuthorityRefNo())
                                    .autoProtectionDate(formatMinusHoursString8(vo.getAutoProtectionDate()))
                                    .announcementDate(formatMinusHoursString8(vo.getAnnouncementDate()))
                                    .announcementNo(vo.getAnnouncementNo())
                                    .build()
                    )
                    .appManagerInfo(
                            OverseaMadridAppRequest.MadridAppManagerInfo.builder()
                                    .deptCode(vo.getDeptName())
                                    .applicantContactInfo(
                                            new CommonRecordResponse.PersonInfo(
                                                    vo.getApplicantContact(),
                                                    vo.getApplicantContactNm()
                                            )
                                    )
                                    .adminMgrInfo(
                                            new CommonRecordResponse.PersonInfo(
                                                    vo.getAdminMgr(),
                                                    vo.getAdminMgrNm()
                                            )
                                    )
                                    .caseMgrInfo(
                                            new CommonRecordResponse.PersonInfo(
                                                    vo.getCaseMgr(),
                                                    vo.getCaseMgrNm()
                                            )
                                    )
                                    .attorneyInfo(
                                            new CommonRecordResponse.PersonInfo(
                                                    vo.getAttorney(),
                                                    vo.getAttorneyNm()
                                            )
                                    )
                                    .build()
                    )
                    .appCounterPartyInfo(
                            new OverseaMadridAppRequest.MadridAppCounterPartyInfo(
                                    clientList,
                                    applicantList
                            )
                    )
                    .appNameInfo(
                            new OverseaMadridAppRequest.MadridAppNameInfo(
                                    vo.getTitleKo(),
                                    vo.getTitleEn()
                            )
                    )
                    .appStrategy(
                            OverseaMadridAppRequest.MadridAppStrategy.builder()
                                    .originalRegInfo(
                                            new OverseaMadridAppRequest.OriginalRegInfo(
                                                    formatMinusHoursString8(vo.getOriginalRegDate()),
                                                    vo.getOriginalRegNo()
                                            )
                                    )
                                    .designated(
                                            stringToList(vo.getDesignated())
                                    )
                                    .subsequent(
                                            stringToList(vo.getSubsequent())
                                    )
                                    .registeredStates(
                                            stringToList(vo.getRegisteredStates())
                                    )
                                    .build()
                    )
                    .appManagement(
                            OverseaMadridAppRequest.MadridAppManagement.builder()
                                    .abandonReceiptDate(formatMinusHoursString8(vo.getAbandonReceiptDate()))
                                    .abandonDate(formatMinusHoursString8(vo.getAbandonDate()))
                                    .abandonNote(vo.getAbandonNote())
                                    .build()
                    )
                    .goodsClass(
                            new OverseaMadridAppRequest.GoodsClass(
                                    vo.getGoodsClass()
                            )
                    )
                    .appNote(
                            new OverseaMadridAppRequest.AppNote(
                                    vo.getNote()
                            )
                    )
                    .appMaintenance(
                            OverseaMadridAppRequest.MadridAppMaintenance.builder()
                                    .domesticRegInfo(
                                            new OverseaMadridAppRequest.DomesticRegInfo(
                                                    formatMinusHoursString8(vo.getDomesticRegDate()),
                                                    vo.getDomesticRegNo()
                                            )
                                    )
                                    .regDate(formatMinusHoursString8(vo.getRegDate()))
                                    .regNo(vo.getRegNo())
                                    .paymentInstallment(vo.getPaymentInstallment())
                                    .standardDeadline(formatMinusHoursString8(vo.getStandardDeadline()))
                                    .penaltyDeadline(formatMinusHoursString8(vo.getPenaltyDeadline()))
                                    .annuityOrderDate(formatMinusHoursString8(vo.getAnnuityOrderDate()))
                                    .annuityAgency(vo.getAnnuityAgency())
                                    .build()
                    )
                    .fileInfo(
                            CommonRecordResponse.FileInfo.from(fileList)
                    )
                    .build();
        }
    }
}
