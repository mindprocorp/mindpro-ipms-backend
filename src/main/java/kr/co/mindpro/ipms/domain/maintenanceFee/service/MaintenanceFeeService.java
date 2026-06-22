package kr.co.mindpro.ipms.domain.maintenanceFee.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.maintenanceFee.dto.request.MaintenanceFeeRequest;
import kr.co.mindpro.ipms.domain.maintenanceFee.dto.response.MaintenanceFeeResponse;

import java.util.List;

/**
 * @author : seokho
 * @fileName : MaintenanceFeeService.java
 * @since : 2026. 4. 2.
 */
public interface MaintenanceFeeService {

    void createMaintenanceFee(MaintenanceFeeRequest.CreateMaintenanceFeeRequest request);

    BaseSearchResponse<MaintenanceFeeResponse.MaintenanceFeeList> getMaintFeeListByAppSeq(BaseSearchRequest request);

    MaintenanceFeeResponse.MaintenanceFeeDetail getMaintenanceFeeDetail(String appSeq, String maintenanceFeeSeq);

    void softDeleteMaintenanceFee(String appSeq, String maintenanceFeeSeq);

    void multiSoftDeleteMaintenanceFee(String appSeq, List<String> maintenanceFeeSeq);

    void hardDeleteMaintenanceFee(String appSeq, String maintenanceFeeSeq);

    void multiHardDeleteMaintenanceFee(String appSeq, List<String> maintenanceFeeSeq);
}
