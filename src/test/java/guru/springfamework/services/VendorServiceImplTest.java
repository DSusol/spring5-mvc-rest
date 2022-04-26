package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VendorServiceImplTest {

    public static final String BASE_URL = "/shop/vendors/";

    @Mock
    VendorRepository vendorRepository;

    @Mock
    VendorMapper vendorMapper;

    @InjectMocks
    VendorServiceImpl serviceUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllVendors() {
        //given
        List<Vendor> vendorList = new ArrayList<>(List.of(
                new Vendor(),
                new Vendor()
        ));

        //when
        when(vendorRepository.findAll()).thenReturn(vendorList);
        when(vendorMapper.vendorToVendorDTO(any(Vendor.class))).thenReturn(new VendorDTO());
        List<VendorDTO> vendorDTOList = serviceUnderTest.findAllVendors();

        //then
        assertEquals(2, vendorDTOList.size());
        verify(vendorRepository).findAll();
        verify(vendorMapper, times(2)).vendorToVendorDTO(any(Vendor.class));
    }

    @Test
    public void findVendorById() {
        //given
        Vendor vendor = new Vendor();
        VendorDTO convertedVendor = new VendorDTO();

        //when
        when(vendorRepository.findById(2L)).thenReturn(Optional.of(vendor));
        when(vendorMapper.vendorToVendorDTO(vendor)).thenReturn(convertedVendor);
        VendorDTO vendorDTO = serviceUnderTest.findVendorById(2L);

        //then
        assertNotNull(vendorDTO);
        assertEquals(BASE_URL + "2", vendorDTO.getUrl());
        assertEquals(convertedVendor, vendorDTO);
        verify(vendorRepository).findById(2L);
        verify(vendorMapper).vendorToVendorDTO(vendor);
    }

    @Test
    public void createVendor() {
        //given
        VendorDTO vendorDTOtoSave = new VendorDTO();
        Vendor vendorToSave = new Vendor();
        Vendor savedVendor = new Vendor();
        VendorDTO vendorDTOtoReturn = new VendorDTO();

        //when
        when(vendorMapper.vendorDTOtoVendor(vendorDTOtoSave)).thenReturn(vendorToSave);
        when(vendorRepository.save(vendorToSave)).thenReturn(savedVendor);
        when(vendorMapper.vendorToVendorDTO(savedVendor)).thenReturn(vendorDTOtoReturn);
        VendorDTO savedVendorDTO = serviceUnderTest.createVendor(vendorDTOtoSave);

        //then
        assertNotNull(savedVendorDTO);
        assertNotNull(savedVendorDTO.getUrl());
        assertEquals(vendorDTOtoReturn, savedVendorDTO);
        assertTrue(savedVendorDTO.getUrl().contains(BASE_URL));
        verify(vendorMapper).vendorDTOtoVendor(vendorDTOtoSave);
        verify(vendorRepository).save(vendorToSave);
        verify(vendorMapper).vendorToVendorDTO(savedVendor);
    }

    @Test
    public void updateVendor() {
        //given
        VendorDTO vendorDTOtoUpdate = new VendorDTO();
        Vendor vendorToSave = Vendor.builder().id(2L).build();
        Vendor savedVendor = new Vendor();
        VendorDTO vendorDTOtoReturn = new VendorDTO();

        //when
        when(vendorMapper.vendorDTOtoVendor(vendorDTOtoUpdate)).thenReturn(vendorToSave);
        when(vendorRepository.save(vendorToSave)).thenReturn(savedVendor);
        when(vendorMapper.vendorToVendorDTO(savedVendor)).thenReturn(vendorDTOtoReturn);
        VendorDTO updatedVendorDTO = serviceUnderTest.updateVendor(2L, vendorDTOtoUpdate);

        //then
        assertNotNull(updatedVendorDTO);
        assertEquals(BASE_URL + "2", updatedVendorDTO.getUrl());
        assertEquals(vendorDTOtoReturn, updatedVendorDTO);
        verify(vendorMapper).vendorDTOtoVendor(vendorDTOtoUpdate);
        verify(vendorRepository).save(vendorToSave);
        verify(vendorMapper).vendorToVendorDTO(savedVendor);
    }

    @Test
    public void patchVendor() {
        //given
        VendorDTO vendorDTOtoPatch = VendorDTO.builder().name("UpdatedName").build();
        Vendor foundByIdVendor = Vendor.builder().id(2L).name("BaseName").build();
        Vendor savedVendor = new Vendor();
        VendorDTO vendorDTOtoReturn = new VendorDTO();

        //when
        when(vendorRepository.findById(2L)).thenReturn(Optional.of(foundByIdVendor));
        when(vendorRepository.save(foundByIdVendor)).thenReturn(savedVendor);
        when(vendorMapper.vendorToVendorDTO(savedVendor)).thenReturn(vendorDTOtoReturn);
        VendorDTO patchedVendorDTO = serviceUnderTest.patchVendor(2L, vendorDTOtoPatch);

        //then
        assertNotNull(patchedVendorDTO);
        assertEquals(BASE_URL + "2", patchedVendorDTO.getUrl());
        assertEquals("UpdatedName", foundByIdVendor.getName());
        assertEquals(vendorDTOtoReturn, patchedVendorDTO);
        verify(vendorRepository).findById(2L);
        verify(vendorRepository).save(foundByIdVendor);
    }

    @Test
    public void deleteVendor() {
        serviceUnderTest.deleteVendor(2L);
        verify(vendorRepository).deleteById(2L);
    }
}