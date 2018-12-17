package pl.jdomanski.esu.equipment;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pl.jdomanski.esu.equipmentEvent.EquipmentEvent;
import pl.jdomanski.esu.equipmentEvent.EquipmentState;
import pl.jdomanski.esu.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Slf4j
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String inventoryNumber;
    private String name;
    private boolean toDelete;
    private boolean asset;
    private String note;
    @ManyToOne
    @JoinColumn(name="created_by")
    private User createdBy;
    private EquipmentState state = EquipmentState.IN_STOCK;
    private String serialNumber;

    @OneToMany(mappedBy="equipment")
    @OrderBy("timestamp DESC")
    private List<EquipmentEvent> history;

    public Equipment(){
        log.debug("New equipment constructed");
    }
}
