package com.clientui.mediscreen.controller;


import com.clientui.mediscreen.domain.NotesBean;
import com.clientui.mediscreen.domain.PatientAssessmentBean;
import com.clientui.mediscreen.domain.PatientBeans;
import com.clientui.mediscreen.repository.MsNotesProxy;
import com.clientui.mediscreen.repository.MsPatientAssessmentProxy;
import com.clientui.mediscreen.repository.MsPatientProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Controller
public class NotesController {

    private final MsNotesProxy notesProxy;

    @Autowired
    MsPatientProxy patientProxy;

    @Autowired
    MsPatientAssessmentProxy patientAssessmentProxy;

    Logger log = LoggerFactory.getLogger(NotesController.class);


    public NotesController(@Autowired MsNotesProxy notesProxy, @Autowired MsPatientProxy patientProxy) {
        this.notesProxy = notesProxy;
        this.patientProxy = patientProxy;
    }


    @GetMapping("/notes/list")
    public List<NotesBean> view(Model model) {
        List<NotesBean> notes = notesProxy.getAllNotes();
        List<PatientBeans> patients = patientProxy.listPatients();

        // Récupérer les informations des patients correspondant à chaque note
        for (NotesBean note : notes) {
            int patId = note.getPatId();

            for(PatientBeans patient : patients){
                if(patient.getId() == patId){
                    note.setPatientFullName(patient.getFirstName() + " " + patient.getLastName());
                    break;
                }
            }
        }

        model.addAttribute("notes", notes);
        return notes;
    }


    @GetMapping("/notes/addNote")
    public String addNotes(Model model) {
        model.addAttribute("notes", new NotesBean());
        model.addAttribute("patients", patientProxy.listPatients());
        return "notes/add";
    }

    @PostMapping("/notes/addNote")
    public String validateNotes(Model model, NotesBean notesBean) {
        notesProxy.validateNotes(notesBean);
        model.addAttribute("notes");
        return "redirect:/notes/list";
    }

    @GetMapping("/notes/update/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        notesProxy.showUpdateForm(id);
        notesProxy.getAllNotes();
        log.info("Find notes by id SUCCESS");
        NotesBean notesBean = notesProxy.getNoteById(id);
        model.addAttribute("notes", notesBean);
        return "notes/update";
    }

    @PostMapping("/notes/update/{id}")
    public String updateNotes(@PathVariable("id") String id, @Valid NotesBean notesBean, BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.error("Update Notes FAILED");
            return "notes/update";
        }

        notesBean.setId(id);
        notesProxy.validateNotes(notesBean);
        log.info("Update Notes SUCCESS");
        model.addAttribute("notes", notesProxy.getAllNotes());
        return "redirect:/notes/list";
    }

    @GetMapping("/notes/delete/{id}")
    public String deleteNotes(@PathVariable("id") String id, Model model) {
        notesProxy.deleteNotes(id);
        model.addAttribute("notes", notesProxy.getAllNotes());
        log.info("SUCCESS DELETE");
        return "redirect:/notes/list";
    }


}


