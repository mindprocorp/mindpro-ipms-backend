package kr.co.mindpro.ipms.domain.conflict.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
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
    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    @Schema(description = "이의심판 마스터 정보 (순수 심판 고유 필드)")
    public class ConflictResultMstVO extends BaseVO {

    @Schema(description = "심판 결과 시퀀스 (PK)", example = "CFR20260120-001")
    private String officeSeq;

    @Schema(description = "심판 시퀀스 (PK)", example = "CFR20260120-001")
    private String conflictSeq;

    @Schema(description = "심판 결과 시퀀스 (PK)", example = "CFTMST20260000020")
    private String conflictResultSeq;

    @Schema(description = "판결사건번호")
    private String judgmentCaseNo;

    @CommonMapping(type="DATE", group="CONFRES", code="", description="판결일")
    @Schema(description = "판결일", example = "2026-02-22T23:59:59")
    private String judgmentDate;

    @Schema(description = "심결/판결 내용")
    private String judgmentContent;

    @Schema(description = "판결 구분 코드", example = "JTC01")
    private String judgmentCategoryCode;
    private String judgmentCategoryName;

    @Schema(description = "심결문 조회 URL")
    private String judgmentSearchUrl;

    // --- CommonMapping 적용 필드 (일자/관계자) ---

    @CommonMapping(type="DATE", group="CFTRSL", code="", description="판결일")
    @Schema(description = "판결일(종결일)", example = "2026-06-15")
    private String resultDecisionDate;

    @CommonMapping(type="DATE", group="CFTRSL", code="", description="청구일")
    @Schema(description = "청구일", example = "2026-01-20")
    private String resultRequestDate;

    @CommonMapping(type="PERSON", group="CFTRSL", code="", description="청구인")
    @Schema(description = "청구인 (대표 성명)", example = "홍길동")
    private String resultPetitioner;
    @Schema(description = "청구인 (대표 성명)", example = "홍길동")
    private String resultPetitionerName;

    @CommonMapping(type="PERSON", group="CFTRSL", code="", description="피청구인")
    @Schema(description = "피청구인 (대표 성명)", example = "(주)삼성")
    private String resultRespondent;

    @Schema(description = "피청구인 (대표 성명)", example = "(주)삼성")
    private String resultRespondentName;

}
