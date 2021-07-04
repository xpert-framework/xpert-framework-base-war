package com.base.dao.audit.impl;

import com.base.application.BaseDAOImpl;
import com.base.dao.audit.QueryAuditingDAO;
import com.base.modelo.audit.QueryAuditing;
import javax.ejb.Stateless;

/**
 *
 * @author Ayslan
 */
@Stateless
public class QueryAuditingDAOImpl extends BaseDAOImpl<QueryAuditing> implements QueryAuditingDAO {

    @Override
    public Class getEntityClass() {
        return QueryAuditing.class;
    }
    
    
    
}
