package com.example.demo.commute;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommuteService {
    @Autowired
    private CommuteDao cdao;

    public CommuteDto get(int commuteNum) {
        Commute c = cdao.findById(commuteNum).orElse(null);
        return new CommuteDto(c.getNum(), c.getMember(), c.getBasicDate(), c.getStartTime(), c.getEndTime(), c.getReason(), c.getEditStartTime(), c.getEditEndTime(), c.getEditBasicDate());
    }

    public CommuteDto getByDateAndUserName(String BasicDate, String Username) {
        Commute c = cdao.findByBasicDateAndMemberUsername(BasicDate, Username);
        if (c != null)
            return new CommuteDto(c.getNum(), c.getMember(), c.getBasicDate(), c.getStartTime(), c.getEndTime(), c.getReason(), c.getEditStartTime(), c.getEditEndTime(), c.getEditBasicDate());
        else return null;

    }
    
    
    // 근태관리
    public ArrayList<CommuteDto> getAll() {
        ArrayList<Commute> list = (ArrayList<Commute>) cdao.findAll(Sort.by(Sort.Direction.DESC, "num"));
        ArrayList<CommuteDto> list2 = new ArrayList<>();
        for (Commute c : list) {
            list2.add(new CommuteDto(c.getNum(), c.getMember(), c.getBasicDate(), c.getStartTime(), c.getEndTime(), c.getReason(), c.getEditStartTime(), c.getEditEndTime(), c.getEditBasicDate()));
        }
        return list2;
    }
    
    // 내 출퇴근이력
    public ArrayList<CommuteDto> getMyList(Long id) {
        ArrayList<Commute> list = cdao.findByMemberIdOrderByNumDesc(id);
        ArrayList<CommuteDto> list2 = new ArrayList<>();
        for (Commute c : list) {
            list2.add(new CommuteDto(c.getNum(), c.getMember(), c.getBasicDate(), c.getStartTime(), c.getEndTime(), c.getReason(), c.getEditStartTime(), c.getEditEndTime(), c.getEditBasicDate()));
        }
        return list2;
    }
    
    // 근태수정요청목록
    public ArrayList<CommuteDto> getEditRequestList(String editStartTime, String editEndTime) {

        List<Commute> list = cdao.findAll();
        List<Commute> filteredList = list.stream()
                .filter(c -> c.getEditStartTime() != null || c.getEditEndTime() != null)
                .collect(Collectors.toList());
        
        ArrayList<CommuteDto> list2 = new ArrayList<>();
        for (Commute c : filteredList) {
            list2.add(new CommuteDto(c.getNum(), c.getMember(), c.getBasicDate(), c.getStartTime(), c.getEndTime(), c.getReason(), c.getEditStartTime(), c.getEditEndTime(), c.getEditBasicDate()));
        }

        return list2;
    }


    public CommuteDto save(CommuteDto dto) {
        Commute c = cdao.save(new Commute(dto.getNum(), dto.getMember(), dto.getBasicDate(), dto.getStartTime(), dto.getEndTime(), dto.getReason(), dto.getEditStartTime(), dto.getEditEndTime(), dto.getEditBasicDate()));
        return new CommuteDto(c.getNum(), c.getMember(), c.getBasicDate(), c.getStartTime(), c.getEndTime(), c.getReason(), c.getEditStartTime(), c.getEditEndTime(), c.getEditBasicDate());
    }

    public Page<CommuteDto> getByOption(String type, String option, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "commuteNum")); // size는 한 페이지에 표시할 데이터 수를 지정합니다.
        Page<Commute> list;
        if (Objects.equals("basicDate", type)) {
            list = cdao.findByBasicDateLike(option, pageable);
        } else if (Objects.equals("name", type)) {
            list = cdao.findByMemberNameContaining(option, pageable);
        } else if (Objects.equals("deptName", type)) {
            list = cdao.findByMemberDeptNameContaining(option, pageable);
        } else {
            list = cdao.findAll(pageable);
        }
        return list.map(CommuteDto::of);
    }

    public Page<CommuteDto> getByOptionAndMember(String type, String option, Long memberId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Commute> list = null;
        if (Objects.equals("basicDate", type)) {
            list = cdao.findByBasicDateLikeAndMemberId(option, memberId, pageable);
        }
        return list.map(CommuteDto::of);
    }


    public Page<Commute> getList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.cdao.findAll(pageable);
    }


    /*public Page<Commute> getMyList(Long id, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.cdao.findByMemberId(id, pageable);
    }*/
    

    /*public Page<Commute> getEditRequestList(int page, String editStartTime, String editEndTime) {
        Pageable pageable = PageRequest.of(page, 10); // size는 한 페이지에 표시할 데이터 수를 지정합니다.
        List<Commute> list = cdao.findAll();
        List<Commute> filteredList = list.stream()
                .filter(c -> c.getEditStartTime() != null || c.getEditEndTime() != null)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredList.size());

        Page<Commute> pages = new PageImpl<>(filteredList.subList(start, end), pageable, filteredList.size());

        return pages;
    }*/

}
