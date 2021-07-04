package com.base.modelo.audit;

import com.base.modelo.controleacesso.Usuario;
import com.xpert.audit.model.AbstractAuditing;
import com.xpert.i18n.I18N;
import com.xpert.utils.StringUtils;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Ayslan
 */
@Entity
public class Auditing extends AbstractAuditing implements Serializable {

    @Id
    @SequenceGenerator(name = "Auditing", allocationSize = 1, sequenceName = "auditing_id_seq")
    @GeneratedValue(generator = "Auditing")
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auditing")
    private List<Metadata> metadatas;

    public String getIcon() {
        switch (getAuditingType()) {
            case INSERT:
                return "fas fa-save";
            case UPDATE:
                return "fas fa-edit";
            case DELETE:
                return "fa fa-trash";
        }
        return null;
    }

    public String getEntityName() {
        return I18N.get(StringUtils.getLowerFirstLetter(getEntity()));
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public List getMetadatas() {
        return metadatas;
    }

    @Override
    public void setMetadatas(List metadatas) {
        this.metadatas = metadatas;
    }

    @Override
    public String getUserName() {
        if (usuario != null) {
            return usuario.getNome();
        }
        return null;
    }
}
