package models;

import org.mongodb.morphia.annotations.Embedded;

/**
 * Pricing Model embedded in product model
 * <p/>
 * Created by siwananda on 11/22/2014.
 */
@Embedded
public class PricingModel {
    Double cost;
    Double price;
    Double promo_price;
    Double savings;
    Double on_sale;

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPromo_price() {
        return promo_price;
    }

    public void setPromo_price(Double promo_price) {
        this.promo_price = promo_price;
    }

    public Double getSavings() {
        return savings;
    }

    public void setSavings(Double savings) {
        this.savings = savings;
    }

    public Double getOn_sale() {
        return on_sale;
    }

    public void setOn_sale(Double on_sale) {
        this.on_sale = on_sale;
    }
}
