package pl.jdomanski.esu.equipmentEvent;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentEventRepository extends CrudRepository<EquipmentEvent,Long> {
}
