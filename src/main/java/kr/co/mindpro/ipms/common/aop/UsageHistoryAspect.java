package kr.co.mindpro.ipms.common.aop;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.mindpro.ipms.common.util.ClientIpUtil;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.history.usage.service.UsageHistoryService;
import kr.co.mindpro.ipms.domain.history.usage.vo.UsageHistoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * @author : mindpro
 * @fileName : UsageHistoryAspect.java
 * @since : 2026. 4. 7.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UsageHistoryAspect {

    private final UsageHistoryService usageHistoryService;

    /**
     * @Operation (스웨거 어노테이션)이 붙은 모든 API 실행을 가로챈다
     */
    @Around("@annotation(io.swagger.v3.oas.annotations.Operation)" +
            "&& within(kr.co.mindpro.ipms.domain..*)" +
            "&& !within(kr.co.mindpro.ipms.domain.auth..*)" +
            "&& !within(kr.co.mindpro.ipms.domain.code..*)" +
            "&& !within(kr.co.mindpro.ipms.domain.duedate..*)" +
            "&& !within(kr.co.mindpro.ipms.domain.example..*)" +
            "&& !within(kr.co.mindpro.ipms.domain.user..*)" )
    public Object logUsageHistory(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;
        String note = null;

        // 1. 메인 비즈니스 로직(컨트롤러)을 먼저 실행한다.
        try {
            result = joinPoint.proceed();
            note = "정상 처리"; // 에러 없이 통과하면 정상 처리
        } catch (Exception e) {
            note = "에러 발생: " + e.getMessage(); // 에러 터지면 에러 내용 기록
            throw e; //GlobalExceptionHandler가 처리하게 다시 던져줌
        } finally {
            // 2. 비즈니스 로직이 성공하든, 예외를 뱉든 무조건 finally에서 이력을 저장
            try {
                saveHistoryAsync(joinPoint, note);
            } catch (Exception ex) {
                log.error(">>> [Usage History AOP] 사용 이력 수집 중 자체 에러 발생: {}", ex.getMessage());
            }
        }

        return result;
    }

    /**
     * 실질적인 데이터 파싱 및 저장 로직
     */
    private void saveHistoryAsync(ProceedingJoinPoint joinPoint, String note) {

        // [1] Request 객체 꺼내기 (AOP 환경이라 RequestContextHolder 사용)
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return; // 혹시라도 스케줄러 같은 비-웹 환경에서 돌면 패스
        HttpServletRequest request = attributes.getRequest();

        // [2] 타겟 메서드 시그니처 및 스웨거 어노테이션 파싱
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String menuName = "미지정";
        String actionName = "미지정";

        Tag tagAnnotation = joinPoint.getTarget().getClass().getAnnotation(Tag.class);
        if (tagAnnotation != null) {
            menuName = tagAnnotation.name();
        }

        Operation operationAnnotation = method.getAnnotation(Operation.class);
        if (operationAnnotation != null) {
            actionName = operationAnnotation.summary(); // 스웨거 summary를 행위명으로
        }

        if ("사용이력(Usage History)".equals(menuName) && "리스트 검색 조회".equals(actionName)) {
            log.debug(">>> [Usage History] '사용 이력 리스트' 조회는 기록하지 않습니다.");
            return;
        }

        // [3] 도메인 공통 Prefix 자르기
        String fullClassName = joinPoint.getTarget().getClass().getName();
        String targetClass = fullClassName.replace("kr.co.mindpro.ipms.", "");
        String targetMethod = method.getName();

        // [4] 접속자 HTTP 정보 수집
        String clientIp = ClientIpUtil.getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String reqUrl = request.getRequestURI();
        String reqMethod = request.getMethod();

        // [5] 보안 컨텍스트에서 유저 시퀀스와 사무소 시퀀스 추출
        String userMstSeq = SecurityUtil.getUserMstSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();

        // 혹시 모를 비회원(Null) 방어 코드 (유틸 내부 로직에 따라 선택)
        if (userMstSeq == null) userMstSeq = "ANONYMOUS";
        if (officeSeq == null) officeSeq = "SYSTEM";

        // [6] VO 빌더 조립
        UsageHistoryVO vo = UsageHistoryVO.builder()
                .userMstSeq(userMstSeq)
                .officeSeq(officeSeq)
                .menuName(menuName)
                .actionName(actionName)
                .reqUrl(reqUrl)
                .reqMethod(reqMethod)
                .clientIp(clientIp)
                .userAgent(userAgent)
                .targetClass(targetClass)
                .targetMethod(targetMethod)
                .note(note)
                .build();

        // [7] 서비스 호출 (여기서 REQUIRES_NEW로 안전)
        usageHistoryService.insertUsageHistory(vo);
    }
}
