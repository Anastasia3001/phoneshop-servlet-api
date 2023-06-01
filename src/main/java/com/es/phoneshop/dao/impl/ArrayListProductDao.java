package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.GenericDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortingField;
import com.es.phoneshop.enums.SortingType;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.model.comparator.DescriptionAndPriceComparator;
import com.es.phoneshop.model.comparator.DescriptionComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao extends GenericDao<Product> implements ProductDao {

    private ArrayListProductDao() {
        id = 0L;
        items = new ArrayList<>();
    }

    public static ArrayListProductDao getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ArrayListProductDao INSTANCE = new ArrayListProductDao();
    }

    @Override
    public Product getProduct(Long id) {
        return lock.read(() -> {
            Optional.ofNullable(id)
                    .orElseThrow(() -> new IllegalArgumentException("Unable to find product with null id"));
            return items.stream()
                    .filter(product -> product.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
        });
    }

    @Override
    public List<Product> findProducts(String description, SortingField sortingField, SortingType sortingType) {
        return lock.read(() -> {
            List<Product> foundProducts = items.stream()
                    .filter(product -> product.getPrice() != null && product.getStock() > 0)
                    .filter(product -> description == null || description.isEmpty() || countMatchingWords(product.getDescription(), description) != 0)
                    .sorted(new DescriptionComparator(description))
                    .sorted(new DescriptionAndPriceComparator(sortingField, sortingType))
                    .collect(Collectors.toList());
            return foundProducts;
        });
    }

    public static long countMatchingWords(String productDescription, String description) {
        return Stream.of(description.split("[^A-Za-z0-9I]+"))
                .filter(word -> productDescription.contains(word))
                .count();
    }

    @Override
    public void save(Product product) {
        lock.write(() -> {
            Optional.ofNullable(product)
                    .orElseThrow(() -> new IllegalArgumentException("Product equals null"));
            Optional.ofNullable(product.getId())
                    .ifPresentOrElse(
                            (id) -> {
                                Product foundProduct = getProduct(product.getId());
                                items.set(items.indexOf(foundProduct), product);
                            },
                            () -> {
                                product.setId(id++);
                                items.add(product);
                            });
        });
    }

    @Override
    public void delete(Long id) {
        lock.write(() -> {
            items.removeIf(product -> product.getId().equals(id));
        });
    }
}
