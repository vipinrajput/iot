package com.consumer.iot.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.consumer.iot.constants.Message;
import com.consumer.iot.custom.exception.AirplaneModeEnableException;
import com.consumer.iot.custom.exception.ElementNotFoundException;
import com.consumer.iot.dto.response.TrackDeviceResponseDTO;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(AirplaneModeEnableException.class)
	public ResponseEntity<TrackDeviceResponseDTO> airplaneEnable(AirplaneModeEnableException exception) {
		return new ResponseEntity<>(buildResponseDTO(Message.DEVICE_NOT_FOUND_DESCRIPTION, exception),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<TrackDeviceResponseDTO> technicalException(Exception exception) {
		return new ResponseEntity<>(buildResponseDTO(Message.TECHNICAL_ERROR_DESCRIPTION, exception),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ElementNotFoundException.class)
	public ResponseEntity<TrackDeviceResponseDTO> handleNotFoundException(ElementNotFoundException exception) {
		return new ResponseEntity<>(buildResponseDTO(Message.PRODUCTID_NOT_FOUND_DESCRIPTION, exception),
				HttpStatus.NOT_FOUND);
	}

	private TrackDeviceResponseDTO buildResponseDTO(String message, Exception exception) {
		LOGGER.error("Exception ", exception.getMessage());
		return TrackDeviceResponseDTO.builder().description(message).build();
	}
}
