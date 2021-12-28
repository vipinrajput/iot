package com.consumer.iot.dto.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import com.consumer.iot.dto.enumtype.BatteryPercentage;
import com.consumer.iot.dto.enumtype.BatteryStatus;
import com.consumer.iot.dto.enumtype.ProductName;
import com.consumer.iot.dto.enumtype.Switch;
import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class CSVBinderDTO {

	private final String cycleTrackerStartWith = "WG";
	private final String generalTrackerStartWith = "69";

	@CsvBindByName(column = "DateTime", required = true)
	private Long dateTime;

	@CsvBindByName(column = "EventId", required = true)
	private Long eventId;

	@CsvBindByName(column = "ProductId", required = true)
	private String productId;

	@CsvBindByName(column = "Latitude")
	private BigDecimal latitude;

	@CsvBindByName(column = "Longitude")
	private BigDecimal longitude;

	@CsvBindByName(column = "Battery", required = true)
	private double battery;

	@CsvBindByName(column = "Light", required = true)
	private String light;

	@CsvBindByName(column = "AirplaneMode", required = true)
	private String airplaneMode;

	public Optional<Boolean> isAirplaneMode() {
		if (Objects.nonNull(airplaneMode) && Switch.ON.getMode().equals(airplaneMode)) {
			return Optional.of(Boolean.TRUE);
		} else if (Objects.nonNull(airplaneMode) && Switch.OFF.getMode().equals(airplaneMode))
			return Optional.of(Boolean.FALSE);
		return Optional.empty();
	}

	/**
	 * Get battery status.
	 * 
	 * @return
	 */
	public String getBatteryStatus() {
		int status = (int) (battery * 100);
		if (status <= BatteryPercentage.NINETYEIGHT.getValue()) {
			return BatteryStatus.FULL.getValue();
		} else if (status <= BatteryPercentage.SIXTY.getValue()) {
			return BatteryStatus.HIGH.getValue();
		} else if (status <= BatteryPercentage.FOURTY.getValue()) {
			return BatteryStatus.MEDIUM.getValue();
		} else if (status <= BatteryPercentage.TEN.getValue()) {
			return BatteryStatus.LOW.getValue();
		} else
			return BatteryStatus.CRITICAL.getValue();
	}

	/**
	 * Get track name
	 * 
	 * @return
	 */
	public String getTrackerName() {
		if (productId.startsWith(cycleTrackerStartWith)) {
			return ProductName.CYCLE_PLUS_TRACKER.getTrackName();
		} else if (productId.startsWith(generalTrackerStartWith))
			return ProductName.GENERAL_TRACKER.getTrackName();
		return ProductName.UNKNOWN_TRACKER.getTrackName();
	}
}
