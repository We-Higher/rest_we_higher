package com.example.demo.diary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/diary")
public class DiaryController {
	@Autowired
	private DiaryService service;

	@Value("${spring.servlet.multipart.location}")
	private String path;

	@PostMapping("")
	public Map add(DiaryDto dto) {
		Map map = new HashMap();
		boolean flag = true;
		DiaryDto res = null;
		MultipartFile f = dto.getF();
		String fname = f.getOriginalFilename();
		File newFile = new File(path + fname);
		try {
			f.transferTo(newFile);
			dto.setFname(fname);
			res = service.save(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			flag = false;
			e.printStackTrace();
		}
		map.put("flag", flag);
		map.put("dto", res);
		return map;
	}

	@GetMapping("")
	public Map list() {
		Map map = new HashMap();
		ArrayList<DiaryDto> list = service.getAll();
		map.put("list", list);
		return map;
	}

	@GetMapping("/{num}")
	public Map get(@PathVariable("num") int num) {
		Map map = new HashMap();
		DiaryDto dto = service.get(num);
		map.put("dto", dto);
		return map;
	}

	@PutMapping("")
	public Map edit(DiaryDto dto) {
		boolean flag = true;
		try {
			DiaryDto origin = service.get(dto.getNum());
			origin.setTitle(dto.getTitle());
			origin.setContent(dto.getContent());
			dto = service.save(origin);
		} catch (Exception e) {
			System.out.println(e);
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("dto", dto);
		return map;
	}

	@DeleteMapping("/{num}")
	public Map del(@PathVariable("num") int num) {
		boolean flag = true;
		try {
			service.del(num);
		} catch (Exception e) {
			System.out.println(e);
			flag = false;
		}
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("num", num);
		return map;
	}
}
