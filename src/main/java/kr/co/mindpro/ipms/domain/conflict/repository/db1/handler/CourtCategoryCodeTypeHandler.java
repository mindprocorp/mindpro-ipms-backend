package kr.co.mindpro.ipms.domain.conflict.repository.db1.handler;


import kr.co.mindpro.ipms.common.enums.CodeEnumTypeHandler;
import kr.co.mindpro.ipms.domain.conflict.dto.enums.ConflictCourtCategoryCode;

public class CourtCategoryCodeTypeHandler  extends CodeEnumTypeHandler<ConflictCourtCategoryCode> {

    public CourtCategoryCodeTypeHandler() {
        super(ConflictCourtCategoryCode.class);
    }
}
