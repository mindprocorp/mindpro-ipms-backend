package kr.co.mindpro.ipms.domain.registry;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mindpro.ipms.common.dto.response.RegistryResponse;
import kr.co.mindpro.ipms.common.registry.RegistryProvider;
import kr.co.mindpro.ipms.domain.registry.repository.db1.CountryMapper;
import lombok.RequiredArgsConstructor;

/**
 * @author : seokho
 * @fileName : CountryRegistryProvider.java
 * @since : 2026. 04. 28.
 * @description : 국가 목록 레지스트리 공급자
 *                GET /api/v1/common/registry/country 로 노출됩니다.
 */
@Service
@RequiredArgsConstructor
public class CountryRegistryProvider implements RegistryProvider {

    private final CountryMapper countryMapper;

    @Override
    public String getRegistryType() {
        return "country";
    }

    @Override
    public boolean supports(String type) {
        return "country".equals(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistryResponse> getRegistryItems(String type) {
        return countryMapper.findAllCountries().stream()
                .map(vo -> new RegistryResponse(
                        vo.getCtryCode(),   // id = 국가코드
                        vo.getCtryNmKo(),   // label = 국가명(한글)
                        vo.getCtryNmEn()    // attributes = 국가명(영문)
                ))
                .toList();
    }
}
