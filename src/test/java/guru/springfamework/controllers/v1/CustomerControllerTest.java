package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.services.CustomerService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        when(customerService.findAllCustomerDTO()).thenReturn(customerDTOList);

        //then
        mockMvc.perform(get("/api/v1/customers/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerDTOSList", hasSize(2)));

        verify(customerService).findAllCustomerDTO();
    }

    @Test
    public void findCustomerByLastName() throws Exception {
        //given
        CustomerDTO customerDTO = CustomerDTO.builder().firstname("First Name").build();

        //when
        when(customerService.findCustomerDTObyName("InputName")).thenReturn(customerDTO);

        //then
        mockMvc.perform(get("/api/v1/customers/InputName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", is("First Name")));

        verify(customerService).findCustomerDTObyName("InputName");
    }
}