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
import java.util.Collection;
import java.util.List;
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
    public void addAttributes(Model model) {

        Long equipmentsLent = equipmentRepository.countByState(EquipmentState.LENDED);
        Long equipmentsInStock = equipmentsLent + equipmentRepository.countByState(EquipmentState.IN_STOCK);

        model.addAttribute("equipmentsLent", equipmentsLent);
        model.addAttribute("equipmentsInStock", equipmentsInStock);
    }

    @GetMapping("/")
    public String equipmentList(Model model,
                                @RequestParam(name = "query", required = false, defaultValue = "") String query,
                                @RequestParam(name = "state", required = false, defaultValue = "0") Integer state,
                                @RequestParam(name = "attribs", required = false, defaultValue = "all") String attribs) {


        model.addAttribute("state", state);
        model.addAttribute("attribs", attribs);
        model.addAttribute("states", EquipmentState.values());
        model.addAttribute("query", query);

//        log.debug("request params: query{}"dd, );

        Boolean asset = null;
        Boolean toDelete = null;
        switch (attribs) {
            case "all":
                break;
            case "st":
                asset = true;
                break;
            case "nn":
                asset = false;
                break;
            case "all_toDelete":
                toDelete = true;
                break;
            case "st_toDelete":
                asset = true;
                toDelete = true;
                break;
            case "nn_toDelete":
                asset = false;
                toDelete = true;
                break;
        }
        log.info("Model is {}", model);

        log.info("State is {}", state);
        query = "%" + query + "%";
        if (state < 0) {
            state = null;
        }
        Collection<Equipment> equipments = equipmentRepository.findAllFromQuery(query, state, asset, toDelete);
        model.addAttribute("equipments", equipments);
        return "home";
    }

    @GetMapping("/equipment")
    public String getEquipmentDetails(Model model,
                                      @RequestParam(value = "id") Long id) {

        Equipment equipment = equipmentRepository.findById(id).get();

        log.info("getEquipmentDetails for id {}", id);
        model.addAttribute("equipment", equipment);

        return "equipment.view";
    }


    @GetMapping("/equipment/edit")
    public String getEquipmentForm(@RequestParam(name = "id", required = false, defaultValue = "-1") Long id,
                                   Model model) {

        Optional optionalEquipment = equipmentRepository.findById(id);
        EquipmentDTO dto = new EquipmentDTO();
        boolean newEquipmentMode = true;

        if (optionalEquipment.isPresent()) {
            Equipment equipment = (Equipment) optionalEquipment.get();
            dto.copyPropertiesFrom(equipment);
            newEquipmentMode = false;
        }

        model.addAttribute("dto", dto);
        model.addAttribute("newEquipmentMode", newEquipmentMode);
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

        if (id < 0 && !dto.isDisplayWarning()) {
            if (equipmentRepository.findByInventoryNumber(dto.getInventoryNumber()) != null){
                dto.setDisplayWarning(true);
                return "equipment.form";
            }
        }

        Equipment equipment;
        Optional optionalEquipment = equipmentRepository.findById(id);

        if (optionalEquipment.isPresent()) {
            equipment = (Equipment) optionalEquipment.get();
        } else {
            equipment = new Equipment();
        }


        User user = (User) authentication.getPrincipal();
        setEquipment(equipment, dto, user);

        equipmentRepository.save(equipment);

        if (optionalEquipment.isPresent()) {
            log.info("Edited equipment - redirecting");
            return "redirect:/equipment?id=" + id ;
        }

        log.info("Saved new equipment {}", equipment);

        EquipmentEvent event = new EquipmentEvent();

        event.setEquipmentWithEquipmentState(equipment);
        event.setDate(dto.getReceptionDate());
        event.setNote(dto.getEventNote());
        event.setEnteredBy(user);

        equipmentEventRepository.save(event);
        log.info("Saved new event {}", event);
        log.info("Equipment after saving event {}", equipment);

        return "redirect:/equipment?id="+id;
    }

    private void setEquipment(Equipment equipment, EquipmentDTO dto,
                              User user) {
        equipment.setName(dto.getName());
        equipment.setInventoryNumber(dto.getInventoryNumber());
        equipment.setSerialNumber(dto.getSerialNumber());
        equipment.setAsset(dto.isAsset());
        equipment.setToDelete(dto.isToDelete());
        equipment.setNote(dto.getEquipmentNote());

        equipment.setCreatedBy(user);
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
