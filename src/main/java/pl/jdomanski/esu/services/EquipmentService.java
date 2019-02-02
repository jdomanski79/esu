package pl.jdomanski.esu.services;

import java.util.Collection;
import java.util.Optional;

import pl.jdomanski.esu.model.Equipment;
import pl.jdomanski.esu.model.EquipmentState;

public interface EquipmentService {
	
	Optional<Equipment> findById( Long id);
	
	Equipment save(Equipment equipment);
	
	Long countByState(EquipmentState state);

	Collection<Equipment> findAllFromQuery(String query, Integer state, Boolean asset, Boolean toDelete);

	Collection<Equipment> findByInventoryNumber(String inventoryNumber);
}
