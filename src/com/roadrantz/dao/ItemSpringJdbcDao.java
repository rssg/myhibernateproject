package com.roadrantz.dao;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.context.ApplicationContext;

import standalone.beans.Category;
import standalone.beans.Item;
import standalone.beans.Resource;

public class ItemSpringJdbcDao extends SimpleJdbcDaoSupport implements ItemDao{
	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final String ITEM_INSERT = "INSERT INTO au_items (itemId, categoryId, displayName, " +
			"description, price, priceCurrency, sellerId, originCountry, resourceId) " +
			"values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_NEXT_ITEM_ID = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name='au_items'";
	
	public ItemSpringJdbcDao()
	{
		
	}
	public ItemSpringJdbcDao(ApplicationContext ac)
	{
		this.setJdbcTemplate((JdbcTemplate)ac.getBean("jdbcTemplate"));
	}

	@Override
	public int insertItem(Item item, ResourceDao resDao) throws IOException {
		// Add Resource First
		int resourceId = resDao.insertResource(new Resource(0, item.getFile().getOriginalFilename(), item.getFile().getContentType(), item.getFile().getBytes()));
		
		System.out.println("Adding Item to db");
		int nextItemId = getNextItemId();
		int rowsAffected = getJdbcTemplate().update(ITEM_INSERT, new Object[] {
				/* itemId - not required. Its a sequence ,*/
				java.sql.Types.NULL,
				item.getCategoryId(), 
				item.getDisplayName(), 
				item.getDescription(),
				item.getPrice(),
				item.getPriceCurrency(),
				item.getSellerId()==null?"test":item.getSellerId(),
				item.getOriginCountry()==null?"SG":item.getOriginCountry(),
				resourceId
				});
		item.setItemId(getJdbcTemplate().queryForInt( "select last_insert_id()" ));
		return rowsAffected;
	}
	
	@Override
	public int getNextItemId()
	{
		List<Integer> itemList = getJdbcTemplate().query(GET_NEXT_ITEM_ID,
				new Object[] { },
				new RowMapper() {
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException
				{	
					Integer nextItemId = new Integer(rs.getInt("AUTO_INCREMENT")); 
					return nextItemId;
				}
					});
		return itemList.get(0).intValue();		
	}
}