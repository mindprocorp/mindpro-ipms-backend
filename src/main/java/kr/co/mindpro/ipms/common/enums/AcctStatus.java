package kr.co.mindpro.ipms.common.enums;

/**
 * ACCT_STATUS 그룹 코드 (utb_code_dtl).
 * dtl_cd 값과 cd_nm을 한 곳에서 관리하여 매직 스트링 사용을 방지한다.
 */
public enum AcctStatus implements BaseEnum {
    PENDING("PENDING", "대기"),
    ACTIVE("ACTIVE", "사용중"),
    LOCKED("LOCKED", "잠김");

    public static final String GROUP_CODE = "ACCT_STATUS";

    private final String code;
    private final String name;

    AcctStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
