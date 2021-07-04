package com.base.modelo.audit;

import com.base.modelo.controleacesso.Usuario;
import com.xpert.audit.model.AbstractQueryAuditing;
import com.xpert.i18n.I18N;
import com.xpert.utils.StringUtils;
import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Ayslan
 */
@Entity
public class QueryAuditing extends AbstractQueryAuditing implements Serializable {

    @Id
    @SequenceGenerator(name = "QueryAuditing", allocationSize = 1, sequenceName = "queryauditing_id_seq")
    @GeneratedValue(generator = "QueryAuditing")
    private Long id;

    @ManyToOne
    private Usuario usuario;

    public String getEntityName() {
        if (getEntity() != null) {
            return I18N.get(StringUtils.getLowerFirstLetter(getEntity()));
        }
        return null;
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
    public String getUserName() {
        if (usuario != null) {
            return usuario.getNome();
        }
        return null;
    }
}
