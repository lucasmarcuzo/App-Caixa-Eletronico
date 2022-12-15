package br.com.marcuzo.caixaeletronico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class ModuloAdministrativoActivity extends AppCompatActivity {

    private Button btn_relatorioCedulas;
    private Button btn_valorTotalDisponivel;
    private Button btn_reposicaoCedulas;
    private Button btn_alterarCotaMinima;
    private Button btn_sairAdm;
    private Button btn_voltarAoMenuAnteriorRelatorioCedulas;
    private Button btn_voltarAoMenuPrincipalValorTotalDisponivel;
    private Button btn_voltarAoMenuAnteriorReposicaoCelulas;
    private CardView cv_adm;
    private CardView cv_relatorioCedulas;
    private CardView cv_valorTotalDisponivel;
    private CardView cv_reposicaoCedulas;
    private CardView cv_cotaMinima;
    private TextView nomeAdm;
    private TextView tv_nota100RelatorioCedulas;
    private TextView tv_nota50RelatorioCedulas;
    private TextView tv_nota20RelatorioCedulas;
    private TextView tv_nota10RelatorioCedulas;
    private TextView tv_nota5RelatorioCedulas;
    private TextView tv_nota2RelatorioCedulas;
    private TextView tv_valorTotalDisponivel;
    private CaixaEletronico caixaEletronico;
    private EditText tv_nota100ReposicaoCedulas;
    private EditText tv_nota50ReposicaoCedulas;
    private EditText tv_nota20ReposicaoCedulas;
    private EditText tv_nota10ReposicaoCedulas;
    private EditText tv_nota5ReposicaoCedulas;
    private EditText tv_nota2ReposicaoCedulas;
    private TextView tv_valorTotalDisponivelReposicaoCedulas;
    private Button btn_salvarReposicaoCedulas;
    private TextView tv_cotaMinimaDisponivel;
    private EditText et_valorCotaMinima;
    private Button btn_salvarCotaMinima;
    private Button btn_voltarAoMenuAnteriorCotaMinima;

    private TextWatcher mascaraCotaMinima;
    private final Locale ptBr = new Locale("pt", "BR");
    private NumberFormat formatter = NumberFormat.getCurrencyInstance(ptBr);

    private BigDecimal valorTotalReposicaoCedulas = BigDecimal.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_adm);

        //Inicialização de variáveis.
        this.btn_relatorioCedulas = findViewById(R.id.btn_relatorioCedulas);
        this.btn_valorTotalDisponivel = findViewById(R.id.btn_valorTotalDisponivel);
        this.btn_reposicaoCedulas = findViewById(R.id.btn_reposicaoCedulas);
        this.btn_alterarCotaMinima = findViewById(R.id.btn_alterarCotaMinima);
        this.btn_sairAdm = findViewById(R.id.btn_sairAdm);
        this.btn_voltarAoMenuAnteriorRelatorioCedulas = findViewById(R.id.btn_voltarAoMenuAnteriorRelatorioCedulas);
        this.cv_adm = findViewById(R.id.cv_adm);
        this.cv_relatorioCedulas = findViewById(R.id.cv_relatorioCedulas);
        this.nomeAdm = findViewById(R.id.tv_nomeAdm);
        this.tv_nota100RelatorioCedulas = findViewById(R.id.tv_nota100RelatorioCedulas);
        this.tv_nota50RelatorioCedulas = findViewById(R.id.tv_nota50RelatorioCedulas);
        this.tv_nota20RelatorioCedulas = findViewById(R.id.tv_nota20RelatorioCedulas);
        this.tv_nota10RelatorioCedulas = findViewById(R.id.tv_nota10RelatorioCedulas);
        this.tv_nota5RelatorioCedulas = findViewById(R.id.tv_nota5RelatorioCedulas);
        this.tv_nota2RelatorioCedulas = findViewById(R.id.tv_nota2RelatorioCedulas);
        this.cv_valorTotalDisponivel = findViewById(R.id.cv_valorTotalDisponivel);
        this.tv_valorTotalDisponivel = findViewById(R.id.tv_valorTotalDisponivel);
        this.btn_voltarAoMenuPrincipalValorTotalDisponivel = findViewById(R.id.btn_voltarAoMenuPrincipalValorTotalDisponivel);
        this.tv_nota100ReposicaoCedulas = findViewById(R.id.tv_nota100ReposicaoCedulas);
        this.tv_nota50ReposicaoCedulas = findViewById(R.id.tv_nota50ReposicaoCedulas);
        this.tv_nota20ReposicaoCedulas = findViewById(R.id.tv_nota20ReposicaoCedulas);
        this.tv_nota10ReposicaoCedulas = findViewById(R.id.tv_nota10ReposicaoCedulas);
        this.tv_nota5ReposicaoCedulas = findViewById(R.id.tv_nota5ReposicaoCedulas);
        this.tv_nota2ReposicaoCedulas = findViewById(R.id.tv_nota2ReposicaoCedulas);
        this.tv_valorTotalDisponivelReposicaoCedulas = findViewById(R.id.tv_valorTotalDisponivelReposicaoCedulas);
        this.btn_salvarReposicaoCedulas = findViewById(R.id.btn_salvarReposicaoCedulas);
        this.cv_reposicaoCedulas = findViewById(R.id.cv_reposicaoCedulas);
        this.btn_voltarAoMenuAnteriorReposicaoCelulas = findViewById(R.id.btn_voltarAoMenuAnteriorReposicaoCelulas);
        this.cv_cotaMinima = findViewById(R.id.cv_cotaMinima);
        this.tv_cotaMinimaDisponivel = findViewById(R.id.tv_cotaMinimaDisponivel);
        this.et_valorCotaMinima = findViewById(R.id.et_valorCotaMinima);
        this.btn_salvarCotaMinima = findViewById(R.id.btn_salvarCotaMinima);
        this.btn_voltarAoMenuAnteriorCotaMinima = findViewById(R.id.btn_voltarAoMenuAnteriorCotaMinima);


        //Exibindo o nome do administrador & pegando os dados do Caixa Eletrônico.
        this.caixaEletronico = (CaixaEletronico) getIntent().getSerializableExtra("caixaEletronicoAdm");
        this.nomeAdm.setText(getIntent().getStringExtra("nomeAdm"));

        //Máscara para o campo de valor de cota mímima.
        this.mascaraCotaMinima = new ConvercaoMonetaria(this.et_valorCotaMinima, this.ptBr);

        //Botoes de voltar.
        voltarParaTelaDeLogin(btn_sairAdm);
        voltarAoMenuAnteriorAdm(btn_voltarAoMenuAnteriorRelatorioCedulas);
        voltarAoMenuAnteriorAdm(btn_voltarAoMenuPrincipalValorTotalDisponivel);
        voltarAoMenuAnteriorAdm(btn_voltarAoMenuAnteriorReposicaoCelulas);
        voltarAoMenuAnteriorAdm(btn_voltarAoMenuAnteriorCotaMinima);

        //Liberação de botão para salvar a nova cota mínima.
        liberarSalvarCotaMinima(et_valorCotaMinima);

        //Menu Adm:
            //Botão emitir relatório de cédulas.
            btn_relatorioCedulas.setOnClickListener(v -> {
                this.esconderCards();
                this.cv_relatorioCedulas.setVisibility(CardView.VISIBLE);
                emitirRelatorioCedulas();
            });

            //Botão para exibir o valor total disponivel no caixa.
            btn_valorTotalDisponivel.setOnClickListener(v -> {
                this.esconderCards();
                this.cv_valorTotalDisponivel.setVisibility(CardView.VISIBLE);
                this.exibirValorTotalDisponivel();
            });

            //Botão para reposição de cédulas.
            btn_reposicaoCedulas.setOnClickListener(v -> {
                this.esconderCards();
                this.cv_reposicaoCedulas.setVisibility(CardView.VISIBLE);
                emitirRelatorioCedulas();
                this.tv_valorTotalDisponivelReposicaoCedulas.setText(formatter.format(caixaEletronico.getSaldoCaixa()).replace("R$", "R$ "));
            });

            //Botão para exibir e modificar a cota mínima de cédulas.
            btn_alterarCotaMinima.setOnClickListener(v -> {
                this.esconderCards();
                this.cv_cotaMinima.setVisibility(CardView.VISIBLE);
                this.mascaraCotaMinima = new ConvercaoMonetaria(this.et_valorCotaMinima, this.ptBr);
                this.limparCampoAtualizacaoValorCotaMinima();
                this.exibirCotaMinima();
            });

        //Calcular valor total no caixa quando o valor no EditText for alterado.
        verificarValorEditText(tv_nota100ReposicaoCedulas);
        verificarValorEditText(tv_nota50ReposicaoCedulas);
        verificarValorEditText(tv_nota20ReposicaoCedulas);
        verificarValorEditText(tv_nota10ReposicaoCedulas);
        verificarValorEditText(tv_nota5ReposicaoCedulas);
        verificarValorEditText(tv_nota2ReposicaoCedulas);

        //Botão para salvar a reposição de cédulas.
        btn_salvarReposicaoCedulas.setOnClickListener(v -> {
            this.salvarReposicaoCedulas();
            this.esconderCards();
            this.cv_relatorioCedulas.setVisibility(CardView.VISIBLE);
            emitirRelatorioCedulas();
        });

        //Botão para salvar a nova cota mínima.
        btn_salvarCotaMinima.setOnClickListener(v -> {
            this.salvarCotaMinima();
            this.limparCampoAtualizacaoValorCotaMinima();
            this.exibirCotaMinima();
        });
    }

    //Liberar btn atualizar cota minima.
    private void liberarSalvarCotaMinima(EditText et){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //sem uso
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString() != "R$0,00"){
                    btn_salvarCotaMinima.setEnabled(true);
                    btn_salvarCotaMinima.setBackgroundTintList(getColorStateList(R.color.amarelo_sol));
                }
                else{
                    btn_salvarCotaMinima.setEnabled(false);
                    btn_salvarCotaMinima.setBackgroundTintList(getColorStateList(R.color.amarelo_desativado));
                }
            }
            @Override
            public void afterTextChanged(Editable e) {
                //sem uso
            }
        });
    }

    //Limpar campo do valor para ser atualizado da cota mínima.
    private void limparCampoAtualizacaoValorCotaMinima(){
        Util.limparFormatacaoCampo(et_valorCotaMinima, mascaraCotaMinima);

    }

    //Salvar o valor da cota mínima atualizado.
    private void salvarCotaMinima() {
        if (et_valorCotaMinima.getText().toString().isEmpty()) {
            Toast.makeText(this, "Digite um valor para a cota mínima.", Toast.LENGTH_SHORT).show();
        } else {
            Util.esconderTeclado(this);
            String valor = et_valorCotaMinima.getText().toString().substring(2).replace(".", "").replace(",", ".");
            valor = valor.substring(1, valor.length()-3) + ".00";
            BigDecimal valorCotaMinima = new BigDecimal(valor);
            caixaEletronico.setCotaMinima(valorCotaMinima);
            Toast.makeText(this, "Cota mínima atualizada com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    //Exibir o valor da cota mínima do caixa.
    private void exibirCotaMinima() {
        this.tv_cotaMinimaDisponivel.setText(formatter.format(caixaEletronico.getCotaMinima()).replace("R$", "R$ "));
    }

    //Calcular valor total quando valor do EditText for alterado.
    private void verificarValorEditText(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    calcularValorTotalReposicaoCedulas();
                }
                else{
                    editText.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //Exibe o valor total disponível no caixa.
    private void exibirValorTotalDisponivel() {
        this.tv_valorTotalDisponivel.setText(formatter.format(caixaEletronico.getSaldoCaixa()).replace("R$", "R$ "));
    }

    //Voltar para o menu anterior.
    private void voltarAoMenuAnteriorAdm(Button btn) {
        btn.setOnClickListener(v -> {
            Util.esconderTeclado(this);
            this.esconderCards();
            cv_adm.setVisibility(View.VISIBLE);
        });
    }

    //Voltar para a tela de Login.
    private void voltarParaTelaDeLogin(Button btn){
        btn.setOnClickListener(v -> {
            this.finish();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("caixaEletronico", this.caixaEletronico);
            startActivity(intent);
        });
    }

    //Esconder cards.
    private void esconderCards(){
        this.cv_adm.setVisibility(CardView.GONE);
        this.cv_relatorioCedulas.setVisibility(CardView.GONE);
        this.cv_valorTotalDisponivel.setVisibility(CardView.GONE);
        this.cv_reposicaoCedulas.setVisibility(CardView.GONE);
        this.cv_cotaMinima.setVisibility(CardView.GONE);
    }


    //Método para emitir o relatório de cédulas.
    public void emitirRelatorioCedulas(){
        LinkedHashMap<Integer, Integer> notasDisponiveis = this.caixaEletronico.getNotasDisponiveis();

        this.tv_nota100RelatorioCedulas.setText("0");
        this.tv_nota100ReposicaoCedulas.setText("0");
        this.tv_nota50RelatorioCedulas.setText("0");
        this.tv_nota50ReposicaoCedulas.setText("0");
        this.tv_nota20RelatorioCedulas.setText("0");
        this.tv_nota20ReposicaoCedulas.setText("0");
        this.tv_nota10RelatorioCedulas.setText("0");
        this.tv_nota10ReposicaoCedulas.setText("0");
        this.tv_nota5RelatorioCedulas.setText("0");
        this.tv_nota5ReposicaoCedulas.setText("0");
        this.tv_nota2RelatorioCedulas.setText("0");
        this.tv_nota2ReposicaoCedulas.setText("0");

        if(notasDisponiveis != null){
            for(Map.Entry<Integer, Integer> nd : notasDisponiveis.entrySet()) {
                switch (nd.getKey()) {
                    case 100:
                        this.tv_nota100RelatorioCedulas.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                        this.tv_nota100ReposicaoCedulas.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                        break;
                    case 50:
                        this.tv_nota50RelatorioCedulas.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                        this.tv_nota50ReposicaoCedulas.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                        break;
                    case 20:
                        this.tv_nota20RelatorioCedulas.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                        this.tv_nota20ReposicaoCedulas.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                        break;
                    case 10:
                        this.tv_nota10RelatorioCedulas.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                        this.tv_nota10ReposicaoCedulas.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                        break;
                    case 5:
                        this.tv_nota5RelatorioCedulas.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                        this.tv_nota5ReposicaoCedulas.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                        break;
                    case 2:
                        this.tv_nota2RelatorioCedulas.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                        this.tv_nota2ReposicaoCedulas.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                        break;
                }
            }
        }
    }

    //Método para salvar a reposição de cédulas e atualizar o saldo do caixa.
    private void salvarReposicaoCedulas(){
        LinkedHashMap<Integer, Integer> notasReposicao = new LinkedHashMap<>();
        notasReposicao.put(100, Integer.parseInt(tv_nota100ReposicaoCedulas.getText().toString()));
        notasReposicao.put(50, Integer.parseInt(tv_nota50ReposicaoCedulas.getText().toString()));
        notasReposicao.put(20, Integer.parseInt(tv_nota20ReposicaoCedulas.getText().toString()));
        notasReposicao.put(10, Integer.parseInt(tv_nota10ReposicaoCedulas.getText().toString()));
        notasReposicao.put(5, Integer.parseInt(tv_nota5ReposicaoCedulas.getText().toString()));
        notasReposicao.put(2, Integer.parseInt(tv_nota2ReposicaoCedulas.getText().toString()));

        this.caixaEletronico.setValorDeNotasEmCaixa(notasReposicao);
        this.caixaEletronico.setSaldoCaixa(this.valorTotalReposicaoCedulas);
        Toast.makeText(this, "Reposição de cédulas realizada com sucesso!", Toast.LENGTH_LONG).show();
    }


    //Método para calcular o valor total de cédulas disponíveis no caixa.
    public void calcularValorTotalReposicaoCedulas(){
        this.valorTotalReposicaoCedulas = BigDecimal.valueOf(0);
        double nota100 = Double.parseDouble(tv_nota100ReposicaoCedulas.getText().toString());
        double nota50 = Double.parseDouble(tv_nota50ReposicaoCedulas.getText().toString());
        double nota20 = Double.parseDouble(tv_nota20ReposicaoCedulas.getText().toString());
        double nota10 = Double.parseDouble(tv_nota10ReposicaoCedulas.getText().toString());
        double nota5 = Double.parseDouble(tv_nota5ReposicaoCedulas.getText().toString());
        double nota2 = Double.parseDouble(tv_nota2ReposicaoCedulas.getText().toString());

        this.valorTotalReposicaoCedulas = BigDecimal.valueOf((nota100 * 100) + (nota50 * 50) + (nota20 * 20) + (nota10 * 10) + (nota5 * 5) + (nota2 * 2));
        this.tv_valorTotalDisponivelReposicaoCedulas.setText("Valor total no caixa:\n"+ formatter.format(valorTotalReposicaoCedulas).replace("R$", "R$ "));
    }


}