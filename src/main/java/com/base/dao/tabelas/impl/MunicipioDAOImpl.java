package com.base.dao.tabelas.impl;

import com.base.application.BaseDAOImpl;
import com.base.dao.tabelas.MunicipioDAO;
import com.base.modelo.tabelas.Municipio;
import javax.ejb.Stateless;

/**
 *
 * @author ayslanms
 */
@Stateless
public class MunicipioDAOImpl extends BaseDAOImpl<Municipio> implements MunicipioDAO {

    @Override
    public Class getEntityClass() {
        return Municipio.class;
    }

}
