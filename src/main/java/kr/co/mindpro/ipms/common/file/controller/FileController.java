package kr.co.mindpro.ipms.common.file.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.file.dto.request.FileRequest;
import kr.co.mindpro.ipms.common.file.dto.response.FileResponse;
import kr.co.mindpro.ipms.common.file.service.FileService;
import kr.co.mindpro.ipms.domain.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * [Controller] 파일 관리 API
 * S3 저장소 업로드 및 DB 마스터 연동을 제공합니다.
 */
@Slf4j
@Tag(name = "File API", description = "S3 파일 관리 및 DB 연동 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;
    
    /**
     * 다중 파일 업로드 API (추가)
     * POST /api/v1/files/multiUpload
     */
    @Operation(summary = "다중 파일 업로드", description = "여러 개의 파일을 S3에 업로드하고 DB 정보를 생성합니다.")
    @PostMapping(value = "/multiUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<FileResponse>>> multiUpload(
            @RequestPart("files") 
            @ArraySchema(
                schema = @Schema(type = "string", format = "binary"),
                arraySchema = @Schema(description = "업로드할 파일 리스트")
            ) List<MultipartFile> files,
            @RequestPart(value = "data", required = false) @Valid FileRequest fileRequest,
            @AuthenticationPrincipal UserVO loginUser
    ) {

        String userId = (loginUser != null) ? loginUser.getUserId() : "SYSTEM";
        log.info("API: 다중 파일 업로드 요청 - Count: {}, User: {}", files.size(), userId);

        List<FileResponse> responses = fileService.multiUpload(files, fileRequest, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "다중 파일 업로드 및 DB 등록이 완료되었습니다.", responses));
    }
    
    /**
     * 파일 업로드 API (DB 연동 포함)
     * POST /api/v1/files/upload
     */
    @Operation(summary = "파일 업로드", description = "S3 업로드 및 DB(utb_file_mst) 정보를 생성합니다.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileResponse>> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") FileRequest fileRequest, // JSON 문자열로 수신
            @AuthenticationPrincipal UserVO loginUser) { // Security 연동 기준
        
        String userId = (loginUser != null) ? loginUser.getUserId() : "SYSTEM";
        
        log.info("API: 파일 업로드 요청 - FileName: {}, User: {}", file.getOriginalFilename(), userId);
        
        // 2. 서비스 호출
        FileResponse response = fileService.uploadFile(file, fileRequest, userId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "파일 업로드 및 DB 등록이 완료되었습니다.", response));
    }

    /**
     * 파일 삭제 API (물리/논리 삭제)
     * DELETE /api/v1/files/delete
     */
    @Operation(summary = "파일 삭제", description = "S3 물리 삭제 및 DB 논리 삭제(del_yn='Y')를 수행합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteFile(
            @Parameter(description = "파일 일련번호 (PK)", example = "FILE_20260121_000001")
            @RequestParam("fileSeq") String fileSeq,
            @Parameter(description = "삭제할 파일의 S3 URL", example = "https://...")
            @RequestParam("fileUrl") String fileUrl,
            @AuthenticationPrincipal UserVO loginUser) {
        
        String userId = (loginUser != null) ? loginUser.getUserId() : "SYSTEM";
        log.info("API: 파일 삭제 요청 - Seq: {}, User: {}", fileSeq, userId);
        
        fileService.deleteFile(fileSeq, fileUrl, userId);
        
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "파일이 성공적으로 삭제되었습니다."));
    }

    /**
     * 파일 다운로드 API (Proxy)
     * S3 URL을 전달받아 서버가 스트림으로 내려줍니다. (CORS 우회 및 강제 다운로드)
     */
    @Operation(summary = "파일 다운로드", description = "S3 URL을 통해 서버가 파일을 대신 다운로드하여 브라우저에 스트림으로 전달합니다.")
    @GetMapping("/download")
    public void downloadFile(
            @Parameter(description = "다운로드할 파일의 S3 URL", example = "https://...")
            @RequestParam("fileUrl") String fileUrl,
            HttpServletResponse response) {
        
        log.info("API: 파일 다운로드 프록시 요청 - URL: {}", fileUrl);
        fileService.downloadFile(fileUrl, response);
    }

    /**
     * 파일 목록 조회 API
     */
    @Operation(summary = "파일 목록 조회", description = "특정 경로(Prefix)의 파일 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<FileResponse>>> getFileList(
            @Parameter(description = "조회 경로(예: uploads/UPL/)", example = "uploads/UPL/")
            @RequestParam(value = "prefix", required = false, defaultValue = "") String prefix) {
        
        log.info("API: 파일 목록 조회 요청 - Prefix: {}", prefix);
        List<FileResponse> fileList = fileService.getFileList(prefix);
        
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "파일 목록 조회 성공", fileList));
    }

    /**
     * 다중 파일 ZIP 다운로드 API
     */
    @Operation(summary = "다중 파일 압축 다운로드", description = "여러 파일 시퀀스(fileSeq)를 받아 하나의 ZIP 파일로 다운로드합니다.")
    @PostMapping("/download-zip")
    public void downloadZipFiles(
            @Parameter(description = "다운로드할 파일 시퀀스 리스트")
            @org.springframework.web.bind.annotation.RequestBody java.util.Map<String, List<String>> body,
            HttpServletResponse response) {
        
        List<String> fileSeqs = body.get("fileSeqs");
        log.info("API: 다중 파일 ZIP 다운로드 요청 - Seq count: {}", fileSeqs != null ? fileSeqs.size() : 0);
        fileService.downloadZipFiles(fileSeqs, response);
    }
}
