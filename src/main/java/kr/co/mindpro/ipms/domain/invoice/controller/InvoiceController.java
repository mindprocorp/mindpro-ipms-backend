package kr.co.mindpro.ipms.domain.invoice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.invoice.dto.request.InvoiceRequest;
import kr.co.mindpro.ipms.domain.invoice.dto.response.InvoiceResponse;
import kr.co.mindpro.ipms.domain.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [Controller] 청구서 API
 *
 * @author	 : min
 * @fileName : InvoiceController.java
 * @since	 : 2026. 01. 07.
 */
@Tag(name = "청구서 API", description = "청구서 CRUD API")
@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    /* =========================================================================
     * [1] 내국청구서
     * ========================================================================= */

    @Operation(summary = "내국청구서 저장")
    @PostMapping("/domestic")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceDomesticDetail>> saveDomestic(@Valid @RequestBody InvoiceRequest.InvoiceDomesticDetail data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED.value(), "내국 저장 성공", invoiceService.createDomesticInvoice(data)));
    }

    @Operation(summary = "내국 상세 조회")
    @GetMapping("/domestic/{invoiceSeq}")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceDomesticDetail>> getDomesticDetail(@PathVariable String invoiceSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "내국 상세 조회 성공", invoiceService.getDomesticDetail(invoiceSeq)));
    }

    @Operation(summary = "내국 목록 검색")
    @PostMapping("/domestic/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.InvoiceDomesticDetail>>> getDomesticList(@RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "내국 목록 조회 성공", invoiceService.getDomesticList(request)));
    }

    /* =========================================================================
     * [2] 인커밍청구서
     * ========================================================================= */

    @Operation(summary = "인커밍청구서 저장")
    @PostMapping("/incoming")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceIncomingDetail>> saveIncoming(@Valid @RequestBody InvoiceRequest.InvoiceIncomingDetail data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED.value(), "인커밍 저장 성공", invoiceService.createIncomingInvoice(data)));
    }

    @Operation(summary = "인커밍 상세 조회")
    @GetMapping("/incoming/{invoiceSeq}")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceIncomingDetail>> getIncomingDetail(@PathVariable String invoiceSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "인커밍 상세 조회 성공", invoiceService.getIncomingDetail(invoiceSeq)));
    }

    @Operation(summary = "인커밍 목록 검색")
    @PostMapping("/incoming/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.InvoiceIncomingDetail>>> getIncomingList(@RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "인커밍 목록 조회 성공", invoiceService.getIncomingList(request)));
    }

    /* =========================================================================
     * [3] 아웃고잉청구서
     * ========================================================================= */

    @Operation(summary = "아웃고잉청구서 저장")
    @PostMapping("/outgoing")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceOutgoingDetail>> saveOutgoing(@Valid @RequestBody InvoiceRequest.InvoiceOutgoingDetail data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED.value(), "아웃고잉 저장 성공", invoiceService.createOutgoingInvoice(data)));
    }

    @Operation(summary = "아웃고잉 상세 조회")
    @GetMapping("/outgoing/{invoiceSeq}")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceOutgoingDetail>> getOutgoingDetail(@PathVariable String invoiceSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "아웃고잉 상세 조회 성공", invoiceService.getOutgoingDetail(invoiceSeq)));
    }

    @Operation(summary = "아웃고잉 목록 검색")
    @PostMapping("/outgoing/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.InvoiceOutgoingDetail>>> getOutgoingList(@RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "아웃고잉 목록 조회 성공", invoiceService.getOutgoingList(request)));
    }

    /* =========================================================================
     * [추가] 사건별 청구 내역 목록 조회 (그리드용)
     * ========================================================================= */

    @Operation(summary = "사건별 청구 목록 조회", description = "사건번호(tblSeq)를 기반으로 합산된 청구 내역 목록을 조회합니다.")
    @PostMapping("/list-by-case")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.InvoiceListDetail>>> getInvoiceListByCase(
            @Valid @RequestBody BaseSearchRequest request) {
        BaseSearchResponse<InvoiceResponse.InvoiceListDetail> result = invoiceService.getInvoiceListByCase(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "사건별 청구 목록 조회 성공", result));
    }

    /* =========================================================================
     * [탭 1] 청구 상세 내역 (Claim) API
     * ========================================================================= */

    @Operation(summary = "청구 상세 내역 일괄 저장", description = "기존 내역을 삭제하고 리스트를 새로 저장한 후 최신 목록을 반환합니다.")
    @PostMapping("/claims/save-all/{invoiceSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.InvoiceClaimDetail>>> saveClaimList(
            @PathVariable String invoiceSeq, @RequestBody List<InvoiceRequest.InvoiceClaimDetail> data) {
        BaseSearchResponse<InvoiceResponse.InvoiceClaimDetail> result = invoiceService.saveInvoiceClaimList(invoiceSeq, data);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "청구 내역 일괄 저장 성공", result));
    }

    @Operation(summary = "청구 상세 내역 단건 저장")
    @PostMapping("/claims")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceClaimDetail>> saveClaim(@Valid @RequestBody InvoiceRequest.InvoiceClaimDetail data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED.value(), "청구 내역 저장 성공", invoiceService.saveInvoiceClaim(data)));
    }

    @Operation(summary = "청구 상세 내역 목록 조회")
    @PostMapping("/claims/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.InvoiceClaimDetail>>> getClaimList(
            @Valid @RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "청구 내역 목록 조회 성공", invoiceService.getInvoiceClaimList(request)));
    }

    @Operation(summary = "청구 상세 내역 단건 조회")
    @GetMapping("/claims/detail/{claimSeq}")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceClaimDetail>> getClaimDetail(@PathVariable String claimSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "청구 내역 상세 조회 성공", invoiceService.getClaimDetail(claimSeq)));
    }

    @Operation(summary = "대리인 청구 내역 저장")
    @PostMapping("/agentClaims")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceClaimDetail>> saveAgentClaims(
            @Valid @RequestBody InvoiceRequest.InvoiceClaimDetail data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED.value(), "대리인청구 내역 저장 성공", invoiceService.saveAgentClaim(data)));
    }

    @Operation(summary = "대리인 청구 내역 목록 조회")
    @PostMapping("/agentClaims/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.InvoiceClaimDetail>>> getAgentClaimsList(
            @Valid @RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "대리인청구 내역 목록 조회 성공", invoiceService.getAgentClaimList(request)));
    }

    @Operation(summary = "대리인 청구 내역 단건 조회")
    @GetMapping("/agentClaims/detail/{claimSeq}")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceClaimDetail>> getAgentClaimsDetail(@PathVariable String claimSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "대리인청구 내역 상세 조회 성공", invoiceService.getClaimDetail(claimSeq)));
    }

    /* =========================================================================
     * [탭 2] 입금 및 선수금 (Banking) API
     * ========================================================================= */

    @Operation(summary = "입금/선수금 일괄 저장")
    @PostMapping("/{invoiceSeq}/banking/save-all")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.InvoiceBankingDetail>>> saveBankingList(
            @PathVariable String invoiceSeq, @RequestBody List<InvoiceRequest.InvoiceBankingDetail> data) {
        BaseSearchResponse<InvoiceResponse.InvoiceBankingDetail> result = invoiceService.saveInvoiceBankingList(invoiceSeq, data);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "입금 내역 일괄 저장 성공", result));
    }

    @Operation(summary = "입금/선수금 단건 저장")
    @PostMapping("/banking")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceBankingDetail>> saveBanking(@Valid @RequestBody InvoiceRequest.InvoiceBankingDetail data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED.value(), "입금 정보 저장 성공", invoiceService.saveInvoiceBanking(data)));
    }

    @Operation(summary = "입금/선수금 목록 조회")
    @PostMapping("/banking/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.InvoiceBankingDetail>>> getBankingList(
            @Valid @RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "입금 목록 조회 성공", invoiceService.getInvoiceBankingList(request)));
    }

    @Operation(summary = "입금/선수금 단건 조회")
    @GetMapping("/banking/detail/{bankingSeq}")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceBankingDetail>> getBankingDetail(@PathVariable String bankingSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "입금 상세 조회 성공", invoiceService.getBankingDetail(bankingSeq)));
    }

    @Operation(summary = "해외 송금내역 저장")
    @PostMapping("/foreign-banking")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceForeignBankingDetail>> saveForeignBanking(
            @Valid @RequestBody InvoiceRequest.InvoiceForeignBankingDetail data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED.value(), "해외 송금 정보 저장 성공", invoiceService.saveForeignBanking(data)));
    }

    @Operation(summary = "해외 송금내역 목록 조회")
    @PostMapping("/foreign-banking/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.InvoiceForeignBankingDetail>>> getForeignBankingList(
            @Valid @RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "해외 송금 목록 조회 성공", invoiceService.getForeignBankingList(request)));
    }

    @Operation(summary = "해외 송금내역 단건 조회")
    @GetMapping("/foreign-banking/detail/{bankingSeq}")
    public ResponseEntity<ApiResponse<InvoiceResponse.InvoiceForeignBankingDetail>> getForeignBankingDetail(@PathVariable String bankingSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "해외 송금 상세 조회 성공", invoiceService.getForeignBankingDetail(bankingSeq)));
    }

    /* =========================================================================
     * [탭 3] 실적 분배 (Performance) API
     * ========================================================================= */

    @Operation(summary = "실적 분배 일괄 저장")
    @PostMapping("/performance/save-all/{invoiceSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.PerformanceDetail>>> savePerformanceList(
            @PathVariable String invoiceSeq, @RequestBody List<InvoiceRequest.PerformanceDetail> data) {
        BaseSearchResponse<InvoiceResponse.PerformanceDetail> result = invoiceService.savePerformanceList(invoiceSeq, data);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "실적 분배 일괄 저장 성공", result));
    }

    @Operation(summary = "실적 분배 단건 저장")
    @PostMapping("/performance")
    public ResponseEntity<ApiResponse<InvoiceResponse.PerformanceDetail>> savePerformance(@Valid @RequestBody InvoiceRequest.PerformanceDetail data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED.value(), "실적 정보 저장 성공", invoiceService.savePerformance(data)));
    }

    @Operation(summary = "실적 분배 목록 조회")
    @PostMapping("/performance/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.PerformanceDetail>>> getPerformanceList(@Valid @RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "실적 목록 조회 성공", invoiceService.getPerformanceList(request)));
    }

    @Operation(summary = "실적 분배 단건 조회")
    @GetMapping("/performance/detail/{performanceSeq}")
    public ResponseEntity<ApiResponse<InvoiceResponse.PerformanceDetail>> getPerformanceDetail(@PathVariable String performanceSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "실적 상세 조회 성공", invoiceService.getPerformanceDetail(performanceSeq)));
    }

    @Operation(summary = "출원정보 검색")
    @PostMapping("/searchApp/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<InvoiceResponse.AppDetail>>> getAppDetail(@Valid @RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "출원정보 조회 성공", invoiceService.getAppDetailList(request)));
    }

    /* =========================================================================
     * 삭제 API
     * ========================================================================= */

    @Operation(summary = "청구서 단건 삭제")
    @DeleteMapping("/{invoiceSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteInvoice(@PathVariable String invoiceSeq) {
        invoiceService.deleteInvoice(invoiceSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "청구서 삭제 성공"));
    }

    @Operation(summary = "청구서 일괄 삭제")
    @PostMapping("/delete-list")
    public ResponseEntity<ApiResponse<Void>> deleteInvoiceList(@RequestBody List<String> invoiceSeqs) {
        invoiceService.deleteInvoiceList(invoiceSeqs);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "청구서 일괄 삭제 성공"));
    }

    @Operation(summary = "청구 상세 내역 단건 삭제")
    @DeleteMapping("/claims/{claimSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteClaim(@PathVariable String claimSeq) {
        invoiceService.deleteClaim(claimSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "청구 내역 삭제 성공"));
    }

    @Operation(summary = "청구 상세 내역 일괄 삭제")
    @PostMapping("/claims/delete-list/{invoiceSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteClaimList(
            @PathVariable String invoiceSeq, @RequestBody List<String> claimSeqs) {
        invoiceService.deleteClaimList(claimSeqs, invoiceSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "청구 내역 일괄 삭제 성공"));
    }

    @Operation(summary = "입금/선수금 단건 삭제")
    @DeleteMapping("/banking/{bankingSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteBanking(@PathVariable String bankingSeq) {
        invoiceService.deleteBanking(bankingSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "입금 내역 삭제 성공"));
    }

    @Operation(summary = "입금/선수금 일괄 삭제")
    @PostMapping("/banking/delete-list/{invoiceSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteBankingList(
            @PathVariable String invoiceSeq, @RequestBody List<String> bankingSeqs) {
        invoiceService.deleteBankingList(bankingSeqs, invoiceSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "입금 내역 일괄 삭제 성공"));
    }

    @Operation(summary = "실적 분배 단건 삭제")
    @DeleteMapping("/performance/{performanceSeq}")
    public ResponseEntity<ApiResponse<Void>> deletePerformance(@PathVariable String performanceSeq) {
        invoiceService.deletePerformance(performanceSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "실적 분배 삭제 성공"));
    }

    @Operation(summary = "실적 분배 일괄 삭제")
    @PostMapping("/performance/delete-list/{invoiceSeq}")
    public ResponseEntity<ApiResponse<Void>> deletePerformanceList(
            @PathVariable String invoiceSeq, @RequestBody List<String> performanceSeqs) {
        invoiceService.deletePerformanceList(performanceSeqs, invoiceSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "실적 분배 일괄 삭제 성공"));
    }
}