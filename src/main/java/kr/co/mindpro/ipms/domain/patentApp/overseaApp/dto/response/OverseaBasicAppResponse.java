package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaBasicAppRequest;
import lombok.Builder;

import java.util.Arrays;
import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaBasicAppResponse.java
 * @since : 2026. 3. 6.
 */
public class OverseaBasicAppResponse {
    @Builder
    public record OverseaBasicAppDetailResponse(
            @Schema(description = "출원 식별자")
            String appExtSeq,

            @Builder
            @Schema(description = "출원_사건관리")
            OverseaBasicAppRequest.BasicAppCaseMng appCaseMng,

            @Builder
            @Schema(description = "담당 정보")
            OverseaBasicAppRequest.BasicAppManagerInfo appManagerInfo,

            @Builder
            @Schema(description = "당사자 정보")
            OverseaBasicAppRequest.BasicAppCounterParty appCounterPartyInfo,

            @Builder
            @Schema(description = "명칭 정보")
            OverseaBasicAppRequest.BasicAppNameInfo appNameInfo,

            @Builder
            @Schema(description = "물품류")
            OverseaBasicAppRequest.GoodsClass goodsClass,

            @Builder
            @Schema(description = "명세서 구성요소")
            OverseaBasicAppRequest.BasicAppSpecificElement appSpecificElement,

            @Builder
            @Schema(description = "비고")
            OverseaBasicAppRequest.AppNote appNote,

            @Builder
            @Schema(description = "명세서 구성요소")
            OverseaBasicAppRequest.BasicAppStrategy appStrategy,

            @Builder
            @Schema(description = "지정국가 정보 (해외 전용)")
            OverseaBasicAppRequest.BasicAppDesignatedStateInfo designatedStateInfo,

            @Schema(description = "첨부파일정보")
            List<CommonRecordResponse.FileInfo> fileInfo
    ) {
            public static OverseaBasicAppResponse.OverseaBasicAppDetailResponse from(CommonAppVO vo,
                                                                                     List<CommonRecordResponse.CounterPartyInfo> clients,
                                                                                     List<CommonRecordResponse.CounterPartyInfo> applicants,
                                                                                     List<CommonRecordResponse.CounterPartyInfo> regMgrs,
                                                                                     List<PaperResponseVO> files) {
                    return OverseaBasicAppDetailResponse.builder()
                            .appExtSeq(vo.getAppExtSeq())

                            // 1. 사건 관리 (상단 바)
                            .appCaseMng(OverseaBasicAppRequest.BasicAppCaseMng.builder()
                                    .rightType(new CommonRecordResponse.CodeInfo(
                                            vo.getRightTypeCode(),
                                            vo.getRightTypeName()
                                    ))       // 권리
                                    .appType(new CommonRecordResponse.CodeInfo(
                                            vo.getAppTypeCode(),
                                            vo.getAppTypeName()
                                    ))           // 출원종류
                                    .ourRef(vo.getOurRef())             // OurRef
                                    .receiptDate(formatMinusHoursString8(vo.getReceiptDate()))   // 접수일
                                    .appCompleteDate(formatMinusHoursString8(vo.getAppCompleteDate())) // 출원완료일
                                    .caseNo(vo.getCaseNo())             // 사건번호
                                    // 출원담당자 (상단)
                                    .appManagerInfo(new CommonRecordResponse.PersonInfo(
                                            vo.getAppManager(),
                                            vo.getAppManagerNm()))
                                    .build())

                            // 2. 담당 정보 (좌측 상단)
                            // (화면에 없는 출원인담당(ApplicantContact) 제외)
                            .appManagerInfo(OverseaBasicAppRequest.BasicAppManagerInfo.builder()
                                    .deptCode(vo.getDeptName())         // 부서
                                    .adminMgrInfo(new CommonRecordResponse.PersonInfo(
                                            vo.getAdminMgr(),
                                            vo.getAdminMgrNm())
                                    ) // 관리담당자
                                    .caseMgrInfo(new CommonRecordResponse.PersonInfo(
                                            vo.getCaseMgr(),
                                            vo.getCaseMgrNm())
                                    )     // 사건담당자
                                    .attorneyInfo(new CommonRecordResponse.PersonInfo(
                                            vo.getAttorney(),
                                            vo.getAttorneyNm())
                                    ) // 담당변리사
                                    .build())

                            // 3. 당사자 정보 (좌측 하단)
                            // (화면에 없는 해외대리인, 해외의뢰인 제외)
                            .appCounterPartyInfo(OverseaBasicAppRequest.BasicAppCounterParty.builder()
                                    .clientInfo(clients) // 의뢰인
                                    .clientContactInfo(new CommonRecordResponse.PersonInfo(vo.getClientContact(), vo.getClientContactNm())) // 의뢰인 담당자
                                    .applicantInfo(applicants) // 출원인
                                    .inventorInfo(new CommonRecordResponse.PersonInfo(vo.getInventor(), vo.getInventorNm()))     // 창작자
                                    .regMgrInfo(regMgrs)             // 등록권리자
                                    .build())

                            // 4. 명칭 정보
                            .appNameInfo(new OverseaBasicAppRequest.BasicAppNameInfo(vo.getTitleKo(), vo.getTitleEn()))

                            // 5. 물품류
                            .goodsClass(new OverseaBasicAppRequest.GoodsClass(vo.getGoodsClass()))

                            // 6. 명세서 구성요소
                            // (화면에 해외명세서는 없음)
                            .appSpecificElement(OverseaBasicAppRequest.BasicAppSpecificElement.builder()
                                    .grade(new CommonRecordResponse.CodeInfo(
                                            vo.getGradeCode(),
                                            vo.getGradeName()
                                    ))
                                    .independentClaims(vo.getIndependentClaims())
                                    .dependentClaims(vo.getDependentClaims())
                                    .overseaSpecPage(vo.getOverseaSpecPage())     // 명세서
                                    .drawingCount(vo.getDrawingCount()) // 도면
                                    .build())

                            // 7. 전략 설정 (화면엔 '국제출원'만 존재)
                            .appStrategy(new OverseaBasicAppRequest.BasicAppStrategy(
                                    new OverseaBasicAppRequest.GlobalAppInfo(
                                            formatMinusHoursString8(vo.getGlobalAppDate()), // 출원일
                                            vo.getGlobalAppNo()    // 출원번호
                                    )
                            ))

                            // 9. 비고
                            .appNote(new OverseaBasicAppRequest.AppNote(vo.getNote()))

                            // 10. 지정국가 (우측 사이드바)
                            .designatedStateInfo(new OverseaBasicAppRequest.BasicAppDesignatedStateInfo(
                                    vo.getDesignatedIndividual() != null
                                            ? Arrays.stream(vo.getDesignatedIndividual().split(",")).map(String::trim).toList()
                                            : null,
                                    vo.getDesignatedPct() != null
                                            ? Arrays.stream(vo.getDesignatedPct().split(",")).map(String::trim).toList()
                                            : null,
                                    vo.getDesignatedEp() != null
                                            ? Arrays.stream(vo.getDesignatedEp().split(",")).map(String::trim).toList()
                                            : null,
                                    vo.getDesignatedMadrid() != null
                                            ? Arrays.stream(vo.getDesignatedMadrid().split(",")).map(String::trim).toList()
                                            : null,
                                    vo.getDesignatedIntlDesign() != null
                                            ? Arrays.stream(vo.getDesignatedIntlDesign().split(",")).map(String::trim).toList()
                                            : null,
                                    formatMinusHoursString8(vo.getAbandonDate()),
                                    vo.getAbandonNote())
                            )
                            .fileInfo(CommonRecordResponse.FileInfo.from(files))

                            .build();
            }
    }
}
