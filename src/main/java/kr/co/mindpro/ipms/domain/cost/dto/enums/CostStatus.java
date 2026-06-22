package kr.co.mindpro.ipms.domain.cost.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum CostStatus implements BaseEnum {
    READY("R"),
    SHIPPING("S"),
    DONE("D");


    @JsonValue
    private final String code;

    private static final Map<String, CostStatus> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            CostStatus::getCode,
                            e -> e
                    ));

    CostStatus(String code) {
        this.code = code;
    }

    public String getCode() { return code; }

    public static CostStatus fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
