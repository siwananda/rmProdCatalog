package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import play.Logger;
import play.data.validation.Constraints;
import utils.MorphiaObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Product Model for product list
 * <p/>
 * Created by siwananda on 11/20/2014.
 */

@Entity("product_list")
public class ProductModel {

    @Id
    ObjectId _id;

    /**
     * Stores id field as integer since Mongo 2.6.x will store long as NumberLong
     * NumberLong can't be queried using regex for 'like' search using $where
     */
    @Indexed
    @Constraints.Required
    Integer id;

    @Constraints.Required
    String title;

    @Embedded
    PricingModel pricing;

    public String getMongoId() {
        return _id.toString();
    }

    public void setMongoId(String mongoId) {
        this._id = new ObjectId(mongoId);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PricingModel getPricing() {
        return pricing;
    }

    public void setPricing(PricingModel pricing) {
        this.pricing = pricing;
    }

    /**
     * Finds a product based on id (not ObjectId)
     *
     * @param productId id for the product to search
     * @return found product
     */
    public static ProductModel findByProductId(Integer productId) {
        if (MorphiaObject.datastore != null) {
            return MorphiaObject.datastore.createQuery(ProductModel.class)
                    .filter("id = ", productId).get();
        } else {
            return new ProductModel();
        }
    }

    /**
     * Find a list of product based on the given query.
     * Query will be piped into $where in mongo
     *
     * @param query the query to run
     * @param skip  skip amount, 0 if no skipping
     * @param limit how many records to retrieve. 0 for all.
     * @return list of found products
     */
    public static List<ProductModel> listByQuery(String query, int skip, int limit) {
        if (query != null && !query.equalsIgnoreCase("")) {
            return MorphiaObject.datastore.createQuery(ProductModel.class)
                    .where(query).offset(skip).limit(limit).retrievedFields(true, "id", "title").asList();
        } else {
            return MorphiaObject.datastore.createQuery(ProductModel.class)
                    .offset(skip).limit(limit).retrievedFields(true, "id", "title").asList();
        }
    }

    /**
     * Finds a list of product
     *
     * @param skip  skip amount, 0 if no skipping
     * @param limit how many records to retrieve. 0 for all.
     * @return list of found products
     */
    public static List<ProductModel> list(int skip, int limit) {
        if (MorphiaObject.datastore != null) {
            return listByQuery("", skip, limit);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Finds all documents in this collection
     *
     * @return all the products
     */
    public static List<ProductModel> all() {
        if (MorphiaObject.datastore != null) {
            return MorphiaObject.datastore.find(ProductModel.class).asList();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Creates a new document.
     * If product ID is null during creation, auto generate id based on current max number
     *
     * @param productModel instance to be saved
     * @return generated key
     */
    public static Key<ProductModel> create(ProductModel productModel) {
        if (productModel.id == null) {
            ProductModel model = MorphiaObject.datastore.createQuery(ProductModel.class).order("-id").limit(1).get();
            Logger.debug("current largest id field: {}", model.getId());
            productModel.setId(model.getId() + 1);
        }

        return MorphiaObject.datastore.save(productModel);
    }

    /**
     * Updates a document. Using mongo id as the criteria instead of id.
     *
     * @param productModel instance to be updated
     * @return true if update successful, false otherwise
     */
    public static boolean update(ProductModel productModel) {
        Query<ProductModel> query = MorphiaObject.datastore.createQuery(ProductModel.class).field("_id").equal(new ObjectId(productModel.getMongoId()));
        UpdateOperations<ProductModel> updateOperations = MorphiaObject.datastore.createUpdateOperations(ProductModel.class)
                .set("title", productModel.getTitle())
                .set("pricing.price", productModel.getPricing().getPrice());

        UpdateResults updateResults = MorphiaObject.datastore.update(query, updateOperations);
        return updateResults.getUpdatedCount() > 0;
    }
}
