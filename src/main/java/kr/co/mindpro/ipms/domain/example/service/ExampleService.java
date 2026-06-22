package kr.co.mindpro.ipms.domain.example.service;

import kr.co.mindpro.ipms.domain.example.dto.request.ExampleCreateRequest;
import kr.co.mindpro.ipms.domain.example.dto.request.ExampleListRequest;
import kr.co.mindpro.ipms.domain.example.dto.response.ExampleDetailResponse;
import kr.co.mindpro.ipms.domain.example.dto.response.ExampleListResponse;

/**
 * [Service Interface] 이의심판 관리 서비스
 *
 * @author   : min
 * @fileName : ConflictService.java
 * @since    : 2026. 01. 07.
 */
public interface ExampleService {


    void createAndModifyConflict(ExampleCreateRequest request);

    ExampleDetailResponse getConflictDetail(String confictSeq );

    ExampleListResponse getConflictList(ExampleListRequest conflictListRequest);

    String getTest();
}