package kr.co.mindpro.ipms.domain.paper.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : mindpro
 * @fileName : PaperDossierVO.java
 * @since : 2026. 2. 4.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaperDossierVO extends PaperResponseVO {

    @Schema(description = "파일 생성자 이름")
    private String userNameKo;
}
