package kr.co.mindpro.ipms.domain.board.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.common.file.dto.response.FileResponse;
import kr.co.mindpro.ipms.common.file.service.FileService;
import kr.co.mindpro.ipms.common.util.DataConvertUtil;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.board.dto.request.BoardRequest;
import kr.co.mindpro.ipms.domain.board.dto.response.BoardResponse;
import kr.co.mindpro.ipms.domain.board.repository.db1.BoardMapper;
import kr.co.mindpro.ipms.domain.board.service.BoardService;
import kr.co.mindpro.ipms.domain.board.vo.*;
import kr.co.mindpro.ipms.domain.vo.UtbCommentMappVO;
import kr.co.mindpro.ipms.domain.vo.UtbCommentVO;
import kr.co.mindpro.ipms.domain.paper.service.PaperService;
import kr.co.mindpro.ipms.domain.paper.vo.PaperRequestVO;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 게시판 도메인 통합 비즈니스 로직 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final PaperService paperService;
    private final FileService fileService;
    private final ObjectMapper objectMapper;

    @Autowired
    @Lazy
    private BoardService self;

    // =========================================================================
    // 1. 게시글 관리 (Board Posting)
    // =========================================================================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BoardResponse.BoardDetail saveBoard(BoardRequest.BoardDetail request, List<MultipartFile> files) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        BoardMstVO vo = BoardMstVO.builder()
                .boardSeq(request.boardSeq())
                .officeSeq(officeSeq)
                .categoryCode(request.categoryCode())
                .title(request.title())
                .content(request.content())
                .postStatus(StringUtils.hasText(request.postStatus()) ? request.postStatus() : "PUBLISHED")
                .isPinned(request.isPinned())
                .pinnedStartAt(request.pinnedStartAt())
                .pinnedEndAt(request.pinnedEndAt())
                .createUser(loginUser)
                .updateUser(loginUser)
                .delYn("N")
                .build();

        if (StringUtils.hasText(request.boardSeq())) {
            boardMapper.updateBoardMst(vo);
        } else {
            boardMapper.insertBoardMst(vo);
        }

        // 1. 기존 파일 삭제 처리
        if (request.deleteFileSeqList() != null && !request.deleteFileSeqList().isEmpty()) {
            paperService.softDeleteFilesByFileSeqList(vo.getBoardSeq(), request.deleteFileSeqList());
        }

        // 2. 신규 파일 등록 처리
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    paperService.saveFileMapping(
                            PaperRequestVO.builder()
                                    .officeSeq(officeSeq)
                                    .tblSeq(vo.getBoardSeq())
                                    .tblCode("BRDMST")
                                    .docSeq("99")
                                    .file(file)
                                    .fileCategoryCode("boardAttach")
                                    .createUser(loginUser)
                                    .build()
                    );
                }
            }
        }

        return fetchBoardDetail(vo.getBoardSeq());
    }

    @Override
    @Transactional
    public BoardResponse.BoardDetail getBoardDetail(String boardSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        boardMapper.incrementViewCount(boardSeq, officeSeq);
        return fetchBoardDetail(boardSeq);
    }

    private BoardResponse.BoardDetail fetchBoardDetail(String boardSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        BoardMstVO vo = boardMapper.findBoardBySeq(boardSeq, officeSeq);
        if (vo == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        List<PaperResponseVO> fileList = paperService.getFileMappByWork(boardSeq, officeSeq);
        List<CommonRecordResponse.FileInfo> fileDtoList = CommonRecordResponse.FileInfo.from(fileList);

        return BoardResponse.BoardDetail.builder()
                .boardSeq(vo.getBoardSeq())
                .category(CommonRecordResponse.CodeInfo.builder()
                        .code(vo.getCategoryCode())
                        .codeName(vo.getCategoryCodeName())
                        .build())
                .title(vo.getTitle())
                .content(vo.getContent())
                .viewCount(vo.getViewCount())
                .fileList(fileDtoList)
                .postStatus(vo.getPostStatus())
                .createUser(BoardResponse.CreateUserInfo.builder()
                        .userSeq(vo.getCreateUser())
                        .userName(vo.getCreateUserName())
                        .deptName(vo.getCreateUserDeptName())
                        .profileImageUrl(vo.getCreateUserProfileImageUrl())
                        .build())
                .createAt(DataConvertUtil.formatDateTimeString12(vo.getCreateAt()))
                .updateAt(DataConvertUtil.formatDateTimeString12(vo.getUpdateAt()))
                .isPinned(vo.getIsPinned())
                .pinnedStartAt(vo.getPinnedStartAt())
                .pinnedEndAt(vo.getPinnedEndAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<BoardResponse.BoardListItem> getBoardList(BaseSearchRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        request.setOfficeSeq(officeSeq);
        request.setUserDeptSeq(boardMapper.selectUserDeptSeq(SecurityUtil.getUserInfoSeq(), officeSeq));
        List<BoardMstVO> dbList = boardMapper.selectSearchBoardList(request);
        int totalCount = boardMapper.selectSearchBoardListCount(request);
        List<BoardResponse.BoardListItem> resultList = dbList.stream()
                .map(BoardResponse.BoardListItem::from)
                .toList();
        return BaseSearchResponse.of(resultList, totalCount, request.getPage(), request.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBoard(String boardSeq) {
        boardMapper.deleteBoardMst(boardSeq, SecurityUtil.getOfficeSeq());
    }

    private List<String> parseTags(String tags) {
        if (tags == null || tags.isBlank()) return Collections.emptyList();
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    // =========================================================================
    // 2. 게시판 설정 (Board Selection & Configuration)
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponse.BoardMainSection> getBoardMain(int limit) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userInfoSeq = SecurityUtil.getUserInfoSeq();
        String userDeptSeq = boardMapper.selectUserDeptSeq(userInfoSeq, officeSeq);

        BaseSearchRequest request = new BaseSearchRequest();
        request.setOfficeSeq(officeSeq);
        List<BoardConfigVO> allConfigs = boardMapper.selectBoardConfigList(request);
        for (BoardConfigVO config : allConfigs) {
            List<BoardPermissionVO> permissions = boardMapper.selectBoardPermissionList(config.getConfigSeq());
            config.setPermissions(permissions);
        }

        List<BoardMstVO> recentPosts = boardMapper.selectRecentPostsForMain(officeSeq, limit);
        Map<String, List<BoardResponse.BoardListItem>> postsByConfig = recentPosts.stream()
                .collect(Collectors.groupingBy(
                        BoardMstVO::getCategoryCode,
                        Collectors.mapping(BoardResponse.BoardListItem::from, Collectors.toList())
                ));

        Map<String, List<BoardConfigVO>> childrenByParent = allConfigs.stream()
                .filter(c -> StringUtils.hasText(c.getParentSeq()))
                .collect(Collectors.groupingBy(BoardConfigVO::getParentSeq));

        return allConfigs.stream()
                .filter(c -> !StringUtils.hasText(c.getParentSeq()))
                .map(c -> buildMainSection(c, childrenByParent, postsByConfig, userInfoSeq, userDeptSeq))
                .toList();
    }

    private BoardResponse.BoardMainSection buildMainSection(
            BoardConfigVO config,
            Map<String, List<BoardConfigVO>> childrenByParent,
            Map<String, List<BoardResponse.BoardListItem>> postsByConfig,
            String userInfoSeq,
            String userDeptSeq) {

        List<BoardResponse.BoardMainSection> children =
                childrenByParent.getOrDefault(config.getConfigSeq(), List.of()).stream()
                        .map(c -> buildMainSection(c, childrenByParent, postsByConfig, userInfoSeq, userDeptSeq))
                        .toList();

        boolean canRead = hasPermission(config.getPermissions(), "READ_AUTH", userInfoSeq, userDeptSeq);

        return BoardResponse.BoardMainSection.builder()
                .configSeq(config.getConfigSeq())
                .boardType(config.getBoardType())
                .boardName(config.getBoardName())
                .status(config.getStatus())
                .dispOrd(config.getDispOrd())
                .canWrite(hasPermission(config.getPermissions(), "WRITE_AUTH", userInfoSeq, userDeptSeq))
                .canRead(canRead)
                .recentPosts(canRead ? postsByConfig.getOrDefault(config.getConfigSeq(), List.of()) : List.of())
                .children(children)
                .build();
    }

    @Override
    public BaseSearchResponse<BoardConfigVO> getBoardConfigTree(BaseSearchRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userInfoSeq = SecurityUtil.getUserInfoSeq();
        request.setOfficeSeq(officeSeq);

        String userDeptSeq = boardMapper.selectUserDeptSeq(userInfoSeq, officeSeq);

        List<BoardConfigVO> list = boardMapper.selectBoardConfigList(request);
        for (BoardConfigVO config : list) {
            config.setMasters(boardMapper.selectBoardMasterList(config.getConfigSeq()));
            List<BoardPermissionVO> permissions = boardMapper.selectBoardPermissionList(config.getConfigSeq());
            config.setPermissions(permissions);
            config.setCanWrite(hasPermission(permissions, "WRITE_AUTH", userInfoSeq, userDeptSeq));
            config.setCanRead(hasPermission(permissions, "READ_AUTH", userInfoSeq, userDeptSeq));
        }
        List<BoardConfigVO> tree = buildTree(list);
        return BaseSearchResponse.of(tree, tree.size(), 1, tree.size());
    }

    private boolean hasPermission(List<BoardPermissionVO> permissions, String role, String userInfoSeq, String userDeptSeq) {
        List<BoardPermissionVO> rolePerms = permissions.stream()
                .filter(p -> role.equals(p.getTargetRole()))
                .toList();
        if (rolePerms.isEmpty()) return true;
        return rolePerms.stream().anyMatch(p -> {
            if ("EMPLOYEE".equals(p.getTargetType())) return userInfoSeq.equals(p.getRefSeq());
            if ("DEPT".equals(p.getTargetType())) return p.getRefSeq().equals(userDeptSeq);
            return false;
        });
    }

    private List<BoardConfigVO> buildTree(List<BoardConfigVO> elements) {
        List<BoardConfigVO> result = new ArrayList<>();
        Map<String, BoardConfigVO> map = new HashMap<>();
        for (BoardConfigVO element : elements) {
            element.setChildren(new ArrayList<>());
            map.put(element.getConfigSeq(), element);
        }
        for (BoardConfigVO element : elements) {
            if (StringUtils.hasText(element.getParentSeq())) {
                BoardConfigVO parent = map.get(element.getParentSeq());
                if (parent != null) parent.getChildren().add(element);
                else result.add(element);
            } else {
                result.add(element);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void createBoardConfig(BoardRequest.BoardConfig request) {
        BoardConfigVO vo = request.boardConfig();
        vo.setOfficeSeq(SecurityUtil.getOfficeSeq());
        vo.setCreateUser(SecurityUtil.getUserInfoSeq());
        boardMapper.insertBoardConfig(vo);
        if (request.masters() != null && !request.masters().isEmpty()) {
            for (BoardMasterVO master : request.masters()) {
                master.setConfigSeq(vo.getConfigSeq());
                master.setCreateUser(SecurityUtil.getUserInfoSeq());
                boardMapper.insertBoardMaster(master);
            }
        }
        saveBoardPermissions(vo.getConfigSeq(), request.permissions());
    }

    @Override
    @Transactional
    public void updateBoardConfig(BoardRequest.BoardConfig request) {
        BoardConfigVO vo = request.boardConfig();
        vo.setOfficeSeq(SecurityUtil.getOfficeSeq());
        vo.setUpdateUser(SecurityUtil.getUserInfoSeq());
        boardMapper.updateBoardConfig(vo);
        boardMapper.deleteBoardMasters(vo.getConfigSeq());
        if (request.masters() != null && !request.masters().isEmpty()) {
            for (BoardMasterVO master : request.masters()) {
                master.setConfigSeq(vo.getConfigSeq());
                master.setCreateUser(SecurityUtil.getUserInfoSeq());
                boardMapper.insertBoardMaster(master);
            }
        }
        boardMapper.deleteBoardPermissions(vo.getConfigSeq());
        saveBoardPermissions(vo.getConfigSeq(), request.permissions());
    }

    private void saveBoardPermissions(String configSeq, List<BoardPermissionVO> permissions) {
        if (permissions == null || permissions.isEmpty()) return;
        String createUser = SecurityUtil.getUserInfoSeq();
        for (BoardPermissionVO perm : permissions) {
            perm.setConfigSeq(configSeq);
            perm.setCreateUser(createUser);
            boardMapper.insertBoardPermission(perm);
        }
    }

    @Override
    @Transactional
    public void deleteBoardConfig(String configSeq) {
        boardMapper.deleteBoardConfig(configSeq, SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq());
    }

    @Override
    @Transactional
    public void updateBoardConfigOrder(List<BoardRequest.BoardConfigOrderItem> items) {
        String updateUser = SecurityUtil.getUserInfoSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();
        for (BoardRequest.BoardConfigOrderItem item : items) {
            boardMapper.updateBoardConfigOrder(item.configSeq(), item.dispOrd(), item.parentSeq(), officeSeq, updateUser);
        }
    }

    // =========================================================================
    // 3. 게시판 데이터 백업 (Board Backup)
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<BoardBackupVO> getBoardBackupList(BaseSearchRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        List<BoardBackupVO> list = boardMapper.selectBoardBackupList(officeSeq);
        return BaseSearchResponse.of(list, list.size(), 1, list.size());
    }

    @Override
    @Transactional
    public void requestBackup(BoardBackupVO vo) {
        vo.setStatus("REQUEST");
        boardMapper.insertBoardBackup(vo);
        self.processBackupAsync(vo);
    }

    /**
     * 비동기 데이터 백업 처리 로직 (주석으로 분리)
     * @param vo 백업 요청 정보
     */
    @Async
    @Override
    public void processBackupAsync(BoardBackupVO vo) {
        try {
            vo.setStatus("PROCESSING");
            boardMapper.updateBoardBackupStatus(vo);

            BaseSearchRequest searchReq = new BaseSearchRequest();
            searchReq.setOfficeSeq(vo.getOfficeSeq());
            searchReq.setPageSize(999999);
            
            List<BoardMstVO> boardList = boardMapper.selectBoardBackupData(searchReq);
            byte[] jsonData = objectMapper.writeValueAsBytes(boardList);

            String fileName = "board_backup_" + vo.getBackupSeq() + ".json";
            MultipartFile multipartFile = new JsonMultipartFile(jsonData, fileName);

            // 3. 파일 업로드 (임시 저장소 - temp/board/yyyy/MM/dd 구조 적용)
            FileResponse fileRes = fileService.uploadTempFile(multipartFile, vo.getCreateUser(), "board");

            vo.setStatus("COMPLETED");
            vo.setFileName(fileName);
            vo.setFileSize((long) jsonData.length);
            vo.setFilePath(fileRes.fileUrl());
            vo.setDownloadUrl(fileRes.fileUrl());
            vo.setFileSeq(fileRes.fileSeq());
            boardMapper.updateBoardBackupStatus(vo);
            
        } catch (Exception e) {
            log.error("Backup process failed for seq: {}", vo.getBackupSeq(), e);
            vo.setStatus("FAILED");
            boardMapper.updateBoardBackupStatus(vo);
        }
    }

    @Override
    public BoardBackupVO getBoardBackup(String backupSeq) {
        return boardMapper.selectBoardBackup(backupSeq);
    }

    @Override
    @Transactional
    public void deleteBoardBackup(String backupSeq, String userId) {
        boardMapper.deleteBoardBackup(backupSeq);
    }

    @Override
    public void downloadBackupFile(String backupSeq, HttpServletResponse response) {
        BoardBackupVO vo = boardMapper.selectBoardBackup(backupSeq);
        if (vo == null || vo.getFilePath() == null) {
            throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
        }
        fileService.downloadFile(vo.getFilePath(), response);
    }

    // =========================================================================
    // 4. 게시판 시스템 상세 설정 (Board System Config)
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public BoardSystemConfigVO getBoardSystemConfig(String officeSeq) {
        BoardSystemConfigVO config = boardMapper.selectBoardSystemConfig(officeSeq);
        if (config == null) {
            return BoardSystemConfigVO.builder()
                    .officeSeq(officeSeq)
                    .maxFileSize(500)
                    .maxBodySize(50)
                    .trashRetentionDays("30")
                    .allowMasterChangeYn("Y")
                    .build();
        }
        return config;
    }

    @Override
    @Transactional
    public void saveBoardSystemConfig(BoardSystemConfigVO vo) {
        vo.setOfficeSeq(SecurityUtil.getOfficeSeq());
        vo.setCreateUser(SecurityUtil.getUserInfoSeq());
        vo.setUpdateUser(SecurityUtil.getUserInfoSeq());
        boardMapper.upsertBoardSystemConfig(vo);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCleanupTargetCount(String officeSeq, String configSeq, int days) {
        return boardMapper.countCleanupTargets(officeSeq, configSeq, days);
    }

    @Override
    @Transactional
    public int performCleanup(String officeSeq, String configSeq, int days, String userId) {
        return boardMapper.executeCleanup(officeSeq, configSeq, days, userId);
    }

    // =========================================================================
    // 5. 댓글 관리 (Comment)
    // =========================================================================

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponse.CommentItem> getCommentList(String boardSeq) {
        List<UtbCommentVO> flatList = boardMapper.selectCommentList(boardSeq);
        return buildCommentTree(flatList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BoardResponse.CommentItem saveComment(String boardSeq, BoardRequest.CommentSave request) {
        String loginUser = SecurityUtil.getUserInfoSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();

        BoardMstVO board = boardMapper.findBoardBySeq(boardSeq, officeSeq);
        if (board == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        UtbCommentVO commentVO = UtbCommentVO.builder()
                .parentCommentSeq(request.parentCommentSeq())
                .commentType(StringUtils.hasText(request.commentType()) ? request.commentType() : "BOARD")
                .commentContent(request.commentContent())
                .commentCode(request.commentCode())
                .createUser(loginUser)
                .build();
        boardMapper.insertComment(commentVO);

        UtbCommentMappVO mappVO = UtbCommentMappVO.builder()
                .boardSeq(boardSeq)
                .boardCategoryCode(board.getCategoryCode())
                .commentSeq(commentVO.getCommentSeq())
                .createUser(loginUser)
                .build();
        boardMapper.insertCommentMapp(mappVO);

        return BoardResponse.CommentItem.from(boardMapper.selectComment(commentVO.getCommentSeq()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BoardResponse.CommentItem updateComment(String commentSeq, BoardRequest.CommentSave request) {
        String loginUser = SecurityUtil.getUserInfoSeq();

        UtbCommentVO vo = UtbCommentVO.builder()
                .commentSeq(commentSeq)
                .commentType(request.commentType())
                .commentContent(request.commentContent())
                .commentCode(request.commentCode())
                .updateUser(loginUser)
                .build();
        boardMapper.updateComment(vo);

        return BoardResponse.CommentItem.from(boardMapper.selectComment(commentSeq));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(String commentSeq) {
        String loginUser = SecurityUtil.getUserInfoSeq();
        boardMapper.deleteComment(commentSeq, loginUser);
        boardMapper.deleteCommentMapp(commentSeq, loginUser);
    }

    private List<BoardResponse.CommentItem> buildCommentTree(List<UtbCommentVO> flatList) {
        Map<String, BoardResponse.CommentItem> map = new LinkedHashMap<>();
        for (UtbCommentVO vo : flatList) {
            map.put(vo.getCommentSeq(), BoardResponse.CommentItem.from(vo));
        }
        List<BoardResponse.CommentItem> roots = new ArrayList<>();
        for (UtbCommentVO vo : flatList) {
            BoardResponse.CommentItem item = map.get(vo.getCommentSeq());
            if (StringUtils.hasText(vo.getParentCommentSeq())) {
                BoardResponse.CommentItem parent = map.get(vo.getParentCommentSeq());
                if (parent != null) parent.replies().add(item);
                else roots.add(item);
            } else {
                roots.add(item);
            }
        }
        removeDeletedLeaves(roots);
        return roots;
    }

    private void removeDeletedLeaves(List<BoardResponse.CommentItem> items) {
        items.removeIf(item -> "Y".equals(item.delYn()) && item.replies().isEmpty());
        for (BoardResponse.CommentItem item : items) {
            removeDeletedLeaves(item.replies());
        }
    }

    // =========================================================================
    // 6. 내부 헬퍼 클래스 (Inner Helper Classes)
    // =========================================================================

    /**
     * 내부용 MultipartFile 구현체
     */
    private static class JsonMultipartFile implements MultipartFile {
        private final byte[] content;
        private final String name;

        public JsonMultipartFile(byte[] content, String name) {
            this.content = content;
            this.name = name;
        }

        @Override public String getName() { return "file"; }
        @Override public String getOriginalFilename() { return name; }
        @Override public String getContentType() { return "application/json"; }
        @Override public boolean isEmpty() { return content == null || content.length == 0; }
        @Override public long getSize() { return content.length; }
        @Override public byte[] getBytes() throws IOException { return content; }
        @Override public InputStream getInputStream() throws IOException { return new ByteArrayInputStream(content); }
        @Override public void transferTo(java.io.File dest) throws IOException, IllegalStateException {}
    }

}