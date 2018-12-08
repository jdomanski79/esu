package pl.jdomanski.esu.equipment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.jdomanski.esu.equipmentEvent.EquipmentState;

@Repository
public interface EquipmentRepository extends CrudRepository<Equipment, Long> {
    Long countByState(EquipmentState state);
}
