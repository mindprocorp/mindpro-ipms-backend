package kr.co.mindpro.ipms.common.exception;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;


/**
 * 클라이언트에게 보여줄 일관된 JSON 규격
 *
 * @author	 : mindpro
 * @fileName	 : ErrorResponse.java
 * @since	 : 2025. 12. 24.
 */
@Getter
@Builder
public class ErrorResponse {
	private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String code;
    private final String message;

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message(message)
                .build();
    }
}