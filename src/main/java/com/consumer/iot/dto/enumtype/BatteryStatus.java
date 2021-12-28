package com.consumer.iot.dto.enumtype;

public enum BatteryStatus {

    FULL("Full"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low"),
    CRITICAL("Critical");

    private final String value;

    BatteryStatus(String value) {
        this.value = value;
    }

	public String getValue() {
		return value;
	}
}
