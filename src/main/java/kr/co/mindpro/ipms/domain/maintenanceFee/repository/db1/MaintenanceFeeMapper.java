package kr.co.mindpro.ipms.domain.maintenanceFee.repository.db1;

import kr.co.mindpro.ipms.domain.maintenanceFee.dto.request.MaintenanceFeeRequest;
import kr.co.mindpro.ipms.domain.maintenanceFee.vo.MaintenanceFeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : seokho
 * @fileName : MaintenanceFeeMapper.java
 * @since : 2026. 4. 3.
 */
@Mapper
public interface MaintenanceFeeMapper {

    int getDuplicateMaintenanceCnt(String officeSeq, String appSeq, String maintenanceFeeSeq);

    int insertMaintenanceFee(MaintenanceFeeVO maintenanceFeeVO);

    int updateMaintenanceFee(MaintenanceFeeVO maintenanceFeeVO);

    int getMaintenanceFeeCnt(String officeSeq, String appSeq);

    List<MaintenanceFeeVO> getMaintenanceFeeList(String officeSeq, String appSeq);

    MaintenanceFeeVO getMaintenanceFee(String maintenanceFeeSeq, String officeSeq, String appSeq);

    int softDeleteMaintenanceFee(String maintenanceFeeSeq, String officeSeq, String appSeq, String userSeq);

    int softDeleteMaintenanceFeeByList(@Param("maintenanceFeeSeqList") List<String> maintenanceFeeSeqList, @Param("officeSeq") String officeSeq, @Param("appSeq") String appSeq, @Param("userSeq") String userSeq);

    int hardDeleteMaintenanceFee(String maintenanceFeeSeq, String officeSeq, String appSeq);

    int hardDeleteMaintenanceFeeByList(@Param("maintenanceFeeSeqList") List<String> maintenanceFeeSeqList, @Param("officeSeq") String officeSeq, @Param("appSeq") String appSeq);
}
