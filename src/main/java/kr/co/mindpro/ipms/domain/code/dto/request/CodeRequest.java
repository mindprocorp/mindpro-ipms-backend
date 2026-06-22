package kr.co.mindpro.ipms.domain.code.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 이의심판 상세 request
 */
@Data
@Builder
@NoArgsConstructor  // 빌더와 함께 쓰려면 기본 생성자 필요
@AllArgsConstructor // 빌더와 함께 쓰려면 전체 생성자 필요
public class CodeRequest {

    @Schema(description = "코드 시퀀스", example = "1")
    private Long codeSeq;

    @Schema(description = "그룹 시퀀스", example = "1")
    private Long groupSeq;

    @Schema(description = "그룹코드", example = "GROUP01")
    private List<String> groupCode;


    @Schema(description = "그룹코드 명", example = "그룹코드 명")
    private String groupName;

    @Schema(description = "코드", example = "코드")
    private List<String> code;

    @Schema(description = "코드 명", example = "TEXT")
    private String codeName;

    @Schema(description = "생성자", example = "userId")
    private String createUser;

    @Schema(description = "수정자", example = "userId")
    private String updateUser;

    @Schema(description = "삭제여부", example = "Y/N")
    private String delYn;
}
