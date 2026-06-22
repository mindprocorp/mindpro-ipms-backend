package kr.co.mindpro.ipms.common.notification;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 이메일 인증 코드 발급/검증 서비스
 * - 6자리 숫자 코드 생성
 * - 5분 만료
 * - 메모리 기반 (운영 시 Redis로 전환 가능)
 */
@Slf4j
@Service
public class VerificationCodeService {

    private static final int CODE_LENGTH = 6;
    private static final long EXPIRATION_MINUTES = 5;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final Map<String, CodeEntry> codeStore = new ConcurrentHashMap<>();

    private record CodeEntry(String code, LocalDateTime expiresAt) {}

    public String generateCode(String email) {
        String code = String.format("%0" + CODE_LENGTH + "d",
                RANDOM.nextInt((int) Math.pow(10, CODE_LENGTH)));

        codeStore.put(email.toLowerCase(),
                new CodeEntry(code, LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES)));

        log.info("인증 코드 발급: email={}", email);
        return code;
    }

    public boolean verify(String email, String code) {
        CodeEntry entry = codeStore.get(email.toLowerCase());
        if (entry == null) {
            return false;
        }
        if (LocalDateTime.now().isAfter(entry.expiresAt())) {
            codeStore.remove(email.toLowerCase());
            return false;
        }
        if (entry.code().equals(code)) {
            codeStore.remove(email.toLowerCase());
            return true;
        }
        return false;
    }

    /** 만료된 코드 정리 (10분마다) */
    @Scheduled(fixedRate = 600_000)
    public void cleanupExpiredCodes() {
        LocalDateTime now = LocalDateTime.now();
        int before = codeStore.size();
        codeStore.entrySet().removeIf(e -> now.isAfter(e.getValue().expiresAt()));
        int removed = before - codeStore.size();
        if (removed > 0) {
            log.debug("만료 인증 코드 정리: {}건 제거", removed);
        }
    }
}
