package kr.co.mindpro.ipms.domain.incident.service;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.incident.dto.response.IncidentResponse;

import java.util.List;

public interface IncidentService {

    /** 업무 시퀀스(tblSeq)별 청구 내역 조회 */
    BaseSearchResponse<IncidentResponse.IncidentClaimDetail> getClaimsByTblSeq(String tblSeq);

    /** 청구서 삭제 (소프트 삭제) */
    int deleteClaims(List<String> invoiceSeqs);

    /** 청구서 완전 삭제 (하드 삭제) */
    int deleteClaimsHard(List<String> invoiceSeqs);
}
