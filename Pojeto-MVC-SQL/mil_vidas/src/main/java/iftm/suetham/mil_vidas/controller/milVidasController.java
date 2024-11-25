package iftm.suetham.mil_vidas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class milVidasController {

    @GetMapping("/wishList")
    public String wishList(Model model) {
        model.addAttribute("nome", "É assim que acaba");
        return "wishList";
    }
}
