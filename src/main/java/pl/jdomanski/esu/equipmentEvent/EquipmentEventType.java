package pl.jdomanski.esu.equipmentEvent;

public enum EquipmentEventType {
    RECEPTION("Przyjęcie"),
    TRANSFER("Przerzut"),
    DELETE("Kasacja"),
    RERECEPTION("Ponowne przyjęcie");

    private String description;

    public String getDescription() {
        return description;
    }

    EquipmentEventType(String description) {
        this.description = description;
    }
}