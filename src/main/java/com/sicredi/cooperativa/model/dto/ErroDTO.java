package com.sicredi.cooperativa.model.dto;

public class ErroDTO {

    private String mensagemUsuario;
    private String mensagemDesenvolvedor;

    public static ErroDTO buildMensagemErroSomenteUsuario(String mensagem) {
        ErroDTO erroDTO = new ErroDTO();
        erroDTO.setMensagemUsuario(mensagem);

        return erroDTO;
    }

    public static ErroDTO buildMensagemErro(String mensagemUsuario, String mensagemDesenvolvedor) {
        ErroDTO erroDTO = new ErroDTO();

        erroDTO.setMensagemUsuario(mensagemUsuario);
        erroDTO.setMensagemDesenvolvedor(mensagemDesenvolvedor);

        return erroDTO;
    }

    public String getMensagemUsuario() {
        return mensagemUsuario;
    }

    public void setMensagemUsuario(String mensagemUsuario) {
        this.mensagemUsuario = mensagemUsuario;
    }

    public String getMensagemDesenvolvedor() {
        return mensagemDesenvolvedor;
    }

    public void setMensagemDesenvolvedor(String mensagemDesenvolvedor) {
        this.mensagemDesenvolvedor = mensagemDesenvolvedor;
    }

}
