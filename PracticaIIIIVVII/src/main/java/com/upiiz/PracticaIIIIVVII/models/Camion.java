package com.upiiz.PracticaIIIIVVII.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "camion",
        uniqueConstraints = @UniqueConstraint(name="uk_camion_matricula", columnNames = "matricula"))
public class Camion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_camion")
    private Long id;

    @Column(nullable = false, length = 20)
    private String matricula;

    @Column(length = 50)
    private String modelo;

    @Column(length = 30)
    private String potencia;

    @Column(length = 30)
    private String tipo;

    @ManyToMany(mappedBy = "camiones")
    private Set<Camionero> conductores = new HashSet<>();

    public Camion() {}

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getPotencia() { return potencia; }
    public void setPotencia(String potencia) { this.potencia = potencia; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Set<Camionero> getConductores() { return conductores; }
    public void setConductores(Set<Camionero> conductores) { this.conductores = conductores; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Camion)) return false;
        Camion other = (Camion) o;
        return id != null && id.equals(other.id);
    }
    @Override public int hashCode() { return 31; }

    @Override public String toString() {
        return "Camion{id=" + id + ", matricula='" + matricula + "'}";
    }
}
