package kr.co.mindpro.ipms.domain.conflict.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.util.CommonMapping;
import kr.co.mindpro.ipms.domain.conflict.vo.ConflictResultMstVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 이의심판 생성 request
 */
@Data
@Builder
@NoArgsConstructor

public class ConflictResultResponse {
    public record ConflictList(
    @Schema(description = "심판 결과 시퀀스 (PK)", example = "CFR20260120-001")
    String officeSeq,

                    @Schema(description = "심판 시퀀스 (PK)", example = "CFR20260120-001")
     String conflictSeq,

    @Schema(description = "심판 결과 시퀀스 (PK)", example = "CFTMST20260000020")
     String conflictResultSeq,

    @Schema(description = "판결사건번호")
     String judgmentCaseNo,

    @CommonMapping(type="DATE", group="CONFRES", code="", description="판결일")
    @Schema(description = "판결일", example = "2026-02-22T23:59:59")
     String judgmentDate,

    @Schema(description = "심결/판결 내용")
     String judgmentContent,

    @Schema(description = "판결 구분 코드", example = "JTC01")
    CommonRecordResponse.CodeInfo judgmentCategoryCode,

    @Schema(description = "심결문 조회 URL")
     String judgmentSearchUrl,

    // --- CommonMapping 적용 필드 (일자/관계자) ---

    @CommonMapping(type="DATE", group="CFTRSL", code="", description="판결일")
    @Schema(description = "판결일(종결일)", example = "2026-06-15")
     String resultDecisionDate,

    @CommonMapping(type="DATE", group="CFTRSL", code="", description="청구일")
    @Schema(description = "청구일", example = "2026-01-20")
     String resultRequestDate,

    @CommonMapping(type="PERSON", group="CFTRSL", code="", description="청구인")
    @Schema(description = "청구인 (대표 성명)", example = "홍길동")
     String resultPetitionerName,

    @CommonMapping(type="PERSON", group="CFTRSL", code="", description="피청구인")
    @Schema(description = "피청구인 (대표 성명)", example = "(주)삼성")
     String resultRespondentName
    ){}




}


