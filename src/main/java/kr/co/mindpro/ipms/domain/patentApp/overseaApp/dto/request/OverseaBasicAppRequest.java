package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaBasicAppRequest.java
 * @since : 2026. 3. 6.
 */
public class OverseaBasicAppRequest {

    public record CreateOverseaBasicApp(
            @Schema(description = "해외기본 시퀀스(수정에 필요)")
            String appExtSeq,

            @Builder
            @Schema(description = "출원_사건관리")
            BasicAppCaseMng appCaseMng,

            @Builder
            @Schema(description = "담당 정보")
            BasicAppManagerInfo appManagerInfo,

            @Builder
            @Schema(description = "당사자 정보")
            BasicAppCounterParty appCounterPartyInfo,

            @Builder
            @Schema(description = "명칭 정보")
            BasicAppNameInfo appNameInfo,

            @Builder
            @Schema(description = "물품류")
            GoodsClass goodsClass,

            @Builder
            @Schema(description = "명세서 구성요소")
            BasicAppSpecificElement appSpecificElement,

            @Builder
            @Schema(description = "비고")
            AppNote appNote,

            @Builder
            @Schema(description = "명세서 구성요소")
            BasicAppStrategy appStrategy,

            @Builder
            @Schema(description = "지정국가 정보 (해외 전용)")
            BasicAppDesignatedStateInfo designatedStateInfo
    ) {}

    @Builder
    public record BasicAppCaseMng(

            @Schema(description = "권리")
            CommonRecordResponse.CodeInfo rightType,

            @Schema(description = "출원종류")
            CommonRecordResponse.CodeInfo appType,

            @Schema(description = "OurRef", example = "OUR123456")
            String ourRef,

            @Schema(description = "접수일", example = "20260109")
            String receiptDate,

            @Schema(description = "출원완료일", example = "20260215")
            String appCompleteDate,

            @Schema(description = "출원담당자 정보")
            CommonRecordResponse.PersonInfo appManagerInfo,

            @Schema(description = "사건분야코드 or 사건번호", example = "CASE-2026-0005")
            String caseNo
    ) {}

    @Builder
    public record BasicAppManagerInfo(
            @Schema(description = "부서", example = "해외관리팀")
            String deptCode,

            @Schema(description = "관리담당자")
            CommonRecordResponse.PersonInfo adminMgrInfo,

            @Schema(description = "사건담당자")
            CommonRecordResponse.PersonInfo caseMgrInfo,

            @Schema(description = "담당변리사")
            CommonRecordResponse.PersonInfo attorneyInfo
    ) {}

    @Builder
    public record BasicAppCounterParty(
            @Schema(description = "의뢰인")
            List<CommonRecordResponse.CounterPartyInfo> clientInfo,

            @Schema(description = "의뢰인 담당자(해외기본 전용)")
            CommonRecordResponse.PersonInfo clientContactInfo,

            @Schema(description = "출원인")
            List<CommonRecordResponse.CounterPartyInfo> applicantInfo,

            @Schema(description = "창작자")
            CommonRecordResponse.PersonInfo inventorInfo,

            @Schema(description = "등록권리자")
            List<CommonRecordResponse.CounterPartyInfo> regMgrInfo
    ) {}

    @Schema(description = "명칭_정보")
    public record BasicAppNameInfo(
            @Schema(description = "국문 명칭", example = "차세대 반도체 제조 장치")
            String titleKo,

            @Schema(description = "영문 명칭", example = "Next-gen Semiconductor Manufacturing Device")
            String titleEn
    ) {}

    @Schema(description = "물품류")
    public record GoodsClass(
            @Schema(description = "물품류", example = "09류")
            String goodsClass
    ) {}

    @Builder
    public record BasicAppSpecificElement(
            @Schema(description = "등급")
            CommonRecordResponse.CodeInfo grade,

            @Schema(description = "독립항", example = "5")
            String independentClaims,

            @Schema(description = "종속항", example = "6")
            String dependentClaims,

            @Schema(description = "명세서", example = "7")
            String overseaSpecPage,

            @Schema(description = "도면수", example = "5")
            String drawingCount
    ) {}

    @Schema(description = "비고")
    public record AppNote(

            @Schema(description = "비고", example = "testVal")
            String note
    ) {}

    @Schema(description = "국제출원정보")
    public record GlobalAppInfo(
            @Schema(description = "국제출원일", example = "20251120")
            String globalAppDate,

            @Schema(description = "국제출원번호", example = "PCT/KR2025/001234")
            String globalAppNo
    ) {}

    public record BasicAppStrategy(
            @Schema(description = "국제_출원_정보")
            GlobalAppInfo globalAppInfo
    ) {}

    public record BasicAppDesignatedStateInfo(
            @Schema(description = "개국", example = "[\"US\", \"JP\", \"CN\"]")
            List<String> designatedIndividual,

            @Schema(description = "PCT", example = "[\"PCT\"]")
            List<String> designatedPct,

            @Schema(description = "EP", example = "[\"DE\", \"FR\", \"GB\"]")
            List<String> designatedEp,

            @Schema(description = "마드리드", example = "[\"MADRID\"]")
            List<String> designatedMadrid,

            @Schema(description = "국제디자인", example = "[\"HAGUE\"]")
            List<String> designatedIntlDesign,

            @Schema(description = "포기일자", example = "20291231")
            String abandonDate,

            @Schema(description = "포기내용", example = "전략적 포기")
            String abandonContent
    ) {}


}
