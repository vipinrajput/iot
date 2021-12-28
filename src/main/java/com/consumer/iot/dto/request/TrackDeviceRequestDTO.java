package com.consumer.iot.dto.request;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackDeviceRequestDTO {
	
	@NotEmpty(message = "File path is required")
	private String filePath;
}
