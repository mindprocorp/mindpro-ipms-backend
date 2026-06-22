package kr.co.mindpro.ipms.domain.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 이의심판 생성 request
 */
@Data
public class ExampleListResponse {

    @Schema(description = "이의심판 리스트")
    private List<ExampleList> conflictList;


}


