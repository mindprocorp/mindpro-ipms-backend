package kr.co.mindpro.ipms.domain.ai.tool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class KiprisKeywordSearchTool {

    private final RestTemplate restTemplate;

    @Value("${api.kipris.key}") private String kiprisApiKey;
    @Value("${api.kipris.word-search-url}") private String wordSearchUrl;

    @Tool(description = """
        KIPRIS 오픈 API를 사용하여 키워드로 외부 지식재산권 목록을 검색합니다. 
        우리 DB에 없는 신규 건이나 외부 등록 현황을 파악할 때 호출하십시오.
        결과 규격: '출원번호(appNo): 값' 등이 포함된 마크다운 표 또는 리스트 텍스트로 반환됩니다.
        """)
    public String searchKiprisByKeyword(String keyword, String yearLimit) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(wordSearchUrl)
                    .queryParam("word", keyword)
                    .queryParam("year", (StringUtils.hasText(yearLimit) ? yearLimit : "0"))
                    .queryParam("patent", "true").queryParam("utility", "true")
                    .queryParam("accessKey", kiprisApiKey).build().toUriString();
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            log.error("KIPRIS 키워드 검색 실패", e);
            return "KIPRIS 검색 실패: " + e.getMessage();
        }
    }
}
