package kr.co.mindpro.ipms.domain.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * 게시판 관련 요청 DTO 클래스 모음
 *
 * @author   : dev
 * @fileName : BoardRequest.java
 * @since    : 2026. 04. 13.
 */
public class BoardRequest {

    /**
     * 게시글 등록/수정 요청 DTO
     */
    @Schema(description = "게시글 등록/수정 요청")
    public record BoardDetail(

            @Schema(description = "게시글 시퀀스 (수정 시 필수, 등록 시 null)", example = "BRD20260413-001")
            String boardSeq,

            @Schema(description = "카테고리 코드", example = "NOTICE")
            @NotBlank(message = "카테고리를 선택해주세요.")
            String categoryCode,

            @Schema(description = "게시글 제목", example = "2026년 1월 공지사항")
            @NotBlank(message = "제목을 입력해주세요.")
            @Size(max = 500, message = "제목은 500자 이내여야 합니다.")
            String title,

            @Schema(description = "게시글 내용")
            String content,

            @Schema(description = "게시글 상태 (PUBLISHED / DRAFT)", example = "PUBLISHED")
            String postStatus,

            @Schema(description = "상단 고정 여부 (Y/N)")
            String isPinned,

            @Schema(description = "고정 시작일시")
            String pinnedStartAt,

            @Schema(description = "고정 종료일시")
            String pinnedEndAt,

            @Schema(description = "삭제할 파일 시퀀스 목록")
            java.util.List<String> deleteFileSeqList
    ) {}

    /**
     * 댓글 등록/수정 요청 DTO
     */
    @Schema(description = "댓글 등록/수정 요청")
    public record CommentSave(

            @Schema(description = "상위 댓글 식별자 (대댓글 시 필수)")
            String parentCommentSeq,

            @Schema(description = "댓글 타입", example = "BOARD")
            String commentType,

            @Schema(description = "댓글 내용")
            @NotBlank(message = "댓글 내용을 입력해주세요.")
            @Size(max = 2000, message = "댓글은 2000자 이내여야 합니다.")
            String commentContent,

            @Schema(description = "댓글 코드")
            String commentCode
    ) {}

    /**
     * 게시판 설정(카테고리/게시판) 생성 및 수정 요청 DTO
     */
    @Schema(description = "게시판 설정 요청")
    public record BoardConfig(
            @Schema(description = "게시판 설정 정보")
            kr.co.mindpro.ipms.domain.board.vo.BoardConfigVO boardConfig,

            @Schema(description = "게시판 마스터(관리자) 목록")
            java.util.List<kr.co.mindpro.ipms.domain.board.vo.BoardMasterVO> masters,

            @Schema(description = "권한 목록. 빈 배열이면 해당 role 전체 허용, 항목이 있으면 지정 대상만 허용")
            java.util.List<kr.co.mindpro.ipms.domain.board.vo.BoardPermissionVO> permissions
    ) {}

    /**
     * 게시판 설정 순서 변경 요청 DTO
     */
    @Schema(description = "게시판 설정 순서 항목")
    public record BoardConfigOrderItem(
            @Schema(description = "게시판 설정 시퀀스")
            String configSeq,

            @Schema(description = "정렬 순서")
            Integer dispOrd,

            @Schema(description = "상위 게시판 설정 시퀀스")
            String parentSeq
    ) {}
}
