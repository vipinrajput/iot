package com.consumer.iot.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "datetime", "longitude", "lat", "status", "battery", "description" })
public class TrackDeviceResponseDTO {
	@JsonProperty("id")
	private String productId;
	private String name;
	private String status;
	private String datetime;
	private String battery;
	private String description;
	@JsonProperty("long")
	private String longitude;
	private String lat;
}
