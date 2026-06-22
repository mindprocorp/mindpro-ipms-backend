package kr.co.mindpro.ipms.domain.example.dto.request.base;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.example.vo.ExampleMstVO;
import lombok.Data;

@Data
public class ExampleAppBaseInfo {
    @Schema(description = "계류법정", example = "OVERSEA")
    private String userId;

    @Schema(description = "대리인구분", example = "123123")
    private String userPw;

    @Schema(description = "구분", example = "마인드")
    private String userPw1;

    @Schema(description = "권리", example = "R001")
    private String userPw2;

    public static ExampleAppBaseInfo from(ExampleMstVO conflictMstVO){
        ExampleAppBaseInfo oppoAppBaseInfo = new ExampleAppBaseInfo();
        oppoAppBaseInfo.setUserId("");

        return oppoAppBaseInfo;
    }
}
