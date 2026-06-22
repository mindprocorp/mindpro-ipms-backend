package kr.co.mindpro.ipms.domain.registry;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mindpro.ipms.common.dto.response.RegistryResponse;
import kr.co.mindpro.ipms.common.registry.RegistryProvider;
import kr.co.mindpro.ipms.domain.registry.repository.db1.CodeRegMapper;
import lombok.RequiredArgsConstructor;

/**
 * [공통코드] 파라미터 type을 grp_cd로 매핑하여 처리하는 공급자
 */
@Service
@RequiredArgsConstructor
public class CodeRegistryProvider implements RegistryProvider {

    private final CodeRegMapper codeRegMapper;

    @Override
    public String getRegistryType() { 
        // 컨트롤러의 dynamicProviders 그룹에 포함시키기 위한 식별자
        return "common"; 
    }

    /**
     * 입력받은 type이 DB의 utb_code_mst.grp_cd에 존재하는지 확인
     */
    @Override
    @Transactional(readOnly = true)
    public boolean supports(String type) {
        return type != null && codeRegMapper.existsMasterGroup(type);
    }

    /**
     * type(grp_cd)을 조건으로 상세 코드(utb_code_dtl) 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<RegistryResponse> getRegistryItems(String type) {
        // type 파라미터가 곧 DB의 grp_cd가 됩니다.
        return codeRegMapper.findDetailsByGroup(type).stream()
                .map(vo -> new RegistryResponse(
                        vo.getDtlCd(),   // 코드값 (Value)
                        vo.getCdNm(),    // 코드명 (Label)
                        vo.getRefVal()  // 참조값 (Attribute)
                ))
                .toList();
    }
}
