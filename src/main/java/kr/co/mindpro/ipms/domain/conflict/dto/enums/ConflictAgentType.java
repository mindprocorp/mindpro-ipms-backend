package kr.co.mindpro.ipms.domain.conflict.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 대리인구분
 */
public enum ConflictAgentType implements BaseEnum {
    NON_CANCEL_JUDGMENT("CFTCTI0001","청구인대리","불사용취소심판");



    private final String code;
    private final String desc;
    private final String nameKr;

    private static final Map<String, ConflictAgentType> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            ConflictAgentType::getCode,
                            e -> e
                    ));

    ConflictAgentType(String code, String desc, String nameKr) {
        this.desc = desc;
        this.code = code;
        this.nameKr = nameKr;
    }

    @JsonValue
    public String getCode() { return code; }

    public static ConflictAgentType fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
