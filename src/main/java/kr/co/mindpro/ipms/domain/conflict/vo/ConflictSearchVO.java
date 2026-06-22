package kr.co.mindpro.ipms.domain.conflict.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 분쟁/심판(Conflict) 통합 매핑 VO
 */
@Data
@Builder
@Schema(description = "")
public class ConflictSearchVO {



    private String targetTable; // mst 또는 app
    private String dbColumn;    // 컬럼명
    private String operator;    // = 또는 LIKE
    private String value;       // 검색값


}