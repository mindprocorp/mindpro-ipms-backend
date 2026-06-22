package kr.co.mindpro.ipms.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * [DTO] 로그인 성공 응답 객체
 * 2026년 통합 스키마 반영: userMstSeq 필드 추가
 */
@Builder
@Schema(description = "로그인 성공 응답 데이터")
public record LoginResponse(
    @Schema(description = "Access Token (API 요청 인증용)", example = "eyJhbGciOiJIUzI1NiJ9...")
    String accessToken,

    @Schema(description = "Refresh Token (토큰 재발급용)", example = "defGhi789...")
    String refreshToken,

    @Schema(description = "토큰 타입", example = "Bearer")
    String tokenType,

    @Schema(description = "액세스 토큰 만료 시간 (초 단위)", example = "3600")
    long expiresIn,

    @Schema(description = "사용자 정보 일련번호 (PK)", example = "USERIF20260000001")
    String userInfoSeq,    
    
    @Schema(description = "사용자 마스터 일련번호 (PK)", example = "USERMS20260000001")
    String userMstSeq,
    
    @Schema(description = "소속 사무실 일련번호 (FK)", example = "PGOKR20260000001")
    String officeSeq, // 추가된 필드    

    @Schema(description = "사용자 이름", example = "홍길동")
    String userNm,

    @Schema(description = "사용자 권한", example = "ROLE_USER")
    String userRole
) {}
