package kr.co.mindpro.ipms.domain.conflict.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.conflict.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 이의심판 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 */
@Mapper
public interface ConflictMapper {

    int ConflictTotalCount(String officeSeq);

    /**
     * 분쟁 마스터 저장
     */
    int insertConflictMst(ConflictMstVO vo);

    /**
     * 분쟁 마스터 수정 (추가)
     */
    int updateConflictMst(ConflictMstVO vo);

    /**
     * 분쟁 마스터 상세 조회 (통합 MergeVO)
     */
    ConflictMergeVO findConflictBySeq(@Param("conflictSeq") String conflictSeq, @Param("officeSeq") String officeSeq);

    /**
     * 분쟁 마스터 리스트 조회 (일반/기타 통합)
     */
    List<ConflictMergeVO> findConflictMstList(
            @Param("officeSeq") String officeSeq,
            @Param("etcYn") String etcYn,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );

    void insertConflictMstFromMerge(ConflictMergeVO mergeVO);

    /**
     * 동적 검색 조건을 기반으로 컨플릭트 리스트 조회
     */
    List<ConflictMergeVO> selectSearchConflictList(
            @Param("request") BaseSearchRequest request

    );

    /* =========================================================================
     * 원심하급심 (결과 내역) 관련
     * ========================================================================= */

    /**
     * 결과 정보 등록
     */
    int insertConflictResult(ConflictResultMstVO vo);

    /**
     * 결과 정보 수정 (추가)
     */
    int updateConflictResult(ConflictResultMstVO vo);

    /**
     * 결과 목록 조회
     */
    List<ConflictResultMstVO> selectConflictResultList(
            @Param("officeSeq") String officeSeq,
            @Param("conflictSeq") String conflictSeq
    );

    /**
     * 결과 단건 상세 조회 (추가 - 서비스 레이어에서 저장 후 조회 시 필요)
     */
    Optional<ConflictResultMstVO> selectConflictResultDetail(
            @Param("conflictResultSeq") String conflictResultSeq,
            @Param("officeSeq") String officeSeq
    );

    /**
     * 분쟁 마스터 삭제
     */
    void deleteConflictMst(@Param("conflictSeq") String conflictSeq, @Param("officeSeq") String officeSeq);

    /**
     * 원심하급심 결과 목록 삭제 (저장 전 기존 데이터 제거용)
     */
    void deleteConflictResultByConflictSeq(@Param("conflictSeq") String conflictSeq, @Param("officeSeq") String officeSeq);

    /**
     * 연결된 출원 마스터의 권리(right_category) / 출원인관리번호(retain_seq) 만 부분 갱신.
     * 이의심판 화면에서 두 값은 utb_conflict_mst 가 아닌 utb_app_mst 에 저장되므로,
     * 기존 출원을 사용하는 경우 form 값과 동기화하기 위해 호출한다.
     */
    int updateAppRightAndClientRef(
            @Param("appSeq") String appSeq,
            @Param("officeSeq") String officeSeq,
            @Param("rightTypeCode") String rightTypeCode,
            @Param("clientRef") String clientRef,
            @Param("updateUser") String updateUser
    );

    /**
     * 이미지 파일 논리 삭제
     */
    int softDeleteConflictFileByFileSeq(
            @Param("officeSeq") String officeSeq,
            @Param("fileSeq") String fileSeq,
            @Param("updateUser") String updateUser
    );
}