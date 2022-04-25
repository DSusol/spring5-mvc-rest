package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    public static final String BASE_URL = "/shop/customers/";
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<CustomerDTO> findAllCustomer() {
        return customerRepository.findAll().stream()
                .map(customerMapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO findCustomerByName(String name) {
        return customerMapper.convert(customerRepository.findByLastname(name));
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        Customer customerToSave = customerMapper.reverseConvert(customerDTO);
        Customer savedCustomer = customerRepository.save(customerToSave);
        savedCustomer.setUrl(BASE_URL + savedCustomer.getId());
        savedCustomer = customerRepository.save(savedCustomer);
        return customerMapper.convert(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customerToSave = customerMapper.reverseConvert(customerDTO);
        customerToSave.setId(id);
        customerToSave.setUrl(BASE_URL + id);
        Customer savedCustomer = customerRepository.save(customerToSave);
        return customerMapper.convert(savedCustomer);
    }

    @Override
    public CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO) {
        Customer foundCustomer = customerRepository.findById(id).orElse(null);
        if(foundCustomer == null) {
            throw new ResourceNotFoundException("Resource not found at id = " + id);
        }

        if(customerDTO.getFirstname() != null) {
            foundCustomer.setFirstname(customerDTO.getFirstname());
        }
        if(customerDTO.getLastname() != null) {
            foundCustomer.setLastname(customerDTO.getLastname());
        }

        return customerMapper.convert(customerRepository.save(foundCustomer));
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }


}
