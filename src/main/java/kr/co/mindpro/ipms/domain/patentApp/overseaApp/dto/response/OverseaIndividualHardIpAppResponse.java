package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaIndividualHardIpAppRequest;
import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaIndividualAppResponse.java
 * @since : 2026. 3. 9.
 */
public class OverseaIndividualHardIpAppResponse {
    @Builder
    public record HardIpAppDetailResponse(
            @Schema(description = "해외 기본 식별키")
            String appExtSeq,

            @Schema(description = "출원 식별키")
            String appSeq,

            @Schema(description = "출원상태")
            CommonRecordResponse.CodeInfo appStatus,

            @Schema(description = "출원기본정보")
            OverseaIndividualHardIpAppRequest.HardIpAppBaseInfo appBaseInfo,

            @Schema(description = "출원 사건관리")
            OverseaIndividualHardIpAppRequest.HardIpAppCaseMng appCaseMng,

            @Schema(description = "담당 정보")
            OverseaIndividualHardIpAppRequest.HardIpAppManagerInfo appManagerInfo,

            @Schema(description = "당사자 정보")
            OverseaIndividualHardIpAppRequest.HardIpAppCounterPartyInfo appCounterPartyInfo,

            @Schema(description = "명칭 정보")
            OverseaIndividualHardIpAppRequest.HardIpAppNameInfo appNameInfo,

            @Schema(description = "명세서 구성요소")
            OverseaIndividualHardIpAppRequest.HardIpAppSpecificElement appSpecificElement,

            @Schema(description = "출원 전략설정")
            OverseaIndividualHardIpAppRequest.HardIpAppStrategy appStrategy,

            @Schema(description = "비고")
            OverseaIndividualHardIpAppRequest.HardIpAppNote appNote,

            @Schema(description = "출원 행정관리")
            OverseaIndividualHardIpAppRequest.HardIpAppManagement appManagement,

            @Schema(description = "등록/권리유지 관리")
            OverseaIndividualHardIpAppRequest.HardIpAppMaintenance appMaintenance,

            @Schema(description = "요약/청구 탭")
            OverseaIndividualHardIpAppRequest.ClaimSummaryInfo claimSummaryInfo,

            @Schema(description = "첨부파일정보")
            List<CommonRecordResponse.FileInfo> fileInfo
    ) {
        public static OverseaIndividualHardIpAppResponse.HardIpAppDetailResponse fromVOViewPatent(
                CommonAppVO vo,
                List<CommonRecordResponse.CounterPartyInfo> clientList,
                List<CommonRecordResponse.CounterPartyInfo> applicantList,
                List<CommonRecordResponse.CounterPartyInfo> foreignAgentList,
                List<CommonRecordResponse.CounterPartyInfo> regMgrList,
                List<PaperResponseVO> fileList) {
            return OverseaIndividualHardIpAppResponse.HardIpAppDetailResponse.builder()
                    .appSeq(vo.getAppSeq())
                    .appExtSeq(vo.getAppExtSeq())
                    .appStatus(new CommonRecordResponse.CodeInfo(vo.getStatusCode(), vo.getStatusName()))

                    // 1. 사건 관리
                    .appCaseMng(
                            OverseaIndividualHardIpAppRequest.HardIpAppCaseMng.builder()
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
                            OverseaIndividualHardIpAppRequest.HardIpAppBaseInfo.builder()
                                    .appOrderDate(formatMinusHoursString8(vo.getAppOrderDate()))
                                    .appDeadline(formatMinusHoursString8(vo.getAppDeadline()))
                                    .oaDeliveryDate(formatMinusHoursString8(vo.getOaDeliveryDate()))
                                    .appDate(formatMinusHoursString8(vo.getAppDate()))
                                    .appNo(vo.getAppNo())
                                    .build()
                    )
                    // 3. 담당 정보
                    .appManagerInfo(
                            new OverseaIndividualHardIpAppRequest.HardIpAppManagerInfo(
                                    vo.getDeptName(),
                                    new CommonRecordResponse.PersonInfo(
                                            vo.getApplicantContact(),
                                            vo.getApplicantContactNm()
                                    ),
                                    new CommonRecordResponse.PersonInfo(
                                            vo.getAdminMgr(),
                                            vo.getAdminMgrNm()
                                    ),
                                    new CommonRecordResponse.PersonInfo(
                                            vo.getCaseMgr(),
                                            vo.getCaseMgrNm()
                                    ),
                                    new CommonRecordResponse.PersonInfo(
                                            vo.getAttorney(),
                                            vo.getAttorneyNm()
                                    )
                            )
                    )
                    // 4. 당사자 정보
                    .appCounterPartyInfo(
                            new OverseaIndividualHardIpAppRequest.HardIpAppCounterPartyInfo(
                                    foreignAgentList,
                                    clientList,
                                    applicantList,
                                    new CommonRecordResponse.PersonInfo(
                                            vo.getInventor(),
                                            vo.getInventorNm()
                                    ),
                                    regMgrList
                            )
                    )
                    // 5. 명칭 정보
                    .appNameInfo(
                            new OverseaIndividualHardIpAppRequest.HardIpAppNameInfo(
                                    vo.getTitleKo(),
                                    vo.getTitleEn()
                            )
                    )
                    // 6. 명세서 정보
                    .appSpecificElement(
                            new OverseaIndividualHardIpAppRequest.HardIpAppSpecificElement(
                                    CommonRecordResponse.CodeInfo.builder()
                                            .code(vo.getGradeCode())
                                            .codeName(vo.getGradeName())
                                            .build(),
                                    vo.getIndependentClaims(),
                                    vo.getDependentClaims(),
                                    vo.getOverseaSpecPage(), // 해외명세서 페이지 (순서 DTO 확인 필요)
                                    vo.getDrawingCount(), // 도면 수
                                    vo.getSpecPage()
                            )
                    )
                    // 7. 전략 설정 (관계 정보)
                    .appStrategy(
                            OverseaIndividualHardIpAppRequest.HardIpAppStrategy.builder()
                                    .provisionalAppInfo(
                                            new OverseaIndividualHardIpAppRequest.ProvisionalAppInfo(
                                                    formatMinusHoursString8(vo.getProvisionalAppDate()),
                                                    vo.getProvisionalAppNo()
                                            )
                                    )
                                    .firstAppInfo(
                                            new OverseaIndividualHardIpAppRequest.FirstAppInfo(
                                                    formatMinusHoursString8(vo.getFirstAppDate()),
                                                    vo.getFirstAppNo()
                                            ))
                                    .originalAppInfo(
                                            new OverseaIndividualHardIpAppRequest.OriginalAppInfo(
                                                    formatMinusHoursString8(vo.getOriginalAppDate()),
                                                    vo.getOriginalAppNo()
                                            )
                                    )
                                    .reAppInfo(
                                            new OverseaIndividualHardIpAppRequest.ReAppInfo(
                                                    formatMinusHoursString8(vo.getReAppDate()),
                                                    vo.getReAppNo()
                                            )
                                    )
                                    .globalAppInfo(
                                            new OverseaIndividualHardIpAppRequest.GlobalAppInfo(
                                                    formatMinusHoursString8(vo.getGlobalAppDate()),
                                                    vo.getGlobalAppNo()
                                            )
                                    )
                                    .build()
                    )
                    .appNote(
                            new OverseaIndividualHardIpAppRequest.HardIpAppNote(
                                    vo.getNote()
                            )
                    )
                    .appManagement(
                            OverseaIndividualHardIpAppRequest.HardIpAppManagement.builder()
                                    .ipcClassification(vo.getIpcClassification())
                                    .parentRegAppDate(formatMinusHoursString8(vo.getParentRegAppDate()))
                                    .examRequestDeadline(formatMinusHoursString8(vo.getExamRequestDeadline()))
                                    .examRequestOrderDate(formatMinusHoursString8(vo.getExamRequestOrderDate()))
                                    .examRequestDate(formatMinusHoursString8(vo.getExamRequestDate()))
                                    .pubDate(formatMinusHoursString8(vo.getPubDate()))
                                    .pubNo(vo.getPubNo())
                                    .announcementDate(formatMinusHoursString8(vo.getAnnouncementDate()))
                                    .announcementNo(vo.getAnnouncementNo())
                                    .abandonOrderDate(formatMinusHoursString8(vo.getAbandonOrderDate()))
                                    .abandonDate(formatMinusHoursString8(vo.getAbandonDate()))
                                    .abandonNote(vo.getAbandonNote())
                                    .build()
                    )
                    // 9. 유지 관리
                    .appMaintenance(
                            OverseaIndividualHardIpAppRequest.HardIpAppMaintenance.builder()
                                    .finalClaimCount(vo.getFinalClaimsCount())
                                    .kipoDelayDays(vo.getKipoDelayDays())
                                    .rightPeriod(formatMinusHoursString8(vo.getRightPeriod()))
                                    .isAnnuityManaged(vo.getIsAnnuityManaged())
                                    .regDecisionDate(formatMinusHoursString8(vo.getRegDecisionDate()))
                                    .regReceiptDate(formatMinusHoursString8(vo.getRegReceiptDate()))
                                    .regNormalDeadline(formatMinusHoursString8(vo.getRegNormalDeadline()))
                                    .regGraceDeadline(formatMinusHoursString8(vo.getRegGraceDeadline()))
                                    .regOrderDate(formatMinusHoursString8(vo.getRegOrderDate()))
                                    .regPaymentDate(formatMinusHoursString8(vo.getRegPaymentDate()))
                                    .regDate(formatMinusHoursString8(vo.getRegDate()))
                                    .regNo(vo.getRegNo())
                                    .regAnnounceDate(formatMinusHoursString8(vo.getRegAnnounceDate()))
                                    .regAnnounceNo(vo.getRegAnnounceNo())
                                    .nextPaymentInstallment(vo.getNextPaymentInstallment())
                                    .annuityOrderDate(formatMinusHoursString8(vo.getAnnuityOrderDate()))
                                    .annuityAgency(vo.getAnnuityAgency())
                                    .standardDeadline(formatMinusHoursString8(vo.getStandardDeadline()))
                                    .penaltyDeadline(formatMinusHoursString8(vo.getPenaltyDeadline()))
                                    .build()
                    )
                    .claimSummaryInfo(
                            new OverseaIndividualHardIpAppRequest.ClaimSummaryInfo(
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
