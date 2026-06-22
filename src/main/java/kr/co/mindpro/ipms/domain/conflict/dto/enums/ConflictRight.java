package kr.co.mindpro.ipms.domain.conflict.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 권리
 */
public enum ConflictRight implements BaseEnum {
    BRAND("R001","상표","상표");



    private final String code;
    private final String desc;
    private final String nameKr;

    private static final Map<String, ConflictRight> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            ConflictRight::getCode,
                            e -> e
                    ));

    ConflictRight(String code, String desc, String nameKr) {
        this.desc = desc;
        this.code = code;
        this.nameKr = nameKr;
    }

    @JsonValue
    public String getCode() { return code; }

    public static ConflictRight fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
