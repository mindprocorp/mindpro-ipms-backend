package kr.co.mindpro.ipms.domain.rnd.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

/**
 * @author : seokho
 * @fileName : RndVO.java
 * @since : 2026. 2. 5.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RndVO extends BaseVO {
    @Schema(description = "연결될 출원 식별자")
    private String appSeq;

    @JsonIgnore
    @Schema(description = "사무소 식별자")
    private String officeSeq;

    @Schema(description = "연구과제 식별자")
    private String rndSeq;

    @Schema(description = "프로젝트 번호(과제번호)")
    private String projectNo;

    @Schema(description = "국가부처명")
    private String ministryName;

    @Schema(description = "과제관리(전문)기관명")
    private String agencyName;

    @Schema(description = "연구과제 고유번호(국가연구개발사업번호, 사내연구과제코드 등)")
    private String researchNo;

    @Schema(description = "연구사업명")
    private String bizName;

    @Schema(description = "연구과제 이름")
    private String rndName;

    @Schema(description = "기여율(지분 비율) - (소수점 둘째자리까지 표기)", example = "100.00")
    private double shareRatio;

    @Schema(description = "과제수행기관명(대표연구소)")
    private String mainLab;

    @Schema(description = "참여기관(수행연구소)")
    private String performingLab;

    @Schema(description = "연구과제 시작일자")
    private OffsetDateTime rndStartDate;

    @Schema(description = "연구과제 종료 일자")
    private OffsetDateTime rndClosingDate;

    @Schema(description = "연구비 총액")
    private String totalRndCost;


}
