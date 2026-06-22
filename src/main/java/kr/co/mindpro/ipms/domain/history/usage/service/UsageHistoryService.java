package kr.co.mindpro.ipms.domain.history.usage.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.history.usage.dto.response.UsageHistoryResponse;
import kr.co.mindpro.ipms.domain.history.usage.vo.UsageHistoryVO;

/**
 * @author : seokho
 * @fileName : UsageHistoryService.java
 * @since : 2026. 4. 7.
 */
public interface UsageHistoryService {

    // 1. AOP에서 호출할 사용 이력 저장 로직
    void insertUsageHistory(UsageHistoryVO vo);

    // 2. 프론트엔드 목록 조회용 로직
    BaseSearchResponse<UsageHistoryResponse> getUsageHistoryList(BaseSearchRequest req);
}
