package kr.co.mindpro.ipms.domain.example.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 이의심판 상세 request
 */
@Data
public class ExampleListRequest {

    @Schema(description = "사용자 시퀀스")
    private Long customerInfoSeq;

    @Schema(description = "상태" , example = "마감경과 , 미청구 , 보정서마감")
    private String type1;




}
