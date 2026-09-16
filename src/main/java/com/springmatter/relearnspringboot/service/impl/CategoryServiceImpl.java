package com.springmatter.relearnspringboot.service.impl;

import com.springmatter.relearnspringboot.dto.record.CategoryRequest;
import com.springmatter.relearnspringboot.dto.record.CategoryResponse;
import com.springmatter.relearnspringboot.dto.record.ProductRequest;
import com.springmatter.relearnspringboot.dto.record.ProductResponse;
import com.springmatter.relearnspringboot.entity.Category;
import com.springmatter.relearnspringboot.entity.Product;
import com.springmatter.relearnspringboot.mapper.CategoryMapper;
import com.springmatter.relearnspringboot.mapper.ProductMapper;
import com.springmatter.relearnspringboot.repository.CategoryRepository;
import com.springmatter.relearnspringboot.repository.ProductRepository;
import com.springmatter.relearnspringboot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest rq) {
        Category category = categoryMapper.toCategoryEnity(rq);
        if (!CollectionUtils.isEmpty(rq.products())) {
            rq.products().stream().filter(Objects::nonNull).forEach(product -> {
                Product pro = productMapper.toProductEntity(product);
                category.addProduct(pro);
            });
        }
        return categoryMapper.toCategoryResponse(categoryRepository.save(category), null);

    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest rq) {
        Category oldCategory = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryMapper.updateCategory(rq, oldCategory);

        // case category dont have products
        if (CollectionUtils.isEmpty(rq.products())) {
            List<Product> productsToRemove = new ArrayList<>(oldCategory.getProducts());
            productsToRemove.forEach(oldCategory::removeProduct);
            return categoryMapper.toCategoryResponse(categoryRepository.save(oldCategory), null);
        }

        // case category have products
        caseUpdateCategryWithProducts(rq, oldCategory);
        return categoryMapper.toCategoryResponse(categoryRepository.save(oldCategory), null);
    }

    private Category caseUpdateCategryWithProducts(CategoryRequest rq, Category oldCategory) {
        // case category have products
        // get list ids from old record product in category
        List<Long> newProductIds = rq.products().stream().filter(Objects::nonNull).map(ProductRequest::id).toList();

        // remove product dont have in update category
        removeOldProductsInCategory(newProductIds, oldCategory);

        // get list ids product in old category
        List<Long> oldProductIds = oldCategory.getProducts().stream().filter(Objects::nonNull).map(Product::getId).toList();

        // get map exist product with new record category
        Map<Long, Product> existingProductMap = getMapProductFetch(newProductIds, oldProductIds);

        rq.products().stream().filter(Objects::nonNull).forEach(productRequest -> {
            // case product dont have id
            if (productRequest.id() == null) {
                Product productNew = productMapper.toProductEntity(productRequest);
                oldCategory.addProduct(productNew);
            }
            // case product has id
            else {
                // case product has id and it dont contain in list ids old record
                if (!oldProductIds.contains(productRequest.id())) {
                    Product productUnAddCategory = existingProductMap.get(productRequest.id());
                    if (productUnAddCategory == null) {
                        throw new IllegalArgumentException("Product not found in DB with id: " + productRequest.id());
                    }
                    productMapper.updateProductEntity(productRequest, productUnAddCategory);
                    oldCategory.addProduct(productUnAddCategory);
                } else {
                    Product updateCurrentProduct = oldCategory.getProducts().stream()
                            .filter(i -> i.getId().equals(productRequest.id()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Product not found in category"));
                    productMapper.updateProductEntity(productRequest, updateCurrentProduct);
                }
            }
        });
        return oldCategory;

    }

    private void removeOldProductsInCategory(List<Long> newProductIds, Category oldCategory) {
        List<Product> productsToRemove = oldCategory.getProducts().stream()
                .filter(item -> !newProductIds.contains(item.getId()))
                .toList();
        productsToRemove.forEach(oldCategory::removeProduct);
    }

    private Map<Long, Product> getMapProductFetch(List<Long> newProductIds, List<Long> oldProductIds) {
        // get list id product have in new record and dont have id in old category
        List<Long> productIdsFetch = newProductIds.stream()
                .filter(item -> !oldProductIds.contains(item))
                .toList();

        List<Product> existingProducts = productRepository.findProductByIdIn(productIdsFetch);
        Map<Long, Product> existingProductMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(existingProducts)) {
            existingProductMap = existingProducts.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(Product::getId, p -> p));
        }
        return existingProductMap;
    }


    @Override
    public void deleteCategory(Long id) {
        Category oldCategory = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        List<Product> existingProducts = new ArrayList<>(oldCategory.getProducts());
        if (!CollectionUtils.isEmpty(existingProducts)) {
            existingProducts.forEach(oldCategory::removeProduct);
        }
        categoryRepository.delete(oldCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long id) {
        Category oldCategory = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        List<Product> existingProducts = oldCategory.getProducts();
        if (CollectionUtils.isEmpty(existingProducts)) {
            return categoryMapper.toCategoryResponse(oldCategory, null);
        }
        List<ProductResponse> productResponses = existingProducts.stream().map(productMapper::mapToProductResponse).toList();
        return categoryMapper.toCategoryResponse(oldCategory, productResponses);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        if (CollectionUtils.isEmpty(categoryPage.getContent())) {
            return Page.empty(pageable);
        }
        List<Category> categoryList = categoryPage.getContent();
        categoryRepository.getCategoriesWithProducts(categoryList);

        List<CategoryResponse> categoryResponses = categoryList.stream().map(category -> {
            List<ProductResponse> productResponses = category.getProducts().stream()
                    .filter(Objects::nonNull)
                    .map(productMapper::mapToProductResponse)
                    .toList();
            return categoryMapper.toCategoryResponse(category, productResponses);
        }).toList();

        return new PageImpl<>(categoryResponses, pageable, categoryPage.getTotalElements());
    }
}
