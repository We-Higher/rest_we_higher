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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender emailSender;

    public void sendMultipleMessage(MailDto mailDto) throws MessagingException, IOException, TemplateException {
        // file 객체 널 체크
        if (mailDto.getF() == null || mailDto.getF().isEmpty()) {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            //메일 제목 설정
            helper.setSubject(mailDto.getTitle().strip());

            //참조자 설정
            helper.setCc(mailDto.getCcAddress());

            helper.setText(mailDto.getContent().strip(), false);

            helper.setFrom(mailDto.getFrom().strip());

            //수신자 한번에 전송
            helper.setTo(mailDto.getAddress());
            emailSender.send(message);

            // 파일이 없는 경우에 대한 처리
            log.warn("No file provided for email attachment. Sending email without attachment.");

            // 파일이 없다면 첨부 파일을 추가하지 않음
        } else {
            // 파일이 있는 경우에는 첨부 파일을 추가
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            //메일 제목 설정
            helper.setSubject(mailDto.getTitle().strip());

            //참조자 설정
            helper.setCc(mailDto.getCcAddress());

            helper.setText(mailDto.getContent().strip(), false);

            helper.setFrom(mailDto.getFrom().strip());

            //첨부 파일 설정
            String fileName = StringUtils.cleanPath(mailDto.getF().getOriginalFilename().strip());

            helper.addAttachment(MimeUtility.encodeText(fileName, "UTF-8", "B"), new ByteArrayResource(IOUtils.toByteArray(mailDto.getF().getInputStream())));

            // 수신자 개별 전송
            for (String s : mailDto.getAddress()) {
                s.strip();
                helper.setTo(s);
                emailSender.send(message);
            }

            //수신자 한번에 전송
            helper.setTo(mailDto.getAddress());
            emailSender.send(message);

            log.info("Mail with attachment sent successfully.");
        }
    }
}