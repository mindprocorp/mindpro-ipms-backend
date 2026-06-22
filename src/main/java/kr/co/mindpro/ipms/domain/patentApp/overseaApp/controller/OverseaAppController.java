package kr.co.mindpro.ipms.domain.patentApp.overseaApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.service.AppCommonService;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.*;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.*;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.service.OverseaAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author : seokho
 * @fileName : OverseaAppService.java
 * @since : 2026. 2. 10.
 */
@Slf4j
@Tag(name = "해외 출원 (개국 이외)", description = "해외 출원 - 개국 이외 권리별 CRUD API")
@RestController
@RequestMapping("/api/oversea/")
@RequiredArgsConstructor
public class OverseaAppController {

    private final OverseaAppService overseaService;
    private final AppCommonService appCommonService;

    /**
     * 해외 출원건 전체 리스트 조회 (페이지 포함)
     * GET /api/oversea/list
     * */
    @Operation(summary = "해외 출원 리스트 조회", description = "해외 출원 리스트 API")
    @PostMapping("/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<OverseaAppListResponse.AppListDetailResponse>>> getOverseaList(@RequestBody BaseSearchRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "해외 출원 리스트 조회 성공", overseaService.getOverseaList(request))
        );
    }

    /**
     * 해외 출원 - PCT 등록 API
     * POST /api/oversea/pct
     * description : pct는 출원에 해당하나 권리를 보장해주는 제도가 아님.
     * pct 출원을 하면 전 세계 가입국에 '동시에 출원한 것과 같은 효과'를 부여하여, 우선일을 선점하는 제도.
     * 특허/실용신안인 기술적인 범위만 가능하며 국내 출원이 없어도 가능함.
     * 국내 출원은 12개월 내에 해외 특허를 진행해야 하지만 pct 출원을 하면
     * 우선일(최초 출원일)로부터 30~31개월까지 해외 진출 결정을 유예할 수 있음.
     *
     * 성격: PCT는 '국제출원' 행위이며, 그 자체로 권리를 부여하는 '등록' 제도가 아님.
     * 효과: 단 한 번의 출원으로 전 세계 가입국 전체에 동시 출원 효과를 발생시키며, 기술 선점일(우선일)을 확보함.
     * 범위: 특허 및 실용신안(기술적 발명)에 한정되며, 국내 출원 없이 Direct PCT도 가능함.
     * 장점: 국내 출원 후 12개월 내에 개별 국가로 나가야 하는 압박에서 벗어나, 우선일로부터 30~31개월까지 해외 진출 국가 결정을 미룰 수 있음 (시간 및 비용 효율화).
     */
    @Operation(summary = "해외 pct 등록", description = "해외 pct 등록 API")
    @PostMapping(value ="/pct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createPct(
            /*@Valid*/@Parameter(description = "해외 pct 출원 데이터 (JSON)") @RequestPart("data") OverseaPctAppRequest.CreatePctAppRequest request,
                      @Parameter(description = "대표도 이미지 파일") @RequestPart(value = "mainDrawingFile", required = false) MultipartFile mainDrawingFile
    ) {

        String appSeq = overseaService.savePct(request, mainDrawingFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "해외 PCT 특허 출원 등록이 완료되었습니다.", appSeq));
    }

    /**
     * 해외 출원 - 상세 조회 API
     * GET /api/oversea/{rightType}/{appSeq}
     */
    @Operation(summary = "해외 pct 조회", description = "해외 pct 조회 API")
    @GetMapping("/pct/{appSeq}")
    public ResponseEntity<ApiResponse<OverseaPctAppResponse.PctAppDetailResponse>> getPctDetail(
            /*@Valid*/@PathVariable @Parameter(description = "조회할 해외 pct appSeq", example = "APPMST20260000002") String appSeq) {

        OverseaPctAppResponse.PctAppDetailResponse appDetailResponse = overseaService.getPctDetail(appSeq);;

        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "해외 출원 - PCT 상세조회 성공", appDetailResponse));
    }



    /**
     * 해외 출원 - EP 등록 API
     * POST /api/oversea/ep
     * Description: 유럽 내 여러 국가에서 특허권을 얻기 위해 각국 특허청이 아닌 유럽특허청(EPO)에 한 번만 신청하는 통합 특허 시스템.
     * Effect: EPO의 심사를 통과(Grant)하면, 내가 선택한 유럽 각국에서 개별 특허를 받은 것과 동일한 법적 효과를 가짐.
     * Scope: 기술적 발명(특허)에 한정됨. (상표, 디자인, 실용신안은 EP 시스템으로 출원 불가)
     * Advantage: 여러 나라에 개별 출원하는 것보다 비용이 저렴하고, 영어·독어·불어 중 하나로만 심사를 진행하므로 관리가 매우 효율적임.
     */
    @Operation(summary = "해외 EP 등록", description = "해외 EP 등록 API")
    @PostMapping(value ="/ep", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createEp(
            /*@Valid*/@Parameter(description = "해외 EP 출원 데이터 (JSON)") @RequestPart("data") OverseaEpAppRequest.CreateEpAppRequest request,
            @Parameter(description = "대표도 이미지 파일") @RequestPart(value = "mainDrawingFile", required = false) MultipartFile mainDrawingFile) {

        String appSeq = overseaService.saveEp(request, mainDrawingFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "해외 EP 등록이 완료되었습니다.", appSeq));
    }



    /**
     * 해외 출원 - 상세 조회 API
     * GET /api/oversea/ep/{appSeq}
     */
    @Operation(summary = "해외 EP 조회", description = "해외 EP 조회 API")
    @GetMapping("/ep/{appSeq}")
    public ResponseEntity<ApiResponse<OverseaEpAppResponse.EpAppDetailResponse>> getEpDetail(
            /*@Valid*/@PathVariable @Parameter(description = "조회할 해외 EP appSeq", example = "APPMST20260000002") String appSeq) {

        OverseaEpAppResponse.EpAppDetailResponse appDetailResponse = overseaService.getEpDetail(appSeq);;

        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "해외 출원 - EP 상세조회 성공", appDetailResponse));
    }

    /**
     * 해외 출원 - 마드리드 등록 API
     * POST /api/oversea/madrid
     * Description: 하나의 언어로 작성된 하나의 출원서를 통해 여러 국가에 상표권을 동시에 신청할 수 있는 국제 상표 통합 관리 시스템.
     * Effect: 지정한 국가의 특허청에 상표를 직접 출원한 것과 동일한 법적 효과를 가지며, 등록 후에는 하나의 국제등록번호로 전 세계 상표권을 통합 관리함.
     * Scope: 상표(Trademark)에만 한정됨. (브랜드 이름, 로고, 슬로건 등)
     * Advantage: 국가별로 현지 대리인을 선임할 필요가 없어 초기 비용이 대폭 절감되고, 상표권 갱신이나 주소 변경 등을 WIPO를 통해 한 번에 처리할 수 있어 유지 관리가 매우 편리함.
     * !!반드시 국내 상표 권리가 있어야 가능함. 국내 상표 권리가 소멸되면 같이 사라짐.
     */
    @Operation(summary = "해외 마드리드 등록", description = "해외 마드리드 등록 API")
    @PostMapping(value ="/madrid", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createMadrid(
            /*@Valid*/@Parameter(description = "해외 마드리드 출원 데이터 (JSON)") @RequestPart("data") OverseaMadridAppRequest.CreateMadridRequest request,
            @Parameter(description = "상표 이미지 파일") @RequestPart(value = "trademarkImage", required = false) MultipartFile trademarkImage) {

        String appSeq = overseaService.saveMadrid(request, trademarkImage);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "해외 마드리드 등록이 완료되었습니다.", appSeq));
    }

    /**
     * 해외 출원 - 상세 조회 API
     * GET /api/oversea/madrid/{appSeq}
     */
    @Operation(summary = "해외 마드리드 조회", description = "해외 마드리드 조회 API")
    @GetMapping("/madrid/{appSeq}")
    public ResponseEntity<ApiResponse<OverseaMadridAppResponse.MadridAppDetailResponse>> getMadridDetail(
            /*@Valid*/@PathVariable @Parameter(description = "조회할 해외 마드리드 appSeq", example = "APPMST20260000002") String appSeq) {

        OverseaMadridAppResponse.MadridAppDetailResponse appDetailResponse = overseaService.getMadridDetail(appSeq);;

        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "해외 출원 - 마드리드 상세조회 성공", appDetailResponse));
    }

    /**
     * 해외 출원 - 국제디자인 등록 API
     * POST /api/oversea/interDesign
     * Description: 여러 국가에 각각 디자인 출원을 할 필요 없이, 하나의 언어로 작성된 하나의 출원서를 WIPO(세계지식재산기구)에 제출하여 여러 국가에서 동시에 보호받는 시스템.
     * Effect: 지정한 각 국가의 특허청에 디자인을 직접 출원한 것과 동일한 법적 효과를 가지며, 국제 등록부 한 곳에서 통합 관리됨.
     * Scope: 산업 디자인(Industrial Design)에 한정됨. (제품의 외관, 모양, 패턴, 색채, 그리고 웹/앱의 UI/UX, 아이콘 등)
     * Advantage: 마드리드(상표)와 달리 국내 기초 출원이 없어도 바로 국제 출원이 가능하며, 한 번의 출원에 최대 100개까지의 디자인을 묶어서 낼 수 있어 매우 경제적임.
     */
    @Operation(summary = "해외 국제디자인 등록", description = "해외 국제디자인 등록 API")
    @PostMapping(value ="/interDesign", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createInterDesign(
            /*@Valid*/@Parameter(description = "해외 국제디자인 출원 데이터 (JSON)") @RequestPart("data") OverseaInterDesignAppRequest.CreateInterDesignAppRequest request,
            @Parameter(description = "대표 이미지 파일") @RequestPart(value = "mainImageFile", required = false) MultipartFile mainImageFile
    ) {

        String appSeq = overseaService.saveInterDesign(request, mainImageFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "해외 국제디자인 등록이 완료되었습니다.", appSeq));
    }

    /**
     * 해외 출원 - 상세 조회 API
     * GET /api/oversea/interDesign/{appSeq}
     */
    @Operation(summary = "해외 국제디자인 조회", description = "해외 국제디자인 조회 API")
    @GetMapping("/interDesign/{appSeq}")
    public ResponseEntity<ApiResponse<OverseaInterDesignAppResponse.InterDesignAppDetailResponse>> getInterDesignDetail(
            /*@Valid*/@PathVariable @Parameter(description = "조회할 해외 국제디자인 appSeq", example = "APPMST20260000002") String appSeq) {

        OverseaInterDesignAppResponse.InterDesignAppDetailResponse appDetailResponse = overseaService.getInterDesignDetail(appSeq);;

        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), "해외 출원 - 국제디자인 상세조회 성공", appDetailResponse));
    }

    /**
     * 해외 출원 삭제(물리적) API
     */
    @Operation(summary = "해외 출원 - 삭제(물리적)", description = "해외 출원 - 삭제(물리적) API")
    @DeleteMapping("/delete/hard/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteOverseaApp(
            @PathVariable @Parameter(description = "삭제할 appSeq", example = "APPMST20260000002") String appSeq) {

        appCommonService.hardDeleteAppCommon(appSeq);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(), "해외 출원 - 삭제(물리적)가 완료되었습니다."));
    }

    /**
     * 해외 출원 삭제(논리적) API
     */
    @Operation(summary = "해외 출원 - 삭제(논리적)", description = "해외 출원 - 삭제(논리적) API")
    @DeleteMapping("/delete/soft/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> softDeleteOverseaApp(
            @PathVariable @Parameter(description = "삭제할 appSeq", example = "APPMST20260000002") String appSeq) {

        appCommonService.softDeleteAppCommon(appSeq);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(), "해외 출원 - 삭제(논리적)가 완료되었습니다."));
    }

    /**
     * 해외 마드리드 - 상표 이미지 삭제 API
     */
    @Operation(summary = "해외 마드리드 - 상표 이미지 삭제", description = "해외 마드리드 - 상표 이미지 삭제 API")
    @DeleteMapping("/madrid/image/{fileSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteMadridImage(
            @PathVariable @Parameter(description = "삭제할 fileSeq", example = "FILE20260000001") String fileSeq) {

        overseaService.deleteMadridImage(fileSeq);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(), "해외 마드리드 상표 이미지가 삭제되었습니다."));
    }
}
