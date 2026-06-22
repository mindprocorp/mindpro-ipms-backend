package kr.co.mindpro.ipms.domain.maintenanceFee.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.maintenanceFee.dto.request.MaintenanceFeeRequest;
import kr.co.mindpro.ipms.domain.maintenanceFee.dto.response.MaintenanceFeeResponse;
import kr.co.mindpro.ipms.domain.maintenanceFee.service.MaintenanceFeeService;
import kr.co.mindpro.ipms.domain.requiredDoc.dto.response.RequiredDocResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : seokho
 * @fileName : MaintenanceFeeController.java
 * @since : 2026. 4. 2.
 */
@Slf4j
@Tag(name = "유지비 탭 관련 API", description = "유지비 탭 CRUD API")
@RestController
@RequestMapping("/api/maintenance-fee")
@RequiredArgsConstructor
public class MaintenanceFeeController {

    private final MaintenanceFeeService maintenanceFeeService;

    @Operation(summary = "유지비 정보 등록", description = "유지비 정보를 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createMaintenanceFee(@RequestBody MaintenanceFeeRequest.CreateMaintenanceFeeRequest request) {

        maintenanceFeeService.createMaintenanceFee(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "유지비 등록이 정상적으로 완료되었습니다.", null));
    }

    @Operation(summary = "유지비 목록 조회", description = "유지비 목록을 조회합니다.")
    @PostMapping("/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<MaintenanceFeeResponse.MaintenanceFeeList>>> getMaintFeeListByAppSeq(@RequestBody BaseSearchRequest request) {

        BaseSearchResponse<MaintenanceFeeResponse.MaintenanceFeeList> response = maintenanceFeeService.getMaintFeeListByAppSeq(request);

        return ResponseEntity.ok()
                .body(ApiResponse.success(HttpStatus.OK.value(), "구비서류 목록 조회가 완료되었습니다.", response));
    }

    @Operation(summary = "유지비 단건 조회", description = "유지비 단건 정보를 조회합니다.")
    @GetMapping("/{appSeq}/{maintFeeSeq}")
    public ResponseEntity<ApiResponse<MaintenanceFeeResponse.MaintenanceFeeDetail>> getMaintenanceFeeDetail(@PathVariable String appSeq, @PathVariable String maintFeeSeq) {

        MaintenanceFeeResponse.MaintenanceFeeDetail res = maintenanceFeeService.getMaintenanceFeeDetail(appSeq, maintFeeSeq);

        return ResponseEntity.ok()
                .body(ApiResponse.success(HttpStatus.OK.value(), "구비서류 단건 조회가 완료되었습니다.", res));
    }

    @Operation(summary = "유지비 단건 논리적 삭제", description = "유지비 단건을 논리적으로 삭제합니다.")
    @DeleteMapping("/delete/soft/{appSeq}/{maintFeeSeq}")
    public ResponseEntity<ApiResponse<Void>> softDeleteMaintenanceFee(@PathVariable String appSeq, @PathVariable String maintFeeSeq) {

        maintenanceFeeService.softDeleteMaintenanceFee(appSeq, maintFeeSeq);

        return ResponseEntity.ok()
                .body(ApiResponse.success(HttpStatus.OK.value(), "구비서류 단건 논리적 삭제가 완료되었습니다."));
    }

    @Operation(summary = "유지비 다건 논리적 삭제", description = "유지비 다건을 논리적으로 삭제합니다.")
    @DeleteMapping("/multi-delete/soft/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiSoftDeleteMaintenanceFee(@PathVariable String appSeq, @RequestBody List<String> maintFeeSeqList) {

        maintenanceFeeService.multiSoftDeleteMaintenanceFee(appSeq, maintFeeSeqList);

        return ResponseEntity.ok()
                .body(ApiResponse.success(HttpStatus.OK.value(), "구비서류 다건 논리적 삭제가 완료되었습니다."));
    }

    @Operation(summary = "유지비 단건 물리적 삭제", description = "유지비 단건을 물리적으로 삭제합니다.")
    @DeleteMapping("/delete/hard/{appSeq}/{maintFeeSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteMaintenanceFee(@PathVariable String appSeq, @PathVariable String maintFeeSeq) {

        maintenanceFeeService.hardDeleteMaintenanceFee(appSeq, maintFeeSeq);

        return ResponseEntity.ok()
                .body(ApiResponse.success(HttpStatus.OK.value(), "유지비 단건 물리적 삭제가 완료되었습니다."));
    }

    @Operation(summary = "유지비 다건 물리적 삭제", description = "유지비 다건을 물리적으로 삭제합니다.")
    @DeleteMapping("/multi-delete/hard/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiHardDeleteMaintenanceFee(@PathVariable String appSeq, @RequestBody List<String> maintFeeSeqList) {

        maintenanceFeeService.multiHardDeleteMaintenanceFee(appSeq, maintFeeSeqList);

        return ResponseEntity.ok()
                .body(ApiResponse.success(HttpStatus.OK.value(), "유지비 다건 물리적 삭제가 완료되었습니다."));
    }
}
