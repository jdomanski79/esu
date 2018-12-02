package pl.jdomanski.esu.equipment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.jdomanski.esu.equipmentEvent.EquipmentEvent;
import pl.jdomanski.esu.equipmentEvent.EquipmentEventRepository;
import pl.jdomanski.esu.equipmentEvent.EquipmentEventType;

import javax.validation.Valid;

@Controller
@Slf4j
public class EquipmentController {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentEventRepository equipmentEventRepository;

    @Autowired
    public EquipmentController(EquipmentRepository equipmentRepository, EquipmentEventRepository equipmentEventRepository ){
        this.equipmentRepository = equipmentRepository;
        this.equipmentEventRepository = equipmentEventRepository;
    }


    @ModelAttribute
    public void addStatesToModel(Model model){
        model.addAttribute("states", EquipmentState.values());
    }

    @GetMapping("/")
    public String equipmentList(Model model){
        Equipment eq = new Equipment();
         model.addAttribute("equipments", equipmentRepository.findAll());

        return "home";
    }

    @GetMapping("/equipment")
    public String getEquipmentForm(Model model,
                                   @RequestParam(value = "id", required = false) Long id){
        log.info("getEquipmentForm method. id = {}", id);

        String formTitle;
        Equipment equipment;
        if (id == null){
            formTitle = "Nowy sprzet";
            equipment = new Equipment();
        } else {
            formTitle = "Dane sprzetu";
            equipment = equipmentRepository.findById(id).get();
        }

        model.addAttribute("equipment", equipment);
        model.addAttribute("formTitle",formTitle);
        return "equipment.form";
    }

    @PostMapping("/equipment")
    public String postEquipmentForm(@Valid Equipment equipment, BindingResult result,
                                    RedirectAttributes redirectAttributes, Model model){
        if (result.hasErrors()){
            model.addAttribute("equipment", equipment);
            redirectAttributes.addFlashAttribute("message", "W formularzu sa bledy");
        }

        //TODO dodanie uzytkownika do obiektu eqipment



        equipmentRepository.save(equipment);
        log.info("Saved new equipment {}", equipment);

        EquipmentEvent event = new EquipmentEvent();

        event.setEquipment(equipment);
        event.setDate(equipment.getCreated());
        event.setType(EquipmentEventType.RECEPTION);

        equipmentEventRepository.save(event);
        log.info("Saved new event {}", event);
        log.info("Equpment after saving event {}", equipment);

        return "redirect:/";
    }

}
