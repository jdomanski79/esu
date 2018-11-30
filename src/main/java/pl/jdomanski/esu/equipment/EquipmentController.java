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

import javax.validation.Valid;

@Controller
@Slf4j
public class EquipmentController {

    private final EquipmentRepository repository;

    @Autowired
    public EquipmentController(EquipmentRepository repository){
        this.repository = repository;
    }


    @ModelAttribute
    public void addStatesToModel(Model model){
        model.addAttribute("states", EquipmentState.values());
    }

    @GetMapping("/")
    public String equipmentList(Model model){
        Equipment eq = new Equipment();
         model.addAttribute("equipments", repository.findAll());

        return "home";
    }

    @GetMapping("/equipment")
    public String getEquipmentForm(Model model,
                                   @RequestParam(value = "id", required = false) Long id){
        log.info("getEquipmentForm method. id = {}", id);

        String formTitle;
        if (id == null){
            formTitle = "Nowy sprzet";
        } else {
            formTitle = "Dane sprzetu";
            Equipment equipment = repository.findById(id).get();
            model.addAttribute("equipment", equipment);
        }

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

        repository.save(equipment);
        log.info("Saved new equipment {}", equipment);

        return "redirect:/";
    }

}
