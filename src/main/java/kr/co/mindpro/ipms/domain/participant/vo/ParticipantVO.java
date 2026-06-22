package kr.co.mindpro.ipms.domain.participant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * 관계자 테이블 매핑 VO
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "관계자 상세 정보")
public class ParticipantVO extends BaseVO {

    private String workCategory;
    @Schema(description = "관계자 일련번호 (PK)", example = "PARTIC20260000005")
    private String participantSeq;

    @Schema(description = "업무 일련번호 (FK)", example = "CFTINF20260000005")
    private String tblSeq;

    @Schema(description = "사용자 정보 일련번호 (FK)", example = "USERIF20260000001")
    private String userInfoSeq;

    @Schema(description = "사무소 일련번호", example = "OFFICE_001")
    private String officeSeq;

    @Schema(description = "대표 관계자 여부 (Y/N)", example = "Y", allowableValues = {"Y", "N"})
    private String mainYn;

    @Schema(description = "관계자 구분 코드 (APP:출원인, INV:발명자 등)", example = "APP")
    private String participantCode;

    @Schema(description = "지분율 (0~100 사이 정수)", example = "100")
    private Integer shareRatio;

    @Schema(description = "성명(한글)", example = "홍길동")
    private String userNameKo;  // 조인을 통해 가져올 필드


}