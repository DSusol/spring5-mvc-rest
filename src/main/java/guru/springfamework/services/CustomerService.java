package guru.springfamework.services;

import guru.springfamework.api.v1.model.CustomerDTO;

import java.util.List;

public interface CustomerService {

    List<CustomerDTO> findAllCustomer();

    CustomerDTO findCustomerByName(String name);

    CustomerDTO createNewCustomer(CustomerDTO customerDTO);
}
