package kr.co.mindpro.ipms.domain.memo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.memo.dto.request.MemoRequest;

import kr.co.mindpro.ipms.domain.memo.dto.response.MemoResponse;
import kr.co.mindpro.ipms.domain.memo.service.MemoService;
import kr.co.mindpro.ipms.domain.memo.vo.MemoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [Controller] 이의심판 API
 *
 * @author	 : min
 * @fileName : MemoController.java
 * @since	 : 2026. 01. 07.
 */
@Slf4j
@Tag(name = "메모 API", description = "메모 CRUD API")
@RestController
@RequestMapping("/api/Memo")
@RequiredArgsConstructor
public class MemoController {
    private final MemoService memoService;

    /**
     * 메모 저장 (단건)
     * Conflict의 /result 저장 방식과 동일한 규격
     */
    @Operation(summary = "메모 저장", description = "메모 정보를 등록합니다. (Mst+Mapp 일괄처리)")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MemoResponse.MemoDetail>> saveMemo(
            @RequestPart(value = "data") MemoRequest.MemoDetail request,
            @Parameter(description = "메모 첨부파일") @RequestPart(value = "files", required = false) List<MultipartFile> files) {


        MemoResponse.MemoDetail result = memoService.registerMemo(request, files);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "메모가 저장되었습니다.", result));
    }

    /**
     * 업무별 메모 목록 조회
     * DueDate의 findAllByWork와 동일한 구조
     */
    @Operation(summary = "메모 목록 조회", description = "특정 업무(tblSeq)에 등록된 모든 메모를 조회합니다.")
    @GetMapping("/list/{tblSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<MemoResponse.MemoDetail>>> getMemoList(@PathVariable String tblSeq) {


        BaseSearchResponse<MemoResponse.MemoDetail> list = memoService.getMemoListByWork(tblSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "메모 목록 조회 성공", list)
        );
    }

    /**
     * 메모 상세 조회 (단건)
     * memoSeq를 기준으로 특정 메모의 상세 정보를 조회합니다.
     */
    @Operation(summary = "메모 상세 조회", description = "메모 일련번호(memoSeq)를 통해 상세 정보를 조회합니다.")
    @GetMapping("/{memoSeq}")
    public ResponseEntity<ApiResponse<MemoResponse.MemoDetail>> getMemoDetail(@PathVariable String memoSeq) {

        log.debug("메모 상세 조회 요청 - memoSeq: {}", memoSeq);

        MemoResponse.MemoDetail result = memoService.getMemoDetail(memoSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "메모 상세 조회 성공", result)
        );
    }

    /**
     * [삭제] 메모 논리적 삭제
     * */
    @Operation(summary = "메모 논리적 삭제", description = "메모 일련번호(memoSeq)를 통해 메모를 논리적으로 삭제합니다.")
    @DeleteMapping("/delete/soft/{tblSeq}/{memoSeq}")
    public ResponseEntity<ApiResponse<Void>> softDeleteMemo(@PathVariable String tblSeq, @PathVariable String memoSeq) {

        memoService.softDeleteMemo(tblSeq, memoSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "메모 논리적 삭제 성공", null)
        );
    }

    /**
     * [삭제] 메모 다건 논리적 삭제
     * */
    @Operation(summary = "메모 다건 논리적 삭제", description = "메모 다건 일련번호(memoSeq)를 통해 메모를 논리적으로 삭제합니다.")
    @DeleteMapping("/multi-delete/soft/{tblSeq}")
    public ResponseEntity<ApiResponse<Void>> multiSoftDeleteMemo(@PathVariable String tblSeq, @RequestBody List<String> memoSeqList) {

        memoService.softDeleteMemoByList(tblSeq, memoSeqList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "메모 다건 논리적 삭제 성공", null)
        );
    }

    /**
     * [삭제] 메모 물리적 삭제
     * */
    @Operation(summary = "메모 논리적 삭제", description = "메모 일련번호(memoSeq)를 통해 메모를 논리적으로 삭제합니다.")
    @DeleteMapping("/delete/hard/{tblSeq}/{memoSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteMemo(@PathVariable String tblSeq, @PathVariable String memoSeq) {

        memoService.hardDeleteMemo(tblSeq, memoSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "메모 논리적 삭제 성공", null)
        );
    }

    /**
     * [삭제] 메모 다건 논리적 삭제
     * */
    @Operation(summary = "메모 다건 논리적 삭제", description = "메모 다건 일련번호(memoSeq)를 통해 메모를 논리적으로 삭제합니다.")
    @DeleteMapping("/multi-delete/hard/{tblSeq}")
    public ResponseEntity<ApiResponse<Void>> multiHardDeleteMemo(@PathVariable String tblSeq, @RequestBody List<String> memoSeqList) {

        memoService.hardDeleteMemoByList(tblSeq, memoSeqList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "메모 다건 논리적 삭제 성공", null)
        );
    }
}