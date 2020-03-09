package com.sumavision.tetris.zoom.contacts;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ContactsMessagePO.class, idClass = Long.class)
public interface ContactsMessageDAO extends BaseDAO<ContactsMessagePO>{

}
