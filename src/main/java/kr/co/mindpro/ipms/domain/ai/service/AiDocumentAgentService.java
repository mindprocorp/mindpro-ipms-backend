package kr.co.mindpro.ipms.domain.ai.service;

import org.springframework.web.multipart.MultipartFile;
import kr.co.mindpro.ipms.domain.ai.dto.response.DocumentAnalysisResponse;

public interface AiDocumentAgentService {
    DocumentAnalysisResponse analyzeAndRegisterDocument(MultipartFile file);
}
