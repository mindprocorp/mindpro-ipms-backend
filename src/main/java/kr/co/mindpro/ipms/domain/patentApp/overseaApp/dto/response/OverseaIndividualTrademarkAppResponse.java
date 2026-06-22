package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaIndividualTrademarkAppRequest;
import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaIndividualTrademarkAppResponse.java
 * @since : 2026. 3. 10.
 */
public class OverseaIndividualTrademarkAppResponse {
    @Builder
    public record TrademarkAppDetailResponse(
            @Schema(description = "해외 기본 식별자")
            String appExtSeq,

            @Schema(description = "출원 식별키")
            String appSeq,

            @Schema(description = "출원상태")
            CommonRecordResponse.CodeInfo appStatus,

            @Schema(description = "출원 사건 관리")
            OverseaIndividualTrademarkAppRequest.TrademarkAppCaseMng appCaseMng,

            @Schema(description = "출원 기본 정보")
            OverseaIndividualTrademarkAppRequest.TrademarkAppBaseInfo appBaseInfo,

            @Schema(description = "담당 정보")
            OverseaIndividualTrademarkAppRequest.TrademarkAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            OverseaIndividualTrademarkAppRequest.TrademarkAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭 정보")
            OverseaIndividualTrademarkAppRequest.TrademarkAppNameInfo appNameInfo,

            @Schema(description = "물품류 (상표/디자인)")
            OverseaIndividualTrademarkAppRequest.GoodsClass goodsClass,

            @Schema(description = "출원 전략설정")
            OverseaIndividualTrademarkAppRequest.TrademarkAppStrategy appStrategy,

            @Schema(description = "비고")
            OverseaIndividualTrademarkAppRequest.AppNote appNote,

            @Schema(description = "출원 행정관리")
            OverseaIndividualTrademarkAppRequest.TrademarkAppManagement appManagement,

            @Schema(description = "등록 및 권리유지 관리")
            OverseaIndividualTrademarkAppRequest.TrademarkAppMaintenance appMaintenance,

            @Schema(description = "첨부파일정보")
            List<CommonRecordResponse.FileInfo> fileInfo
    ) {
        public static OverseaIndividualTrademarkAppResponse.TrademarkAppDetailResponse fromVOViewTrademark(
                CommonAppVO vo,
                List<CommonRecordResponse.CounterPartyInfo> clientList,
                List<CommonRecordResponse.CounterPartyInfo> applicantList,
                List<CommonRecordResponse.CounterPartyInfo> foreignAgentList,
                List<CommonRecordResponse.CounterPartyInfo> regMgrList,
                List<PaperResponseVO> fileList) {
            return OverseaIndividualTrademarkAppResponse.TrademarkAppDetailResponse.builder()
                    .appSeq(vo.getAppSeq())
                    .appExtSeq(vo.getAppExtSeq())
                    .appStatus(new CommonRecordResponse.CodeInfo(vo.getStatusCode(), vo.getStatusName()))

                    // 1. 사건 관리
                    .appCaseMng(
                            OverseaIndividualTrademarkAppRequest.TrademarkAppCaseMng.builder()
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
                                    .appCategory(
                                            CommonRecordResponse.CodeInfo.builder()
                                                    .code(vo.getAppCategoryCode())
                                                    .codeName(vo.getAppCategoryName())
                                                    .build()
                                    )
                                    .appCountryInfo(
                                            CommonRecordResponse.CodeInfo.builder()
                                                    .code(vo.getCountryCode())
                                                    .codeName(vo.getCountryName())
                                                    .build()
                                    )
                                    .appCountry(vo.getCountryName())
                                    .receiptDate(formatMinusHoursString8(vo.getReceiptDate()))
                                    .ourRef(vo.getOurRef())
                                    .yourRef(vo.getYourRef())
                                    .clientRef(vo.getClientRef())
                                    .build()
                    )
                    // 2. 기본 정보
                    .appBaseInfo(
                            OverseaIndividualTrademarkAppRequest.TrademarkAppBaseInfo.builder()
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
                                    .originalAppInfo(
                                            new OverseaIndividualTrademarkAppRequest.OriginalAppInfo(
                                                    formatMinusHoursString8(vo.getOriginalAppDate()),
                                                    vo.getOriginalAppNo()
                                            )
                                    )
                                    .build()
                    )
                    // 3. 담당 정보
                    .appManagerInfo(
                            OverseaIndividualTrademarkAppRequest.TrademarkAppManagerInfo.builder()
                                    .deptCode(vo.getDeptName())
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
                            new OverseaIndividualTrademarkAppRequest.TrademarkAppCounterPartyInfo(
                                    clientList,
                                    applicantList,
                                    new CommonRecordResponse.PersonInfo(
                                            vo.getAppManager(),
                                            vo.getAppManagerNm()
                                    ),
                                    regMgrList,
                                    foreignAgentList
                            )
                    )
                    // 5. 명칭 정보
                    .appNameInfo(
                            new OverseaIndividualTrademarkAppRequest.TrademarkAppNameInfo(
                                    vo.getTitleKo(),
                                    vo.getTitleEn()
                            )
                    )
                    // 7. 전략 설정 (관계 정보)
                    .appStrategy(
                            OverseaIndividualTrademarkAppRequest.TrademarkAppStrategy.builder()
                                    .originalAppInfo(
                                            new OverseaIndividualTrademarkAppRequest.OriginalAppInfo(
                                                    formatMinusHoursString8(vo.getOriginalAppDate()),
                                                    vo.getOriginalAppNo()
                                            )
                                    )
                                    .reAppInfo(
                                            new OverseaIndividualTrademarkAppRequest.ReAppInfo(
                                                    formatMinusHoursString8(vo.getReAppDate()),
                                                    vo.getReAppNo()
                                            )
                                    )
                                    .build()
                    )
                    .goodsClass(
                            new OverseaIndividualTrademarkAppRequest.GoodsClass(
                                    vo.getGoodsClass()
                            )
                    )
                    .appNote(
                            new OverseaIndividualTrademarkAppRequest.AppNote(
                                    vo.getNote()
                            )
                    )
                    .appManagement(
                            OverseaIndividualTrademarkAppRequest.TrademarkAppManagement.builder()
                                    .announcementDecisionDate(formatMinusHoursString8(vo.getAnnouncementDecisionDate()))
                                    .announcementDate(formatMinusHoursString8(vo.getAnnouncementDate()))
                                    .announcementNo(vo.getAnnouncementNo())
                                    .abandonOrderDate(formatMinusHoursString8(vo.getAbandonOrderDate()))
                                    .abandonDate(formatMinusHoursString8(vo.getAbandonDate()))
                                    .abandonNote(vo.getAbandonNote())
                                    .build()
                    )
                    // 9. 유지 관리
                    .appMaintenance(
                            OverseaIndividualTrademarkAppRequest.TrademarkAppMaintenance.builder()
                                    .isRenewalManaged(vo.getIsRenewalManaged())
                                    .renewalDeadline(formatMinusHoursString8(vo.getRenewalDeadline()))
                                    .regDecisionDate(formatMinusHoursString8(vo.getRegDecisionDate()))
                                    .regNormalDeadline(formatMinusHoursString8(vo.getRegNormalDeadline()))
                                    .regGraceDeadline(formatMinusHoursString8(vo.getRegGraceDeadline()))
                                    .regOrderDate(formatMinusHoursString8(vo.getRegOrderDate()))
                                    .regPaymentDate(formatMinusHoursString8(vo.getRegPaymentDate()))
                                    .regDate(formatMinusHoursString8(vo.getRegDate()))
                                    .regNo(vo.getRegNo())
                                    .regAnnounceDate(formatMinusHoursString8(vo.getRegAnnounceDate()))
                                    .regAnnounceNo(vo.getRegAnnounceNo())
                                    .nextPaymentInstallment(vo.getNextPaymentInstallment())
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
