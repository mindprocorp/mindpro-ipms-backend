package kr.co.mindpro.ipms.domain.file.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 파일 마스터 테이블 매핑 객체 (utb_file_mst)
 * 
 * @author   : mindpro
 * @fileName : FileMstVO.java
 * @since    : 2026. 01. 21.
 */
@Getter 
@Setter
@ToString(callSuper = true)
public class FileMstVO extends BaseVO {

    /** 파일 일련번호 (PK) */
    private String fileSeq;           

    /** 파일 저장소 일련번호 */
    private String fileRepositorySeq;
    
    /** 서류 일련번호 */
    private String fileDocSeq;        // docSeq 대신 테이블 컬럼명 반영

    /** 저장 파일명 */
    private String fileNm;

    /** 원본 파일명 */
    private String fileOriginalNm;

    /** 파일 크기 (Byte) */
    private Integer fileSize;

    /** 파일 표시 크기 (예: 10KB, 1.2MB) */
    private String fileDisplaySize;

    /** 파일 경로/URL */
    private String fileUrl;

    /** 비고 */
    private String note;
}

