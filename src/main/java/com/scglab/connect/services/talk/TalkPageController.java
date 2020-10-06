package com.scglab.connect.services.talk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/page")
public class TalkPageController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/main")
    public String rooms() {
        return "/talk/room";
    }
}
