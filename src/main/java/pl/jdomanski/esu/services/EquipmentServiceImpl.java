package pl.jdomanski.esu.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pl.jdomanski.esu.model.Equipment;
import pl.jdomanski.esu.model.EquipmentState;
import pl.jdomanski.esu.repositories.EquipmentRepository;

@Service
public class EquipmentServiceImpl implements EquipmentService{

	private final EquipmentRepository equipmentRepository;
	
	public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
		this.equipmentRepository = equipmentRepository;
	}

	@Override
	public Optional<Equipment> findById(Long id) {
		return equipmentRepository.findById(id);
	}
	
	@Override
	public Equipment save(Equipment equipment) {
		return equipmentRepository.save(equipment);
	}

	@Override
	public Long countByState(EquipmentState state) {
		return equipmentRepository.countByState(state);
	}

	@Override
	public Collection<Equipment> findAllFromQuery(String query, Integer state, Boolean asset, Boolean toDelete) {
		return equipmentRepository.findAllFromQuery(query, state, asset, toDelete);
	}

	@Override
	public Collection<Equipment> findByInventoryNumber(String inventoryNumber) {
		return equipmentRepository.findByInventoryNumber(inventoryNumber);
	}

}
