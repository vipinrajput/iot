package com.consumer.iot.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.consumer.iot.constants.Message;
import com.consumer.iot.dto.enumtype.DeviceStatus;
import com.consumer.iot.dto.model.CSVBinderDTO;
import com.consumer.iot.dto.response.TrackDeviceResponseDTO;
import com.consumer.iot.dto.response.TrackDeviceResponseDTO.TrackDeviceResponseDTOBuilder;

@Component
public class TrackDeviceResponseMapper {

	/**
	 * Map CSVBinderDTO DTO data in TrackDeviceResponseDTO DTO.
	 * 
	 * @param csvBinderDTO
	 * @return
	 */
	public TrackDeviceResponseDTO trackDeviceResponseDTOMapper(CSVBinderDTO csvBinderDTO) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(csvBinderDTO.getDateTime()),
				ZoneId.systemDefault());

		TrackDeviceResponseDTOBuilder builder = TrackDeviceResponseDTO.builder().productId(csvBinderDTO.getProductId())
				.datetime(dateTime.format(format)).battery(csvBinderDTO.getBatteryStatus())
				.name(csvBinderDTO.getTrackerName());

		if (csvBinderDTO.isAirplaneMode().isPresent() && !csvBinderDTO.isAirplaneMode().get()) {
			builder.lat(csvBinderDTO.getLatitude().toString()).longitude(csvBinderDTO.getLongitude().toString())
					.status(DeviceStatus.ACTIVE.getStatus()).description(Message.LOCATION_IDENTIFIED_DESCRIPTION);
		} else
			builder.status(DeviceStatus.INACTIVE.getStatus()).description(Message.AIRPLANE_MODE_ENABLED_DESCRIPTION);

		return builder.build();
	}
}
