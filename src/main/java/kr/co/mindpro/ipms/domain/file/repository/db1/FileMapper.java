package kr.co.mindpro.ipms.domain.file.repository.db1;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.file.vo.DocumentMstVO;
import kr.co.mindpro.ipms.domain.file.vo.FileMstVO;

import java.util.List;

/**
 * 파일 관리 데이터 접근 인터페이스 (DB1)
 */
@Mapper
public interface FileMapper {

    /**
     * 파일 마스터 정보 등록 (utb_file_mst)
     * @param mstVO 파일 마스터 정보
     * @return 등록된 행 수
     */
    int insertFileMaster(FileMstVO mstVO);

    /**
     * 파일 논리 삭제 처리 (del_yn = 'Y')
     * @param fileSeq 파일 일련번호 (PK)
     * @param userId 수정자 ID
     * @return 수정된 행 수
     */
    int updateDeleteYn(@Param("fileSeq") String fileSeq, @Param("userId") String userId);
    
    /**
     * 저장소 구분 코드(typeDir)를 기반으로 저장소 시퀀스 조회
     * @param repositoryKind (예: REC, SUB, UPL 등)
     * @return file_repository_seq
     */
    String selectRepositorySeqByKind(@Param("repositoryKind") String repositoryKind);
    
    /**
     * 서류 마스터 정보 조회 (경로 생성을 위해 entry_type, doc_div 필요)
     * @param docSeq 서류 일련번호
     * @return DocumentMstVO
     */
    DocumentMstVO selectDocumentMstById(@Param("docSeq") Long docSeq);

    /**
     * 여러 파일 시퀀스로 파일 마스터 정보(원본 파일명 및 S3 키 조합) 조회
     * @param fileSeqs 파일 시퀀스 리스트
     * @return 파일 마스터 정보 리스트
     */
    List<FileMstVO> selectFilesBySeqs(@Param("fileSeqs") List<String> fileSeqs);
}
