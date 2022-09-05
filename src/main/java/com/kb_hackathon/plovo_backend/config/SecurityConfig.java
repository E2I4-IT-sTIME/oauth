package com.kb_hackathon.plovo_backend.config;


import com.kb_hackathon.plovo_backend.config.jwt.CustomAuthenticationEntryPoint;
import com.kb_hackathon.plovo_backend.config.jwt.JwtRequestFilter;
import com.kb_hackathon.plovo_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    public static final String FRONT_URL = "http://localhost:3000";

    private final CorsFilter corsFilter;

    // @Bean -> 해당 메소드의 리턴되는 오브젝트를 IoC로 등록해줌
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()  // session 을 사용하지 않음
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .addFilter(corsFilter); // @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)

        http.authorizeRequests()
                .antMatchers(FRONT_URL+"/main/**")
                .authenticated()
                .anyRequest().permitAll()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.addFilterBefore(new JwtRequestFilter(
                userRepository
        ), UsernamePasswordAuthenticationFilter.class);
    }

}
