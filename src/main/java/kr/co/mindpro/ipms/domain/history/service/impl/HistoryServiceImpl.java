package kr.co.mindpro.ipms.domain.history.service.impl;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.history.dto.request.HistoryRequest;
import kr.co.mindpro.ipms.domain.history.dto.response.HistoryResponse;
import kr.co.mindpro.ipms.domain.history.repository.db1.HistoryMapper;
import kr.co.mindpro.ipms.domain.history.service.HistoryService;
import kr.co.mindpro.ipms.domain.history.vo.ModifiedHistVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryMapper historyMapper;


    @Override
    @Transactional
    public HistoryResponse.ModifiedHistDetail saveHistory(HistoryRequest.ModifiedHistDetail request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        // 1. 히스토리 VO 빌더 주입
        ModifiedHistVO vo = ModifiedHistVO.builder()

                .tblSeq(request.tblSeq())
                .beforeValue(request.beforeValue())
                .afterValue(request.afterValue())
                .modifiedContent(request.modifiedContent())
                .note(request.note())
                .officeSeq(officeSeq)
                .modifiedDate(request.modifiedDate())
                .createUser(userSeq)
                .updateUser(userSeq)
                .delYn("N")
                .build();

        historyMapper.insertModifiedHist(vo);

        ModifiedHistVO savedVo = historyMapper.selectModifiedHistDetail(vo.getModifiedHistSeq(), officeSeq);
        return HistoryResponse.ModifiedHistDetail.builder()
                .modifiedHistSeq(savedVo.getModifiedHistSeq())
                .tblSeq(savedVo.getTblSeq())
                .beforeValue(savedVo.getBeforeValue())
                .afterValue(savedVo.getAfterValue())
                .modifiedContent(savedVo.getModifiedContent())
                .note(savedVo.getNote())
                .modifiedDate(savedVo.getModifiedDate())
                .createUser(savedVo.getCreateUser())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HistoryResponse.ModifiedHistDetail getHistoryDetail(String modifiedHistSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        ModifiedHistVO vo = historyMapper.selectModifiedHistDetail(modifiedHistSeq, officeSeq);
        if (vo == null) return null;

        return HistoryResponse.ModifiedHistDetail.builder()
                .modifiedHistSeq(vo.getModifiedHistSeq())
                .tblSeq(vo.getTblSeq())
                .beforeValue(vo.getBeforeValue())
                .afterValue(vo.getAfterValue())
                .modifiedContent(vo.getModifiedContent())
                .note(vo.getNote())
                .modifiedDate(vo.getModifiedDate())
                .createUser(vo.getCreateUser())
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<HistoryResponse.HistorySearchListDetail> getHistoryList(BaseSearchRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        request.setOfficeSeq(officeSeq);
        int page = request.getPage() > 0 ? request.getPage() : 1;
        int pageSize = request.getPageSize() > 0 ? request.getPageSize() : 10;
        request.setOffSet((page - 1) * pageSize);

        List<HistoryResponse.HistorySearchListDetail> list = historyMapper.selectHistoryList(request);
        int totalElements = historyMapper.selectHistoryListTotalCount(request);

        return BaseSearchResponse.of(list, totalElements, page, pageSize);
    }

    @Override
    @Transactional
    public <T> void compareAndLog(String tblSeq, String updateType, T oldObj, T newObj) {
        if (oldObj == null || newObj == null) {
            log.warn("Cannot compare history log: oldObj or newObj is null. tblSeq: {}", tblSeq);
            return;
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        Class<?> clazz = oldObj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object oldVal = field.get(oldObj);
                Object newVal = field.get(newObj);

                String beforeStr = oldVal == null ? "" : oldVal.toString();
                String afterStr = newVal == null ? "" : newVal.toString();

                if (!beforeStr.equals(afterStr)) {
                    // Extract Korean name from @Schema annotation
                    String fieldName = field.getName();
                    if (field.isAnnotationPresent(Schema.class)) {
                        Schema schema = field.getAnnotation(Schema.class);
                        if (schema.description() != null && !schema.description().isEmpty()) {
                            fieldName = schema.description();
                        }
                    }

                    ModifiedHistVO vo = ModifiedHistVO.builder()
                            .tblSeq(tblSeq)
                            .officeSeq(officeSeq)
                            .beforeValue(beforeStr)
                            .afterValue(afterStr)
                            .modifiedContent(updateType) // e.g. "수정", "상태변경"
                            .note(fieldName) // 항목명
                            .createUser(userSeq)
                            .updateUser(userSeq)
                            .build();

                    historyMapper.insertModifiedHist(vo);
                }
            } catch (IllegalAccessException e) {
                log.error("Failed to access field for history logging: {}", field.getName(), e);
            }
        }
    }
    @Override
    public void deleteModifiedHist(String modifiedHistSeq) {
        historyMapper.deleteModifiedHist(modifiedHistSeq, SecurityUtil.getOfficeSeq());
    }

    @Override
    @Transactional
    public void deleteModifiedHistList(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            historyMapper.deleteModifiedHistList(ids, SecurityUtil.getOfficeSeq());
        }
    }
}
