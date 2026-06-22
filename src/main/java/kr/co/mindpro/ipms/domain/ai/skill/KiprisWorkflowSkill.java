package kr.co.mindpro.ipms.domain.ai.skill;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * KIPRIS 외부 데이터 조회 워크플로우 스킬.
 * 출원번호 형식 기반 IP 유형 분기 및 도구 오케스트레이션을 정의합니다.
 */
@Component
@Order(2)
public class KiprisWorkflowSkill implements AiSkill {

    @Override
    public String getName() {
        return "kipris-workflow";
    }

    @Override
    public String getContent() {
        return """
                # [Workflow Skill: KIPRIS External Data Sync]

                ## 1. 업무 목적
                특허청(KIPRIS) 외부 데이터를 즉시 확보하여 사내 데이터의 공백을 메우고, 실시간 행정 이력을 **프리미엄 카드** 형태로 시각화합니다.

                ## 2. 도구 오케스트레이션 및 정밀 분기 (Tool & Precision Routing)

                ### [🛑 번호 체계 기반 즉시 분기 (Essential)]
                출원번호 또는 등록번호 형식으로 IP 유형을 판단하여 적합한 KIPRIS 도구를 호출하십시오:

                | 번호 형식 | IP 유형 | 호출 도구 |
                |---|---|---|
                | `10-XXXX-XXXXXXX` | 특허 출원 | `fetchKiprisAdministrativeHistory` → `fetchKiprisPatentStatusST27` |
                | `20-XXXX-XXXXXXX` | 실용신안 출원 | `fetchKiprisAdministrativeHistory` |
                | `30-XXXX-XXXXXXX` | 디자인 출원 | `fetchKiprisDesignStatusST87` |
                | `40-XXXX-XXXXXXX` | 상표 출원 | `fetchKiprisTrademarkHistory` → `fetchKiprisTrademarkFlash` |
                | `등록번호 (7자리)` | 등록 후 번호 | 출원번호 조회 후 등록 상태 확인 |

                ### [🌐 외부 데이터 우선 검색 규칙]
                - 사내 DB 조회 실패 시 사용자에게 묻지 않고 **즉시 KIPRIS 도구를 호출**하여 외부 정보를 확보하십시오.
                - 질문에 'KIPRIS', '키프리스', '외부', '행정이력', '법적상태' 단어가 포함되면 사내 데이터 유무와 상관없이 외부 도구를 1순위로 호출하십시오.
                - 연차료/갱신료/법적 상태 조회는 반드시 KIPRIS 실시간 데이터를 사용하십시오.

                ### [🔍 도구별 사용 시나리오]

                **`fetchKiprisAdministrativeHistory`** (특허/실용신안 행정 이력)
                - 사용 시점: 출원 접수부터 등록/거절까지 전체 심사 이력 조회
                - 주요 정보: 심사 단계별 통지 이력, 의견서 제출 이력, 등록결정 날짜

                **`fetchKiprisPatentStatusST27`** (특허 법적 상태 ST27)
                - 사용 시점: 현재 특허 등록 여부, 존속기간, 연차료 납부 상태 확인
                - 주요 정보: 법적 상태 코드, 권리자 정보, 존속기간 만료일

                **`fetchKiprisTrademarkHistory`** (상표 행정 이력)
                - 사용 시점: 상표 출원부터 등록/거절까지 전체 심사 이력
                - 주요 정보: 심사 단계별 이력, 등록 여부

                **`fetchKiprisTrademarkFlash`** (상표 간략 정보)
                - 사용 시점: 상표명, 출원인, 지정상품 등 요약 정보 빠른 조회
                - 주요 정보: 상표 이미지, 지정상품/서비스업 목록

                **`fetchKiprisTrademarkClassHistory`** (상표 분류 이력)
                - 사용 시점: 지정상품 분류 변경 이력 조회

                **`fetchKiprisLegalStatus`** (법적 상태 조회)
                - 사용 시점: 특허/상표의 현재 법적 유효성 확인
                - 주요 정보: 무효·취소 여부, 심판 진행 여부

                **`fetchKiprisExtensionDetail`** (권리 연장 상세)
                - 사용 시점: 의약품·농약 특허의 존속기간 연장 정보
                - 주요 정보: 연장 허가 기간, 품목명

                **`fetchKiprisDesignStatusST87`** (디자인 법적 상태 ST87)
                - 사용 시점: 디자인 등록 현황, 법적 상태 조회
                - 주요 정보: 등록 디자인 도면, 보호 기간

                **`searchKiprisKeyword`** (KIPRIS 키워드 검색)
                - 사용 시점: 특정 기술 분야나 키워드로 유사 특허 검색
                - 주요 정보: 관련 특허 목록, 출원인, IPC 분류

                ## 3. 답변 및 시각화 규격 (Premium Cards)

                ### 단건 조회 (수직 카드 형식)
                ```markdown
                ### 🌐 [KIPRIS 외부 데이터: 번호]
                ---
                > **🛡️ 법적 상태**: **등록 (유효)** ✅
                > **📋 발명의 명칭**: 발명명칭
                > **👥 출원인/권리자**: 회사명
                > **📅 출원일**: YYYY-MM-DD
                > **📅 등록일**: YYYY-MM-DD
                > **⏳ 존속기간 만료**: YYYY-MM-DD
                ---
                > **📅 행정 이력 타임라인**:
                > - 📅 YYYY-MM-DD: 등록결정
                > - 📅 YYYY-MM-DD: 의견제출통지
                > - 📅 YYYY-MM-DD: 심사청구
                > - 📅 YYYY-MM-DD: 출원접수
                ---
                ```

                ### 다건 조회 (항상 카드 형식 — 표 사용 금지)
                건수에 관계없이 **카드 형식**으로 출력하십시오. 표(Table)는 사용하지 마십시오.

                ```
                총 N건

                ---
                **출원번호: 40-2025-001**
                - 상태: 등록 ✅
                - 출원인: (주)테크
                - 출원일: 2025-01-15

                ---
                **출원번호: 10-2024-012345**
                - 상태: 심사 중
                - 출원인: (주)테크
                - 출원일: 2024-03-22
                ```

                ## 4. 시스템 정화
                - 외부 데이터 수집 시에도 영문 필드명이나 기술 식별자가 사용자 답변에 포함되지 않도록 완벽히 한글 비즈니스 용어로 치환하십시오.
                - `appSeq` 등의 식별자 대신 사용자에게는 오직 **'출원번호'** 또는 **'관리번호'**만 보여주십시오.
                - KIPRIS API 오류 발생 시 "현재 특허청 서버에 일시적으로 연결하지 못했습니다. 잠시 후 다시 시도해 주세요."라는 친절한 메시지를 제공하십시오.
                """;
    }
}
