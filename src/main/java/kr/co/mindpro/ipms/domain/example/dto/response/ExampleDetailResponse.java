package kr.co.mindpro.ipms.domain.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.example.dto.request.base.ExampleAppCaseMng;
import kr.co.mindpro.ipms.domain.example.dto.request.base.ExampleAppBaseInfo;
import kr.co.mindpro.ipms.domain.example.dto.request.base.ExampleAppManagerInfo;
import lombok.Data;

/**
 * 이의심판 생성 request
 */
@Data
public class ExampleDetailResponse {

    @Schema(description = "출원 사건관리")
    private ExampleAppCaseMng conflictAppCaseMng;

    @Schema(description = "출원 기본정보")
    private ExampleAppBaseInfo conflictAppBaseInfo;

    @Schema(description = "닫당 정보")
    private ExampleAppManagerInfo conflictAppManagerInfo;



}
