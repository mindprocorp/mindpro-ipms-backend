package kr.co.mindpro.ipms.common.registry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.RegistryResponse;
import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.domain.registry.repository.db1.OfficeMapper;
import kr.co.mindpro.ipms.domain.registry.vo.OfficeVO;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * [Controller] 시스템 기초 정보 통합 조회 API
 * 인증 필요 경로와 비인증(Public) 경로를 구분하여 처리합니다.
 *
 * @author   : mindpro
 * @fileName : CommonRegistryController.java
 * @since    : 2026. 1. 7.
 */
@Tag(name = "Common Registry", description = "기초 정보 통합 조회 API")
@RestController
@RequestMapping("/api/v1/common/registry")
public class CommonRegistryController {

    private final Map<String, RegistryProvider> staticProviders;
    private final List<RegistryProvider> dynamicProviders;
    private final OfficeMapper officeMapper;

    public CommonRegistryController(List<RegistryProvider> providerList, OfficeMapper officeMapper) {
        this.officeMapper = officeMapper;
        // 1. "office", "country" 등 고정된 타입을 반환하는 Provider 분리
        this.staticProviders = providerList.stream()
                .filter(p -> !p.getRegistryType().equals("common"))
                .collect(Collectors.toMap(RegistryProvider::getRegistryType, p -> p));
        
        // 2. "common" 처럼 파라미터에 따라 유동적으로 작동하는 Provider 분리
        this.dynamicProviders = providerList.stream()
                .filter(p -> p.getRegistryType().equals("common"))
                .toList();
    }    

    /** [인증 필요] 일반 조회 */
    @Operation(summary = "레지스트리 통합 조회 (인증 필요)")
    @GetMapping("/{type}")
    public ApiResponse<List<RegistryResponse>> getRegistry(@PathVariable String type) {
        return processRegistry(type);
    }

    /** [비인증 허용] 공개용 조회 - 확장형 */
    @Operation(summary = "공개용 레지스트리 조회 (비인증)")
    @GetMapping("/public/{type}")
    public ApiResponse<List<RegistryResponse>> getPublicRegistry(@PathVariable String type) {
        // 보안을 위해 비인증 조회가 허용된 타입만 필터링 (화이트리스트)
        List<String> allowedPublicTypes = List.of("office", "dept-public"); 
        
        if (!allowedPublicTypes.contains(type)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        return processRegistry(type);
    }

    /** [비인증 허용] 초대코드로 사무소 검증 */
    @Operation(summary = "초대코드로 사무소 검증 (비인증)")
    @GetMapping("/public/verify-invite-code")
    public ApiResponse<RegistryResponse> verifyInviteCode(@RequestParam String code) {
        OfficeVO office = officeMapper.findByInviteCode(code)
                .orElseThrow(() -> new BusinessException("유효하지 않은 초대코드입니다.", ErrorCode.INVALID_REGISTRY_TYPE));
        return ApiResponse.success(200, "검증 성공",
                new RegistryResponse(office.getOfficeSeq(), office.getOfficeShortName(), null));
    }

    private ApiResponse<List<RegistryResponse>> processRegistry(String type) {
        // 1. 고정 맵에서 먼저 찾기 (성능 우선)
        RegistryProvider provider = staticProviders.get(type);
        
        // 2. 맵에 없다면 동적 Provider(CommonCode 등)에서 supports 체크
        if (provider == null) {
            provider = dynamicProviders.stream()
                    .filter(p -> p.supports(type))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REGISTRY_TYPE));
        }

        // 3. 수정된 인터페이스에 따라 type 파라미터 전달
        return ApiResponse.success(HttpStatus.OK.value(), "조회 성공", provider.getRegistryItems(type));
    }
}