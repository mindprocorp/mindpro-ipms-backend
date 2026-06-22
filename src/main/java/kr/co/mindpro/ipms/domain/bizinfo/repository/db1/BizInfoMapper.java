package kr.co.mindpro.ipms.domain.bizinfo.repository.db1;

import kr.co.mindpro.ipms.domain.bizinfo.vo.BizInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 청구서 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : DuedateMapper.java
 * @since	 : 2026. 01. 07.
 */
@Mapper
public interface BizInfoMapper {
    /**
     * 새로운 사업자 정보 시퀀스 생성
     */
    String getNextBizInfoSeq();

    /**
     * 사무소별 사업자 정보 목록 조회
     */
    List<BizInfoVO> findAllByOffice(@Param("officeSeq") String officeSeq, @Param("offset") int offset,@Param("limit")  int limit);

    /**
     * 사업자 정보 상세 조회
     */
    BizInfoVO findById(@Param("bizInfoSeq") String bizInfoSeq, @Param("officeSeq") String officeSeq);

    /**
     * 사업자 정보 신규 저장 (이력 관리)
     */
    int insertBizInfo(BizInfoVO vo);


    /**
     * 사업자 정보 논리 삭제 (del_yn = 'Y')
     */
    int deleteBizInfo(@Param("bizInfoSeq") String bizInfoSeq,
                      @Param("officeSeq") String officeSeq,
                      @Param("userId") String userId);

    /**
     * 사업자 정보 수정
     */
    int updateBizInfo(BizInfoVO vo);
}