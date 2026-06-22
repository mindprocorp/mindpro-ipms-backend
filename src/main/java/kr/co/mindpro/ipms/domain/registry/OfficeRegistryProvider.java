package kr.co.mindpro.ipms.domain.registry;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mindpro.ipms.common.dto.response.RegistryResponse;
import kr.co.mindpro.ipms.common.registry.RegistryProvider;
import kr.co.mindpro.ipms.domain.registry.repository.db1.OfficeMapper;
import lombok.RequiredArgsConstructor;

/**
 * 시스템 기초 정보 제공을 위한 사무소 레지스트리 공급자
 *
 * @author	 : intst
 * @fileName : OfficeRegistryProvider.java
 * @since	 : 2026. 1. 7.
 */
@Service
@RequiredArgsConstructor
public class OfficeRegistryProvider implements RegistryProvider {

    private final OfficeMapper officeMapper;

    @Override
    public String getRegistryType() { 
        return "office"; 
    }
    
    /**
     * 요청 타입이 "office"와 정확히 일치할 때만 처리 권한을 가집니다.
     */
    @Override
    public boolean supports(String type) {
        return "office".equals(type);
    }

    /**
     * 사무소 데이터만 리턴하며, 파라미터 type은 식별 용도로만 사용합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<RegistryResponse> getRegistryItems(String type) {
        return officeMapper.findAllActiveOffices().stream()
                .map(vo -> new RegistryResponse(
                        vo.getOfficeSeq(), 
                        vo.getOfficeShortName(), 
                        null
                ))
                .toList();
    }
}