package models;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductModelTest {

    @Test
    public void testCreate() throws Exception {
        ProductModel newProduct = new ProductModel();
        newProduct.setTitle("woohoo");
        newProduct.getPricing().setPrice(1.20);
        newProduct.getPricing().setCost(1.00);
        ProductModel.create(newProduct);
    }

    //
//    public void testRemove() throws Exception {
//
//    }
//
    @Test
    public void testListProduct() throws Exception {
        List<ProductModel> productModelList = ProductModel.list(0, 10);
        assertEquals(productModelList.size(), 10);
    }
//
//    public void testFindById() throws Exception {
//
//    }
//
//    public void testFindByName() throws Exception {
//
//    }
}