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
                        @RequestParam(required = false) String ccAddress,
                        @RequestParam String title,
                        @RequestParam String content,
                        @RequestParam(required = false) MultipartFile file) throws MessagingException, IOException, TemplateException {
        MailDto dto = new MailDto();
        dto.setFrom(username);

        // 클라이언트에서 콤마로 구분된 주소를 배열로 변환
        String[] addresses = address.split(",");
        dto.setAddress(addresses);

        // ccAddress가 제공되었는지 확인
        if (ccAddress != null && !ccAddress.isEmpty()) {
            String[] ccAddresses = ccAddress.split(",");
            dto.setCcAddress(ccAddresses);
        }

        dto.setTitle(title);
        dto.setContent(content);
        dto.setF(file);

        System.out.println("from = " + username);
        System.out.println("address = " + address);
        System.out.println("ccAddress = " + ccAddress);
        System.out.println("title = " + title);
        System.out.println("content = " + content);

        // 파일이 있는 경우에는 로그에 출력
        if (file != null) {
            System.out.println("file.getOriginalFilename() = " + file.getOriginalFilename());
        } else {
            System.out.println("No file attached.");
        }

        Map map = new HashMap();
        mservice.sendMultipleMessage(dto);
        map.put("dto", dto);
        System.out.println("메일 전송 완료");
        return map;
    }
}
