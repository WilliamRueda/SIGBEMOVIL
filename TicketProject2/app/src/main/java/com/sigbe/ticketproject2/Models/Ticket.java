package com.sigbe.ticketproject2.Models;

import java.util.Date;

public class Ticket {

    private int consecutivoticket;
    private Date $fechacompra;
    private int $valor;
    private int $estadoticket;
    private String $tipoTicket;
    private Usuario $usuario;

    public int getConsecutivoticket() {
        return consecutivoticket;
    }

    public void setConsecutivoticket(int consecutivoticket) {
        this.consecutivoticket = consecutivoticket;
    }

    public Date get$fechacompra() {
        return $fechacompra;
    }

    public void set$fechacompra(Date $fechacompra) {
        this.$fechacompra = $fechacompra;
    }

    public int get$valor() {
        return $valor;
    }

    public void set$valor(int $valor) {
        this.$valor = $valor;
    }

    public int get$estadoticket() {
        return $estadoticket;
    }

    public void set$estadoticket(int $estadoticket) {
        this.$estadoticket = $estadoticket;
    }

    public String get$tipoTicket() {
        return $tipoTicket;
    }

    public void set$tipoTicket(String $tipoTicket) {
        this.$tipoTicket = $tipoTicket;
    }

    public Usuario get$usuario() {
        return $usuario;
    }

    public void set$usuario(Usuario $usuario) {
        this.$usuario = $usuario;
    }
}
