package kr.co.mindpro.ipms.domain.user.service.impl;

import kr.co.mindpro.ipms.common.registry.RegistryProvider;
import kr.co.mindpro.ipms.common.dto.response.RegistryResponse;
import kr.co.mindpro.ipms.domain.user.repository.db1.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRegistryProvider implements RegistryProvider {
    private final UserMapper userMapper;

    @Override
    public String getRegistryType() { return "user"; }
    
    /**
     * 요청 타입이 "user"일 경우에만 처리를 허용합니다.
     */
    @Override
    public boolean supports(String type) {
        return getRegistryType().equals(type);
    }    

    @Override
    @Transactional(readOnly = true)
    public List<RegistryResponse> getRegistryItems(String type) {
        return userMapper.findAllUsers().stream()
                .filter(u -> "N".equalsIgnoreCase(u.getDelYn()))
                .map(u -> new RegistryResponse(u.getUserId(), u.getUserNameKo(), u.getUserCategoryCode()))
                .toList();
    }
}