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

        //TODO dodanie uzytkownika do obiektu eqipment
        //TODO

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

        EquipmentEvent event = new EquipmentEvent(equipment);

        event.setDate(equipment.getCreated());
        event.setNote(dto.getNote());
        event.setEnteredBy(user);

        equipmentEventRepository.save(event);
        log.info("Saved new event {}", event);
        log.info("Equipment after saving event {}", equipment);

        return "redirect:/";
    }

    @GetMapping("/equipment/transfer")
    public String getEquipmentTransfer(Model model,
                                       @RequestParam(value = "equipmentId") Long equipmentId,
                                       @RequestParam(value = "equipmentName") String equipmentName,
                                       @RequestParam(value = "equipmentInventoryNumber") String equipmentInventoryNumber) {

        EquipmentEvent equipmentEvent = new EquipmentEvent();

        model.addAttribute("equipmentEvent", equipmentEvent);
        model.addAttribute("equipmentId", equipmentId);
        model.addAttribute("equipmentName", equipmentName);
        model.addAttribute("equipmentInventoryNumber", equipmentInventoryNumber);

        return "equipment.transfer.html";

    }

    @PostMapping("/equipment/transfer")
    public String postEquipmentTransfer(@Valid EquipmentEvent event, BindingResult result,
                                        @RequestParam(name = "equipmentId") Long equipmentId,
                                        Authentication authentication) {

        Equipment equipment = equipmentRepository.findById(equipmentId).get();
        equipment.setState(EquipmentState.TRANSFERED);
        equipmentRepository.save(equipment);

        event.setEquipmentState(EquipmentState.TRANSFERED);
        event.setEquipment(equipment);
        event.setEnteredBy((User) authentication.getPrincipal());
        equipmentEventRepository.save(event);
        log.info("Equipment id: {}, new event: {}", equipmentId, event.getEquipmentState().getEventDescription());

        return "redirect:/equipment?id=" + equipmentId;
    }

    @GetMapping("/equipment/delete")
    public String getEquipmentDelete(Model model,
                                        @RequestParam(name = "id") Long id) {

        model.addAttribute("equipment", equipmentRepository.findById(id).get());

        return "equipment.delete";
    }

    @PostMapping("/equipment/delete")
    public String postEquipmentDelete(EquipmentEvent event, BindingResult result,
                                         @RequestParam(name = "id") Long id,
                                         Authentication authentication) {

        Equipment equipment = equipmentRepository.findById(id).get();
        equipment.setState(EquipmentState.DELETED);

        equipmentRepository.save(equipment);

        event.setEquipmentState(EquipmentState.DELETED);
        event.setEquipment(equipment);
        event.setEnteredBy((User) authentication.getPrincipal());

        equipmentEventRepository.save(event);

        return "redirect:/equipment?id=" + id;

    }
}
