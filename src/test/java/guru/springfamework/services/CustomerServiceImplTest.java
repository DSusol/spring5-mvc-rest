package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CustomerMapper customerMapper;

    @InjectMocks
    CustomerServiceImpl serviceUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllCustomerDTO() {
        //given
        List<Customer> customers = new ArrayList<>(List.of(
                new Customer(),
                new Customer()
        ));

        //when
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.convert(any(Customer.class))).thenReturn(new CustomerDTO());
        List<CustomerDTO> customerDTOS = serviceUnderTest.findAllCustomer();

        //then
        assertEquals(2, customerDTOS.size());
        verify(customerRepository).findAll();
        verify(customerMapper, times(2)).convert(any(Customer.class));
    }

    @Test
    public void findCustomerDTObyName() {
        //given
        Customer customer = new Customer();
        CustomerDTO customerDTO = CustomerDTO.builder().firstname("Test Name").build();

        //when
        when(customerRepository.findByLastname("name")).thenReturn(new Customer());
        when(customerMapper.convert(customer)).thenReturn(customerDTO);
        CustomerDTO resultCustomer = serviceUnderTest.findCustomerByName("name");

        //then
        assertNotNull(resultCustomer);
        assertEquals("Test Name", resultCustomer.getFirstname());
        verify(customerRepository).findByLastname(anyString());
        verify(customerMapper).convert(any(Customer.class));
    }

    @Test
    public void createNewCustomer() {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        Customer revertedCustomer = new Customer();
        Customer savedCustomer = Customer.builder().id(2L).build();
        CustomerDTO returnedCustomerDTO = new CustomerDTO();

        //when
        when(customerMapper.reverseConvert(customerDTO)).thenReturn(revertedCustomer);
        when(customerRepository.save(revertedCustomer)).thenReturn(savedCustomer);
        when(customerRepository.save(savedCustomer)).thenReturn(savedCustomer);
        when(customerMapper.convert(savedCustomer)).thenReturn(returnedCustomerDTO);

        CustomerDTO resultCustomer = serviceUnderTest.createNewCustomer(customerDTO);

        //then
        assertNotNull(resultCustomer);
        assertEquals("/shop/customers/2", savedCustomer.getUrl());
        assertEquals(returnedCustomerDTO, resultCustomer);
        verify(customerMapper).reverseConvert(customerDTO);
        verify(customerRepository).save(revertedCustomer);
        verify(customerRepository).save(savedCustomer);
        verify(customerMapper).convert(savedCustomer);
    }

    @Test
    public void updateCustomer() {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        Customer revertedCustomer = new Customer();
        Customer savedCustomer = new Customer();
        CustomerDTO returnedCustomerDTO = new CustomerDTO();

        //when
        when(customerMapper.reverseConvert(customerDTO)).thenReturn(revertedCustomer);
        when(customerRepository.save(revertedCustomer)).thenReturn(savedCustomer);
        when(customerMapper.convert(savedCustomer)).thenReturn(returnedCustomerDTO);
        CustomerDTO resultCustomer = serviceUnderTest.updateCustomer(2L, customerDTO);

        //then
        assertNotNull(resultCustomer);
        assertEquals(returnedCustomerDTO, resultCustomer);
        assertEquals(Long.valueOf(2L), revertedCustomer.getId());
        assertEquals("/shop/customers/2", revertedCustomer.getUrl());
        verify(customerMapper).reverseConvert(customerDTO);
        verify(customerRepository).save(revertedCustomer);
        verify(customerMapper).convert(savedCustomer);
    }

    @Test
    public void deleteCustomerById() {
        serviceUnderTest.deleteCustomerById(2L);
        verify(customerRepository).deleteById(2L);
    }
}