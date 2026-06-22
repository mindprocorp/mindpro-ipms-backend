package kr.co.mindpro.ipms.domain.memo.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum MemoStatus implements BaseEnum {
    READY("R"),
    SHIPPING("S"),
    DONE("D");


    @JsonValue
    private final String code;

    private static final Map<String, MemoStatus> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            MemoStatus::getCode,
                            e -> e
                    ));

    MemoStatus(String code) {
        this.code = code;
    }

    public String getCode() { return code; }

    public static MemoStatus fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
