package kr.co.mindpro.ipms.domain.board.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시판 첨부파일 정보 VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "게시판 첨부파일 정보")
public class BoardFileVO {

    @Schema(description = "파일 시퀀스")
    private String fileSeq;

    @Schema(description = "파일명")
    private String fileName;

    @Schema(description = "다운로드 URL")
    private String downloadUrl;
}
