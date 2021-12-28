package com.consumer.iot.rest;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.consumer.iot.custom.exception.AirplaneModeEnableException;
import com.consumer.iot.custom.exception.ElementNotFoundException;
import com.consumer.iot.dto.request.TrackDeviceRequestDTO;
import com.consumer.iot.dto.response.TrackDeviceResponseDTO;
import com.consumer.iot.service.TrackDeviceService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/iot", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class IOTConsumerRest {

	private static final Logger LOGGER = LoggerFactory.getLogger(IOTConsumerRest.class);

	@Autowired
	TrackDeviceService trackDeviceService;

	/**
	 * This end point save(refresh) device data in memory.
	 * 
	 * @param deviceRequestDTO
	 * @return
	 * @throws FileNotFoundException
	 */
	@ApiOperation(value = "To save data", response = TrackDeviceResponseDTO.class, tags = "refreshData")
	@PostMapping(path = "/event/v1")
	public ResponseEntity<TrackDeviceResponseDTO> refreshData(@RequestBody TrackDeviceRequestDTO deviceRequestDTO)
			throws FileNotFoundException {
		LOGGER.info("refreshData API calling");
		return new ResponseEntity<>(trackDeviceService.loadTrackDeviceData(deviceRequestDTO), HttpStatus.OK);
	}

	/**
	 * This end point fetch device data on the bases of productId and timeStamp.
	 * 
	 * @param productId
	 * @param tstmp
	 * @return
	 * @throws NumberFormatException
	 * @throws AirplaneModeEnableException
	 * @throws ElementNotFoundException
	 */
	@ApiOperation(value = "Fetch data according to product id", response = TrackDeviceResponseDTO.class, tags = "fetchDeviceData")
	@GetMapping(path = "/event/v1", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TrackDeviceResponseDTO> fetchDeviceData(@RequestParam("ProductId") String productId,
			@RequestParam("tstmp") Optional<String> tstmp) {

		LOGGER.info("fetchDeviceData API calling");
		long timeStamp = ObjectUtils.isEmpty(tstmp) ? Instant.now().toEpochMilli() : Long.parseLong(tstmp.get());
		return new ResponseEntity<>(trackDeviceService.fetchTrackDeviceData(productId, timeStamp), HttpStatus.OK);
	}

}
