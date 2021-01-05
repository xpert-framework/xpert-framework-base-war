package com.base.filter;

import com.base.mb.controleacesso.SessaoUsuarioMB;
import com.xpert.security.filter.AbstractSecurityFilter;
import com.xpert.security.session.AbstractUserSession;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.CDI;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebFilter;

/**
 * Filtro para controle de acesso de usuarios
 *
 * @author Ayslan
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = {SecurityFilter.PATTERN})
public class SecurityFilter extends AbstractSecurityFilter {

    private static final Logger logger = Logger.getLogger(SecurityFilter.class.getName());
    /**
     * tudo que estiver dentro desta pasta necessita de controle de acesso
     */
    public static final String PATTERN = "/view/*";
    private static final String HOME = "/index.jsf";
    /**
     * lista de URLs a serem ignoradas pelo controle de acesso
     */
    private static final String[] IGNORE_URL = {"/view/home.jsf"};

    @Override
    public AbstractUserSession getSessionBean(ServletRequest request) {
        return CDI.current().select(SessaoUsuarioMB.class).get();
    }

    @Override
    public String getUserSessionName() {
        return "sessaoUsuarioMB";
    }

    @Override
    public String getHomePage() {
        return HOME;
    }

    @Override
    public String[] getIgnoredUrls() {
        return IGNORE_URL;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.log(Level.INFO, "Iniciando Filter SecurityFilter para url: {0}", PATTERN);
        logger.log(Level.INFO, "URLs ignoradas para controle de acesso: {0}", IGNORE_URL);
        logger.log(Level.INFO, "Pagina Inicial: {0}", HOME);
    }

    @Override
    public void destroy() {
    }
}
