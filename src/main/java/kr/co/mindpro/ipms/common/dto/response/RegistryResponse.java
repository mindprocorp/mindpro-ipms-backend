package kr.co.mindpro.ipms.common.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시스템 등록 정보 공통 응답")
public record RegistryResponse(
    @Schema(description = "코드 ID", example = "PGOKR20260000001") String id,
    @Schema(description = "표시 명칭", example = "테스트사무소") String label,
    @Schema(description = "추가 메타데이터") Object attributes
) {}