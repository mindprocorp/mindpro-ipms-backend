package kr.co.mindpro.ipms.domain.example.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 계류법정
 */
public enum ExamplePendingCourt implements BaseEnum {
    DOMESTIC("C001","국내","국내"),
    OVERSEA("C002","국외","국외");


    private final String code;
    private final String desc;
    private final String nameKr;

    private static final Map<String, ExamplePendingCourt> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            ExamplePendingCourt::getCode,
                            e -> e
                    ));

    ExamplePendingCourt(String code, String desc, String nameKr) {
        this.code = code;
        this.desc = desc;
        this.nameKr = nameKr;

    }

    @JsonValue
    public String getCode() { return code; }

    public static ExamplePendingCourt fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
