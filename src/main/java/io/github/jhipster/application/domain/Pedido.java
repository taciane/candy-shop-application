package io.github.jhipster.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Pedido.
 */
@Entity
@Table(name = "pedido")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "data")
    private LocalDate data;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "pedido_produto",
               joinColumns = @JoinColumn(name = "pedidos_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "produtos_id", referencedColumnName = "id"))
    private Set<Produto> produtos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("pedidos")
    private Usuario usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public Pedido data(LocalDate data) {
        this.data = data;
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Set<Produto> getProdutos() {
        return produtos;
    }

    public Pedido produtos(Set<Produto> produtos) {
        this.produtos = produtos;
        return this;
    }

    public Pedido addProduto(Produto produto) {
        this.produtos.add(produto);
        produto.getPedidos().add(this);
        return this;
    }

    public Pedido removeProduto(Produto produto) {
        this.produtos.remove(produto);
        produto.getPedidos().remove(this);
        return this;
    }

    public void setProdutos(Set<Produto> produtos) {
        this.produtos = produtos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Pedido usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pedido pedido = (Pedido) o;
        if (pedido.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pedido.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pedido{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            "}";
    }
}
