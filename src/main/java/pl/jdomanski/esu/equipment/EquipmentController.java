package pl.jdomanski.esu.equipment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jdomanski.esu.EquipmentDTO;
import pl.jdomanski.esu.equipmentEvent.EquipmentEvent;
import pl.jdomanski.esu.equipmentEvent.EquipmentEventRepository;
import pl.jdomanski.esu.equipmentEvent.EquipmentState;
import pl.jdomanski.esu.user.User;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@Slf4j
public class EquipmentController {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentEventRepository equipmentEventRepository;

    public EquipmentController(EquipmentRepository equipmentRepository, EquipmentEventRepository equipmentEventRepository) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentEventRepository = equipmentEventRepository;
    }


    @ModelAttribute
    public void addStatesToModel(Model model) {
        model.addAttribute("states", EquipmentState.values());
    }

    @GetMapping("/")
    public String equipmentList(Model model) {

        Long lended = equipmentRepository.countByState(EquipmentState.LENDED);
        Long inStock = lended + equipmentRepository.countByState(EquipmentState.IN_STOCK);

        model.addAttribute("inStock", inStock);
        model.addAttribute("lended", lended);
        model.addAttribute("equipments", equipmentRepository.findAll());

        return "home";
    }

    @GetMapping("/equipment")
    public String getEquipmentDetails(Model model,
                                      @RequestParam(value = "id") Long id) {

        Equipment equipment = equipmentRepository.findById(id).get();

        log.info("getEquipmentDetails for id {}", id);
        model.addAttribute("equipment", equipment);

        //TODO dodać atrybut "uwagi" - zbudować string z eventu np. Z: Ruda Śl, dostarczył: Ł. Flasza, poprz. nr: blabla
        return "equipment.view";
    }

    @GetMapping("/equipment/edit")
    public String getEquipmentForm(@RequestParam(name = "id", required = false, defaultValue = "-1") Long id,
                                   Model model) {

        Optional optionalEquipment = equipmentRepository.findById(id);
        EquipmentDTO dto = new EquipmentDTO();

        if(optionalEquipment.isPresent()){
            Equipment equipment = (Equipment) optionalEquipment.get();
            dto.copyPropertiesFrom(equipment);
        }

        model.addAttribute("dto", dto);
        return "equipment.form";
    }

    @PostMapping("/equipment/edit")
    public String postEquipmentForm(@Valid @ModelAttribute("dto") EquipmentDTO dto,
                                    BindingResult result,
                                    @RequestParam(name = "id", required = false, defaultValue = "-1") Long id,
                                    Authentication authentication) {
        if (result.hasErrors()) {
            log.info("Errors in form");
            return "equipment.form";
        }

        Equipment equipment;
        Optional optionalEquipment = equipmentRepository.findById(id);

        if (optionalEquipment.isPresent()){
            equipment = (Equipment) optionalEquipment.get();
        } else {
            equipment = new Equipment();
        }

        equipment.setCreated(dto.getDate());
        equipment.setName(dto.getName());
        equipment.setInventoryNumber(dto.getInventoryNumber());
        equipment.setSerialNumber(dto.getSerialNumber());
        equipment.setAsset(dto.isAsset());
        equipment.setToDelete(dto.isToDelete());

        User user = (User) authentication.getPrincipal();
        equipment.setCreatedBy(user);

        equipmentRepository.save(equipment);

        if (optionalEquipment.isPresent()){
            log.info("Edited equipment - redirecting");
            return "redirect:/";
        }
        log.info("Saved new equipment {}", equipment);

        EquipmentEvent event = new EquipmentEvent();

        event.setEquipmentWithEquipmentState(equipment);
        event.setDate(equipment.getCreated());
        event.setNote(dto.getNote());
        event.setEnteredBy(user);

        equipmentEventRepository.save(event);
        log.info("Saved new event {}", event);
        log.info("Equipment after saving event {}", equipment);

        return "redirect:/";
    }

    @GetMapping("/equipment/newevent")
    public String getNewEvent(Model model,
                              @RequestParam(value = "equipmentid") Long id,
                              @RequestParam(value = "action") EquipmentState action) {

        EquipmentEvent event = new EquipmentEvent();
        event.setEquipmentState(action);

        Equipment equipment = equipmentRepository.findById(id).get();

        model.addAttribute("equipment", equipment);
        model.addAttribute("event", event);

        return "equipment.event";


    }

    @PostMapping("/equipment/newevent")
    public String postNewEvent(@ModelAttribute EquipmentEvent event,
                               BindingResult result,
                               @RequestParam(value = "equipmentid") Long id,
                               @RequestParam(value = "action") EquipmentState action,
                               Authentication authentication) {

        Equipment equipment = equipmentRepository.findById(id).get();
        equipment.setState(action);

        equipmentRepository.save(equipment);

        event.setEquipmentWithEquipmentState(equipment);

        User enteredBy = (User) authentication.getPrincipal();
        event.setEnteredBy(enteredBy);

        equipmentEventRepository.save(event);

        return "redirect:/equipment?id=" + id;
    }
}
