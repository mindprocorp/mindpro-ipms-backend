package kr.co.mindpro.ipms.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * [Enum] 에러 코드 관리 클래스
 * 모든 비즈니스 예외에 대한 상태 코드, 에러 코드, 메시지를 한곳에서 관리합니다.
 *
 * @author   : mindpro
 * @fileName : ErrorCode.java
 * @since    : 2025. 12. 24.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // --- 공통 에러 (Common / C) ---
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "올바르지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "지원하지 않는 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 내부 오류가 발생했습니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "C004", "이미 존재하는 리소스입니다."), // UserServiceImpl 등에서 공통으로 사용
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C005", "요청하신 데이터를 찾을 수 없습니다."), // 범용 NOT_FOUND
    
    // --- 인증/권한 에러 (Auth / A) ---
    AUTH_FAILED(HttpStatus.UNAUTHORIZED, "A001", "아이디 또는 비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "만료된 토큰입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A004", "접근 권한이 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "A005", "현재 비밀번호가 일치하지 않습니다."),
    INSUFFICIENT_PERMISSION(HttpStatus.FORBIDDEN, "A006", "해당 작업을 수행할 권한이 부족합니다."),    

    // --- 사용자 에러 (User / U) ---
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "존재하지 않는 사용자입니다."),
    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "U002", "이미 사용 중인 아이디입니다."),

    // --- 파일 에러 (File / F) ---
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "F001", "파일 업로드 중 오류가 발생했습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "F002", "파일을 찾을 수 없습니다."),
    REPOSITORY_NOT_FOUND(HttpStatus.NOT_FOUND, "F003", "유효한 파일 저장소 정보를 찾을 수 없습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "F004", "허용되지 않는 파일 형식입니다."),
	
    // --- 레지스트리 에러 (Registry / R) ---
    INVALID_REGISTRY_TYPE(HttpStatus.BAD_REQUEST, "R001", "지원하지 않는 레지스트리 타입입니다.");
	
    private final HttpStatus status;
    private final String code;
    private final String message;
}