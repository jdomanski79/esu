package pl.jdomanski.esu.equipmentEvent;

import lombok.Data;
import pl.jdomanski.esu.equipment.Equipment;
import pl.jdomanski.esu.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class EquipmentEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="equipment_id")
    private Equipment equipment;
    private EquipmentEventType type;
    private LocalDate date;
    private String person;
    private String place;
    @ManyToOne
    @JoinColumn(name="entered_by")
    private User enteredBy;
    private String document;

}
