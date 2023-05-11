package api;

import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import util.WSUtil;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class EMS {
	private DefaultHttpClient httpclient = new DefaultHttpClient();
	private Principal principal = null;

	private final int SC_CREATED = 201;
	// This will be updated from configuration file & will represent ems URL
	private String serverUrl = "";
	private String uri = "ems/v85/ws/";
	private int vendorID = 1; // DEMOMA

	/**
	 * Loads trustStore and trustStorePassword
	 * 
	 * @throws Exception
	 */
	public void loadProperties() throws Exception {

		Properties props = new Properties();

		InputStream in = WSUtil.getInputStream("configure.properties");

		props.load(in);
		serverUrl = props.getProperty("serverUrl");

		if (serverUrl == null || serverUrl.equals(""))
			throw new IllegalArgumentException("Server URL cannot be empty in configure.properties");

		serverUrl = serverUrl.trim();
		serverUrl = serverUrl.endsWith("/") ? serverUrl : serverUrl + "/";

	}

	/**
	 * Sample code for verify login
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean verifyLogin() throws Exception {

		HttpContext context = new BasicHttpContext();
		// HttpPost post = new HttpPost(serverUrl + "ems/v64/ws/login.ws");
		String url = serverUrl + uri + "login.ws";
		String authenticationDetail = WSUtil
				.readFileAsString("AuthenticationDetailSample.xml");
		// post.setEntity(new StringEntity(authenticationDetail, HTTP.UTF_8));
		// System.out.println(authenticationDetail +" " + url );
		HttpResponse emsResponse = WSUtil.doPost(url, authenticationDetail,
				principal, httpclient);
		// HttpResponse emsResponse = httpclient.execute(post, context);

		// ************* Close the connection
		// emsResponse.getEntity().consumeContent();
		emsResponse.getEntity().getContent().close();

		// *******************Below is the code sample to fetch Session Id from
		// response header.
		/*
		 * Header[] cookieHeader=emsResponse.getHeaders("Set-Cookie");
		 * if(cookieHeader!=null && cookieHeader.length>0){ for(Header
		 * header:cookieHeader){ for(HeaderElement
		 * element:header.getElements()){
		 * if(element.getName().equalsIgnoreCase("JSESSIONID")){
		 * sessionId=element.getValue(); break; } } if(sessionId!=null &&
		 * sessionId.length()>0) break; }
		 * 
		 * }
		 */

		// *********** principal is set after login for future use.
		principal = (Principal) context.getAttribute(ClientContext.USER_TOKEN);

		if (emsResponse.getStatusLine().getStatusCode() == 200)
			return true;
		else
			return false;
	}

	public NodeList getAllFeatures() {
		// System.out.println("getAllFeatures  ------- ");

		String url = serverUrl + uri + "vendor/" + vendorID + "/feature.ws";
		// System.out.println("url = "+url);
		try {
			String allFeatures = WSUtil.doGet(url, null, principal, httpclient);
			Document doc = convertStringToXML(allFeatures);
			NodeList nodeList = doc.getElementsByTagName("instance");
			return nodeList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String getFeatureByFeatureID(String identifier) {
		NodeList nodeList = getAllFeatures();
		if (nodeList == null)
			return null;
		for (int itr = 0; itr < nodeList.getLength(); itr++) {

			Node node = nodeList.item(itr);
			String featureID = node.getAttributes().getNamedItem("identifier").getNodeValue();
			String id = node.getAttributes().getNamedItem("id").getNodeValue();
			// System.out.println("featureID : " + featureID + " type=" +
			// featureID.getClass() + " identifier =" + identifier + " id =" + id +" eq=" +
			// identifier.equals(featureID));
			// System.out.println("Node ===============================: " +
			// node.getAttributes().getNamedItem("name"));
			// System.out.println("Node ===============================: " +
			// node.getAttributes().getNamedItem("identifier"));
			if (identifier.equals(featureID))
				return id;
			// if (node.getNodeType() == Node.ELEMENT_NODE) {

			// Element eElement = (Element) node;
			// System.out.println("eElement: "+ eElement);
			// System.out.println("Name: "+
			// eElement.getElementsByTagName("id").item(0).getTextContent());
			// System.out.println("Title: "+
			// eElement.getElementsByTagName("name").item(0).getTextContent());
			// System.out.println("Title: "+
			// eElement.getElementsByTagName("name").item(0).getTextContent());
			// System.out.println("Title: "+
			// eElement.getElementsByTagName("identifier").item(0).getTextContent());
			// }
		}

		return null;

	}

	public Feature getFeatureDetails(String id) {
		// System.out.println("getFeatureDetails  ------- " + id);
		String featureId = getFeatureByFeatureID(id);
		// System.out.println("featureId  ------- " + featureId);
		if (featureId == null)
			return null;
		String url = serverUrl + uri + "feature/" + featureId + ".ws";
		try {
			String details = WSUtil.doGet(url, null, principal, httpclient);
			Document doc = convertStringToXML(details);
			Element el = doc.getDocumentElement();
			String[] res = new String[5];
			res[0] = el.getElementsByTagName("featureIdentifier").item(0).getTextContent();
			res[1] = el.getElementsByTagName("featureName").item(0).getTextContent();
			res[2] = el.getElementsByTagName("featureDescription").item(0).getTextContent();
			res[3] = el.getElementsByTagName("refId1").item(0).getTextContent();
			res[4] = el.getElementsByTagName("refId2").item(0).getTextContent();
			Feature feature = new Feature(res[0], res[1], res[2], res[3], res[4]);

			return feature;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public ArrayList<Feature> getFeatureList() {
		NodeList nodeList = getAllFeatures();
		ArrayList<Feature> featureList = new ArrayList<>();
		if (nodeList == null)
			return null;

		for (int itr = 0; itr < nodeList.getLength(); itr++) {

			Node node = nodeList.item(itr);
			String id = node.getAttributes().getNamedItem("id").getNodeValue();
			// System.out.println("Node  ==============: " + node.getAttributes().getNamedItem("name"));
			// System.out.println("Node  ==============: " + node.getAttributes().getNamedItem("identifier"));
			// System.out.println("Node  ==============: " + id);

			String url = serverUrl + uri + "feature/" + id + ".ws";
			try {
				String details = WSUtil.doGet(url, null, principal, httpclient);
				Document doc = convertStringToXML(details);
				Element el = doc.getDocumentElement();
				String[] res = new String[5];
				res[0] = el.getElementsByTagName("featureIdentifier").item(0).getTextContent();
				res[1] = el.getElementsByTagName("featureName").item(0).getTextContent();
				res[2] = el.getElementsByTagName("featureDescription").item(0).getTextContent();
				res[3] = el.getElementsByTagName("refId1").item(0).getTextContent();
				res[4] = el.getElementsByTagName("refId2").item(0).getTextContent();
				Feature feature = new Feature(res[0], res[1], res[2], res[3], res[4]);

				// System.out.println();
				// System.out.println(feature.getFeatureIdentifier());
				// System.out.println(feature.getFeatureName());
				// System.out.println(feature.getFeatureDescription());
				// System.out.println(feature.getRefId1());
				// System.out.println(feature.getRefId2());
				// System.out.println();

				featureList.add(feature);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Create an iterator for the list
		// using iterator() method
		// Iterator<Feature> iter
		// = featureList.iterator();
		//
		// // Displaying the values after iterating
		// // through the list
		// System.out.println("\nThe iterator values"
		// + " of list are: ");
		// System.out.println("=======================================================================
		// ");
		// while (iter.hasNext()) {
		// Feature f = iter.next();
		// System.out.println(f.getFeatureIdentifier() + " ");
		// System.out.println(f.getFeatureName() + " ");
		// System.out.println(f.getFeatureDescription() + " ");
		// System.out.println(f.getRefId1() + " ");
		// System.out.println(f.getRefId2() + " ");
		// System.out.println();
		// }
		return featureList;
	}

	public Document convertStringToXML(String xmlString) {
		// Parser that produces DOM object trees from XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// API to obtain DOM Document instance
		DocumentBuilder builder = null;
		try {
			// Create DocumentBuilder with default configuration
			builder = factory.newDocumentBuilder();

			// Parse the content to Document object
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Sample code to create customer
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createCustomer() throws Exception {
		String url = serverUrl + "ems/v64/ws/customer.ws";
		String customerXML = WSUtil.readFileAsString("CustomerSample.xml");

		// ******* principal was set after login
		HttpResponse emsResponse = WSUtil.doPut(url, customerXML, principal,
				httpclient);

		if (emsResponse.getStatusLine().getStatusCode() != SC_CREATED) {
			throw new Exception("Create Customer Failed -"
					+ WSUtil.getDataFromResponse(emsResponse));
		} else {
			emsResponse.getEntity().consumeContent();
			return emsResponse.getFirstHeader("Location").getValue();
		}
	}

	/**
	 * Sample code to create product
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createProduct() throws Exception {
		// To pass in which vendor need to add this product, in sample there is
		// only one vendor
		int vendorID = 1;
		String url = serverUrl + "ems/v64/ws/vendor/" + vendorID
				+ "/product.ws";
		String productXML = WSUtil.readFileAsString("CloudProductSample.xml");

		// ******* principal was set after login
		HttpResponse emsResponse = WSUtil.doPut(url, productXML, principal,
				httpclient);

		if (emsResponse.getStatusLine().getStatusCode() != SC_CREATED) {
			throw new Exception("Create Product Failed -"
					+ WSUtil.getDataFromResponse(emsResponse));
		} else {
			emsResponse.getEntity().consumeContent();
			return emsResponse.getFirstHeader("Location").getValue();
		}
	}

	/**
	 * To create Named Entitlement
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createNamedEntitlement() throws Exception {
		return createEntitlement("CloudNamedEntitlementSample.xml");
	}

	/**
	 * To create UnNamed Entitlement
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createUnNamedEntitlement() throws Exception {
		return createEntitlement("CloudUnNamedEntitlementSample.xml");
	}

	/**
	 * Sample code to create Entitlement webservice
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createEntitlement(String inputXML) throws Exception {

		String url = serverUrl + "ems/v64/ws/entitlement.ws";
		String entitlementXml = WSUtil.readFileAsString(inputXML);

		// ******* principal was set after login
		HttpResponse emsResponse = WSUtil.doPut(url, entitlementXml, principal,
				httpclient);

		if (emsResponse.getStatusLine().getStatusCode() != SC_CREATED) {
			throw new Exception("Create Entitlement Failed -"
					+ WSUtil.getDataFromResponse(emsResponse));
		} else {
			emsResponse.getEntity().consumeContent();
			return emsResponse.getFirstHeader("Location").getValue();
		}
	}

	/**
	 * Sample code to execute getEntitlement by id webService
	 * 
	 * @param entId
	 *              entitlement id
	 * @return
	 * @throws Exception
	 */
	public String getEntitlement(Integer entId) throws Exception {

		String url = serverUrl + "ems/v64/ws/entitlement/" + entId + ".ws";

		return WSUtil.doGet(url, null, principal, httpclient);
	}

	/**
	 * Sample code to get Usage Data of created Entitlement
	 * 
	 * @param productKeyID
	 *                     product key id of created entitlement
	 * @param productId
	 *                     product id of product which is added in entitlement
	 * @return
	 * @throws Exception
	 */
	public String getUsageData(String productKeyID, int productId)
			throws Exception {
		String url = serverUrl + "ems/v64/ws/productKey/" + productKeyID
				+ "/product/" + productId + "/usagedata.ws";
		return WSUtil.doGet(url, null, principal, httpclient);
	}

	/**
	 * The main method to call all web services
	 * 
	 * @param args
	 */
	// public void main(String[] args) {

	// try {
	// loadProperties();
	// System.out.println("Attempting Login........");
	// boolean verified = verifyLogin();
	// if (verified) {
	// System.out.println("Login Successfull.........");
	// System.out.println("Creating Customer..........");
	// Integer.parseInt(createCustomer());
	// System.out.println("Creating Product..........");
	// int productID = Integer.parseInt(createProduct());

	// System.out.println("Creating UnNamed Entitlement..........");
	// Integer unNamedEntId = Integer
	// .parseInt(createUnNamedEntitlement());
	// System.out.println("Entitlement created with Id - "
	// + unNamedEntId);

	// System.out.println("Creating Named Entitlement..........");
	// Integer namedEntId = Integer.parseInt(createNamedEntitlement());
	// System.out.println("Entitlement created with Id - "
	// + namedEntId);

	// System.out.println("Getting UnNamed Entitlement..........");
	// String unNamedEntXML = getEntitlement(unNamedEntId);
	// System.out.println("Getting Product Key from Entitlement..........");
	// String productKey = WSUtil.getDataFromXml(unNamedEntXML,
	// "productKeyId");
	// System.out.println("Product Key Found - " + productKey);

	// System.out
	// .println("Getting UsageData of UnNamed Entitlement..........");
	// String usageDataXML = getUsageData(productKey, productID);
	// System.out.println("UsageData of UnNamed Entitlement - "
	// + usageDataXML);
	// } else {
	// throw new Exception("Not able to login");
	// }

	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}
