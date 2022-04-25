package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.bootstrap.Bootstrap;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerServiceImplITest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    CustomerService serviceUnderTest;

    @Before
    public void setUp() throws Exception {
        System.out.println(">>>>> Loading Customer Data");
        System.out.println(customerRepository.findAll().size());

        Bootstrap bootstrap = new Bootstrap(categoryRepository, customerRepository);
        bootstrap.run();

        serviceUnderTest = new CustomerServiceImpl(customerRepository, CustomerMapper.INSTANCE);

    }

    @Test
    public void patchCustomerFirstName() {

        //given
        Long customerId = customerRepository.findAll().get(0).getId();
        Customer originalCustomer = customerRepository.findById(customerId).orElse(null);
        CustomerDTO revisedCustomerDTO = CustomerDTO.builder().firstname("TestFirstName").build();

        //when
        CustomerDTO updatedCustomerDTO = serviceUnderTest.patchCustomer(customerId, revisedCustomerDTO);
        Customer updatedCustomer = customerRepository.findById(customerId).orElse(null);

        //then
        assertEquals(customerId, updatedCustomerDTO.getId());
        assertEquals("TestFirstName", updatedCustomerDTO.getFirstname());

        assertNotNull(originalCustomer);
        assertNotNull(updatedCustomer);
        assertEquals("TestFirstName", updatedCustomer.getFirstname());
        assertEquals(originalCustomer.getId(), updatedCustomer.getId());
        assertEquals(originalCustomer.getLastname(), updatedCustomer.getLastname());
        assertEquals(originalCustomer.getUrl(), updatedCustomer.getUrl());
    }

    @Test
    public void patchCustomerLastName() {

        //given
        Long customerId = customerRepository.findAll().get(0).getId();
        Customer originalCustomer = customerRepository.findById(customerId).orElse(null);
        CustomerDTO revisedCustomerDTO = CustomerDTO.builder().lastname("TestLastName").build();

        //when
        CustomerDTO updatedCustomerDTO = serviceUnderTest.patchCustomer(customerId, revisedCustomerDTO);
        Customer updatedCustomer = customerRepository.findById(customerId).orElse(null);

        //then
        assertEquals(customerId, updatedCustomerDTO.getId());
        assertEquals("TestLastName", updatedCustomerDTO.getLastname());

        assertNotNull(originalCustomer);
        assertNotNull(updatedCustomer);
        assertEquals("TestLastName", updatedCustomer.getLastname());
        assertEquals(originalCustomer.getId(), updatedCustomer.getId());
        assertEquals(originalCustomer.getFirstname(), updatedCustomer.getFirstname());
        assertEquals(originalCustomer.getUrl(), updatedCustomer.getUrl());
    }
}
