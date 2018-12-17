package pl.jdomanski.esu.equipment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.jdomanski.esu.equipmentEvent.EquipmentState;

import java.util.Collection;
import java.util.List;

@Repository
public interface EquipmentRepository extends CrudRepository<Equipment, Long> {
    Long countByState(EquipmentState state);

    @Query("SELECT e from Equipment e where "
            + "(?1 is null or "
            + "lower(e.inventoryNumber) like ?1 or "
            + "lower(e.name) like ?1 or "
            + "lower(e.serialNumber) like ?1)" +
            " and (?2 is null or e.state = ?2)" +
            " and (?3 is null or e.asset = ?3)" +
            " and (?4 is null or e.toDelete= ?4)")
    Collection<Equipment> findAllFromQuery(String query, Integer state, Boolean asset, Boolean toDelete);
}
