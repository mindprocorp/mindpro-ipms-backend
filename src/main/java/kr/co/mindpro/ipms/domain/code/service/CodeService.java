package kr.co.mindpro.ipms.domain.code.service;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.code.dto.request.CodeRequest;
import kr.co.mindpro.ipms.domain.code.dto.request.GroupCodeRequest;
import kr.co.mindpro.ipms.domain.code.dto.response.CodeDtlResponse;
import kr.co.mindpro.ipms.domain.code.dto.response.CodeMstResponse;
import kr.co.mindpro.ipms.domain.code.dto.response.CodeResponse;
import kr.co.mindpro.ipms.domain.code.dto.response.DocumentResponse;
import kr.co.mindpro.ipms.domain.code.vo.CodeDtlVO;
import kr.co.mindpro.ipms.domain.code.vo.CodeMstVO;
import kr.co.mindpro.ipms.domain.example.dto.request.ExampleCreateRequest;
import org.apache.ibatis.annotations.Param;

/**
 * [Service Interface] 이의심판 관리 서비스
 *
 * @author   : mindpro
 * @fileName : ConflictService.java
 * @since    : 2026. 01. 07.
 */
public interface CodeService {


    void findById(ExampleCreateRequest request);

    List<CodeResponse> getCommonCodeDetail(CodeRequest codeRequest);

    void createAndModifyCommonCode(CodeRequest codeRequest);

    void createAndModifyGroupCommonCode(@Valid GroupCodeRequest groupCodeRequest);

    /** 그룹(코드 분류) 논리 삭제 — 자식 디테일까지 함께 삭제. */
    void deleteCodeMst(String codeSeq);
    
    // 마스터 코드 목록 조회
    List<CodeMstResponse> getCodeMstList(CodeMstVO search);
    
    // 특정 그룹의 상세 코드 목록 조회
    List<CodeDtlResponse> getCodeDtlList(String grpCd);
    
    //     다중 그룹 상세 코드 조회 (Map 형태 반환)
    Map<String, List<CodeDtlResponse>> getCodeDtlMapByGrpCodes(List<String> grpCdList);    
    
    // 상세 코드 일괄 저장 (등록/수정/삭제)
    void saveCodeDtlList(List<CodeDtlVO> dtlList);

    // 서류 항목 조회
    BaseSearchResponse<DocumentResponse> getDocumentList(
            @Param("entryType") String entryType,
            @Param("patType") String patType,
            @Param("docDiv") String docDiv
    );


}