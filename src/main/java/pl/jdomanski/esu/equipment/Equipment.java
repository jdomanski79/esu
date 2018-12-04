package pl.jdomanski.esu.equipment;

import lombok.Data;
import pl.jdomanski.esu.equipmentEvent.EquipmentEvent;
import pl.jdomanski.esu.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String inventoryNumber;
    private String name;
    private boolean toDelete;
    private boolean asset;
    private LocalDate created = LocalDate.now();
    @ManyToOne
    @JoinColumn(name="created_by")
    private User createdBy;
    private EquipmentState state = EquipmentState.IN_STOCK;
    private String serialNumber;

    @OneToMany(mappedBy="equipment")
    private List<EquipmentEvent> history;
}
