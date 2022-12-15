package br.com.marcuzo.caixaeletronico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class ModuloClienteActivity extends AppCompatActivity implements Serializable {

    //Declaração de variáveis.
    private EditText et_valorSaque;
    private EditText et_codigoDeBarrasPagamentoCliente;
    private EditText et_valorPagamentoCliente;
    private EditText et_dataVencimentoBoleto;
    private Button btn_efetuarSaque;
    private Button btn_voltarAoMenuAnteriorCliente;
    private Button btn_voltarAoMenuAnteriorNotasSacadas;
    private Button btn_realizarNovoSaque;
    private Button btn_voltarAoMenuAnteriorSaldoInsuficiente;
    private Button btn_tentarRealizarNovoSaque;
    private Button btn_saqueMenuPrincipal;
    private Button btn_verSaldoMenuPrincipal;
    private Button btn_pagamentosMenuPrincipal;
    private Button btn_minhaLocalizacaoMenuPrincipal;
    private Button btn_obterLocalizacaoCliente;
    private Button btn_voltarAoMenuAnteriorMinhaLocalizacao;
    private Button btn_voltarAoMenuAnteriorSaldoCliente;
    private Button btn_voltarAoMenuAnteriorPagamentoCliente;
    private Button btn_efetuarPagamentoBoleto;
    private Button btn_sairMenuPrincipal;
    private Button btn_voltarAoMenuAnteriorComprovantePagamento;
    private Button btn_tentarRealizarNovoPagamento;
    private Button btn_voltarAoMenuAnteriorSaldoPagamentoInsuficiente;
    private CardView cv_saque;
    private CardView cv_notasSacadas;
    private CardView cv_saldoInsuficiente;
    private CardView cv_menuPrincipal;
    private CardView cv_minhaLocalizacao;
    private CardView cv_saldoCliente;
    private CardView cv_comprovantePagamento;
    private CardView cv_pagamentoCliente;
    private CardView cv_saldoPagamentoIndisponivel;
    private TextView tv_nota100;
    private TextView tv_nota50;
    private TextView tv_nota20;
    private TextView tv_nota10;
    private TextView tv_nota5;
    private TextView tv_nota2;
    private TextView tv_nomeCliente;
    private TextView tv_mensagemErroSaque;
    private TextView tv_saldoAtual;
    private TextView tv_valorPagadorComprovante;
    private TextView tv_valorDataPagamentoComprovante;
    private TextView tv_valorDataVencimentoPagamento;
    private TextView tv_valorPagamentoComprovante;
    private TextView tv_valorCodigoBarrasComprovante;
    private TextView tv_mensagemErroPagamento;
    private CaixaEletronico caixaEletronico;
    private Conta contaCliente;
    private WebView wv_googleMaps;
    private ImageButton ib_cameraPagamentoCliente;

    //Máscara para os campos de valores monetários.
    private final Locale ptBr = new Locale("pt", "BR");
    private TextWatcher mascaraValorMonetarioSaque;
    private TextWatcher mascaraValorMonetarioPagamentoCliente;
    private NumberFormat formatter = NumberFormat.getCurrencyInstance(ptBr);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_cliente);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},00);

        //Inicialização de variáveis.
        this.et_valorSaque = findViewById(R.id.et_valorSaque);
        this.btn_efetuarSaque = findViewById(R.id.btn_efetuarSaque);
        this.btn_voltarAoMenuAnteriorCliente = findViewById(R.id.btn_voltarAoMenuAnteriorCliente);
        this.cv_saque = findViewById(R.id.cv_saque);
        this.cv_notasSacadas = findViewById(R.id.cv_notasSacadas);
        this.cv_saldoInsuficiente = findViewById(R.id.cv_saqueIndisponivel);
        this.btn_voltarAoMenuAnteriorNotasSacadas = findViewById(R.id.btn_voltarAoMenuAnteriorNotasSacadas);
        this.btn_realizarNovoSaque = findViewById(R.id.btn_realizarNovoSaque);
        this.tv_nota100 = findViewById(R.id.tv_nota100);
        this.tv_nota50 = findViewById(R.id.tv_nota50);
        this.tv_nota20 = findViewById(R.id.tv_nota20);
        this.tv_nota10 = findViewById(R.id.tv_nota10);
        this.tv_nota5 = findViewById(R.id.tv_nota5);
        this.tv_nota2 = findViewById(R.id.tv_nota2);
        this.btn_voltarAoMenuAnteriorSaldoInsuficiente = findViewById(R.id.btn_voltarAoMenuAnteriorSaldoInsuficiente);
        this.btn_tentarRealizarNovoSaque = findViewById(R.id.btn_tentarRealizarNovoSaque);
        this.tv_nomeCliente = findViewById(R.id.tv_nomeCliente);
        this.tv_mensagemErroSaque = findViewById(R.id.tv_mensagemErroSaque);
        this.cv_menuPrincipal = findViewById(R.id.cv_menuPrincipal);
        this.btn_saqueMenuPrincipal = findViewById(R.id.btn_SaqueMenuPrincipal);
        this.btn_verSaldoMenuPrincipal = findViewById(R.id.btn_verSaldoMenuPrincipal);
        this.btn_pagamentosMenuPrincipal = findViewById(R.id.btn_pagamentosMenuPrincipal);
        this.btn_minhaLocalizacaoMenuPrincipal = findViewById(R.id.btn_minhaLocalizacaoMenuPrincipal);
        this.btn_obterLocalizacaoCliente = findViewById(R.id.btn_obterLocalizacaoCliente);
        this.cv_minhaLocalizacao = findViewById(R.id.cv_minhaLocalizacao);
        this.wv_googleMaps = findViewById(R.id.wv_googleMaps);
        this.btn_voltarAoMenuAnteriorMinhaLocalizacao = findViewById(R.id.btn_voltarAoMenuAnteriorMinhaLocalizacao);
        this.tv_saldoAtual = findViewById(R.id.tv_saldoAtual);
        this.cv_saldoCliente = findViewById(R.id.cv_saldoCliente);
        this.btn_voltarAoMenuAnteriorSaldoCliente = findViewById(R.id.btn_voltarAoMenuAnteriorSaldoCliente);
        this.cv_pagamentoCliente = findViewById(R.id.cv_pagamentoCliente);
        this.btn_voltarAoMenuAnteriorPagamentoCliente = findViewById(R.id.btn_voltarAoMenuAnteriorPagamentoCliente);
        this.et_codigoDeBarrasPagamentoCliente = findViewById(R.id.et_codigoDeBarrasPagamentoCliente);
        this.ib_cameraPagamentoCliente = findViewById(R.id.ib_cameraPagamentoCliente);
        this.btn_sairMenuPrincipal = findViewById(R.id.btn_sairMenuPrincipal);
        this.btn_efetuarPagamentoBoleto = findViewById(R.id.btn_efetuarPagamentoBoleto);
        this.et_valorPagamentoCliente = findViewById(R.id.et_valorPagamentoCliente);
        this.et_dataVencimentoBoleto = findViewById(R.id.et_dataVencimentoBoleto);
        this.btn_voltarAoMenuAnteriorComprovantePagamento = findViewById(R.id.btn_voltarAoMenuAnteriorComprovantePagamento);
        this.cv_comprovantePagamento = findViewById(R.id.cv_comprovantePagamento);
        this.tv_valorPagadorComprovante = findViewById(R.id.tv_valorPagadorComprovante);
        this.tv_valorPagamentoComprovante = findViewById(R.id.tv_valorPagamentoComprovante);
        this.tv_valorDataVencimentoPagamento = findViewById(R.id.tv_valorDataVencimentoPagamento);
        this.tv_valorDataPagamentoComprovante = findViewById(R.id.tv_valorDataPagamentoComprovante);
        this.tv_valorCodigoBarrasComprovante = findViewById(R.id.tv_valorCodigoBarrasComprovante);
        this.cv_saldoPagamentoIndisponivel = findViewById(R.id.cv_saldoPagamentoIndisponivel);
        this.tv_mensagemErroPagamento = findViewById(R.id.tv_mensagemErroPagamento);
        this.btn_voltarAoMenuAnteriorSaldoPagamentoInsuficiente = findViewById(R.id.btn_voltarAoMenuAnteriorSaldoPagamentoInsuficiente);
        this.btn_tentarRealizarNovoPagamento = findViewById(R.id.btn_tentarRealizarNovoPagamento);

        //Exibir o nome do usuário na tela & pegando os dados da conta.
        this.caixaEletronico = (CaixaEletronico) getIntent().getSerializableExtra("caixaEletronico");
        this.tv_nomeCliente.setText("Bem-Vindo, " + getIntent().getStringExtra("nomeUsuario"));
        this.contaCliente = (Conta) getIntent().getSerializableExtra("contaCliente");
        this.caixaEletronico.setContaCliente(this.contaCliente);

        //Máscaras para os campos de monetários.
        this.mascaraValorMonetarioSaque = new ConvercaoMonetaria(this.et_valorSaque, this.ptBr);
        this.mascaraValorMonetarioPagamentoCliente = new ConvercaoMonetaria(this.et_valorPagamentoCliente, this.ptBr);

        //Botoes de voltar.
        voltarAoMenuAnteriorCliente(btn_voltarAoMenuAnteriorCliente);
        voltarAoMenuAnteriorCliente(btn_voltarAoMenuAnteriorNotasSacadas);
        voltarAoMenuAnteriorCliente(btn_voltarAoMenuAnteriorSaldoInsuficiente);
        voltarAoMenuAnteriorCliente(btn_voltarAoMenuAnteriorMinhaLocalizacao);
        voltarAoMenuAnteriorCliente(btn_voltarAoMenuAnteriorSaldoCliente);
        voltarAoMenuAnteriorCliente(btn_voltarAoMenuAnteriorPagamentoCliente);
        voltarAoMenuAnteriorCliente(btn_voltarAoMenuAnteriorComprovantePagamento);
        voltarAoMenuAnteriorCliente(btn_voltarAoMenuAnteriorSaldoPagamentoInsuficiente);
        voltarParaTelaDeLogin(btn_sairMenuPrincipal);

        //Botoes que necessitam de liberação.
        liberarSaque(et_valorSaque);
        liberarPagamentoBoleto(et_codigoDeBarrasPagamentoCliente);
        liberarPagamentoBoleto(et_dataVencimentoBoleto);
        liberarPagamentoBoleto(et_valorPagamentoCliente);

        //Menu principal:
            //Botão de saque
            btn_saqueMenuPrincipal.setOnClickListener(v -> {
                realizarNovoSaque();
            });

            //Botão de saldo
            btn_verSaldoMenuPrincipal.setOnClickListener(v -> {
                this.esconderCards();
                this.cv_saldoCliente.setVisibility(View.VISIBLE);
            });

            //Botão de pagamentos.
            btn_pagamentosMenuPrincipal.setOnClickListener(v -> {
                this.esconderCards();
                this.cv_pagamentoCliente.setVisibility(View.VISIBLE);
                Util.limparFormatacaoCampo(et_valorPagamentoCliente, mascaraValorMonetarioPagamentoCliente);
                et_dataVencimentoBoleto.setText("");
                et_codigoDeBarrasPagamentoCliente.setText("");
                et_codigoDeBarrasPagamentoCliente.requestFocus(0);
            });

            //Botão de localização.
            btn_minhaLocalizacaoMenuPrincipal.setOnClickListener(v -> {
                this.esconderCards();
                this.cv_minhaLocalizacao.setVisibility(View.VISIBLE);
                Localizacao.encontrarPosicao(this, wv_googleMaps);
            });

        //Menu de Localização.
            //Botão de Obter Localização.
            btn_obterLocalizacaoCliente.setOnClickListener(v -> {
                Localizacao.encontrarPosicao(this, wv_googleMaps);
            });


        //Menu de Saldo.
            //Botão de verificar Saldo.
            btn_verSaldoMenuPrincipal.setOnClickListener(v -> {
                this.esconderCards();
                this.cv_saldoCliente.setVisibility(View.VISIBLE);
                this.tv_saldoAtual.setText(this.formatter.format(this.contaCliente.getSaldoConta()).replace("R$", "R$ "));
            });


        //Menu de saque:
            //Botão efetuar Saque.
            btn_efetuarSaque.setOnClickListener(v -> {
                efetuarSaque();
            });

            //Botão realizar novo saque.
            btn_realizarNovoSaque.setOnClickListener(v -> {
                realizarNovoSaque();
            });

            //Botão realizar novo saque (saldo insuficiente).
            btn_tentarRealizarNovoSaque.setOnClickListener(v -> {
                realizarNovoSaque();
            });

        //Menu de Pagamentos:
            //Exibir calendário para o usuário quando o campo de data vencimento for selecionado.
            Util.exibirCalendario(this, et_dataVencimentoBoleto);

            //Inserir mascara campo data vencimento.
            et_dataVencimentoBoleto.addTextChangedListener(ConvercaoData.mascaraData(et_dataVencimentoBoleto));

            //Botão ler código de barras.
            ib_cameraPagamentoCliente.setOnClickListener(v -> {
                    IntentIntegrator integrator = new IntentIntegrator(this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Leitor de Código de Barras");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.setBeepEnabled(true);
                    integrator.initiateScan();
            });

            //Botão para efetuar pagamento de boleto.
            btn_efetuarPagamentoBoleto.setOnClickListener(v -> {
                efetuarPagamentoBoleto();
            });

            //Saldo insuficiente para pagamento de boleto.
            //Botão para tentar efetuar pagamento de boleto.
            btn_tentarRealizarNovoPagamento.setOnClickListener(v -> {
                btn_pagamentosMenuPrincipal.post(() -> btn_pagamentosMenuPrincipal.performClick());
            });

    }

    //Efetuar pagamento de boleto.
    private void efetuarPagamentoBoleto() {
        Util.esconderTeclado(this);
        String pagador = getIntent().getStringExtra("nomeUsuario");
        String dataPagamento = Util.dataHoraAtual();
        String dataVencimento = et_dataVencimentoBoleto.getText().toString();
        String valorPagamento = ConvercaoMonetaria.retirarSimbolosMonetarios(et_valorPagamentoCliente, this);
        String codigoDeBarras = et_codigoDeBarrasPagamentoCliente.getText().toString();

        if (codigoDeBarras.isEmpty() || dataVencimento.isEmpty() || valorPagamento.isEmpty())
        {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Verificando se tem saldo disponível para pagamento e efetuando o pagamento.
            if(this.caixaEletronico.fazerPagamentoBoleto(valorPagamento)){
                this.tv_valorPagadorComprovante.setText(pagador);
                this.tv_valorDataPagamentoComprovante.setText(dataPagamento);
                this.tv_valorDataVencimentoPagamento.setText(dataVencimento);
                this.tv_valorPagamentoComprovante.setText("R$ " + valorPagamento);
                this.tv_valorCodigoBarrasComprovante.setText(codigoDeBarras);
                this.esconderCards();
                this.cv_comprovantePagamento.setVisibility(View.VISIBLE);
            }
            else
            {
                String respostaPagamento = caixaEletronico.retornarMensagemSaque();
                this.esconderCards();
                this.cv_saldoPagamentoIndisponivel.setVisibility(View.VISIBLE);
                tv_mensagemErroPagamento.setText(respostaPagamento);
            }
        }
    }

    //Definir tela para realizar novo saque.
    private void realizarNovoSaque() {
        this.esconderCards();
        this.cv_saque.setVisibility(View.VISIBLE);
        Util.limparFormatacaoCampo(et_valorSaque, mascaraValorMonetarioSaque);
    }

    //Efetuar saque e exibir notas sacadas.
    private void efetuarSaque() {
        if(et_valorSaque.getText().toString().trim().compareTo("R$0,00") == 0) {
            et_valorSaque.setError("Digite um valor válido!");
        }
        else {
            Util.esconderTeclado(this);
            String valor = et_valorSaque.getText().toString().substring(2).replace(".", "").replace(",", ".");
            if (valor.indexOf(".") != valor.length() - 3) {
                Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
                et_valorSaque.setText(new BigDecimal(0).toString());
            }
            else{
                try {
                    valor = valor.substring(1, valor.length() - 3) + ".00";
                    BigDecimal valorSaque = new BigDecimal(valor);
                    this.esconderCards();
                    this.cv_notasSacadas.setVisibility(View.VISIBLE);

                    tv_nota100.setText("0");
                    tv_nota50.setText("0");
                    tv_nota20.setText("0");
                    tv_nota10.setText("0");
                    tv_nota5.setText("0");
                    tv_nota2.setText("0");

                    LinkedHashMap<Integer, Integer> qtdeNotas = caixaEletronico.sacarValor(valorSaque);
                    String respostaSaque = caixaEletronico.retornarMensagemSaque();
                    if (qtdeNotas != null) {
                        for (Map.Entry<Integer, Integer> nd : qtdeNotas.entrySet()) {
                            switch (nd.getKey()) {
                                case 100:
                                    tv_nota100.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                                    break;
                                case 50:
                                    tv_nota50.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                                    break;
                                case 20:
                                    tv_nota20.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                                    break;
                                case 10:
                                    tv_nota10.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                                    break;
                                case 5:
                                    tv_nota5.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                                    break;
                                case 2:
                                    tv_nota2.setText((nd.getValue().equals(0) ? "0" : nd.getValue().toString()));
                                    break;
                            }
                        }
                    }
                    else {
                        this.esconderCards();
                        this.cv_saldoInsuficiente.setVisibility(View.VISIBLE);
                        tv_mensagemErroSaque.setText(respostaSaque);
                    }
                }
                catch (Exception e) {
                    this.esconderCards();
                    this.cv_saldoInsuficiente.setVisibility(View.VISIBLE);
                    tv_mensagemErroSaque.setText("Não foi possivel realizar o saque no momento.");
                }
            }
        }
    }

    //Liberar saque.
    private void liberarSaque(EditText et){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //sem uso
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString() != "R$0,00"){
                    btn_efetuarSaque.setEnabled(true);
                    btn_efetuarSaque.setBackgroundTintList(getColorStateList(R.color.amarelo_sol));
                }
                else{
                    btn_efetuarSaque.setEnabled(false);
                    btn_efetuarSaque.setBackgroundTintList(getColorStateList(R.color.amarelo_desativado));
                }
            }
            @Override
            public void afterTextChanged(Editable e) {
                //sem uso
            }
        });
    }

    //Liberar pagamento boleto.
    private void liberarPagamentoBoleto(EditText et){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //sem uso
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et_codigoDeBarrasPagamentoCliente.getText().toString() != null && !et_codigoDeBarrasPagamentoCliente.getText().toString().isEmpty()
                        && et_valorPagamentoCliente.getText().toString() != null && !et_valorPagamentoCliente.getText().toString().isEmpty()
                        && et_dataVencimentoBoleto.getText().toString() != null && !et_dataVencimentoBoleto.getText().toString().isEmpty()
                        && et_dataVencimentoBoleto.getText().toString() != "00/00/0000" ){
                    btn_efetuarPagamentoBoleto.setEnabled(true);
                    btn_efetuarPagamentoBoleto.setBackgroundTintList(getColorStateList(R.color.amarelo_sol));
                }
                else{
                    btn_efetuarPagamentoBoleto.setEnabled(false);
                    btn_efetuarPagamentoBoleto.setBackgroundTintList(getColorStateList(R.color.amarelo_desativado));
                }
            }
            @Override
            public void afterTextChanged(Editable e) {
                //sem uso
            }
        });
    }

    //Esconder cards.
    private void esconderCards(){
        this.cv_menuPrincipal.setVisibility(View.GONE);
        this.cv_minhaLocalizacao.setVisibility(View.GONE);
        this.cv_saque.setVisibility(View.GONE);
        this.cv_saldoInsuficiente.setVisibility(View.GONE);
        this.cv_saldoCliente.setVisibility(View.GONE);
        this.cv_notasSacadas.setVisibility(View.GONE);
        this.cv_pagamentoCliente.setVisibility(View.GONE);
        this.cv_comprovantePagamento.setVisibility(View.GONE);
        this.cv_saldoPagamentoIndisponivel.setVisibility(CardView.GONE);
    }

    //Voltar para o menu principal.
    private void voltarAoMenuAnteriorCliente(Button btn) {
        btn.setOnClickListener(v -> {
            Util.esconderTeclado(this);
            this.esconderCards();
            cv_menuPrincipal.setVisibility(View.VISIBLE);
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

    //Método que recebe o resultado do leitor de código de barras.
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {

            String barcode = scanResult.getContents();
            this.et_codigoDeBarrasPagamentoCliente.setText(barcode);
        }
    }
}