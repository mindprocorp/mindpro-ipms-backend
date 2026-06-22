package kr.co.mindpro.ipms.domain.ai.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class ReasoningFutureDateTool {

    @Tool(description = """
        기준일로부터 일정 기간(일, 월, 년) 후의 미래 날짜를 계산합니다. 
        마감일 자동 산출이나 유효 기간 만료일 계산 시 사용하십시오.
        결과 규격: '{yyyy-MM-dd}' 형태의 날짜 문자열로 반환됩니다.
        """)
    public String reasoningFutureDate(String baseDate, int amount, String unit) {
        try {
            LocalDate date = LocalDate.parse(baseDate);
            return switch (unit.toLowerCase()) {
                case "day", "days" -> date.plus(amount, ChronoUnit.DAYS).format(DateTimeFormatter.ISO_DATE);
                case "month", "months" -> date.plus(amount, ChronoUnit.MONTHS).format(DateTimeFormatter.ISO_DATE);
                case "year", "years" -> date.plus(amount, ChronoUnit.YEARS).format(DateTimeFormatter.ISO_DATE);
                default -> "ERROR|지원하지 않는 단위입니다: " + unit;
            };
        } catch (Exception e) {
            return "ERROR|날짜 형식이 올바르지 않거나 계산 중 오류가 발생했습니다.";
        }
    }
}
