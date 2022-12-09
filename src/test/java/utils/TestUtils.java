package utils;

import clients.Client;
import io.restassured.response.ValidatableResponse;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class TestUtils {

    public static String[] chooseIngredients(Client client, int ingredientNumber) {
        ValidatableResponse getIngredientsResponse = client.getIngredients();
        List<Map<String, String>> availableIngredients = getIngredientsResponse.extract().path("data");
        List<String> availableIngredientIds = availableIngredients.stream().map(x -> x.get("_id")).collect(Collectors.toList());

        String[] ingredientsForOrder = new String[ingredientNumber];
        for (int i = 0; i < ingredientNumber; i++) {
            ingredientsForOrder[i] = availableIngredientIds.get((new Random()).nextInt(availableIngredientIds.size()));
        }
        return ingredientsForOrder;
    }
}

