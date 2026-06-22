package kr.co.mindpro.ipms.domain.code.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CodeVo extends BaseVO {
    private Long codeSeq;
    private Long GroupSeq;
    private String groupCode;
    private String groupName;
    private String code;
    private String codeName;
}
