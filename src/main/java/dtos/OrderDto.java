package dtos;

public class OrderDto {
    private String[] ingredients;

    public OrderDto(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public OrderDto() {
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
