package com.briup.environment.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.briup.environment.backup.BackUp;
import com.briup.environment.bean.Environment;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.ConfigurationAware;
import com.briup.environment.util.WossModel;

public class Gather implements ConfigurationAware,WossModel{
	private String path;
	private String backgather;
	private BackUp backup;

	@Override
	public void init(Properties prop) throws Exception {
		path=prop.getProperty("path");
		backgather=prop.getProperty("backgather");
	}

	@Override
	public void setConfiguration(Configuration config) {
		backup=config.getBackUp();
		
	}
	/**
	 * 采集：通过流来读取文件
	 * @throws Exception 
	 * */	
	public Collection<Environment> gather() throws Exception{
		File file =new File(path);	
		FileReader fReader=new FileReader(file);
		BufferedReader bufferedReader=new BufferedReader(fReader);
		//String string=bufferedReader.readLine();
		Environment environment=null;
		long index=0l;
		long oindex=0l;
		if(backup.load(backgather)!=null){
			oindex=(long) backup.load(backgather);
			backup.deleteBackup(backgather);
		}
		//跳过oindex
		bufferedReader.skip(oindex);
		List<Environment>list=new ArrayList<Environment>();
		String string=null;
		while((string=bufferedReader.readLine())!=null){
			index+=string.length()+1l+oindex;
			String info[]=string.split("[|]");
			environment=new Environment();
			environment.setSrcId(info[0]);
			environment.setDstId(info[1]);
			environment.setDevId(info[2]);
			environment.setSersorAddress(info[3]);
			environment.setCount(Integer.parseInt(info[4]));
			environment.setCmd(info[5]);
			environment.setStatus(Integer.parseInt(info[7]));
			environment.setGather_date(new Timestamp(Long.parseLong(info[8])));
			if(environment.getSersorAddress().equals("16")){
				environment.setName("温度");
				int value=Integer.parseInt(info[6].substring(0,4),16);
				//System.out.println(value);
				float data=(float) (((float)value*0.00268127)-46.85);
				environment.setData(data);
				//System.out.println(data);
				list.add(environment);
				environment.setName("湿度");
				int value1=Integer.parseInt(info[6].substring(4,8),16);
				//System.out.println(value1);
				float data1=(float) (((float)value*0.00190735)-6);
				environment.setData(data1);
				list.add(environment);
			}else if(environment.getSersorAddress().equals("256")){
				environment.setName("光照强度");
				int da=Integer.parseInt(info[6].substring(0,4),16);
				environment.setData(da);
				list.add(environment);
				
			}else if(environment.getSersorAddress().equals("1280")){
				environment.setName("二氧化碳浓度");
				int da=Integer.parseInt(info[6].substring(0,4),16);
				environment.setData(da);
				list.add(environment);
			}
		}
		backup.store(backgather,index);
		if(fReader!=null){
			fReader.close();
		}if(bufferedReader!=null){
			bufferedReader.close();
		}
		return list;
	}

}
