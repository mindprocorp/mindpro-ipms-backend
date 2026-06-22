package kr.co.mindpro.ipms.domain.rnd.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.rnd.vo.RndVO;
import lombok.Builder;
import lombok.Data;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;

/**
 * @author : seokho
 * @fileName : RndResponse.java
 * @since : 2026. 2. 23.
 */
@Data
public class RndResponse {

    @Builder
    public record RndResponseDetail(
            @Schema(description = "연결될 출원 식별자")
            String appSeq,

            @Schema(description = "연구과제 식별자")
            String rndSeq,

            @Schema(description = "과제고유번호")
            String researchNo,

            @Schema(description = "과제번호")
            String projectNo,

            @Schema(description = "국가부처명")
            String ministryName,

            @Schema(description = "과제관리(전문)기관명")
            String agencyName,

            @Schema(description = "연구사업명")
            String bizName,

            @Schema(description = "연구과제명")
            String rndName,

            @Schema(description = "기여율(지분 비율) - (소수점 둘째자리까지 표기)")
            double shareRatio,

            @Schema(description = "연구시작일")
            String rndStartDate,

            @Schema(description = "연구종료일")
            String rndClosingDate,

            @Schema(description = "과제수행기관명")
            String mainLab,

            @Schema(description = "참여기관")
            String performingLab,

            @Schema(description = "연구비 총액", example = "150000")
            String totalRndCost,

            @Schema(description = "비고")
            String note
    ) {
        public static RndResponseDetail of (RndVO vo) {
            return RndResponseDetail.builder()
                    .appSeq(vo.getAppSeq())
                    .rndSeq(vo.getRndSeq())
                    .researchNo(vo.getResearchNo())
                    .projectNo(vo.getProjectNo())
                    .ministryName(vo.getMinistryName())
                    .agencyName(vo.getAgencyName())
                    .bizName(vo.getBizName())
                    .rndName(vo.getRndName())
                    .shareRatio(vo.getShareRatio())
                    .rndStartDate(formatMinusHoursString8(vo.getRndStartDate()))
                    .rndClosingDate(formatMinusHoursString8(vo.getRndClosingDate()))
                    .mainLab(vo.getMainLab())
                    .performingLab(vo.getPerformingLab())
                    .totalRndCost(vo.getTotalRndCost())
                    .note(vo.getNote())
                    .build();
        }
    }

}
