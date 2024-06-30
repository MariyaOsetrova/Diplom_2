package Model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class Order {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
