package kr.co.mindpro.ipms.domain.gracePeriod.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * @author : seokho
 * @fileName : GracePeriodVO.java
 * @since : 2026. 2. 3.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class GracePeriodVO extends BaseVO {

    @Schema(description = "출원 seq", example = "PAT20260000005")
    private String appSeq;

    @Schema(description = "사무소 seq", example = "PGOKR20260000002")
    private String officeSeq;

    @Schema(description = "공지예외 식별자", example = "GRCPRD20260000001")
    private String gracePeriodSeq;

    @Schema(description = "공지예외 주장 내용")
    private String gracePeriodContentCode;

    @Schema(description = "공지예외 주장 내용")
    private String gracePeriodContentName;

    @Schema(description = "제출 마감일자", example = "2026-02-06")
    private OffsetDateTime submitDeadLineDate;

    @Schema(description = "제출일", example = "2026-02-06")
    private OffsetDateTime submitClosingDate;

    @Schema(description = "공지예외 주장일", example = "2026-02-06")
    private OffsetDateTime gracePeriodDate;
}
