package kr.co.mindpro.ipms.common.nts;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.common.nts.dto.NtsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 국세청 API 비즈니스 서비스
 * 
 * @author	 : mindpro
 * @since	 : 2026. 1. 12.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NtsApiService {

    private final NtsApiClient ntsApiClient;
    private final ObjectMapper objectMapper;

    public ApiResponse<NtsDto.StatusResponse> getBusinessStatus(String businessNumber) {
        Map<String, Object> response;
        
        try {
            // 1. 외부 API 호출
            response = ntsApiClient.fetchStatus(businessNumber);
        } catch (Exception e) {
            log.error("NTS API Connection Error: {}", e.getMessage());
            // [BusinessException 적용] 공통 에러코드 사용 및 상세 메시지 전달
            throw new BusinessException("국세청 API 호출 중 오류가 발생했습니다.", ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 2. 응답 데이터 추출
        List<Object> dataList = (List<Object>) response.get("data");

        if (dataList == null || dataList.isEmpty()) {
            return ApiResponse.success(HttpStatus.OK.value(), "조회 결과가 없습니다.", null);
        }

        try {
            // 3. 응답 객체 매핑
            NtsDto.StatusResponse statusResponse = objectMapper.convertValue(dataList.get(0), NtsDto.StatusResponse.class);
            return ApiResponse.success(HttpStatus.OK.value(), "사업자 상태 조회 성공", statusResponse);
        } catch (Exception e) {
            log.error("NTS Data Mapping Error: {}", e.getMessage());
            throw new BusinessException("데이터 변환 중 오류가 발생했습니다.", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
