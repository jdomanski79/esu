package pl.jdomanski.esu.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.jdomanski.esu.model.Equipment;
import pl.jdomanski.esu.repositories.EquipmentRepository;

public class EquipmentServiceImplTest {

	EquipmentServiceImpl equipmentService;

	@Mock
	EquipmentRepository equipmentRepository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		equipmentService = new EquipmentServiceImpl(equipmentRepository);
	}

	@Test
	public void serviceExists() {
		assertNotNull(equipmentService);
	}

	@Test
	public void findByIdTest() {
		final Long testedId = 1l;
		// given
		Equipment equipment = new Equipment();
		equipment.setId(testedId);
		Optional<Equipment> optionalEquipment = Optional.of(equipment);
		
		//when
		when(equipmentRepository.findById(testedId)).thenReturn(optionalEquipment);
		
		//then
		assertEquals(equipmentService.findById(testedId), optionalEquipment);
		verify(equipmentRepository,times(1)).findById(testedId);

	}

}
