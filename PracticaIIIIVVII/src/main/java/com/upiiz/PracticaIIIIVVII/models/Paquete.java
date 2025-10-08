package com.upiiz.PracticaIIIIVVII.models;

import jakarta.persistence.*;

@Entity
@Table(name = "paquete",
        uniqueConstraints = @UniqueConstraint(name="uk_paquete_codigo", columnNames = "codigo"))
public class Paquete {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paquete")
    private Long id;

    @Column(nullable = false, length = 30)
    private String codigo;

    @Column(length = 300)
    private String descripcion;

    @Column(length = 150)
    private String destinatario;

    @Column(length = 200)
    private String direccion;

    // (1,1) Distribuye -> ManyToOne obligatorio
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_camionero",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_paquete_camionero"))
    private Camionero camionero;

    // (1,1) Destino -> ManyToOne obligatorio
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ciudad_destino",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_paquete_ciudad"))
    private Ciudad ciudadDestino;

    public Paquete() {}

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Camionero getCamionero() { return camionero; }
    public void setCamionero(Camionero camionero) { this.camionero = camionero; }
    public Ciudad getCiudadDestino() { return ciudadDestino; }
    public void setCiudadDestino(Ciudad ciudadDestino) { this.ciudadDestino = ciudadDestino; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paquete)) return false;
        Paquete other = (Paquete) o;
        return id != null && id.equals(other.id);
    }
    @Override public int hashCode() { return 31; }

    @Override public String toString() {
        return "Paquete{id=" + id + ", codigo='" + codigo + "'}";
    }
}
