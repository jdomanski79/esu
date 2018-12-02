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
    @ManyToOne
    @JoinColumn(name = "edited_by")
    private User editedBy;
    private LocalDate edited;
    private String description;
    private EquipmentState state = EquipmentState.IN_STOCK;
    private String serialNumber;
    private String lastOwner;
    private String lastInventoryNumber;

    @OneToMany(mappedBy="equipment")
    private List<EquipmentEvent> history;
}
