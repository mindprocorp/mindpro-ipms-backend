package kr.co.mindpro.ipms.domain.patentApp.domesticApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.service.AppCommonService;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.request.DomesticDesignAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.request.DomesticHardIpAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.request.DomesticTrademarkRequest;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.response.*;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.service.DomesticAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [Controller] 국내 출원
 *
 * @author : seokho
 * @fileName : DomesticAppController.java
 * @since : 2026. 1. 31.
 */
@Slf4j
@Tag(name = "국내 출원 API", description = "국내 출원 CRUD API")
@RestController
@RequestMapping("/api/domesticApp")
@RequiredArgsConstructor
public class DomesticAppController {

    private final DomesticAppService domesticAppService;
    private final AppCommonService appCommonService;

    /**
     * 국내 출원 검색 리스트 조회
     */
    @Operation(summary = "국내 출원 리스트 조회", description = "검색 조건에 맞는 국내 출원 목록을 조회합니다.")
    @PostMapping("/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<DomesticAppListResponse.AppListResponse>>> getDomesticAppList(@RequestBody BaseSearchRequest request) {

        log.info(">>> 국내 출원 리스트 조회 요청: {}", request);

        BaseSearchResponse<DomesticAppListResponse.AppListResponse> response = domesticAppService.getDomesticAppSearchList(request);

        // 서비스 호출 (결과는 PageResponseVO로 감싸서 리턴)
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "국내 출원 리스트 조회 성공", response));
    }

    /**
     * 국내 출원 - 특허/실용신안 등록 API
     * POST /api/domesticApp/patent
     */
    @Operation(summary = "국내 출원 - 특허/실용신안 등록", description = "국내 출원 - 특허/실용신안 등록 API")
    @PostMapping(value = "/patent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createHardIpApp(
            /*@Valid*/@Parameter(description = "국내 특허 출원 데이터 (JSON)") @RequestPart("data") DomesticHardIpAppRequest.CreateHardIpAppRequest request,
            @Parameter(description = "대표도 이미지 파일") @RequestPart(value = "mainDrawingFile", required = false) MultipartFile mainDrawingFile
    ) {

        String appSeq = domesticAppService.createHardIpApp(request, mainDrawingFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "신규 국내 출원 - 특허/실용신안 등록이 완료되었습니다.", appSeq));
    }

    /**
     * 국내 출원 - 디자인 등록 API
     * POST /api/domesticApp/design
     */
    @Operation(summary = "국내 출원 - 디자인 등록", description = "국내 출원 - 디자인 등록 API")
    @PostMapping(value = "/design", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createDesign(
            /*@Valid*/@Parameter(description = "국내 디자인 출원 데이터 (JSON)") @RequestPart("data") DomesticDesignAppRequest.CreateDomesticDesignAppRequest request,
                      @Parameter(description = "사시도 이미지 파일") @RequestPart(value = "multiViewDrawingFile", required = false) MultipartFile multiViewDrawingFile) {

        String appSeq = domesticAppService.createDesignApp(request, multiViewDrawingFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "신규 국내 출원 - 디자인 등록이 완료되었습니다.", appSeq));
    }

    /**
     * 국내 출원 - 상표 등록 API
     * POST /api/domesticApp/trademark
     * */
    @Operation(summary = "국내 출원 - 상표 등록", description = "국내 출원 - 상표 등록 API")
    @PostMapping(value = "/trademark", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createTrademark(
            /*@Valid*/@Parameter(description = "국내 상표 출원 데이터 (JSON)") @RequestPart("data") DomesticTrademarkRequest.CreateTrademarkAppRequest request,
                      @Parameter(description = "상표 이미지 파일") @RequestPart(value = "trademarkImageFile", required = false) MultipartFile trademarkImageFile) {

        String appSeq = domesticAppService.createTrademarkApp(request, trademarkImageFile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "신규 국내 출원 - 상표 등록이 완료되었습니다.", appSeq));
    }

    /**
     * 국내 출원 상세조회 API
     * POST /api/domesticApp/{rightType}/{appSeq}
     * */
    @Operation(summary = "국내 출원 - 상세조회", description = "국내 출원 - 상세조회 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    oneOf = {
                                            DomesticHardIpAppResponse.HardIpAppDetailResponse.class,
                                            DomesticDesignAppResponse.DesignAppDetailResponse.class,
                                            DomesticTrademarkAppResponse.TrademarkAppDetailResponse.class
                                    }
                            )
                    )
            )
    })
    @GetMapping("/{rightType}/{appSeq}")
    public ResponseEntity<ApiResponse<? extends DomesticAppDetailResponse>> getDetailByRight(
            @PathVariable @Parameter(description = "조회할 권리 구분", example = "patent") String rightType,
            @PathVariable @Parameter(description = "조회할 해외 개국 appSeq", example = "APPMST20260000002") String appSeq) {

        List<String> allowedTypes = List.of("patent", "practice", "design", "trademark");

        if (!allowedTypes.contains(rightType.toLowerCase())) {
            throw new IllegalArgumentException("유효하지 않은 권리 구분입니다: " + rightType);
        }

        DomesticAppDetailResponse appDetailResponse = domesticAppService.getDomesticAppDetail(appSeq);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "국내 출원 상세조회 성공", appDetailResponse));
    }

    /**
     * 국내 출원 삭제(물리적) API
     * */
    @Operation(summary = "국내 출원 - 삭제(물리적)", description = "국내 출원 - 삭제(물리적) API")
    @DeleteMapping("/delete/hard/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteDomesticApp(
            @PathVariable @Parameter(description = "삭제할 appSeq", example = "APPMST20260000002") String appSeq) {

        appCommonService.hardDeleteAppCommon(appSeq);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(), "국내 출원 - 삭제가 완료되었습니다."));
    }

    /**
     * 국내 출원 삭제(논리적) API
     * */
    @Operation(summary = "국내 출원 - 삭제(논리적)", description = "국내 출원 - 삭제(논리적) API")
    @DeleteMapping("/delete/soft/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> softDeleteDomesticApp(
            @PathVariable @Parameter(description = "삭제할 appSeq", example = "APPMST20260000002") String appSeq) {

        appCommonService.softDeleteAppCommon(appSeq);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(), "국내 출원 - 삭제가 완료되었습니다."));
    }


}
