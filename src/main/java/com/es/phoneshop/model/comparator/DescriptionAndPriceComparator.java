package com.es.phoneshop.model.comparator;

import com.es.phoneshop.enums.SortingField;
import com.es.phoneshop.enums.SortingType;
import com.es.phoneshop.model.Product;

import java.util.Comparator;

public class DescriptionAndPriceComparator implements Comparator<Product> {
    private SortingField sortingField;
    private SortingType sortingType;

    public DescriptionAndPriceComparator(SortingField sortingField, SortingType sortingType) {
        this.sortingField = sortingField;
        this.sortingType = sortingType;
    }

    @Override
    public int compare(Product product1, Product product2) {
        if (sortingField != null && SortingField.DESCRIPTION == sortingField) {
            if (SortingType.ASC == sortingType) {
                return product1.getDescription().compareTo(product2.getDescription());
            } else {
                return product2.getDescription().compareTo(product1.getDescription());
            }
        } else if (sortingField != null && SortingField.PRICE == sortingField) {
            if (SortingType.ASC == sortingType) {
                return product1.getPrice().compareTo(product2.getPrice());
            } else {
                return product2.getPrice().compareTo(product1.getPrice());
            }
        }
        return 0;
    }
}
