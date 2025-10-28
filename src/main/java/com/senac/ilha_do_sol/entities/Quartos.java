package com.senac.ilha_do_sol.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of="id")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "quartos")
public class Quartos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(unique = true, nullable = false)
    private String numeroQuarto;

    @Column(nullable = false)
    private int capacidadeMax;

    @Column(nullable = false)
    private int camas;

    @Column(nullable = false)
    private double valor;

    private String imageUrl;

}
