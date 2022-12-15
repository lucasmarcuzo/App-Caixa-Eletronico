package br.com.marcuzo.caixaeletronico;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class ConvercaoData {

    //Mascara para valores de data.
    public static TextWatcher mascaraData(final EditText et) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = ConvercaoData.retirarMascara(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : "##/##/####".toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                et.setText(mascara);
                et.setSelection(mascara.length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Sem implementação
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Sem implementação
            }
        };
    }

    //Método para remover a mascara de um campo.
    public static String retirarMascara(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]", "").replaceAll("[:]", "").replaceAll("[)]", "");
    }

}
