package kr.co.mindpro.ipms.common.notification;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 공통 이메일 발송 서비스 (HTML 템플릿 기반)
 *
 * @author  : mindpro
 * @since   : 2026. 03. 16.
 */
@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromEmail;
    private final String adminEmail;
    private String baseLayout;

    public EmailService(
            @org.springframework.beans.factory.annotation.Autowired(required = false) JavaMailSender mailSender,
            @Value("${spring.mail.username:no-reply@localhost}") String fromEmail,
            @Value("${app.admin-email:info@mindpro.co.kr}") String adminEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
        this.adminEmail = adminEmail;
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        String html = buildHtml(
                "PASSWORD RESET",
                "비밀번호를<br>재설정해 주세요",
                "안녕하세요.<br>비밀번호 재설정 요청이 접수되었습니다.<br>아래 버튼을 눌러 새 비밀번호를 설정해 주세요.",
                buttonBlock(resetLink, "비밀번호 재설정하기"),
                "유의사항",
                "&middot; 본 링크는 발송 시점으로부터 <span style=\"color:#111111;font-weight:500;\">5분간</span> 유효합니다.<br>"
                + "&middot; 본인이 요청하지 않은 경우 이 메일을 무시하세요."
        );
        send(to, "[IPMS] 비밀번호 재설정 안내", html);
    }

    public void sendVerificationEmail(String to, String code) {
        String html = buildHtml(
                "EMAIL VERIFICATION",
                "이메일 인증을<br>완료해 주세요",
                "안녕하세요.<br>IPMS 회원가입을 위한 이메일 인증 코드입니다.<br>아래 인증 코드를 입력해 주세요.",
                codeBlock(code),
                "유의사항",
                "&middot; 인증 코드는 발송 시점으로부터 <span style=\"color:#111111;font-weight:500;\">5분간</span> 유효합니다.<br>"
                + "&middot; 본인이 요청하지 않은 경우 이 메일을 무시하세요."
        );
        send(to, "[IPMS] 이메일 인증 코드", html);
    }

    /**
     * 개인 회원가입 성공 시 관리자(info@mindpro.co.kr) 알림 메일 발송
     *
     * @param memberName    신규 회원 성명
     * @param memberEmail   신규 회원 이메일(로그인 ID)
     * @param mobileNo      신규 회원 연락처
     * @param memberType    회원 구분 코드 (INDIVIDUAL 등)
     * @param registeredAt  가입 일시 (yyyy-MM-dd HH:mm:ss)
     */
    public void sendNewMemberNotification(String memberName, String memberEmail,
                                           String mobileNo, String memberType,
                                           String registeredAt) {
        String content = infoTableBlock(
                new String[][]{{
                        "성명", memberName != null ? memberName : "-",
                        "이메일/아이디", memberEmail != null ? memberEmail : "-",
                        "연락처", mobileNo != null ? mobileNo : "-",
                        "회원구분", memberType != null ? memberType : "-",
                        "가입일시", registeredAt != null ? registeredAt : "-"
                }}
        );
        String html = buildHtml(
                "NEW MEMBER REGISTRATION",
                "신규 회원이<br>가입했습니다",
                "새로운 개인 회원이 IPMS에 가입하였습니다.<br>아래 정보를 확인해주세요.",
                content,
                "안내",
                "&middot; 본 메일은 시스템 자동 발송입니다.<br>&middot; 회원 상세 정보는 IPMS 관리자 페이지에서 확인하세요."
        );
        send(adminEmail, "[IPMS] 신규 회원가입 알림 - " + (memberName != null ? memberName : memberEmail), html);
    }

    /**
     * 사업자 회원가입 성공 시 관리자(info@mindpro.co.kr) 알림 메일 발송
     *
     * @param memberName    담당자 성명
     * @param memberEmail   담당자 이메일(로그인 ID)
     * @param mobileNo      담당자 연락처
     * @param corpName      회사명
     * @param bizRegNo      사업자등록번호
     * @param ceoName       대표자명
     * @param registeredAt  가입 일시 (yyyy-MM-dd HH:mm:ss)
     */
    public void sendNewCorporateNotification(String memberName, String memberEmail,
                                              String mobileNo, String corpName,
                                              String bizRegNo, String ceoName,
                                              String register, String registeredAt) {
        String content = infoTableBlock(
                new String[][]{{
                        "담당자명", memberName != null ? memberName : "-",
                        "이메일/아이디", memberEmail != null ? memberEmail : "-",
                        "연락처", mobileNo != null ? mobileNo : "-",
                        "회사명", corpName != null ? corpName : "-",
                        "사업자번호", bizRegNo != null ? bizRegNo : "-",
                        "대표자", ceoName != null ? ceoName : "-",
                        "가입일시", registeredAt != null ? registeredAt : "-"
                }}
        );
        String html = buildHtml(
                "NEW CORPORATE REGISTRATION",
                "신규 " + register + "가<br>가입했습니다",
                "새로운 " + register + " 회원이 IPMS에 가입하였습니다.<br>아래 정보를 확인해주세요.",
                content,
                "안내",
                "&middot; 본 메일은 시스템 자동 발송입니다.<br>&middot; 회원 상세 정보는 IPMS 관리자 페이지에서 확인하세요."
        );
        send(adminEmail, "[IPMS] 신규 " + register + " 회원가입 알림 - " + (corpName != null ? corpName : memberEmail), html);
    }

    private void send(String to, String subject, String htmlBody) {
        if (mailSender == null) {
            log.warn("메일 발송 스킵 (SMTP 미설정): to={}", to);
            return;
        }
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(msg);
            log.info("이메일 발송 성공: {}", to);
        } catch (Exception e) {
            log.warn("이메일 발송 실패: to={}, error={}", to, e.getMessage());
        }
    }

    private String buildHtml(String subtitle, String title, String body,
                             String content, String infoTitle, String infoBody) {
        return loadBaseLayout()
                .replace("{{SUBTITLE}}", subtitle)
                .replace("{{TITLE}}", title)
                .replace("{{BODY}}", body)
                .replace("{{CONTENT}}", content)
                .replace("{{INFO_TITLE}}", infoTitle)
                .replace("{{INFO_BODY}}", infoBody);
    }

    private String loadBaseLayout() {
        if (baseLayout == null) {
            try (InputStream is = new ClassPathResource("templates/email/base-layout.html").getInputStream()) {
                baseLayout = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.error("이메일 템플릿 로드 실패", e);
                return "";
            }
        }
        return baseLayout;
    }

    private static String buttonBlock(String href, String label) {
        return """
            <table width="100%%" cellpadding="0" cellspacing="0"><tr>
              <td style="padding:32px 48px 0;">
                <table width="100%%" cellpadding="0" cellspacing="0"><tr>
                  <td style="background:#111111;border-radius:12px;">
                    <a href="%s" target="_blank" style="display:block;padding:17px;color:#ffffff;font-size:14px;font-weight:500;text-decoration:none;text-align:center;letter-spacing:0.5px;">%s</a>
                  </td>
                </tr></table>
              </td>
            </tr></table>
            """.formatted(href, label);
    }

    private static String codeBlock(String code) {
        return """
            <table width="100%%" cellpadding="0" cellspacing="0"><tr>
              <td style="padding:32px 48px 0;">
                <table width="100%%" cellpadding="0" cellspacing="0"><tr>
                  <td style="background:#F7F8FA;border-radius:12px;padding:28px;text-align:center;">
                    <p style="margin:0 0 8px;font-size:12px;color:#999999;">인증 코드</p>
                    <p style="margin:0;font-size:32px;font-weight:700;color:#111111;letter-spacing:8px;">%s</p>
                  </td>
                </tr></table>
              </td>
            </tr></table>
            """.formatted(code);
    }

    /**
     * 회원 정보를 행(row) 형태로 나열하는 정보 테이블 블록 생성.
     * 입력: 1차원 배열로 [레이블, 값, 레이블, 값, ...] 순서.
     */
    private static String infoTableBlock(String[][] groups) {
        StringBuilder rows = new StringBuilder();
        for (String[] pairs : groups) {
            for (int i = 0; i + 1 < pairs.length; i += 2) {
                rows.append(String.format(
                    "<tr>" +
                    "<td style=\"padding:6px 0;font-size:12px;color:#888888;white-space:nowrap;width:90px;\">%s</td>" +
                    "<td style=\"padding:6px 0;font-size:13px;color:#111111;font-weight:500;\">%s</td>" +
                    "</tr>",
                    pairs[i], pairs[i + 1]
                ));
            }
        }
        return String.format(
            "<table width=\"100%%%%\" cellpadding=\"0\" cellspacing=\"0\"><tr>" +
            "<td style=\"padding:28px 48px 0;\">" +
            "<table width=\"100%%%%\" cellpadding=\"0\" cellspacing=\"0\" " +
            "style=\"border-collapse:collapse;\">%s</table>" +
            "</td></tr></table>",
            rows
        );
    }
}
