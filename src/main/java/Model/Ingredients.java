package Model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class Ingredients {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Ingredient> data;
    private String success;

}
