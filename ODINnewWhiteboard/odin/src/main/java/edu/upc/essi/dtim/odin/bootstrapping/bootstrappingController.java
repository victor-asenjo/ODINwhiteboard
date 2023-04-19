package edu.upc.essi.dtim.odin.bootstrapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("test")
public class bootstrappingController {
    @GetMapping
    public String getTestData() {
        String mv = "Hello world on navigator";
        return mv;
    }

}