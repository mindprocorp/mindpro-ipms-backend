package kr.co.mindpro.ipms.domain.cost.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.cost.dto.request.AnnuityYearRequest;
import kr.co.mindpro.ipms.domain.cost.dto.request.CostSaveRequest;
import kr.co.mindpro.ipms.domain.cost.dto.response.AnnuityYearResponse;
import kr.co.mindpro.ipms.domain.cost.dto.response.CostDetailResponse;
import kr.co.mindpro.ipms.domain.cost.service.CostService;
import kr.co.mindpro.ipms.domain.cost.vo.CostVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [Controller] 비용 API
 *
 * @author	 : mindpro
 * @fileName : CostController.java
 * @since	 : 2026. 01. 07.
 */
@Slf4j
@Tag(name = "비용 API", description = "비용(수수료/관납료) CRUD API")
@RestController
@RequestMapping("/api/cost")
@RequiredArgsConstructor
public class CostController {

    private final CostService costService;

//    /**
//     * [조회] 특정 업무 상세 조회 (가공된 Map 응답)
//     * 사용자님의 의견대로 CostDetailResponse를 반환하여
//     * 프론트엔드가 yyyyMMdd 포맷과 Map 구조를 바로 쓸 수 있게 합니다.
//     */
//    @Operation(summary = "업무별 비용 상세 조회", description = "특정 업무(tblSeq)에 연결된 비용 항목들을 Map 구조로 조회합니다.")
//    @GetMapping("/detail/{officeSeq}/{tblSeq}")
//    public ResponseEntity<ApiResponse<CostDetailResponse>> getCostDetails(
//            @PathVariable String officeSeq, @PathVariable String tblSeq) {
//
//        log.debug("Cost 상세 조회 요청 - officeSeq: {}, tblSeq: {}", officeSeq, tblSeq);
//
//        // 프론트엔드가 별도 파싱 없이 Key-Value로 즉시 매핑할 수 있도록 응답 객체 생성
//        CostDetailResponse response = costService.getCostMapByWork(tblSeq, officeSeq);
//
//        return ResponseEntity.ok(
//                ApiResponse.success(HttpStatus.OK.value(), "비용 상세조회 성공", response)
//        );
//    }

    /**
     * [저장] 비용 일괄 저장 및 수정 (Upsert)
     * 화면의 동적 리스트(Map 형태) 데이터를 순회하며 DB 존재 여부에 따라
     * Insert/Update를 자동으로 분기 처리합니다.
     */
//    @Operation(summary = "비용 일괄 저장", description = "화면에서 입력한 비용 정보들을 일괄 저장 및 수정(Upsert)합니다.")
//    @PostMapping("/save")
//    public ResponseEntity<ApiResponse<Void>> saveAllCosts(@Valid @RequestBody CostSaveRequest request) {
//        log.debug("Cost 일괄 저장 요청 - tblSeq: {}", request.getTblSeq());
//
//        costService.saveAllCosts(request);
//
//        // 데이터가 없는 저장 성공 메시지는 ApiResponse.success(status, message) 규격 사용
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.success(HttpStatus.CREATED.value(), "비용 정보 저장 및 수정이 완료되었습니다."));
//    }

    /**
     * [조회] 비용 리스트 통합 검색
     * VO에 정의된 검색 조건을 기반으로 다건의 비용 내역을 반환합니다.
     */
    @Operation(summary = "비용 리스트 검색", description = "조건에 맞는 비용 리스트를 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<CostVO>>> getCostList(CostVO searchVO) {
        List<CostVO> list = costService.getCostList(searchVO);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "비용 리스트 조회 성공", list)
        );
    }

    /**
     * [조회] 비용 리스트 검색
     * 특/실, 디자인 연차관리 탭 - 출원 건에 연결되어있는 다건의 비용 내역을 반환합니다.
     */
    @Operation(summary = "연차관리 리스트 검색", description = "연결된 업무 Seq에 맞는 연차관리 리스트를 조회합니다.")
    @GetMapping("/list/{appSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<AnnuityYearResponse.AnnuityYearDetailResponse>>> getAnnuityYearListByWork(@PathVariable @Parameter(description = "연결된 업무 seq", example = "PAT20260000005") String appSeq) {

        BaseSearchResponse<AnnuityYearResponse.AnnuityYearDetailResponse> res = costService.getAnnuityYearListByWork(appSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "비용 리스트 조회 성공", res)
        );
    }

    /**
     * [저장] 비용 단건 등록
     * 특/실, 디자인 연차관리 탭 - 특정 시점에 발생하는 단일 비용 항목을 등록하기 위한 API입니다.
     */
    @Operation(summary = "연차관리 단건 등록", description = "개별 연차관리 정보를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerCost(@Valid @RequestBody AnnuityYearRequest.AnnuityYearTabRequest request) {

        costService.saveAnnuityYear(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "비용 단건 등록이 완료되었습니다."));
    }

    /**
     * [삭제] 연차관리 단건 논리적 삭제.
     * */
    @Operation(summary = "비용관련 (연차관리/갱신관리) 단건 논리적 삭제", description = "연결된 업무 Seq에 맞는 연차관리/갱신관리 단건 정보를 논리적으로 삭제합니다.")
    @DeleteMapping("/delete/soft/{tblSeq}/{costSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteAnnuityYear(@PathVariable String tblSeq, @PathVariable String costSeq) {

        costService.softDeleteCostWithCostMapp(tblSeq, costSeq);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(), "연차/갱신관리 단건 삭제가 완료되었습니다."));
    }

    /**
     * [삭제] 연차관리 다건 논리적 삭제.
     * */
    @Operation(summary = "비용관련 (연차관리/갱신관리) 다건 논리적 삭제", description = "연결된 업무 Seq에 맞는 연차관리/갱신관리 다건 정보를 논리적으로 삭제합니다.")
    @DeleteMapping("/multi-delete/soft/{tblSeq}")
    public ResponseEntity<ApiResponse<Void>> multiDeleteAnnuityYear(@PathVariable("tblSeq") String tblSeq, @RequestBody List<String> targetSeqList) {

        costService.softDeleteCostWithCostMappByList(tblSeq, targetSeqList);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(), "연차/갱신관리 다건 삭제가 완료되었습니다."));
    }

    /**
     * [삭제] 연차관리 단건 물리적 삭제.
     * */
    @Operation(summary = "비용관련 (연차관리/갱신관리) 단건 물리적 삭제", description = "연결된 업무 Seq에 맞는 연차관리/갱신관리 단건 정보를 물리적으로 삭제합니다.")
    @DeleteMapping("/delete/hard/{tblSeq}/{costSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteAnnuityYear(@PathVariable String tblSeq, @PathVariable String costSeq) {

        costService.hardDeleteCostWithCostMapp(tblSeq, costSeq);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(), "연차/갱신관리 단건 삭제가 완료되었습니다."));
    }

    /**
     * [삭제] 연차관리 다건 물리적 삭제.
     * */
    @Operation(summary = "비용관련 (연차관리/갱신관리) 다건 물리적 삭제", description = "연결된 업무 Seq에 맞는 연차관리/갱신관리 다건 정보를 물리적으로 삭제합니다.")
    @DeleteMapping("/multi-delete/hard/{tblSeq}")
    public ResponseEntity<ApiResponse<Void>> multiHardDeleteAnnuityYear(@PathVariable("tblSeq") String tblSeq, @RequestBody List<String> targetSeqList) {

        costService.hardDeleteCostWithCostMappByList(tblSeq, targetSeqList);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(), "연차/갱신관리 다건 삭제가 완료되었습니다."));
    }

    @Operation(summary = "연차관리 단건 조회", description = "연결된 업무 Seq에 맞는 연차관리 단건 정보를 조회합니다.")
    @GetMapping("/cost/{tblSeq}/{costSeq}")
    public ResponseEntity<ApiResponse<AnnuityYearResponse.AnnuityYearDetailResponse>> getAnnuityYearDetail(@PathVariable String tblSeq, @PathVariable String costSeq) {

        AnnuityYearResponse.AnnuityYearDetailResponse res = costService.getAnnuityYearDetail(tblSeq, costSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "비용 리스트 조회 성공", res)
        );
    }

    /**
     * [저장] 비용 단건 등록
     * 상표 갱신관리 탭 - 특정 시점에 발생하는 단일 비용 항목을 등록하기 위한 API입니다.
     */
    @Operation(summary = "갱신관리 단건 등록", description = "개별 갱신관리 정보를 등록합니다.")
    @PostMapping("/renewalMng/register")
    public ResponseEntity<ApiResponse<Void>> registerRenewalMng(@Valid @RequestBody CostSaveRequest.TrademarkRenewalRequest request) {

        costService.saveRenewalMng(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "비용 단건 등록이 완료되었습니다."));
    }

    /**
     * [조회] 비용 리스트 검색
     * 상표 갱신관리 탭 - 출원 건에 연결되어있는 다건의 비용 내역을 반환합니다.
     */
    @Operation(summary = "갱신관리 리스트 검색", description = "연결된 업무 Seq에 맞는 갱신관리 리스트를 조회합니다.")
    @GetMapping("/renewalMng/{appSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<CostDetailResponse.TrademarkRenewalResponse>>> getRenewalMngList(@PathVariable @Parameter(description = "연결된 업무 seq", example = "APPMST20260000259") String appSeq) {

        BaseSearchResponse<CostDetailResponse.TrademarkRenewalResponse> res = costService.getRenewalMngList(appSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "비용 리스트 조회 성공", res)
        );
    }

    @Operation(summary = "갱신관리 단건 조회", description = "갱신관리 단건 조회 API")
    @GetMapping("/renewalMng/detail/{tblSeq}/{costSeq}")
    public ResponseEntity<ApiResponse<CostDetailResponse.TrademarkRenewalResponse>> getRenewalMngDetail(@PathVariable String tblSeq, @PathVariable String costSeq) {

        CostDetailResponse.TrademarkRenewalResponse res = costService.getRenewalMngDetail(tblSeq, costSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "비용 리스트 조회 성공", res)
        );
    }


}