package com.consumer.iot.service;

import java.io.FileNotFoundException;

import com.consumer.iot.dto.request.TrackDeviceRequestDTO;
import com.consumer.iot.dto.response.TrackDeviceResponseDTO;

public interface TrackDeviceService {

	TrackDeviceResponseDTO loadTrackDeviceData(TrackDeviceRequestDTO deviceRequestDTO) throws FileNotFoundException;

	TrackDeviceResponseDTO fetchTrackDeviceData(String productId, long tstmp);
}
