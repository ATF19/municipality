package com.municipality.backend.infrastructure.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
    @Value("\${municipality.cors.allowed-domain}")
    private val allowedDomain: String,
    private val sessionFilter: SessionFilter
) : WebSecurityConfigurerAdapter() {

    override fun configure(httpSecurity: HttpSecurity) {
        // We use app level authentication/authorization
        httpSecurity.cors().and().csrf().disable().authorizeRequests().antMatchers("/**").permitAll()
        httpSecurity.addFilterBefore(sessionFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf(allowedDomain)
        config.allowedMethods = listOf(
            HttpMethod.GET.name,
            HttpMethod.POST.name,
            HttpMethod.PUT.name,
            HttpMethod.DELETE.name,
            HttpMethod.OPTIONS.name
        )
        config.allowCredentials = true
        config.addAllowedHeader(CorsConfiguration.ALL)
        config.addExposedHeader(HttpHeaders.CONTENT_DISPOSITION)
        val urlConfig = UrlBasedCorsConfigurationSource()
        urlConfig.registerCorsConfiguration("/**", config)
        return urlConfig
    }
}