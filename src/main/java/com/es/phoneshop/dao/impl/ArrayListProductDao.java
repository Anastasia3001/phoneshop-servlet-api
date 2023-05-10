package com.es.phoneshop.dao.impl;

import com.es.phoneshop.FunctionalReadWriteLock;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortingField;
import com.es.phoneshop.enums.SortingType;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.Product;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao implements ProductDao {
    private List<Product> products;
    private final AtomicLong id;
    private final FunctionalReadWriteLock lock;

    private ArrayListProductDao() {
        products = new ArrayList<>();
        id = new AtomicLong();
        lock = new FunctionalReadWriteLock();
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
            if (id == null) throw new IllegalArgumentException("Unable to find product with null id");
            return products.stream()
                    .filter(product -> product.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
        });
    }

    @Override
    public List<Product> findProducts(String description, SortingField sortingField, SortingType sortingType) {
        return lock.read(() -> {
            List<Product> foundProducts = products.stream()
                    .filter(product -> product.getPrice() != null && product.getStock() > 0)
                    .collect(Collectors.toList());
            if (description != null) {
                List<String> words = Stream.of(description.split("[^A-Za-z0-9I]+"))
                        .distinct()
                        .collect(Collectors.toList());
                foundProducts = foundProducts.stream()
                        .filter(product -> description.isEmpty() || countWordsInDescription(words, product.getDescription()) != 0)
                        .sorted((x, y) -> (int) (countWordsInDescription(words, y.getDescription()) - countWordsInDescription(words, x.getDescription())))
                        .collect(Collectors.toList());
            }
            return sortProducts(foundProducts, sortingField, sortingType);
        });
    }


    private List<Product> sortProducts(List<Product> products, SortingField sortingField, SortingType sortingType) {
        if (sortingField != null && SortingField.description == sortingField) {
            if (SortingType.asc == sortingType) {
                return products.stream()
                        .sorted(Comparator.comparing(Product::getDescription))
                        .collect(Collectors.toList());
            } else {
                return products.stream()
                        .sorted(Comparator.comparing(Product::getDescription).reversed())
                        .collect(Collectors.toList());
            }
        } else if(sortingField != null && SortingField.price == sortingField) {
            if (SortingType.asc == sortingType) {
                return products.stream()
                        .sorted(Comparator.comparing(Product::getPrice))
                        .collect(Collectors.toList());
            } else {
                return products.stream()
                        .sorted(Comparator.comparing(Product::getPrice).reversed())
                        .collect(Collectors.toList());
            }
        }
        return products;
    }


    private long countWordsInDescription(List<String> words, String description) {
        return words.stream()
                .filter(word -> Stream.of(description.split("[^A-Za-z0-9I]+"))
                        .distinct()
                        .filter(w -> w.equals(word)).count() == 1)
                .count();
    }

    @Override
    public void save(Product product) {
        lock.write(() -> {
            if (product == null) throw new IllegalArgumentException("Product equals null");
            Optional.ofNullable(product.getId())
                    .ifPresentOrElse(
                            (id) -> {
                                Product foundProduct = getProduct(product.getId());
                                products.set(products.indexOf(foundProduct), product);
                            },
                            () -> {
                                product.setId(id.incrementAndGet());
                                products.add(product);
                            });
        });
    }

    @Override
    public void delete(Long id) {
        lock.write(() -> {
            products.remove(getProduct(id));
        });
    }
}
