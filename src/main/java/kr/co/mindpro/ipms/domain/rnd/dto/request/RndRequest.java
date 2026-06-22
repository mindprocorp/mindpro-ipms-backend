package kr.co.mindpro.ipms.domain.rnd.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.rnd.vo.RndVO;
import lombok.Data;

import java.time.OffsetDateTime;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.parseToOffsetDateTime;


/**
 * @author : seokho
 * @fileName : RndRequest.java
 * @since : 2026. 2. 5.
 */
@Data
public class RndRequest {
    public record RnbRequestDetail(
            @Schema(description = "연구과제 식별자")
            String rndSeq,

            @Schema(description = "연결된 출원 식별자")
            String appSeq,

            @Schema(description = "연구과제 고유번호(국가연구개발사업번호, 사내연구과제코드 등)", example = "12-123-1234")
            String researchNo,

            @Schema(description = "프로젝트 번호(과제번호)", example = "123321")
            String projectNo,

            @Schema(description = "국가부처명", example = "특허청")
            String ministryName,

            @Schema(description = "과제관리(전문)기관명", example = "관리_마프")
            String agencyName,

            @Schema(description = "연구사업명", example = "MIND_ipms")
            String bizName,

            @Schema(description = "연구과제명", example = "ipms_test")
            String rndName,

            @Schema(description = "기여율(지분 비율) - (소수점 둘째자리까지 표기)", example = "100.00")
            double shareRatio,

            @Schema(description = "과제수행기관명(대표연구소)", example = "마프")
            String mainLab,

            @Schema(description = "참여기관(수행연구소)", example = "마프_연구소")
            String performingLab,

            @Schema(description = "연구과제 시작일자", example = "2026-02-05")
            String rndStartDate,

            @Schema(description = "연구과제 종료 일자", example = "2026-02-10")
            String rndClosingDate,

            @Schema(description = "연구비 총액", example = "150000")
            String totalRndCost,

            @Schema(description = "메모", example = "test_note")
            String note
    ) {
        public static RndVO setRndVO(RndRequest.RnbRequestDetail request) {
            return RndVO.builder()
                    .appSeq(request.appSeq)
                    .researchNo(request.researchNo)
                    .projectNo(request.projectNo)
                    .ministryName(request.ministryName)
                    .agencyName(request.agencyName)
                    .bizName(request.bizName)
                    .rndName(request.rndName)
                    .shareRatio(request.shareRatio)
                    .mainLab(request.mainLab)
                    .performingLab(request.performingLab)
                    .rndStartDate(parseToOffsetDateTime(request.rndStartDate))
                    .rndClosingDate(parseToOffsetDateTime(request.rndClosingDate))
                    .totalRndCost(request.totalRndCost)
                    .note(request.note)
                    .build();
        }
    }
}
