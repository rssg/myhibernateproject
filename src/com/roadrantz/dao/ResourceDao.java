package com.roadrantz.dao;

import org.springframework.jdbc.support.lob.LobHandler;

import standalone.beans.Item;
import standalone.beans.Resource;

public interface ResourceDao {
	int insertResource(final Resource res);
	byte[] getResource(String resourceId);
}
