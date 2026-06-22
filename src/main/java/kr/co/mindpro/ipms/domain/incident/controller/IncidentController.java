package kr.co.mindpro.ipms.domain.incident.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.incident.dto.response.IncidentResponse;
import kr.co.mindpro.ipms.domain.incident.service.IncidentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "사건 관리 API", description = "사건 관리 및 공통 기능 API")
@RestController
@RequestMapping("/api/incident")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @Operation(summary = "업무 시퀀스(tblSeq)별 청구 내역 리스트 조회", description = "업무 시퀀스에 연결된 모든 청구서의 청구 내역을 조회합니다.")
    @GetMapping("/claims/{tblSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<IncidentResponse.IncidentClaimDetail>>> getClaimsByTblSeq(@PathVariable String tblSeq) {
        BaseSearchResponse<IncidentResponse.IncidentClaimDetail> result = incidentService.getClaimsByTblSeq(tblSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "업무별 청구 내역 조회 성공", result));
    }

    @Operation(summary = "청구서 삭제 (소프트 삭제)", description = "선택한 청구서들을 삭제 처리합니다.")
    @DeleteMapping("/claims")
    public ResponseEntity<ApiResponse<Integer>> deleteClaims(@RequestBody List<String> invoiceSeqs) {
        int result = incidentService.deleteClaims(invoiceSeqs);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "청구서 삭제 성공", result));
    }
}
