package kr.co.mindpro.ipms.domain.history.usage.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.history.usage.dto.response.UsageHistoryResponse;
import kr.co.mindpro.ipms.domain.history.usage.vo.UsageHistoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : seokho
 * @fileName : UsageHistoryMapper.java
 * @since : 2026. 4. 7.
 */
@Mapper
public interface UsageHistoryMapper {

    // 1. 사용 이력 단건 저장 (AOP에서 비동기나 트랜잭션 분리해서 호출)
    int insertUsageHistory(UsageHistoryVO vo);

    // 2. 사용 이력 리스트 조회 (화면 그리드용)
    List<UsageHistoryResponse> selectUsageHistoryList(BaseSearchRequest req);

    // 3. 사용 이력 전체 카운트 (페이징 계산용)
    int selectUsageHistoryTotalCount(BaseSearchRequest req);
}
