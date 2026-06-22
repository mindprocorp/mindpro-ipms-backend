package kr.co.mindpro.ipms.common.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.jobprogress.dto.response.JobProgressResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Schema(description = "페이징 응답 공통 래퍼")
public class CommonRecordResponse {
    @Schema(description = "사람 정보 (ID 및 성명)")
    @Builder
    public record PersonInfo(
            @Schema(description = "사용자 시퀀스/ID", example = "USERIF20260000002")
            String userSeq,

            @Schema(description = "사용자 성명", example = "홍길동")
            String userName
    ) {}

    @Schema(description = "공통 코드 정보 (code 및 name)")
    @Builder
    public record CodeInfo(
            @Schema(description = "공통코드 코드", example = "10")
            String code,

            @Schema(description = "공통코드 이름", example = "출원")
            String codeName
    ) {}

    @Schema(description = "document 코드 정보 (code 및 name)")
    @Builder
    public record DocumentInfo(
            @Schema(description = "document seq", example = "20")
            String docSeq,

            @Schema(description = "document 이름", example = "제출서류")
            String docName
    ) {}

    @Builder
    public record FileInfo(
            @Schema(description = "파일 시퀀스" , format = "SEQ")
            String fileSeq,

            @Schema(description = "파일 이름")
            String fileName,

            @Schema(description = "파일 크기")
            String fileSize,

            @Schema(description = "파일 경로")
            String fileUrl,

            @Schema(description = "docSeq")
            String docSeq,

            @Schema(description = "docNm")
            String docNm,

            @Schema(description = "등록 일시")
            String createAt
    ) {
        // 특정 카테고리의 파일을 찾는 정적 메서드
        public static List<FileInfo> from(List<PaperResponseVO> fileList) {
            if (fileList == null) return Collections.emptyList();

            return fileList.stream()
                    // 1. 필수 값인 fileSeq가 없는 데이터는 제외
                    .filter(f -> f.getFileSeq() != null)
                    // 2. fileSeq를 키로 사용하여 중복 제거 및 데이터 변환
                    .collect(java.util.stream.Collectors.toMap(
                            PaperResponseVO::getFileSeq,
                            f -> FileInfo.builder()
                                    .fileSeq(f.getFileSeq())
                                    .fileName(f.getFileName())
                                    .fileSize(f.getFileDisplaySize())
                                    .fileUrl(f.getDownloadUrl())
                                    .docSeq(f.getDocSeq())
                                    .docNm(f.getDocName())
                                    .createAt(kr.co.mindpro.ipms.common.util.DataConvertUtil.formatOffsetDateTime(f.getCreateAt()))
                                    .build(),
                            (existing, replacement) -> existing // 중복 키 발생 시 첫 번째 데이터 유지
                    ))
                    // 3. Map의 Value들만 뽑아서 다시 List로 변환
                    .values()
                    .stream()
                    .toList();
        }
    }
    @Builder
    public record CustomerInfo(
            String customerSeq,
            String customerName
    ){

    }

    @Builder
    public record CounterPartyInfo(
            @Schema(description = "당사자 시퀀스", example = "CUSTMR20260000010")
            String counterPartySeq,

            @Schema(description = "당사자 이름", example = "마인드프로10")
            String counterPartyName,

            @Schema(description = "당사자 메모", example = "마인드프로10 메모메모")
            String counterPartyNote,

            @Schema(description = "당사자 순번", example = "1")
            String counterPartyOrderNo
    ) {}

}