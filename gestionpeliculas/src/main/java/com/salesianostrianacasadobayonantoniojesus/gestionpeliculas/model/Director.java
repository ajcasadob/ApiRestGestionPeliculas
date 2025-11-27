package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Director {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nombre;


    private Integer anioNacimiento;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "director")
    private Set<Pelicula > peliculas = new HashSet<>();

    private static final int EDAD_MINIMA = 18;

    public int calcularEdad(int anioActual) {
        return anioActual - this.anioNacimiento;
    }

    public boolean esMayorDeEdad(int anioActual) {
        return calcularEdad(anioActual) >= EDAD_MINIMA;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Director director = (Director) o;
        return getId() != null && Objects.equals(getId(), director.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
