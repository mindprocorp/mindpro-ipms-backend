package kr.co.mindpro.ipms.domain.duedate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
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
public class DueDateVO extends BaseVO {

    private String workCategory;
    // --- [UtbDuedateMapp 관련 필드 : 관계 정보] ---

    @Schema(description = "기일 매핑 일련번호 (Mapp PK)", example = "MAP20260000001")
    private String mappingDuedateSeq;

    @Schema(description = "업무 일련번호 (특허/상표 등)", example = "PAT20260000005")
    private String tblSeq;

    @Schema(description = "업무 코드", example = "PAT20260000005")
    private String tblCode;

    @Schema(description = "사무소 일련번호", example = "OFFICE2026001")
    private String officeSeq;

    @Schema(description = "사용자 정보 일련번호", example = "USER20260001")
    private String userInfoSeq;


    // --- [UtbDuedateMst 관련 필드 : 실제 기일 본체 데이터] ---

    @Schema(description = "기일 마스터 일련번호 (Mst PK)", example = "DUEMST20260000001")
    private String duedateSeq;

    @Schema(description = "실제 기일 날짜", example = "2026-01-20T15:00:00+09:00")
    private OffsetDateTime duedateDate;

    @Schema(description = "기일 종류 코드 (대분류)", example = "DUE")
    private String duedateKindCode;

    @Schema(description = "기일 상세 구분 코드 (소분류/Key)", example = "DUE_01")
    private String duedateCategoryCode;

    @Schema(description = "기일 완료 여부", allowableValues = {"Y", "N"}, example = "N")
    private String duedateCompleteYn;

    @Schema(description = "알람 설정 코드", example = "ALM_SET_01")
    private String alarmEstablishmentCode;

    @Schema(description = "알람 발송 여부", allowableValues = {"Y", "N"}, example = "Y")
    private String alarmYn;

    @Schema(description = "알람 처리 완료 여부", allowableValues = {"Y", "N"}, example = "N")
    private String alarmCompleteYn;

    @Schema(description = "알람 카테고리 코드", example = "CAT_NOTICE")
    private String alarmCategoryCode;

    @Schema(description = "비고", example = "특이사항 기재")
    private String note;
}