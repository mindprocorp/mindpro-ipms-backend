package kr.co.mindpro.ipms.domain.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "순서 변경 요청")
public record ReorderRequest(
    @Schema(description = "대상 타입 (MENU, OFFICE_CODE, DEPT)")
    String target,
    @Schema(description = "정렬된 PK 목록 (순서대로)")
    List<String> orderedSeqs
) {}
