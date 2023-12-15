package com.example.demo.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MailDto {
    private String from;
    private String[] address;
    private String[] ccAddress;
    private String title;
    private String content;
    private MultipartFile f;
}
