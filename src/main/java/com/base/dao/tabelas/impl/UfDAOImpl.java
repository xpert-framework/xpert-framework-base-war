package com.base.dao.tabelas.impl;

import com.base.application.BaseDAOImpl;
import com.base.dao.tabelas.UfDAO;
import com.base.modelo.tabelas.Uf;
import javax.ejb.Stateless;

/**
 *
 * @author ayslanms
 */
@Stateless
public class UfDAOImpl extends BaseDAOImpl<Uf> implements UfDAO {

    @Override
    public Class getEntityClass() {
        return Uf.class;
    }

}
