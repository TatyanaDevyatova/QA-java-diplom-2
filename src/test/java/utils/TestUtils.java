package utils;

import clients.Client;
import io.restassured.response.ValidatableResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class TestUtils {

    public static String[] chooseIngredients(Client client, int ingredientNumber) {
        ValidatableResponse getIngredientsResponse = client.getIngredients();
        List<Map<String, String>> availableIngredients = getIngredientsResponse.extract().path("data");
        List<String> availableIngredientIds = availableIngredients
                .stream()
                .map(x -> x.get("_id"))
                .collect(Collectors.toList());
        return Arrays
                .stream(new String[ingredientNumber])
                .map(x -> x = availableIngredientIds.get((new Random()).nextInt(availableIngredientIds.size())))
                .toArray(String[]::new);
    }
}