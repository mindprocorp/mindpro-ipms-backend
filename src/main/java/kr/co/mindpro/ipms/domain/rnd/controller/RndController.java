package kr.co.mindpro.ipms.domain.rnd.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.rnd.dto.request.RndRequest;
import kr.co.mindpro.ipms.domain.rnd.dto.response.RndResponse;
import kr.co.mindpro.ipms.domain.rnd.service.RndService;
import kr.co.mindpro.ipms.domain.rnd.vo.RndVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : seokho
 * @fileName : RndController.java
 * @since : 2026. 2. 5.
 */
@Slf4j
@Tag(name = "연구과제 API", description = "연구과제 CRUD API")
@RestController
@RequestMapping("/api/rnd")
@RequiredArgsConstructor
public class RndController {

    private final RndService rndService;

    /**
     * [저장] 연구과제 단건 등록
     * 단일 연구과제 항목을 등록하기 위한 API입니다.
     */
    @Operation(summary = "연구과제 단건 등록", description = "개별 연구과제 정보를 등록합니다.")
    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<Void>> saveRnd(@RequestBody RndRequest.RnbRequestDetail request) {

        rndService.saveRnd(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "연구과제 단건 등록이 완료되었습니다."));
    }

    /**
     * [조회] 연구과제 리스트 조회
     * 출원 seq와 연결된 연구과제 리스트를 조회하는 API 입니다.
     * */
    @Operation(summary = "연구과제 리스트 조회", description = "출원에 연결된 연구과제 리스트를 조회합니다.")
    @GetMapping("/list/{appSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<RndResponse.RndResponseDetail>>> getRndListByWork(@PathVariable @Parameter(description = "연결된 출원 seq", example = "PAT20260000005") String appSeq) {

        BaseSearchResponse<RndResponse.RndResponseDetail> res = rndService.getRndList(appSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "공지예외 리스트 조회 성공", res));
    }

    /**
     * [조회] 연구과제 단건 상세조회
     * */
    @Operation(summary = "연구과제 단건 상세조회", description = "출원에 연결된 연구과제 단건 상세조회를 합니다.")
    @GetMapping("/{appSeq}/{rndSeq}")
    public ResponseEntity<ApiResponse<RndResponse.RndResponseDetail>> getRndDetail(@PathVariable @Parameter(description = "연결된 출원 seq", example = "PAT20260000005") String appSeq, @PathVariable @Parameter(description = "연구과제 seq") String rndSeq) {

        RndResponse.RndResponseDetail res = rndService.getRndDetail(appSeq, rndSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "공지예외 단건 상세조회 성공", res));
    }

    /**
     * [삭제] 연구과제 단건 논리적 삭제
     * */
    @Operation(summary = "연구과제 단건 논리적 삭제", description = "연구과제 단건 논리적 삭제를 수행합니다.")
    @DeleteMapping("/delete/soft/{appSeq}/{rndSeq}")
    public ResponseEntity<ApiResponse<Void>> softDeleteRnd(
            @PathVariable @Parameter(description = "삭제할 연구과제 seq") String rndSeq,
            @PathVariable @Parameter(description = "삭제할 연구과제 appSeq") String appSeq) {

        rndService.softDeleteRnd(appSeq, rndSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "연구과제 단건 논리적 삭제가 완료되었습니다."));
    }

    /**
     * [삭제] 연구과제 다건 논리 삭제
     * */
    @Operation(summary = "연구과제 다건 논리 삭제", description = "연구과제 다건 논리적 삭제를 수행합니다.")
    @DeleteMapping("/multi-delete/soft/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiSoftDeleteRnd(
            @PathVariable @Parameter(description = "연결된 연구과제 appSeq") String appSeq,
            @RequestBody List<String> rndSeqList) {

        rndService.softDeleteRndByList(appSeq, rndSeqList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "연구과제 다건 논리 삭제가 완료되었습니다."));
    }

    /**
     * [삭제] 연구과제 단건 물리 삭제
     * */
    @Operation(summary = "연구과제 단건 물리 삭제", description = "연구과제 단건 물리 삭제를 수행합니다.")
    @DeleteMapping("/delete/hard/{appSeq}/{rndSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteRnd(
            @PathVariable @Parameter(description = "삭제할 연구과제 seq") String rndSeq,
            @PathVariable @Parameter(description = "연결된 연구과제 appSeq") String appSeq) {

        rndService.hardDeleteRnd(appSeq, rndSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "연구과제 단건 물리 삭제가 완료되었습니다."));
    }

    /**
     * [삭제] 연구과제 다건 물리 삭제
     * */
    @Operation(summary = "연구과제 다건 물리 삭제", description = "연구과제 다건 물리 삭제를 수행합니다.")
    @DeleteMapping("/multi-delete/hard/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiHardDeleteRnd(
            @PathVariable @Parameter(description = "연결된 연구과제 appSeq") String appSeq,
            @RequestBody List<String> rndSeqList) {

        rndService.hardDeleteRndByList(appSeq, rndSeqList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "연구과제 다건 물리 삭제가 완료되었습니다."));
    }
}
