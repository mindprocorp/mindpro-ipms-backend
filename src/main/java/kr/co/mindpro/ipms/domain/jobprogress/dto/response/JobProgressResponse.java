package kr.co.mindpro.ipms.domain.jobprogress.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.util.CommonMapping;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import lombok.Builder;
import lombok.Data;


import java.util.List;
import java.util.stream.Collectors;

/**
 * 관계자 상세 정보 응답 DTO
 */
@Data
@Builder
@Schema(description = "관계자 상세 정보 응답")
public class JobProgressResponse {
    /**
     * 진행사항 목록 응답
     */


    /**
     * 진행사항 목록 wrapper (페이징 대응용)
     */
    @Builder
    @Schema(description = "진행사항 목록")
    public record JobProgressListResponse(

            @Schema(description = "진행사항 목록")
            List<JobProgressDetail> list,

            int totalCount
    ) {}


/**
 * 진행사항 상세 응답
 */
    @Builder
    @Schema(description = "진행사항 상세 응답")
    public record JobProgressDetail(

            @Schema(description = "진행사항 일련번호", example = "PRG202600001")
            String progressSeq,

            /* --- 통지 / 접수 --- */
            @Schema(description = "통지일", example = "20260115")
            String noticeDate,

            @Schema(description = "대리인 접수일", example = "20260116")
            String agentReceiptDate,

            @Schema(description = "서류 목록")
            List<CommonRecordResponse.FileInfo> PaperFiles,


            /** 서류 구분 */
            @Schema(description = "접수서류 시퀀스",example = "{ \"docSeq\": \"10\", \"docName\": \"\" })")
            CommonRecordResponse.DocumentInfo receiptDoc,

            @Schema(description = "제출서류 시퀀스",example = "{ \"docSeq\": \"10\", \"docName\": \"\" })")
            CommonRecordResponse.DocumentInfo submitDoc,

//            @Schema(description = "접수서류명", example = "거절이유통지서.pdf")
//            String receiptDocName,
//            @Schema(description = "제출서류명", example = "보정서_최종본.pdf")
//            String submitDocName,
//            @Schema(description = "제출서류 시퀀스",example = "10")
//            String submitDocSeq, // 이 필드에 실제 파일을 담아서 보냅니다.
//            @Schema(description = "접수서류 시퀀스",example = "10")
//            String receiptDocSeq, // 이 필드에 실제 파일을 담아서 보냅니다.

            @CommonMapping(type = "PERSON", group = "PRG", description = "심사관")
            @Schema(description = "심사관 정보")
            CommonRecordResponse.PersonInfo examiner,

            @Schema(description = "접수서류 내용", example = "거절이유통지서 수령")
            String receiptDocContent,

            /* --- 접수 보고 --- */
            @Schema(description = "접수보고 마감일", example = "20260120")
            String receiptReportLimitDate,

            @Schema(description = "접수보고일", example = "20260118")
            String receiptReportDate,

            // String -> PersonInfo
            @CommonMapping(type = "PERSON", group = "PRG", description = "접수보고 담당자")
            @Schema(description = "접수보고 담당자 정보")
            CommonRecordResponse.PersonInfo receiptReportManager,

            /* --- 검토 --- */
            @Schema(description = "검토의견 마감일", example = "20260125")
            String reviewOpinionLimitDate,

            @Schema(description = "검토보고일", example = "20260123")
            String reviewReportDate,

            // String -> PersonInfo
            @CommonMapping(type = "PERSON", group = "PRG", description = "검토보고 담당자")
            @Schema(description = "검토보고 담당자 정보")
            CommonRecordResponse.PersonInfo reviewReportManager,

            /* --- 지시 --- */
            @Schema(description = "지시일", example = "20260124")
            String instructionDate,

            @Schema(description = "지시내용", example = "보정서 작성 후 제출 진행")
            String instructionContent,

            /* --- 기연 / 제출 --- */
            @Schema(description = "기연 여부", example = "1")
            String extensionCount,

            @Schema(description = "서류 마감일", example = "20260205")
            String documentLimitDate,

            @Schema(description = "서류 제출일", example = "20260203")
            String documentSubmitDate,

//            @Schema(description = "접수서류 파일 상세")
//            ProgressFileInfo receiptPaperFile,

            /* --- 제출 대상 / 담당 --- */
            @Schema(description = "제출 대상", example = "특허심판원")
            CommonRecordResponse.CodeInfo target,

            @Schema(description = "부서", example = "특허1팀")
            String deptName,

            // String -> PersonInfo
            @CommonMapping(type = "PERSON", group = "PRG", description = "제출 담당자")
            @Schema(description = "제출 담당자 정보")
            CommonRecordResponse.PersonInfo submitManager,

            @Schema(description = "비고", example = "기연 1회 적용")
            String note,

            /* --- 제출 보고 --- */
            @Schema(description = "제출보고 마감일", example = "20260207")
            String submitReportLimitDate,

            @Schema(description = "제출보고일", example = "20260206")
            String submitReportDate,

            // String -> PersonInfo
            @CommonMapping(type = "PERSON", group = "PRG", description = "제출보고 담당자")
            @Schema(description = "제출보고 담당자 정보")
            CommonRecordResponse.PersonInfo submitReportManager
    ) {}

    @Builder
    public record ProgressFileInfo(
            String fileName,
            String fileSize,
            String fileUrl,
            String fileSeq,
            String docSeq,
            String docName
    ) {
        // 특정 카테고리의 파일을 찾는 정적 메서드
        public static List<ProgressFileInfo> from(List<PaperResponseVO> fileList) {
            if (fileList == null || fileList.isEmpty()) return List.of();

            // fileSeq를 key로 하여 중복 제거 (이미 있는 파일이면 무시)
            return fileList.stream()
                    .filter(f -> f.getFileSeq() != null)
                    .collect(Collectors.toMap(
                            PaperResponseVO::getFileSeq, // Key
                            f -> ProgressFileInfo.builder() // Value
                                    .fileName(f.getFileName())
                                    .fileSize(f.getFileDisplaySize())
                                    .fileUrl(f.getDownloadUrl())
                                    .fileSeq(f.getFileSeq())
                                    .docSeq(f.getDocSeq())
                                    .docName(f.getDocName())
                                    .build(),
                            (existing, replacement) -> existing // 중복 시 기존(첫 번째) 데이터 유지
                    ))
                    .values()
                    .stream()
                    .toList();
        }
    }



    /**
     * 등록 / 수정 응답
     */
    @Builder
    @Schema(description = "진행사항 저장 응답")
    public record JobProgressSaveResponse(

            @Schema(description = "진행사항 일련번호", example = "PRG202600001")
            String progressSeq

    ) {}

}