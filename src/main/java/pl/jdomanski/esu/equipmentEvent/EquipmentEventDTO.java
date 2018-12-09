package pl.jdomanski.esu.equipmentEvent;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Slf4j
public class EquipmentEventDTO {

    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String note;
    private String document;
    private EquipmentState state;

    public EquipmentEventDTO(EquipmentEvent event){
        log.info("Constructor EquipmentEventDTO for event {}", event);
        this.id = event.getId();
        this.date = event.getDate();
        this.document = event.getDocument();
        this.note = event.getNote();
        this.state = event.getEquipmentState();
    }

    public EquipmentEventDTO(){
        log.info("Constructor of Equipment event DTO ");
    }
}
