package com.jk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("tolist")
    public String tolist(){

        return "list";
    }
}
