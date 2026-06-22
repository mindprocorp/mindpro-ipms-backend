package kr.co.mindpro.ipms.domain.invoice.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.invoice.vo.*;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppMstVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 청구서 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : InvoiceMapper.java
 * @since	 : 2026. 01. 07.
 */
@Mapper
public interface InvoiceMapper {
    /**
     * 청구서 마스터 저장 (내국/인커밍/아웃고잉 통합)
     */
    int insertInvoiceMst(InvoiceMstVO vo);

    /**
     * 청구서 마스터 단건 조회 (히스토리용)
     */
    InvoiceMstVO selectInvoiceMst(@Param("invoiceSeq") String invoiceSeq, @Param("officeSeq") String officeSeq);

    /**
     * 청구서 상세 단건 조회
     */
    InvoiceMergeVO findInvoiceBySeq(@Param("invoiceSeq") String invoiceSeq, @Param("officeSeq") String officeSeq);

    /**
     * 카테고리별 청구 목록 조회 (내국/인커밍/아웃고잉 분기)
     */
    List<InvoiceMergeVO> selectInvoiceListByCategory(@Param("request") BaseSearchRequest request, @Param("inOutType") String inOutType);

    /**
     * 카테고리별 청구 목록 총 개수 조회
     */
    int selectInvoiceListByCategoryCount(@Param("request") BaseSearchRequest request, @Param("inOutType") String inOutType);

    /**
     * 청구서 정보 수정
     */
    int updateInvoiceMst(InvoiceMstVO vo);

    /**
     * 청구서 논리 삭제
     */
    int deleteInvoice(@Param("invoiceSeq") String invoiceSeq, @Param("officeSeq") String officeSeq, @Param("updateUser") String updateUser);



    /* =========================================================================
     * [탭 1] 청구 상세 내역 (utb_invoice_claim)
     * ========================================================================= */
    List<InvoiceClaimVO> selectInvoiceClaimList(@Param("request") BaseSearchRequest request, @Param("claimKind") String claimKind);
    Optional<InvoiceClaimVO> selectClaimDetail(@Param("claimSeq") String claimSeq, @Param("officeSeq") String officeSeq);
    int insertInvoiceClaim(InvoiceClaimVO vo);
    int updateInvoiceClaim(InvoiceClaimVO vo);
    int insertInvoiceClaimBatch(@Param("list") List<InvoiceClaimVO> list);
    int deleteInvoiceClaims(@Param("invoiceSeq") String invoiceSeq, @Param("officeSeq") String officeSeq);

    /* =========================================================================
     * [탭 2] 입금 및 선수금 (utb_invoice_banking)
     * ========================================================================= */
    // 1. 목록 조회 (일반 입금/선수금)
    /**
     * 입금/송금 통합 목록 조회 (필터링 및 페이징 적용)
     * @param request 페이징 정보 및 검색조건 (tblSeq 포함)
     * @param bankingCategory 10:입금, 20:출금, 30:선입금 등
     * @param bankingKind 해외대리인송금 등 상세 구분
     */
    List<InvoiceBankingVO> selectInvoiceBankingListFiltered(
            @Param("request") BaseSearchRequest request,
            @Param("bankingCategory") String bankingCategory,
            @Param("bankingKind") String bankingKind
    );
    // [신규] 해외 송금 내역 목록 조회 (banking_kind = '해외대리인송금' 조건)
    List<InvoiceBankingVO> selectInvoiceBankingListFiltered(@Param("invoiceSeq") String invoiceSeq, @Param("officeSeq") String officeSeq);

    // 2. 단건 조회 (상세 내역 - 일반/해외송금 공용)
    Optional<InvoiceBankingVO> selectBankingDetail(@Param("bankingSeq") String bankingSeq, @Param("officeSeq") String officeSeq);

    // 3. 단건 등록
    int insertInvoiceBanking(InvoiceBankingVO vo);

    // 4. 단건 수정
    int updateInvoiceBanking(InvoiceBankingVO vo);

    // 5. 일괄 등록 (Batch)
    int insertInvoiceBankingBatch(@Param("list") List<InvoiceBankingVO> list);

    // 6. 일괄 삭제 (Replace용)
    int deleteInvoiceBankings(@Param("invoiceSeq") String invoiceSeq, @Param("officeSeq") String officeSeq);
    /* =========================================================================
     * [탭 3] 실적 분배 (utb_invoice_performance)
     * ========================================================================= */
    // 1. 목록 조회
    List<InvoicePerformanceVO> selectPerformanceList(@Param("invoiceSeq") String invoiceSeq, @Param("officeSeq") String officeSeq,@Param("offSet") int offSet,@Param("pageSize")  int pageSize);

    // 2. 단건 조회
    Optional<InvoicePerformanceVO> selectPerformanceDetail(@Param("performanceSeq") String performanceSeq, @Param("officeSeq") String officeSeq);

    // 3. 단건 등록
    int insertPerformance(InvoicePerformanceVO vo);

    // 4. 단건 수정
    int updatePerformance(InvoicePerformanceVO vo);

    // 5. 일괄 등록 (Batch)
    int insertPerformanceBatch(@Param("list") List<InvoicePerformanceVO> list);

    // 6. 일괄 삭제 (Replace용)
    int deletePerformances(@Param("invoiceSeq") String invoiceSeq, @Param("officeSeq") String officeSeq);





    List<InvoiceMergeVO>
    findAppDetailList(@Param("request") BaseSearchRequest request);

    List<InvoiceMergeVO> selectInvoiceListByCase(@Param("request") BaseSearchRequest request);

    int selectInvoiceListByCaseCount(@Param("request") BaseSearchRequest request);

    /* =========================================================================
     * 삭제 및 일괄 삭제
     * ========================================================================= */
    /** 단건 삭제 */
    int deleteInvoiceMst(@Param("invoiceSeq") String invoiceSeq, @Param("officeSeq") String officeSeq, @Param("updateUser") String updateUser);
    int deleteInvoiceClaim(@Param("claimSeq") String claimSeq, @Param("officeSeq") String officeSeq, @Param("updateUser") String updateUser);
    int deleteInvoiceBanking(@Param("bankingSeq") String bankingSeq, @Param("officeSeq") String officeSeq, @Param("updateUser") String updateUser);
    int deleteInvoicePerformance(@Param("performanceSeq") String performanceSeq, @Param("officeSeq") String officeSeq, @Param("updateUser") String updateUser);

    /** 일괄 삭제 */
    int deleteInvoiceList(@Param("officeSeq") String officeSeq, @Param("invoiceSeqs") List<String> invoiceSeqs, @Param("updateUser") String updateUser);
    int deleteClaimList(@Param("officeSeq") String officeSeq, @Param("claimSeqs") List<String> claimSeqs, @Param("updateUser") String updateUser);
    int deleteBankingList(@Param("officeSeq") String officeSeq, @Param("bankingSeqs") List<String> bankingSeqs, @Param("updateUser") String updateUser);
    int deletePerformanceList(@Param("officeSeq") String officeSeq, @Param("performanceSeqs") List<String> performanceSeqs, @Param("updateUser") String updateUser);
}

