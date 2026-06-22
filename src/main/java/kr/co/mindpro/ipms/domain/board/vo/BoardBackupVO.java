package kr.co.mindpro.ipms.domain.board.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 게시판 백업 이력 테이블 매핑 객체
 * DB 테이블 utb_board_backup_hist 의 컬럼과 1:1 대응됩니다.
 *
 * @author   : mindpro
 * @fileName : BoardBackupVO.java
 * @since    : 2026. 04. 13.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "게시판 백업 이력 정보")
public class BoardBackupVO extends BaseVO {

    @Schema(description = "백업 시퀀스 (PK)")
    private String backupSeq;

    @Schema(description = "사무소 시퀀스 (FK)")
    private String officeSeq;

    @Schema(description = "대상 게시판 설정 시퀀스 (ALL 이면 전체)")
    private String configSeq;

    @Schema(description = "게시판 명칭 (백업 당시)")
    private String boardName;

    @Schema(description = "백업 상태 (REQUEST, PROCESSING, COMPLETED, FAILED)")
    private String status;

    @Schema(description = "백업 파일 저장 경로 (S3 Key)")
    private String filePath;

    @Schema(description = "파일 크기 (Byte)")
    private Long fileSize;

    @Schema(description = "파일 명칭")
    private String fileName;

    @Schema(description = "백업 파일 시퀀스 (utb_file_repository FK)")
    private String fileSeq;

    @Schema(description = "직접 다운로드 URL")
    private String downloadUrl;

    @Schema(description = "요청자 명칭")
    private String requestUserName;
}
