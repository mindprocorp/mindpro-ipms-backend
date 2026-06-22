package kr.co.mindpro.ipms.common.util;

import java.time.LocalDate;

import io.hypersistence.tsid.TSID;

public class IdGenerator {
    /**
     * F(File) + 현재년도 + TSID 생성 (F2026_0ET6YJ9G6S1QE (총 19자)
     * 시간순 정렬이 가능하며 varchar(30)에 넉넉히 들어감
     */
    public static String generateTSID() {
    		String year = String.valueOf(LocalDate.now().getYear());
        String tsid = TSID.fast().toString();
        return String.format("F%s_%s", year, tsid);
    }
    
    /**
     * type + 현재년도 + TSID 생성 (G2026_0ET6YJ9G6S1QE (총 19자)
     * 시간순 정렬이 가능하며 varchar(30)에 넉넉히 들어감
     */
    public static String generateTSID(String type) {
        String code = switch (type.toUpperCase()) {
            case "PRODUCT" -> "G";
            case "LOCARNO" -> "L";
            default -> throw new RuntimeException("Invalid type");
        };

        String year = String.valueOf(LocalDate.now().getYear());
        String tsid = TSID.fast().toString();

        return String.format("%s%s_%s", code, year, tsid);
    }    
}
