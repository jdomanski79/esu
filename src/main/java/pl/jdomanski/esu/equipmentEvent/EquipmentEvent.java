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
    private EquipmentState equipmentState;
    @ManyToOne
    @JoinColumn(name="entered_by")
    private User enteredBy;
    private LocalDate date = LocalDate.now();
    private String document;
    private String note;

    public EquipmentEvent(Equipment equipment) {
        this.equipment = equipment;
        this.equipmentState = equipment.getState();
    }

    public EquipmentEvent(){
    };

}
