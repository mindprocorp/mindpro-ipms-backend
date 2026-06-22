package kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author : seokho
 * @fileName : AppPatentVO.java
 * @since : 2026. 1. 9.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class AppPatentVO extends BaseVO {

    /** 출원_식별자 */
    private String appSeq;

    /** 사무소_식별자 */
    private String officeSeq;

    /** 특허/실용신안 */
    private String patentSeq;

    /** 출원_구분 */
    private String appCategoryCode;
    private String appCategoryName;

    /** 대표_도면_파일 */
    private String mainDrawingFile;

    /** 요약 내용 */
    private String summary;

    /** 청구 범위 */
    private String claimScope;

}
