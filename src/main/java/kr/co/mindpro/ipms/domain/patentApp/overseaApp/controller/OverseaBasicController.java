package kr.co.mindpro.ipms.domain.patentApp.overseaApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaBasicAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaAppListResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaBasicAppListResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaBasicAppResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.service.OverseaBasicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : seokho
 * @fileName : OverseaBasicController.java
 * @since : 2026. 1. 23.
 */
@Slf4j
@Tag(name = "해외 출원 - 기본", description = "해외 출원 - 기본 CRUD API")
@RestController
@RequestMapping("/api/oversea/basic")
@RequiredArgsConstructor
public class OverseaBasicController {

    private final OverseaBasicService overseaBasicService;

    /**
     * 해외 기본 리스트 출력
     * POST /api/oversea/basic/list
     * */
    @Operation(summary = "해외 기본 리스트 조회", description = "해외 기본 리스트 조회 API")
    @PostMapping("/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<OverseaBasicAppListResponse.BasicListDetailResponse>>> getBasicList(@RequestBody BaseSearchRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "해외 출원 리스트 조회 성공", overseaBasicService.getBasicList(request))
        );
    }

    /**
     * 국내 출원 - 기본 등록 API
     * POST /api/oversea/basic
     */
    @Operation(summary = "해외 출원 - 기본 등록", description = "해외 출원 - 기본 등록 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createApp(
            /*@Valid*/@Parameter(description = "해외 출원 데이터 (JSON)") @RequestPart("data") OverseaBasicAppRequest.CreateOverseaBasicApp request,
                      @Parameter(description = "대표 이미지 파일") @RequestPart(value = "mainImageFile", required = false) MultipartFile mainImageFile) {

        String appExtSeq = overseaBasicService.createBasicApp(request, mainImageFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "해외 출원 - 기본 등록이 완료되었습니다.", appExtSeq));
    }

    /**
     * 해외 출원 - 기본 상세 API
     * GET /api/oversea/basic/{extSeq}
     */
    @Operation(summary = "해외 출원 - 기본 조회", description = "해외 출원 - 기본 조회 API")
    @GetMapping("/{extSeq}")
    public ResponseEntity<ApiResponse<OverseaBasicAppResponse.OverseaBasicAppDetailResponse>> getOverseaAppDetail(
            /*@Valid*/@Parameter(description = "조회할 해외 기본 extSeq", example = "EXTMST20260000001") @PathVariable("extSeq") String extSeq) {

        OverseaBasicAppResponse.OverseaBasicAppDetailResponse appDetailResponse = overseaBasicService.getOverseaBasicAppDetail(extSeq);

        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "해외 출원 - 기본 상세조회 성공", appDetailResponse));
    }

    /**
     * 해외 기본 상세 안에 해외 출원 탭 API (해외기본에 연결되어있는 해외 출원 리스트)
     * GET /api/oversea/basic/list/{extSeq}
     * */
    @Operation(summary = "해외 출원 기본에 연결된 리스트 조회", description = "해외 출원 기본에 연결된 리스트 조회 API")
    @PostMapping("/list/chainApp")
    public ResponseEntity<ApiResponse<BaseSearchResponse<OverseaAppListResponse.AppListDetailResponse>>> getBasicChainOverseaAppList(@RequestBody BaseSearchRequest request) {

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "해외 기본에 연결된 해외 출원 리스트 조회 성공", overseaBasicService.getBasicChainOverseaAppList(request)));
    }

    /**
     * 해외기본 논리적 삭제 API
     * */
    @Operation(summary = "해외기본 논리적 삭제", description = "해외기본 논리적 삭제 API")
    @DeleteMapping("/{extSeq}")
    public ResponseEntity<ApiResponse<Void>> softDeleteBasicApp(@PathVariable String extSeq) {

        overseaBasicService.softDeleteBasicApp(extSeq);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "해외기본 논리적 삭제 성공"));
    }
}
