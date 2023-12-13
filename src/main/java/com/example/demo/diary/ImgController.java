package com.example.demo.diary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImgController {
	@Value("${spring.servlet.multipart.location}")
	private String path;

	@GetMapping("/read-img/{fname}")
	public ResponseEntity<byte[]> read_img(@PathVariable("fname") String fname) {// fname: 파일명
		File f = new File(path + fname);// 파일 객체 생성
		System.out.println("f path:"+f.getAbsolutePath());
		// 응답의 헤더. 응답 페이지의 크기, 마임타입, 첨부파일.. 정보를 갖는다
		HttpHeaders header = new HttpHeaders();

		// ResponseEntity: 응답 데이터
		ResponseEntity<byte[]> result = null;
		try {
			header.add("Content-Type", Files.probeContentType(f.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(f), header, HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
