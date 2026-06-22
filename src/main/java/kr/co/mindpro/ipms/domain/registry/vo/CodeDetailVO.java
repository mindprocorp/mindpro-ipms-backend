package kr.co.mindpro.ipms.domain.registry.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * [공통코드] 상세 정보 조회를 위한 Value Object
 * utb_code_dtl 테이블의 상세 데이터와 가공된 참조값을 담습니다.
 *
 * @author   : intst
 * @fileName : CodeDetailVO.java
 * @since    : 2026. 1. 7.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CodeDetailVO {

    private String dtlCd;
    private String cdNm;
    private String refVal;

}
