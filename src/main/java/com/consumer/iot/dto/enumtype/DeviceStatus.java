package com.consumer.iot.dto.enumtype;

public enum DeviceStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive"),
    NA("N/A");
	
	private final String status;
	DeviceStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
}
