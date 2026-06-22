package kr.co.mindpro.ipms.domain.jobprogress.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.util.CommonMapping;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 관계자 상세 정보 응답 DTO
 */
@Data
@Builder
@Schema(description = "관계자 상세 정보 응답")
public class JobProgressRequest {

    @Builder
    @Schema(description = "진행사항 등록 요청")
    public record JobProgressDetail(

            // update 로직을 위한 항목. 식별키가 들어왔을 때 수정 로직.
            @Schema(description = "진행사항 식별키")
            String progressSeq,

            @Schema(description = "업무 시퀀스 (부모)", example = "APPMST20260000001")
            String tblSeq,

            @Schema(description = "대상코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            CommonRecordResponse.CodeInfo target,

            @Schema(description = "진행 상태", example = "진행중")
            String progressState,

            /* =========================
             * 통지 / 접수
             * ========================= */


            /** 서류 구분 */
            @Schema(description = "접수서류 시퀀스",example = "{ \"docSeq\": \"10\", \"docName\": \"\" })")
            CommonRecordResponse.DocumentInfo receiptDoc,

            @Schema(description = "제출서류 시퀀스",example = "{ \"docSeq\": \"10\", \"docName\": \"\" })")
            CommonRecordResponse.DocumentInfo submitDoc,

            @CommonMapping(type = "DATE", group = "PRG", description = "통지일")
            @Schema(description = "통지일", example = "20260115")
            String noticeDate,

            @CommonMapping(type = "DATE", group = "PRG", description = "대리인 접수일")
            @Schema(description = "대리인 접수일", example = "20260116")
            String agentReceiptDate,

//            @Schema(description = "접수서류명", example = "거절이유통지서.pdf")
//            String receiptDocName,

            @Schema(description = "접수서류 내용", example = "거절이유통지서 수령 및 분석")
            String receiptDocContent,

            @CommonMapping(type = "PERSON", group = "PRG", description = "심사관")
            @Schema(description = "심사관", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo examiner,

            /* =========================
             * 접수 보고
             * ========================= */

            @CommonMapping(type = "DATE", group = "PRG", description = "접수보고 마감일")
            @Schema(description = "접수보고 마감일", example = "20260120")
            String receiptReportLimitDate,

            @CommonMapping(type = "DATE", group = "PRG", description = "접수보고일")
            @Schema(description = "접수보고일", example = "20260118")
            String receiptReportDate,

            @CommonMapping(type = "PERSON", group = "PRG", description = "접수보고 담당자")
            @Schema(description = "접수보고 담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo receiptReportManager,

            /* =========================
             * 검토
             * ========================= */

            @CommonMapping(type = "DATE", group = "PRG", description = "검토의견 마감일")
            @Schema(description = "검토의견 마감일", example = "20260125")
            String reviewOpinionLimitDate,

            @CommonMapping(type = "DATE", group = "PRG", description = "검토보고일")
            @Schema(description = "검토보고일", example = "20260123")
            String reviewReportDate,

            @CommonMapping(type = "PERSON", group = "PRG", description = "검토보고 담당자")
            @Schema(description = "검토보고 담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo reviewReportManager,

            /* =========================
             * 지시
             * ========================= */

            @CommonMapping(type = "DATE", group = "PRG", description = "지시일")
            @Schema(description = "지시일", example = "20260124")
            String instructionDate,

            @Schema(description = "지시내용", example = "보정서 작성 후 제출 진행 요망")
            String instructionContent,

            /* =========================
             * 기연 / 제출
             * ========================= */

            @Schema(description = "기연 여부/내용", example = "1회 연장")
            String extensionCount,

            @CommonMapping(type = "DATE", group = "PRG", description = "서류 마감일")
            @Schema(description = "서류 마감일", example = "20260205")
            String documentLimitDate,

            @CommonMapping(type = "DATE", group = "PRG", description = "서류 제출일")
            @Schema(description = "서류 제출일", example = "20260203")
            String documentSubmitDate,

//            @Schema(description = "제출서류명", example = "보정서_최종본.pdf")
//            String submitDocName,

            /* =========================
             * 제출 대상 / 담당
             * ========================= */

            @Schema(description = "부서명", example = "특허1팀")
            String deptName,

            @CommonMapping(type = "PERSON", group = "PRG", description = "제출 담당자")
            @Schema(description = "제출 담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo submitManager,

            @Schema(description = "비고", example = "특이사항 없음")
            String note,

            /* =========================
             * 제출 보고
             * ========================= */

            @CommonMapping(type = "DATE", group = "PRG", description = "제출보고 마감일")
            @Schema(description = "제출보고 마감일", example = "20260207")
            String submitReportLimitDate,

            @CommonMapping(type = "DATE", group = "PRG", description = "제출보고일")
            @Schema(description = "제출보고일", example = "20260206")
            String submitReportDate,

            @CommonMapping(type = "PERSON", group = "PRG", description = "제출보고 담당자")
            @Schema(description = "제출보고 담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })")
            CommonRecordResponse.PersonInfo submitReportManager,

            @Schema(description = "삭제할 파일 시퀀스 목록")
            List<String> deleteFileSeqList

    ) {}
}