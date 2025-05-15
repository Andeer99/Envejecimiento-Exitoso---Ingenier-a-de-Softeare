package com.IngdeSoftware.EnvejecimientoExitoso.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    /* === roles almacenados como cadenas: ADMIN, CLIENTE, … === */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "role")
    private Set<String> roles;

    /* ----------  MÉTODO HELPER NUEVO  ---------- */
    /** Devuelve el primer rol o «CLIENTE» si el set está vacío */
    public String getMainRole() {
        return roles == null || roles.isEmpty()
                ? "CLIENTE"
                : roles.iterator().next();
    }

    /* (opcional) helper para añadir un rol sin exponer el set */
    public void addRole(String role) {
        roles.add(role);
    }

    /* ----------  getters / setters  ---------- */
    public Long getId()          { return id; }
    public void setId(Long id)   { this.id = id; }

    public String getEmail()     { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword()  { return password; }
    public void setPassword(String pw) { this.password = pw; }

    public Set<String> getRoles()            { return roles; }
    public void setRoles(Set<String> roles)  { this.roles = roles; }
}
