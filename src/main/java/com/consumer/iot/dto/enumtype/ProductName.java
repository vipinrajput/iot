package com.consumer.iot.dto.enumtype;

public enum ProductName {

    CYCLE_PLUS_TRACKER("CyclePlusTracker"),
    GENERAL_TRACKER("GeneralTracker"),
    UNKNOWN_TRACKER("Unknown");
	
	private final String trackName;
	ProductName(String trackName) {
		this.trackName = trackName;
	}
	public String getTrackName() {
		return trackName;
	}
}
