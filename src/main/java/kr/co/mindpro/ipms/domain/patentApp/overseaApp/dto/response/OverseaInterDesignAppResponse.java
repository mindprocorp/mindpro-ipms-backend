package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaInterDesignAppRequest;
import lombok.Builder;

import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.stringToList;

/**
 * @author : seokho
 * @fileName : OverseaInterDesignAppResponse.java
 * @since : 2026. 3. 12.
 */
public class OverseaInterDesignAppResponse {
    @Builder
    public record InterDesignAppDetailResponse(
            @Schema(description = "출원 식별자")
            String appSeq,

            @Schema(description = "해외 기본 식별자")
            String appExtSeq,

            @Schema(description = "출원상태")
            CommonRecordResponse.CodeInfo appStatus,

            @Schema(description = "출원 사건관리")
            OverseaInterDesignAppRequest.InterDesignAppCaseMng appCaseMng,

            @Schema(description = "출원기본정보")
            OverseaInterDesignAppRequest.InterDesignAppBaseInfo appBaseInfo,

            @Schema(description = "담당 정보")
            OverseaInterDesignAppRequest.InterDesignAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            OverseaInterDesignAppRequest.InterDesignAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭_정보")
            OverseaInterDesignAppRequest.InterDesignAppNameInfo appNameInfo,

            @Schema(description = "출원 전략설정")
            OverseaInterDesignAppRequest.InterDesignAppStrategy appStrategy,

            @Schema(description = "설명/요점")
            OverseaInterDesignAppRequest.InterDesignDescription designDescription,

            @Schema(description = "비고")
            OverseaInterDesignAppRequest.AppNote appNote,

            @Schema(description = "출원 행정관리")
            OverseaInterDesignAppRequest.InterDesignAppManagement appManagement,

            @Schema(description = "등록/권리유지 관리")
            OverseaInterDesignAppRequest.InterDesignAppMaintenance appMaintenance,

            @Schema(description = "첨부파일정보")
            List<CommonRecordResponse.FileInfo> fileInfo
    ) {
        public static OverseaInterDesignAppResponse.InterDesignAppDetailResponse fromVOViewInterDesign(
                CommonAppVO vo,
                List<CommonRecordResponse.CounterPartyInfo> clientList,
                List<CommonRecordResponse.CounterPartyInfo> applicantList,
                List<CommonRecordResponse.CounterPartyInfo> regMgrList,
                List<PaperResponseVO> fileList
        ) {
            return OverseaInterDesignAppResponse.InterDesignAppDetailResponse.builder()
                    .appSeq(vo.getAppSeq())
                    .appExtSeq(vo.getAppExtSeq())
                    .appStatus(new CommonRecordResponse.CodeInfo(vo.getStatusCode(), vo.getStatusName()))
                    .appCaseMng(
                            OverseaInterDesignAppRequest.InterDesignAppCaseMng.builder()
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
                                    .clientRef(vo.getClientRef())
                                    .build()
                    )
                    .appBaseInfo(
                            OverseaInterDesignAppRequest.InterDesignAppBaseInfo.builder()
                                    .authoritySubmissionDate(formatMinusHoursString8(vo.getAuthoritySubmissionDate()))
                                    .hagueDeliveryDate(formatMinusHoursString8(vo.getHagueDeliveryDate()))
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
                                    .wipoRefNo(vo.getWipoRefNo())
                                    .regDate(formatMinusHoursString8(vo.getRegDate()))
                                    .regNo(vo.getRegNo())
                                    .build()
                    )
                    .appManagerInfo(
                            OverseaInterDesignAppRequest.InterDesignAppManagerInfo.builder()
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
                            new OverseaInterDesignAppRequest.InterDesignAppCounterPartyInfo(
                                    clientList,
                                    applicantList,
                                    new CommonRecordResponse.PersonInfo(
                                            vo.getInventor(),
                                            vo.getInventorNm()
                                    ),
                                    regMgrList
                            )
                    )
                    .appNameInfo(
                            new OverseaInterDesignAppRequest.InterDesignAppNameInfo(
                                    vo.getTitleKo(),
                                    vo.getTitleEn()
                            )
                    )
                    .appStrategy(
                            OverseaInterDesignAppRequest.InterDesignAppStrategy.builder()
                                    .designated(stringToList(vo.getDesignated()))
                                    .registeredStates(stringToList(vo.getRegisteredStates()))
                                    .build()
                    )
                    .appNote(
                            new OverseaInterDesignAppRequest.AppNote(
                                    vo.getNote()
                            )
                    )
                    .appManagement(
                            OverseaInterDesignAppRequest.InterDesignAppManagement.builder()
                                    .amendNoticeDate(formatMinusHoursString8(vo.getAmendNoticeDate()))
                                    .amendDeadline(formatMinusHoursString8(vo.getAmendDeadline()))
                                    .amendSubmitDate(formatMinusHoursString8(vo.getAmendSubmitDate()))
                                    .publicYn(vo.getPublicYn())
                                    .defermentMonthCount(vo.getDefermentMonthCount())
                                    .pubDate(formatMinusHoursString8(vo.getPubDate()))
                                    .pubNo(vo.getPubNo())
                                    .abandonReceiptDate(formatMinusHoursString8(vo.getAbandonReceiptDate()))
                                    .abandonDate(formatMinusHoursString8(vo.getAbandonDate()))
                                    .abandonNote(vo.getAbandonNote())
                                    .build()
                    )
                    .appMaintenance(
                            OverseaInterDesignAppRequest.InterDesignAppMaintenance.builder()
                                    .protectionStartDate(formatMinusHoursString8(vo.getProtectionStartDate()))
                                    .rightPeriod(vo.getRightPeriod())
                                    .paymentInstallment(vo.getPaymentInstallment())
                                    .standardDeadline(formatMinusHoursString8(vo.getStandardDeadline()))
                                    .penaltyDeadline(formatMinusHoursString8(vo.getPenaltyDeadline()))
                                    .build()
                    )
                    .designDescription(
                            new OverseaInterDesignAppRequest.InterDesignDescription(
                                    vo.getDesignDescription(),
                                    vo.getDesignSummary()
                            )
                    )
                    .fileInfo(
                            CommonRecordResponse.FileInfo.from(fileList)
                    )
                    .build();
        }
    }
}
