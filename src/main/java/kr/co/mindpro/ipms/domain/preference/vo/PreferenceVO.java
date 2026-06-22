package kr.co.mindpro.ipms.domain.preference.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.util.CommonMapping;
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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PreferenceVO extends BaseVO {


    /* =========================
     * PK
     * ========================= */
    @Schema(description = "출원 일련번호", example = "APP202600001")
    private String appSeq;

    @Schema(description = "사무소 일련번호", example = "OFFICE001")
    private String officeSeq;

    @Schema(description = "우선권 일련번호", example = "PREF202600001")
    private String preferenceSeq;

    /* =========================
     * 우선권 정보
     * ========================= */
    @Schema(description = "선출원 국가 코드", example = "JP")
    private String priorCountryCode;

    @CommonMapping(type = "DATE", group = "PREFER",code="", description = "우선권 주장일")
    @Schema(description = "우선권 주장일", example = "20260115")
    private String preferenceAssertDate;

    @Schema(description = "우선권 번호", example = "JP2025-123456")
    private String preferenceNo;

    @Schema(description = "WIPO 분류 코드", example = "A01B")
    private String wipoCategoryCode;

    @Schema(description = "우선권 조사 여부", example = "Y")
    private String preferenceSearch;

    @Schema(description = "원문 URL", example = "https://wipo.int/example")
    private String fullContentUrl;

    /* =========================
     * 기한
     * ========================= */
    @CommonMapping(type = "DATE", group = "PREFER",code="", description = "접수일")
    @Schema(description = "접수일", example = "20260110")
    private String preferenceRegDate;

    @CommonMapping(type = "DATE", group = "PREFER",code="", description = "제출 마감일")
    @Schema(description = "제출 마감일", example = "20260210")
    private String submitDeadLineDate;

    @CommonMapping(type = "DATE", group = "PREFER",code="", description = "제출 완료일")
    @Schema(description = "제출 완료일", example = "20260205")
    private String submitClosingDate;

    @Schema(description = "비고", example = "일본 우선권")
    private String note;

}