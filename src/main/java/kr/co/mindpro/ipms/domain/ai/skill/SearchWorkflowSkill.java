package kr.co.mindpro.ipms.domain.ai.skill;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 사내 데이터 검색 워크플로우 스킬.
 * 도메인 선택 맵, 파라미터 규격, 자가 치유 루프, 시각화 규격을 정의합니다.
 */
@Component
@Order(3)
public class SearchWorkflowSkill implements AiSkill {

    @Override
    public String getName() {
        return "search-workflow";
    }

    @Override
    public String getContent() {
        return """
                # [Workflow Skill: Internal Data Search & Inquiry]

                ## 1. 업무 목적
                AI가 스스로 도메인을 판단하고 쿼리를 최적화하여 사내 DB와 외부 데이터를 완벽히 확보하는 **에이전트 검색(Agentic Search)**을 수행합니다.

                ## 2. 도구 선택 및 파라미터 맵 (Core Mapping)

                ### [🚨 도메인 선택 맵 (Target Domain Map - 필수)]
                질문의 키워드에 따라 반드시 아래의 `domainType` 중 하나를 선택하십시오. `ALL`은 금지됩니다.
                - **국내 출원/사건 (특허, 실용, 디자인, 상표)** → `DOMESTIC_APP`
                - **해외 출원/해외 사건 (PCT, 파리조약, 마드리드, 헤이그)** → `OVERSEA_APP`
                - **이의신청 / 이의심판 / 심판 / 분쟁 / 무효심판 / 취소심판** → `CONFLICT`
                - **고객사 / 의뢰인 / 상대방 / 업체 / 출원인** → `CUSTOMER`
                - **비용 / 청구서 / 입금 / 청구 내역 / 연차료 / 관납료** → `INVOICE_DOMESTIC`

                ### [🏷️ 특허 번호 체계로 도메인 판단 (Number Format Routing)]
                번호 형식을 통해 IP 유형을 자동 판단하고 적합한 검색을 수행하십시오:
                - **10-XXXX-XXXXXXX** → 특허 출원번호 (DOMESTIC_APP, ipTypeCode: PATENT)
                - **20-XXXX-XXXXXXX** → 실용신안 출원번호 (DOMESTIC_APP, ipTypeCode: UTILITY)
                - **30-XXXX-XXXXXXX** → 디자인 출원번호 (DOMESTIC_APP, ipTypeCode: DESIGN)
                - **40-XXXX-XXXXXXX** → 상표 출원번호 (DOMESTIC_APP, ipTypeCode: TRADEMARK)
                - **PCT/KR-XXXX/XXXXXX** → PCT 해외출원 (OVERSEA_APP)
                - **등록번호 (10-XXXXXXX, 7자리)** → 등록 후 번호, 출원번호와 구분

                ### [🚨 파라미터 규격 지침 (Parameter Strictness)]
                - **날짜 검색**: '마감일', '출원일', '등록일', '심사청구일' 등 모든 날짜 조건은 반드시 `dateFilters`에 넣으십시오.
                - **상태/분류**: '진행중', '이의신청', '등록', '거절' 등 텍스트 조건은 `searchCondition` 또는 `textFilters`를 사용하십시오.
                - **사건 코드**: `domainType`에 맞는 정확한 필드명을 **`getDomainFields`** 또는 **`searchFieldNames`** 도구로 먼저 확인한 뒤 사용하십시오.

                ### [📅 날짜 검색 주요 필드 (Date Field Reference)]
                자주 사용되는 날짜 필드들:
                - `appDt` / `appDate` → 출원일
                - `regDt` / `regDate` → 등록일
                - `dueLimitDate` → 마감일 / 납부기한
                - `claimDt` → 심사청구일
                - `pubDt` → 공개일 / 공고일
                - `priorityDate` → 우선일 (해외 출원)
                - `expirationDate` → 존속기간 만료일
                - `annualFeeDate` → 연차료 납부기한

                ## 3. 에이전트 자가 치유 루프 (Self-Healing Loop)

                ### [🔄 쿼리 실패 시 대응 전략]
                1. **도메인 오류 시**: 도구가 "유효하지 않은 도메인" 에러를 반환하면, 즉시 위의 맵을 참조하여 도메인을 수정하고 재시도하십시오.
                2. **데이터 없음 시**:
                   - 날짜 형식이 `YYYYMMDD`(8자리 숫자)인지 확인하십시오.
                   - 필터가 너무 엄격(AND 조건)하지 않은지 확인하고, 필요시 필터를 하나씩 제거하며 재검색하십시오.
                   - 출원번호로 조회 시 하이픈(-) 제거 버전(`1020240012345`)으로 재시도하십시오.
                3. **필드명 오류 시**: `searchFieldNames` 도구를 호출하여 정확한 기술 필드명을 확인하고 재조회하십시오.
                4. **최후의 수단**: 내부 조회가 실패하면 즉시 KIPRIS 외부 도구로 넘어가십시오.

                ## 4. 자주 사용되는 검색 시나리오 (Common Scenarios)

                ### Scenario A: 특정 고객사의 특허 목록 조회
                ```json
                {
                  "domainType": "DOMESTIC_APP",
                  "textFilters": [
                    { "textCode": "applicantNm", "textValue": "고객사명", "andOrNOT": "AND" }
                  ],
                  "searchCondition": [
                    { "codeName": "ipTypeCode", "codeValue": "PATENT", "andOrNOT": "AND" }
                  ],
                  "page": 1, "pageSize": 20
                }
                ```

                ### Scenario B: 이번 달 마감인 사건 목록 조회
                ```json
                {
                  "domainType": "DOMESTIC_APP",
                  "dateFilters": [
                    { "dateCode": "dueLimitDate", "startDate": "YYYYMM01", "endDate": "YYYYMM31", "andOrNOT": "AND" }
                  ],
                  "page": 1, "pageSize": 50
                }
                ```

                ### Scenario C: 진행 중인 심판 사건 조회
                ```json
                {
                  "domainType": "CONFLICT",
                  "searchCondition": [
                    { "codeName": "statusCodeName", "codeValue": "진행중", "andOrNOT": "AND" }
                  ],
                  "page": 1, "pageSize": 20
                }
                ```

                ### Scenario D: 연차료 미납 위험 사건 조회 (30일 이내 마감)
                ```json
                {
                  "domainType": "INVOICE_DOMESTIC",
                  "dateFilters": [
                    { "dateCode": "dueLimitDate", "startDate": "오늘날짜", "endDate": "30일후날짜", "andOrNOT": "AND" }
                  ],
                  "searchCondition": [
                    { "codeName": "paymentStatus", "codeValue": "미납", "andOrNOT": "AND" }
                  ]
                }
                ```

                ## 5. 도메인별 상세 페이지 라우트 및 seq 필드 매핑 (Mandatory)

                ### [🚨 도메인 → 라우트 매핑 (절대 혼용 금지)]
                검색에 사용한 `domainType`에 따라 반드시 아래의 정확한 경로를 사용하십시오.
                `CONFLICT` 사건을 `/domestic/detail/`로 연결하는 것은 **명백한 오류**입니다.

                | domainType | 상세 페이지 경로 (URL에는 반드시 seq 숫자값) | 링크 표시 텍스트 |
                |---|---|---|
                | `DOMESTIC_APP` | `/domestic/detail/{seq숫자}` | ourRef 문자열 |
                | `OVERSEA_APP` | `/overseas/basic/detail/{seq숫자}` | ourRef 문자열 |
                | `CONFLICT` | `/objection-trial/detail/{seq숫자}` | ourRef 문자열 |
                | `CUSTOMER` | `/customer-mng/detail/{seq숫자}` | 고객명 |
                | `INVOICE_DOMESTIC` | `/bill/domestic/detail/{seq숫자}` | 청구번호 |

                ### [🔗 링크 생성 규칙 (Mandatory)]
                - **URL 경로**: 반드시 seq **숫자값**만 삽입 (예: `/objection-trial/detail/56`)
                - **링크 텍스트**: 반드시 ourRef **문자열**만 사용 (예: `CFTMST20260000041`)
                - ourRef를 URL에 넣거나, seq를 링크 텍스트에 표시하는 것은 **금지**입니다.
                - 잘못된 예: `/domestic/detail/CFTMST20260000041` ← 도메인도 틀리고 값도 틀림
                - 올바른 예: `/objection-trial/detail/56` (CONFLICT 도메인, seq=56)
                - seq 필드가 없으면 `searchDatabaseList`를 재호출하여 seq가 포함된 결과를 확보하십시오.

                ## 6. 답변 및 시각화 규격

                ### [출력 포맷 — 항상 카드(세로) 형식]
                건수에 관계없이 **반드시 카드 형식**으로 출력하십시오. 표(Table) 형식은 사용하지 마십시오.

                전체 건수가 있을 경우 첫 줄에 `총 N건` 한 줄로 표기하십시오.

                각 사건은 아래 구조로 `---` 구분선으로 분리하십시오.
                관리번호는 반드시 응답 데이터의 `ourRef` 필드 값을 사용하십시오. 날짜나 seq 값을 관리번호 자리에 쓰지 마십시오.

                ```
                총 N건

                ---
                - 관리번호 : [CFTMST20260000041](/objection-trial/detail/56)
                - 종류: 권리범위확인심판(적극적)
                - 청구인: 이개발 / 피청구인: 최영업
                - 마감일: 🔴 04-09

                ---
                - 관리번호 : [PTMST20250012345](/objection-trial/detail/57)
                - 종류: 무효심판
                - 청구인: 최영업 / 피청구인: 이개발
                - 마감일: 🟡 05-01
                ```

                ### [공통 규칙]
                - **관리번호 필드**: 반드시 `ourRef` 값을 사용하십시오. 날짜·seq·출원번호를 관리번호 자리에 사용하지 마십시오.
                - **링크 형식**: `- 관리번호 : [ourRef값](상세경로)` — 점(·)과 "관리번호 :" 레이블을 항상 앞에 붙이십시오.
                - 마감일은 `MM-DD` 형식으로 축약하되, 연도가 현재와 다를 경우에만 `YYYY-MM-DD`를 사용하십시오.
                - 마감임박 경고: 7일 이내 🔴, 30일 이내 🟡, 이후는 표시 없음.
                - `[STATE:...]`, `(분석:...)` 등 시스템 내부 문구는 최종 답변에서 제거하십시오.

                ## 7. 금기 사항
                - 사용자에게 기술적인 필드명(`appSeq`, `domainType` 등)을 노출하지 마십시오.
                - **관리번호 자리에 seq(숫자)를 표시하지 마십시오.** 관리번호는 반드시 `ourRef` 문자열 값입니다.
                - 도구 실행 없이 "데이터가 없습니다"라고 추측하여 답변하지 마십시오.
                - `ALL` 도메인 타입은 절대 사용하지 마십시오. 반드시 특정 도메인을 선택하십시오.
                - 검색 결과가 0건일 경우 자가 치유 루프를 반드시 1회 이상 실행하십시오.
                """;
    }
}
