package com.base.application;

import com.base.mb.controleacesso.SessaoUsuarioMB;
import com.base.modelo.audit.Auditing;
import com.base.modelo.controleacesso.Usuario;
import com.xpert.audit.AbstractAuditingListener;
import com.xpert.audit.model.AbstractAuditing;
import com.xpert.faces.utils.FacesUtils;
import javax.faces.context.FacesContext;

/**
 * Implmentacao do AbstractAuditingListener do xpert-framework, aqui serah
 * definido o usuario que esta realizando a acao, ele deve ser pego do bean de
 * sessao #{sessaoUsuarioMB}
 *
 * @author
 */
public class AuditingListenerImpl implements AbstractAuditingListener {

    /**
     * Metodo a ser chamado na auditoria padrao dos eventos INSERT. UPDATE,
     * DELETE
     *
     * @param abstractAuditing
     */
    @Override
    public void onSave(AbstractAuditing abstractAuditing) {
        Auditing auditing = (Auditing) abstractAuditing;
        auditing.setUsuario(getUser());
    }

    

    public Usuario getUser() {
        if (FacesContext.getCurrentInstance() != null) {
            SessaoUsuarioMB sessaoUsuarioMB = FacesUtils.getBeanByEl("#{sessaoUsuarioMB}");
            if (sessaoUsuarioMB != null) {
                return sessaoUsuarioMB.getUser();
            }
        }
        return null;
    }
}
