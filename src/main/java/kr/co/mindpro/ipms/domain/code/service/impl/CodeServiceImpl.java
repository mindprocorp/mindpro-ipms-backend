package kr.co.mindpro.ipms.domain.code.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.code.dto.response.DocumentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mindpro.ipms.domain.code.dto.request.CodeRequest;
import kr.co.mindpro.ipms.domain.code.dto.request.GroupCodeRequest;
import kr.co.mindpro.ipms.domain.code.dto.response.CodeDtlResponse;
import kr.co.mindpro.ipms.domain.code.dto.response.CodeMstResponse;
import kr.co.mindpro.ipms.domain.code.dto.response.CodeResponse;
import kr.co.mindpro.ipms.domain.code.repository.db1.CodeMapper;
import kr.co.mindpro.ipms.domain.code.service.CodeService;
import kr.co.mindpro.ipms.domain.code.vo.CodeDtlVO;
import kr.co.mindpro.ipms.domain.code.vo.CodeMstVO;
import kr.co.mindpro.ipms.domain.code.vo.CodeVo;
import kr.co.mindpro.ipms.domain.example.dto.request.ExampleCreateRequest;
import lombok.RequiredArgsConstructor;

/**
 * 이의심판 비즈니스 로직 구현체
 * 
 * @author   : min
 * @fileName : ConflictServiceImpl.java
 * @since    : 2026. 01. 07.
 */
@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {

    private final CodeMapper codeMapper;

    @Override
    public void findById(ExampleCreateRequest request) {

    }

    @Override
    public List<CodeResponse> getCommonCodeDetail(CodeRequest codeRequest) {

        List<CodeVo> commonCodeDetail = codeMapper.getCommonCodeDetail(codeRequest);
        List<CodeResponse> codeResponseList = commonCodeDetail.stream().map(CodeResponse::of).collect(Collectors.toList());
        return codeResponseList;
    }

    @Override
    public void createAndModifyCommonCode(CodeRequest codeRequest) {
        Long codeSeq = codeRequest.getCodeSeq();

        if(codeSeq == null){
            codeMapper.insertCommonCode(codeRequest);
        }else{
            codeMapper.updateCommonCode(codeRequest);
        }

    }

    @Override
    @Transactional
    public void deleteCodeMst(String codeSeq) {
        if (codeSeq == null || codeSeq.isBlank()) {
            throw new IllegalArgumentException("삭제할 그룹 식별자가 필요합니다.");
        }
        String currentUser = currentUserMstSeq();

        // 1) 대상 그룹의 grp_cd 조회 (하위 디테일 일괄 삭제용)
        String grpCd = codeMapper.selectCodeMstList(new CodeMstVO()).stream()
                .filter(m -> codeSeq.equals(m.getCodeSeq()))
                .map(CodeMstVO::getGrpCd)
                .findFirst()
                .orElse(null);

        // 2) 자식 디테일 모두 논리 삭제
        if (grpCd != null && !grpCd.isBlank()) {
            codeMapper.deleteCodeDtlByGrpCd(grpCd, currentUser);
        }
        // 3) 그룹 자체 논리 삭제
        codeMapper.deleteCodeMst(codeSeq, currentUser);
    }

    @Override
    @Transactional
    public void createAndModifyGroupCommonCode(GroupCodeRequest groupCodeRequest) {
        // [통일] utb_code_mst 단일 테이블로 저장. groupCode → grpCd, groupName → cdNm.
        // groupSeq는 신규 식별 용도(null=INSERT, !=null=UPDATE)로만 사용.
        CodeMstVO vo = CodeMstVO.builder()
                .codeSeq(groupCodeRequest.getGroupSeq() != null ? String.valueOf(groupCodeRequest.getGroupSeq()) : null)
                .grpCd(groupCodeRequest.getGroupCode())
                .cdNm(groupCodeRequest.getGroupName())
                .useYn(groupCodeRequest.getUseYn())
                .delYn(groupCodeRequest.getDelYn())
                .note(groupCodeRequest.getNote())
                .build();
        vo.setCreateUser(groupCodeRequest.getCreateUser());
        vo.setUpdateUser(groupCodeRequest.getUpdateUser());

        if (groupCodeRequest.getGroupSeq() == null) {
            // 같은 grp_cd로 재생성 가능. UNIQUE는 partial index(WHERE del_yn='N')로 활성 행에만 적용됨.
            codeMapper.insertCodeMst(vo);
        } else {
            codeMapper.updateCodeMst(vo);
        }
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public List<CodeMstResponse> getCodeMstList(CodeMstVO search) {
        return codeMapper.selectCodeMstList(search).stream()
                .map(CodeMstResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodeDtlResponse> getCodeDtlList(String grpCd) {
        return codeMapper.selectCodeDtlList(grpCd).stream()
                .map(CodeDtlResponse::of)
                .toList();
    }

    @Override
    @Transactional
    public void saveCodeDtlList(List<CodeDtlVO> dtlList) {
        // 현재 로그인 사용자로 audit 필드 자동 세팅 (프론트가 timestamp/사용자 보내지 않음)
        String currentUser = currentUserMstSeq();

        for (CodeDtlVO vo : dtlList) {
            String status = vo.getRowStatus(); // I: 신규, U: 수정, D: 삭제

            if ("I".equals(status)) {
                vo.setCreateUser(currentUser);
                codeMapper.insertCodeDtl(vo);
            } else if ("U".equals(status)) {
                vo.setUpdateUser(currentUser);
                codeMapper.updateCodeDtl(vo);
            } else if ("D".equals(status)) {
                vo.setUpdateUser(currentUser);
                codeMapper.deleteCodeDtl(vo); // 논리 삭제
            }
        }
    }

    /** SecurityContext에서 현재 사용자 user_mst_seq 추출 (없으면 "SYSTEM") */
    private String currentUserMstSeq() {
        try {
            org.springframework.security.core.Authentication auth =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof kr.co.mindpro.ipms.security.vo.CustomUserDetails u) {
                return u.getUserMstSeq();
            }
        } catch (Exception ignore) { }
        return "SYSTEM";
    }   
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, List<CodeDtlResponse>> getCodeDtlMapByGrpCodes(List<String> grpCdList) {
        // 1. DB에서 리스트 조회
        List<CodeDtlVO> voList = codeMapper.selectCodeDtlListByGrpCodes(grpCdList);

        // 2. Stream을 사용하여 Map<String, List<CodeDtlResponse>>으로 그룹화
        return voList.stream()
                .map(CodeDtlResponse::of) // VO를 Response DTO로 변환
                .collect(Collectors.groupingBy(CodeDtlResponse::grpCd)); 
                // Record의 경우 필드명()이 Getter 역할을 합니다.
    }


    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<DocumentResponse> getDocumentList(String entryType, String patType, String docDiv) {
        // 1. Mapper 호출 (파라미터 순서 교정됨)
        List<DocumentResponse> list = codeMapper.selectDocumentList(entryType, patType, docDiv);

        // 2. BaseSearchResponse 객체 생성 및 반환
        // 현재 페이징 처리가 별도로 없다면 list.size()를 전체 카운트로 활용합니다.
        return BaseSearchResponse.of(
                list,
                1,           // 현재 페이지 (기본값)
                list.isEmpty() ? 10 : list.size() // 페이지 사이즈 (기본값)
        );
    }
}