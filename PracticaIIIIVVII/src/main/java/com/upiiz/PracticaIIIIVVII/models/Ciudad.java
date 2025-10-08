package com.upiiz.PracticaIIIIVVII.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ciudad",
        uniqueConstraints = @UniqueConstraint(name="uk_ciudad_codigo", columnNames = "codigo"))
public class Ciudad {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Long id;

    @Column(nullable = false, length = 10)
    private String codigo;

    @Column(nullable = false, length = 120)
    private String nombre;

    @OneToMany(mappedBy = "ciudadDestino")
    private Set<Paquete> paquetesDestino = new HashSet<>();

    public Ciudad() {}

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Set<Paquete> getPaquetesDestino() { return paquetesDestino; }
    public void setPaquetesDestino(Set<Paquete> paquetesDestino) { this.paquetesDestino = paquetesDestino; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ciudad)) return false;
        Ciudad other = (Ciudad) o;
        return id != null && id.equals(other.id);
    }
    @Override public int hashCode() { return 31; }

    @Override public String toString() {
        return "Ciudad{id=" + id + ", codigo='" + codigo + "', nombre='" + nombre + "'}";
    }
}
