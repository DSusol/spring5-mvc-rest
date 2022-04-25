package guru.springfamework.bootstrap;

import guru.springfamework.domain.Category;
import guru.springfamework.domain.Customer;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import guru.springfamework.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository,
                     CustomerRepository customerRepository,
                     VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
        nuts.setName("Nuts");

        categoryRepository.saveAll(List.of(fruits, dried, fresh, exotic, nuts));
        log.debug(">>> Loaded " + categoryRepository.findAll().size() + " categories.");

        Customer joe = Customer.builder()
                .firstname("Joe")
                .lastname("Newman")
                .url("/shop/customers/1")
                .build();

        Customer michael = Customer.builder()
                .firstname("Michael")
                .lastname("Lachappele")
                .url("/shop/customers/2")
                .build();

        Customer david = Customer.builder()
                .firstname("David")
                .lastname("Winter")
                .url("/shop/customers/3")
                .build();

        customerRepository.saveAll(List.of(joe, michael, david));
        log.debug(">>> Loaded " + customerRepository.findAll().size() + " customers.");

        Vendor fruitsLtd = Vendor.builder().name("Fruits supplier ltd").build();
        Vendor nutsLtd = Vendor.builder().name("Nuts supplier ltd").build();
        Vendor exoticLtd = Vendor.builder().name("Exotic supplier ltd").build();
        Vendor staffLtd = Vendor.builder().name("Staff supplier ltd").build();
        Vendor toysLtd = Vendor.builder().name("Toys supplier ltd").build();

        vendorRepository.saveAll(List.of(fruitsLtd, nutsLtd, exoticLtd, staffLtd, toysLtd));
        log.debug(">>> Loaded " + vendorRepository.findAll().size() + " vendors.");
    }
}
