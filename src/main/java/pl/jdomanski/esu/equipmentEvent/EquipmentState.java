package pl.jdomanski.esu.equipmentEvent;

public enum EquipmentState {
    IN_STOCK("Przyjęcie", "Na stanie"),
    TRANSFERED("Przerzut", "Przeniesione"),
    DELETED("Kasacja", "Skasowane"),
    LENDED("Wypozyczenie", "Wypozyczone");

    private String eventDescription;
    private String stateDescription;

    public String getEventDescription() {
        return eventDescription;
    }

    public String getStateDescription(){
        return stateDescription;
    }

    EquipmentState(String eventDescription, String stateDescription) {
        this.eventDescription = eventDescription;
        this.stateDescription = stateDescription;
    }
}
