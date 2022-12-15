package br.com.marcuzo.caixaeletronico;

import java.io.Serializable;
import java.math.BigDecimal;

public class Conta implements Serializable {

    private String nomeCliente;

    public BigDecimal getSaldoConta() {
        return saldoConta;
    }

    public void setSaldoConta(BigDecimal saldoConta) {
        this.saldoConta = saldoConta;
    }

    private BigDecimal saldoConta;

    public Conta(String nomeCliente) {
        this.nomeCliente = nomeCliente;
        if(nomeCliente.equals("jose")){
            this.saldoConta = new BigDecimal("10000.00");
        }
        else if(nomeCliente.equals("maria")){
            this.saldoConta = new BigDecimal("5000.00");
        }
        else if(nomeCliente.equals("joao")){
            this.saldoConta = new BigDecimal("20000.00");
        }
        else{
            this.saldoConta = new BigDecimal("30000.00");
        }


    }
}
