package me.androidbox.busbybaking.recipieslist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;

import javax.inject.Inject;

import me.androidbox.busbybaking.model.Recipe;
import me.androidbox.busbybaking.networkapi.RecipesAPI;
import me.androidbox.busbybaking.recipieslist.di.DaggerTestBusbyComponent;
import me.androidbox.busbybaking.recipieslist.di.MockRecipeSchedulersModule;
import me.androidbox.busbybaking.recipieslist.di.TestBusbyComponent;
import rx.Observable;
import rx.Subscription;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by smason on 6/23/2017 AD.
 */
public class RecipeListModelImpTest {

    @Mock Subscription subscription;
    @Mock RecipesAPI recipesAPI;
    @Mock RecipeListModelContract.RecipeGetAllListener recipeGetAllListener;
    @Mock List<Recipe> recipes;

    @Inject RecipeSchedulers recipeSchedulers;

    private RecipeListModelContract recipeListModel;

    @Before
    public void setup() {

        TestBusbyComponent testBusbyComponent = DaggerTestBusbyComponent.builder()
                .mockRecipeSchedulersModule(new MockRecipeSchedulersModule())
                .build();

        testBusbyComponent.inject(RecipeListModelImpTest.this);

        MockitoAnnotations.initMocks(RecipeListModelImpTest.this);
        recipeListModel = new RecipeListModelImp(recipesAPI, recipeSchedulers);
    }

    @Test(expected = NullPointerException.class)
    public void testShouldThrowExceptionOnNullParameter() {
        recipeListModel = new RecipeListModelImp(null, null);
    }

    @Test
    public void testRecipeListModelShouldNotBeNull() {
        assertNotNull(recipeListModel);
    }

    @Test
    public void testShouldGetRecipesFromAPI() {
        when(recipesAPI.getAllRecipes()).thenReturn(Observable.just(recipes));

        recipeListModel.getRecipesFromAPI(recipeGetAllListener);

        verify(recipesAPI, times(1)).getAllRecipes();
        verify(recipeGetAllListener, times(1)).onRecipeGetAllSuccess(recipes);
        verify(recipeGetAllListener, never()).onRecipeGetAllFailure(anyString());
    }

    @Test
    public void testShouldFailToGetRecipesFromAPI() {
        when(recipesAPI.getAllRecipes())
                .thenReturn(Observable.<List<Recipe>>error(
                        new Throwable(new RuntimeException("Failed to get recipes"))));

        recipeListModel.getRecipesFromAPI(recipeGetAllListener);

        verify(recipesAPI, times(1)).getAllRecipes();
        verify(recipeGetAllListener, times(1)).onRecipeGetAllFailure(anyString());
        verify(recipeGetAllListener, never()).onRecipeGetAllSuccess(recipes);
    }

    @Test
    public void testShouldShutdown() {
        when(subscription.isUnsubscribed()).thenReturn(false);
        final Field subscriptionField;

        try {
            subscriptionField = recipeListModel.getClass().getDeclaredField("subscription");
            subscriptionField.setAccessible(true);
            subscriptionField.set(recipeListModel, subscription);
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        }
        catch(IllegalAccessException e) {
            e.printStackTrace();
        }

        recipeListModel.releaseResources();

        verify(subscription, times(1)).unsubscribe();
    }
}