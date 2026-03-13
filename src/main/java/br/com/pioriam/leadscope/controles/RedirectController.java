package br.com.pioriam.leadscope.controles;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/")
    public String redirectSwagger() {
        return "redirect:/leads-doc";
    }
}