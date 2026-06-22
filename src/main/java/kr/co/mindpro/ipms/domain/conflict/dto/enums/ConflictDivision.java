package kr.co.mindpro.ipms.domain.conflict.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 구분
 */
public enum ConflictDivision implements BaseEnum {
    DOMESTIC("D001","국내","국내","domestic"),
    OVERSEA("D002","국외","국내","overSea");



    private final String code;
    private final String desc;
    private final String nameKr;
    private final String nameEn;

    private static final Map<String, ConflictDivision> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            ConflictDivision::getCode,
                            e -> e
                    ));

    ConflictDivision(String code, String desc, String nameKr, String nameEn) {
        this.desc = desc;
        this.code = code;
        this.nameKr = nameKr;
        this.nameEn = nameEn;
    }

    @JsonValue
    public String getCode() { return code; }

    public static ConflictDivision fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
