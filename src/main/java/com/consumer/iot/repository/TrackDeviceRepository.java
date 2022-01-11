package com.consumer.iot.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.consumer.iot.custom.exception.ElementNotFoundException;
import com.consumer.iot.dto.model.CSVBinderDTO;

@Repository
public class TrackDeviceRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrackDeviceRepository.class);
	Map<String, List<CSVBinderDTO>> csvBinderMap;

	/**
	 * Save save in memory
	 * 
	 * @param csvBinderDTOs
	 */
	public void saveData(List<Object> csvBinderDTOs) {
		this.csvBinderMap = conversion(csvBinderDTOs);
		LOGGER.info("Save data successfully.");
	}

	/**
	 * Fetch data according to product id
	 * 
	 * @param productId
	 * @param dateTime
	 * @return
	 */
	public List<CSVBinderDTO> fetchList(String productId, Long dateTime) {
		LOGGER.info("Fetch data on product id base.");
		List<CSVBinderDTO> list = csvBinderMap.get(productId);
		return list;
	}

	/**
	 * Fetch data on TimeStamp base.
	 * 
	 * @param productId
	 * @param dateTime
	 * @return
	 * @throws ElementNotFoundException
	 */
	public CSVBinderDTO fetchDataOnTimeStamp(String productId, Long dateTime) {
		LOGGER.info("TrackDeviceServiceImpl Entry.");
		List<CSVBinderDTO> csvBinderDTOs = csvBinderMap.get(productId);
		if (Objects.isNull(csvBinderDTOs)) {
			throw new ElementNotFoundException();
		}
		CSVBinderDTO csvBinderDTO = csvBinderDTOs.stream()
				.filter(a -> a.getDateTime() <= dateTime || a.getDateTime() >= dateTime)
				.min(Comparator.comparingLong(d -> {
					if (dateTime > d.getDateTime())
						return dateTime - d.getDateTime();
					else
						return d.getDateTime() - dateTime;
				})).get();
		return csvBinderDTO;
	}

	/**
	 * Conversion List to Map.
	 * 
	 * @param cvsBinderDTOs
	 * @return
	 */
	private Map<String, List<CSVBinderDTO>> conversion(List<Object> cvsBinderDTOs) {
		LOGGER.info("Conversion List to Map");
		return cvsBinderDTOs.stream().map(dto -> (CSVBinderDTO) dto)
				.collect(Collectors.groupingBy(CSVBinderDTO::getProductId, Collectors.toList()));
	}

}
