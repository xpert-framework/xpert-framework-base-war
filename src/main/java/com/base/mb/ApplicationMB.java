package com.base.mb;

import com.base.GeracaoDadosSistema;
import com.xpert.template.Icons;
import com.xpert.template.Template;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Named;
import javax.servlet.ServletContext;

/**
 * Como esse managed bean tem o escopo de "Application" e a opcao "eager = true"
 * o metodo "@PostConstruct" sera chamado ao iniciar a aplicacao
 *
 * @author ayslan
 */
@Named
@ApplicationScoped
public class ApplicationMB {

    @EJB
    private GeracaoDadosSistema geracaoDadosSistema;

    public void init(@Observes @Initialized(ApplicationScoped.class) ServletContext payload) {
        //gerar permissoes ao iniciar aplicacao
        geracaoDadosSistema.generate();

        configurarTemplate();
    }

    public void configurarTemplate() {

        /**
         * configurar icones.
         *
         * Os icones sao acessados por #{icons.edit}, #{icons.audit}
         *
         */
        Icons icons = Template.icons();
        icons.fontAwesome();

        /**
         * To add custom icons:
         * <pre>
         * icons.put("teste", "fas fa-teste");
         * icons.put("teste-1", "fas fa-teste-1");
         * </pre>
         *
         */
    }
}
