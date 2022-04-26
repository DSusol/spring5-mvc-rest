package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {

    public static final String BASE_URL = "/shop/vendors/";

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    public List<VendorDTO> findAllVendors() {
        return vendorRepository.findAll().stream()
                .map(vendorMapper::vendorToVendorDTO)
                .peek(vendorDTO -> vendorDTO.setUrl(BASE_URL+vendorDTO.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public VendorDTO findVendorById(Long id) {
        Vendor vendor = vendorRepository.findById(id).orElse(null);
        if(vendor == null){
            throw new ResourceNotFoundException();
        }
        VendorDTO returnedVendor = vendorMapper.vendorToVendorDTO(vendor);
        returnedVendor.setUrl(BASE_URL + id);
        return returnedVendor;
    }

    @Override
    public VendorDTO createVendor(VendorDTO vendorDTO) {
        return saveVendor(vendorMapper.vendorDTOtoVendor(vendorDTO));
    }

    @Override
    public VendorDTO updateVendor(Long id, VendorDTO vendorDTO) {
        vendorDTO.setId(id);
        return saveVendor(vendorMapper.vendorDTOtoVendor(vendorDTO));
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
        Vendor foundVendor = vendorRepository.findById(id).orElse(null);
        if(foundVendor == null){
            throw new ResourceNotFoundException();
        }
        if(!foundVendor.getName().equals(vendorDTO.getName())) {
            foundVendor.setName(vendorDTO.getName());
        }
        return saveVendor(foundVendor);
    }

    @Override
    public void deleteVendor(Long id) {
        vendorRepository.deleteById(id);
    }

    private VendorDTO saveVendor(Vendor vendor){
        Vendor savedVendor = vendorRepository.save(vendor);
        VendorDTO returnedVendor = vendorMapper.vendorToVendorDTO(savedVendor);
        returnedVendor.setUrl(BASE_URL + vendor.getId());
        return returnedVendor;
    }
}
