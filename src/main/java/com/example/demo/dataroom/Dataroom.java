package com.example.demo.dataroom;

import com.example.demo.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Dataroom {
    @Id
    @SequenceGenerator(name="seq_gen", sequenceName="seq_dataroom", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_gen")
    private int num;

    @ManyToOne
    @JoinColumn(nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;
    private String wdate;
    private String title;
    private String content;
    private String fname;
    @Column(columnDefinition = "int default 0", nullable = false)
    private int cnt;
}