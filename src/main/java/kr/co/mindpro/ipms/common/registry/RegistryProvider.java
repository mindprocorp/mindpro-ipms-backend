package kr.co.mindpro.ipms.common.registry;

import kr.co.mindpro.ipms.common.dto.response.RegistryResponse;
import java.util.List;

/**
 * 도메인별 기초 데이터를 공급하기 위한 공통 인터페이스 규격
 *
 * @author	 : mindpro
 * @fileName	 : RegistryProvider.java
 * @since	 : 2026. 1. 7.
 */
public interface RegistryProvider {
    /**
     * 공급자의 대표 유형을 반환합니다.
     * 고정 데이터(office 등)는 고유 명칭을, 공통 코드군은 'common' 등을 반환합니다.
     */
    String getRegistryType();

    /**
     * 요청된 타입(type)을 이 공급자가 처리할 수 있는지 여부를 확인합니다.
     * @param type 요청된 레지스트리 식별자
     */
    default boolean supports(String type) {
        return getRegistryType().equals(type);
    }

    /**
     * 요청된 타입에 해당하는 기초 데이터 목록을 반환합니다.
     * @param type 요청된 레지스트리 식별자 (grp_cd 등)
     */
    List<RegistryResponse> getRegistryItems(String type);
}