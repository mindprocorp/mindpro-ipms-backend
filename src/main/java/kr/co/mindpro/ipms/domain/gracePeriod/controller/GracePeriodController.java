package kr.co.mindpro.ipms.domain.gracePeriod.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.gracePeriod.dto.request.GracePeriodRequest;
import kr.co.mindpro.ipms.domain.gracePeriod.dto.response.GracePeriodResponse;
import kr.co.mindpro.ipms.domain.gracePeriod.service.GracePeriodService;
import kr.co.mindpro.ipms.domain.gracePeriod.vo.GracePeriodVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : seokho
 * @fileName : GracePeriodController.java
 * @since : 2026. 2. 3.
 */
@Slf4j
@Tag(name = "공지예외 API", description = "공지예외 CRUD API")
@RestController
@RequestMapping("/api/gracePeriod")
@RequiredArgsConstructor
public class GracePeriodController {

    private final GracePeriodService gracePeriodService;

    /**
     * [저장] 공지예외 단건 등록
     * 특정 시점에 발생하는 단일 공지예외 항목을 등록하기 위한 API입니다.
     */
    @Operation(summary = "공지예외 단건 등록", description = "개별 공지예외 정보를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> insertGracePeriod(@Valid @RequestBody GracePeriodRequest.SaveRequest request) {

        gracePeriodService.registerGracePeriod(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "공지예외 단건 등록이 완료되었습니다."));
    }

    /**
     * [조회] 공지예외 리스트 조회
     * 출원 seq와 연결된 공지예외 리스트를 조회하는 API 입니다.
     * */
    @Operation(summary = "공지예외 리스트 조회", description = "출원에 연결된 공지예외 리스트를 조회합니다.")
    @GetMapping("/list/{appSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<GracePeriodResponse.DetailResponse>>> getGracePeriodListByWork(@PathVariable @Parameter(description = "연결된 출원 seq", example = "APPMST20260000424") String appSeq) {

        BaseSearchResponse<GracePeriodResponse.DetailResponse> res = gracePeriodService.getGracePeriodListByWork(appSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "공지예외 리스트 조회 성공", res));
    }

    /**
     * [조회] 공지예외 단건 상세조회
     * */
    @Operation(summary = "공지예외 단건 상세조회", description = "출원에 연결된 공지예외 단건 상세조회를 조회합니다.")
    @GetMapping("/{appSeq}/{gracePeriodSeq}")
    public ResponseEntity<ApiResponse<GracePeriodResponse.DetailResponse>> getGracePeriodDetail(@PathVariable @Parameter(description = "연결된 출원 seq", example = "APPMST20260000424") String appSeq, @PathVariable @Parameter(description = "공지예외 seq") String gracePeriodSeq) {

        GracePeriodResponse.DetailResponse res = gracePeriodService.getGracePeriodDetail(appSeq, gracePeriodSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "공지예외 리스트 조회 성공", res));
    }

    /**
     * [삭제] 공지예외 단건 논리적 삭제
     * */
    @Operation(summary = "공지예외 단건 논리적 삭제", description = "공지예외 단건 논리적 삭제 API")
    @DeleteMapping("/delete/soft/{appSeq}/{gracePeriodSeq}")
    public ResponseEntity<ApiResponse<Void>> softDeleteGracePeriod(@PathVariable String appSeq, @PathVariable String gracePeriodSeq) {

        gracePeriodService.softDeleteGracePeriod(appSeq, gracePeriodSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "공지예외 단건 논리적 삭제 성공", null));
    }

    /**
     * [삭제] 공지예외 다건 논리적 삭제
     * */
    @Operation(summary = "공지예외 다건 논리적 삭제", description = "공지예외 다건 논리적 삭제 API")
    @DeleteMapping("/multi-delete/soft/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiSoftDeleteGracePeriodByList(@PathVariable String appSeq, @RequestBody List<String> gracePeriodSeqList) {

        gracePeriodService.softDeleteGracePeriodByList(appSeq, gracePeriodSeqList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "공지예외 다건 논리적 삭제 성공", null));
    }

    /**
     * [삭제] 공지예외 단건 물리적 삭제
     * */
    @Operation(summary = "공지예외 단건 물리적 삭제", description = "공지예외 단건 물리적 삭제 API")
    @DeleteMapping("/delete/hard/{appSeq}/{gracePeriodSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteGracePeriod(@PathVariable String appSeq, @PathVariable String gracePeriodSeq) {

        gracePeriodService.hardDeleteGracePeriod(appSeq, gracePeriodSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "공지예외 단건 물리적 삭제 성공", null));
    }

    /**
     * [삭제] 공지예외 다건 물리적 삭제
     * */
    @Operation(summary = "공지예외 다건 물리적 삭제", description = "공지예외 다건 물리적 삭제 API")
    @DeleteMapping("/multi-delete/hard/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiHardDeleteGracePeriodByList(@PathVariable String appSeq, @RequestBody List<String> gracePeriodSeqList) {

        gracePeriodService.hardDeleteGracePeriodByList(appSeq, gracePeriodSeqList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "공지예외 다건 물리적 삭제 성공", null));
    }
}
