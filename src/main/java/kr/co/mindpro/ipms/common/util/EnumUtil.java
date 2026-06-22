package kr.co.mindpro.ipms.common.util;

import kr.co.mindpro.ipms.common.enums.BaseEnum;

public class EnumUtil {

    /**
     * enum 데이터 조회
     * @param enumClass
     * @param code
     * @return
     * @param <E>
     */
    public static <E extends Enum<E> & BaseEnum>
    E fromCode(Class<E> enumClass, String code) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
