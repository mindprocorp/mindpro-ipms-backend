package kr.co.mindpro.ipms.domain.example.dto.request.base;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.example.dto.enums.ExampleDivision;
import kr.co.mindpro.ipms.domain.example.vo.ExampleMstVO;
import lombok.Data;

@Data
public class ExampleAppCaseMng {
    @Schema(description = "계류법정", example = "OVERSEA")
    private String userId;

    @Schema(description = "대리인구분", example = "123123")
    private String userPw;

    @Schema(description = "구분", example = "D001")
    private ExampleDivision type;

    @Schema(description = "권리", example = "R001")
    private String right;



    public static ExampleAppCaseMng from(ExampleMstVO conflictMstVO){
        ExampleAppCaseMng appCaseMng = new ExampleAppCaseMng();
        appCaseMng.setRight("");
        appCaseMng.setUserId("");
        appCaseMng.setRight("");

        return appCaseMng;
    }
}
