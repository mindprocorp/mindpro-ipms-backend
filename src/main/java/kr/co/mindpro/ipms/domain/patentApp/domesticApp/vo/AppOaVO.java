package kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author : seokho
 * @fileName : AppOaVO.java
 * @since : 2026. 1. 9.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppOaVO extends BaseVO {

    /** 출원_식별자 */
    private String appSeq;

    /** 사무소_식별자 */
    private String officeSeq;

    /** OA_식별자 */
    private String oaSeq;

    /** OA_순서 */
    private Integer oaIndex;

    /** 서류_구분_코드 */
    private String paperCategoryCode;

    /** OA_서류_파일 */
    private String oaPaperFile;

    /** 마감_일자 */
    private LocalDateTime deadLineDate;

    /** 보정서_작성여부 */
    private String retouchWriteyn;

    /** 담당자 */
    private String staff;

    /** 메인_변리사 */
    private String mainPatAttorney;

    /** 원_파일 */
    private String originalFile;

    /** 수정_파일 */
    private String updateFile;

    /** 상태 */
    private String state;


}
