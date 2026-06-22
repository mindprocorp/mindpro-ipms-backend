package kr.co.mindpro.ipms.domain.paper.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum PaperStatus implements BaseEnum {
    READY("R"),
    SHIPPING("S"),
    DONE("D");


    @JsonValue
    private final String code;

    private static final Map<String, PaperStatus> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            PaperStatus::getCode,
                            e -> e
                    ));

    PaperStatus(String code) {
        this.code = code;
    }

    public String getCode() { return code; }

    public static PaperStatus fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
