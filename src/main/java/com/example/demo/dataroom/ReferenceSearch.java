package com.example.demo.dataroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ReferenceSearch {
    String select; //구분용
    String search; //사용자가 검색한 value
}