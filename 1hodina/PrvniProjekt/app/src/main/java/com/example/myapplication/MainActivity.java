package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView tvDisplay;
    private boolean isNewOp = true;
    // Nové proměnné pro výpočty
    private String operator = "";
    private double firstValue = 0;

    // list operandů na displeji
    private StringBuilder currentNumber = new StringBuilder();
    // list hodnot na pozadí
    private ArrayList<Double> numbers = new ArrayList<>();
    // list operátorů
    private ArrayList<String> operators = new ArrayList<>();
    private boolean lastInputWasOp = false;
    private boolean hasResults = false; // Pomocník pro detekci, zda jen opakujeme rovná se


    private double last_operand = 0;
    private String last_operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);

        // Tlačítko AC (vše smazat)
//        findViewById(R.id.butt_clearall).setOnClickListener(v -> {
//            tvDisplay.setText("0");
//            firstValue = 0;
//            operator = "";
//            isNewOp = true;
//        });

        if (savedInstanceState != null) {
            // Vybalíme data z "balíčku"
            tvDisplay.setText(savedInstanceState.getString("displayText"));
            currentNumber = new StringBuilder(savedInstanceState.getString("currentNumber"));
            numbers = (ArrayList<Double>) savedInstanceState.getSerializable("numbers");
            operators = (ArrayList<String>) savedInstanceState.getSerializable("operators");
            lastInputWasOp = savedInstanceState.getBoolean("lastInputWasOp");
            hasResults = savedInstanceState.getBoolean("hasResults");
            last_operator = savedInstanceState.getString("last_operator");
            last_operand = savedInstanceState.getDouble("last_operand");
            // Pokud si ukládáš i lastOperator a lastOperand pro opakované "=", přidej je sem taky
        } else {
            // Pokud je to úplně první spuštění aplikace, vytvoříme nové prázdné listy
            numbers = new ArrayList<>();
            operators = new ArrayList<>();
            currentNumber = new StringBuilder();
        }

        findViewById(R.id.butt_clearall).setOnClickListener(v -> fullClear());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Zabalíme všechna důležitá data do balíčku "outState"
        outState.putString("displayText", tvDisplay.getText().toString());
        outState.putString("currentNumber", currentNumber.toString());
        outState.putSerializable("numbers", numbers);
        outState.putSerializable("operators", operators);
        outState.putBoolean("lastInputWasOp", lastInputWasOp);
        outState.putBoolean("hasResults", hasResults);
        outState.putString("last_operator", last_operator);
        outState.putDouble("last_operand", last_operand);

    }

    private void fullClear() {
        tvDisplay.setText("0");
        currentNumber.setLength(0);
        numbers.clear();
        operators.clear();
        lastInputWasOp = false;
    }


    // Metoda pro všechna číselná tlačítka (0-9 a čárka)
    public void onNumberClick(View view) {
        if (hasResults && !lastInputWasOp) {
            fullClear(); // Pokud píšeš po výsledku, smaž vše a začni znovu
            hasResults = false;
        }

        if (isNewOp) {
            tvDisplay.setText("");
        }
        isNewOp = false;

        Button button = (Button) view;
        String currentText = tvDisplay.getText().toString();
        String buttonText = button.getText().toString();


        // Zamezení více čárkám v jednom čísle
        if (buttonText.equals(",") && currentText.contains(",")) {
            return;
        }

        currentNumber.append(buttonText);
        lastInputWasOp = false;
        updateDisplay();
        //tvDisplay.append(buttonText);
    }

    public void onOpClick(View view) {
        Button button = (Button) view;
        String op = button.getText().toString();

        String currentText = tvDisplay.getText().toString();

        if (currentNumber.length() > 0) {
            // Uložíme rozepsané číslo do listu
            String val = currentNumber.toString().replace(",", ".");
            numbers.add(Double.parseDouble(val));
            operators.add(op);
            currentNumber.setLength(0); // Vyčistíme pro další číslo
            lastInputWasOp = true;

        }   else if (lastInputWasOp && !operators.isEmpty()) {
            // Uživatel chce jen změnit operátor (kliká na operátory po sobě)
            operators.set(operators.size() - 1, op);
        }

        updateDisplay();

//        // SCÉNÁŘ A: Uživatel chce jen změnit operátor (kliká na operátory po sobě)
//        if (isNewOp && !operator.isEmpty()) {
//            operator = op; // Změníme operátor v paměti
//
//            // Upravíme zobrazení na displeji (usekneme starý operátor a dáme nový)
//            // Předpokládáme, že formát je "Číslo Operátor"
//            if (currentText.contains(" ")) {
//                String numberPart = currentText.split(" ")[0];
//                tvDisplay.setText(numberPart + " " + operator);
//            }
//            return; // Ukončíme metodu, zbytek se neprovede
//        }
//
//        // SCÉNÁŘ B: První stisknutí operátoru po zadání čísla
//        String val = currentText.replace(",", ".");
//        firstValue = Double.parseDouble(val);
//        operator = op;
//
//        // Zobrazíme na displeji číslo i zvolený operátor
//        tvDisplay.setText(currentText + " " + operator);
//
//        isNewOp = true; // Příprava pro zadání druhého čísla
    }

    public void onSciFunctionClick(View view) {
        Button b = (Button) view;
        String func = b.getText().toString();

        try {
            // Získáme aktuální hodnotu z displeje (StringBuilder nebo TextView)
            String valText = currentNumber.length() > 0 ? currentNumber.toString() : tvDisplay.getText().toString();
            double val = Double.parseDouble(valText.replace(",", "."));
            double result = 0;

            switch (func) {
                case "sin(x)":
                    result = Math.sin(Math.toRadians(val));
                    break;
                case "cos(x)":
                    result = Math.cos(Math.toRadians(val));
                    break;
                case "x²":
                    result = Math.pow(val, 2);
                    break;
                case "√":
                    if (val >= 0) result = Math.sqrt(val);
                    else { tvDisplay.setText("Chyba"); return; }
                    break;
            }

            // Výsledek dosadíme jako aktuální číslo
            currentNumber.setLength(0);
            String resStr = String.valueOf(result).replace(".0", "").replace(".", ",");
            currentNumber.append(resStr);
            updateDisplay();

        } catch (Exception e) {
            tvDisplay.setText("Chyba");
        }
    }

    // Pomocná metoda, aby se kód neopakoval a předešlo se pádům
    private double calculateSimple(double n1, String op, double n2) {
        switch (op) {
            case "+": return n1 + n2;
            case "-": return n1 - n2;
            case "x": return n1 * n2;
            case "/": return (n2 != 0) ? n1 / n2 : 0;

            case "x^y":
                return Math.pow(n1,n2);
            case "y√x":
                if (n2 != 0) return Math.pow(n1, 1.0 / n2);
                else return 0;

            default: return n1;
        }
    }

    // --- METODA PRO VÝSLEDEK (=) ---
    public void onEqualClick(View view) {

        // SCÉNÁŘ 1: Opakované stisknutí rovná se (pro "5 + 2 = 7 = 9 = 11")
        if (numbers.isEmpty() && currentNumber.length() > 0 && !last_operator.isEmpty() && !lastInputWasOp) {
            if (hasResults) {
                double currentVal = Double.parseDouble(currentNumber.toString().replace(",", "."));
                double result = calculateSimple(currentVal, last_operator, last_operand);

                displayFinalResult(result);
                return;
            }
        }

        // Pokud je v currentNumber ještě číslo, musíme ho přidat do listu
        if (currentNumber.length() > 0) {
            String val = currentNumber.toString().replace(",", ".");
            numbers.add(Double.parseDouble(val));
            currentNumber.setLength(0);

        } else if (lastInputWasOp && !operators.isEmpty()) {
            // Pokud končíme operátorem (např "1 + 2 +"), smažeme ten poslední
            operators.remove(operators.size() - 1);

        }

        if (numbers.isEmpty()) return;

        // uložíme si poslední operand a operátor
        if (!operators.isEmpty()) {
            last_operand = numbers.get(numbers.size() - 1);
            last_operator = operators.get(operators.size() - 1);
        }

        // VÝPOČET (Postupně procházíme listy)
        double result = numbers.get(0);
        for (int i = 0; i < operators.size(); i++) {
            String op = operators.get(i);
            double nextNum = (i + 1 < numbers.size()) ? numbers.get(i + 1) : 0;
            result = calculateSimple(result,op,nextNum);
            }


        // Zobrazení výsledku a reset pro další výpočet
        String resStr = String.valueOf(result).replace(".0", "").replace(".", ",");
        tvDisplay.setText(resStr);



        // Připravíme kalkulačku na další práci s výsledkem
//        numbers.clear();
//        operators.clear();
//        currentNumber.setLength(0);
//        currentNumber.append(resStr);
//        numbers.add(result);
//        lastInputWasOp = false;
        displayFinalResult(result);
        hasResults = true;

//        String val = tvDisplay.getText().toString().replace(",", ".");
//        double secondValue = Double.parseDouble(val);
//        double result = 0;
//
//        switch (operator) {
//            case "+": result = firstValue + secondValue; break;
//            case "-": result = firstValue - secondValue; break;
//            case "x": result = firstValue * secondValue; break;
//            case "/":
//                if (secondValue != 0) result = firstValue / secondValue;
//                break;
//        }
//
//        // Zobrazení výsledku (převedeme zpět tečku na čárku pro vzhled)
//        String finalResult = String.valueOf(result);
//        if (finalResult.endsWith(".0")) {
//            finalResult = finalResult.replace(".0", "");
//        }
//        tvDisplay.setText(finalResult.replace(".", ","));
//        isNewOp = true;
    }


    public void onDeleteClick(View view) {
        String text = tvDisplay.getText().toString();

        // Pokud je tam víc než jeden znak, smažeme poslední
        if (text.length() > 1) {
            text = text.substring(0, text.length() - 1);
            tvDisplay.setText(text);
        } else {
            // Pokud zbývá poslední znak, nastavíme nulu a připravíme se na nové číslo
            tvDisplay.setText("0");
            isNewOp = true;
        }
    }

    public void onPlusMinusClick(View view) {
        String val = tvDisplay.getText().toString().replace(",", ".");
        if (!val.equals("0")) {
            double num = Double.parseDouble(val);
            num = num * -1;

            // Zobrazíme výsledek (opět ošetříme .0 a vrátíme čárku)
            String resultText = String.valueOf(num);
            if (resultText.endsWith(".0")) {
                resultText = resultText.replace(".0", "");
            }
            tvDisplay.setText(resultText.replace(".", ","));
        }
    }

    private void displayFinalResult(double result) {
        String resStr = String.valueOf(result).replace(".0", "").replace(".", ",");
        tvDisplay.setText(resStr);

        // Vyčistíme listy, ale výsledek necháme v currentNumber pro další práci
        numbers.clear();
        operators.clear();
        currentNumber.setLength(0);
        currentNumber.append(resStr);
        lastInputWasOp = false;
    }

    private void updateDisplay() {
        StringBuilder fullExpression = new StringBuilder();

        // Poskládáme výraz z listů a aktuálního čísla
        for (int i = 0; i < numbers.size(); i++) {
            String n = String.valueOf(numbers.get(i)).replace(".0", "").replace(".", ",");
            fullExpression.append(n).append(" ").append(operators.get(i)).append(" ");
        }
        fullExpression.append(currentNumber);

        String display = fullExpression.toString().trim();
        tvDisplay.setText(display.isEmpty() ? "0" : display);
    }
}


