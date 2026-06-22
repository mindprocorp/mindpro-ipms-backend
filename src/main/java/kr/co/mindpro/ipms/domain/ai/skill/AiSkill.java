package kr.co.mindpro.ipms.domain.ai.skill;

/**
 * AI 시스템 프롬프트에 주입할 스킬(Skill) 명세를 정의하는 인터페이스.
 * 구현 클래스를 @Component로 등록하면 AiSkillLoader가 자동으로 수집합니다.
 */
public interface AiSkill {

    /** 스킬 식별 이름 (로그, 디버깅용) */
    String getName();

    /** 시스템 프롬프트에 삽입될 마크다운 형식의 스킬 명세 내용 */
    String getContent();
}
