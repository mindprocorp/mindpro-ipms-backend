package kr.co.mindpro.ipms.domain.auth.repository.db1;

import kr.co.mindpro.ipms.domain.auth.vo.SocialAuthMappVO;
import kr.co.mindpro.ipms.domain.auth.vo.SocialAuthVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface SocialAuthMapper {

    Optional<SocialAuthMappVO> findMappByProviderAndId(
        @Param("authApproach") String authApproach,
        @Param("authSeq") String authSeq
    );

    void insertSocialAuth(SocialAuthVO vo);

    void insertSocialAuthMapp(SocialAuthMappVO vo);

    void updateSocialAuthToken(
        @Param("authApproach") String authApproach,
        @Param("authSeq") String authSeq,
        @Param("authToken") String authToken
    );
}
