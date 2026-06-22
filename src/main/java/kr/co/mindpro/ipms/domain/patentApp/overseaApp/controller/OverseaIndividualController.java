package kr.co.mindpro.ipms.domain.patentApp.overseaApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.service.AppCommonService;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaIndividualDesignAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaIndividualHardIpAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaIndividualTrademarkAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaIndividualDesignAppResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaIndividualHardIpAppResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaIndividualTrademarkAppResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.service.OverseaIndividualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : seokho
 * @fileName : OverseaPatentController.java
 * @since : 2026. 1. 28.
 */
@Slf4j
@Tag(name = "해외 출원 - 개국", description = "해외 출원 - 개국 권리별 CRUD API")
@RestController
@RequestMapping("/api/oversea/individual")
@RequiredArgsConstructor
public class OverseaIndividualController {

    private final OverseaIndividualService overseaIndividualService;
    private final AppCommonService appCommonService;

    /**
     * 해외 출원 - 개국 > 특허/실용신안 등록 API
     * POST /api/oversea/individual/patent
     */
    @Operation(summary = "해외 개국 특/실 등록", description = "해외 개국 특/실 등록 API")
    @PostMapping(path ="/patent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createPatent(
            /*@Valid*/@Parameter(description = "해외 개국 > 특/실 출원 데이터 (JSON)") @RequestPart("data") OverseaIndividualHardIpAppRequest.CreateHardIpRequest request,
                      @Parameter(description = "대표 이미지 파일") @RequestPart(value = "mainImageFile", required = false) MultipartFile mainImageFile) {

        String appSeq = overseaIndividualService.createOverseaHardIpApp(request, mainImageFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "해외 개국 특/실 등록이 완료되었습니다.", appSeq));
    }

    /**
     * 해외 출원 - 개국 > 디자인 등록 API
     * POST /api/oversea/individual/design
     */
    @Operation(summary = "해외 개국 디자인 등록", description = "해외 개국 디자인 등록 API")
    @PostMapping(path = "/design", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createDesign(
            /*@Valid*/@Parameter(description = "해외 개국 > 디자인 출원 데이터 (JSON)") @RequestPart("data") OverseaIndividualDesignAppRequest.CreateDesignAppRequest request,
                      @Parameter(description = "사시도 이미지 파일") @RequestPart(value = "mainImageFile", required = false) MultipartFile multiViewDrawingFile) {

        String appSeq = overseaIndividualService.createOverseaDesignApp(request, multiViewDrawingFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "해외 개국 디자인 등록이 완료되었습니다.", appSeq));
    }

    /**
     * 해외 출원 - 개국 > 상표 등록 API
     * POST /api/oversea/individual/trademark
     */
    @Operation(summary = "해외 개국 상표 등록", description = "해외 개국 상표 등록 API")
    @PostMapping(path = "/trademark", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createTrademark(
            /*@Valid*/@Parameter(description = "해외 개국 > 상표 출원 데이터 (JSON)") @RequestPart("data") OverseaIndividualTrademarkAppRequest.CreateTrademarkAppRequest request,
                      @Parameter(description = "상표 이미지 파일") @RequestPart(value = "mainImageFile", required = false) MultipartFile trademarkImageFile) {

        String appSeq = overseaIndividualService.createOverseaTrademarkApp(request, trademarkImageFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "해외 개국 상표 등록이 완료되었습니다.", appSeq));
    }

    /**
     * 해외 개국 출원 - 특/실 상세 API
     */
    @GetMapping("/patent/{appSeq}")
    public ResponseEntity<ApiResponse<OverseaIndividualHardIpAppResponse.HardIpAppDetailResponse>> getOverseaHardIpAppDetail(
            @PathVariable @Parameter(description = "조회할 해외 개국 appSeq", example = "APPMST20260000002") String appSeq
    ) {
        OverseaIndividualHardIpAppResponse.HardIpAppDetailResponse appDetailResponse = overseaIndividualService.getOverseaHardIpAppDetail(appSeq);

        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "해외 출원 - 개국 특/실 상세조회 성공", appDetailResponse));
    }

    /**
     * 해외 개국 출원 - 디자인 상세 API
     */
    @GetMapping("/design/{appSeq}")
    public ResponseEntity<ApiResponse<OverseaIndividualDesignAppResponse.DesignAppDetailResponse>> getOverseaDesignAppDetail(
            @PathVariable @Parameter(description = "조회할 해외 개국 appSeq", example = "APPMST20260000002") String appSeq
    ) {
        OverseaIndividualDesignAppResponse.DesignAppDetailResponse appDetailResponse = overseaIndividualService.getOverseaDesignAppDetail(appSeq);

        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "해외 출원 - 개국 디자인 상세조회 성공", appDetailResponse));
    }

    /**
     * 해외 개국 출원 - 상표 상세 API
     */
    @GetMapping("/trademark/{appSeq}")
    public ResponseEntity<ApiResponse<OverseaIndividualTrademarkAppResponse.TrademarkAppDetailResponse>> getOverseaTrademarkAppDetail(
            @PathVariable @Parameter(description = "조회할 해외 개국 appSeq", example = "APPMST20260000002") String appSeq
    ) {
        OverseaIndividualTrademarkAppResponse.TrademarkAppDetailResponse appDetailResponse = overseaIndividualService.getOverseaTrademarkAppDetail(appSeq);

        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "해외 출원 - 개국 상표 상세조회 성공", appDetailResponse));
    }

    /**
     * 해외 출원 삭제(물리적) API
     * */
    @Operation(summary = "개국 출원 - 삭제(물리적)", description = "국내 출원 - 삭제(물리적) API")
    @DeleteMapping("/hardDelete/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteDomesticApp(
            @PathVariable @Parameter(description = "삭제할 appSeq", example = "APPMST20260000002") String appSeq) {

        appCommonService.hardDeleteAppCommon(appSeq);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(), "개국 출원 - 삭제가 완료되었습니다."));
    }

    /**
     * 해외 출원 삭제(논리적) API
     * */
    @Operation(summary = "개국 출원 - 삭제(논리적)", description = "국내 출원 - 삭제(논리적) API")
    @DeleteMapping("/softDelete/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> softDeleteDomesticApp(
            @PathVariable @Parameter(description = "삭제할 appSeq", example = "APPMST20260000002") String appSeq) {

        appCommonService.softDeleteAppCommon(appSeq);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(), "개국 출원 - 삭제가 완료되었습니다."));
    }
}
