package kr.co.mindpro.ipms.domain.example.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 사건종류
 */
public enum ExampleTypeIncident implements BaseEnum {
    NON_CANCEL_JUDGMENT("N001","불사용취소심판","불사용취소심판");



    private final String code;
    private final String desc;
    private final String nameKr;

    private static final Map<String, ExampleTypeIncident> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            ExampleTypeIncident::getCode,
                            e -> e
                    ));

    ExampleTypeIncident(String code, String desc, String nameKr) {
        this.desc = desc;
        this.code = code;
        this.nameKr = nameKr;
    }

    @JsonValue
    public String getCode() { return code; }

    public static ExampleTypeIncident fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
