package com.smartapps.satrainschedule;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;

import com.smartapps.satrainschedule.TrainSchedule.ScheduleData;

//import com.smartapps.safindtrains.Train.TrainData;


@Path("/")
public class TrainScheduleResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5278229852877855876L;

	@GET
	@Path("trainschedule")
    @Produces({MediaType.APPLICATION_JSON})	
	public String readPnr(@QueryParam("trainNum") String trainNum) throws Exception {
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	
    	String url = "http://enquiry.indianrail.gov.in/mntes/MntesServlet?action=TrainRunning&subAction=TrainSchedule&event=show&trainNo=" + trainNum;
    	HttpPost httpPost = new HttpPost(url);
    	httpPost.setHeader("Referer", url);
    	
    	List <NameValuePair> nvps = new ArrayList <NameValuePair>();
    	nvps.add(new BasicNameValuePair("trainNo", trainNum));
    	httpPost.setEntity(new UrlEncodedFormEntity(nvps));
    	
    	CloseableHttpResponse response = httpclient.execute(httpPost);

	    HttpEntity entity = response.getEntity();
	    Document doc = Jsoup.parse(entity.getContent(), "UTF-8", "");
	     
	    TrainSchedule train = new TrainSchedule();
	    Elements tables = doc.select("table.tableBorder300");
	    int count = 0;
	    for (Iterator<Element> itt = tables.iterator(); itt.hasNext(); ) {
	    	Element table = itt.next();
	    	count++;
	    	
	    	if(count == 4)
	    	{
	    		Elements td = table.select("td");
	    		train.setDaysRunning(td.get(2).text().replace("\u00a0","").trim());
	    	}
	    	if(count == 5)
	    	{
	    		int rowCount = 0;
	    		Elements trs = table.select("tr");
	    		
	    		for (Iterator<Element> itt1 = trs.iterator(); itt1.hasNext(); ) {
	    	    
	    			Element te = itt1.next();
	    			if(rowCount != 0)
	    			{
	    				Elements td = te.select("td");
	    				
	    				if(td.size() > 6)
	    				{
	    					ScheduleData data = train.new ScheduleData();
		    				data.setStationName(td.get(1).text().trim()+"("+td.get(2).text().trim()+")");
		    				data.setArrivalTime(td.get(3).text().trim().equals("SRC") ? "Source" : td.get(3).text().trim());
		    				data.setDepartureTime(td.get(4).text().trim().equals("DSTN") ? "Destn" : td.get(4).text().trim());
		    				data.setDistance(td.get(5).text().trim().equals("") ? "0 KM" : td.get(5).text().trim()+" KM");
		    				data.setDay(td.get(6).text().trim());
		    				
		    				train.getScheduleData().add(data);
	    				}
	    			}
	    			rowCount ++;
	    		}
	    	}
	    }
	   
	    if(train.getScheduleData().size() == 0)
	    {
	    	String error = doc.select("h1").first().text().trim();
	    	if(!error.equals(""))
	    		return "\"error\":"+"\""+error+"\"";
	        
	    	error = doc.select("h2").first().text().trim();
	    	if(!error.equals(""))
	    		return "\"error\":"+"\""+error+"\"";
	    	
	    	error = doc.select("h3").first().text().trim();
	    	if(!error.equals(""))
	    		return "\"error\":"+"\""+error+"\"";
	    	
	    	error = doc.select("h3").first().text().trim();
	    	if(!error.equals(""))
	    		return "\"error\":"+"\""+error+"\"";
	    	
	    	error = doc.select("a:contains(SORRY)").first().text().trim();
	    	if(!error.equals(""))
	    		return "\"error\":"+"\""+error+"\"";
	    	
	    	return "{\"error\" : \"The IndianRail server is not responding. Please try again later!\"}";
	    }
	    
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
		objectMapper.getSerializationConfig().set(Feature.INDENT_OUTPUT, true);
		objectMapper.getSerializationConfig().set(Feature.USE_ANNOTATIONS, true);
		
		return objectMapper.writeValueAsString(train);
	}

	
	public static String toString(Document doc) {
	    try {
	        StringWriter sw = new StringWriter();
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	        transformer.transform(new DOMSource((Node) doc), new StreamResult(sw));
	        return sw.toString();
	    } catch (Exception ex) {
	        throw new RuntimeException("Error converting to String", ex);
	    }
	}
}
