package Model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class Ingredient {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String _id;
    private String type;
    private String name;
}
