package kr.co.mindpro.ipms.domain.example.dto.request.base;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.example.vo.ExampleMstVO;
import lombok.Data;

@Data
public class ExampleAppManagerInfo {
    @Schema(description = "계류법정", example = "OVERSEA")
    private String userId;

    @Schema(description = "대리인구분", example = "123123")
    private String userPw;


    @Schema(description = "권리", example = "R001")

    public static ExampleAppManagerInfo from(ExampleMstVO conflictMstVO){
        ExampleAppManagerInfo conflictAppManagerInfo = new ExampleAppManagerInfo();
        conflictAppManagerInfo.setUserId("");

        return conflictAppManagerInfo;
    }
}
