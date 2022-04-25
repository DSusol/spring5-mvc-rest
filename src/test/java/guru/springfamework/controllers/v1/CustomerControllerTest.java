package guru.springfamework.controllers.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController controllerUnderTest;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();
    }

    @Test
    public void findAllCustomers() throws Exception {
        //given
        List<CustomerDTO> customerDTOList = new ArrayList<>(List.of(
                new CustomerDTO(),
                new CustomerDTO()
        ));

        //when
        when(customerService.findAllCustomer()).thenReturn(customerDTOList);

        //then
        mockMvc.perform(get("/api/v1/customers/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerDTOSList", hasSize(2)));

        verify(customerService).findAllCustomer();
    }

    @Test
    public void findCustomerByLastName() throws Exception {
        //given
        CustomerDTO customerDTO = CustomerDTO.builder().firstname("First Name").build();

        //when
        when(customerService.findCustomerByName("InputName")).thenReturn(customerDTO);

        //then
        mockMvc.perform(get("/api/v1/customers/InputName")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", is("First Name")));

        verify(customerService).findCustomerByName("InputName");
    }

    @Test
    public void createNewCustomer() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        CustomerDTO returnedCustomerDTO = CustomerDTO.builder()
                .id(2L)
                .firstname("Test First Name")
                .url("customer url")
                .build();

        //when
        when(customerService.createNewCustomer(customerDTO)).thenReturn(returnedCustomerDTO);

        //then
        mockMvc.perform(post("/api/v1/customers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJsonString(customerDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstname", is("Test First Name")))
                .andExpect(jsonPath("$.url", is("customer url")));

        verify(customerService).createNewCustomer(any(CustomerDTO.class));
    }

    private String convertToJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error converting to json object");
        }
    }
}