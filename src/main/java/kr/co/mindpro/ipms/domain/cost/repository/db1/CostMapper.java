package kr.co.mindpro.ipms.domain.cost.repository.db1;

import kr.co.mindpro.ipms.domain.cost.dto.response.AnnuityYearResponse;
import kr.co.mindpro.ipms.domain.cost.dto.response.CostDetailResponse;
import kr.co.mindpro.ipms.domain.cost.vo.CostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 비용 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : CostMapper.java
 * @since	 : 2026. 01. 07.
 */
@Mapper
public interface CostMapper {

    /** [조회] 특정 업무에 연결된 모든 비용 리스트 조회 */
    List<CostVO> findAllByWork(@Param("tblSeq") String tblSeq, @Param("officeSeq") String officeSeq);

    /** [조회] 조건별 비용 검색 */
    List<CostVO> findCostList(CostVO searchVO);

    /** [저장] 비용 Mst 등록 */
    int insertCostMst(CostVO vo);

    /** [저장] 비용 Mapp 등록 */
    int insertCostMapp(CostVO vo);

    CostVO getAnnuityYearDetail(String tblSeq, String officeSeq, String costSeq);

    CostVO getRenewalMngDetail(String tblSeq, String officeSeq, String costSeq);

    /** * [삭제 1] 비용 마스터(Mst) 논리 삭제
     * Mapp을 참조하여 연관된 본체 데이터를 먼저 삭제 처리
     */
    int deleteCostMstByWork(@Param("tblSeq") String tblSeq, @Param("officeSeq") String officeSeq);

    /** * [삭제 2] 비용 매핑(Mapp) 논리 삭제
     * 관계 테이블을 삭제 처리 (영향받은 행 수 반환)
     */
    void deleteCostMappByWork(@Param("tblSeq") String tblSeq, @Param("officeSeq") String officeSeq);

    /**
     * 시퀀스 미리 채증
     */
    String getNextCostSeq();
    String getNextMappingCostSeq();

    /** [수정] 필요 시 금액 정보만 수정 (기존 로직 유지용) */
    int updateCostAmount(CostVO vo);

    int getDuplicateAnnuityYearCnt(CostVO vo);

    int getDuplicateRenewalMngCnt(CostVO vo);

    /** (tblSeq, category) 기준으로 기존 mapp의 mapping_cost_seq 조회 */
    String findMappingCostSeqByTblSeqAndCategory(
            @Param("officeSeq") String officeSeq,
            @Param("tblSeq") String tblSeq,
            @Param("costCategoryCode") String costCategoryCode
    );

    int updateCostMst(CostVO vo);

    int softDeleteCost(
            @Param("officeSeq") String officeSeq,
            @Param("costSeq") String costSeq,
            @Param("updateUser") String updateUser
    );

    int softDeleteCostByList(
            @Param("officeSeq") String officeSeq,
            @Param("updateUser") String updateUser,
            @Param("costSeqList") List<String> costSeqList
    );

    int softDeleteCostMapp(
            @Param("officeSeq") String officeSeq,
            @Param("tblSeq") String tblSeq,
            @Param("mappingCostSeq") String mappingCostSeq,
            @Param("updateUser") String updateUser
    );

    int softDeleteCostMappByList(
            @Param("officeSeq") String officeSeq,
            @Param("updateUser") String updateUser,
            @Param("tblSeq") String tblSeq,
            @Param("mappingCostSeqList") List<String> mappingCostSeqList
    );

    int hardDeleteCost(
            @Param("officeSeq") String officeSeq,
            @Param("tblSeq") String tblSeq,
            @Param("costSeq") String costSeq
    );

    int hardDeleteCostByList(
            @Param("officeSeq") String officeSeq,
            @Param("costSeqList") List<String> costSeqList
    );

    int hardDeleteCostMapp(
            @Param("officeSeq") String officeSeq,
            @Param("tblSeq") String tblSeq,
            @Param("mappingCostSeq") String mappingCostSeq
    );

    int hardDeleteCostMappByList(
            @Param("officeSeq") String officeSeq,
            @Param("tblSeq") String tblSeq,
            @Param("mappingCostSeqList") List<String> mappingCostSeqList
    );

}