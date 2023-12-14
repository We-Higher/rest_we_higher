package com.example.demo.dataroom;

import com.example.demo.auth.SecurityMember;
import com.example.demo.board.BoardDto;
import com.example.demo.board.BoardService;
import com.example.demo.diary.DiaryDto;
import com.example.demo.employee.EmployeeService;
import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/dataroom")
public class DataroomController {

	@Autowired
	private DataroomService dservice;

	@Autowired
	private MemberService mservice;

    @Value("${spring.servlet.multipart.location}")
    private String path;
    
    // 데이터룸 목록
	@GetMapping("")
	public Map emplist() {
		ArrayList<DataroomDto> list = dservice.getAll();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		Map map = new HashMap();
		map.put("mdto", mdto);
		map.put("list", list);
		return map;
	}
	
	
	//데이터룸 등록
	@PostMapping("")
	public Map dataAdd(DataroomDto dto) {

        boolean flag = true;
        DataroomDto res = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SecurityMember member = (SecurityMember) authentication.getPrincipal();
        MemberDto mdto = mservice.getMember(member.getUsername());
        MultipartFile f = dto.getF();
        String fname = f.getOriginalFilename();
        dto.setMember(new Member(mdto.getId(), mdto.getUsername(), mdto.getPwd(), mdto.getName(), mdto.getEmail(), mdto.getPhone(), mdto.getAddress(), mdto.getCompanyName(), mdto.getDeptCode(), mdto.getDeptName(), mdto.getCompanyRank(), mdto.getCompanyRankName(), mdto.getNewNo(), mdto.getComCall(), mdto.getIsMaster(), mdto.getStatus(), mdto.getCstatus(), mdto.getOriginFname(), mdto.getThumbnailFname(), mdto.getNewMemNo(), mdto.getRemain(),mdto.getMonthMember()));
        File newFile = new File(path + fname);
        try {
            f.transferTo(newFile);
            dto.setFname(fname);
            res = dservice.save(dto);
            
    	} catch (Exception e) {

    		flag = false;
    		e.printStackTrace();
    	}
        Map map = new HashMap();
        map.put("flag", flag);
		map.put("dto", res);
		return map;
	}
	
	// 수정폼
    @GetMapping("/edit/{num}")
    public Map editForm(@PathVariable("num") int num) {
    	
		Map map = new HashMap();
		DataroomDto d = dservice.getDataroom(num);
        map.put("dto", d);
		return map;
    }
    
	// 데이터룸 상세페이지
	@GetMapping("/{num}")
	public Map getdetail(@PathVariable("num") int num) {
		Map map = new HashMap();
		DataroomDto d = dservice.getDataroom(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto m = mservice.getMember(id);
		map.put("m,", m);
		map.put("dto", d);
		return map;
	}
	 
	// 데이터룸 수정
	@PutMapping("/edit")
    public Map dataEdit(MultipartFile file, DataroomDto dto) {
    	
        boolean flag = true;
        MultipartFile f = dto.getF();
        DataroomDto d2 = dservice.getDataroom(dto.getNum());
        
        if (f != null && f.getSize() > 0) {
            String fname = f.getOriginalFilename();
            File newFile = new File(path + fname);
            try {
                f.transferTo(newFile);
                dto.setFname(fname);
                File delFile = new File(path + d2.getFname());
                delFile.delete();
        	} catch (Exception e) {
        		flag = false;
        		e.printStackTrace();
        	}
        } else {
            dto.setFname(d2.getFname());
        }
        dto.setWdate(d2.getWdate());
        
        DataroomDto d = dservice.save(dto);
        
        Map map = new HashMap();
        map.put("flag", flag);
    	map.put("dto", d);
        
		return map;
    }
	
	// 데이터룸 삭제
	@PostMapping("/del")
	public Map del(int num) {
		
        DataroomDto origin = dservice.getDataroom(num);
        if (origin != null) {
            File delFile = new File(path + origin.getFname());
            delFile.delete();
            dservice.delDataroom(num);
        }
		Map map = new HashMap();
		ArrayList<DataroomDto> list = dservice.getAll();
		map.put("num", num);
		map.put("list", list);
		return map;
	}
	
	
	@PostMapping("/down")
	public ResponseEntity<byte[]> datadown(String fname, int num) {// fname: 파일명
		File f = new File(path + fname);// 파일 객체 생성
		System.out.println("f path:"+f.getAbsolutePath());
		HttpHeaders header = new HttpHeaders();
		ResponseEntity<byte[]> result = null;
		try {
			header.add("Content-Type", Files.probeContentType(f.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(f), header, HttpStatus.OK);
			dservice.editCnt(num);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	
    /*@PostMapping("/add")
    public String add(Principal principal, ) {
        MultipartFile f = dto.getF();
        String fname = f.getOriginalFilename();
        MemberDto mdto = memberService.getMember(principal.getName());
        dto.setMember(new Member(mdto.getId(), mdto.getUsername(), mdto.getPwd(), mdto.getName(), mdto.getEmail(), mdto.getPhone(), mdto.getAddress(), mdto.getCompanyName(), mdto.getDeptCode(), mdto.getDeptName(), mdto.getCompanyRank(), mdto.getCompanyRankName(), mdto.getNewNo(), mdto.getComCall(), mdto.getIsMaster(), mdto.getStatus(), mdto.getCstatus(), mdto.getOriginFname(), mdto.getThumbnailFname(), mdto.getNewMemNo(), mdto.getRemain(),mdto.getMonthMember()));
        File newFile = new File(path + fname);
        try {
            f.transferTo(newFile);
            dto.setFname(fname);
            //dto.setMember(Member.builder().id(mdto.getId()).build());
            service.save(dto);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "redirect:/dataroom/list";
    }*/

    /*@RequestMapping("/list")
    public void list(Model m, @RequestParam(value = "page", defaultValue = "1") int page, Principal principal) {
        Page<Dataroom> paging = this.service.getList(page - 1);
        m.addAttribute("paging", paging);
        ArrayList<DataroomDto> list = service.getAll();
        m.addAttribute("list", list);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDateTime.now().format(formatter1);
        MemberDto mdto = memberService.getMember(principal.getName());
        m.addAttribute("date", date);
        m.addAttribute("name", mdto.getName());
    }

    @GetMapping("/search")
    public String searchReferenceList(ReferenceSearch referenceSearch, @RequestParam(value = "page", defaultValue = "1") int page, Model model, Principal principal) {
        Page<DataroomDto> listReference = service.getSearchReference(referenceSearch, page-1);
        model.addAttribute("paging", listReference);
        model.addAttribute("kw", referenceSearch);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDateTime.now().format(formatter1);
        MemberDto mdto = memberService.getMember(principal.getName());
        model.addAttribute("date", date);
        model.addAttribute("name", mdto.getName());
        return "dataroom/list";
    }

    @GetMapping("/detail")
    public void detailForm(int num, Model m) {
        DataroomDto dto = service.getDataroom(num);
        m.addAttribute("dto", dto);
    }

    @GetMapping("/edit")
    public void editForm(int num, Model m) {
        DataroomDto dto = service.getDataroom(num);
        m.addAttribute("dto", dto);
    }

    @GetMapping("/del")
    public String del(int num) {
        DataroomDto origin = service.getDataroom(num);
        if (origin != null) {
            File delFile = new File(path + origin.getFname());
            delFile.delete();
            service.delDataroom(num);
        }
        return "redirect:/dataroom/list";
    }


*/

}