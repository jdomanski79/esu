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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.jdomanski.esu.EquipmentDTO;
import pl.jdomanski.esu.equipmentEvent.EquipmentEvent;
import pl.jdomanski.esu.equipmentEvent.EquipmentEventRepository;
import pl.jdomanski.esu.equipmentEvent.EquipmentState;
import pl.jdomanski.esu.user.User;

import javax.validation.Valid;

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

    @GetMapping("/equipment/new")
    public String getEquipmentForm(Model model) {

        EquipmentDTO dto = new EquipmentDTO();

        model.addAttribute("dto", dto);
        model.addAttribute("formTitle", "Podaj dane nowego sprzetu:e");
        return "equipment.form";
    }

    @PostMapping("/equipment/new")
    public String postEquipmentForm(@Valid EquipmentDTO dto,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    Model model,
                                    Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("equipmentDTO", dto);
            redirectAttributes.addFlashAttribute("message", "W formularzu sa bledy");
        }

        User user = (User) authentication.getPrincipal();

        Equipment equipment = new Equipment();

        equipment.setName(dto.getName());
        equipment.setInventoryNumber(dto.getInventoryNumber());
        equipment.setSerialNumber(dto.getSerialNumber());
        equipment.setAsset(dto.isAsset());
        equipment.setToDelete(dto.isToDelete());
        equipment.setCreatedBy(user);

        equipmentRepository.save(equipment);

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
