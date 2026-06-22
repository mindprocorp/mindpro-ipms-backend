package kr.co.mindpro.ipms.domain.registry.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @author : seokho
 * @fileName : CountryVO.java
 * @since : 2026. 04. 28.
 * @description : 국가 정보 Value Object (utb_ctry 테이블)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CountryVO {

    /** 국가 코드 */
    private String ctryCode;

    /** 국가명 (한글) */
    private String ctryNmKo;

    /** 국가명 (영문) */
    private String ctryNmEn;
}
