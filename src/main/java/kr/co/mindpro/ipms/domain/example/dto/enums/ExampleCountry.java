package kr.co.mindpro.ipms.domain.example.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 출원국가
 */
public enum ExampleCountry implements BaseEnum {
    KOREA("ko","한국","한국","한국"),
    JAPAN("jp","일본","일본","일본");



    private final String code;
    private final String desc;
    private final String nameKr;
    private final String nameEn;

    private static final Map<String, ExampleCountry> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            ExampleCountry::getCode,
                            e -> e
                    ));

    ExampleCountry(String code, String desc, String nameKr, String nameEn) {
        this.desc = desc;
        this.code = code;
        this.nameKr = nameKr;
        this.nameEn = nameEn;
    }

    @JsonValue
    public String getCode() { return code; }

    public static ExampleCountry fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
