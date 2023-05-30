package com.clientui.mediscreen.controller;

import com.clientui.mediscreen.domain.PatientBeans;
import com.clientui.mediscreen.repository.MsPatientProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;


import javax.validation.Valid;
import java.util.List;

@Controller
public class PatientController {

    private final MsPatientProxy patientProxy;
    Logger log = LoggerFactory.getLogger(MsPatientProxy.class);


    public PatientController(MsPatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    @GetMapping("/patient/list")
    public List<PatientBeans> view(Model model) {
        List<PatientBeans> patients = patientProxy.listPatients();
        model.addAttribute("patients", patients);
        return patients;
    }

    @GetMapping("/patient/add")
    public String addPatient(Model model) {
        model.addAttribute("patient", new PatientBeans());
        return "patient/add";
    }


    @PostMapping("/patient/add")
    public String validatePatient(Model model,  PatientBeans patientBeans) {
        patientProxy.validatePatient(patientBeans);
        model.addAttribute("patient", patientProxy.listPatients());
        return "redirect:/patient/list";
    }


    @GetMapping("/patient/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
       patientProxy.showUpdateForm(id);
        patientProxy.listPatients();
        PatientBeans patientBeans = patientProxy.findPatientById(id);
        model.addAttribute("patient",patientBeans);
        return "patient/update";
    }

    @PostMapping("/patient/update/{id}")
    public String updateUser(@PathVariable("id") int id, @Valid PatientBeans user,
                             BindingResult result, Model model) {


        if (result.hasErrors()) {
            log.error("Update user FAILED");
            return "patient/update";
        }

        user.setId(id);
        patientProxy.validatePatient(user);
        log.info("Update User SUCCESS");

        model.addAttribute("patient", patientProxy.listPatients());

        return "redirect:/patient/list";

    }

    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable("id")int id, Model model){

        patientProxy.deletePatient(id);
        model.addAttribute("patients", patientProxy.listPatients());
        log.info("Delete Patient SUCCESS ");
        return "redirect:/patient/list";
    }


}
