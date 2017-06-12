package me.androidbox.busbybaking.recipieslist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.androidbox.busbybaking.R;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipeItemListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_fragment_container, RecipeListView.newInstance(), RecipeListView.TAG)
                    .commit();
        }
    }

    @Override
    public void onRecipeItem() {
        Timber.d("onRecipeItem");
    }
}
