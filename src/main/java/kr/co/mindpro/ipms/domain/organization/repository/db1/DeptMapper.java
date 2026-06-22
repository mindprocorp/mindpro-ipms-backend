package kr.co.mindpro.ipms.domain.organization.repository.db1;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.organization.vo.DeptVO;

@Mapper
public interface DeptMapper {

    List<DeptVO> selectDeptTree(@Param("officeSeq") String officeSeq);

    Optional<DeptVO> selectDeptBySeq(@Param("deptSeq") String deptSeq);

    int insertDept(DeptVO vo);

    int updateDept(DeptVO vo);

    int deleteDept(@Param("deptSeq") String deptSeq);

    int updateDescendantsUseYn(@Param("deptSeq") String deptSeq, @Param("useYn") String useYn);

    int updateChildPaths(@Param("oldPath") String oldPath,
                         @Param("newPath") String newPath,
                         @Param("officeSeq") String officeSeq);
}
