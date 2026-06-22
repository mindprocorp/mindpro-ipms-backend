package kr.co.mindpro.ipms.domain.example.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.example.dto.enums.ExampleCourtCategoryCode;
import kr.co.mindpro.ipms.domain.example.dto.request.base.ExampleAppCaseMng;
import kr.co.mindpro.ipms.domain.example.dto.request.base.ExampleAppBaseInfo;
import kr.co.mindpro.ipms.domain.example.dto.request.base.ExampleAppManagerInfo;
import lombok.Data;

/**
 * 이의심판 생성 request
 */
@Data
public class ExampleCreateRequest {

    @Schema(description = "출원 사건관리")
    private ExampleAppCaseMng appCaseMng;

    @Schema(description = "출원 기본정보")
    private ExampleAppBaseInfo oppoAppBaseInfo;

    @Schema(description = "닫당자 정보")
    private ExampleAppManagerInfo oppoAppManagerInfo;

    @Schema(description = "ENUM 테스트")
    private ExampleCourtCategoryCode conflictCourtCategoryCode;



}
