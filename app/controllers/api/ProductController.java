package controllers.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.ProductModel;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

/**
 * Rest controller for anything related to product.
 * <p/>
 * Created by siwananda on 11/20/2014.
 */
public class ProductController extends Controller {

    /**
     * Searches product on a given query string.
     * Expecting idQuery as request parameter
     *
     * @return arrays of product with a max amount of fixed 5
     */
    public static Result searchProduct() {
        Map queryString = request().queryString();
        if (queryString.containsKey("idQuery")) {

            //search and return max of 10
            String queryRequest = request().getQueryString("idQuery");
            List<ProductModel> productModelList = ProductModel.listByQuery("/^" + queryRequest + ".*/.test(this.id)", 0, 10);

            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode queryResult = objectMapper.createArrayNode();
            for (ProductModel productModel : productModelList) {
                queryResult.add(Json.toJson(productModel));
            }

            return ok(queryResult);
        }
        return internalServerError("No id query passed");
    }

    /**
     * Lists all the available product
     *
     * @return json array of all product.
     */
    public static Result listProducts() {
        List<ProductModel> productModelList = ProductModel.all();

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode queryResult = objectMapper.createArrayNode();
        for (ProductModel productModel : productModelList) {
            queryResult.add(Json.toJson(productModel));
        }

        return ok(queryResult);
    }

    /**
     * Gets a particular product by passed id
     *
     * @return json of found ProductModel
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getProduct(Integer id) {
        JsonNode response = Json.toJson(ProductModel.findByProductId(id));
        return ok(response);
    }


    /**
     * Updates existing product
     *
     * @param id id of the product to update
     * @return updated product if successful
     * @throws JsonProcessingException
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result updateProduct(Integer id) throws JsonProcessingException {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data");
        } else {
            Integer objectId = json.findPath("id").intValue();
            if (id == null || objectId <= 0) {
                return badRequest("Missing [id]");
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                ProductModel productModel = objectMapper.treeToValue(json, ProductModel.class);
                boolean updated = ProductModel.update(productModel);
                return updated ? ok(json) : internalServerError("Update failed. Can't find ID?");
            }
        }
    }
}
