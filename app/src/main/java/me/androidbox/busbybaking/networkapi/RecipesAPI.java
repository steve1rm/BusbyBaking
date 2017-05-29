package me.androidbox.busbybaking.networkapi;

import java.util.List;

import me.androidbox.busbybaking.model.Recipe;
import me.androidbox.busbybaking.utils.Constants;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by steve on 5/25/17.
 */

public interface RecipesAPI {
    @GET(Constants.ENDPOINT)
    Observable<List<Recipe>> getAllRecipes();
}