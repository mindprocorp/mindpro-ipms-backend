package kr.co.mindpro.ipms.domain.history.usage.service.impl;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.history.usage.repository.db1.UsageHistoryMapper;
import kr.co.mindpro.ipms.domain.history.usage.service.UsageHistoryService;
import kr.co.mindpro.ipms.domain.history.usage.dto.response.UsageHistoryResponse;
import kr.co.mindpro.ipms.domain.history.usage.vo.UsageHistoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : seokho
 * @fileName : UsageHistoryServiceImpl.java
 * @since : 2026. 4. 7.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsageHistoryServiceImpl implements UsageHistoryService {

    private final UsageHistoryMapper usageHistoryMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertUsageHistory(UsageHistoryVO vo) {

        try {
            vo.setOfficeSeq(SecurityUtil.getOfficeSeq());

            usageHistoryMapper.insertUsageHistory(vo);
        } catch (Exception e) {
            log.error(">>> [ERROR] 사용 이력 저장 실패! (데이터는 롤백 안 됨): {}", e.getMessage());
        }

    }

    @Override
    public BaseSearchResponse<UsageHistoryResponse> getUsageHistoryList(BaseSearchRequest req) {
        // 사무소 식별자 세팅.
        req.setOfficeSeq(SecurityUtil.getOfficeSeq());

        // 전체 카운트 조회
        int totalCount = usageHistoryMapper.selectUsageHistoryTotalCount(req);

        // 리스트 조회
        List<UsageHistoryResponse> list = usageHistoryMapper.selectUsageHistoryList(req);

        return BaseSearchResponse.of(list, totalCount, req.getPage(), req.getPageSize());
    }
}
