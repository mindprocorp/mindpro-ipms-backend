package kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.vo.AppExtMstVO;
import lombok.Builder;

import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

/**
 * @author : seokho
 * @fileName : OverseaBasicAppListResponse.java
 * @since : 2026. 3. 12.
 */
public class OverseaBasicAppListResponse {
    @Builder(toBuilder = true)
    @Schema(description = " 해외 기초 출원 리스트 응답")
    public record BasicListDetailResponse(
            @Schema(description = "출원 식별자")
            String appExtSeq,

            @Schema(description = "권리")
            CommonRecordResponse.CodeInfo rightType,

            @Schema(description = "현재상태")
            CommonRecordResponse.CodeInfo status,

            @Schema(description = "ourRef")
            String ourRef,

            @Schema(description = "접수일")
            String receiptDate,

            @Schema(description = "출원종류")
            CommonRecordResponse.CodeInfo appType,

            @Schema(description = "의뢰인 이름 모음")
            String clientNm,

            @Schema(description = "출원인 이름 모음")
            String applicantNm,

            @Schema(description = "명칭정보")
            OverseaAppNameInfo appNameInfo,

            @Schema(description = "개국수")
            int designatedIndividualCnt,

            @Schema(description = "PCT수")
            int designatedPctCnt,

            @Schema(description = "EP수")
            int designatedEpCnt,

            @Schema(description = "마드리드 수")
            int designatedMadridCnt,

            @Schema(description = "국제디자인 수")
            int designatedIntlDesignCnt,

            @Schema(description = "포기취하일")
            String abandonDate,

            @Schema(description = "포기내용")
            String abandonNote,

            @Schema(description = "관리담당자")
            CommonRecordResponse.PersonInfo adminMgrInfo,

            @Schema(description = "사건담당자")
            CommonRecordResponse.PersonInfo caseMgrInfo,

            @Schema(description = "담당변리사")
            CommonRecordResponse.PersonInfo attorneyInfo,

            @Schema(description = "비고")
            String note
    ) {
        public static OverseaBasicAppListResponse.BasicListDetailResponse from(CommonAppVO vo) {
            if (vo == null) return null;

            return BasicListDetailResponse.builder()
                    .appExtSeq(vo.getAppExtSeq())
                    .rightType(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getRightTypeCode())
                            .codeName(vo.getRightTypeName())
                            .build()
                    )
                    .status(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getStatusCode())
                            .codeName(vo.getStatusName())
                            .build()
                    )
                    .ourRef(vo.getOurRef())
                    .receiptDate(formatMinusHoursString8(vo.getReceiptDate()))
                    .appType(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getAppTypeCode())
                            .codeName(vo.getAppTypeName())
                            .build()
                    )
                    .clientNm(vo.getClientNm())
                    .applicantNm(vo.getApplicantNm())
                    .appNameInfo(new OverseaAppNameInfo(
                            vo.getTitleKo(),
                            vo.getTitleEn())
                    )

                    .designatedIndividualCnt(vo.getIndividualCountryCnt() == null ? 0 : vo.getIndividualCountryCnt())
                    .designatedPctCnt(vo.getPctCnt() == null ? 0 : vo.getPctCnt())
                    .designatedEpCnt(vo.getEpCnt() == null ? 0 : vo.getEpCnt())
                    .designatedMadridCnt(vo.getMadridCnt() == null ? 0 : vo.getMadridCnt())
                    .designatedIntlDesignCnt(vo.getInternationalDesignCnt() == null ? 0 : vo.getInternationalDesignCnt())
                    .abandonDate(formatMinusHoursString8(vo.getAbandonDate()))
                    .abandonNote(vo.getAbandonNote())
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
                    .note(vo.getNote())
                    .build();
        }
    }

    @Schema(description = "명칭_정보")
    public record OverseaAppNameInfo(
            @Schema(description = "국문 명칭", example = "차세대 반도체 제조 장치")
            String titleKo,

            @Schema(description = "영문 명칭", example = "Next-gen Semiconductor Manufacturing Device")
            String titleEn
    ) {}
}
