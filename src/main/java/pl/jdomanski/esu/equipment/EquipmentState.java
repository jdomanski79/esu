package pl.jdomanski.esu.equipment;

public enum EquipmentState {
    IN_STOCK("Na stanie"),
    TRANSFERED("Przeniesione"),
    DEPOSIT("Depozyt"),
    DELETED("Skasowane");

    private String description;

    EquipmentState(String description) {
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }
}
