package br.com.marcuzo.caixaeletronico;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Util {

    static Locale locale = new Locale("pt", "BR");

    //Esconder o teclado
    public static void esconderTeclado(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Mostrar o teclado
    public static void mostrarTeclado(View view, Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    //Método para formatar o valor das mascara de entrada de valores monetários.
    public static void limparFormatacaoCampo(EditText et, TextWatcher mascara) {
        if(et.getText().toString() != null && !et.getText().toString().isEmpty()){
            et.removeTextChangedListener(mascara);
            et.setText("");
            et.addTextChangedListener(mascara);
            et.requestFocus(0);
        }
        else{
            et.addTextChangedListener(mascara);
            et.requestFocus(0);
        }
    }

    //Método para exibir um calendário
    public static void exibirCalendario(Activity activity, EditText et) {
        et.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(activity, (datePicker, year1, month1, day1) -> {
                calendar.set(year1, month1, day1);
                String format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
                Date date;

                try {
                    date = sdf.parse(sdf.format(calendar.getTime()));
                    String dayS = new SimpleDateFormat("dd", Locale.ENGLISH).format(date);
                    String monthS = new SimpleDateFormat("MM", Locale.ENGLISH).format(date);
                    String yearS = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(date);

                    et.setText((dayS + "/" + monthS + "/" + yearS));
                } catch (ParseException ignored) {
                    Toast.makeText(activity, "Erro ao converter a data", Toast.LENGTH_SHORT).show();
                }
            }, year, month, day);
            datePickerDialog.show();
            datePickerDialog.getDatePicker();
        });
    }

    //Retornar data e hora atual.
    public static String dataHoraAtual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", locale);
        Date date = new Date();
        return dateFormat.format(date);
    }
}
