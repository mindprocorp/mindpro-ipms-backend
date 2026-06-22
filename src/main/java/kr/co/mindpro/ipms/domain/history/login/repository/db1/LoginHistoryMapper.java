package kr.co.mindpro.ipms.domain.history.login.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.history.login.dto.response.LoginHistoryResponse;
import kr.co.mindpro.ipms.domain.history.login.vo.LoginHistoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : seokho
 * @fileName : LoginHistoryMapper.java
 * @since : 2026. 4. 7.
 */
@Mapper
public interface LoginHistoryMapper {

    /**
     * [저장] 로그인 이력 저장.
     * */
    void insertLoginHistory(LoginHistoryVO vo);

    List<LoginHistoryResponse> selectLoginHistoryList(BaseSearchRequest request);

    int selectLoginHistoryTotalCount(BaseSearchRequest request);
}
