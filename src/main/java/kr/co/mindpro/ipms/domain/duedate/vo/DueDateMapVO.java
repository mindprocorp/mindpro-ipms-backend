package kr.co.mindpro.ipms.domain.duedate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

/**
 * 기일 테이블 매핑 객체
 * DB 테이블 Duedate_mst 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : DuedateVO.java
 * @since	 : 2026. 01. 07.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "기일 통합 관리 객체 (Master + Mapping 통합)")
public class DueDateMapVO extends BaseVO {


    @Schema(description = "기일 매핑 일련번호 (Mapp PK)", example = "MAP20260000001")
    private String mappingDuedateSeq;

    @Schema(description = "업무 일련번호 (특허/상표 등)", example = "PAT20260000005")
    private String tblSeq;

    @Schema(description = "업무 코드", example = "PAT20260000005")
    private String tblCode;

    @Schema(description = "사무소 일련번호", example = "OFFICE2026001")
    private String officeSeq;

    private String duedateSeq;




}