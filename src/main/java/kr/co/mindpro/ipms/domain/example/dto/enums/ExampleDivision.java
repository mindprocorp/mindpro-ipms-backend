package kr.co.mindpro.ipms.domain.example.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 구분
 */
public enum ExampleDivision implements BaseEnum {
    DOMESTIC("D001","국내","국내","domestic"),
    OVERSEA("D002","국외","국내","overSea");



    private final String code;
    private final String desc;
    private final String nameKr;
    private final String nameEn;

    private static final Map<String, ExampleDivision> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            ExampleDivision::getCode,
                            e -> e
                    ));

    ExampleDivision(String code, String desc, String nameKr, String nameEn) {
        this.desc = desc;
        this.code = code;
        this.nameKr = nameKr;
        this.nameEn = nameEn;
    }

    @JsonValue
    public String getCode() { return code; }

    public static ExampleDivision fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
