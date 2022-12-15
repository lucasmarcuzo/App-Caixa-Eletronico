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

public class LoginActivity extends AppCompatActivity {

    // Declaração de variáveis.
    EditText et_usuario;
    EditText et_senha;
    Button btn_login;
    Button btn_sair;
    CaixaEletronico caixaEletronico;
    Button btn_voltarAoMenuPrincipalCotaMinima;
    TextView tv_mensagemErroCotaMinima;
    CardView cv_cotaMinima;
    CardView cv_login;
    Conta contaCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicialização de variáveis.
        this.et_usuario = findViewById(R.id.et_usuario);
        this.et_senha = findViewById(R.id.et_senha);
        this.btn_login = findViewById(R.id.btn_login);
        this.btn_sair = findViewById(R.id.btn_voltarAoMenuPrincipalMainActivity);
        this.btn_voltarAoMenuPrincipalCotaMinima = findViewById(R.id.btn_voltarAoMenuAnteriorCotaMinima);
        this.tv_mensagemErroCotaMinima = findViewById(R.id.tv_mensagemErroCotaMinima);
        this.cv_cotaMinima = findViewById(R.id.cv_cotaMinima);
        this.cv_login = findViewById(R.id.cv_login);

        //Iniciar caixa eletrônico.
        //Abastecer somente se não tiver sido abastecido.
        try {
            this.caixaEletronico = (CaixaEletronico) getIntent().getSerializableExtra("caixaEletronico");
            if(this.caixaEletronico == null) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            this.caixaEletronico = new CaixaEletronico();
            verificarCotaMinima();
        }

        //Botão voltar ao menu principal (cota minima)
        btn_voltarAoMenuPrincipalCotaMinima.setOnClickListener(v -> {
            cv_login.setVisibility(View.VISIBLE);
            cv_cotaMinima.setVisibility(View.INVISIBLE);
        });

        liberarLogin(et_usuario);
        liberarLogin(et_senha);

        //Sair do aplicativo.
        btn_sair.setOnClickListener(v -> {
            sair();
        });

        //Acessar módulos.
        btn_login.setOnClickListener(v -> {
            if(et_usuario.getText().toString().trim().isEmpty()){
                et_usuario.setError("Campo obrigatório");
            }
            else if(et_senha.getText().toString().trim().isEmpty()){
                et_senha.setError("Campo obrigatório");
            }
            else if (et_usuario.getText().toString().equals("admin") && et_senha.getText().toString().equals("admin")) {
                abrirModuloAdm(this.caixaEletronico);
            }
            else if (et_usuario.getText().toString().equals("usuario") && et_senha.getText().toString().equals("usuario")
                    || et_usuario.getText().toString().equals("jose") && et_senha.getText().toString().equals("jose")
                    || et_usuario.getText().toString().equals("maria") && et_senha.getText().toString().equals("maria")
                    || et_usuario.getText().toString().equals("joao") && et_senha.getText().toString().equals("joao"))
            {
                    this.contaCliente = new Conta(et_usuario.getText().toString());
                    abrirModuloCliente(this.caixaEletronico, this.contaCliente);
            }
            else{
                et_usuario.setError("Usuário ou senha inválidos");
                et_senha.setError("Usuário ou senha inválidos");
            }

        });
    }

    //Sair do aplicativo.
    public void sair() {
        finish();
    }

    //Liberar login.
    private void liberarLogin(EditText et){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //sem uso
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_login.setEnabled(!et_usuario.getText().toString().trim().isEmpty()
                                   && !et_senha.getText().toString().trim().isEmpty());

                if(et_usuario.getText().toString().equals("admin") && et_senha.getText().toString().equals("admin"))
                {
                    btn_login.setEnabled(true);
                    btn_login.setFocusable(true);
                    btn_login.setText(R.string.modulo_adm);
                    btn_login.setBackgroundTintList(getColorStateList(R.color.azul_fundo));
                }
                else
                {
                    btn_login.setEnabled(true);
                    btn_login.setFocusable(true);
                    btn_login.setText(R.string.modulo_cliente);
                    if(btn_login.isEnabled()){
                        btn_login.setBackgroundTintList(getColorStateList(R.color.amarelo_sol));
                    }
                    else{
                        btn_login.setBackgroundTintList(getColorStateList(R.color.amarelo_desativado));
                    }
                }

            }
            @Override
            public void afterTextChanged(Editable e) {
                //sem uso
            }
        });
    }

    //Abrir módulo administrador.
    public void abrirModuloAdm(CaixaEletronico caixaEletronico) {
        this.finish();
        Intent intent = new Intent(this, ModuloAdministrativoActivity.class);
        intent.putExtra("nomeAdm", et_usuario.getText().toString());
        intent.putExtra("caixaEletronicoAdm", caixaEletronico);
        startActivity(intent);
    }

    //Abrir módulo cliente.
    public void abrirModuloCliente(CaixaEletronico caixaEletronico, Conta contaCliente) {
        this.finish();
        Intent intent = new Intent(this, ModuloClienteActivity.class);
        intent.putExtra("nomeUsuario", et_usuario.getText().toString());
        intent.putExtra("caixaEletronico", caixaEletronico);
        intent.putExtra("contaCliente", contaCliente);
        startActivity(intent);
    }

    //Verificar se o valor em caixa está abaixo da cota mínima.
    public void verificarCotaMinima() {
        //Verificar se o caixa eletrônico está abaixo da cota mínima.
        if(this.caixaEletronico.getCaixaVazio()) {
            String respostaSaque = caixaEletronico.retornarMensagemSaque();
            cv_login.setVisibility(View.INVISIBLE);
            cv_cotaMinima.setVisibility(View.VISIBLE);
            tv_mensagemErroCotaMinima.setText(respostaSaque);
        }
    }
}