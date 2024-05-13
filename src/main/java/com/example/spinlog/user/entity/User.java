package com.example.spinlog.user.entity;

import com.example.spinlog.article.entity.Article;
import com.example.spinlog.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING) //TODO DB 에서 디폴트값 NONE 으로 설정
    private Mbti mbti;

    @Enumerated(EnumType.STRING) //TODO DB 에서 디폴트값 NONE 으로 설정
    private Gender gender;

    @Min(0) @Max(100_000_000) //TODO validation 코드를 entity 에 넣어도 되는지
    private Integer budget;

    @Column(nullable = false, unique = true)
    private String authenticationName; //oauth2 provider + "_" + oauth2 provider id

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Article> articles = new ArrayList<>();

    @Builder //Builder 에서 id 를 제외하기 위해, 클래스 레벨이 아닌 생성자 레벨에 @Builder 사용
    public User(String email, Mbti mbti, Gender gender, Integer budget, String authenticationName) {
        this.email = email;
        this.mbti = mbti;
        this.gender = gender;
        this.budget = budget;
        this.authenticationName = authenticationName;
    }

    public void changeProfile(String email) {
        this.email = email;
    }
}