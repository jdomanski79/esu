package pl.jdomanski.esu.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import pl.jdomanski.esu.model.Equipment;
import pl.jdomanski.esu.repositories.EquipmentEventRepository;
import pl.jdomanski.esu.services.EquipmentService;

public class EquipmentControllerTest {

	EquipmentController controller;
	
	@Mock
	EquipmentService equipmentService;
	
	@Mock
	EquipmentEventRepository equipmentEventRepository;
	
	MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		controller = new EquipmentController(equipmentService, equipmentEventRepository);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	@Ignore
	public void testAddAttributes() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testEquipmentList() throws Exception{
		
		//given
		Equipment equipment = new Equipment();
		equipment.setId(1L);
	
		Collection<Equipment> equipments = new HashSet<Equipment>();
		equipments.add(equipment);
		
		//when
		when(equipmentService.findAllFromQuery(anyString(),anyInt(),anyBoolean(),anyBoolean())).
				thenReturn(equipments);
		
		//then
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("home"))
			.andExpect(model().attributeExists("equipments"));
			
	}

	@Test
	@Ignore
	public void testGetEquipmentDetails() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	@Ignore
	public void testGetEquipmentForm() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	@Ignore
	public void testPostEquipmentForm() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	@Ignore
	public void testGetNewEvent() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	@Ignore
	public void testPostNewEvent() {
		fail("Not yet implemented"); // TODO
	}

}
