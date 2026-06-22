package kr.co.mindpro.ipms.domain.code.service.impl;

import kr.co.mindpro.ipms.domain.code.dto.request.CodeRequest;
import kr.co.mindpro.ipms.domain.code.dto.request.GroupCodeRequest;
import kr.co.mindpro.ipms.domain.code.service.CodeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
@Transactional
class CodeServiceImplTest {

    @Autowired
    CodeService codeService;

    @Test
    @DisplayName("공통코드 조회")
    void getCommonCodeDetail() {
        CodeRequest codeRequest = new CodeRequest();
        //codeRequest.setGroupCode("CODE1");
        //codeRequest.setCode(Arrays.asList("A01"));
        codeRequest.setGroupCode(Arrays.asList("CODE1","GROUP1"));
        codeService.getCommonCodeDetail(codeRequest);
    }

    @Test
    @DisplayName("공통코드 등록/수정")
    @Commit
    void createCommonCode() {
        CodeRequest codeRequest = new CodeRequest();
        codeRequest.setGroupCode(Arrays.asList("CODE2"));
        codeRequest.setGroupName("그룹코드3");
        //codeRequest.setCode("B01");
        codeRequest.setCodeName("사과1");
        codeRequest.setCodeSeq(4L);
        codeRequest.setDelYn("Y");
        codeService.createAndModifyCommonCode(codeRequest);
    }

    @Test
    @Commit
    @DisplayName("그룹코드 등록/수정")
    void createAndModifyGroupCommonCode() {
        GroupCodeRequest groupCodeRequest = new GroupCodeRequest();
        groupCodeRequest.setGroupCode("GROUP1");
        groupCodeRequest.setGroupName("그룹명");
        codeService.createAndModifyGroupCommonCode(groupCodeRequest);
    }
}