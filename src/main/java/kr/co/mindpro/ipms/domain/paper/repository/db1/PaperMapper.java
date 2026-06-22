package kr.co.mindpro.ipms.domain.paper.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.paper.dto.request.PaperRequest;
import kr.co.mindpro.ipms.domain.paper.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 청구서 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : PaperMapper.java
 * @since	 : 2026. 01. 07.
 */
@Mapper
public interface PaperMapper {
    /** * [조회] 특정 업무 시퀀스에 연결된 모든 활성 파일 매핑 리스트
     */
    List<PaperResponseVO> findAllByWork(@Param("tblSeq") String tblSeq, @Param("officeSeq") String officeSeq);

    /**
     * [조회] 전자포대 탭 단건 상세조회
     * */
    List<PaperDossierVO> getDossierDetail(@Param("officeSeq") String officeSeq, @Param("tblSeq") String tblSeq, @Param("fileMappSeq") String fileMappSeq);

    /** * [조회] 사무소별 파일 매핑 전체 리스트 (관리자용 등)
     */
    List<PaperRequestVO> findListByOfficeSeq(@Param("officeSeq") String officeSeq);

    /**
     * [조회] 업무별 전자포대(파일 매칭) 리스트 조회
     * */
    List<PaperDossierVO> findAllByWorkDossier(@Param("tblSeq") String tblSeq, @Param("officeSeq") String officeSeq);

    /** * [저장] 신규 파일 매핑 등록 (utb_file_mapp 단건 인서트)
     */
    int insert(PaperMstVO vo);

    /** * [삭제] 업무 단위 파일 매핑 일괄 논리 삭제 (Soft Delete)
     * - 특정 사건의 모든 첨부파일 관계를 해제할 때 사용
     */
    int deleteByWork(PaperRequestVO vo);

    /** * [삭제] 동일 조건의 기존 매핑만 선택적 삭제
     * - 동일한 파일 종류(Type/Kind/Category)가 등록될 때 기존 것만 교체하기 위함
     */
    int deleteExistingMapping(PaperRequestVO vo);

    List<PaperDossierArchiveVO> findDossierArchiveByOffice(@Param("request") BaseSearchRequest request);

    int CntDossierArchiveByOffice(@Param("request") BaseSearchRequest request);

    int getDuplicateDossierCnt(@Param("vo") PaperMstVO vo);

    int updateDossier(@Param("vo") PaperMstVO vo);

    int softDeleteDossier(@Param("officeSeq") String officeSeq,
                          @Param("tblSeq") String tblSeq,
                          @Param("fileMappSeq") String fileMappSeq,
                          @Param("userSeq") String userSeq);

    /** * [삭제] 전자포대 다건 논리 삭제 */
    int softDeleteDossierList(@Param("officeSeq") String officeSeq,
                              @Param("tblSeq") String tblSeq,
                              @Param("fileMappSeqList") List<String> fileMappSeqList,
                              @Param("userSeq") String userSeq);

    /** * [삭제] 전자포대 단건 물리 삭제 */
    int hardDeleteDossier(@Param("officeSeq") String officeSeq,
                          @Param("tblSeq") String tblSeq,
                          @Param("fileMappSeq") String fileMappSeq);

    /** * [삭제] 전자포대 다건 물리 삭제 */
    int hardDeleteDossierList(@Param("officeSeq") String officeSeq,
                              @Param("tblSeq") String tblSeq,
                              @Param("fileMappSeqList") List<String> fileMappSeqList);

    void softDeleteFilesByTblSeq(@Param("officeSeq") String officeSeq,
                                @Param("tblSeq") String tblSeq,
                                @Param("fileKindCode") String fileKindCode,
                                @Param("updateUser") String updateUser);

    void softDeleteFilesByFileSeqList(@Param("officeSeq") String officeSeq,
                                     @Param("tblSeq") String tblSeq,
                                     @Param("fileSeqList") List<String> fileSeqList,
                                     @Param("updateUser") String updateUser);

    /** [삭제] fileSeq만으로 이미지 단건 논리 삭제 (tblSeq 불필요) */
    void softDeleteByFileSeq(@Param("officeSeq") String officeSeq,
                             @Param("fileSeq") String fileSeq,
                             @Param("updateUser") String updateUser);

    void insertFileMapping(PaperMstVO mappVo);

    String getNewFileMappSeq();
}