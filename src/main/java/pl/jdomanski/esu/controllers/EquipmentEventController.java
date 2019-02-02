package pl.jdomanski.esu.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.jdomanski.esu.model.EquipmentEvent;
import pl.jdomanski.esu.model.EquipmentEventDTO;
import pl.jdomanski.esu.repositories.EquipmentEventRepository;
import pl.jdomanski.esu.utils.Mappings;

import javax.validation.Valid;

@Controller
@Slf4j
public class EquipmentEventController {

    private final EquipmentEventRepository eventRepository;

    @Autowired
    EquipmentEventController(EquipmentEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping(Mappings.EVENT_EDIT)
    public ModelAndView getEventEdit(@RequestParam(name = "id") Long id) {
        EquipmentEvent event = eventRepository.findById(id).get();
        EquipmentEventDTO dto = new EquipmentEventDTO(event);

        ModelAndView modelAndView = new ModelAndView("event.edit", "dto", dto);

        log.info("Editing event: id {}, type {} for equipment id {}",
                event.getId(), event.getEquipmentState(), event.getEquipment().getId());
        log.info("DTO: {}", dto);
        return modelAndView;
    }

    @PostMapping(Mappings.EVENT_EDIT)
    public String postEventEdit(@ModelAttribute("dto") @Valid EquipmentEventDTO dto,
                                BindingResult result) {


        if (result.hasErrors()) {
            log.info("errors in event.edit form");
            return "event.edit";
        }

        EquipmentEvent event = eventRepository.findById(dto.getId()).get();

        log.info("DTO from form {}", dto);
        event.setDate(dto.getDate());
        event.setDocument(dto.getDocument());
        event.setNote(dto.getNote());

        eventRepository.save(event);
        log.info("Saving event {}", event);

        return Mappings.REDIRECT_EQUIPMENT_VIEW_ID + event.getEquipment().getId();
    }
}
