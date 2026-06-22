package kr.co.mindpro.ipms.domain.paper.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

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
public class PaperRequestVO extends PaperMstVO {
    @Schema(description = "접수파일 서류구분")
    private String receiptDocSeq; // 이 필드에 실제 파일을 담아서 보냅니다.

    @Schema(description = "제출파일 서류구분")
    private String submitDocSeq; // 이 필드에 실제 파일을 담아서 보냅니다.

    @Schema(description = "물리 파일 객체 (DB 저장 안됨)")
    private MultipartFile file; // 이 필드에 실제 파일을 담아서 보냅니다.

}