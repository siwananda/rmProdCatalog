package models;

import org.junit.Test;
import org.mongodb.morphia.Key;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductModelTest {

    @Test
    public void testCreate() throws Exception {
        ProductModel newProduct = new ProductModel();
        newProduct.setTitle("woohoo");
        PricingModel newPricing = new PricingModel();
        newPricing.setPrice(1.20);
        newPricing.setCost(1.00);
        newProduct.setPricing(newPricing);
        Key newProductKey = ProductModel.create(newProduct);
        assertTrue(newProductKey.getId() != null);
    }

    @Test
    public void testListProduct() throws Exception {
        List<ProductModel> productModelList = ProductModel.list(0, 10);
        assertEquals(productModelList.size(), 10);
    }

    @Test
    public void testFindById() throws Exception {
        ProductModel productModel = ProductModel.findByProductId(9653);
        assertTrue(9653 == productModel.getId());
    }

    @Test
    public void testFindByQuery() throws Exception {
        List<ProductModel> productModelList = ProductModel.listByQuery("/^" + 96 + ".*/.test(this.id)", 0, 10);
        assertTrue(productModelList.size()>0);
    }
}