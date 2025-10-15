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
@Table(name = "reservas")
public class Reservas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @OneToOne
    @JoinColumn(name = "quarto_id", nullable = false)
    private Quartos quarto;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime dataCheckIn;
    private LocalDateTime dataCheckOut;
    private LocalDateTime createdAt;

}
