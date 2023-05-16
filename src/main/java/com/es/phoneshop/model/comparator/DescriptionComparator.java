package com.es.phoneshop.model.comparator;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.Product;

import java.util.Comparator;

public class DescriptionComparator implements Comparator<Product> {
    private String description;

    public DescriptionComparator(String description) {
        this.description = description;
    }

    @Override
    public int compare(Product product1, Product product2) {
        if (description == null) {
            return 0;
        }
        return (int) (ArrayListProductDao.countMatchingWords(product2.getDescription(), description) -
                ArrayListProductDao.countMatchingWords(product1.getDescription(), description));
    }
}
