package kr.co.mindpro.ipms.common.file.service;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.mindpro.ipms.common.file.dto.request.FileRequest;
import kr.co.mindpro.ipms.common.file.dto.response.FileResponse;

/**
 * [Service] S3(NCP) 파일 업로드 및 관리 인터페이스
 * 
 * @author	 : mindpro
 * @fileName : FileService.java
 * @since	 : 2025. 12. 19.
 */
public interface FileService {
	
    /**
     * 파일 업로드 프로세스 (S3 저장 및 DB 마스터 기록)
     * @param file 멀티파트 파일
     * @param fileRequest 파일 정보(Type, Kind, Category) 요청 DTO
     * @param userId 생성자 ID
     * @return FileResponse (저장된 파일 상세 정보)
     */
    FileResponse uploadFile(MultipartFile file, FileRequest fileRequest, String userId);
    
    /**
     * 다중 파일 업로드 프로세스
     * - 여러 개의 파일을 반복 순회하며 업로드 및 DB 기록을 수행합니다.
     * 
     * @param files       멀티파트 파일 리스트
     * @param fileRequest 파일 공통 정보 요청 DTO
     * @param userId      등록자(세션) ID
     * @return List<FileResponse> 업로드된 각 파일 정보 리스트
     */
    List<FileResponse> multiUpload(List<MultipartFile> files, FileRequest fileRequest, String userId);    
	
    /**
     * 파일 삭제 프로세스 (S3 물리 삭제 및 DB 논리 삭제)
     * @param fileSeq 삭제할 파일의 일련번호 (PK)
     * @param fileUrl 삭제할 파일의 S3 URL
     * @param userId 수정자 ID
     */
    void deleteFile(String fileSeq, String fileUrl, String userId);
    
    /**
     * 파일 목록 조회 (특정 경로 기준)
     * @param prefix 조회할 경로(폴더) 접두사
     * @return 파일 정보 리스트
     */
    List<FileResponse> getFileList(String prefix);

    @Transactional
    FileResponse uploadAiTempFile(MultipartFile file, String userId);

    @Transactional
    FileResponse uploadTempFile(MultipartFile file, String userId, String category);

    /**
     * S3 파일을 브라우저로 스트리밍 다운로드 (프록시 방식)
     * @param fileUrl S3 객체 URL
     * @param response HttpServletResponse
     */
    void downloadFile(String fileUrl, HttpServletResponse response);

    /**
     * 다중 파일을 하나의 ZIP 파일로 압축하여 브라우저로 스트리밍 다운로드
     * @param fileSeqs 압축할 파일의 일련번호 리스트
     * @param response HttpServletResponse
     */
    void downloadZipFiles(List<String> fileSeqs, HttpServletResponse response);
}
