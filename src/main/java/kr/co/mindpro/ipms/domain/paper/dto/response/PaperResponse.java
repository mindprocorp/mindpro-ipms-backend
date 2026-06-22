package kr.co.mindpro.ipms.domain.paper.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@SuperBuilder
public class PaperResponse {
    @Schema(description = "업무 일련번호", example = "PAT20260000005")
    private String tblSeq;

    @Schema(description = "사무소 일련번호", example = "OFFICE2026001")
    private String officeSeq;

    @Schema(description = "연결된 파일 매핑 리스트")
    private List<PaperResponseVO> list;

    /**
     * [변환] 조 조회 결과를 응답 객체로 가공
     */
    public static PaperResponse of(String tblSeq, String officeSeq, List<PaperResponseVO> list) {
        return PaperResponse.builder()
                .tblSeq(tblSeq)
                .officeSeq(officeSeq)
                .list(list)
                .build();
    }

    @Builder
    public record DossierDetailResponse(
            @Schema(description = "파일 맵핑 시퀀스")
            String fileMappSeq,

            @Schema(description = "업무(테이블) 시퀀스 (CFL, APP, BILL 등의 PK)", example = "CFT20260000022")
            String tblSeq,

            @Schema(description = "문서Seq", example = "CFT20260000022")
            String docSeq,

            @Schema(description = "문서명")
            String docName,

            @Schema(description = "업로드일/시간")
            String uploadAt,

            @Schema(description = "입력받은 등록일", example = "20260120")
            String inputCreateAt,

            @Schema(description = "파일 종류 코드 (ATTACH_DOC_DIV = 10: 출원서류, 20: 중간서류, 30: 등록서류, 40: 고객서류, 99: 기타서류)", example = "10")
            String fileKindCode,
            String fileKindName,

            @Schema(description = "첨부서류명")
            String fileName,

            @Schema(description = "파일 크기")
            String fileSize,

            @Schema(description = "미리보기 url")
            String fileViewUrl,

            @Schema(description = "파일 다운로드 url")
            String fileDownloadUrl,

            @Schema(description = "요약")
            String summary,

            @Schema(description = "업로드자")
            String uploadUser,

            @Schema(description = "문서코드")
            String docCode,

            @Schema(description = "첨부파일 목록")
            List<DossierFileItem> files,

            @Schema(description = "파일 시퀀스 목록 (콤마 구분)")
            String fileSeqs
    ) {}

    @Builder
    public record DossierFileItem(
            String fileSeq,
            String fileName,
            String fileSize,
            String fileViewUrl,
            String fileDownloadUrl
    ) {}

    @Builder
    public record DossierArchiveListResponse(
            @Schema(description = "상위 테이블 시퀀스")
            String parentSeq,

            @Schema(description = "업무(테이블) 시퀀스 (CFL, APP, BILL 등의 PK)", example = "CFT20260000022")
            String tblSeq,

            @Schema(description = "문서Seq")
            CommonRecordResponse.CodeInfo docInfo,

            @Schema(description = "사건구분")
            CommonRecordResponse.CodeInfo caseClassification,

            @Schema(description = "구분")   // CASE_TYPE 대메뉴 이름?
            CommonRecordResponse.CodeInfo caseCategory,

            /**
             * 이지펫 기준 구분에 개국, PCT, 마드리드, EP 등이 있고 권리에 출원구분(특허,실용신안,디자인,상표 등) 되어있는데
             * 구분에 셀렉박스대로 진행하고 권리에 '마드리드(상표)' <- 이런 방식으로 하려고 함.
             */

            @Schema(description = "권리")     // 출원 권리 말고 고객, 청구서는 빈값, 해외 출원의 경우 개국, PCT, 마드리드 등.
            CommonRecordResponse.CodeInfo rightType,

            @Schema(description = "ourRef")
            String ourRef,

            @Schema(description = "출원번호")
            String appNo,

            @Schema(description = "등록번호")
            String regNo,

            @Schema(description = "업로드일/시간")
            String uploadAt,

            @Schema(description = "문서 코드 - 파일 종류 코드 (ATTACH_DOC_DIV = 10: 출원서류, 20: 중간서류, 30: 등록서류, 40: 고객서류, 99: 기타서류)", example = "10")
            CommonRecordResponse.CodeInfo fileKind,

            @Schema(description = "서류구분...?")
            String docKind,

            @Schema(description = "첨부서류")
            String attachDocName,

            @Schema(description = "파일크기")
            String fileSize,

            @Schema(description = "파일 미리보기")
            String fileViewUrl,

            @Schema(description = "파일 url(다운로드)")
            String fileDownloadUrl,

            @Schema(description = "요약")
            String summary,

            @Schema(description = "업로드자(업로드한 사람)")
            String uploadUser,

            @Schema(description = "파일 시퀀스 목록 (콤마 구분)")
            String fileSeqs
    ) {}
}
