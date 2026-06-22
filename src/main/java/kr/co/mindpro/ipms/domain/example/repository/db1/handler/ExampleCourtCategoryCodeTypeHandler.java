package kr.co.mindpro.ipms.domain.example.repository.db1.handler;

import kr.co.mindpro.ipms.common.enums.CodeEnumTypeHandler;
import kr.co.mindpro.ipms.domain.example.dto.enums.ExampleCourtCategoryCode;

public class ExampleCourtCategoryCodeTypeHandler extends CodeEnumTypeHandler<ExampleCourtCategoryCode> {

    public ExampleCourtCategoryCodeTypeHandler() {
        super(ExampleCourtCategoryCode.class);
    }
}
