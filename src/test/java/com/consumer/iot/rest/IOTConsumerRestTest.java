package com.consumer.iot.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.consumer.iot.constants.Message;
import com.consumer.iot.custom.exception.ElementNotFoundException;
import com.consumer.iot.dto.request.TrackDeviceRequestDTO;
import com.consumer.iot.dto.response.TrackDeviceResponseDTO;
import com.consumer.iot.service.TrackDeviceService;

@ExtendWith(MockitoExtension.class)
public class IOTConsumerRestTest {

	@InjectMocks
	private IOTConsumerRest iotConsumerRest;
	@Mock
	private TrackDeviceService trackDeviceService;

	@Test
	public void testRefreshDataSuccessful() throws FileNotFoundException {
		TrackDeviceResponseDTO deviceResponseDTO = TrackDeviceResponseDTO.builder().battery("High").status("Active")
				.lat("51.5185").longitude("-0.1736").datetime("25/02/2020 10:02:17").name("CyclePlusTracker")
				.productId("WG11155638").description("SUCCESS: Location identified.").build();

		when(trackDeviceService.loadTrackDeviceData(Mockito.any(TrackDeviceRequestDTO.class)))
				.thenReturn(deviceResponseDTO);
		TrackDeviceRequestDTO deviceRequestDTO = new TrackDeviceRequestDTO();
		deviceRequestDTO.setFilePath("./data.csv");
		ResponseEntity<TrackDeviceResponseDTO> responseEntity = iotConsumerRest.refreshData(deviceRequestDTO);
		assertTrue(responseEntity.hasBody());
		assertEquals(200, responseEntity.getStatusCodeValue());
		assertEquals(responseEntity.getBody().getDescription(), "SUCCESS: Location identified.");
		assertEquals(responseEntity.getBody().getBattery(), "High");
	}

	@Test
	public void testRefreshDataUnsuccessful() throws FileNotFoundException {
		TrackDeviceRequestDTO deviceRequestDTO = new TrackDeviceRequestDTO();
		deviceRequestDTO.setFilePath("./data.csv");
		when(trackDeviceService.loadTrackDeviceData(Mockito.any(TrackDeviceRequestDTO.class)))
				.thenThrow(new FileNotFoundException());
		assertThrows(FileNotFoundException.class, () -> iotConsumerRest.refreshData(deviceRequestDTO));
	}

	@Test
	public void testFetchTrackDeviceDataSuccessful() {
		// Given
		TrackDeviceResponseDTO deviceResponseDTO = TrackDeviceResponseDTO.builder().productId("6900001001")
				.name("GeneralTracker").datetime("25/02/2020 04:34:18").longitude("-73.935242").lat("40.73071")
				.status("Active").battery("Low").description(Message.SUCCESS_DESCRIPTION).build();
		given(trackDeviceService.fetchTrackDeviceData(anyString(), any(Long.class))).willReturn(deviceResponseDTO);

		// When
		ResponseEntity<TrackDeviceResponseDTO> responseEntity = iotConsumerRest.fetchDeviceData("123",
				Optional.of("456"));
		TrackDeviceResponseDTO ioTResponseDTO = (TrackDeviceResponseDTO) responseEntity.getBody();

		// Then
		assertThat(responseEntity, notNullValue());
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		assertThat(ioTResponseDTO, notNullValue());
		assertThat(ioTResponseDTO.getProductId(), is("6900001001"));
		assertThat(ioTResponseDTO.getName(), is("GeneralTracker"));
		assertThat(ioTResponseDTO.getDatetime(), is("25/02/2020 04:34:18"));
		assertThat(ioTResponseDTO.getLongitude(), is("-73.935242"));
		assertThat(ioTResponseDTO.getLat(), is("40.73071"));
		assertThat(ioTResponseDTO.getStatus(), is("Active"));
		assertThat(ioTResponseDTO.getBattery(), is("Low"));
		assertThat(ioTResponseDTO.getDescription(), is(Message.SUCCESS_DESCRIPTION));
	}

    @Test
    public void testFetchTrackDeviceDataException() {
        when(trackDeviceService.fetchTrackDeviceData(anyString(), any(Long.class))).thenThrow(new ElementNotFoundException());
        assertThrows(ElementNotFoundException.class, () -> iotConsumerRest.fetchDeviceData("123", null));
    }

}
