package kr.co.mindpro.ipms.domain.code.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 이의심판 상세 request
 */
@Data
public class GroupCodeRequest {

    @Schema(description = "그룹 시퀀스", example = "1")
    private Long groupSeq;

    @Schema(description = "그룹코드", example = "GROUP01")
    private String groupCode;


    @Schema(description = "그룹코드 명", example = "그룹코드 명")
    private String groupName;

    @Schema(description = "생성자", example = "userId")
    private String createUser;

    @Schema(description = "수정자", example = "userId")
    private String updateUser;

    @Schema(description = "사용여부", example = "Y/N")
    private String useYn;

    @Schema(description = "삭제여부", example = "Y/N")
    private String delYn;

    @Schema(description = "비고")
    private String note;
}
