package kr.co.mindpro.ipms.domain.history.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.history.dto.request.HistoryRequest;
import kr.co.mindpro.ipms.domain.history.dto.response.HistoryResponse;

public interface HistoryService {
    BaseSearchResponse<HistoryResponse.HistorySearchListDetail> getHistoryList(BaseSearchRequest request);
    /** 변경 사항 기록 */
    HistoryResponse.ModifiedHistDetail saveHistory(HistoryRequest.ModifiedHistDetail request);

    /** 변경 이력 상세 조회 */
    HistoryResponse.ModifiedHistDetail getHistoryDetail(String modifiedHistSeq);
    <T> void compareAndLog(String tblSeq, String updateType, T oldObj, T newObj);

    void deleteModifiedHist(String modifiedHistSeq);
    void deleteModifiedHistList(java.util.List<String> ids);
}
