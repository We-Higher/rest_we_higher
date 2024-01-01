package com.example.demo.member;

import com.example.demo.auth.JwtTokenProvider;
import com.example.demo.member.Member;
import com.example.demo.member.dto.MemberJoinDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController // rest api controller
@CrossOrigin(origins = "*") // 모든 ip로부터 요청 받기 허용
public class MemberController {
    private final PasswordEncoder passwordEncoder;
    private final MemberService service;
    private final JwtTokenProvider tokenprovider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${spring.servlet.multipart.location}")
    private String path;

    @PostMapping("/login")
    public Map login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Map map = new HashMap();
        boolean flag = authentication.isAuthenticated();

        if (flag) {
            MemberDto dto = service.getMember(username);
            String token = tokenprovider.generateJwtToken(new MemberDto().builder().username(username).build());

            dto.setPwd("[PROTECTED]");

            map.put("token", token);
            map.put("dto", dto);
        }
        map.put("flag", flag);

        return map;
    }

    @GetMapping("/auth/mypage")
    public Map mypage() {
        Map map = new HashMap();
        boolean flag = true;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();// 로그인 아이디 추출
        MemberDto mdto = service.getMember(id);
        System.out.println(mdto);
        if (mdto == null) {
            flag = false;
        } else {
            map.put("mdto", mdto);
        }
        map.put("flag", flag);
        return map;
    }

    @PostMapping("/join")
    public Map join(@RequestBody MemberJoinDto memberJoinDto) {
        MemberDto res = service.create(memberJoinDto);
        Map map = new HashMap();
        map.put("name", res.getName());
        return map;
    }

    @GetMapping("/auth/info")
    public Map myinfo() {
        Map map = new HashMap();
        boolean flag = true;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();// 로그인 아이디 추출
        MemberDto m = service.getMember(id);
        System.out.println(m);
        if (m == null) {
            flag = false;
        } else {
            map.put("m", m);
        }
        map.put("flag", flag);
        return map;
    }

    @GetMapping("/edit/{username}")
    public Map editForm(String username) {
        Map map = new HashMap();
        MemberDto dto = service.getMember(username);
        map.put("m", dto);
        return map;
    }

    @PutMapping("/edit")
    public Map edit(@RequestBody MemberDto dto) {
        Map map = new HashMap();
        System.out.println("dto.getDeptCode() = " + dto.getDeptCode());
        System.out.println("dto.getDeptName() = " + dto.getDeptName());
        System.out.println("dto.getPwd() = " + dto.getPwd());

        MemberDto m = service.getMemberByName(dto.getName());
        m.setUsername(dto.getUsername());
        m.setPwd(dto.getPwd());
        m.setName(dto.getName());
        m.setCompanyName(dto.getCompanyName());
        m.setDeptCode(dto.getDeptCode());
        m.setCompanyRank(dto.getCompanyRank());
        m.setNewNo(dto.getNewNo());
        m.setEmail(dto.getEmail());
        m.setAddress(dto.getAddress());
        m.setComCall(dto.getComCall());
        m.setPhone(dto.getPhone());
        m.setIsMaster(dto.getIsMaster());
        m.setStatus(dto.getStatus());
        service.save(m);
        ArrayList<MemberDto> list = service.getAll();
        map.put('m', m);
        map.put("list", list);
        return map;
    }

    @Transactional
    @PostMapping("/del")
    public Map delete(String username) {
        System.out.println("username = " + username);
        service.delete(username);
        Map map = new HashMap();
        ArrayList<MemberDto> list = service.getAll();
        map.put("list", list);
        return map;
    }

    @PutMapping("/auth/pwdedit")
    public Map pwdedit(MemberDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        MemberDto m = service.getMember(id);
        if (dto.getF() != null) {
            MultipartFile f = dto.getF();
            String fname = f.getOriginalFilename();
            File newFile = new File(path + fname);
            try {
                f.transferTo(newFile);
                m.setOriginFname(fname);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("test" + dto.getF());

        m.setUsername(dto.getUsername());
        m.setPwd(passwordEncoder.encode(dto.getPwd()));
        m.setName(dto.getName());
        m.setCompanyName(dto.getCompanyName());
        m.setDeptCode(dto.getDeptCode());
        m.setCompanyRank(dto.getCompanyRank());
        m.setNewNo(dto.getNewNo());
        m.setEmail(dto.getEmail());
        m.setAddress(dto.getAddress());
        m.setComCall(dto.getComCall());
        m.setPhone(dto.getPhone());
        m.setIsMaster(dto.getIsMaster());
        m.setStatus(dto.getStatus());
        service.save(m);
        Map map = new HashMap();
        map.put("mdto", m);
        return map;
    }

    /*@GetMapping("/image/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {

        Path imagePath = Paths.get(path, imageName);
        byte[] imageBytes = Files.readAllBytes(imagePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }*/
   
    @GetMapping("/image/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
        Path imagePath = Paths.get(path, imageName);

        int width = 300;
        int height = 320; 
        byte[] originalImageBytes = Files.readAllBytes(imagePath);
        ByteArrayInputStream bis = new ByteArrayInputStream(originalImageBytes);
        BufferedImage originalImage = ImageIO.read(bis);
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        resizedImage.getGraphics().drawImage(originalImage, 0, 0, width, height, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        byte[] resizedImageBytes = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(resizedImageBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/monthMemberList")
    public Map emplist() {
        ArrayList<MemberDto> list = service.getAll();
        ArrayList<MemberDto> list2 = new ArrayList<>();
        for (MemberDto dto : list) {
            if (dto.getMonthMember() == 1) {
                list2.add(dto);
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        MemberDto mdto = service.getMember(id);
        Map map = new HashMap();
        map.put("mdto", mdto);
        map.put("list", list2);
        return map;
    }

    @PutMapping("/monthMember")
    public Map monthMember(@RequestBody Map<String, List<Long>> requestBody) {
        List<Long> selectedMemberIds = requestBody.get("selectedMembers");
        Map map = new HashMap();
        ArrayList<MemberDto> list = service.getAll();
        map.put("list", list);
        for (MemberDto dto : list) {
            if (selectedMemberIds != null && selectedMemberIds.contains(dto.getId())) {
                dto.setMonthMember(1);
            } else {
                dto.setMonthMember(0);
            }
            service.save(dto);
        }
        return map;
    }

    @GetMapping("/member")
    public Map getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        MemberDto m = service.getMember(id);

        Map map = new HashMap();
        map.put("member", m);

        return map;
    }

    @GetMapping("/test")
    public Map getTest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginMember = (Member) authentication.getPrincipal();

        loginMember.setPwd("[PROTECTED]");

        Map map = new HashMap();
        map.put("member", loginMember);

        return map;
    }
}
