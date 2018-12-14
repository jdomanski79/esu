package pl.jdomanski.esu;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.jdomanski.esu.equipment.Equipment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class EquipmentDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date = LocalDate.now();
    private String name;
    @NotNull
    private String inventoryNumber;
    private String serialNumber;
    private boolean asset;
    private boolean toDelete;
    private String equipmentNote;
    private String eventNote;

    public void setName(String name) {
        this.name = name.trim();
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber.trim();
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber.trim();
    }

    public void copyPropertiesFrom(Equipment equipment) {
        this.date = equipment.getCreated();
        this.name = equipment.getName();
        this.inventoryNumber = equipment.getInventoryNumber();
        this.serialNumber = equipment.getSerialNumber();
        this.asset = equipment.isAsset();
        this.toDelete = equipment.isToDelete();
        this.equipmentNote = equipment.getNote();
    }
}
