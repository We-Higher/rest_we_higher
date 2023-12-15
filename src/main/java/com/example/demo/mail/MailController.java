package com.example.demo.mail;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mservice;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String pwd;

    @Value("${spring.servlet.multipart.location}")
    private String path;

    @GetMapping("/send")
    public Map sendForm() {
        Map map = new HashMap();
        map.put("mailsender", username);
        return map;
    }

    @PostMapping("/send")
    public Map sendMail(@RequestParam String from,
                        @RequestParam String address,
                        @RequestParam String ccAddress,
                        @RequestParam String title,
                        @RequestParam String content,
                        @RequestParam MultipartFile file) throws MessagingException, IOException, TemplateException {
        MailDto dto = new MailDto();
        dto.setFrom(username);
        dto.setAddress(new String[]{address});
        dto.setCcAddress(new String[]{ccAddress});
        dto.setTitle(title);
        dto.setContent(content);
        dto.setF(file);
        System.out.println("from = " + username);
        System.out.println("address = " + address);
        System.out.println("ccAddress = " + ccAddress);
        System.out.println("title = " + title);
        System.out.println("content = " + content);
        System.out.println("file.getOriginalFilename() = " + file.getOriginalFilename());
        Map map = new HashMap();
        mservice.sendMultipleMessage(dto);
        map.put("dto", dto);
        System.out.println("file = " + dto.getF().getOriginalFilename());
        System.out.println("메일 전송 완료");
        return map;
    }
}
