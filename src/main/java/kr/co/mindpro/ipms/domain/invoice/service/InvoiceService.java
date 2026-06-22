package kr.co.mindpro.ipms.domain.invoice.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.invoice.dto.request.InvoiceRequest;
import kr.co.mindpro.ipms.domain.invoice.dto.response.InvoiceResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * [Service Interface] 청구 관리 서비스
 * 등록(Create)과 수정(Update)을 통합하여 상세 정보를 반환합니다.
 *
 * @author   : min
 * @fileName : InvoiceService.java
 * @since    : 2026. 01. 07.
 */
public interface InvoiceService {

    /* =========================================================================
     * [1] 청구 마스터 저장 (등록/수정 통합)
     * ========================================================================= */

    /** 내국 청구서 저장 (등록/수정) */
    InvoiceResponse.InvoiceDomesticDetail createDomesticInvoice(InvoiceRequest.InvoiceDomesticDetail request);

    /** 인커밍 청구서 저장 (등록/수정) */
    InvoiceResponse.InvoiceIncomingDetail createIncomingInvoice(InvoiceRequest.InvoiceIncomingDetail request);

    /** 아웃고잉 청구서 저장 (등록/수정) */
    InvoiceResponse.InvoiceOutgoingDetail createOutgoingInvoice(InvoiceRequest.InvoiceOutgoingDetail request);


    /* =========================================================================
     * [2] 상세 조회 (Detail)
     * ========================================================================= */

    InvoiceResponse.InvoiceDomesticDetail getDomesticDetail(String invoiceSeq);

    InvoiceResponse.InvoiceIncomingDetail getIncomingDetail(String invoiceSeq);

    InvoiceResponse.InvoiceOutgoingDetail getOutgoingDetail(String invoiceSeq);


    /* =========================================================================
     * [3] 목록 조회 (List)
     * ========================================================================= */

    @Transactional(readOnly = true)
    BaseSearchResponse<InvoiceResponse.InvoiceListDetail> getInvoiceListByCase(BaseSearchRequest request);

    BaseSearchResponse<InvoiceResponse.InvoiceDomesticDetail> getDomesticList(BaseSearchRequest request);

    BaseSearchResponse<InvoiceResponse.InvoiceIncomingDetail> getIncomingList(BaseSearchRequest request);

    BaseSearchResponse<InvoiceResponse.InvoiceOutgoingDetail> getOutgoingList(BaseSearchRequest request);


    /* =========================================================================
     * [탭 1] 청구 상세 내역 (Claim)
     * ========================================================================= */

    /** 청구 내역 저장 (단건) */
    InvoiceResponse.InvoiceClaimDetail saveInvoiceClaim(InvoiceRequest.InvoiceClaimDetail request);

    /** 대리인 청구 내역 저장 (단건) */
    InvoiceResponse.InvoiceClaimDetail saveAgentClaim(InvoiceRequest.InvoiceClaimDetail data);

    /** 청구 내역 목록 조회 */
    BaseSearchResponse<InvoiceResponse.InvoiceClaimDetail> getInvoiceClaimList(BaseSearchRequest request);

    /** 대리인 청구 내역 목록 조회 */
    BaseSearchResponse<InvoiceResponse.InvoiceClaimDetail> getAgentClaimList(BaseSearchRequest request);

    /** 청구 내역 상세 조회 */
    InvoiceResponse.InvoiceClaimDetail getClaimDetail(String claimSeq);

    /** 대리인 청구 내역 상세 조회 */
    InvoiceResponse.InvoiceClaimDetail getAgentClaimDetail(String claimSeq);

    /** 청구 내역 일괄 저장 (Replace 방식) */
    BaseSearchResponse<InvoiceResponse.InvoiceClaimDetail> saveInvoiceClaimList(String invoiceSeq, List<InvoiceRequest.InvoiceClaimDetail> requests);


    /* =========================================================================
     * [탭 2] 입금 및 선수금 (Banking)
     * ========================================================================= */

    /** 일반 입금 저장 (단건) */
    InvoiceResponse.InvoiceBankingDetail saveInvoiceBanking(InvoiceRequest.InvoiceBankingDetail request);

    /** 일반 입금 목록 조회 */
    BaseSearchResponse<InvoiceResponse.InvoiceBankingDetail> getInvoiceBankingList(BaseSearchRequest request);

    /** 일반 입금 상세 조회 */
    InvoiceResponse.InvoiceBankingDetail getBankingDetail(String bankingSeq);

    /** 일반 입금 일괄 저장 */
    BaseSearchResponse<InvoiceResponse.InvoiceBankingDetail> saveInvoiceBankingList(String invoiceSeq, List<InvoiceRequest.InvoiceBankingDetail> requests);


    /* =========================================================================
     * [탭 3] 해외송금 (Foreign Banking)
     * ========================================================================= */

    /** 해외 송금 저장 */
    InvoiceResponse.InvoiceForeignBankingDetail saveForeignBanking(InvoiceRequest.InvoiceForeignBankingDetail request);

    /** 해외 송금 목록 조회 */
    BaseSearchResponse<InvoiceResponse.InvoiceForeignBankingDetail> getForeignBankingList(BaseSearchRequest request);

    /** 해외 송금 상세 조회 */
    InvoiceResponse.InvoiceForeignBankingDetail getForeignBankingDetail(String bankingSeq);


    /* =========================================================================
     * [탭 4] 실적 분배 (Performance)
     * ========================================================================= */

    /** 실적 분배 저장 (단건) */
    InvoiceResponse.PerformanceDetail savePerformance(InvoiceRequest.PerformanceDetail request);

    /** 실적 분배 목록 조회 */
    BaseSearchResponse<InvoiceResponse.PerformanceDetail> getPerformanceList(BaseSearchRequest request);

    /** 실적 분배 상세 조회 */
    InvoiceResponse.PerformanceDetail getPerformanceDetail(String performanceSeq);

    /** 실적 분배 일괄 저장 */
    BaseSearchResponse<InvoiceResponse.PerformanceDetail> savePerformanceList(String invoiceSeq, List<InvoiceRequest.PerformanceDetail> requests);


    /* =========================================================================
     * [기타] 출원 정보 검색
     * ========================================================================= */

    BaseSearchResponse<InvoiceResponse.AppDetail> getAppDetailList(BaseSearchRequest request);

    /* =========================================================================
     * 삭제
     * ========================================================================= */
    /** 단건 삭제 */
    void deleteInvoice(String invoiceSeq);
    void deleteClaim(String claimSeq);
    void deleteBanking(String bankingSeq);
    void deletePerformance(String performanceSeq);

    /** 일괄 삭제 */
    void deleteInvoiceList(List<String> invoiceSeqs);
    void deleteClaimList(List<String> claimSeqs, String invoiceSeq);
    void deleteBankingList(List<String> bankingSeqs, String invoiceSeq);
    void deletePerformanceList(List<String> performanceSeqs, String invoiceSeq);

}