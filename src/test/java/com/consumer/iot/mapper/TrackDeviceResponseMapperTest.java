package com.consumer.iot.mapper;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.consumer.iot.dto.model.CSVBinderDTO;
import com.consumer.iot.dto.response.TrackDeviceResponseDTO;

@ExtendWith(MockitoExtension.class)
public class TrackDeviceResponseMapperTest {

	@InjectMocks
	private TrackDeviceResponseMapper deviceResponseMapper;
	
	private CSVBinderDTO generateDataInactive() {
		CSVBinderDTO csvBinderDTO = new CSVBinderDTO();
		csvBinderDTO.setAirplaneMode("ON");
		csvBinderDTO.setBattery(0.99);
		csvBinderDTO.setDateTime(1582605077000L);
		csvBinderDTO.setEventId(10012L);
		csvBinderDTO.setLatitude(new BigDecimal(51.5185));
		csvBinderDTO.setLongitude(new BigDecimal(-0.1736));
		csvBinderDTO.setLight("OFF");
		csvBinderDTO.setProductId("WG11155638");
		return csvBinderDTO;
	}
	
	private CSVBinderDTO generateDataActive() {
		CSVBinderDTO csvBinderDTO = new CSVBinderDTO();
		csvBinderDTO.setAirplaneMode("OFF");
		csvBinderDTO.setBattery(0.99d);
		csvBinderDTO.setDateTime(1582605077000L);
		csvBinderDTO.setEventId(10012L);
		csvBinderDTO.setLatitude(new BigDecimal(51.5185));
		csvBinderDTO.setLongitude(new BigDecimal(-0.1736));
		csvBinderDTO.setLight("OFF");
		csvBinderDTO.setProductId("WG11155638");
		return csvBinderDTO;
	}

	@Test
	public void trackDeviceResponseDTOMapper() {
		CSVBinderDTO csvBinderDTO = generateDataActive();
		TrackDeviceResponseDTO deviceResponseDTO = deviceResponseMapper.trackDeviceResponseDTOMapper(csvBinderDTO);
		assertEquals(deviceResponseDTO.getProductId(), "WG11155638");
		assertEquals(deviceResponseDTO.getStatus(), "Active");
		
		csvBinderDTO = generateDataInactive();
		deviceResponseDTO = deviceResponseMapper.trackDeviceResponseDTOMapper(csvBinderDTO);
		assertEquals(deviceResponseDTO.getProductId(), "WG11155638");
		assertEquals(deviceResponseDTO.getStatus(), "Inactive");
	}
	
}


