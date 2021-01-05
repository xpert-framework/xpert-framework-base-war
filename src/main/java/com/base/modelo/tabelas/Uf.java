package com.base.modelo.tabelas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author ayslanms
 */
@Entity
public class Uf implements Serializable {

    @Id
    @SequenceGenerator(name = "Uf", allocationSize = 1, sequenceName = "uf_id_seq")
    @GeneratedValue(generator = "Uf")
    private Long id;

    @Column(unique = true)
    @NotNull
    private Long codigoIbge;

    @Size(max = 200)
    @NotBlank
    private String nome;

    @Column(unique = true)
    @Size(max = 2)
    @NotBlank
    private String sigla;

    @NotNull
    private Long codigoIbgeRegiao;

    @Size(max = 200)
    @NotBlank
    private String nomeRegiao;

    @Column(precision = 15, scale = 8)
    private BigDecimal longitude;

    @Column(precision = 15, scale = 8)
    private BigDecimal latitude;

    @OrderBy("nome")
    @OneToMany(mappedBy = "uf")
    private List<Municipio> municipios;

    public Uf() {
    }

    public Uf(Long codigoIbge, String nome, String sigla, Long codigoIbgeRegiao, String nomeRegiao, String latitude, String longitude) {
        this.codigoIbge = codigoIbge;
        this.nome = nome == null ? nome : nome.trim().toUpperCase();
        this.sigla = sigla == null ? sigla : sigla.trim().toUpperCase();
        this.codigoIbgeRegiao = codigoIbgeRegiao;
        this.nomeRegiao = nomeRegiao == null ? nomeRegiao : nomeRegiao.trim().toUpperCase();
        this.latitude = new BigDecimal(latitude);
        this.longitude = new BigDecimal(longitude);
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(Long codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome != null) {
            nome = nome.toUpperCase().trim();
        }
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        if (sigla != null) {
            sigla = sigla.toUpperCase().trim();
        }
        this.sigla = sigla;
    }

    public List<Municipio> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
    }

    public Long getCodigoIbgeRegiao() {
        return codigoIbgeRegiao;
    }

    public void setCodigoIbgeRegiao(Long codigoIbgeRegiao) {
        this.codigoIbgeRegiao = codigoIbgeRegiao;
    }

    public String getNomeRegiao() {
        return nomeRegiao;
    }

    public void setNomeRegiao(String nomeRegiao) {
        if (nomeRegiao != null) {
            nomeRegiao = nomeRegiao.toUpperCase().trim();
        }
        this.nomeRegiao = nomeRegiao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Uf other = (Uf) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return sigla;
    }

}
