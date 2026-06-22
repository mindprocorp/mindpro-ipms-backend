package kr.co.mindpro.ipms.domain.board.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 게시판 마스터 테이블 매핑 객체
 * DB 테이블 utb_board_mst 의 컬럼과 1:1 대응됩니다.
 * createUser, createAt, updateUser, updateAt, delYn, note 는 BaseVO 상속.
 *
 * @author   : mindpro
 * @fileName : BoardMstVO.java
 * @since    : 2026. 04. 13.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "게시판 마스터 정보")
public class BoardMstVO extends BaseVO {

    @Schema(description = "게시글 시퀀스 (PK)", example = "BRD20260413-001")
    private String boardSeq;

    @Schema(description = "사무소 시퀀스 (FK)", example = "OFFICE_001")
    private String officeSeq;

    @Schema(description = "카테고리 코드", example = "NOTICE")
    private String categoryCode;

    @Schema(description = "카테고리 명칭", example = "공지사항")
    private String categoryCodeName;

    @Schema(description = "게시글 제목", example = "2026년 1월 공지사항")
    private String title;

    @Schema(description = "게시글 내용 (HTML 허용)")
    private String content;

    @Schema(description = "조회수")
    private Integer viewCount;

    @Schema(description = "게시글 상태 (PUBLISHED / DRAFT)", example = "PUBLISHED")
    private String postStatus;

    @Schema(description = "작성자 이름")
    private String createUserName;

    @Schema(description = "작성자 부서명")
    private String createUserDeptName;

    @Schema(description = "작성자 프로필 이미지 URL")
    private String createUserProfileImageUrl;

    @Schema(description = "첨부파일 여부 (파일이 있으면 Y)")
    private String hasFile;

    @Schema(description = "댓글 수")
    private Integer commentCount;

    @Schema(description = "첨부파일 목록")
    private List<BoardFileVO> files;

    @Schema(description = "상단 고정 여부 (Y/N)")
    private String isPinned;

    @Schema(description = "고정 시작일시")
    private String pinnedStartAt;

    @Schema(description = "고정 종료일시")
    private String pinnedEndAt;
}
