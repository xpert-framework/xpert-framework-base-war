package com.base.dao.audit;

import com.base.modelo.audit.QueryAuditing;
import com.xpert.persistence.dao.BaseDAO;
import javax.ejb.Local;

/**
 *
 * @author Ayslan
 */
@Local
public interface QueryAuditingDAO extends BaseDAO<QueryAuditing> {
    
}
