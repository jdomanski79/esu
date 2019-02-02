package pl.jdomanski.esu.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pl.jdomanski.esu.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Slf4j
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
    private LocalDateTime timestamp = LocalDateTime.now();
    private String document;
    private String note;


    public void setEquipmentWithEquipmentState(Equipment equipment){
        this.equipment = equipment;
        this.equipmentState = equipment.getState();
    }

    @Override
    public String toString() {
        return "EquipmentEvent{" +
                "id=" + id +
                ", equipmentID=" + equipment.getId() +
                ", equipmentState=" + equipmentState +
                ", enteredBy(Name)=" + enteredBy.getName() +
                ", date=" + date +
                ", timestamp=" + timestamp +
                ", document='" + document + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public EquipmentEvent(){
        log.info("New equipment event constructed");
    }

}
