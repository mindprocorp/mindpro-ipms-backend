package kr.co.mindpro.ipms.domain.bizinfo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.bizinfo.dto.request.BizInfoRequest;
import kr.co.mindpro.ipms.domain.bizinfo.dto.response.BizInfoResponse;
import kr.co.mindpro.ipms.domain.bizinfo.service.BizInfoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [Controller] 사업자정보 API
 *
 * @author	 : mindpro
 * @fileName : BizInfoController.java
 * @since	 : 2026. 01. 07.
 */
@Slf4j
@Tag(name = "사업자정보 API", description = "사업자정보 CRUD API")
@RestController
@RequestMapping("/api/BizInfo")
@RequiredArgsConstructor
public class BizInfoController {

    private final BizInfoService bizInfoService;


    @Operation(summary = "사업자 정보 저장", description = "신규 등록 또는 수정 시 이력을 관리하며 저장합니다.")
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<BizInfoResponse.BizInfoDetail>> saveBizInfo(@RequestBody BizInfoRequest.BizInfoDetail vo) {

        BizInfoResponse.BizInfoDetail result = bizInfoService.saveBizInfo(vo);

        // 요청하신 포맷: Status, Message, Data 순서
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "사업자 정보 저장 성공", result)
        );
    }

    @Operation(summary = "사업자 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<BizInfoResponse.BizInfoList>> getBizInfoList(@RequestBody BaseSearchRequest request) {

        BizInfoResponse.BizInfoList list = bizInfoService.getBizInfoList(request);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "사업자 목록 조회 성공", list)
        );
    }


    @Operation(summary = "사업자 상세 조회", description = "일련번호를 통해 특정 사업자의 상세 정보를 조회합니다.")
    @GetMapping("/{bizInfoSeq}") // 상세 조회를 위한 경로 변수
    public ResponseEntity<ApiResponse<BizInfoResponse.BizInfoDetail>> getBizInfoDetail(
            @PathVariable("bizInfoSeq") String bizInfoSeq) {

        BizInfoResponse.BizInfoDetail result = bizInfoService.getBizInfoDetail(bizInfoSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "사업자 상세 조회 성공", result)
        );
    }



}