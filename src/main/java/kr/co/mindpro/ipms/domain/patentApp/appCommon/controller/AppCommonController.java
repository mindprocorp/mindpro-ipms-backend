package kr.co.mindpro.ipms.domain.patentApp.appCommon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.service.AppCommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "공통 출원 API", description = "국내/해외 출원 공통 CRUD API")
@RestController
@RequestMapping("/api/appCommon")
@RequiredArgsConstructor
public class AppCommonController {

    private final AppCommonService appCommonService;

    /**
     * 출원 공통 첨부 이미지 논리 삭제 API
     * DELETE /api/appCommon/file/{appSeq}/{fileSeq}
     */
    @Operation(summary = "출원 공통 - 첨부 이미지 삭제", description = "사시도/상표이미지 등 첨부 이미지를 논리 삭제합니다.")
    @DeleteMapping("/file/{appSeq}/{fileSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteAppFile(
            @PathVariable @Parameter(description = "출원 마스터 Seq", example = "APPMST20260000689") String appSeq,
            @PathVariable @Parameter(description = "삭제할 파일 Seq", example = "FILE20260000001") String fileSeq) {

        appCommonService.deleteAppImageFile(appSeq, fileSeq);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "이미지 삭제가 완료되었습니다."));
    }
}
