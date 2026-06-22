        package kr.co.mindpro.ipms.domain.example.service.impl;

import kr.co.mindpro.ipms.domain.example.dto.enums.ExampleCourtCategoryCode;
import kr.co.mindpro.ipms.domain.example.dto.request.ExampleListRequest;
import kr.co.mindpro.ipms.domain.example.dto.request.base.ExampleAppCaseMng;
import kr.co.mindpro.ipms.domain.example.dto.request.base.ExampleAppBaseInfo;
import kr.co.mindpro.ipms.domain.example.dto.request.base.ExampleAppManagerInfo;
import kr.co.mindpro.ipms.domain.example.dto.request.ExampleCreateRequest;
import kr.co.mindpro.ipms.domain.example.dto.response.ExampleDetailResponse;
import kr.co.mindpro.ipms.domain.example.dto.response.ExampleList;
import kr.co.mindpro.ipms.domain.example.dto.response.ExampleListResponse;
import kr.co.mindpro.ipms.domain.example.repository.db1.ExampleMapper;
import kr.co.mindpro.ipms.domain.example.service.ExampleService;
import kr.co.mindpro.ipms.domain.example.vo.ExampleMstVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

        /**
 * 이의심판 비즈니스 로직 구현체
 * 
 * @author   : min
 * @fileName : ConflictServiceImpl.java
 * @since    : 2026. 01. 07.
 */
@Service
@RequiredArgsConstructor
public class ExampleServiceImpl implements ExampleService {

    private final ExampleMapper conflictMapper;


    @Override
    public void createAndModifyConflict(ExampleCreateRequest request) {

        // 출원사건관리
        ExampleAppManagerInfo conflictAppManagerInfo = request.getOppoAppManagerInfo();
        // 출원기본정보
        ExampleAppBaseInfo conflictAppBaseInfo = request.getOppoAppBaseInfo();
        // 담당정보
        ExampleAppCaseMng conflictAppCaseMng = request.getAppCaseMng();
        // 1. 이의심판 마스터 INSERT

        ExampleCourtCategoryCode conflictCourtCategoryCode = request.getConflictCourtCategoryCode();

        ExampleMstVO conflictMstVO = new ExampleMstVO();
        conflictMstVO.setCourtCategoryCode(conflictCourtCategoryCode);
        conflictMapper.insertConflictMst(
                conflictMstVO
        );

        // 2.






    }

    @Override
    public ExampleDetailResponse getConflictDetail(String confictSeq) {
        ExampleDetailResponse conflictDetailResponse = new ExampleDetailResponse();

        ExampleMstVO conflictMstVO = conflictMapper.findConflictBySeq(confictSeq);

        // ENUM 을 db 컬럼과 매핑가져오는방법
        ExampleCourtCategoryCode courtCategoryCode = conflictMstVO.getCourtCategoryCode();

        // 출원사건관리
        ExampleAppCaseMng appCaseMng = ExampleAppCaseMng.from(conflictMstVO);
        conflictDetailResponse.setConflictAppCaseMng(appCaseMng);

        // 출원기본정보
        ExampleAppBaseInfo conflictAppBaseInfo = ExampleAppBaseInfo.from(conflictMstVO);
        conflictDetailResponse.setConflictAppBaseInfo(conflictAppBaseInfo);

        // 담당정보
        ExampleAppManagerInfo conflictAppManagerInfo = ExampleAppManagerInfo.from(conflictMstVO);
        conflictDetailResponse.setConflictAppManagerInfo(conflictAppManagerInfo);

        return conflictDetailResponse;
    }

    @Override
    public ExampleListResponse getConflictList(ExampleListRequest conflictListRequest) {
        ExampleListResponse conflictListResponse = new ExampleListResponse();

        List<ExampleList> conflictList = conflictMapper.findConflictMstList();

        conflictListResponse.setConflictList(conflictList);
        return conflictListResponse;

    }

    @Override
    public String getTest() {
        return "123123123123";
    }
}