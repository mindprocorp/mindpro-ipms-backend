package kr.co.mindpro.ipms.domain.history.login.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.history.login.dto.response.LoginHistoryResponse;
import kr.co.mindpro.ipms.domain.history.login.vo.LoginHistoryVO;

/**
 * @author : seokho
 * @fileName : LoginHistoryService.java
 * @since : 2026. 4. 7.
 */
public interface LoginHistoryService {

    /**
     * 로그인 이력 저장
     * @parem officeSeq 로그인 시도한 사무소 식별키
     * @param userSeq 로그인 시도한 유저 식별키
     * @param ipAddress 접속 IP
     * @param userAgent 접속 환경 (브라우저, OS 등)
     * @param isSuccess 성공 여부 ('Y' or 'N')
     * @param loginType 로그인 유형 (사번, SNS 등)
     * @param note 실패 사유 등 비고
     */
    void recordLoginHistory(String officeSeq, String userSeq, String ipAddress, String category, String userAgent, String isSuccess, String loginType, String note);

    BaseSearchResponse<LoginHistoryResponse> getLoginHistoryList(BaseSearchRequest request);

    void recordLogoutHistory(HttpServletRequest request);
}
