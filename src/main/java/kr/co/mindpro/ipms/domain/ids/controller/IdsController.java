package kr.co.mindpro.ipms.domain.ids.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.ids.dto.request.IdsRequest;
import kr.co.mindpro.ipms.domain.ids.service.IdsService;
import kr.co.mindpro.ipms.domain.ids.vo.IdsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : mindpro
 * @fileName : IdsController.java
 * @since : 2026. 3. 12.
 */
@Slf4j
@Tag(name = "IDS API", description = "IDS 관련 CRUD API")
@RestController
@RequestMapping("/api/ids")
@RequiredArgsConstructor
public class IdsController {

    private final IdsService idsService;

    /**
     * [저장] IDS 단건 등록
     * 특정 시점에 발생하는 단일 IDS 항목을 등록하기 위한 API입니다.
     */
    @Operation(summary = "IDS 단건 등록", description = "개별 IDS 정보를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> insertGracePeriod(@Valid @RequestBody IdsRequest.SaveIdsRequest request) {

        idsService.saveIds(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "IDS 단건 등록이 완료되었습니다."));
    }

    /**
     * [조회] IDS 리스트 조회
     * */
    @Operation(summary = "IDS 리스트 조회", description = "IDS 리스트를 조회합니다.")
    @GetMapping("/list/{appSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<IdsVO>>> getIdsList(@PathVariable String appSeq) {

        BaseSearchResponse<IdsVO> vo = idsService.getIdsList(appSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "IDS 리스트 조회가 완료되었습니다.", vo));
    }

    /**
     * [조회] IDS 단건 상세 조회
     * */
    @Operation(summary = "IDS 단건 상세조회", description = "IDS 단건 상세조회를 합니다.")
    @GetMapping("/{appSeq}/{idsSeq}")
    public ResponseEntity<ApiResponse<IdsVO>> getIdsDetail(@PathVariable String appSeq, @PathVariable String idsSeq) {

        IdsVO vo = idsService.getIdsDetail(appSeq, idsSeq);

        if (vo == null) {
            throw new RuntimeException("요청한 IDS 건이 존재하지 않습니다.");
        }

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "IDS 리스트 조회가 완료되었습니다.", vo));
    }

    /**
     * [삭제] IDS 논리적 삭제
     * */
    @Operation(summary = "IDS 논리적 삭제", description = "IDS 항목을 논리적으로 삭제합니다.")
    @DeleteMapping("/delete/soft/{appSeq}/{idsSeq}")
    public ResponseEntity<ApiResponse<Void>> softDeleteByIdsSeq(@PathVariable String appSeq, @PathVariable String idsSeq) {

        idsService.softDeleteByIdsSeq(appSeq, idsSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "IDS 논리적 삭제가 완료되었습니다."));
    }

    /**
     * [삭제] IDS 다건 논리적 삭제
     * */
    @Operation(summary = "IDS 다건 논리적 삭제", description = "IDS 다건 항목을 논리적으로 삭제합니다.")
    @DeleteMapping("/multi-delete/soft/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiSoftDeleteByIdsSeqList(@PathVariable String appSeq, @RequestBody List<String> idsSeqList) {

        idsService.softDeleteByIdsSeqList(appSeq, idsSeqList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "IDS 논리적 삭제가 완료되었습니다."));
    }

    /**
     * [삭제] IDS 물리적 삭제
     * */
    @Operation(summary = "IDS 물리적 삭제", description = "IDS 항목을 물리적으로 삭제합니다.")
    @DeleteMapping("/delete/hard/{appSeq}/{idsSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteByIdsSeq(@PathVariable String appSeq, @PathVariable String idsSeq) {

        idsService.hardDeleteByIdsSeq(appSeq, idsSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "IDS 물리적 삭제가 완료되었습니다."));
    }

    /**
     * [삭제] IDS 다건 물리적 삭제
     * */
    @Operation(summary = "IDS 다건 물리적 삭제", description = "IDS 다건 항목을 물리적으로 삭제합니다.")
    @DeleteMapping("/multi-delete/hard/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiHardDeleteByIdsSeqList(@PathVariable String appSeq, @RequestBody List<String> idsSeqList) {

        idsService.hardDeleteByIdsSeqList(appSeq, idsSeqList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "IDS 물리적 삭제가 완료되었습니다."));
    }
}
