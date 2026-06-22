package kr.co.mindpro.ipms.domain.conflict.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 계류법정
 */
public enum ConflictPendingCourt implements BaseEnum {
    DOMESTIC("C001","특허청","특허청"),
    OVERSEA("C002","특허법원","특허법원");


    private final String code;
    private final String desc;
    private final String nameKr;

    private static final Map<String, ConflictPendingCourt> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            ConflictPendingCourt::getCode,
                            e -> e
                    ));

    ConflictPendingCourt(String code, String desc, String nameKr) {
        this.code = code;
        this.desc = desc;
        this.nameKr = nameKr;

    }

    @JsonValue
    public String getCode() { return code; }

    public static ConflictPendingCourt fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
