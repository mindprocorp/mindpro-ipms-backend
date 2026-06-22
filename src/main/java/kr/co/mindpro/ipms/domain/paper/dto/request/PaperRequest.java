package kr.co.mindpro.ipms.domain.paper.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@SuperBuilder
@Schema(description = "서류 일괄 저장 및 수정 요청 객체")
@NoArgsConstructor
public class PaperRequest {

    public record DossierRequest(
            @Schema(description = "파일 맵핑 시퀀스")
            String fileMappSeq,

            @Schema(description = "업무 일련번호 (특허/상표/심판 등)", example = "CFL20260000001")
            String tblSeq,

            @Schema(description = "문서구분 (393: 전자포대 등)", example = "393")
            String docSeq,

            @Schema(description = "서류구분", example = "99")
            String attachDocDiv,

            @Schema(description = "등록일자", example = "2026-02-19T04:42:40.924Z")
            String inputCreateAt,

            @Schema(description = "요약/메모", example = "전자포대 요약입니다.")
            String summary,

            @Schema(description = "삭제할 파일 시퀀스 리스트")
            List<String> deletedFileSeqList
    ) {}

}