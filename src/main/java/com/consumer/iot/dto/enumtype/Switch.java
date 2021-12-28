package com.consumer.iot.dto.enumtype;

public enum Switch {

    OFF("OFF"),
    ON("ON"),
    NA("N/A");

    private final String mode;

	Switch(String mode) {
        this.mode = mode;
    }

	public String getMode() {
		return mode;
	}
}
