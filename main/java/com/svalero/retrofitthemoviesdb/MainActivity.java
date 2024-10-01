package com.svalero.retrofitthemoviesdb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.svalero.retrofitthemoviesdb.json_mapper.Movie;
import com.svalero.retrofitthemoviesdb.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencia al EditText y Button
        searchEditText = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.search_button);

        // Configura el listener del botón de búsqueda
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryString = searchEditText.getText().toString().trim();

                if (!queryString.isEmpty()) {
                    try {
                        int movieId = Integer.parseInt(queryString);  // Convierte el texto a un número
                        // Llama a la función para buscar películas por ID
                        searchMovies(movieId);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Introduce un ID de película válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Mostrar mensaje si el campo está vacío
                    Toast.makeText(MainActivity.this, "Introduce un ID de película", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchMovies(int movieId) {
        Call<Movie> call = RetrofitClient.getInstance().getMovieById(movieId);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();
                    if (movie != null) {
                        // Mostrar el título de la película encontrada
                        Toast.makeText(MainActivity.this,
                                "Película encontrada: " + movie.getTitle(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "No se encontró la película", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error en la búsqueda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
