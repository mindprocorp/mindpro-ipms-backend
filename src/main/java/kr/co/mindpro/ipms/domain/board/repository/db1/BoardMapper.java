package kr.co.mindpro.ipms.domain.board.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.board.vo.BoardBackupVO;
import kr.co.mindpro.ipms.domain.board.vo.BoardConfigVO;
import kr.co.mindpro.ipms.domain.board.vo.BoardMasterVO;
import kr.co.mindpro.ipms.domain.board.vo.BoardMstVO;
import kr.co.mindpro.ipms.domain.board.vo.BoardPermissionVO;
import kr.co.mindpro.ipms.domain.board.vo.BoardSystemConfigVO;
import kr.co.mindpro.ipms.domain.vo.UtbCommentMappVO;
import kr.co.mindpro.ipms.domain.vo.UtbCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 게시판 도메인 완전 통합 데이터 접근 인터페이스
 * (게시글, 게시판 설정, 백업 관리)
 */
@Mapper
public interface BoardMapper {

    // =========================================================================
    // 1. 게시글 관리 (Board Posting)
    // =========================================================================

    /** 게시글 등록 */
    int insertBoardMst(BoardMstVO vo);

    /** 게시글 수정 */
    int updateBoardMst(BoardMstVO vo);

    /** 게시글 단건 조회 (조회수 증가 포함) */
    BoardMstVO findBoardBySeq(@Param("boardSeq") String boardSeq, @Param("officeSeq") String officeSeq);

    /** 게시글 목록 검색 조회 (동적 검색 조건) */
    List<BoardMstVO> selectSearchBoardList(@Param("request") BaseSearchRequest request);

    /** 게시글 목록 전체 건수 조회 */
    int selectSearchBoardListCount(@Param("request") BaseSearchRequest request);

    /** 게시글 삭제 (소프트 딜리트) */
    void deleteBoardMst(@Param("boardSeq") String boardSeq, @Param("officeSeq") String officeSeq);

    /** 조회수 증가 */
    void incrementViewCount(@Param("boardSeq") String boardSeq, @Param("officeSeq") String officeSeq);


    // =========================================================================
    // 2. 게시판 설정 (Board Selection & Configuration)
    // =========================================================================

    /** 게시판 설정 목록 조회 (트리 구조용) */
    List<BoardConfigVO> selectBoardConfigList(@Param("request") BaseSearchRequest request);

    /** 게시판 설정 상세 조회 */
    BoardConfigVO selectBoardConfig(@Param("configSeq") String configSeq);

    /** 게시판 설정 등록 */
    int insertBoardConfig(BoardConfigVO vo);

    /** 게시판 설정 수정 */
    int updateBoardConfig(BoardConfigVO vo);

    /** 게시판 설정 삭제 (논리 삭제) */
    void deleteBoardConfig(@Param("configSeq") String configSeq, @Param("officeSeq") String officeSeq, @Param("updateUser") String updateUser);

    /** 게시판 설정 순서 업데이트 */
    int updateBoardConfigOrder(@Param("configSeq") String configSeq, @Param("dispOrd") int dispOrd, @Param("parentSeq") String parentSeq, @Param("officeSeq") String officeSeq, @Param("updateUser") String updateUser);

    /** 게시판별 최신글 N개 일괄 조회 (메인 페이지용) */
    List<BoardMstVO> selectRecentPostsForMain(@Param("officeSeq") String officeSeq, @Param("limit") int limit);

    /** 게시판 마스터(관리자) 목록 조회 */
    List<BoardMasterVO> selectBoardMasterList(@Param("configSeq") String configSeq);

    /** 게시판 마스터 일괄 삭제 */
    int deleteBoardMasters(@Param("configSeq") String configSeq);

    /** 게시판 마스터 등록 */
    int insertBoardMaster(BoardMasterVO vo);

    /** 게시판 권한 목록 조회 */
    List<BoardPermissionVO> selectBoardPermissionList(@Param("configSeq") String configSeq);

    /** 현재 유저의 부서 seq 조회 */
    String selectUserDeptSeq(@Param("userInfoSeq") String userInfoSeq, @Param("officeSeq") String officeSeq);

    /** 게시판 권한 일괄 삭제 */
    int deleteBoardPermissions(@Param("configSeq") String configSeq);

    /** 게시판 권한 등록 */
    int insertBoardPermission(BoardPermissionVO vo);


    // =========================================================================
    // 3. 게시판 데이터 백업 (Board Backup)
    // =========================================================================

    /** 백업 이력 목록 조회 */
    List<BoardBackupVO> selectBoardBackupList(@Param("officeSeq") String officeSeq);

    /** 백업 이력 상세 조회 */
    BoardBackupVO selectBoardBackup(@Param("backupSeq") String backupSeq);

    /** 백업 요청 등록 */
    int insertBoardBackup(BoardBackupVO vo);

    /** 백업 상태 및 결과 업데이트 */
    int updateBoardBackupStatus(BoardBackupVO vo);

    /** 백업 이력 삭제 (논리 삭제) */
    int deleteBoardBackup(@Param("backupSeq") String backupSeq);

    /** 백업용 전체 데이터 상세 조회 (첨부파일 포함) */
    List<BoardMstVO> selectBoardBackupData(@Param("request") BaseSearchRequest request);

    // =========================================================================
    // 4. 게시판 시스템 상세 설정 (Board System Config)
    // =========================================================================

    /** 게시판 시스템 통합 설정 조회 */
    BoardSystemConfigVO selectBoardSystemConfig(@Param("officeSeq") String officeSeq);

    /** 게시판 시스템 통합 설정 저장 (Upsert) */
    int upsertBoardSystemConfig(BoardSystemConfigVO vo);

    /** 게시판 데이터 정리 대상 건수 조회 */
    int countCleanupTargets(@Param("officeSeq") String officeSeq, @Param("configSeq") String configSeq, @Param("days") int days);

    /** 게시판 데이터 정리 실행 (휴지통 이동) */
    int executeCleanup(@Param("officeSeq") String officeSeq, @Param("configSeq") String configSeq, @Param("days") int days, @Param("updateUser") String updateUser);


    // =========================================================================
    // 5. 댓글 관리 (Comment)
    // =========================================================================

    /** 댓글 등록 */
    int insertComment(UtbCommentVO vo);

    /** 댓글-게시글 매핑 등록 */
    int insertCommentMapp(UtbCommentMappVO vo);

    /** 댓글 단건 조회 */
    UtbCommentVO selectComment(@Param("commentSeq") String commentSeq);

    /** 게시글 댓글 목록 조회 */
    List<UtbCommentVO> selectCommentList(@Param("boardSeq") String boardSeq);

    /** 댓글 수정 */
    int updateComment(UtbCommentVO vo);

    /** 댓글 삭제 (소프트 딜리트) */
    int deleteComment(@Param("commentSeq") String commentSeq, @Param("updateUser") String updateUser);

    /** 댓글 매핑 삭제 (소프트 딜리트) */
    int deleteCommentMapp(@Param("commentSeq") String commentSeq, @Param("updateUser") String updateUser);
}