package kr.co.mindpro.ipms.domain.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 이의심판 생성 request
 */
@Data
public class ExampleList {

    @Schema(description = "구분", example = "D001")
    private String type;

    @Schema(description = "접수일", example = "2025-01-01")
    private String type1;

    @Schema(description = "OurRef", example = "1231233123")
    private String type2;

    @Schema(description = "특허심판원", example = "홍길동")
    private String type3;

    @Schema(description = "사건번호", example = "252303003")
    private String type4;

    @Schema(description = "사건종류", example = "거절결정불복심판")
    private String type4Nm;

    @Schema(description = "대리인구분", example = "청구인대리/미청구인대리")
    private String type9Nm;

    @Schema(description = "의뢰인", example = "홍길동")
    private String type5;

    @Schema(description = "현재상태", example = "진행중")
    private String type6;

    @Schema(description = "사건마감일", example = "2025-01-01")
    private String type7;

    @Schema(description = "yourRef", example = "123123123")
    private String type78;



}


