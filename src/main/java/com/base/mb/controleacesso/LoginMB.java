package com.base.mb.controleacesso;

import com.base.bo.controleacesso.AcessoSistemaBO;
import com.base.dao.controleacesso.UsuarioDAO;
import com.base.modelo.controleacesso.AcessoSistema;
import com.base.modelo.controleacesso.Usuario;
import com.xpert.i18n.I18N;
import com.xpert.security.bean.SecurityLoginBean;
import com.xpert.security.model.User;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

/**
 *
 * @author Ayslan
 */
@Named
@RequestScoped
public class LoginMB extends SecurityLoginBean {

    @EJB
    private UsuarioDAO usuarioDAO;
    @EJB
    private AcessoSistemaBO acessoSistemaBO;
    @Inject
    private SessaoUsuarioMB sessaoUsuarioMB;

    @Override
    public void login() {
        super.login();
    }

    @Override
    public String getRedirectPageWhenSucess() {
        return "/view/home.jsf";
    }

    @Override
    public String getRedirectPageWhenLogout() {
        return "/index.jsf";
    }

    @Override
    public String getUserNotFoundMessage() {
        return I18N.get("business.usuarioOuSenhaInvalidos");
    }

    @Override
    public String getInactiveUserMessage() {
        return I18N.get("business.usuarioInativo");
    }

    @Override
    public EntityManager getEntityManager() {
        return usuarioDAO.getEntityManager();

    }

    @Override
    public boolean isLoginUpperCase() {
        return true;
    }

    @Override
    public boolean isValidateWhenNoRolesFound() {
        return false;
    }

    /**
     * Registrar o acesso do usuario atraves da classe AcessoSistema
     * 
     * @see AcessoSistema
     * @param user 
     */
    @Override
    public void onSucess(User user) {
        Usuario usuario = (Usuario) user;
        acessoSistemaBO.save(usuario);
    }

    @Override
    public Class getUserClass() {
        return Usuario.class;
    }

    @Override
    public SessaoUsuarioMB getUserSession() {
        return getSessaoUsuarioMB();
    }

    public SessaoUsuarioMB getSessaoUsuarioMB() {
        return sessaoUsuarioMB;
    }

    public void setSessaoUsuarioMB(SessaoUsuarioMB sessaoUsuarioMB) {
        this.sessaoUsuarioMB = sessaoUsuarioMB;
    }
}
