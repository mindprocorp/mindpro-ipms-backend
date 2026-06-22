package kr.co.mindpro.ipms.domain.code.dto.response;

import kr.co.mindpro.ipms.domain.code.vo.CodeVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * 이의심판 생성 request
 */
@Data
public class CodeResponse {
    private Long codeSeq;
    private String groupCode;
    private String groupName;
    private String code;
    private String codeName;
    private String createUser;          	// 등록자
    private OffsetDateTime createAt; 		// 등록 일시
    private String updateUser;          	// 수정자
    private OffsetDateTime updateAt; 		// 수정 일시
    private String delYn;
    private String note;

    public static CodeResponse of(CodeVo codeVo){
        CodeResponse codeResponse = new CodeResponse();
        codeResponse.setCodeSeq(codeVo.getCodeSeq());
        codeResponse.setGroupCode(codeVo.getGroupCode());
        codeResponse.setGroupName(codeVo.getGroupName());
        codeResponse.setCode(codeVo.getCode());
        codeResponse.setCodeName(codeVo.getCodeName());
        codeResponse.setCreateUser(codeVo.getCreateUser());
        codeResponse.setCreateAt(codeVo.getCreateAt());
        codeResponse.setUpdateUser(codeVo.getUpdateUser());
        if(codeVo.getUpdateAt() != null){
            codeResponse.setUpdateAt(codeVo.getUpdateAt());
        }


        return codeResponse;
    }



}
