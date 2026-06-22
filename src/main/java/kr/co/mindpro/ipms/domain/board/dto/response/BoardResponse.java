package kr.co.mindpro.ipms.domain.board.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.util.DataConvertUtil;
import kr.co.mindpro.ipms.domain.board.vo.BoardMstVO;
import kr.co.mindpro.ipms.domain.vo.UtbCommentVO;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시판 관련 응답 DTO 클래스 모음
 *
 * @author   : dev
 * @fileName : BoardResponse.java
 * @since    : 2026. 04. 13.
 */
public class BoardResponse {

    /**
     * 게시판 전용 작성자 정보 (부서명, 프로필 이미지 URL 포함)
     */
    @Builder
    @Schema(description = "게시판 작성자 정보")
    public record CreateUserInfo(
            @Schema(description = "사용자 시퀀스") String userSeq,
            @Schema(description = "사용자 성명") String userName,
            @Schema(description = "부서명") String deptName,
            @Schema(description = "프로필 이미지 URL") String profileImageUrl
    ) {}

    /**
     * 게시글 목록 아이템 DTO
     */
    @Builder
    @Schema(description = "게시글 목록 아이템")
    public record BoardListItem(
            @Schema(description = "게시글 시퀀스") String boardSeq,
            @Schema(description = "카테고리") CommonRecordResponse.CodeInfo category,
            @Schema(description = "제목") String title,
            @Schema(description = "조회수") Integer viewCount,
            @Schema(description = "첨부파일 여부") String hasFile,
            @Schema(description = "댓글 수") Integer commentCount,
            @Schema(description = "게시글 상태") String postStatus,
            @Schema(description = "작성자") CommonRecordResponse.PersonInfo createUser,
            @Schema(description = "작성일") String createAt,
            @Schema(description = "상단 고정 여부") String isPinned,
            @Schema(description = "고정 시작일시") String pinnedStartAt,
            @Schema(description = "고정 종료일시") String pinnedEndAt
    ) {
        public static BoardListItem from(BoardMstVO vo) {
            return BoardListItem.builder()
                    .boardSeq(vo.getBoardSeq())
                    .category(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getCategoryCode())
                            .codeName(vo.getCategoryCodeName())
                            .build())
                    .title(vo.getTitle())
                    .viewCount(vo.getViewCount())
                    .hasFile(vo.getHasFile())
                    .commentCount(vo.getCommentCount())
                    .postStatus(vo.getPostStatus())
                    .createUser(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getCreateUser())
                            .userName(vo.getCreateUserName())
                            .build())
                    .createAt(DataConvertUtil.formatDateTimeString12(vo.getCreateAt()))
                    .isPinned(vo.getIsPinned())
                    .pinnedStartAt(vo.getPinnedStartAt())
                    .pinnedEndAt(vo.getPinnedEndAt())
                    .build();
        }
    }

    /**
     * 게시글 상세 응답 DTO
     * 파일 목록은 공통 CommonRecordResponse.FileInfo 를 재사용합니다.
     */
    @Builder
    @Schema(description = "게시글 상세")
    public record BoardDetail(
            @Schema(description = "게시글 시퀀스") String boardSeq,
            @Schema(description = "카테고리") CommonRecordResponse.CodeInfo category,
            @Schema(description = "제목") String title,
            @Schema(description = "내용") String content,
            @Schema(description = "조회수") Integer viewCount,
            @Schema(description = "첨부파일 목록") List<CommonRecordResponse.FileInfo> fileList,
            @Schema(description = "게시글 상태") String postStatus,
            @Schema(description = "작성자") CreateUserInfo createUser,
            @Schema(description = "작성일") String createAt,
            @Schema(description = "수정일") String updateAt,
            @Schema(description = "상단 고정 여부") String isPinned,
            @Schema(description = "고정 시작일시") String pinnedStartAt,
            @Schema(description = "고정 종료일시") String pinnedEndAt
    ) {}

    /**
     * 메인 페이지용 게시판 섹션 DTO (트리 + 최신글 포함)
     */
    @Builder
    @Schema(description = "메인 게시판 섹션")
    public record BoardMainSection(
            @Schema(description = "게시판 설정 시퀀스") String configSeq,
            @Schema(description = "게시판 타입 (CATEGORY/BOARD/LINK)") String boardType,
            @Schema(description = "게시판명") String boardName,
            @Schema(description = "상태") String status,
            @Schema(description = "정렬 순서") Integer dispOrd,
            @Schema(description = "쓰기 권한 여부") Boolean canWrite,
            @Schema(description = "읽기 권한 여부") Boolean canRead,
            @Schema(description = "최신 게시글 목록") List<BoardListItem> recentPosts,
            @Schema(description = "하위 섹션") List<BoardMainSection> children
    ) {}

    /**
     * 댓글 응답 DTO
     */
    @Builder
    @Schema(description = "댓글")
    public record CommentItem(
            @Schema(description = "댓글 식별자") String commentSeq,
            @Schema(description = "상위 댓글 식별자") String parentCommentSeq,
            @Schema(description = "댓글 타입") String commentType,
            @Schema(description = "댓글 내용") String commentContent,
            @Schema(description = "댓글 코드") String commentCode,
            @Schema(description = "작성자") CreateUserInfo createUser,
            @Schema(description = "작성일") String createAt,
            @Schema(description = "수정일") String updateAt,
            @Schema(description = "삭제 여부") String delYn,
            @Schema(description = "대댓글 목록") List<CommentItem> replies
    ) {
        public static CommentItem from(UtbCommentVO vo) {
            boolean deleted = "Y".equals(vo.getDelYn());
            return CommentItem.builder()
                    .commentSeq(vo.getCommentSeq())
                    .parentCommentSeq(vo.getParentCommentSeq())
                    .commentType(vo.getCommentType())
                    .commentContent(deleted ? null : vo.getCommentContent())
                    .commentCode(deleted ? null : vo.getCommentCode())
                    .createUser(deleted ? null : CreateUserInfo.builder()
                            .userSeq(vo.getCreateUser())
                            .userName(vo.getCreateUserName())
                            .deptName(vo.getCreateUserDeptName())
                            .profileImageUrl(vo.getCreateUserProfileImageUrl())
                            .build())
                    .createAt(DataConvertUtil.formatDateTimeString12(vo.getCreateAt()))
                    .updateAt(DataConvertUtil.formatDateTimeString12(vo.getUpdateAt()))
                    .delYn(vo.getDelYn())
                    .replies(new ArrayList<>())
                    .build();
        }
    }
}
