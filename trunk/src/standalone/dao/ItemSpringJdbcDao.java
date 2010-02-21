package standalone.dao;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class ItemSpringJdbcDao extends SimpleJdbcDaoSupport implements ItemDao{
	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final String ITEM_INSERT = "INSERT INTO au_items (categoryId, displayName, " +
			"description, price, priceCurrency, sellerId, originCountry, resourceId) " +
			"values (?, ?, ?, ?, ?, ?, ?, ?)";
	
	public ItemSpringJdbcDao()
	{
		
	}
	public ItemSpringJdbcDao(ApplicationContext ac)
	{
		this.setJdbcTemplate((JdbcTemplate)ac.getBean("jdbcTemplate"));
	}

	@Override
	public int insertItem(Item item) {
		// TODO Auto-generated method stub
		int rowsAffected = getJdbcTemplate().update(ITEM_INSERT, new Object[] {
				/* itemId - not required. Its a sequence ,*/
				item.getCategoryId(), 
				item.getDisplayName(), 
				item.getDescription(),
				item.getPrice(),
				item.getPriceCurrency(),
				item.getSellerId()==null?"test":item.getSellerId(),
				item.getOriginCountry()==null?"SG":item.getOriginCountry(),
				item.getResourceId()==null?"100000":item.getResourceId()
				});
		return rowsAffected;
	}
}