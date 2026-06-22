package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaEpAppRequest;
import lombok.Builder;

import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.stringToList;

/**
 * @author : seokho
 * @fileName : OverseaEpAppResponse.java
 * @since : 2026. 3. 11.
 */
public class OverseaEpAppResponse {
        @Builder
        public record EpAppDetailResponse(
            @Schema(description = "출원 식별자")
            String appSeq,

            @Schema(description = "해외 기본 식별자")
            String appExtSeq,

            @Schema(description = "출원상태")
            CommonRecordResponse.CodeInfo appStatus,

            @Schema(description = "출원 사건관리")
            OverseaEpAppRequest.EpAppCaseMng appCaseMng,

            @Schema(description = "출원기본정보")
            OverseaEpAppRequest.EpAppBaseInfo appBaseInfo,

            @Schema(description = "담당 정보")
            OverseaEpAppRequest.EpAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            OverseaEpAppRequest.EpAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭 정보")
            OverseaEpAppRequest.EpAppNameInfo appNameInfo,

            @Schema(description = "류(class)")
            OverseaEpAppRequest.EpAppIpcClass appIpcClass,

            @Schema(description = "명세서 구성요소")
            OverseaEpAppRequest.EpAppSpecificElement appSpecificElement,

            @Schema(description = "출원 전략설정")
            OverseaEpAppRequest.EpAppStrategy appStrategy,

            @Schema(description = "비고")
            OverseaEpAppRequest.AppNote appNote,

            @Schema(description = "지정국가")
            OverseaEpAppRequest.DesignatedStateInfo designatedStateInfo,

            @Schema(description = "등록국가")
            OverseaEpAppRequest.RegisteredStates registeredStates,

            @Schema(description = "출원 행정관리")
            OverseaEpAppRequest.EpAppManagement appManagement,

            @Schema(description = "등록/권리유지 관리")
            OverseaEpAppRequest.EpAppMaintenance appMaintenance,

            @Schema(description = "요약/청구 탭 정보")
            OverseaEpAppRequest.ClaimSummaryInfo claimSummaryInfo,

            @Schema(description = "첨부파일정보")
            List<CommonRecordResponse.FileInfo> fileInfo
        ) {
            public static OverseaEpAppResponse.EpAppDetailResponse fromVOViewEp(
                    CommonAppVO vo,
                    List<CommonRecordResponse.CounterPartyInfo> clientList,
                    List<CommonRecordResponse.CounterPartyInfo> applicantList,
                    List<CommonRecordResponse.CounterPartyInfo> foreignAgentList,
                    List<PaperResponseVO> fileList
            ) {
                    return OverseaEpAppResponse.EpAppDetailResponse.builder()
                            .appSeq(vo.getAppSeq())
                            .appExtSeq(vo.getAppExtSeq())
                            .appStatus(new CommonRecordResponse.CodeInfo(vo.getStatusCode(), vo.getStatusName()))

                            .appCaseMng(
                                    OverseaEpAppRequest.EpAppCaseMng.builder()
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
                                    OverseaEpAppRequest.EpAppBaseInfo.builder()
                                            .noticeExceptionApply(
                                                    new CommonRecordResponse.CodeInfo(
                                                            vo.getNoticeExceptionApplyCode(),
                                                            vo.getNoticeExceptionApplyName()
                                                    )
                                            )
                                            .appDeadline(formatMinusHoursString8(vo.getAppDeadline()))
                                            .oaDeliveryDate(formatMinusHoursString8(vo.getOaDeliveryDate()))
                                            .appDate(formatMinusHoursString8(vo.getAppDate()))
                                            .appNo(vo.getAppNo())
                                            .divAppInfo(
                                                    new OverseaEpAppRequest.DivAppInfo(
                                                            formatMinusHoursString8(vo.getDivDeadline()),
                                                            formatMinusHoursString8(vo.getDivAppDate()),
                                                            vo.getDivAppNo()
                                                    )
                                            )
                                            .build()
                            )
                            .appManagerInfo(
                                    OverseaEpAppRequest.EpAppManagerInfo.builder()
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
                                    new OverseaEpAppRequest.EpAppCounterPartyInfo(
                                            foreignAgentList,
                                            clientList,
                                            applicantList,
                                            new CommonRecordResponse.PersonInfo(
                                                    vo.getInventor(),
                                                    vo.getInventorNm()
                                            )
                                    )
                            )
                            .appNameInfo(
                                    new OverseaEpAppRequest.EpAppNameInfo(
                                            vo.getTitleKo(),
                                            vo.getTitleEn()
                                    )
                            )
                            .appSpecificElement(
                                    OverseaEpAppRequest.EpAppSpecificElement.builder()
                                            .grade(
                                                    new CommonRecordResponse.CodeInfo(
                                                            vo.getGradeCode(),
                                                            vo.getGradeName()
                                                    )
                                            )
                                            .independentClaims(vo.getIndependentClaims())
                                            .dependentClaims(vo.getDependentClaims())
                                            .overseaSpecPage(vo.getOverseaSpecPage())
                                            .specPage(vo.getSpecPage())
                                            .drawingCount(vo.getDrawingCount())
                                            .build()
                            )
                            .appStrategy(
                                    OverseaEpAppRequest.EpAppStrategy.builder()
                                            .originalAppInfo(
                                                    new OverseaEpAppRequest.OriginalAppInfo(
                                                            formatMinusHoursString8(vo.getOriginalAppDate()),
                                                            vo.getOriginalAppNo()
                                                    )
                                            )
                                            .globalAppInfo(
                                                    new OverseaEpAppRequest.GlobalAppInfo(
                                                            formatMinusHoursString8(vo.getGlobalAppDate()),
                                                            vo.getGlobalAppNo()
                                                    )
                                            )
                                            .build()
                            )
                            .appIpcClass(
                                    new OverseaEpAppRequest.EpAppIpcClass(
                                            vo.getIpcClassification()
                                    )
                            )
                            .designatedStateInfo(
                                    new OverseaEpAppRequest.DesignatedStateInfo(
                                            stringToList(vo.getDesignated())
                                    )
                            )
                            .registeredStates(
                                    new OverseaEpAppRequest.RegisteredStates(
                                            stringToList(vo.getRegisteredStates())
                                    )
                            )
                            .appManagement(
                                    OverseaEpAppRequest.EpAppManagement.builder()
                                            .claimAmendDate(formatMinusHoursString8(vo.getClaimAmendDate()))
                                            .announcementDate(formatMinusHoursString8(vo.getAnnouncementDate()))
                                            .examRequestDeadline(formatMinusHoursString8(vo.getExamRequestDeadline()))
                                            .examRequestOrderDate(formatMinusHoursString8(vo.getExamRequestOrderDate()))
                                            .examRequestDate(formatMinusHoursString8(vo.getExamRequestDate()))
                                            .searchReceiptDate(formatMinusHoursString8(vo.getSearchReceiptDate()))
                                            .searchReportDate(formatMinusHoursString8(vo.getSearchReportDate()))
                                            .epSearchResult(vo.getEpSearchResult())
                                            .pubDate(formatMinusHoursString8(vo.getPubDate()))
                                            .pubNo(vo.getPubNo())
                                            .build()
                            )
                            .appMaintenance(
                                    OverseaEpAppRequest.EpAppMaintenance.builder()
                                            .regDecisionDate(formatMinusHoursString8(vo.getRegDecisionDate()))
                                            .regNormalDeadline(formatMinusHoursString8(vo.getRegNormalDeadline()))
                                            .regGraceDeadline(formatMinusHoursString8(vo.getRegGraceDeadline()))
                                            .regOrderDate(formatMinusHoursString8(vo.getRegOrderDate()))
                                            .regPaymentDate(formatMinusHoursString8(vo.getRegPaymentDate()))
                                            .regDate(formatMinusHoursString8(vo.getRegDate()))
                                            .regNo(vo.getRegNo())
                                            .regAnnounceDate(formatMinusHoursString8(vo.getRegAnnounceDate()))
                                            .regAnnounceNo(vo.getRegAnnounceNo())
                                            .annuityOrderDate(formatMinusHoursString8(vo.getAnnuityOrderDate()))
                                            .annuityAgency(vo.getAnnuityAgency())
                                            .deemedWithdrawalReceiptDate(formatMinusHoursString8(vo.getDeemedWithdrawalReceiptDate()))
                                            .deemedWithdrawalDate(formatMinusHoursString8(vo.getDeemedWithdrawalDate()))
                                            .deemedWithdrawalContent(vo.getDeemedWithdrawalContent())
                                            .build()
                            )
                            .claimSummaryInfo(
                                    new OverseaEpAppRequest.ClaimSummaryInfo(
                                            vo.getSummary(),
                                            vo.getClaimScope()
                                    )
                            )
                            .fileInfo(
                                    CommonRecordResponse.FileInfo.from(fileList)
                            )
                            .appNote(
                                    new OverseaEpAppRequest.AppNote(
                                            vo.getNote()
                                    )
                            )
                            .build();
            }
        }
}
