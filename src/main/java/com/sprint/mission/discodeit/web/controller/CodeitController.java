package com.sprint.mission.discodeit.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class CodeitController {
    @GetMapping
    public String index(){
        return "user-list.html";
    }
}
