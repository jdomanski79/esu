package pl.jdomanski.esu;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class EquipmentDTO {

    private String name;
    private String inventoryNumber;
    @Size(min=1)
    private String serialNumber;
    private boolean asset;
    private boolean toDelete;

    private String note;

    public void setName(String name) {
        this.name = name.trim();
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber.trim();
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber.trim();
    }

    public void setNote(String note) {
        this.note = note.trim();
    }
}
