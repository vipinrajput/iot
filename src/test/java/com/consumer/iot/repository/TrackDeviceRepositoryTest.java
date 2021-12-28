package com.consumer.iot.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.consumer.iot.custom.exception.ElementNotFoundException;
import com.consumer.iot.dto.model.CSVBinderDTO;
import com.consumer.iot.mapper.TrackDeviceResponseMapper;
import com.consumer.iot.service.TrackDeviceService;

@ExtendWith(MockitoExtension.class)
public class TrackDeviceRepositoryTest {

	@InjectMocks
	private TrackDeviceRepository deviceRepository;
	@Mock
	private TrackDeviceService trackDeviceService;
	@Mock
	TrackDeviceResponseMapper mapper;

	private void loadData() {
		List<Object> csvBinderDTOs = new ArrayList<>();
		CSVBinderDTO csvBinderDTO = new CSVBinderDTO();
		csvBinderDTO.setAirplaneMode("ON");
		csvBinderDTO.setBattery(0.99);
		csvBinderDTO.setDateTime(1582605077000L);
		csvBinderDTO.setEventId(10012L);
		csvBinderDTO.setLatitude(new BigDecimal(51.5185));
		csvBinderDTO.setLongitude(new BigDecimal(-0.1736));
		csvBinderDTO.setLight("OFF");
		csvBinderDTO.setProductId("WG11155638");
		csvBinderDTOs.add(csvBinderDTO);
		csvBinderDTOs.add(csvBinderDTO);
		deviceRepository.saveData(csvBinderDTOs);
		assertTrue(deviceRepository.csvBinderMap.size() > 0);
	}

	/**
	 * Fetch data according to product id
	 * 
	 * @param productId
	 * @param dateTime
	 * @return
	 */
	@Test
	public void testFetchList() {
		loadData();
		List<CSVBinderDTO> list = deviceRepository.fetchList("WG11155638", 1582605077000L);
		assertTrue(!list.isEmpty());
	}

	/**
	 * Fetch data on TimeStamp base.
	 * 
	 * @param productId
	 * @param dateTime
	 * @return
	 * @throws ElementNotFoundException
	 */
	@Test
	public void testFetchDataOnTimeStamp() throws ElementNotFoundException {
		loadData();
		CSVBinderDTO binderDTO=deviceRepository.fetchDataOnTimeStamp("WG11155638", 1582605077000L);
		assertEquals(binderDTO.getAirplaneMode(), "ON");
		assertEquals(binderDTO.getProductId(), "WG11155638");
	}
}
