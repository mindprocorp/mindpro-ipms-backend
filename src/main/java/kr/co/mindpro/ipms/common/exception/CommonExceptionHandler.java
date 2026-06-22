package kr.co.mindpro.ipms.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * 모든 예외를 가로채서 JSON으로 변환
 *
 * @author	 : mindpro
 * @fileName	 : CommonExceptionHandler.java
 * @since	 : 2025. 12. 24.
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    /**
     * 비즈니스 로직 중 발생하는 커스텀 예외 처리
     * 서비스 계층에서 throw new BusinessException(ErrorCode.xxx) 호출 시 작동
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException: code={}, message={}", e.getErrorCode().getCode(), e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(ErrorResponse.of(errorCode, e.getMessage()), errorCode.getStatus());
    }

    /**
     * Spring Security 권한 부족 예외 처리 (@PreAuthorize 실패 시)
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException: {}", e.getMessage());
        ErrorCode errorCode = ErrorCode.INSUFFICIENT_PERMISSION; 
        return new ResponseEntity<>(ErrorResponse.of(errorCode), errorCode.getStatus());
    }    

    /**
     * Spring Security 인증 실패 예외 처리 (BadCredentialsException)
     */
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.error("BadCredentialsException: {}", e.getMessage());
        ErrorCode errorCode = ErrorCode.AUTH_FAILED;
        return new ResponseEntity<>(ErrorResponse.of(errorCode), errorCode.getStatus());
    }

    /**
     * 그 외 예상치 못한 모든 최상위 예외 처리
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unhandled Exception: ", e);
        
        // ErrorCode의 기본 메시지 대신, 실제 발생한 예외 메시지(e.getMessage())를 담습니다.
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value())
                        .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                        .message(e.getMessage()) // <-- 이 부분을 수정
                        .build(), 
                ErrorCode.INTERNAL_SERVER_ERROR.getStatus()
        );
    }    
}
