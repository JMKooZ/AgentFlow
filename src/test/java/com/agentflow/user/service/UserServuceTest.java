package com.agentflow.user.service;

import com.agentflow.user.entity.User;
import com.agentflow.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 사용자_생성() {
        // given
        String email = "test@agentflow.com";
        String password = "password123";
        String name = "AgentFlow";

        // when
        Long userId = userService.createUser(
                email,
                password,
                name
        ).getId();

        // then
        User user = userRepository.findById(userId)
                .orElseThrow();

        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getId()).isEqualTo(userId);
    }

    @Test
    void 중복_이메일로_사용자를_생성할_수_없다() {
        // given
        String email = "duplicate@agentflow.com";

        userService.createUser(
                email,
                "password123",
                "첫 번째 사용자"
        );

        // when & then
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                        userService.createUser(
                                email,
                                "password456",
                                "두 번째 사용자"
                        )
                )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용 중인 이메일입니다.");
    }

    @Test
    void 비밀번호는_암호화되어_저장된다() {
        // given
        String password = "password123";

        //when
        Long userId = userService.createUser(
                "passwordTest@agentflow.com",
                password,
                "암호화 테스트"
        ).getId();

        // then
        User user = userRepository.findById(userId)
                .orElseThrow();

        assertThat(user.getPassword()).isNotEqualTo(password);

        assertThat(passwordEncoder.matches(password, user.getPassword())).isTrue();
    }
}