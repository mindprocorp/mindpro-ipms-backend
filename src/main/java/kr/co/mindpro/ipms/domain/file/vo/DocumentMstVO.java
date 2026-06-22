package kr.co.mindpro.ipms.domain.file.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 서류 마스터 테이블 매핑 객체 (utb_document_mst)
 * 
 * @author   : mindpro
 * @fileName : DocumentMstVO.java
 * @since    : 2026. 01. 28.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class DocumentMstVO extends BaseVO {

    /** 서류 일련번호 (PK) */
    private Long docSeq;

    /** 서류 구분 */
    private String docDiv;

    /** 진입 타입 (10:접수, 20:제출 등) */
    private String entryType;

    /** 서류 명칭 */
    private String docNm;

    /** 자동 생성 여부 (Y/N) */
    private String autoYn;

    /** 권리 구분 (특허, 상표 등) */
    private String patType;

    /** 기준일 구분 */
    private String baseDateType;

    /** 기한 단위 */
    private String deadlineUnit;

    /** 기한 값 */
    private Integer deadlineVal;

    /** 참조 값 */
    private String refVal;

    /** 정렬 순서 */
    private Integer sortOrd;

    /** 비고 */
    private String note;
}
