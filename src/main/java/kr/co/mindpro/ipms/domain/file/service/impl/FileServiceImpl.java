package kr.co.mindpro.ipms.domain.file.service.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.common.file.dto.request.FileRequest;
import kr.co.mindpro.ipms.common.file.dto.response.FileResponse;
import kr.co.mindpro.ipms.common.file.service.FileService;
import kr.co.mindpro.ipms.common.util.IdGenerator;
import kr.co.mindpro.ipms.domain.file.repository.db1.FileMapper;
import kr.co.mindpro.ipms.domain.file.vo.DocumentMstVO;
import kr.co.mindpro.ipms.domain.file.vo.FileMstVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * [Service Implementation] NCP Object Storage(S3 호환) 파일 서비스
 * 비즈니스 예외 전환 및 FileResponse(Record) 적용
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "file.storage.type", havingValue = "s3")
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;
    private final FileMapper fileMapper;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.endpoint}")
    private String endpoint;

    /**
     * 파일 업로드
     */    
    @Override
    public FileResponse uploadFile(MultipartFile file, FileRequest fileRequest, String userId) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        try {
            DocumentMstVO docInfo = fileMapper.selectDocumentMstById(fileRequest.docSeq());
            if (docInfo == null) {
                throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
            }
            
            String entryType = docInfo.getEntryType();
            String typeDir = switch (entryType) {
                case "10" -> "REC";
                case "20" -> "SUB";
                case "90" -> "UPL";
                default   -> "EVD";
            };
            
            String folderName = switch (entryType) {
                case "10" -> "reception";
                case "20" -> "submission";
                case "90" -> "upload";
                default   -> "evidence";
            };            
            
            String fileSeq = IdGenerator.generateTSID(); 
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String repositorySeq = fileMapper.selectRepositorySeqByKind(typeDir);
            if (repositorySeq == null) {
                throw new BusinessException(ErrorCode.REPOSITORY_NOT_FOUND);
            }
            
            String savedName = fileSeq + extension;
            String key = folderName + "/" + savedName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, 
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String fileUrl = String.format("%s/%s/%s", endpoint, bucket, key);
            String displaySize = formatFileSize(file.getSize());
            
            FileMstVO fileMstVo = new FileMstVO();
            fileMstVo.setFileSeq(fileSeq);
            fileMstVo.setFileRepositorySeq(repositorySeq);
            fileMstVo.setFileNm(savedName);
            fileMstVo.setFileOriginalNm(originalFilename);
            fileMstVo.setFileSize((int) file.getSize());
            fileMstVo.setFileDisplaySize(displaySize);
            fileMstVo.setFileUrl(fileUrl);
            fileMstVo.setNote(fileRequest.note());
            fileMstVo.setCreateUser(userId);
            fileMstVo.setDelYn("N");
            
            fileMapper.insertFileMaster(fileMstVo);    

            return new FileResponse(fileSeq, originalFilename, key, fileUrl, file.getSize(), Instant.now());
            
        } catch (IOException | S3Exception e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }        
    }
    
    @Override
    @Transactional
    public List<FileResponse> multiUpload(List<MultipartFile> files, FileRequest fileRequest, String userId) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }

        List<FileResponse> responses = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                responses.add(this.uploadFile(file, fileRequest, userId));
            }
        } catch (Exception e) {
            for (FileResponse res : responses) {
                try {
                    s3Client.deleteObject(DeleteObjectRequest.builder()
                            .bucket(bucket)
                            .key(res.fileName())
                            .build());
                } catch (Exception s3Ex) {
                    log.error("Clean-up 중 S3 삭제 실패: {}", res.fileName());
                }
            }
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }
        return responses;
    }
    
    private String formatFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    
    @Override
    @Transactional
    public void deleteFile(String fileSeq, String fileUrl, String userId) {
        if (fileUrl == null || !fileUrl.contains(bucket)) {
            return;
        }
    		
        try {
            String key = extractS3Key(fileUrl);
            s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
            fileMapper.updateDeleteYn(fileSeq, userId);
        } catch (S3Exception e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public List<FileResponse> getFileList(String prefix) {
        try {
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(bucket)
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

            return listResponse.contents().stream()
                    .map(content -> {
                        String fileUrl = String.format("%s/%s/%s", endpoint, bucket, content.key());
                        String originalNameFromKey = content.key().substring(content.key().lastIndexOf("/") + 1);
                        String extractedSeq = "";
                        if (originalNameFromKey.contains("_")) {
                            extractedSeq = originalNameFromKey.substring(originalNameFromKey.indexOf("_") + 1, originalNameFromKey.lastIndexOf("."));
                        }
                        
                        return new FileResponse(
                            extractedSeq,
                            originalNameFromKey,
                            content.key(),
                            fileUrl,
                            content.size(),
                            content.lastModified()
                        );
                    })
                    .collect(Collectors.toList());
        } catch (S3Exception e) {
            log.error("S3 파일 목록 조회 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public FileResponse uploadAiTempFile(MultipartFile file, String userId) {
        return uploadTempFile(file, userId, "ai");
    }

    @Transactional
    @Override
    public FileResponse uploadTempFile(MultipartFile file, String userId, String category) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        try {
            byte[] bytes = file.getBytes();
            long size = bytes.length;
            
            String fileSeq = IdGenerator.generateTSID();
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // [수정] repositoryKind "UPL"의 URL과 매칭되도록 폴더 구조 조정
            String repositorySeq = fileMapper.selectRepositorySeqByKind("UPL");
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String savedName = fileSeq + extension;
            
            // key는 S3 실제 경로 (upload 폴더 하위로 설정)
            String key = String.format("upload/temp/%s/%s/%s", category, datePath, savedName);
            
            // DB의 file_nm은 repository_url(~/upload) 이후의 경로여야 함
            String dbFileNm = String.format("temp/%s/%s/%s", category, datePath, savedName);

            log.info("임시 파일 업로드 시도 - Key: {}, Size: {}", key, size);

            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .build(),
                    RequestBody.fromBytes(bytes)); // InputStream 대신 Bytes 사용으로 0byte 방지

            String fileUrl = String.format("%s/%s/%s", endpoint, bucket, key);

            FileMstVO fileMstVo = new FileMstVO();
            fileMstVo.setFileSeq(fileSeq);
            fileMstVo.setFileRepositorySeq(repositorySeq);
            fileMstVo.setFileNm(dbFileNm); 
            fileMstVo.setFileOriginalNm(originalFilename);
            fileMstVo.setFileSize((int) size);
            fileMstVo.setFileDisplaySize(formatFileSize(size));
            fileMstVo.setFileUrl(fileUrl);
            fileMstVo.setNote(category + " 임시 파일");
            fileMstVo.setCreateUser(userId);
            fileMstVo.setDelYn("N");

            fileMapper.insertFileMaster(fileMstVo);

            return new FileResponse(fileSeq, originalFilename, dbFileNm, fileUrl, size, Instant.now());

        } catch (IOException | S3Exception e) {
            log.error("임시 파일 업로드 실패 (카테고리: {}): {}", category, e.getMessage());
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public void downloadFile(String fileUrl, HttpServletResponse response) {
        if (fileUrl == null || !fileUrl.contains(bucket)) {
            log.warn("다운로드 요청된 URL이 유효하지 않습니다: {}", fileUrl);
            throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
        }

        try {
            String key = extractS3Key(fileUrl);
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            try (ResponseInputStream<GetObjectResponse> s3is = s3Client.getObject(getObjectRequest)) {
                GetObjectResponse s3Response = s3is.response();
                String fileName = key.substring(key.lastIndexOf("/") + 1);
                String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
                
                response.setContentType(s3Response.contentType());
                response.setContentLengthLong(s3Response.contentLength());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
                StreamUtils.copy(s3is, response.getOutputStream());
                response.flushBuffer();
            }
        } catch (IOException | S3Exception e) {
            log.error("S3 파일 다운로드 중 오류 발생: {}", e.getMessage());
            //throw new BusinessException(ErrorCode.FILE_DOWNLOAD_ERROR);
        }
    }

    private String extractS3Key(String fileUrl) {
        if (fileUrl == null) return "";
        String baseUrl = endpoint + "/" + bucket + "/";
        if (fileUrl.startsWith(baseUrl)) {
            return fileUrl.substring(baseUrl.length());
        }
        // 만약 형식이 일치하지 않는 예외적인 경우 기존 방식(fallback) 적용
        int idx = fileUrl.lastIndexOf(bucket + "/");
        if (idx != -1) {
            return fileUrl.substring(idx + bucket.length() + 1);
        }
        return fileUrl;
    }

    @Override
    public void downloadZipFiles(List<String> fileSeqs, HttpServletResponse response) {
        if (fileSeqs == null || fileSeqs.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
        }

        List<FileMstVO> files = fileMapper.selectFilesBySeqs(fileSeqs);
        if (files == null || files.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
        }

        if (files.size() == 1) {
            // 단일 파일인 경우 압축하지 않고 개별 파일 다운로드 처리
            FileMstVO fileVO = files.get(0);
            String fileUrl = fileVO.getFileUrl(); 
            String key = extractS3Key(fileUrl);
            String originalFileName = fileVO.getFileOriginalNm();

            try {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();

                try (ResponseInputStream<GetObjectResponse> s3is = s3Client.getObject(getObjectRequest)) {
                    GetObjectResponse s3Response = s3is.response();
                    String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
                    
                    response.setContentType(s3Response.contentType());
                    response.setContentLengthLong(s3Response.contentLength());
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
                    StreamUtils.copy(s3is, response.getOutputStream());
                    response.flushBuffer();
                }
            } catch (IOException | S3Exception e) {
                log.error("S3 단일 파일 다운로드 중 오류 발생: {}", e.getMessage());
            }
            return;
        }

        // 2개 이상일 경우 ZIP 압축 처리
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"attachments.zip\"");

        try (java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(response.getOutputStream())) {
            java.util.Set<String> addedFileNames = new java.util.HashSet<>();
            
            for (FileMstVO fileVO : files) {
                String fileUrl = fileVO.getFileUrl();
                // 전체 URL에서 고정 경로(endpoint/bucket)를 잘라내어 S3 key 추출
                String key = extractS3Key(fileUrl);
                String originalFileName = fileVO.getFileOriginalNm();

                // 중복 파일명 방지 로직: (1), (2) 형태 추가
                String finalFileName = originalFileName;
                int counter = 1;
                while (addedFileNames.contains(finalFileName)) {
                    int dotIndex = originalFileName.lastIndexOf('.');
                    if (dotIndex != -1) {
                        String nameWithoutExt = originalFileName.substring(0, dotIndex);
                        String ext = originalFileName.substring(dotIndex);
                        finalFileName = nameWithoutExt + " (" + counter + ")" + ext;
                    } else {
                        finalFileName = originalFileName + " (" + counter + ")";
                    }
                    counter++;
                }
                addedFileNames.add(finalFileName);

                try {
                    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build();

                    try (ResponseInputStream<GetObjectResponse> s3is = s3Client.getObject(getObjectRequest)) {
                        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(finalFileName);
                        zos.putNextEntry(zipEntry);
                        StreamUtils.copy(s3is, zos);
                        zos.closeEntry();
                    }
                } catch (S3Exception e) {
                    log.warn("ZIP 포함 중 S3 파일 찾기 실패 (key={}): {}", key, e.getMessage());
                }
            }
            zos.finish();
            response.flushBuffer();
        } catch (IOException e) {
            log.error("S3 다중 파일 ZIP 다운로드 중 오류 발생: {}", e.getMessage());
        }
    }
}