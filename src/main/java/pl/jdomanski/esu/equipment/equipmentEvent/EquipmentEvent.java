package pl.jdomanski.esu.equipment.equipmentEvent;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class EquipmentEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //@ManyToOne
    //private long equipmentId;
    private EquipmentEventType eventType;
    private LocalDateTime date;
    private String whoTaken;
    private String transferPlace;

}
