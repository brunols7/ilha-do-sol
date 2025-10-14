package com.senac.ilha_do_sol.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Endereco {

    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
    private String numero;
    private String cep;

}
