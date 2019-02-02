package pl.jdomanski.esu.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.jdomanski.esu.model.EquipmentEvent;

@Repository
public interface EquipmentEventRepository extends CrudRepository<EquipmentEvent,Long> {
}
