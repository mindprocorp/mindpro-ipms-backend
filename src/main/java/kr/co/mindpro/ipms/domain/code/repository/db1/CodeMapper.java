package kr.co.mindpro.ipms.domain.code.repository.db1;

import java.util.List;

import kr.co.mindpro.ipms.domain.code.dto.response.DocumentResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.code.dto.request.CodeRequest;
import kr.co.mindpro.ipms.domain.code.dto.request.GroupCodeRequest;
import kr.co.mindpro.ipms.domain.code.vo.CodeDtlVO;
import kr.co.mindpro.ipms.domain.code.vo.CodeMstVO;
import kr.co.mindpro.ipms.domain.code.vo.CodeVo;

/**
 * 공통코드 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : CodeMapper.java
 * @since	 : 2026. 01. 07.
 */
@Mapper
public interface CodeMapper {
    List<CodeVo> getCommonCodeDetail(CodeRequest codeRequest);

    void insertCommonCode(CodeRequest codeRequest);

    void updateCommonCode(CodeRequest codeRequest);

    void insertGroupCommonCode(GroupCodeRequest groupCodeRequest);

    void updateGroupCommonCode(GroupCodeRequest groupCodeRequest);
    
    // --- 마스터 코드 관련 ---
    List<CodeMstVO> selectCodeMstList(CodeMstVO search);
    int insertCodeMst(CodeMstVO vo);
    int updateCodeMst(CodeMstVO vo);
    /** 그룹 논리 삭제. */
    int deleteCodeMst(@Param("codeSeq") String codeSeq, @Param("updateUser") String updateUser);
    /** 그룹 삭제 시 자식 디테일 일괄 논리 삭제. */
    int deleteCodeDtlByGrpCd(@Param("grpCd") String grpCd, @Param("updateUser") String updateUser);

    // --- 상세 코드 관련 ---
    List<CodeDtlVO> selectCodeDtlList(String grpCd);
    int insertCodeDtl(CodeDtlVO vo);
    int updateCodeDtl(CodeDtlVO vo);
    int deleteCodeDtl(CodeDtlVO vo); // 논리 삭제(del_yn = 'Y')
    
    /**
     * 여러 그룹 코드에 해당하는 상세 코드 목록 조회
     * @param grpCdList 그룹 코드 리스트 (예: ["USE_YN", "USER_ROLE"])
     */    
    List<CodeDtlVO> selectCodeDtlListByGrpCodes(@Param("grpCdList") List<String> grpCdList);




    List<DocumentResponse> selectDocumentList(
            @Param("entryType") String entryType,
            @Param("patType") String patType,
            @Param("docDiv") String docDiv
    );
}