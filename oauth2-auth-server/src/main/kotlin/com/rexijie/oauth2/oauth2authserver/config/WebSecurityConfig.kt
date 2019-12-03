package com.rexijie.oauth2.oauth2authserver.config

import com.rexijie.oauth2.oauth2authserver.repository.AccessTokenRepository
import com.rexijie.oauth2.oauth2authserver.repository.RefreshTokenRepository
import com.rexijie.oauth2.oauth2authserver.service.CustomUserDetailsService
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@EnableWebSecurity
class WebSecurityConfig(
        val userDetailsService: CustomUserDetailsService,
        val accessTokenRepository: AccessTokenRepository,
        val refreshTokenRepository: RefreshTokenRepository
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/auth/token", "/profile/me").permitAll()
                .antMatchers("/auth/authorize").authenticated()
                .antMatchers("/api/**").authenticated()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
    }

    @Bean("authenticationManagerBean")
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun tokenStore(): TokenStore {
        return CustomTokenStore(
                accessTokenRepository = accessTokenRepository,
                refreshTokenRepository = refreshTokenRepository
        )
    }


    /**
     * Cors Configuration
     * This is currently set to allow all methods from all origins
     */
    @Bean
    fun corsFilter(): FilterRegistrationBean<CorsFilter> {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfig = CorsConfiguration()
        corsConfig.addAllowedMethod("*")
        corsConfig.addAllowedHeader("*")
        corsConfig.addAllowedOrigin("*")
        source.registerCorsConfiguration("/**", corsConfig)
        val bean = FilterRegistrationBean(CorsFilter(source))
        bean.order = 0
        return bean
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

}