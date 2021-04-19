package com.trade_accounting.services.impl;

import com.trade_accounting.models.Product;
import com.trade_accounting.models.dto.ImageDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.repositories.ImageRepository;
import com.trade_accounting.repositories.ProductRepository;
import com.trade_accounting.services.impl.Stubs.ModelStubs;
import com.trade_accounting.services.impl.Stubs.SpecificationStubs;
import com.trade_accounting.utils.DtoMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository repository;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Spy
    private DtoMapperImpl dtoMapper;


    @Test
    void getById() {
        ProductDto productDto = ModelStubs.getProductDto(1L);
        productDto.setImageDtos(new ArrayList<>());

        Product product = ModelStubs.getProduct(1L);
        product.setImages(new ArrayList<>());

        when(repository.getOne(1L)).thenReturn(product);

        ProductDto fact = productService.getById(1L);

        verify(dtoMapper).productToProductDto(product);
        verify(dtoMapper).toImageDto(product.getImages());
        assertEquals(productDto, fact);
    }

    @Test
    void whenGetAllByProductGroupIdThenReturnListOfProducts() {
        when(repository.findAll())
                .thenReturn(
                        Stream.of(
                                ModelStubs.getProduct(1L),
                                ModelStubs.getProduct(2L),
                                ModelStubs.getProduct(3L)
                        ).collect(Collectors.toList())
                );
        List<ProductDto> productDtos = productService.getAll();

        assertNotNull(productDtos, "failure - expected that a list of productDtos not null");
        assertEquals(productDtos.size(), 3, "failure - expected that a size of list of productDtos equals 3");

    }

    @Test
    void whenGetAllThenShouldReturnEmptyListProductDto() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        List<ProductDto> employees = productService.getAll();

        assertNotNull(employees, "failure - expected that a list of ProductDto not null");
        assertEquals(0, employees.size(), "failure - expected that size of list of ProductDto equals 0");
    }

    @Test
    void whenCreateProduct() {
        List<ImageDto> imageDtoList = Stream.of(
                ModelStubs.getImageDto(1L),
                ModelStubs.getImageDto(2L),
                ModelStubs.getImageDto(3L)
        ).collect(Collectors.toList());
        ProductDto productDto = ModelStubs.getProductDto(1L);
        productDto.setImageDtos(imageDtoList);
        productService.create(productDto);
        verify(dtoMapper).toImage(any(List.class), anyString());
        verify(imageRepository).saveAll(any(List.class));
        verify(dtoMapper).productDtoToProduct(productDto);
        verify(repository).saveAndFlush(any(Product.class));
    }

    @Test
    void deleteById() {
        productService.deleteById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void getAllByContractorId() {
        List<Product> productList = Stream.of(ModelStubs.getProduct(1L)).collect(Collectors.toList());
        when(repository.getAllByContractorId(1L)).thenReturn(productList);

        List<ProductDto> allByContractorId = productService.getAllByContractorId(1L);

        verify(repository).getAllByContractorId(1L);
        verify(dtoMapper).toProductDto(any(List.class));
        assertEquals(dtoMapper.toProductDto(productList), allByContractorId);
    }

    @Test
    void search() {
        String searchValue = "value";
        List<Product> stubProductList = Stream.of(ModelStubs.getProduct(1L)).collect(Collectors.toList());
        when(repository.search(searchValue)).thenReturn(stubProductList);

        List<ProductDto> expectedCollect = Stream.of(ModelStubs.getProductDto(1L)).collect(Collectors.toList());

        List<ProductDto> factCollect = productService.search(searchValue);
        verify(dtoMapper).toProductDto(stubProductList);
        assertEquals(expectedCollect, factCollect);
    }

    @Test
    void searchByFilter() {
        when(repository.findAll(Mockito.<Specification<Product>>any()))
                .thenReturn(
                        Stream.of(
                                ModelStubs.getProduct(1L),
                                ModelStubs.getProduct(2L),
                                ModelStubs.getProduct(3L)
                        ).collect(Collectors.toList())
                );

        List<ProductDto> actualProductDtos = productService
                .searchByFilter(SpecificationStubs.getProductSpecificationStub());

        assertNotNull(actualProductDtos, "failure - expected that a list of employeeDto not null");
        assertEquals(3, actualProductDtos.size(), "failure - expected that a list of employeeDto equals 3");
    }
}