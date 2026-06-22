package kr.co.mindpro.ipms.domain.patentApp.appCommon.util;

import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import org.springframework.util.StringUtils;

/**
 * @author : seokho
 * @fileName : AppStatusUtil.java
 * @since : 2026. 4. 21.
 */
public class AppStatusUtil {

    /**
     * 출원 데이터의 상태값(app_state)을 비즈니스 룰에 따라 자동 계산하고 업데이트합니다.
     *
     * @param appVO 상태를 검사하고 업데이트할 CommonAppVO 객체
     */
    public static void calculateAndSetAppState(CommonAppVO appVO) {
        // 방어 로직: 객체가 null이면 그대로 리턴
        if (appVO == null) {
            return;
        }

        String currentStatus = appVO.getStatusCode();
        String appStatus = "10"; // 출원 상태값: 10 (출원 신청 진행 중)
        String dashBoardState = "10";    // 대시보드 관리 default 상태값

        boolean hasGiveupDate = StringUtils.hasText(appVO.getAbandonDate());
        boolean hasDeemedWithdrawalDate = StringUtils.hasText(appVO.getDeemedWithdrawalDate());
        boolean hasRegDate    = StringUtils.hasText(appVO.getRegDate());
        boolean hasAppDate   = StringUtils.hasText(appVO.getAppDate());

        // 비즈니스 룰에 따른 상태값 결정 (우선순위: 포기 > 등록 > 출원 > 기본)
        if (hasGiveupDate || hasDeemedWithdrawalDate) {
            appStatus = "80"; // 포기
            dashBoardState = "30"; // 대시보드 포기 상태
        } else if (hasRegDate) {
            appStatus = "70"; // 출원 등록 완료
            dashBoardState = "20"; // 대시보드 등록 완료 상태
        } else if (hasAppDate) {
            appStatus = "20"; // 출원 중
        }

        // 기존 상태와 다를 경우에만 업데이트 수행
        // currentStatus가 null일 수도 있으므로, newStatus.equals() 방향으로 비교해야 안전
        if (!appStatus.equals(currentStatus) && !dashBoardState.equals(currentStatus)) {
            appVO.setStatusCode(appStatus);
            appVO.setStateCode(dashBoardState);
        }

        // DB/응답에 statusName이 비어 있거나 코드와 동일하게만 들어오는 경우 UI용 라벨 보강
        syncStatusDisplayName(appVO);
    }

    /** APP_STATE 코드에 대응하는 표시명 (CM 미조회·구 그룹코드 불일치 등 대비) */
    private static void syncStatusDisplayName(CommonAppVO appVO) {
        if (appVO == null || !StringUtils.hasText(appVO.getStatusCode())) {
            return;
        }
        String code = appVO.getStatusCode().trim();
        String label = resolveAppStateLabel(code);

        String name = appVO.getStatusName();
        if (!StringUtils.hasText(name) || code.equals(name.trim())) {
            appVO.setStatusName(label);
        }
    }

    private static String resolveAppStateLabel(String statusCode) {
        if (!StringUtils.hasText(statusCode)) {
            return "";
        }
        return switch (statusCode.trim()) {
            case "10" -> "출원 신청 진행 중";
            case "20" -> "출원 중";
            case "70" -> "등록 완료";
            case "80" -> "포기";
            default -> statusCode;
        };
    }
}
