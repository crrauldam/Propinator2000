package com.crraul.propinator2000;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView input; // muestra numero que se esta introduciendo
    TextView result; // muestra resultado de la operacion

    RadioGroup selection; // boton para seleccionar la calidad del trato

    Button deleteButton; // borra un caracter
    LinearLayout pad; // teclado numerico
    ArrayList<Button> nums = new ArrayList<>(); // botones de numero
    Button comma, calculateButton; // coma y boton para calcular

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // obtener elementos del Layout
        input = findViewById(R.id.input);
        result = findViewById(R.id.result);
        selection = findViewById(R.id.selection);
        pad = findViewById(R.id.pad);
        deleteButton = findViewById(R.id.deleteButton);
        comma = findViewById(R.id.coma);
        calculateButton = findViewById(R.id.calculateButton);

        // recorrer linearlayout del pad numerico
        for (int i = 0; i < pad.getChildCount(); i++) {
            LinearLayout temp = (LinearLayout) pad.getChildAt(i);
            // recorrer linearlayout anidados dentro del pad (padRows)
            for (int j = 0; j < temp.getChildCount(); j++) {
                // meter cada boton en el arraylist
                nums.add((Button) temp.getChildAt(j));
            }
        }

        // añade clic listener a cada boton del panel de numeros
        for (Button num : nums) {
            num.setOnClickListener(view -> {
                String buffer = input.getText().toString(); // numero hasta el momento
                String numText = num.getText().toString(); // simbolo del boton

                switch (numText) {
                    case "Borrar": // borra el ultimo caracter
                        if (!buffer.isEmpty()) {
                            input.setText(buffer.substring(0, buffer.length()-1));
                            result.setText("");
                        }
                        break;
                    case ".":
                        // solo añade la coma si no se ha puesto ya
                        if (!buffer.contains(numText)) {
                            input.setText(buffer + numText);
                        }
                        break;
                    default:
                        input.setText(buffer+numText);
                        break;
                }
            });
        }

        // listener de clic del boton de calcular
        calculateButton.setOnClickListener(view -> {
            String buffer = input.getText().toString();
            double amount;
            double total = 0;

            if (!buffer.isEmpty()) {
                amount = Double.parseDouble(buffer);

                switch (getServiceQuality()) {
                    case "Malo":
                        total = amount;
                        break;
                    case "Bueno": // añade +5% si es bueno
                        total = amount + amount/100*5;
                        break;
                    case "Excelente": // añade +15% si es excelente
                        total = amount + amount/100*15;
                        break;
                }
                result.setText("El total es: "+total);
            }
        });
    }

    /**
     * Obtiene la opinion acerca de la calidad del servicio
     * @return opcion elegida, un String vacío si no se ha seleccionado ninguna
     */
    public String getServiceQuality() {
        String selectedValue = "";
        int selectedId = selection.getCheckedRadioButtonId();

        // comprueba que se haya seleccionado alguna opcion
        if (selectedId != -1) {
            RadioButton selectedButton = findViewById(selectedId);
            selectedValue = selectedButton.getText().toString();
        }

        return selectedValue;
    }
}