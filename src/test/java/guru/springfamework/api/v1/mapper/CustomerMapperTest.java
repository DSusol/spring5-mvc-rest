package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerMapperTest {

    CustomerMapper mapperUnderTest;

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = CustomerMapper.INSTANCE;
    }

    @Test
    public void convert() {
        //given
        Customer customer = Customer.builder()
                .id(2L)
                .firstname("First Name")
                .lastname("Last Name")
                .url("test url")
                .build();

        //when
        CustomerDTO customerDTO = mapperUnderTest.convert(customer);

        //then
        assertEquals(Long.valueOf(2L), customerDTO.getId());
        assertEquals("First Name", customerDTO.getFirstname());
        assertEquals("Last Name", customerDTO.getLastname());
        assertEquals("test url", customerDTO.getUrl());
    }
}