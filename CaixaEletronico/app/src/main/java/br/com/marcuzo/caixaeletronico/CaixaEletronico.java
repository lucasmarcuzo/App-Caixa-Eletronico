package br.com.marcuzo.caixaeletronico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CaixaEletronico implements Serializable {

    private int[][] valorEmCaixa;
    private BigDecimal saldoCaixa;
    private boolean cedulasDisponiveis;
    private LinkedHashMap<Integer, Integer> notasDisponiveis;
    private String mensagemSaque;
    private BigDecimal cotaMinima;
    private Boolean caixaVazio;
    private Conta contaCliente;

    public CaixaEletronico() {
        this.valorEmCaixa = new int[6][2];
        this.valorEmCaixa[0][0] = 100;
        this.valorEmCaixa[1][0] = 50;
        this.valorEmCaixa[2][0] = 20;
        this.valorEmCaixa[3][0] = 10;
        this.valorEmCaixa[4][0] = 5;
        this.valorEmCaixa[5][0] = 2;
        this.abastecerCaixa();
        this.calcularNotasDisponiveis();
        this.cotaMinima = new BigDecimal("500.00");
        caixaVazio = false;
    }

    //Abastecer caixa.
    public void abastecerCaixa(){
        this.valorEmCaixa[0][1] = 100;
        this.valorEmCaixa[1][1] = 200;
        this.valorEmCaixa[2][1] = 300;
        this.valorEmCaixa[3][1] = 350;
        this.valorEmCaixa[4][1] = 450;
        this.valorEmCaixa[5][1] = 500;
        this.saldoCaixa = new BigDecimal("32750.00"); //32750.00
    }

    //Verifica quais notas estão disponíveis para o saque.
    private LinkedHashMap<Integer, Integer> calcularNotasDisponiveis(){
        this.notasDisponiveis = new LinkedHashMap<>();
        if(valorEmCaixa[0][1] > 0) {
            this.notasDisponiveis.put(valorEmCaixa[0][0], valorEmCaixa[0][1]);//100
        }
        if(valorEmCaixa[1][1] > 0) {
            this.notasDisponiveis.put(valorEmCaixa[1][0], valorEmCaixa[1][1]);//50
        }
        if(valorEmCaixa[2][1] > 0) {
            this.notasDisponiveis.put(valorEmCaixa[2][0], valorEmCaixa[2][1]);//20
        }
        if (valorEmCaixa[3][1] > 0) {
            this.notasDisponiveis.put(valorEmCaixa[3][0], valorEmCaixa[3][1]);//10
        }
        if (valorEmCaixa[4][1] > 0) {
            this.notasDisponiveis.put(valorEmCaixa[4][0], valorEmCaixa[4][1]);//5
        }
        if (valorEmCaixa[5][1] > 0) {
            this.notasDisponiveis.put(valorEmCaixa[5][0], valorEmCaixa[5][1]);//2
        }
        return this.notasDisponiveis;
    }

    //Sacar o valor do cliente.
    public LinkedHashMap<Integer, Integer> sacarValor(BigDecimal valorSaque){

        if(this.contaCliente.getSaldoConta().compareTo(valorSaque) == -1)
        {
            this.mensagemSaque = "Saldo insuficiente.";
            return null;
        }
        else
        {
            //Verificar se o valor em caixa é menor que o valor da cota mínima.
            if(this.saldoCaixa.compareTo(cotaMinima) < 0){
                this.mensagemSaque = "Caixa Vazio: Chame o Operador!";
                this.caixaVazio = true;
                return null;
            }
            else{
                LinkedHashMap<Integer, Integer> notasDisponiveis = calcularNotasDisponiveis();

                //Verificar e armazenar quantas notas são necessárias para o saque e de qual valor.
                ArrayList<Integer> qtdeNotas = new ArrayList<>();
                Integer notas=valorSaque.intValue();

                for(Map.Entry<Integer, Integer> nd : notasDisponiveis.entrySet()){
                    int aux=(int)(notas / (nd.getKey()));
                    notas -= (aux * nd.getKey());
                    qtdeNotas.add(aux);
                }

                if (notas == 1) {
                    if (qtdeNotas.get(4) >= 1) { // caso o valor do saque restante seja 6 ou 8 transforma em notas de 2
                        qtdeNotas.set(4, (qtdeNotas.get(4) - 1));
                        qtdeNotas.set(5, (qtdeNotas.get(5) + 3));
                    } else if (qtdeNotas.get(3)>= 1) { // caso o valor do saque restante seja 11 ou 13 transforma em notas de 2 e 5
                        qtdeNotas.set(3, (qtdeNotas.get(3) - 1));
                        qtdeNotas.set(4, (qtdeNotas.get(4) + 1));
                        qtdeNotas.set(5, (qtdeNotas.get(5) + 3));
                    } else if (qtdeNotas.get(2) >= 1) { // caso o valor do saque restante seja 21 e 23 transforma em notas de 2, 5 e 10
                        qtdeNotas.set(2, (qtdeNotas.get(2) - 1));
                        qtdeNotas.set(3, (qtdeNotas.get(3) + 1));
                        qtdeNotas.set(4, (qtdeNotas.get(4) + 1));
                        qtdeNotas.set(5, (qtdeNotas.get(5) + 3));
                    } else if (qtdeNotas.get(1) >= 1) { // caso o valor do saque restante seja ou 51 ou 53 transforma em notas de 2
                        qtdeNotas.set(1, (qtdeNotas.get(1) - 1));
                        qtdeNotas.set(2, (qtdeNotas.get(2) + 2));
                        qtdeNotas.set(4, (qtdeNotas.get(4) + 1));
                        qtdeNotas.set(5, (qtdeNotas.get(5) + 3));
                    }
                }

                //Verificar se tem notas disponiveis para a realização do saque do valor passado.
                int aux2 = 0;
                for(Map.Entry<Integer, Integer> nd : notasDisponiveis.entrySet()){

                    if(qtdeNotas.get(aux2) > nd.getValue())
                    {
                        this.mensagemSaque = "Não há cédulas disponíveis para o valor solicitado!";
                        cedulasDisponiveis = false;
                        break;
                    }
                    else
                    {
                        this.mensagemSaque = "Saque realizado com sucesso!";
                        cedulasDisponiveis = true;
                    }
                    aux2++;
                }

                //Verificar se o valor do saque é maior que o valor em caixa.
                //Atulizar o valor de quantas notas foram retiradas do caixa.
                int aux3 = 0;;
                if(cedulasDisponiveis){
                    int j = 0;
                    for(Map.Entry<Integer, Integer> nd : notasDisponiveis.entrySet()) {
                        switch (nd.getKey()) {
                            case 100:
                                valorEmCaixa[0][1] -= qtdeNotas.get(j);
                                break;
                            case 50:
                                valorEmCaixa[1][1] -= qtdeNotas.get(j);
                                break;
                            case 20:
                                valorEmCaixa[2][1] -= qtdeNotas.get(j);
                                break;
                            case 10:
                                valorEmCaixa[3][1] -= qtdeNotas.get(j);
                                break;
                            case 5:
                                valorEmCaixa[4][1] -= qtdeNotas.get(j);
                                break;
                            case 2:
                                valorEmCaixa[5][1] -= qtdeNotas.get(j);
                                break;
                            default:
                                valorEmCaixa[j][1] = 0;
                        }
                        j++;
                    }


                    //Atualizar o saldo do caixa.
                    this.setSaldoCaixa(saldoCaixa.subtract(BigDecimal.valueOf(Math.round(valorSaque.doubleValue()))));
                    this.contaCliente.setSaldoConta(this.contaCliente.getSaldoConta().subtract(BigDecimal.valueOf(Math.round(valorSaque.doubleValue()))));

                    //Retornar a quantidade de notas para o saque.
                    LinkedHashMap<Integer, Integer> notasEQuantidade = new LinkedHashMap<>();
                    for(Map.Entry<Integer, Integer> nd : notasDisponiveis.entrySet()) {
                        notasEQuantidade.put(nd.getKey(), qtdeNotas.get(aux3));
                        aux3++;
                    }
                    return notasEQuantidade;
                }
                else{
                    return null;
                }
            }
        }
    }

    //Retornar mensagem de saque realizado ou não.
    public String retornarMensagemSaque() {
        return this.mensagemSaque;
    }

    //Número de notas disponiveis no caixa.
    public LinkedHashMap<Integer, Integer> getNotasDisponiveis() {
        this.calcularNotasDisponiveis();
        return this.notasDisponiveis;
    }


    //Setar notas disponiveis no caixa.
    public void setValorDeNotasEmCaixa(LinkedHashMap<Integer, Integer> valorEmCaixa) {
        int j = 0;
        for (Map.Entry<Integer, Integer> nd : valorEmCaixa.entrySet()) {
            switch (nd.getKey()) {
                case 100:
                    this.valorEmCaixa[0][1] = nd.getValue();
                    break;
                case 50:
                    this.valorEmCaixa[1][1] = nd.getValue();
                    break;
                case 20:
                    this.valorEmCaixa[2][1] = nd.getValue();
                    break;
                case 10:
                    this.valorEmCaixa[3][1] = nd.getValue();
                    break;
                case 5:
                    this.valorEmCaixa[4][1] = nd.getValue();
                    break;
                case 2:
                    this.valorEmCaixa[5][1] = nd.getValue();
                    break;
                default:
                    this.valorEmCaixa[j][1] = 0;
            }
            j++;
        }

    }

    //Valor total do caixa.
    public BigDecimal getSaldoCaixa() {
        return saldoCaixa;
    }

    //Setar valor ao caixa.
    public void setSaldoCaixa(BigDecimal saldoCaixa) {
        this.saldoCaixa = saldoCaixa;
    }

    //Pegar valor da cota minima.
    public BigDecimal getCotaMinima() {
        return cotaMinima;
    }

    //Setar valor da cota minima.
    public void setCotaMinima(BigDecimal cotaMinima) {
        this.cotaMinima = cotaMinima;
    }

    //Verificar cota minima.
    public Boolean getCaixaVazio() {
        if(saldoCaixa.compareTo(cotaMinima) == -1){
            this.caixaVazio = true;
            this.mensagemSaque = "Caixa Vazio: Chame o Operador!";
        }
        else
        {
            this.caixaVazio = false;
        }
        return this.caixaVazio;
    }

    //Pegar os dados da conta do cliente
    public void setContaCliente(Conta contaCliente) {
        this.contaCliente = contaCliente;
    }

    //Método para realizar pagamento de boleto.
    public boolean fazerPagamentoBoleto(String valorPagamento){
        if(this.contaCliente.getSaldoConta().compareTo(new BigDecimal(valorPagamento)) == -1)
        {
            this.mensagemSaque = "Não foi possível realizar o pagamento.\nSaldo insuficiente.";
            return false;
        }
        else
        {
            this.contaCliente.setSaldoConta(this.contaCliente.getSaldoConta().subtract(new BigDecimal(valorPagamento)));
            this.mensagemSaque  = "Pagamento realizado com sucesso.";
            return true;
        }

    }

}
