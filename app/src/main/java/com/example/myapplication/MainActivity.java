package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.security.KeyPair;


// allows the user to generate and display cryptographic key pairs
//The key generation is implemented using OnClickListener
public class MainActivity extends AppCompatActivity {
    private KeyPair keyPair;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyKeys", Context.MODE_PRIVATE);

        Button generateButton = findViewById(R.id.Generate);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Generate and save the keys (as shown in the previous response)
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Key Pair Generation Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button showKeysButton = findViewById(R.id.Generate);
        showKeysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the keys from SharedPreferences
                String publicKeyString = sharedPreferences.getString("publicKey", "");
                String privateKeyString = sharedPreferences.getString("privateKey", "");

                // Display the keys in a dialog or toast
                if (!publicKeyString.isEmpty() && !privateKeyString.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Generated Keys");
                    builder.setMessage("Public Key: " + publicKeyString + "\nPrivate Key: " + privateKeyString);
                    builder.setPositiveButton("OK", null);
                    builder.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Keys not found in SharedPreferences", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
