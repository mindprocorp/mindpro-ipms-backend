package kr.co.mindpro.ipms.domain.conflict.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.util.CommonMapping;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * 의뢰심판 테이블 매핑 객체
 * DB 테이블 ipms_user 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : OppoVO.java
 * @since	 : 2026. 01. 07.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConflictResultViewVO extends BaseVO {
    @Schema(description = "분쟁 일련번호", example = "CFT20260001")
    private String conflictSeq;

    @Schema(description = "판결 결과 일련번호", example = "RES778899")
    private String conflictResultSeq;

    @Schema(description = "사무소 코드", example = "OFFICE_01")
    private String officeSeq;

    @Schema(description = "판결사건번호", example = "2026허12345")
    private String judgmentCaseNo;

    @CommonMapping(type="DATE", group="CFTRSL", code="", description="판결일")
    @Schema(description = "판결일", example = "2026-02-22T23:59:59")
    private String resultDecisionDate;

    @Schema(description = "판결 내용/결정 요지", example = "청구인 승소 (특허 무효 결정)")
    private String judgmentContent;

    // --- 관계자 정보 (추가됨) ---
    @CommonMapping(type="PERSON", group="CFTRSL", code="", description="청구인")
    @Schema(description = "청구인 (대표 성명)", example = "홍길동")
    private String resultPetitionerName;

    @CommonMapping(type="PERSON", group="CFTRSL", code="", description="피청구인")
    @Schema(description = "피청구인 (대표 성명)", example = "(주)삼성")
    private String resultRespondentName;


    @Schema(description = "판결문 경로", example = "htts://www.naver.com")
    private String judgmentSearchUrl;

    @Schema(description = "판결 구분 코드", example = "JTC01")
    private String judgmentCategoryCode;
}