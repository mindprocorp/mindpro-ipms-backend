package kr.co.mindpro.ipms.domain.registry.repository.db1;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.registry.vo.OfficeVO;

/**
 * 사무소 정보 조회를 위한 MyBatis Mapper 인터페이스
 *
 * @author	 : intst
 * @fileName : OfficeMapper.java
 * @since	 : 2026. 1. 7.
 */
@Mapper
public interface OfficeMapper {
    /**
     * 사용 가능한 사무소 목록 조회 (del_yn = 'N', office_auth_yn = 'Y')
     */
    List<OfficeVO> findAllActiveOffices();

    Optional<OfficeVO> findByInviteCode(@Param("inviteCode") String inviteCode);

    int updateInviteCode(@Param("officeSeq") String officeSeq, @Param("inviteCode") String inviteCode);
}