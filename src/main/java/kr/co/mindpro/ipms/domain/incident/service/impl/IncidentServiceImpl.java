package kr.co.mindpro.ipms.domain.incident.service.impl;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.incident.dto.response.IncidentResponse;
import kr.co.mindpro.ipms.domain.incident.repository.db1.IncidentMapper;
import kr.co.mindpro.ipms.domain.incident.service.IncidentService;
import kr.co.mindpro.ipms.domain.invoice.vo.InvoiceMergeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IncidentServiceImpl implements IncidentService {

    private final IncidentMapper incidentMapper;

    @Override
    public BaseSearchResponse<IncidentResponse.IncidentClaimDetail> getClaimsByTblSeq(String tblSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        List<InvoiceMergeVO> list = incidentMapper.selectClaimsByTblSeq(tblSeq, officeSeq);

        List<IncidentResponse.IncidentClaimDetail> dtoList = list.stream()
                .map(vo -> IncidentResponse.IncidentClaimDetail.builder()
                        .invoiceSeq(vo.getInvoiceSeq())
                        .costCategory(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getInvCategoryCode())
                                .codeName(vo.getInvCategoryName())
                                .build())
                        .itemContent(vo.getInvContent())
                        .note(vo.getNote())
                        .invNo(vo.getInvNo())
                        .invDate(vo.getInvDate())
                        .govFee(vo.getGovFee())
                        .agencyFee(vo.getAgencyFee())
                        .vat(vo.getVat())
                        .etcFee(vo.getEtcFee())
                        .transFee(vo.getTransFee())
                        .totalAmount(vo.getTotalInvAmount())
                        .depAmount(vo.getDepAmount())
                        .unpaidAmount(vo.getUnpaidAmount())
                        .abandonAmount(vo.getAbandonAmount())
                        .taxBillDate(vo.getTaxBillDate())
                        .inOutType(vo.getInOutType())
                        .build())
                .collect(Collectors.toList());

        return BaseSearchResponse.of(dtoList, 1, dtoList.size());
    }

    @Override
    @Transactional
    public int deleteClaims(List<String> invoiceSeqs) {
        if (invoiceSeqs == null || invoiceSeqs.isEmpty()) return 0;
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 상세 및 마스터 소프트 삭제
        incidentMapper.updateClaimDeleteYnByInvoice(invoiceSeqs, officeSeq, loginUser);
        return incidentMapper.updateInvoiceDeleteYn(invoiceSeqs, officeSeq, loginUser);
    }

    @Override
    @Transactional
    public int deleteClaimsHard(List<String> invoiceSeqs) {
        if (invoiceSeqs == null || invoiceSeqs.isEmpty()) return 0;
        String officeSeq = SecurityUtil.getOfficeSeq();

        // 상세 및 마스터 하드 삭제
        incidentMapper.deleteInvoiceClaimHard(invoiceSeqs, officeSeq);
        return incidentMapper.deleteInvoiceMstHard(invoiceSeqs, officeSeq);
    }
}
