package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;
import static kr.co.mindpro.ipms.common.util.DataConvertUtil.stringToList;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaPctAppRequest;
import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaPctAppResponse.java
 * @since : 2026. 3. 11.
 */
public class OverseaPctAppResponse {
    @Builder
    public record PctAppDetailResponse(
            @Schema(description = "출원 식별자")
            String appSeq,

            @Schema(description = "해외 기본 식별자")
            String appExtSeq,

            @Schema(description = "출원상태")
            CommonRecordResponse.CodeInfo appStatus,

            @Schema(description = "출원 사건 관리")
            OverseaPctAppRequest.PctAppCaseMng appCaseMng,

            @Schema(description = "출원 기본 정보")
            OverseaPctAppRequest.PctAppBaseInfo appBaseInfo,

            @Schema(description = "담당 정보")
            OverseaPctAppRequest.PctAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            OverseaPctAppRequest.PctAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭 정보")
            OverseaPctAppRequest.PctAppNameInfo appNameInfo,

            @Schema(description = "출원 전략설정")
            OverseaPctAppRequest.PctAppStrategy appStrategy,

            @Schema(description = "비고")
            OverseaPctAppRequest.AppNote appNote,

            @Schema(description = "출원 행정관리")
            OverseaPctAppRequest.PctAppManagement appManagement,

            @Schema(description = "등록 및 권리유지 관리")
            OverseaPctAppRequest.PctAppMaintenance appMaintenance,

            @Schema(description = "요약/청구 탭 정보")
            OverseaPctAppRequest.ClaimSummaryInfo claimSummaryInfo,

            @Schema(description = "첨부파일정보")
            List<CommonRecordResponse.FileInfo> fileInfo
    ) {
        public static OverseaPctAppResponse.PctAppDetailResponse fromVOViewPct(
                CommonAppVO vo,
                List<CommonRecordResponse.CounterPartyInfo> clientList,
                List<CommonRecordResponse.CounterPartyInfo> applicantList,
                List<PaperResponseVO> fileList) {
            return OverseaPctAppResponse.PctAppDetailResponse.builder()
                    .appSeq(vo.getAppSeq())
                    .appExtSeq(vo.getAppExtSeq())
                    .appStatus(new CommonRecordResponse.CodeInfo(vo.getStatusCode(), vo.getStatusName()))

                    // 1. 사건 관리
                    .appCaseMng(
                            OverseaPctAppRequest.PctAppCaseMng.builder()
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
                    // 2. 기본 정보
                    .appBaseInfo(
                            OverseaPctAppRequest.PctAppBaseInfo.builder()
                                    .noticeExceptionApply(
                                            new CommonRecordResponse.CodeInfo(
                                                    vo.getNoticeExceptionApplyCode(),
                                                    vo.getNoticeExceptionApplyName()
                                            )
                                    )
                                    .appDeadline(formatMinusHoursString8(vo.getAppDeadline()))
                                    .appOrderDate(formatMinusHoursString8(vo.getAppOrderDate()))
                                    .appDate(formatMinusHoursString8(vo.getAppDate()))
                                    .appNo(vo.getAppNo())
                                    .build()
                    )
                    // 3. 담당 정보
                    .appManagerInfo(
                            OverseaPctAppRequest.PctAppManagerInfo.builder()
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
                    // 4. 당사자 정보
                    .appCounterPartyInfo(
                            new OverseaPctAppRequest.PctAppCounterPartyInfo(
                                    clientList,
                                    applicantList,
                                    new CommonRecordResponse.PersonInfo(
                                            vo.getInventor(),
                                            vo.getInventorNm()
                                    )
                            )
                    )
                    // 5. 명칭 정보
                    .appNameInfo(
                            new OverseaPctAppRequest.PctAppNameInfo(
                                    vo.getTitleKo(),
                                    vo.getTitleEn()
                            )
                    )

                    // 7. 전략 설정 (관계 정보)
                    .appStrategy(
                            OverseaPctAppRequest.PctAppStrategy.builder()
                                    .krDesignationYn(vo.getKrDesignationYn())
                                    .deadline20Info(
                                            new OverseaPctAppRequest.Deadline20Info(
                                                    vo.getComplete20Yn(),
                                                    formatMinusHoursString8(vo.getNpe20Deadline()),
                                                    formatMinusHoursString8(vo.getEntry20CompleteDate()),
                                                    stringToList(vo.getApp20Country())
                                            )
                                    )
                                    .deadline30Info(
                                            new OverseaPctAppRequest.Deadline30Info(
                                                    vo.getComplete30Yn(),
                                                    formatMinusHoursString8(vo.getNpe30Deadline()),
                                                    formatMinusHoursString8(vo.getEntry30CompleteDate()),
                                                    stringToList(vo.getApp30Country())
                                            )
                                    )
                                    .build()
                    )
                    .appNote(
                            new OverseaPctAppRequest.AppNote(
                                    vo.getNote()
                            )
                    )
                    .appManagement(
                            OverseaPctAppRequest.PctAppManagement.builder()
                                    .pctFilingFeeInfo(
                                            new OverseaPctAppRequest.PctFilingFeeInfo(
                                                    formatMinusHoursString8(vo.getFilingFeeDeadline()),
                                                    formatMinusHoursString8(vo.getFilingFeePayDate())
                                            )
                                    )
                                    .internationalSearchInfo(
                                            new OverseaPctAppRequest.InternationalSearchInfo(
                                                    formatMinusHoursString8(vo.getIsaReceiptDate()),
                                                    formatMinusHoursString8(vo.getIsrReportDate()),
                                                    vo.getSearchResult()
                                            )
                                    )
                                    .abandonOrderDate(formatMinusHoursString8(vo.getAbandonOrderDate()))
                                    .abandonDate(formatMinusHoursString8(vo.getAbandonDate()))
                                    .abandonNote(vo.getAbandonNote())
                                    .build()
                    )
                    // 9. 유지 관리
                    .appMaintenance(
                            OverseaPctAppRequest.PctAppMaintenance.builder()
                                    .pctIpeInfo(
                                            new OverseaPctAppRequest.PctIpeInfo(
                                                    formatMinusHoursString8(vo.getIpeDeadline()),
                                                    formatMinusHoursString8(vo.getIpeRequestDate()),
                                                    formatMinusHoursString8(vo.getIpeReportDate())
                                            )
                                    )
                                    .intlPubInfo(
                                            new OverseaPctAppRequest.IntlPubInfo(
                                                    formatMinusHoursString8(vo.getIntlReceiptDate()),
                                                    formatMinusHoursString8(vo.getIntlPubDate()),
                                                    vo.getIntlPubNo()
                                            )
                                    )
                                    .build()
                    )
                    .claimSummaryInfo(
                            new OverseaPctAppRequest.ClaimSummaryInfo(
                                    vo.getSummary(),
                                    vo.getClaimScope()
                            )
                    )
                    .fileInfo(
                            CommonRecordResponse.FileInfo.from(fileList)
                    )
                    .build();
        }
    }
}
