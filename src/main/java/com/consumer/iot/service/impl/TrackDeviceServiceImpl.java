package com.consumer.iot.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.consumer.iot.constants.Message;
import com.consumer.iot.custom.exception.AirplaneModeEnableException;
import com.consumer.iot.dto.enumtype.DeviceStatus;
import com.consumer.iot.dto.enumtype.ProductName;
import com.consumer.iot.dto.model.CSVBinderDTO;
import com.consumer.iot.dto.request.TrackDeviceRequestDTO;
import com.consumer.iot.dto.response.TrackDeviceResponseDTO;
import com.consumer.iot.mapper.TrackDeviceResponseMapper;
import com.consumer.iot.repository.TrackDeviceRepository;
import com.consumer.iot.service.TrackDeviceService;
import com.opencsv.bean.CsvToBeanBuilder;

/**
 * This class responsible for track device data save(refresh) and fetch.
 */
@Service
public class TrackDeviceServiceImpl implements TrackDeviceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrackDeviceServiceImpl.class);

	@Autowired
	TrackDeviceRepository trackDeviceRepository;
	@Autowired
	TrackDeviceResponseMapper mapper;

	/**
	 * This method use to load refresh CSV data in memory.
	 * 
	 * @param deviceRequestDTO
	 * @return
	 * @throws FileNotFoundException
	 */
	@Override
	public TrackDeviceResponseDTO loadTrackDeviceData(TrackDeviceRequestDTO deviceRequestDTO)
			throws FileNotFoundException {
		LOGGER.info("File Path " + deviceRequestDTO.getFilePath());
		File file = ResourceUtils.getFile("classpath:"+deviceRequestDTO.getFilePath());
		List<Object> cvsBinderDTO = new CsvToBeanBuilder<>(new FileReader(new File(file.getPath())))
				.withSkipLines(1).withSeparator(',').withType(CSVBinderDTO.class).build().parse();
		trackDeviceRepository.saveData(cvsBinderDTO);
		return TrackDeviceResponseDTO.builder().description(Message.SUCCESS_DESCRIPTION).build();
	}

	/**
	 * This method use to fetch data on base of productId and timeStamp. If
	 * timeStamp is empty use current UTC time.
	 * 
	 * @param productId
	 * @param tstmp
	 * @return
	 */
	@Override
	public TrackDeviceResponseDTO fetchTrackDeviceData(String productId, long tstmp) {

		LOGGER.info("TrackDeviceServiceImpl Entry");

		CSVBinderDTO csvBinderDTO = trackDeviceRepository.fetchDataOnTimeStamp(productId, tstmp);

		// Airplane mode Validation.
		validation(csvBinderDTO);

		TrackDeviceResponseDTO deviceResponseDTO = mapper.trackDeviceResponseDTOMapper(csvBinderDTO);
		// Need to reset status due to Dynamic activity-tracking requirement.
		String status = null;
		if (csvBinderDTO.getTrackerName().equals(ProductName.CYCLE_PLUS_TRACKER.getTrackName())) {
			status = getCyclePlusTrackerStatus(trackDeviceRepository.fetchList(productId, tstmp));
			deviceResponseDTO.setStatus(status);
		}

		return deviceResponseDTO;
	}

	/**
	 * Validation on GPSDataAvailability to check.
	 * 
	 * @param csvBinderDTO
	 * @throws AirplaneModeEnableException
	 */
	private void validation(CSVBinderDTO csvBinderDTO) {
		if (csvBinderDTO.isAirplaneMode().isPresent() && !csvBinderDTO.isAirplaneMode().get()
				&& !isGpsDataAvailable(csvBinderDTO)) {
			LOGGER.error("Airplane mode disable and gps data is not available.");
			throw new AirplaneModeEnableException();
		}
	}

	/**
	 * Checking Device active or not.
	 * 
	 * @param csvBinderDTO
	 * @return
	 */
	private boolean isGpsDataAvailable(CSVBinderDTO csvBinderDTO) {
		return Objects.nonNull(csvBinderDTO.getLatitude()) && Objects.nonNull(csvBinderDTO.getLongitude());
	}

	/**
	 * Get cycle track status.
	 * 
	 * @param map
	 * @return
	 */
	private String getCyclePlusTrackerStatus(List<CSVBinderDTO> list) {
		LOGGER.info("Override status of cycleplustracker.");
		List<CSVBinderDTO> sortedList = list.stream()
				.sorted(Comparator.comparingLong(dto -> ((CSVBinderDTO) dto).getDateTime()).reversed()).limit(3)
				.collect(Collectors.toList());

		int size = sortedList.size();
		boolean isGPSDataAvailable = sortedList.stream().allMatch(dto -> isGpsDataAvailable((CSVBinderDTO) dto));

		if (size == 3 && isGPSDataAvailable) {
			BigDecimal latitude = list.stream().findFirst().get().getLatitude();
			BigDecimal longtitude = list.stream().findFirst().get().getLongitude();
			int count = 0;
			for (CSVBinderDTO dto : sortedList) {
				BigDecimal latitude1 = dto.getLatitude();
				BigDecimal longtitude1 = dto.getLongitude();
				if (Objects.equals(latitude, latitude1) && Objects.equals(longtitude, longtitude1)) {
					count++;
				}
			}
			// If all location status same.
			if (count == sortedList.size())
				return DeviceStatus.INACTIVE.getStatus();
			else
				return DeviceStatus.ACTIVE.getStatus();
		}
		return DeviceStatus.NA.getStatus();
	}

}
