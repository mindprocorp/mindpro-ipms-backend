package kr.co.mindpro.ipms.domain.ai.tool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchDocumentMasterTool {

    private final JdbcTemplate jdbcTemplate;

    @Tool(description = """
        등록/심판 업무 진행 시 필요한 '문서 마스터(docSeq 등)' 정보를 키워드로 검색합니다.
        문서 종류를 특정하거나 분류 코드를 찾을 때 사용하십시오.
        결과 규격: '한글설명(영문필드명): 값' 형식으로 반환됩니다.
        """)
    public String searchDocumentMaster(String docNm, String patType, String eventDiv) {
        try {
            StringBuilder sql = new StringBuilder("SELECT doc_seq, doc_nm, doc_div FROM utb_document_mst WHERE del_yn = 'N' AND doc_nm LIKE ?");
            List<Object> params = new ArrayList<>();
            params.add("%" + docNm + "%");
            if (StringUtils.hasText(patType)) { sql.append(" AND pat_type = ?"); params.add(patType); }
            if (StringUtils.hasText(eventDiv)) { sql.append(" AND event_div = ?"); params.add(eventDiv); }
            
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), params.toArray());
            if (list.isEmpty()) return "해당 명칭을 가진 문서를 찾을 수 없습니다.";
            StringBuilder sb = new StringBuilder();
            for (Map<String, Object> row : list) {
                sb.append("doc_seq: ").append(row.get("doc_seq"))
                  .append(", doc_nm: ").append(row.get("doc_nm"))
                  .append(", doc_div: ").append(row.get("doc_div"))
                  .append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("문서 마스터 조회 실패", e);
            return "조회 중 에러: " + e.getMessage();
        }
    }
}
