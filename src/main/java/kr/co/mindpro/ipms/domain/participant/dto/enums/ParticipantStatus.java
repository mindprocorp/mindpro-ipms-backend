package kr.co.mindpro.ipms.domain.participant.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ParticipantStatus implements BaseEnum {
    READY("R"),
    SHIPPING("S"),
    DONE("D");


    @JsonValue
    private final String code;

    private static final Map<String, ParticipantStatus> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            ParticipantStatus::getCode,
                            e -> e
                    ));

    ParticipantStatus(String code) {
        this.code = code;
    }

    public String getCode() { return code; }

    public static ParticipantStatus fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
