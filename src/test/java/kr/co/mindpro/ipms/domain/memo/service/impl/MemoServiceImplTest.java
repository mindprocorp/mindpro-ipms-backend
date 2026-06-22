package kr.co.mindpro.ipms.domain.memo.service.impl;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.example.dto.response.ExampleDetailResponse;
import kr.co.mindpro.ipms.domain.memo.dto.response.MemoResponse;
import kr.co.mindpro.ipms.domain.memo.service.MemoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemoServiceImplTest {

    @Autowired
    MemoService memoService ;


    @Test
    @DisplayName("이의신청 상세조회")
    void getMemoListByWork() {
        String tblSeq = "12";

        BaseSearchResponse<MemoResponse.MemoDetail> conflictDetail = memoService.getMemoListByWork(tblSeq);
    }

}