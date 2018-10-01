package com.briup.environment.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Properties;

import com.briup.environment.backup.BackUp;
import com.briup.environment.bean.Environment;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.ConfigurationAware;
import com.briup.environment.util.WossModel;

public class DBStore implements ConfigurationAware,WossModel{
	private String driver;
	private String url;
	private String user;
	private String password;
	private String sql;
	private int batchsize;
	private String backdbstore;
	private PreparedStatement preparedStatement;
	private BackUp backup;

	@Override
	public void init(Properties prop) throws Exception {
		 driver = prop.getProperty("driver");
		 url=prop.getProperty("url");
		 user=prop.getProperty("user");
		 password=prop.getProperty("password");
		 backdbstore=prop.getProperty("backdbstore");
		 batchsize=Integer.parseInt(prop.getProperty("batchsize"));
	}

	@Override
	public void setConfiguration(Configuration config) {
		backup=config.getBackUp();
		
	}
	public void saveDB(Collection<Environment> list) throws Exception {
		//1.注册驱动
		Class.forName(driver);
		//2.获取数据库连接对象
		Connection connection =DriverManager.getConnection(url, user, password);
		//3.创建数据库的PreparedStatement
		 int count=0;
		 int day=0;
		 connection.setAutoCommit(false);
		 if(backup.load(backdbstore)!=null){
				Collection<Environment>ocollection=(Collection<Environment>) backup.load(backdbstore);
				backup.deleteBackup(backdbstore);
				list.addAll(ocollection);
			}
		try {
			for (Environment environment : list) {
				//System.out.println(environment);			
				/*
				 * 怎样进入if
				 * preparedStatement==null进入if
				 * preparedStatement!=null判断后面
				 */	
				if(preparedStatement==null||day!=environment.getGather_date().getDate()){		
				day=environment.getGather_date().getDate();
				
				if(preparedStatement!=null){
					preparedStatement.executeBatch();
					connection.commit();
					preparedStatement.clearBatch();
					preparedStatement.close();
				}			
				sql="insert into e_detail_"+day+" values(?,?,?,?,?,?,?,?,?)";
				preparedStatement=connection.prepareStatement(sql);
				
				}
				preparedStatement.setString(1,environment.getName());
				preparedStatement.setString(2, environment.getSrcId());
				preparedStatement.setString(3, environment.getDstId());
				preparedStatement.setString(4, environment.getSersorAddress());
				preparedStatement.setInt(5, environment.getCount());
				preparedStatement.setString(6, environment.getCmd());
				preparedStatement.setInt(7, environment.getStatus());
				preparedStatement.setFloat(8, environment.getData());
				preparedStatement.setTimestamp(9, environment.getGather_date());			
				preparedStatement.addBatch();
				count++;
				if(count==500){
					preparedStatement.executeBatch();
					connection.commit();
					preparedStatement.clearBatch();
					
				}
				
			}
		} catch (Exception e) {
		
			e.printStackTrace();
			connection.rollback();
			backup.store(backdbstore, list);
		}
		
		if(preparedStatement!=null){
			preparedStatement.executeBatch();
			connection.commit();
			preparedStatement.clearBatch();
			preparedStatement.close();
		}
		 
		//4.执行语句
		
		
		
	}

}
