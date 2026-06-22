package kr.co.mindpro.ipms.domain.ai.util;

import kr.co.mindpro.ipms.domain.ai.skill.AiSkill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 등록된 {@link AiSkill} Bean을 수집하여 AI 시스템 프롬프트에 삽입할 스킬 명세를 조합합니다.
 * 새로운 스킬은 AiSkill 인터페이스를 구현하고 @Component로 등록하기만 하면 자동으로 포함됩니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AiSkillLoader {

    private final List<AiSkill> skills;

    public String loadAllSkills() {
        return loadSkillsForIntent(null);
    }

    /**
     * 특정 의도(Intent)와 관련된 Skill을 선별적으로 로드합니다.
     * 현재는 모든 스킬을 로드하며, 추후 intent 기반 필터링으로 고도화 가능합니다.
     */
    public String loadSkillsForIntent(String intent) {
        StringBuilder sb = new StringBuilder();
        for (AiSkill skill : skills) {
            sb.append("\n---\n");
            sb.append("### [Skill Schema: ").append(skill.getName()).append("]\n");
            sb.append(skill.getContent());
            sb.append("\n");
        }
        if (sb.isEmpty()) {
            log.warn("로드된 AI Skill이 없습니다. AiSkill 구현체에 @Component가 있는지 확인하세요.");
            return "Skill 정보를 불러올 수 없습니다.";
        }
        return sb.toString();
    }
}
