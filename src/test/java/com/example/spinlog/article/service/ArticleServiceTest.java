package com.example.spinlog.article.service;

import com.example.spinlog.article.dto.*;
import com.example.spinlog.article.entity.Article;
import com.example.spinlog.article.entity.Emotion;
import com.example.spinlog.article.entity.RegisterType;
import com.example.spinlog.article.repository.ArticleRepository;
import com.example.spinlog.user.entity.Mbti;
import com.example.spinlog.user.entity.User;
import com.example.spinlog.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .email("111@ccc.ccc")
                .authenticationName("test")
                .mbti(Mbti.ISTJ)
                .build();
        userRepository.save(user);
    }

    @Test
    void 게시글_작성_테스트() {
        // Given
        WriteArticleRequestDto requestDto = WriteArticleRequestDto.builder()
                .content("Test Thing")
                .spendDate("2024-04-04T11:22:33")
                .event("Test event")
                .thought("Test thought")
                .emotion("ANNOYED")
                .satisfaction(5F)
                .reason("Test Reason")
                .improvements("Test Improvements")
                .amount(100)
                .registerType("SPEND")
                .build();

        // When
        WriteArticleResponseDto responseDto = articleService.createArticle("test", requestDto);

        // Then
        assertThat(responseDto).isNotNull();
    }

    //    @Test
//    public void 게시글_리스트_조회_테스트() {
//        // Given
//        Pageable pageable = PageRequest.of(0, 10);
//        SearchCond searchCond = new SearchCond("절약", "짜증", "20240430", 5F, "Test word");
//
//        // When
//        Page<ViewArticleResponseDTO> resultPage = articleService.listArticles(pageable, searchCond);
//
//        // Then
//        assertThat(resultPage).isNotNull();
//        assertThat(resultPage.getContent()).isNotEmpty();
//    }
//
    @Test
    void 게시글_1개_조회_테스트() {
        // Given
        User user = User.builder()
                .email("111@ccc.ccc")
                .authenticationName("test2")
                .build();
        userRepository.save(user);

        Article article = Article.builder()
                .user(user)
                .build();
        articleRepository.save(article);

        // When
        ViewArticleResponseDto responseDto = articleService.getArticle("test2", article.getArticleId());

        // Then
        assertThat(responseDto).isNotNull();
    }

    @Test
    void 게시글_조회_실패_테스트() {
        // Given
        Long articleId = 999L; // 존재하지 않는 게시글 ID

        // Then
        assertThatThrownBy(() -> articleService.getArticle("test", articleId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 게시글_수정_테스트() {
        // Given
        User user = User.builder()
                .email("111@ccc.ccc")
                .authenticationName("test2")
                .build();
        userRepository.save(user);

        Article article = Article.builder()
                .user(user)
                .emotion(Emotion.ANNOYED)
                .registerType(RegisterType.SPEND)
                .build();
        articleRepository.save(article);

        UpdateArticleRequestDto updateDto = UpdateArticleRequestDto.builder()
                .spendDate("2024-05-05T12:34:56")
                .emotion(Emotion.SAD.toString())
                .registerType(RegisterType.SAVE.toString())
                .build();

        // When
        articleService.updateArticle("test2", article.getArticleId(), updateDto);

        // Then
        assertThat(article.getEmotion()).isEqualTo(Emotion.SAD);
    }

    @Test
    void 게시글_삭제_테스트() {
        // Given
        User user = User.builder()
                .email("111@ccc.ccc")
                .authenticationName("test2")
                .build();
        userRepository.save(user);

        Article article = Article.builder()
                .user(user)
                .emotion(Emotion.ANNOYED)
                .registerType(RegisterType.SPEND)
                .build();
        articleRepository.save(article);

        // When
        articleService.deleteArticle("test2", article.getArticleId());

        // Then
        assertThat(articleRepository.existsById(article.getArticleId())).isFalse();
    }
}
