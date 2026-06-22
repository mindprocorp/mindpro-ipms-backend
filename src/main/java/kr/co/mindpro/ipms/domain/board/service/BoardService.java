package kr.co.mindpro.ipms.domain.board.service;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.board.dto.request.BoardRequest;
import kr.co.mindpro.ipms.domain.board.dto.response.BoardResponse;
import kr.co.mindpro.ipms.domain.board.vo.BoardBackupVO;
import kr.co.mindpro.ipms.domain.board.vo.BoardConfigVO;
import kr.co.mindpro.ipms.domain.board.vo.BoardSystemConfigVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 게시판 도메인 통합 서비스 인터페이스
 */
public interface BoardService {

    // =========================================================================
    // 1. 게시글 관리 (Board Posting)
    // =========================================================================

    /** 게시글 저장 (등록 및 수정 통합) */
    BoardResponse.BoardDetail saveBoard(BoardRequest.BoardDetail request, List<MultipartFile> files);

    /** 게시글 상세 조회 */
    BoardResponse.BoardDetail getBoardDetail(String boardSeq);

    /** 게시글 목록 검색 조회 */
    BaseSearchResponse<BoardResponse.BoardListItem> getBoardList(BaseSearchRequest request);

    /** 게시글 삭제 */
    void deleteBoard(String boardSeq);

    // =========================================================================
    // 2. 게시판 설정 (Board Selection & Configuration)
    // =========================================================================

    /** 메인 페이지용 게시판 트리 + 최신글 통합 조회 */
    List<BoardResponse.BoardMainSection> getBoardMain(int limit);

    /** 게시판 설정 목록 조회 (트리 구조) */
    BaseSearchResponse<BoardConfigVO> getBoardConfigTree(BaseSearchRequest request);

    /** 게시판 설정 생성 */
    void createBoardConfig(BoardRequest.BoardConfig request);

    /** 게시판 설정 수정 */
    void updateBoardConfig(BoardRequest.BoardConfig request);

    /** 게시판 설정 삭제 */
    void deleteBoardConfig(String configSeq);

    /** 게시판 설정 순서 일괄 변경 */
    void updateBoardConfigOrder(List<BoardRequest.BoardConfigOrderItem> items);

    // =========================================================================
    // 3. 게시판 데이터 백업 (Board Backup)
    // =========================================================================

    /** 백업 이력 목록 조회 */
    BaseSearchResponse<BoardBackupVO> getBoardBackupList(BaseSearchRequest request);

    /** 백업 요청 등록 */
    void requestBackup(BoardBackupVO vo);

    @Async
    void processBackupAsync(BoardBackupVO vo);

    /** 백업 상세 조회 */
    BoardBackupVO getBoardBackup(String backupSeq);

    /** 백업 이력 삭제 */
    void deleteBoardBackup(String backupSeq, String userId);

    // =========================================================================
    // 4. 게시판 시스템 상세 설정 (Board System Config)
    // =========================================================================

    void downloadBackupFile(String backupSeq, HttpServletResponse response);

    /** 게시판 시스템 통합 설정 조회 */
    BoardSystemConfigVO getBoardSystemConfig(String officeSeq);

    /** 게시판 시스템 통합 설정 저장 */
    void saveBoardSystemConfig(BoardSystemConfigVO vo);

    /** 게시판 데이터 정리 대상 건수 조회 */
    int getCleanupTargetCount(String officeSeq, String configSeq, int days);

    /** 게시판 데이터 정리 실행 */
    int performCleanup(String officeSeq, String configSeq, int days, String userId);


    // =========================================================================
    // 5. 댓글 관리 (Comment)
    // =========================================================================

    /** 댓글 목록 조회 (트리 구조) */
    List<BoardResponse.CommentItem> getCommentList(String boardSeq);

    /** 댓글 등록 */
    BoardResponse.CommentItem saveComment(String boardSeq, BoardRequest.CommentSave request);

    /** 댓글 수정 */
    BoardResponse.CommentItem updateComment(String commentSeq, BoardRequest.CommentSave request);

    /** 댓글 삭제 */
    void deleteComment(String commentSeq);
}