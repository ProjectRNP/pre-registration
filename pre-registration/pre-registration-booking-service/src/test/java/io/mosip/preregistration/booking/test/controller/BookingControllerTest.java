package io.mosip.preregistration.booking.test.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import io.mosip.preregistration.booking.code.StatusCodes;
import io.mosip.preregistration.booking.controller.BookingController;
import io.mosip.preregistration.booking.dto.AvailabilityDto;
import io.mosip.preregistration.booking.dto.BookingDTO;
import io.mosip.preregistration.booking.dto.BookingRequestDTO;
import io.mosip.preregistration.booking.dto.ResponseDto;
import io.mosip.preregistration.booking.service.BookingService;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

@RunWith(SpringRunner.class)
@WebMvcTest(BookingController.class)
public class BookingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookingService service;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private AvailabilityDto availabilityDto;

	BookingDTO bookingDTO = new BookingDTO();
	BookingRequestDTO bookingRequestDTO = new BookingRequestDTO();
	Timestamp resTime = new Timestamp(System.currentTimeMillis());
	@SuppressWarnings("rawtypes")
	ResponseDto responseDto = new ResponseDto();
	private Object jsonObject = null;

	@Before
	public void setup() throws FileNotFoundException, ParseException, URISyntaxException {
		availabilityDto = new AvailabilityDto();
		ClassLoader classLoader = getClass().getClassLoader();
		JSONParser parser = new JSONParser();

		URI dataSyncUri = new URI(
				classLoader.getResource("booking.json").getFile().trim().replaceAll("\\u0020", "%20"));
		File file = new File(dataSyncUri.getPath());
		jsonObject = parser.parse(new FileReader(file));

		bookingRequestDTO.setPre_registration_id("12345");
		bookingRequestDTO.setRegistration_center_id("2");
		bookingRequestDTO.setSlotFromTime("09:00");
		bookingRequestDTO.setSlotToTime("09:20");
		bookingRequestDTO.setReg_date(resTime);

		bookingDTO.setRequest(bookingRequestDTO);
	}

	@Test
	public void getAvailability() throws Exception {
		ResponseDto<AvailabilityDto> response = new ResponseDto<>();
		Mockito.when(service.getAvailability(Mockito.any())).thenReturn(response);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v0.1/pre-registration/book/availability")
				.contentType(MediaType.APPLICATION_JSON_VALUE).characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).param("RegCenterId", "1");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	public void saveAvailability() throws Exception {
		ResponseDto<String> response = new ResponseDto<>();
		Mockito.when(service.addAvailability()).thenReturn(response);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v0.1/pre-registration/book/masterSync")
				.contentType(MediaType.APPLICATION_JSON_VALUE).characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void successBookingTest() throws Exception {

		responseDto.setErr(null);
		responseDto.setStatus(true);
		responseDto.setResTime(new Timestamp(System.currentTimeMillis()));
		List<String> respList = new ArrayList<>();
		respList.add(StatusCodes.APPOINTMENT_SUCCESSFULLY_BOOKED.toString());
		responseDto.setResponse(respList);

		Mockito.when(service.bookAppointment(bookingDTO)).thenReturn(responseDto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v0.1/pre-registration/booking/book")
				.contentType(MediaType.APPLICATION_JSON_VALUE).characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).content(jsonObject.toString());

		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

}
