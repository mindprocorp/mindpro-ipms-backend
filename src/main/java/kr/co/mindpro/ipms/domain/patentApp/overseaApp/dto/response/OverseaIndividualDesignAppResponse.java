package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaIndividualDesignAppRequest;
import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaIndividualDesignAppResponse.java
 * @since : 2026. 3. 10.
 */
public class OverseaIndividualDesignAppResponse {
    @Builder
    public record DesignAppDetailResponse(
            @Schema(description = "해외 기본 식별키")
            String appExtSeq,

            @Schema(description = "출원 식별키")
            String appSeq,

            @Schema(description = "출원상태")
            CommonRecordResponse.CodeInfo appStatus,

            @Schema(description = "출원사건정보")
            OverseaIndividualDesignAppRequest.DesignAppCaseMng appCaseMng,

            @Schema(description = "출원기본정보")
            OverseaIndividualDesignAppRequest.DesignAppBaseInfo appBaseInfo,

            @Schema(description = "담당자정보")
            OverseaIndividualDesignAppRequest.DesignAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            OverseaIndividualDesignAppRequest.DesignAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭 정보")
            OverseaIndividualDesignAppRequest.DesignAppNameInfo appNameInfo,

            @Schema(description = "전략 설정")
            OverseaIndividualDesignAppRequest.DesignAppStrategy appStrategy,

            @Schema(description = "비고")
            OverseaIndividualDesignAppRequest.HardIpAppNote appNote,

            @Schema(description = "출원 행정관리")
            OverseaIndividualDesignAppRequest.DesignAppManagement appManagement,

            @Schema(description = "등록/권리유지 관리")
            OverseaIndividualDesignAppRequest.DesignAppMaintenance appMaintenance,

            @Schema(description = "디자인 설명/요점")
            OverseaIndividualDesignAppRequest.DesignDescription designDescription,

            @Schema(description = "첨부파일정보")
            List<CommonRecordResponse.FileInfo> fileInfo
    ) {
        public static OverseaIndividualDesignAppResponse.DesignAppDetailResponse fromVOViewDesign(
                CommonAppVO vo,
                List<CommonRecordResponse.CounterPartyInfo> clientList,
                List<CommonRecordResponse.CounterPartyInfo> applicantList,
                List<CommonRecordResponse.CounterPartyInfo> foreignAgentList,
                List<CommonRecordResponse.CounterPartyInfo> regMgrList,
                List<PaperResponseVO> fileList) {
            return OverseaIndividualDesignAppResponse.DesignAppDetailResponse.builder()
                    .appSeq(vo.getAppSeq())
                    .appExtSeq(vo.getAppExtSeq())
                    .appStatus(new CommonRecordResponse.CodeInfo(vo.getStatusCode(), vo.getStatusName()))

                    // 1. 사건 관리
                    .appCaseMng(
                            OverseaIndividualDesignAppRequest.DesignAppCaseMng.builder()
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
                            OverseaIndividualDesignAppRequest.DesignAppBaseInfo.builder()
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
                                    .build()
                    )
                    // 3. 담당 정보
                    .appManagerInfo(
                            OverseaIndividualDesignAppRequest.DesignAppManagerInfo.builder()
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
                            new OverseaIndividualDesignAppRequest.DesignAppCounterPartyInfo(
                                    foreignAgentList,
                                    clientList,
                                    applicantList,
                                    new CommonRecordResponse.PersonInfo(
                                            vo.getInventor(),
                                            vo.getInventorNm()
                                    ),
                                    new CommonRecordResponse.PersonInfo(
                                            vo.getAppManager(),
                                            vo.getAppManagerNm()
                                    ),
                                    regMgrList
                            )
                    )
                    // 5. 명칭 정보
                    .appNameInfo(
                            new OverseaIndividualDesignAppRequest.DesignAppNameInfo(
                                    vo.getTitleKo(),
                                    vo.getTitleEn()
                            )
                    )
                    // 7. 전략 설정 (관계 정보)
                    .appStrategy(
                            OverseaIndividualDesignAppRequest.DesignAppStrategy.builder()
                                    .parentRegAppDate(formatMinusHoursString8(vo.getParentRegAppDate()))
                                    .parentRegAppNo(vo.getParentRegAppNo())
                                    .firstAppInfo(
                                            new OverseaIndividualDesignAppRequest.FirstAppInfo(
                                                    formatMinusHoursString8(vo.getFirstAppDate()),
                                                    vo.getFirstAppNo()
                                            ))
                                    .originalAppInfo(
                                            new OverseaIndividualDesignAppRequest.OriginalAppInfo(
                                                    formatMinusHoursString8(vo.getOriginalAppDate()),
                                                    vo.getOriginalAppNo()
                                            )
                                    )
                                    .originalRegInfo(
                                            new OverseaIndividualDesignAppRequest.OriginalRegInfo(
                                                    formatMinusHoursString8(vo.getOriginalRegDate()),
                                                    vo.getOriginalRegNo()
                                            )
                                    )
                                    .reAppInfo(
                                            new OverseaIndividualDesignAppRequest.ReAppInfo(
                                                    formatMinusHoursString8(vo.getReAppDate()),
                                                    vo.getReAppNo()
                                            )
                                    )
                                    .build()
                    )
                    .appNote(
                            new OverseaIndividualDesignAppRequest.HardIpAppNote(
                                    vo.getNote()
                            )
                    )
                    .appManagement(
                            OverseaIndividualDesignAppRequest.DesignAppManagement.builder()
                                    .pubDate(formatMinusHoursString8(vo.getPubDate()))
                                    .pubNo(vo.getPubNo())
                                    .abandonOrderDate(formatMinusHoursString8(vo.getAbandonOrderDate()))
                                    .abandonDate(formatMinusHoursString8(vo.getAbandonDate()))
                                    .abandonNote(vo.getAbandonNote())
                                    .build()
                    )
                    // 9. 유지 관리
                    .appMaintenance(
                            OverseaIndividualDesignAppRequest.DesignAppMaintenance.builder()
                                    .kipoDelayDays(vo.getKipoDelayDays())
                                    .rightPeriod(formatMinusHoursString8(vo.getRightPeriod()))
                                    .isAnnuityManaged(vo.getIsAnnuityManaged())
                                    .regDecisionDate(formatMinusHoursString8(vo.getRegDecisionDate()))
                                    .regNormalDeadline(formatMinusHoursString8(vo.getRegNormalDeadline()))
                                    .regGraceDeadline(formatMinusHoursString8(vo.getRegGraceDeadline()))
                                    .regOrderDate(formatMinusHoursString8(vo.getRegOrderDate()))
                                    .regPaymentDate(formatMinusHoursString8(vo.getRegPaymentDate()))
                                    .regDate(formatMinusHoursString8(vo.getRegDate()))
                                    .regNo(vo.getRegNo())
                                    .regAnnounceDate(formatMinusHoursString8(vo.getRegAnnounceDate()))
                                    .regAnnounceNo(vo.getRegAnnounceNo())
                                    .goodsClass(new OverseaIndividualDesignAppRequest.GoodsClass(vo.getGoodsClass()))
                                    .nextPaymentInstallment(vo.getNextPaymentInstallment())
                                    .standardDeadline(formatMinusHoursString8(vo.getStandardDeadline()))
                                    .penaltyDeadline(formatMinusHoursString8(vo.getPenaltyDeadline()))
                                    .annuityOrderDate(formatMinusHoursString8(vo.getAnnuityOrderDate()))
                                    .annuityAgency(vo.getAnnuityAgency())
                                    .build()
                    )
                    .designDescription(
                            new OverseaIndividualDesignAppRequest.DesignDescription(
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
