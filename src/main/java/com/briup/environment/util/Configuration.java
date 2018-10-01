package com.briup.environment.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.environment.backup.BackUp;
import com.briup.environment.client.Client;
import com.briup.environment.client.Gather;
import com.briup.environment.server.DBStore;
import com.briup.environment.server.Server;



public class Configuration {

	private HashMap<String,WossModel> map;
	public void config(){
		String xmlPath="src/main/java/com/briup/environment/util/Config.xml";
		this.config(xmlPath);
	}
	public void config(String xmlPath){
		SAXReader reader=new SAXReader();
		try {
			Document document = reader.read(xmlPath);
			//1.获取根标签
			Element root = document.getRootElement();
			//2.获取二级标签
			List<Element> element = root.elements();
			map=new HashMap<String,WossModel>();
			
			for(Element element1:element){
				String classvalue = element1.attributeValue("class");
				//System.out.println(classvalue);
				Class<?> clazz = Class.forName(classvalue);
				WossModel wossModel= (WossModel) clazz.newInstance();
				String name = element1.getName();
				map.put(name,wossModel);
				List<Element> elements2 = element1.elements();
				Properties prop=new Properties();
				    for(Element ele2: elements2){
				    	String name2 = ele2.getName();
				    	String value = ele2.getStringValue();
				    	prop.setProperty(name2, value);
				    } 
				    //依赖注入，把数据写活
				    wossModel.init(prop);
			}
			Set<Entry<String, WossModel>> entrySet = map.entrySet();
			   for(Entry<String, WossModel> entry:entrySet){
				   WossModel wossModel = entry.getValue();
				   if(wossModel instanceof ConfigurationAware){
					   ((ConfigurationAware)wossModel).
					   setConfiguration(this);
				   }
			   }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Client getClient(){
		return (Client) map.get("cilent");
	}
	public BackUp getBackUp(){
		return (BackUp)map.get("backup");
	}
	public Gather getGather(){
		return (Gather)map.get("gather");
	}
	public Server getServer(){
		return (Server)map.get("server");
	}
	public DBStore getDBStore(){
		return (DBStore)map.get("dbstore");
	}
	public MyLogger getMyLogger(){
		return (MyLogger)map.get("mylogger");
	}
}
