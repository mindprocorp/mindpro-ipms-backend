package kr.co.mindpro.ipms.domain.jobprogress.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.jobprogress.dto.request.JobProgressRequest;
import kr.co.mindpro.ipms.domain.jobprogress.dto.response.JobProgressResponse;
import kr.co.mindpro.ipms.domain.jobprogress.service.JobProgressService;
import kr.co.mindpro.ipms.domain.jobprogress.vo.JobProgressVO;
import kr.co.mindpro.ipms.domain.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [Controller] 이의심판 API
 *
 * @author	 : min
 * @fileName : ParticipantController.java
 * @since	 : 2026. 01. 07.
 */
@Slf4j
@Tag(name = "진행사항 API", description = "진행사항 CRUD API")
@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class JobProgressController {

    private final JobProgressService jobProgressService;

    @Operation(
            summary = "업무별 진행사항 리스트 조회",
            description = "tblSeq를 기준으로 해당 업무의 진행사항 목록을 조회한다.")
    @GetMapping("progress/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<JobProgressResponse.JobProgressDetail>>> getProgressList(BaseSearchRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "진행사항 리스트 조회 성공",
                        jobProgressService.getProgressList(request)
                )
        );
    }

    @Operation(summary = "진행사항 저장 (등록/수정)")
    @PostMapping(value = "progress/register", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<JobProgressResponse.JobProgressDetail>> registerProgress(
            @RequestPart("data") @Valid JobProgressRequest.JobProgressDetail vo,
            @RequestPart(value = "targetFiles", required = false) List<MultipartFile> targetFiles
    ) {
        // 저장 후 결과 데이터 수신
        JobProgressResponse.JobProgressDetail result = jobProgressService.registerProgress(vo, targetFiles);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "진행사항 저장이 완료되었습니다.",
                        result  // 조회된 상세 데이터 반환
                ));
    }

    @Operation(summary = "진행사항 삭제(논리적)")
    @DeleteMapping("progress/delete/soft/{tblSeq}/{progressSeq}")
    public ResponseEntity<ApiResponse<Void>> softDeleteProgress(
            @PathVariable("tblSeq") String tblSeq,
            @PathVariable("progressSeq") String progressSeq
    ) {
        jobProgressService.softDeleteProgress(tblSeq, progressSeq);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "진행사항 단건 논리적 삭제 완료")
        );
    }

    @Operation(summary = "진행사항 다건 삭제(논리적)")
    @DeleteMapping("progress/multi-delete/soft/{tblSeq}")
    public ResponseEntity<ApiResponse<Void>> multiSoftDeleteProgress(
            @PathVariable String tblSeq,
            @RequestBody List<String> progressSeqList
    ) {
        jobProgressService.softDeleteProgressByList(tblSeq, progressSeqList);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "진행사항 다건 논리적 삭제 완료")
        );
    }

    @Operation(summary = "진행사항 삭제(물리적)")
    @DeleteMapping("progress/delete/hard/{tblSeq}/{progressSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteProgress(
            @PathVariable String tblSeq,
            @PathVariable String progressSeq
    ) {
        jobProgressService.hardDeleteProgress(tblSeq, progressSeq);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "진행사항 단건 물리적 삭제 완료")
        );
    }

    @Operation(summary = "진행사항 다건 삭제(물리적)")
    @DeleteMapping("progress/multi-delete/hard/{tblSeq}")
    public ResponseEntity<ApiResponse<Void>> multiHardDeleteProgress(
            @PathVariable String tblSeq,
            @RequestBody List<String> progressSeqList
    ) {
        jobProgressService.hardDeleteProgressByList(tblSeq, progressSeqList);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "진행사항 다건 물리적 삭제 완료")
        );
    }

    @Operation(
            summary = "진행사항 단건 상세 조회",
            description = "진행사항 시퀀스(progressSeq)를 기준으로 상세 내역을 조회한다.")
    @GetMapping("progress/detail/{progressSeq}")
    public ResponseEntity<ApiResponse<JobProgressResponse.JobProgressDetail>> getProgressDetail(
            @PathVariable("progressSeq") String progressSeq
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "진행사항 상세 조회 성공",
                        jobProgressService.getProgressDetail(progressSeq)
                )
        );
    }


}