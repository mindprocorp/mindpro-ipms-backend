package kr.co.mindpro.ipms.domain.example.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.mindpro.ipms.common.enums.BaseEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 사건종류
 */
public enum ExampleCourtCategoryCode implements BaseEnum {
    NON_CANCEL_JUDGMENT("N001","불사용취소심판","불사용취소심판"),
    NON_CANCEL_JUDGMENT_TT("N002","불사용취소심판2","불사용취소심판2");



    private final String code;
    private final String desc;
    private final String nameKr;

    private static final Map<String, ExampleCourtCategoryCode> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            ExampleCourtCategoryCode::getCode,
                            e -> e
                    ));

    ExampleCourtCategoryCode(String code, String desc, String nameKr) {
        this.code = code;
        this.desc = desc;
        this.nameKr = nameKr;
    }

//    @JsonValue
//    @Override
//    public String getCode() {
//        return code;
//    }

    public static ExampleCourtCategoryCode fromCode(String code) {
        return CODE_MAP.get(code);
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }
}
