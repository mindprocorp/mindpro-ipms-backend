package kr.co.mindpro.ipms.domain.example.service.impl;

import kr.co.mindpro.ipms.domain.example.dto.request.ExampleCreateRequest;
import kr.co.mindpro.ipms.domain.example.dto.response.ExampleDetailResponse;
import kr.co.mindpro.ipms.domain.example.service.ExampleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ConflictServiceImplTest {

    @Autowired
    ExampleService conflictService;

    @Test
    @DisplayName("이의신청 상세조회")
    void getConflictDetail() {
        String conflictSeq = "12";
        ExampleDetailResponse conflictDetail = conflictService.getConflictDetail(conflictSeq);
    }

    @Test
    @DisplayName("이의신청 등록")
    void createConflict() {
        ExampleCreateRequest conflictCreateRequest = new ExampleCreateRequest();
        conflictService.createAndModifyConflict(conflictCreateRequest);
    }
}