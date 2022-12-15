package br.com.marcuzo.caixaeletronico;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class ConvercaoMonetaria implements TextWatcher {
    private final WeakReference<EditText> editTextWeakReference;
    private final Locale locale;

    // Construtor
    public ConvercaoMonetaria(EditText editText, Locale locale) {
        this.editTextWeakReference = new WeakReference<>(editText);
        this.locale = locale != null ? locale : Locale.getDefault();
    }

    // Métodos que formata o valor digitado no EditText
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Não faz nada
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Não faz nada
    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editText == null) return;
        editText.removeTextChangedListener(this);

        BigDecimal parsed = parseToBigDecimal(editable.toString(), locale);
        String formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }

    // Método que converte o valor para BigDecimal.
    private BigDecimal parseToBigDecimal(String value, Locale locale) {
        String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());
        String cleanString = value.replaceAll(replaceable, "");
        return new BigDecimal(cleanString).setScale(
                2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR
        );
    }

    //Retirar simbolos monetarios de valor bigdecimal.
    public static String retirarSimbolosMonetarios(EditText et, Activity activity) {
        String valor = "";
        if(et.getText().toString().trim().compareTo("R$0,00") == 0) {
            et.setError("Digite um valor válido!");
            return null;
        }
        else {
            valor = et.getText().toString().substring(2).replace(".", "").replace(",", ".");
            if (valor.indexOf(".") != valor.length() - 3) {
                Toast.makeText(activity, "Valor inválido", Toast.LENGTH_SHORT).show();
                et.setText(new BigDecimal(0).toString());
            }
            else{
                valor = valor.substring(1, valor.length() - 3) + ".00";
            }
            return valor;
        }

    }
}