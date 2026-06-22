package kr.co.mindpro.ipms.domain.paper.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 청구서 테이블 매핑 객체
 * DB 테이블 ipms_user 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : CostVO.java
 * @since	 : 2026. 01. 07.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaperResponseVO extends PaperMstVO {

    @Schema(description = "화면용 파일 사이즈")
    private String fileDisplaySize;

    @Schema(description = "파일명")
    private String fileName;

    @Schema(description = "파일명")
    private String downloadUrl;

    @Schema(description = "다중 파일 시퀀스 목록")
    private String fileSeqs;
}