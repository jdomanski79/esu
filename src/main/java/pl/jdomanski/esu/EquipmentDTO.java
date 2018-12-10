package pl.jdomanski.esu;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class EquipmentDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date = LocalDate.now();
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
