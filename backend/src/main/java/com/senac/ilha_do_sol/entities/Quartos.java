package com.senac.ilha_do_sol.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of="id")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quartos")
public class Quartos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private String numeroQuarto;
    private int capacidadeMax;
    private int camas;
    private double valor;
    private String imageUrl;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

}
