package kr.co.mindpro.ipms.common.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * [DTO] 공통 응답 객체 (2025 표준)
 * 모든 API 응답의 가독성과 일관성을 보장합니다.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 직접 생성 제한
@Schema(description = "전역 공통 응답 규격")
public class ApiResponse<T> {
	
    @Schema(description = "HTTP 상태 코드", example = "200")
    private final int status;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private final String message;

    @Schema(description = "결과 데이터 (성공 시에만 포함)")
    private final T data;

    @Schema(description = "응답 발생 시간", example = "2025-12-24T14:40:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    /**
     * 성공 응답 (데이터 미포함)
     * 주로 등록, 수정, 삭제 성공 시 사용합니다.
     */
    public static <T> ApiResponse<T> success(int status, String message) {
        return new ApiResponse<>(status, message, null, LocalDateTime.now());
    }

    /**
     * 성공 응답 (데이터 포함)
     * 주로 단건 조회, 목록 조회 성공 시 사용합니다.
     */
    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(status, message, data, LocalDateTime.now());
    }
}
