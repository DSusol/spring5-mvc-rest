package guru.springfamework.controllers.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorControllerTest {

    public static final String BASE_URL = "/api/v1/vendors/";

    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController controllerUnderTest;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();
    }

    @Test
    public void findAllVendors() throws Exception {
        //given
        List<VendorDTO> vendorDTOList = new ArrayList<>(List.of(
                new VendorDTO(), new VendorDTO(), new VendorDTO()
        ));

        //when
        when(vendorService.findAllVendors()).thenReturn(vendorDTOList);

        //then
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorDTOList", hasSize(3)));

        verify(vendorService).findAllVendors();
    }

    @Test
    public void findVendorById() throws Exception {
        //given
        VendorDTO foundVendorDTO = VendorDTO.builder().name("Test Name").build();

        //when
        when(vendorService.findVendorById(2L)).thenReturn(foundVendorDTO);

        //then
        mockMvc.perform(get(BASE_URL + "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Name")));

        verify(vendorService).findVendorById(2L);
    }

    @Test
    public void createVendor() throws Exception {
        //given
        VendorDTO vendorDTO = new VendorDTO();
        VendorDTO createdVendorDTO = VendorDTO.builder().id(5L).name("TestName").url("url").build();

        //when
        when(vendorService.createVendor(vendorDTO)).thenReturn(createdVendorDTO);

        //then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJsonString(vendorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.name", is("TestName")))
                .andExpect(jsonPath("$.url", is("url")));

        verify(vendorService).createVendor(vendorDTO);
    }

    @Test
    public void updateVendor() throws Exception {
        //given
        VendorDTO vendorDTO = new VendorDTO();
        VendorDTO updatedVendorDTO = VendorDTO.builder().id(2L).name("TestName").url("url").build();

        //when
        when(vendorService.updateVendor(2L, vendorDTO)).thenReturn(updatedVendorDTO);

        //then
        mockMvc.perform(put(BASE_URL + "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJsonString(vendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("TestName")))
                .andExpect(jsonPath("$.url", is("url")));

        verify(vendorService).updateVendor(anyLong(), any(VendorDTO.class));
    }

    @Test
    public void patchVendor() throws Exception {
        //given
        VendorDTO vendorDTO = new VendorDTO();
        VendorDTO patchedVendorDTO = VendorDTO.builder().id(5L).name("TestName").url("url").build();

        //when
        when(vendorService.patchVendor(2L, vendorDTO)).thenReturn(patchedVendorDTO);

        //then
        mockMvc.perform(patch(BASE_URL + "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJsonString(vendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.name", is("TestName")))
                .andExpect(jsonPath("$.url", is("url")));

        verify(vendorService).patchVendor(anyLong(), any(VendorDTO.class));
    }

    @Test
    public void deleteVendor() throws Exception {
        mockMvc.perform(delete(BASE_URL + "2"))
                .andExpect(status().isOk());
        verify(vendorService).deleteVendor(2L);
    }

    private String convertToJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting to json object");
        }
    }
}