package com.consumer.iot.dto.enumtype;

public enum BatteryPercentage {
	
    NINETYEIGHT(98),
    SIXTY(60),
    FOURTY(40),
    TEN(10);

    private final int value;

    BatteryPercentage(int value) {
        this.value = value;
    }

	public int getValue() {
		return value;
	}

}
