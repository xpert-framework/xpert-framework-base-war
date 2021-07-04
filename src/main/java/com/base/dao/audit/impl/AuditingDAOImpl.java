package com.base.dao.audit.impl;

import com.base.application.BaseDAOImpl;
import com.base.dao.audit.AuditingDAO;
import com.base.modelo.audit.Auditing;
import javax.ejb.Stateless;

/**
 *
 * @author Ayslan
 */
@Stateless
public class AuditingDAOImpl extends BaseDAOImpl<Auditing> implements AuditingDAO {

    @Override
    public Class getEntityClass() {
        return Auditing.class;
    }

}
