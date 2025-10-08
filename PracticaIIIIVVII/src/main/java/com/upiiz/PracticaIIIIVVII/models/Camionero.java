package com.upiiz.PracticaIIIIVVII.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "camionero",
        uniqueConstraints = @UniqueConstraint(name="uk_camionero_rfc", columnNames = "rfc"))
public class Camionero {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_camionero")
    private Long id;

    @NotBlank @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank @Column(nullable = false, length = 150)
    private String apellidos;

    @NotBlank @Column(nullable = false, length = 13)
    private String rfc;

    @Column(length = 20)
    private String telefono;

    @Column(length = 200)
    private String direccion;

    @DecimalMin("0.0") @Digits(integer = 10, fraction = 2)
    private BigDecimal salario;

    // N:M con Camion vía tabla "conduce"
    @ManyToMany
    @JoinTable(name = "conduce",
            joinColumns = @JoinColumn(name = "id_camionero", foreignKey = @ForeignKey(name="fk_conduce_camionero")),
            inverseJoinColumns = @JoinColumn(name = "id_camion", foreignKey = @ForeignKey(name="fk_conduce_camion")))
    private Set<Camion> camiones = new HashSet<>();

    // 1:N con Paquete
    @OneToMany(mappedBy = "camionero")
    private Set<Paquete> paquetes = new HashSet<>();

    public Camionero() {}

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
    public Set<Camion> getCamiones() { return camiones; }
    public void setCamiones(Set<Camion> camiones) { this.camiones = camiones; }
    public Set<Paquete> getPaquetes() { return paquetes; }
    public void setPaquetes(Set<Paquete> paquetes) { this.paquetes = paquetes; }

    // Helpers para sincronizar ambos lados
    public void addCamion(Camion c) { this.camiones.add(c); c.getConductores().add(this); }
    public void removeCamion(Camion c) { this.camiones.remove(c); c.getConductores().remove(this); }
    public void addPaquete(Paquete p) { this.paquetes.add(p); p.setCamionero(this); }

    // equals/hashCode por id (patrón recomendado por Hibernate)
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Camionero)) return false;
        Camionero other = (Camionero) o;
        return id != null && id.equals(other.id);
    }
    @Override public int hashCode() { return 31; }

    @Override public String toString() {
        return "Camionero{id=" + id + ", rfc='" + rfc + "', nombre='" + nombre + " " + apellidos + "'}";
    }
}
