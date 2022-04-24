package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.CategoryDTO;
import guru.springfamework.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CategoryMapperTest {

    CategoryMapper mapperUnderTest;

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = CategoryMapper.INSTANCE;
    }

    @Test
    public void convert() {
        //given
        Category category = Category.builder().id(2L).name("Cat Name").build();

        //when
        CategoryDTO categoryDTO = mapperUnderTest.convert(category);

        //then
        assertEquals(Long.valueOf(2L), categoryDTO.getId());
        assertEquals("Cat Name", categoryDTO.getName());
    }
}