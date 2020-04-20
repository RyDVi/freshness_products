package com.rydvi.product.edibility.recognizer.api;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ProductsViewModel extends ViewModel {
    private MutableLiveData<List<Product>> products = new MutableLiveData();

    public MutableLiveData<List<Product>> getProducts() {
        return products;
    }

    public void refreshProducts() {
        products.postValue(DataRepo.getInstance().loadProducts());
    }
}
