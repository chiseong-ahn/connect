package com.scglab.connect.services.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.services.chat.JwtTokenProvider;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "관리", description = "관리자메뉴 > 계정관리")
public class AuthController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final JwtTokenProvider jwtTokenProvider;
	
	@GetMapping("/user")
    @ResponseBody
    public LoginInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        LoginInfo user = LoginInfo.builder().name(name).token(jwtTokenProvider.generateToken(name)).build();
        
        this.logger.debug("user : " + user.toString());
        
        return user;
    }
}