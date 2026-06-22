package kr.co.mindpro.ipms.domain.ai.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
public class ReasoningDateDifferenceTool {

    @Tool(description = """
        두 날짜 사이의 차이(일, 월, 년)를 계산합니다. 
        마감일 임박 여부나 서비스 기간을 산출할 때 사용하십시오.
        결과 규격: '차이값: {수치}' 형태의 텍스트로 반환됩니다.
        """)
    public String reasoningDateDifference(String date1, String date2) {
        try {
            LocalDate start = LocalDate.parse(date1);
            LocalDate end = LocalDate.parse(date2);
            long diff = ChronoUnit.DAYS.between(start, end);
            return "차이값: " + diff;
        } catch (Exception e) {
            log.error("날짜 계산 오류: {} - {}", date1, date2, e);
            return "ERROR|날짜 형식이 올바르지 않거나 계산 중 오류가 발생했습니다.";
        }
    }
}
