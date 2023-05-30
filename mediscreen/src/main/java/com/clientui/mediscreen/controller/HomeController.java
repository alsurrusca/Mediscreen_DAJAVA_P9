package com.clientui.mediscreen.controller;


import com.clientui.mediscreen.domain.PatientBeans;
import com.clientui.mediscreen.repository.MsPatientProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
public class HomeController {
    private final MsPatientProxy patientProxy;

    public HomeController(MsPatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }


    @GetMapping("/")
    public String home(Model model){
        return "home";
    }




}
