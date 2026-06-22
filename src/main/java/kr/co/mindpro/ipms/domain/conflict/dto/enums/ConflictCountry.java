package kr.co.mindpro.ipms.domain.conflict.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 출원국가
 */
public enum ConflictCountry implements BaseEnum {
    KOREA("ko","한국","한국","한국"),
    JAPAN("jp","일본","일본","일본");



    private final String code;
    private final String desc;
    private final String nameKr;
    private final String nameEn;

    private static final Map<String, ConflictCountry> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            ConflictCountry::getCode,
                            e -> e
                    ));

    ConflictCountry(String code, String desc, String nameKr, String nameEn) {
        this.desc = desc;
        this.code = code;
        this.nameKr = nameKr;
        this.nameEn = nameEn;
    }

    @JsonValue
    public String getCode() { return code; }

    public static ConflictCountry fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
