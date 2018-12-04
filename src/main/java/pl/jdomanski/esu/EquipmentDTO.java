package pl.jdomanski.esu;

import lombok.Data;

@Data
public class EquipmentDTO {

    private String name;
    private String inventoryNumber;
    private String serialNumber;
    private boolean asset;
    private boolean toDelete;

    private String note;

}
