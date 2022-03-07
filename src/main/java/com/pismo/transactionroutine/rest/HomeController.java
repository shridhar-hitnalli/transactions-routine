package com.pismo.transactionroutine.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Shridhar
 */

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home() {
        return "redirect:swagger-ui.html";
    }
}
