package kr.co.mindpro.ipms.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.board.dto.request.BoardRequest;
import kr.co.mindpro.ipms.domain.board.dto.response.BoardResponse;
import kr.co.mindpro.ipms.domain.board.service.BoardService;
import kr.co.mindpro.ipms.domain.board.vo.BoardBackupVO;
import kr.co.mindpro.ipms.domain.board.vo.BoardConfigVO;
import kr.co.mindpro.ipms.domain.board.vo.BoardSystemConfigVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Tag(name = "게시판 API", description = "게시판 통합 API")
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // =========================================================================
    // 1. 게시글 관리. (Board Posting)
    // =========================================================================

    @Operation(summary = "게시글 저장", description = "게시글을 등록하거나 수정합니다. boardSeq가 있으면 수정, 없으면 등록합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BoardResponse.BoardDetail>> saveBoard(
            @RequestPart("data") @Valid BoardRequest.BoardDetail data,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        BoardResponse.BoardDetail result = boardService.saveBoard(data, files);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "게시글이 저장되었습니다.", result));
    }

    @Operation(summary = "게시글 목록 검색 조회")
    @PostMapping("/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<BoardResponse.BoardListItem>>> getBoardList(
            @RequestBody BaseSearchRequest request) {

        BaseSearchResponse<BoardResponse.BoardListItem> response = boardService.getBoardList(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "게시글 목록 조회 성공", response));
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{boardSeq}")
    public ResponseEntity<ApiResponse<BoardResponse.BoardDetail>> getBoardDetail(@PathVariable String boardSeq) {
        BoardResponse.BoardDetail detail = boardService.getBoardDetail(boardSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공", detail));
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{boardSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@PathVariable String boardSeq) {
        boardService.deleteBoard(boardSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "삭제 성공", null));
    }

    // =========================================================================
    // 2. 게시판 설정 (Board Selection & Configuration)
    // =========================================================================

    @Operation(summary = "메인 페이지 게시판 통합 조회", description = "게시판 트리와 각 게시판별 최신글 N개를 한 응답으로 반환합니다.")
    @GetMapping("/main")
    public ResponseEntity<ApiResponse<List<BoardResponse.BoardMainSection>>> getBoardMain(
            @RequestParam(defaultValue = "6") int limit) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공", boardService.getBoardMain(limit)));
    }

    @Operation(summary = "게시판 설정 트리 조회")
    @PostMapping("/config/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<BoardConfigVO>>> getBoardConfigTree(@RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공", boardService.getBoardConfigTree(request)));
    }

    @Operation(summary = "게시판 설정 생성")
    @PostMapping("/config")
    public ResponseEntity<ApiResponse<Void>> createBoardConfig(@RequestBody BoardRequest.BoardConfig reqDTO) {
        boardService.createBoardConfig(reqDTO);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "저장 성공", null));
    }

    @Operation(summary = "게시판 설정 수정")
    @PutMapping("/config")
    public ResponseEntity<ApiResponse<Void>> updateBoardConfig(@RequestBody BoardRequest.BoardConfig reqDTO) {
        boardService.updateBoardConfig(reqDTO);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "수정 성공", null));
    }

    @Operation(summary = "게시판 설정 순서 변경")
    @PutMapping("/config/order")
    public ResponseEntity<ApiResponse<Void>> updateBoardConfigOrder(@RequestBody List<BoardRequest.BoardConfigOrderItem> items) {
        boardService.updateBoardConfigOrder(items);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "순서 변경 성공", null));
    }

    @Operation(summary = "게시판 설정 삭제")
    @DeleteMapping("/config/{configSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteBoardConfig(@PathVariable String configSeq) {
        boardService.deleteBoardConfig(configSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "삭제 성공", null));
    }

    // =========================================================================
    // 3. 게시판 시스템 상세 설정 (Board System Config)
    // =========================================================================


    @Operation(summary = "게시판 시스템 통합 설정 조회")
    @GetMapping("/config/system")
    public ResponseEntity<ApiResponse<BoardSystemConfigVO>> getSystemConfig() {
        String officeSeq = SecurityUtil.getOfficeSeq();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공", boardService.getBoardSystemConfig(officeSeq)));
    }

    @Operation(summary = "게시판 시스템 통합 설정 저장")
    @PostMapping("/config/system")
    public ResponseEntity<ApiResponse<Void>> saveSystemConfig(@RequestBody BoardSystemConfigVO vo) {
        boardService.saveBoardSystemConfig(vo);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "저장 성공", null));
    }

    // =========================================================================
    // 4. 게시판 데이터 백업 (Board Backup)
    // =========================================================================

    @Operation(summary = "백업 이력 목록 조회")
    @PostMapping("/backup/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<BoardBackupVO>>> getBackupList(@RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공", boardService.getBoardBackupList(request)));
    }

    @Operation(summary = "백업 요청 등록")
    @PostMapping("/backup/request")
    public ResponseEntity<ApiResponse<Void>> requestBackup(@RequestBody BoardBackupVO vo) {
        vo.setOfficeSeq(SecurityUtil.getOfficeSeq());
        vo.setCreateUser(SecurityUtil.getUserInfoSeq());
        boardService.requestBackup(vo);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "백업 요청 성공", null));
    }

    @Operation(summary = "백업 이력 삭제")
    @DeleteMapping("/backup/{backupSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteBackup(@PathVariable String backupSeq) {
        String userId = SecurityUtil.getUserInfoSeq();
        boardService.deleteBoardBackup(backupSeq, userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "삭제 성공", null));
    }

    @Operation(summary = "백업 파일 다운로드")
    @GetMapping("/backup/download/{backupSeq}")
    public void downloadBackup(@PathVariable String backupSeq, HttpServletResponse response) {
        boardService.downloadBackupFile(backupSeq, response);
    }

    // =========================================================================
    // 5. 게시판 데이터 정리 (Board Cleanup)
    // =========================================================================

    @Operation(summary = "정리 대상 게시글 건수 조회")
    @GetMapping("/cleanup/count")
    public ResponseEntity<ApiResponse<Integer>> getCleanupCount(
            @RequestParam(required = false, defaultValue = "ALL") String configSeq,
            @RequestParam int days) {
        
        String officeSeq = SecurityUtil.getOfficeSeq();
        int count = boardService.getCleanupTargetCount(officeSeq, configSeq, days);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공", count));
    }

    @Operation(summary = "게시글 일괄 정리 실행")
    @PostMapping("/cleanup/execute")
    public ResponseEntity<ApiResponse<Integer>> executeCleanup(
            @RequestParam(required = false, defaultValue = "ALL") String configSeq,
            @RequestParam int days) {

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();
        int count = boardService.performCleanup(officeSeq, configSeq, days, userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "정리 성공", count));
    }

    // =========================================================================
    // 6. 댓글 관리 (Comment)
    // =========================================================================

    @Operation(summary = "댓글 목록 조회")
    @GetMapping("/{boardSeq}/comment")
    public ResponseEntity<ApiResponse<List<BoardResponse.CommentItem>>> getCommentList(@PathVariable String boardSeq) {
        List<BoardResponse.CommentItem> result = boardService.getCommentList(boardSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "댓글 목록 조회 성공", result));
    }

    @Operation(summary = "댓글 등록")
    @PostMapping("/{boardSeq}/comment")
    public ResponseEntity<ApiResponse<BoardResponse.CommentItem>> saveComment(
            @PathVariable String boardSeq,
            @RequestBody @Valid BoardRequest.CommentSave request) {
        BoardResponse.CommentItem result = boardService.saveComment(boardSeq, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "댓글이 등록되었습니다.", result));
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/comment/{commentSeq}")
    public ResponseEntity<ApiResponse<BoardResponse.CommentItem>> updateComment(
            @PathVariable String commentSeq,
            @RequestBody @Valid BoardRequest.CommentSave request) {
        BoardResponse.CommentItem result = boardService.updateComment(commentSeq, request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "댓글이 수정되었습니다.", result));
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comment/{commentSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable String commentSeq) {
        boardService.deleteComment(commentSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "댓글이 삭제되었습니다.", null));
    }
}