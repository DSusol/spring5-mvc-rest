package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CategoryMapper;
import guru.springfamework.api.v1.model.CategoryDTO;
import guru.springfamework.domain.Category;
import guru.springfamework.repositories.CategoryRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CategoryMapper categoryMapper;

    @InjectMocks
    CategoryServiceImpl serviceUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllCategories() {
        //given
        List<Category> categoryList = new ArrayList<>(List.of(
                new Category(),
                new Category()
        ));

        //when
        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(categoryMapper.convert(any(Category.class))).thenReturn(new CategoryDTO());
        List<CategoryDTO> list = serviceUnderTest.getAllCategories();

        //then
        assertEquals(2, list.size());
        verify(categoryRepository).findAll();
        verify(categoryMapper, times(2)).convert(any(Category.class));
    }

    @Test
    public void getCategoryByName() {
        //given
        CategoryDTO categoryDTO = CategoryDTO.builder().name("DTO Name").build();

        //when
        when(categoryRepository.findByName("Test Name")).thenReturn(new Category());
        when(categoryMapper.convert(any(Category.class))).thenReturn(categoryDTO);
        CategoryDTO resultCategoryDTO = serviceUnderTest.getCategoryByName("Test Name");

        //then
        assertNotNull(resultCategoryDTO);
        assertEquals("DTO Name", resultCategoryDTO.getName());
        verify(categoryRepository).findByName(anyString());
        verify(categoryMapper).convert(any(Category.class));
    }
}