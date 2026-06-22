package kr.co.mindpro.ipms.domain.ai.tool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchSystemDictionaryTool {

    private final JdbcTemplate jdbcTemplate;

    @Tool(description = """
        [코드/마스터 정보 조회] 시스템에 등록된 공통 코드(상태코드, 구분코드 등)나 문서 마스터 정보를 검색합니다. 
        **주의**: 사건마감일, 출원번호 등 일반적인 데이터 필드명을 찾을 때는 이 도구 대신 `필드명검색` 도구를 사용하십시오.
        결과 규격: '코드명(코드값): 설명' 형식으로 반환됩니다.
        """)
    public String searchSystemDictionary(String category, String keyword) {
        try {
            StringBuilder result = new StringBuilder();
            if ("code_mst".equalsIgnoreCase(category)) {
                jdbcTemplate.query("SELECT grp_cd, cd_nm, note FROM utb_code_mst WHERE del_yn = 'N' AND cd_nm LIKE ?",
                        rs -> { result.append(String.format("Group: %s, Name: %s, Note: %s\n", rs.getString(1), rs.getString(2), rs.getString(3))); }, "%" + keyword + "%");
            } else if ("code_dtl".equalsIgnoreCase(category)) {
                jdbcTemplate.query("SELECT grp_cd, dtl_cd, cd_nm FROM utb_code_dtl WHERE del_yn = 'N' AND (cd_nm LIKE ? OR grp_cd LIKE ?)",
                        rs -> { result.append(String.format("Group: %s, Detail: %s, Name: %s\n", rs.getString(1), rs.getString(2), rs.getString(3))); }, "%" + keyword + "%", "%" + keyword + "%");
            } else if ("document_mst".equalsIgnoreCase(category)) {
                jdbcTemplate.query("SELECT doc_seq, doc_nm, ref_val FROM utb_document_mst WHERE del_yn = 'N' AND doc_nm LIKE ?",
                        rs -> { result.append(String.format("Seq: %s, Name: %s, Ref: %s\n", rs.getString(1), rs.getString(2), rs.getString(3))); }, "%" + keyword + "%");
            }
            return result.length() > 0 ? result.toString() : "조회된 사전 정보가 없습니다.";
        } catch (Exception e) {
            log.error("시스템 사전 조회 실패", e);
            return "조회 실패: " + e.getMessage();
        }
    }
}
