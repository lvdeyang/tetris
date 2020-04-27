package com.sumavision.tetris.commons.util.xml;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLReader {

	private Document document;
	
	public XMLReader(String xml) throws Exception{
		 DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	 
		 this.document = builder.parse(new InputSource(new StringReader(xml)));
	}
	
	
	public XMLReader(InputStream xml) throws Exception{
		 DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		 this.document = builder.parse(xml);
	}
	
	/**
	 * -------------------------
	 * -------------------------
	 * -------读取节点内容---------
	 * -------------------------
	 * -------------------------
	 */
	
	/**
	 * 根据key路径读取文档中指定节点的内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月3日 下午3:30:40
	 * @param String keyPath key.key.key
	 * @return String xml节点内容
	 */
	public String readString(String keyPath) throws Exception{
		return readString(keyPath, this.document);
	}
	
	/**
	 * 根据key路径读取上下文节点中指定节点的内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 下午5:14:58
	 * @param String keyPath key.key.key
	 * @param Node context 上下文节点
	 * @return 节点内容
	 */
	public String readString(String keyPath, Node context){
		String[] keys = keyPath.split("\\.");
		Node currentNode = null;
		if(context.getNodeName().equals(keys[0])){
			currentNode = context;
		}else{
			NodeList nodeList = context.getChildNodes();
			for(int j=0; j<nodeList.getLength(); j++){
				Node scopeNode = nodeList.item(j);
				if(scopeNode.getNodeName().equals(keys[0])){
					currentNode = scopeNode;
					break;
				}
			}
		}
		if(currentNode == null) return null;
		if(keys.length > 1){
			for(int i=1; i<keys.length; i++){
				String scopeKey = keys[i];
				boolean finded = false;
				for(int j=0; j<currentNode.getChildNodes().getLength(); j++){
					Node scopeNode = currentNode.getChildNodes().item(j);
					if(scopeNode.getNodeName().equals(scopeKey)){
						if(i == keys.length-1){
							return scopeNode.getTextContent();
						}else{
							currentNode = scopeNode;
						}
						finded = true;
						break;
					}
				}
				if(!finded) return null;
			}
		}else{
			return currentNode.getTextContent();
		}
		return null;
	}
	
	public Long readLong(String keyPath) throws Exception{
		return readLong(keyPath, this.document);
	}
	
	public Long readLong(String keyPath, Node context) throws Exception{
		return Long.valueOf(readString(keyPath, context));
	}
	
	/**
	 * -------------------------
	 * -------------------------
	 * -------读取节点属性---------
	 * -------------------------
	 * -------------------------
	 */
	
	/**
	 * 根据key路径读取文档中指定节点的属性内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月3日 下午3:30:40
	 * @param String keyPath key.key.key
	 * @param String attribute 目标节点属性
	 * @return String 节点属性
	 */
	public String readAttribute(String keyPath, String attribute) throws Exception{
		return readAttribute(keyPath, attribute, this.document);
	}
	
	/**
	 * 根据key路径读取上下文节点中指定节点的内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 下午5:19:28
	 * @param String keyPath key.key.key
	 * @param String attribute 属性名
	 * @param Node context 上下文节点
	 * @return String 节点属性
	 */
	public String readAttribute(String keyPath, String attribute, Node context) throws Exception{
		String[] keys = keyPath.split("\\.");
		Node currentNode = null;
		if(keys[0].equals(context.getNodeName())){
			currentNode = context;
		}else{
			NodeList nodeList = document.getChildNodes();
			for(int j=0; j<nodeList.getLength(); j++){
				Node scopeNode = nodeList.item(j);
				if(scopeNode.getNodeName().equals(keys[0])){
					currentNode = scopeNode;
					break;
				}
			}
		}
		if(currentNode == null) return null;;
		if(keys.length > 1){
			for(int i=1; i<keys.length; i++){
				String scopeKey = keys[i];
				boolean finded = false;
				for(int j=0; j<currentNode.getChildNodes().getLength(); j++){
					Node scopeNode = currentNode.getChildNodes().item(j);
					if(scopeNode.getNodeName().equals(scopeKey)){
						if(i == keys.length-1){
							return scopeNode.getAttributes().getNamedItem(attribute).getNodeValue();
						}else{
							currentNode = scopeNode;
						}
						finded = true;
						break;
					}
				}
				if(!finded) return null;
			}
		}else{
			return currentNode.getAttributes().getNamedItem(attribute).getNodeValue();
		}
		return null;
	}
	
	/**
	 * -------------------------
	 * -------------------------
	 * -------读取节点列表---------
	 * -------------------------
	 * -------------------------
	 */
	
	/**
	 * 根据key路径读取文档中指定节点列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 下午5:29:00
	 * @param String keyPath key.key.key
	 * @return List<Node> 目标节点列表
	 */
	public List<Node> readNodeList(String keyPath) throws Exception{
		return readNodeList(keyPath, this.document);
	}
	
	/**
	 * 根据key路径读取上下文节点中指定节点列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 下午5:29:00
	 * @param String keyPath key.key.key
	 * @param Node context 上下文节点
	 * @return List<Node> 目标节点列表
	 */
	public List<Node> readNodeList(String keyPath, Node context) throws Exception{
		String[] keys = keyPath.split("\\.");
		List<Node> currentNodeList = new ArrayList<Node>();
		Node currentNode = null;
		if(keys[0].equals(context.getNodeName())){
			currentNode = context;
		}else{
			NodeList nodeList = document.getChildNodes();
			for(int j=0; j<nodeList.getLength(); j++){
				Node scopeNode = nodeList.item(j);
				if(scopeNode.getNodeName().equals(keys[0])){
					currentNode = scopeNode;
					break;
				}
			}
		}
		if(currentNode == null) return null;
		if(keys.length > 1){
			for(int i=1; i<keys.length; i++){
				String scopeKey = keys[i];
				boolean finded = false;
				for(int j=0; j<currentNode.getChildNodes().getLength(); j++){
					Node scopeNode = currentNode.getChildNodes().item(j);					
					if(scopeNode.getNodeName().equals(scopeKey)){
						finded = true;
						if(keys.length == i+1){
							currentNodeList.add(scopeNode);
						}else{
							currentNode = scopeNode;
						}
					}
				}
				if(!finded) return null;
			}
		}
		return currentNodeList;
	}
	
	public static void main(String[] args) throws Exception{
		String xml = "<eee><ddd><PathFormat><obj><a><b>1b</b></a></obj><obj><a><b>2b</b></a></obj></PathFormat></ddd></eee>";
		XMLReader reader = new XMLReader(xml);
		List<Node> nodes = reader.readNodeList("eee.ddd.PathFormat.obj");
		System.out.println(nodes.size());
		for(Node node: nodes){
			System.out.println(reader.readString("a.b", node));
		}
	}
}
