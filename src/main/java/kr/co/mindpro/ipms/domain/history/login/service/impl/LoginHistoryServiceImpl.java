package kr.co.mindpro.ipms.domain.history.login.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.util.ClientIpUtil;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.history.login.repository.db1.LoginHistoryMapper;
import kr.co.mindpro.ipms.domain.history.login.service.LoginHistoryService;
import kr.co.mindpro.ipms.domain.history.login.dto.response.LoginHistoryResponse;
import kr.co.mindpro.ipms.domain.history.login.vo.LoginHistoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : seokho
 * @fileName : LoginHistoryServiceImpl.java
 * @since : 2026. 4. 7.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {

    private final LoginHistoryMapper loginHistoryMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordLoginHistory(String officeSeq, String userSeq, String ipAddress, String category, String userAgent, String isSuccess, String loginType, String note) {
        try {
            LoginHistoryVO historyVO = LoginHistoryVO.builder()
                    .officeSeq(officeSeq)
                    .userMstSeq(userSeq)
//                    .loginHistorySeq(loginHistorySeq) -- DBMS function으로 자동 채번해줌.
                    .category(category)
                    .loginIp(ipAddress)
                    .loginSuccessYn(isSuccess)
                    .loginDeviceType(parseDeviceType(userAgent)) // 디바이스 추출 헬퍼 메서드
                    .loginCountry("KR") // 필요시 GeoIP 연동, 일단 KR 고정
                    .loginType(loginType)
                    .createUser(userSeq)
                    .note(note)
                    .build();

            // 3. Mapper 호출
            loginHistoryMapper.insertLoginHistory(historyVO);

        } catch (Exception e) {
            log.error(">>> [ERROR] 로그인/로그아웃 이력 저장 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    @Override
    public BaseSearchResponse<LoginHistoryResponse> getLoginHistoryList(BaseSearchRequest request) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());

        int totalCount = loginHistoryMapper.selectLoginHistoryTotalCount(request);

        List<LoginHistoryResponse> list = loginHistoryMapper.selectLoginHistoryList(request);

        return BaseSearchResponse.of(list, totalCount, request.getPage(), request.getPageSize());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordLogoutHistory(HttpServletRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userMstSeq = SecurityUtil.getUserMstSeq();

        String userAgent = request.getHeader("User-Agent");
        String clientIp = ClientIpUtil.getClientIp(request);

        recordLoginHistory(officeSeq, userMstSeq, clientIp, "LOGOUT", userAgent, "N", "NORMAL", "로그아웃 성공");

    }

    /**
     * User-Agent 문자열을 파싱해서 대략적인 디바이스 유형을 추출하는 헬퍼 메서드
     * */
    private String parseDeviceType(String userAgent) {
        if (userAgent == null) return "UNKNOWN";
        String ua = userAgent.toUpperCase();
        if (ua.contains("MOBI") || ua.contains("ANDROID") || ua.contains("IPHONE")) {
            return "MOBILE";
        } else if (ua.contains("IPAD") || ua.contains("TABLET")) {
            return "TABLET";
        }
        return "PC";
    }
}
