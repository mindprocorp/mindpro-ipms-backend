package kr.co.mindpro.ipms.domain.paper.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

/**
 * @author : mindpro
 * @fileName : PaperDossierArchiveVO.java
 * @since : 2026. 3. 25.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaperDossierArchiveVO extends BaseVO {
    @Schema(description = "상위 테이블 시퀀스")
    private String parentSeq;

    @Schema(description = "파일 식별키")
    private String tblSeq;

    private String caseClassificationCode;

    private String caseClassificationName;

    private String caseCategoryCode;

    private String caseCategoryName;

    @Schema(description = "출원루트코드")
    private String appRouteCode;

    private String appRouteCodeName;

    private String rightCategoryCode;

    private String rightCategoryName;

    private String assetNo;

    private String appNo;

    private String regNo;

    private String fileSeq;

    private String docSeq;

    private String docName;

    private String fileKindCode;

    private String fileKindName;

    private String fileName;

    private String fileOriginalName;

    private String downloadUrl;

    private String fileDisplaySize;

    private String uploadUserSeq;

    private String uploadUserName;

    private String note;

    private OffsetDateTime uploadDate;

}
