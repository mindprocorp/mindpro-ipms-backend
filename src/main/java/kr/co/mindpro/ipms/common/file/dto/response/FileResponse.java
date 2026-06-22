package kr.co.mindpro.ipms.common.file.dto.response;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * [DTO] S3(NCP) 파일 정보 응답
 * Java Record를 사용하여 불변 객체로 정의합니다.
 *
 * @author	 : intst
 * @fileName : FileResponse.java
 * @since	 : 2025. 12. 24.
 */
@Schema(description = "S3(NCP) 파일 정보 응답")
public record FileResponse(
		
    @Schema(description = "파일 일련번호(PK)", example = "F2025_0ET6YJ9G6S1QE")
    String fileSeq,
		
    @Schema(description = "원본 파일명", example = "uploads_original.png")
    String originalFilename,		
    
    @Schema(description = "파일 키(경로 포함 파일명)", example = "uploads/uuid-image.png")
    String fileName,

    @Schema(description = "파일 접근 전체 URL", example = "https://kr.object.ncloudstorage.com/bucket/uploads/file.png")
    String fileUrl,

    @Schema(description = "파일 크기 (Byte)", example = "102400")
    Long size,

    @Schema(description = "최종 수정 일시")
    Instant lastModified
) {
    /**
     * 바이트 단위를 가독성 좋은 단위(KB, MB 등)로 변환하는 정적 메서드
     * Record 내부에 로직이 필요한 경우 아래와 같이 정의할 수 있습니다.
     */
    public String getDisplaySize() {
        if (size == null || size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.2f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}