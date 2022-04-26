package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.domain.Vendor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VendorMapperTest {

    VendorMapper mapperUnderTest;

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = new VendorMapperImpl();
    }

    @Test
    public void vendorToVendorDTO() {
        //given
        Vendor vendor = Vendor.builder().id(2L).name("Vendor Name").build();

        //when
        VendorDTO vendorDTO = mapperUnderTest.vendorToVendorDTO(vendor);

        //then
        assertEquals(Long.valueOf(2L), vendorDTO.getId());
        assertEquals("Vendor Name", vendorDTO.getName());
    }

    @Test
    public void vendorDTOtoVendor() {
        //given
        VendorDTO vendorDTO = VendorDTO.builder().id(2L).name("Vendor Name").build();

        //when
        Vendor vendor = mapperUnderTest.vendorDTOtoVendor(vendorDTO);

        //then
        assertEquals(Long.valueOf(2L), vendor.getId());
        assertEquals("Vendor Name", vendor.getName());
    }
}