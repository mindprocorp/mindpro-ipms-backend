package kr.co.mindpro.ipms.domain.incident.repository.db1;

import kr.co.mindpro.ipms.domain.invoice.vo.InvoiceMergeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IncidentMapper {

    /** 업무 시퀀스(tblSeq)별 청구 내역 조회 */
    List<InvoiceMergeVO> selectClaimsByTblSeq(@Param("tblSeq") String tblSeq, @Param("officeSeq") String officeSeq);

    /** 청구서 소프트 삭제 (del_yn = 'Y') */
    int updateInvoiceDeleteYn(@Param("invoiceSeqs") List<String> invoiceSeqs, @Param("officeSeq") String officeSeq, @Param("loginUser") String loginUser);

    /** 청구 상세 소프트 삭제 */
    int updateClaimDeleteYnByInvoice(@Param("invoiceSeqs") List<String> invoiceSeqs, @Param("officeSeq") String officeSeq, @Param("loginUser") String loginUser);

    /** 청구서 하드 삭제 */
    int deleteInvoiceMstHard(@Param("invoiceSeqs") List<String> invoiceSeqs, @Param("officeSeq") String officeSeq);

    /** 청구 상세 하드 삭제 */
    int deleteInvoiceClaimHard(@Param("invoiceSeqs") List<String> invoiceSeqs, @Param("officeSeq") String officeSeq);
}
