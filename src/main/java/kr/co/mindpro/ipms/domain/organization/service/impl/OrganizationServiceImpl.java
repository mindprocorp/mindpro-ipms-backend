package kr.co.mindpro.ipms.domain.organization.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mindpro.ipms.domain.organization.repository.db1.ApprTemplateMapper;
import kr.co.mindpro.ipms.domain.organization.repository.db1.DeptMapper;
import kr.co.mindpro.ipms.domain.organization.repository.db1.FormTemplateMapper;
import kr.co.mindpro.ipms.domain.organization.repository.db1.FormTemplateTargetMapper;
import kr.co.mindpro.ipms.domain.organization.repository.db1.DefaultCodeMapper;
import kr.co.mindpro.ipms.domain.organization.repository.db1.OfficeCodeMapper;
import kr.co.mindpro.ipms.domain.organization.service.OrganizationService;
import kr.co.mindpro.ipms.domain.organization.vo.ApprTemplateLineVO;
import kr.co.mindpro.ipms.domain.organization.vo.ApprTemplateVO;
import kr.co.mindpro.ipms.domain.organization.vo.DeptVO;
import kr.co.mindpro.ipms.domain.organization.vo.FormTemplateTargetVO;
import kr.co.mindpro.ipms.domain.organization.vo.FormTemplateVO;
import kr.co.mindpro.ipms.domain.organization.vo.OfficeCodeVO;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrganizationServiceImpl implements OrganizationService {

    private final DeptMapper deptMapper;
    private final DefaultCodeMapper defaultCodeMapper;
    private final OfficeCodeMapper officeCodeMapper;
    private final ApprTemplateMapper apprTemplateMapper;
    private final FormTemplateMapper formTemplateMapper;
    private final FormTemplateTargetMapper formTemplateTargetMapper;

    @Override
    public List<DeptVO> getDeptTree(String officeSeq) {
        return deptMapper.selectDeptTree(officeSeq);
    }

    @Override
    @Transactional
    public void createDept(DeptVO vo) {
        // 1. 기초 정보 설정 및 depth 계산
        if (vo.getParentDeptSeq() != null && !vo.getParentDeptSeq().isBlank()) {
            DeptVO parent = deptMapper.selectDeptBySeq(vo.getParentDeptSeq())
                    .orElseThrow(() -> new IllegalArgumentException("상위 부서를 찾을 수 없습니다."));
            
            int parentDepth = 0;
            try {
                parentDepth = (parent.getDepth() != null) ? Integer.parseInt(parent.getDepth()) : 0;
            } catch (NumberFormatException e) {
                log.warn("부모 부서의 depth 형식이 올바르지 않습니다: {}", parent.getDepth());
            }
            vo.setDepth(String.valueOf(parentDepth + 1));
            // 초기 insert 시 임시 경로 (deptSeq가 아직 없으므로)
            vo.setDeptPath((parent.getDeptPath() != null ? parent.getDeptPath() : "") + "/TEMP");
        } else {
            vo.setDepth("0");
            vo.setDeptPath("/TEMP");
        }

        // 2. 부서 등록 (MyBatis selectKey에 의해 deptSeq 채번됨)
        deptMapper.insertDept(vo);

        // 3. 채번된 deptSeq를 사용하여 최종 경로(deptPath) 업데이트
        if (vo.getParentDeptSeq() != null && !vo.getParentDeptSeq().isBlank()) {
            DeptVO parent = deptMapper.selectDeptBySeq(vo.getParentDeptSeq()).orElse(null);
            String pPath = (parent != null && parent.getDeptPath() != null) ? parent.getDeptPath() : "";
            vo.setDeptPath(pPath + "/" + vo.getDeptSeq());
        } else {
            vo.setDeptPath("/" + vo.getDeptSeq());
        }
        deptMapper.updateDept(vo);
        
        log.info("### 부서 등록 완료: deptSeq={}, deptName={}, deptPath={}", 
                vo.getDeptSeq(), vo.getDeptName(), vo.getDeptPath());
    }

    @Override
    @Transactional
    public void updateDept(DeptVO vo) {
        deptMapper.updateDept(vo);
        // 숨기기/보이기 시 하위 부서도 일괄 변경
        if (vo.getUseYn() != null) {
            deptMapper.updateDescendantsUseYn(vo.getDeptSeq(), vo.getUseYn());
        }
    }

    @Override
    @Transactional
    public void deleteDept(String deptSeq) {
        deptMapper.deleteDept(deptSeq);
    }

    @Override
    public List<OfficeCodeVO> getOfficeCodeList(String codeClass, String officeSeq) {
        return officeCodeMapper.selectByClassAndOffice(codeClass, officeSeq);
    }

    @Override
    @Transactional
    public OfficeCodeVO getOrCreateOfficeCode(String officeSeq, String codeClass, String codeName) {
        // 기존 코드 검색
        List<OfficeCodeVO> existing = officeCodeMapper.selectByClassAndOffice(codeClass, officeSeq);
        for (OfficeCodeVO code : existing) {
            if (code.getCodeName().equals(codeName)) {
                return code;
            }
        }

        // 없으면 자동 생성
        OfficeCodeVO newCode = new OfficeCodeVO();
        newCode.setOfficeSeq(officeSeq);
        newCode.setCodeClass(codeClass);
        newCode.setOfficeCode(codeClass + "_" + System.currentTimeMillis());
        newCode.setCodeName(codeName);
        newCode.setSortOrd(String.valueOf(existing.size() + 1));
        newCode.setCreateUser("SYSTEM");
        newCode.setUpdateUser("SYSTEM");
        officeCodeMapper.insertOfficeCode(newCode);

        return newCode;
    }

    @Override
    @Transactional
    public void updateOfficeCode(OfficeCodeVO vo) {
        officeCodeMapper.updateOfficeCode(vo);
    }

    @Override
    @Transactional
    public void deleteOfficeCode(String officeCodeSeq) {
        officeCodeMapper.deleteOfficeCode(officeCodeSeq);
    }

    @Override
    public List<ApprTemplateVO> getApprTemplateList(String officeSeq, String templateName) {
        return apprTemplateMapper.selectTemplateList(officeSeq, templateName);
    }

    @Override
    public ApprTemplateVO getApprTemplateDetail(String apprTemplateSeq) {
        ApprTemplateVO template = apprTemplateMapper.selectTemplateBySeq(apprTemplateSeq)
                .orElseThrow(() -> new IllegalArgumentException("결재선 템플릿을 찾을 수 없습니다."));
        template.setLines(apprTemplateMapper.selectTemplateLines(apprTemplateSeq));
        return template;
    }

    @Override
    @Transactional
    public void saveApprTemplate(ApprTemplateVO vo) {
        if (vo.getApprTemplateSeq() != null && !vo.getApprTemplateSeq().isBlank()) {
            apprTemplateMapper.updateTemplate(vo);
            apprTemplateMapper.deleteTemplateLines(vo.getApprTemplateSeq());
        } else {
            apprTemplateMapper.insertTemplate(vo);
        }

        if (vo.getLines() != null) {
            for (ApprTemplateLineVO line : vo.getLines()) {
                line.setApprTemplateSeq(vo.getApprTemplateSeq());
                line.setCreateUser(vo.getCreateUser());
                line.setUpdateUser(vo.getUpdateUser());
                apprTemplateMapper.insertTemplateLine(line);
            }
        }
    }

    @Override
    @Transactional
    public void deleteApprTemplate(String apprTemplateSeq) {
        apprTemplateMapper.deleteTemplateLines(apprTemplateSeq);
        apprTemplateMapper.deleteTemplate(apprTemplateSeq);
    }

    @Override
    public List<FormTemplateVO> getFormTemplateList(String officeSeq, String categoryCode, String templateName, String userMstSeq) {
        return formTemplateMapper.selectTemplateList(officeSeq, categoryCode, templateName, userMstSeq);
    }

    @Override
    public FormTemplateVO getFormTemplateDetail(String formTemplateSeq) {
        FormTemplateVO template = formTemplateMapper.selectTemplateBySeq(formTemplateSeq)
                .orElseThrow(() -> new IllegalArgumentException("서식 템플릿을 찾을 수 없습니다."));
        template.setTargets(formTemplateTargetMapper.selectTargetsByTemplateSeq(formTemplateSeq));
        return template;
    }

    @Override
    @Transactional
    public void saveFormTemplate(FormTemplateVO vo) {
        // 공통 규칙에 따라 SecurityUtil에서 소속 및 사용자 정보 추출
        vo.setOfficeSeq(SecurityUtil.getOfficeSeq());
        if (vo.getFormTemplateSeq() == null || vo.getFormTemplateSeq().isBlank()) {
            vo.setCreateUser(SecurityUtil.getUserInfoSeq());
        }
        vo.setUpdateUser(SecurityUtil.getUserInfoSeq());

        if (vo.getFormTemplateSeq() != null && !vo.getFormTemplateSeq().isBlank()) {
            formTemplateMapper.updateTemplate(vo);
            formTemplateTargetMapper.deleteTargetsByTemplateSeq(vo.getFormTemplateSeq());
        } else {
            formTemplateMapper.insertTemplate(vo);
        }

        if (vo.getTargets() != null) {
            for (FormTemplateTargetVO target : vo.getTargets()) {
                target.setFormTemplateSeq(vo.getFormTemplateSeq());
                target.setCreateUser(vo.getCreateUser());
                target.setUpdateUser(vo.getUpdateUser());
                formTemplateTargetMapper.insertTarget(target);
            }
        }
        
        log.info("### 서식 저장 완료: officeSeq={}, templateName={}, seq={}", 
                vo.getOfficeSeq(), vo.getTemplateName(), vo.getFormTemplateSeq());
    }

    @Override
    @Transactional
    public void deleteFormTemplate(String formTemplateSeq) {
        formTemplateTargetMapper.deleteTargetsByTemplateSeq(formTemplateSeq);
        formTemplateMapper.deleteTemplate(formTemplateSeq);
    }

    @Override
    @Transactional
    public void initializeDefaultCodes(String officeSeq, String officeName) {
        // 1. 시드 코드 복사
        List<OfficeCodeVO> defaults = defaultCodeMapper.selectAllDefaults();
        for (OfficeCodeVO seed : defaults) {
            OfficeCodeVO code = new OfficeCodeVO();
            code.setOfficeSeq(officeSeq);
            code.setCodeClass(seed.getCodeClass());
            code.setOfficeCode(seed.getCodeClass() + "_" + seed.getCodeName());
            code.setCodeName(seed.getCodeName());
            code.setCodeNameEn(seed.getCodeNameEn());
            code.setSortOrd(seed.getSortOrd());
            code.setUseYn(seed.getUseYn());
            code.setCreateUser("SYSTEM");
            code.setUpdateUser("SYSTEM");
            officeCodeMapper.insertOfficeCode(code);
        }
        log.info("### 사무소 기본 코드 초기화 완료: officeSeq={}, 총 {}건", officeSeq, defaults.size());

        // 2. 루트 부서 생성 (회사명)
        DeptVO rootDept = new DeptVO();
        rootDept.setOfficeSeq(officeSeq);
        rootDept.setDeptCode(officeName);
        rootDept.setDeptName(officeName);
        rootDept.setDepth("0");
        rootDept.setSortOrd("1");
        rootDept.setCreateUser("SYSTEM");
        rootDept.setUpdateUser("SYSTEM");
        deptMapper.insertDept(rootDept);
        rootDept.setDeptPath("/" + rootDept.getDeptSeq());
        deptMapper.updateDept(rootDept);
        log.info("### 루트 부서 생성 완료: officeSeq={}, deptName={}", officeSeq, officeName);
    }
}
