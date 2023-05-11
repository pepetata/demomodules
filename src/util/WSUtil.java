package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WSUtil {
	
	private static String sessionId = null;
	/**
	 * Sample method to execute a Post call
	 * @param url
	 * @param nvps
	 * @param principal
	 * @param httpclient
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doPost(String url, String inputXml, Principal principal, HttpClient httpclient)throws Exception {
        HttpContext context = new BasicHttpContext();
        context.setAttribute(ClientContext.USER_TOKEN, principal);
        HttpPost post =  new HttpPost(url);
        post.setEntity(new StringEntity(inputXml, HTTP.UTF_8));
        HttpResponse response = httpclient.execute(post, context);
        
	    Header[] cookieHeader=response.getHeaders("Set-Cookie"); 
	    if(cookieHeader!=null && cookieHeader.length>0){
	    	for(Header header:cookieHeader){
	    		for(HeaderElement element:header.getElements()){
	    			if(element.getName().equalsIgnoreCase("JSESSIONID")){
		    			sessionId=element.getValue();
		    			break;
		    		}	
	    		}
	    		if(sessionId!=null && sessionId.length()>0)
	    			break;
	    	}
	    }	
        return response;
    }

	/**
	 * 
	 * @param url
	 * @param inputXml
	 * @param principal
	 * @param httpclient
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doPut(String url, String inputXml, Principal principal, HttpClient httpclient)throws Exception {
        HttpContext context = new BasicHttpContext();
        context.setAttribute(ClientContext.USER_TOKEN, principal);
        HttpPut put =  new HttpPut(url);
        put.addHeader("Cookie", "JSESSIONID=" + sessionId);
        
        put.setEntity(new StringEntity(inputXml, HTTP.UTF_8));
        
        HttpResponse response = httpclient.execute(put, context);
        return response;
    }
	
	/**
	 * 
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static String getDataFromResponse(HttpResponse response)throws Exception{
		HttpEntity entity = response.getEntity();
        StringBuffer sb = new StringBuffer();
        if (entity != null) {
    		try {
    			InputStream instream = entity.getContent();
    			 byte[] tmp = new byte[2048];
    			 while ((instream.read(tmp)) != -1) {
    				 
    				 String value = new String(tmp,"UTF-8");
    				 sb.append(value.trim());
    				 tmp = new byte[2048];
    			 }
    			 instream.close();
			} catch (Exception e1) {
				return e1.getLocalizedMessage();
			}
         }
          return sb.toString();
	}
	/**
	 * Sample method to execute a get call.
	 * @param url
	 * @param urlParams
	 * @param principal
	 * @param httpclient
	 * @return
	 * @throws Exception
	 */
    public static String doGet(String url, Map<String, String> urlParams, Principal principal, HttpClient httpclient)throws Exception  {

    	HttpContext context = new BasicHttpContext();
        context.setAttribute(ClientContext.USER_TOKEN, principal);
        HttpGet get = new HttpGet(url);
        StringBuffer query = new StringBuffer();
        get.addHeader("Cookie", "JSESSIONID=" + sessionId);
        
        // If url have some search parameters
        if (urlParams!=null) {
			for (String paramName : urlParams.keySet()) {
				String paramValue = urlParams.get(paramName);
				setParameter(query, paramName, paramValue);
			}
			get.setURI(new URI(URLDecoder.decode(get.getURI() + query.toString(),"UTF-8")));
		}

        HttpResponse response = httpclient.execute(get, context);
        
        return getDataFromResponse(response);
      }


    /**
     * 
     * @param url
     * @param principal
     * @param httpclient
     * @return
     * @throws Exception
     */
    public static String doDelete(String url, Principal principal, HttpClient httpclient)throws Exception  {

    	HttpContext context = new BasicHttpContext();
        context.setAttribute(ClientContext.USER_TOKEN, principal);
        HttpDelete delete = new HttpDelete(url);
        delete.addHeader("Cookie", "JSESSIONID=" + sessionId);
        
        HttpResponse response = httpclient.execute(delete, context);
        
        return getDataFromResponse(response);
      }
    
    
    	/**
    	 * 
    	 * @param query
    	 * @param key
    	 * @param value
    	 */
      private static void setParameter(StringBuffer query , String key, String value) {

          if (query.toString().equals("")) {
               query.append("?" + key + "=" + value + "&");
         } else {
                query.append(key + "=" + value + "&");
          }
     }

      /**
       * Method to read xml file and return data as String
       * @param filePath
       * @return
       * @throws java.io.IOException
       */
      public static String readFileAsString(String filePath)
      throws java.io.IOException{
          StringBuffer fileData = new StringBuffer(1000);
          BufferedReader reader = new BufferedReader(
                 new InputStreamReader(getInputStream(filePath)));
          char[] buf = new char[1024];
          int numRead=0;
          while((numRead=reader.read(buf)) != -1){
              String readData = String.valueOf(buf, 0, numRead);
              fileData.append(readData);
              buf = new char[1024];
          }
          reader.close();
          return fileData.toString();
      }
      
      public static InputStream getInputStream(String fileName) {
    	  return WSUtil.class
			.getClassLoader().getResourceAsStream(fileName);
      }

		/**
		 * Method to read xml(String) and return value for provided node name
		 * @param entXml
		 * @param nodeName
		 * @return
		 */
		public static String getDataFromXml(String inputXml, String nodeName){
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			String pkId=null;
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document dom = db.parse(new InputSource(new java.io.StringReader(inputXml)));
				NodeList nl = dom.getElementsByTagName(nodeName);
				if(nl != null && nl.getLength() > 0) {
					for(int i = 0 ; i < nl.getLength();i++) {

						Node el = nl.item(i);
						pkId=el.getTextContent();
						
					}
				}

			}catch(ParserConfigurationException pce) {
				pce.printStackTrace();
			}catch(SAXException se) {
				se.printStackTrace();
			}catch(IOException ioe) {
				ioe.printStackTrace();
			}
			return pkId;
		}


}
