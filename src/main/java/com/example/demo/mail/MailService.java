package com.example.demo.mail;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender emailSender;

    public void sendMultipleMessage(MailDto mailDto) {
        String[] recipients = mailDto.getAddress();

        if (recipients != null && recipients.length > 0) {
            for (String recipient : recipients) {
                try {
                    if (isValidEmailAddress(recipient)) {
                        MimeMessage message = emailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(message, true);

                        // 메일 제목 설정
                        helper.setSubject(mailDto.getTitle().strip());

                        // 참조자 설정
                        if (mailDto.getCcAddress() != null) {
                            helper.setCc(mailDto.getCcAddress());
                        }

                        helper.setText(mailDto.getContent().strip(), false);

                        helper.setFrom(mailDto.getFrom().strip());
                        helper.setTo(recipient.trim());

                        // 파일이 있는 경우에는 첨부 파일 추가
                        if (mailDto.getF() != null && !mailDto.getF().isEmpty()) {
                            String fileName = StringUtils.cleanPath(mailDto.getF().getOriginalFilename().strip());
                            helper.addAttachment(MimeUtility.encodeText(fileName, "UTF-8", "B"),
                                    new ByteArrayResource(IOUtils.toByteArray(mailDto.getF().getInputStream())));
                        }

                        // 메일 전송
                        emailSender.send(message);
                        log.info("Mail sent successfully to: {}", recipient);
                    } else {
                        log.error("Invalid email address: {}", recipient);
                    }
                } catch (MessagingException | IOException e) {
                    // 메일 전송 중 오류가 발생한 경우 로깅
                    log.error("Error sending mail to {}: {}", recipient, e.getMessage());
                }
            }
        } else {
            log.error("No recipients specified for the email.");
        }
    }

    private boolean isValidEmailAddress(String email) {
        // 간단한 이메일 유효성 검사 예시
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }
}

