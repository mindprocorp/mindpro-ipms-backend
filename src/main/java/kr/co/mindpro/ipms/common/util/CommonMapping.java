package kr.co.mindpro.ipms.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 객체의 필드를 공통 모듈로 매핑하기 위한 메타데이터 정의
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonMapping {

    /** 매핑 타입 (예: "DATE", "PERSON") */
    String type();

    /** DB 공통 코드값 (예: "DUE_RECP") */
    String code() default "";


    String group() default ""; // "CFT"(분쟁) 또는 "APP"(출원)

    /** 필드 설명 */
    String description() default "";
}