package com.consumer.iot.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.consumer.iot.custom.exception.AirplaneModeEnableException;
import com.consumer.iot.custom.exception.ElementNotFoundException;
import com.consumer.iot.dto.model.CSVBinderDTO;
import com.consumer.iot.dto.request.TrackDeviceRequestDTO;
import com.consumer.iot.dto.response.TrackDeviceResponseDTO;
import com.consumer.iot.mapper.TrackDeviceResponseMapper;
import com.consumer.iot.repository.TrackDeviceRepository;

@ExtendWith(MockitoExtension.class)
public class TrackDeviceServiceImplTest {

	@InjectMocks
	private TrackDeviceServiceImpl trackDeviceService;
	@Mock
	private TrackDeviceRepository deviceRepository;
	@Mock
	TrackDeviceResponseMapper mapper;

	private String path = "./data.csv";

	/**
	 * Method to Test: loadData What is the Scenario: Successful service call which
	 * receives a valid CSV path and updates the IoT list What is the Result:
	 * Returns a deviceResponseDTO with the expected description
	 */
	@Test
	public void testLoadTrackDeviceData() throws FileNotFoundException {
		TrackDeviceRequestDTO deviceRequestDTO = new TrackDeviceRequestDTO();
		deviceRequestDTO.setFilePath(path);		
		TrackDeviceResponseDTO deviceResponseDTO = trackDeviceService.loadTrackDeviceData(deviceRequestDTO);

		assertThat(deviceResponseDTO, notNullValue());
		assertThat(deviceResponseDTO.getDescription(), is("data refreshed"));
	}

	@Test
	public void testFetchTrackDeviceData() throws AirplaneModeEnableException, ElementNotFoundException {
		CSVBinderDTO csvBinderDTO = new CSVBinderDTO();
		csvBinderDTO.setAirplaneMode("ON");
		csvBinderDTO.setBattery(0.99);
		csvBinderDTO.setDateTime(1582605077000L);
		csvBinderDTO.setEventId(10012L);
		csvBinderDTO.setLatitude(new BigDecimal(51.5185));
		csvBinderDTO.setLongitude(new BigDecimal(-0.1736));
		csvBinderDTO.setLight("OFF");
		csvBinderDTO.setProductId("WG11155638");
		when(deviceRepository.fetchDataOnTimeStamp(Mockito.anyString(), Mockito.anyLong())).thenReturn(csvBinderDTO);

		TrackDeviceResponseDTO deviceResponseDTO = TrackDeviceResponseDTO.builder().battery("High").status("Active")
				.lat("51.5185").longitude("-0.1736").datetime("25/02/2020 10:02:17").name("CyclePlusTracker")
				.productId("WG11155638").description("SUCCESS: Location identified.").build();
		when(mapper.trackDeviceResponseDTOMapper(Mockito.any(CSVBinderDTO.class))).thenReturn(deviceResponseDTO);

		List<CSVBinderDTO> binderDTOs = new ArrayList<>();
		binderDTOs.add(csvBinderDTO);
		binderDTOs.add(csvBinderDTO);
		binderDTOs.add(csvBinderDTO);
		when(deviceRepository.fetchList(Mockito.anyString(), Mockito.anyLong())).thenReturn(binderDTOs);

		TrackDeviceResponseDTO trackDeviceResponseDTO = trackDeviceService.fetchTrackDeviceData(path, 0);

		assertThat(trackDeviceResponseDTO, notNullValue());
		assertThat(trackDeviceResponseDTO.getDescription(), is("SUCCESS: Location identified."));
	}
	
	@Test
	public void testFetchTrackDeviceDataNA() throws AirplaneModeEnableException, ElementNotFoundException {
		CSVBinderDTO csvBinderDTO = new CSVBinderDTO();
		csvBinderDTO.setAirplaneMode("ON");
		csvBinderDTO.setBattery(0.99);
		csvBinderDTO.setDateTime(1582605077000L);
		csvBinderDTO.setEventId(10012L);
		csvBinderDTO.setLatitude(new BigDecimal(51.5185));
		csvBinderDTO.setLongitude(new BigDecimal(-0.1736));
		csvBinderDTO.setLight("OFF");
		csvBinderDTO.setProductId("WG11155638");
		when(deviceRepository.fetchDataOnTimeStamp(Mockito.anyString(), Mockito.anyLong())).thenReturn(csvBinderDTO);

		TrackDeviceResponseDTO deviceResponseDTO = TrackDeviceResponseDTO.builder().battery("High").status("Active")
				.lat("51.5185").longitude("-0.1736").datetime("25/02/2020 10:02:17").name("CyclePlusTracker")
				.productId("WG11155638").description("SUCCESS: Location identified.").build();
		when(mapper.trackDeviceResponseDTOMapper(Mockito.any(CSVBinderDTO.class))).thenReturn(deviceResponseDTO);

		List<CSVBinderDTO> binderDTOs = new ArrayList<>();
		binderDTOs.add(csvBinderDTO);
		binderDTOs.add(csvBinderDTO);
		when(deviceRepository.fetchList(Mockito.anyString(), Mockito.anyLong())).thenReturn(binderDTOs);

		TrackDeviceResponseDTO trackDeviceResponseDTO = trackDeviceService.fetchTrackDeviceData(path, 0);

		assertThat(trackDeviceResponseDTO, notNullValue());
		assertThat(trackDeviceResponseDTO.getDescription(), is("SUCCESS: Location identified."));
	}

	@Test
	public void testFetchTrackDeviceDataActive() throws AirplaneModeEnableException, ElementNotFoundException {
		CSVBinderDTO csvBinderDTO = new CSVBinderDTO();
		csvBinderDTO.setAirplaneMode("ON");
		csvBinderDTO.setBattery(0.99);
		csvBinderDTO.setDateTime(1582605077000L);
		csvBinderDTO.setEventId(10012L);
		csvBinderDTO.setLatitude(new BigDecimal(51.5185));
		csvBinderDTO.setLongitude(new BigDecimal(-0.1736));
		csvBinderDTO.setLight("OFF");
		csvBinderDTO.setProductId("WG11155638");
		when(deviceRepository.fetchDataOnTimeStamp(Mockito.anyString(), Mockito.anyLong())).thenReturn(csvBinderDTO);

		TrackDeviceResponseDTO deviceResponseDTO = TrackDeviceResponseDTO.builder().battery("High").status("Active")
				.lat("51.5185").longitude("-0.1736").datetime("25/02/2020 10:02:17").name("CyclePlusTracker")
				.productId("WG11155638").description("SUCCESS: Location identified.").build();
		when(mapper.trackDeviceResponseDTOMapper(Mockito.any(CSVBinderDTO.class))).thenReturn(deviceResponseDTO);

		List<CSVBinderDTO> binderDTOs = new ArrayList<>();
		binderDTOs.add(csvBinderDTO);
		binderDTOs.add(csvBinderDTO);
		csvBinderDTO = new CSVBinderDTO();
		csvBinderDTO.setAirplaneMode("ON");
		csvBinderDTO.setBattery(0.99);
		csvBinderDTO.setDateTime(1582605077000L);
		csvBinderDTO.setEventId(10012L);
		csvBinderDTO.setLatitude(new BigDecimal(51.5115));
		csvBinderDTO.setLongitude(new BigDecimal(-0.1726));
		csvBinderDTO.setLight("OFF");
		csvBinderDTO.setProductId("WG11155638");
		binderDTOs.add(csvBinderDTO);
		when(deviceRepository.fetchList(Mockito.anyString(), Mockito.anyLong())).thenReturn(binderDTOs);

		TrackDeviceResponseDTO trackDeviceResponseDTO = trackDeviceService.fetchTrackDeviceData(path, 0);

		assertThat(trackDeviceResponseDTO, notNullValue());
		assertThat(trackDeviceResponseDTO.getDescription(), is("SUCCESS: Location identified."));
	}

	@Test
	public void testFetchTrackDeviceDataFailed() throws AirplaneModeEnableException, ElementNotFoundException {
		CSVBinderDTO csvBinderDTO = new CSVBinderDTO();
		csvBinderDTO.setAirplaneMode("OFF");
		csvBinderDTO.setBattery(0.99);
		csvBinderDTO.setDateTime(1582605077000L);
		csvBinderDTO.setEventId(10012L);
		csvBinderDTO.setLatitude(null);
		csvBinderDTO.setLongitude(null);
		csvBinderDTO.setLight("OFF");
		csvBinderDTO.setProductId("WG11155638");
		when(deviceRepository.fetchDataOnTimeStamp(Mockito.anyString(), Mockito.anyLong())).thenReturn(csvBinderDTO);

		assertThrows(AirplaneModeEnableException.class, () -> trackDeviceService.fetchTrackDeviceData(path, 0));
	}
}
