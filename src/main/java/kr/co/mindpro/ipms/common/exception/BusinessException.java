package kr.co.mindpro.ipms.common.exception;

import lombok.Getter;

/**
 * 공통 커스텀 예외
 *
 * @author	 : mindpro
 * @fileName	 : BusinessException.java
 * @since	 : 2025. 12. 24.
 */
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}