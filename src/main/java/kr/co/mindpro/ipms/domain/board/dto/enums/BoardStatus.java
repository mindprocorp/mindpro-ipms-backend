package kr.co.mindpro.ipms.domain.board.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum BoardStatus implements BaseEnum {
    READY("R"),
    SHIPPING("S"),
    DONE("D");


    @JsonValue
    private final String code;

    private static final Map<String, BoardStatus> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            BoardStatus::getCode,
                            e -> e
                    ));

    BoardStatus(String code) {
        this.code = code;
    }

    public String getCode() { return code; }

    public static BoardStatus fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
