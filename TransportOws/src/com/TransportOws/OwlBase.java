package com.TransportOws;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.management.Query;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import com.github.andrewoma.dexx.collection.internal.adapter.SortedMapAdapter;


//import


public class OwlBase {
	private String request;
	private Model m1;
	private int num_uber=0;
	private int num_other=0;
	private static OwlBase ow=null;

	private OwlBase() throws FileNotFoundException, SQLException
	{
		request="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
		 		"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" + 
		 		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
		 		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" + 
		 		"PREFIX : <http://www.semanticweb.org/asus-pc/ontologies/2019/11/untitled-ontology-17#>";
		
		Model m = ModelFactory.createDefaultModel();//=FileManager.get().loadModel("/home/samir/Documents/transportTp.owl");
		this.m1=m.read(new FileInputStream("/home/samir/Documents/transportTp.owl"),null,"TTL");
		 //m1.write(System.out);

		
		 
	}
	
	public static OwlBase getInstance()
	{
		if(ow==null)
		{
			synchronized(OwlBase.class)
			{
				if(ow==null)
					try {
						ow=new OwlBase();
					} catch (FileNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		return ow;
	}
	
	public List<String> get_type_transport(String type)
	
	{
		
		List<String>results=new ArrayList<String>();
		
		org.apache.jena.query.Query q=QueryFactory.create(this.request+
		 		"SELECT ?x ?y \n" + 
		 		"	WHERE { ?x rdf:type :"+type+"}");
		
		 QueryExecution qe=QueryExecutionFactory.create(q,m1);
		 org.apache.jena.query.ResultSet rs=qe.execSelect();
		 List<String>Stations=new ArrayList<String>();
		 
		 if(rs==null)
			 return null;
		 else {
		 while(rs.hasNext())
		 {
			 QuerySolution qs=rs.nextSolution();
			 String transpotsName=qs.getResource("x").getLocalName();
			 results.add(transpotsName);
			 
			
		 }
		 }
		 
		/* for(String s:results)
			 System.out.println(s);*/
		 
		
		 
		 return results;
	
	

}
	
	public Map<String ,Travel> get_possible_route(String depart, String arrive,String filter)
	{
		Map<String,Travel>results=new LinkedHashMap<String, Travel>();
		//SortedMap<String,Travel>results=new TreeMap<String, Travel>();
	
		String request1=null;
		request1=this.request+
				"SELECT ?t ?m ?s ?p ?g\n"+
				"WHERE {{?t :aPourDépart :"+depart+" . ?t :PourDestination :"+arrive+" .?t"+
				" :constitué ?m . FILTER NOT EXISTS { ?m :aPourStation ?s } .?t :Prix ?p .?t :EmissionDeGaz ?g}\n"+
				"UNION { ?t :aPourDépart :"+depart+" . ?t :PourDestination :"+arrive+"  .?t :constitué ?m ." +
				" ?m :aPourStation ?s .?t :Prix ?p  .?m :EmissionDeGaz ?g}} ";
		
		
		if(filter.matches("Prix"))
		
			request1+=" ORDER BY ?p";
		else if(filter.matches("Gaz"))
			request1+=" ORDER BY ?g";
		
		org.apache.jena.query.Query q=QueryFactory.create(request1);
		
		 QueryExecution qe=QueryExecutionFactory.create(q,m1);
		 org.apache.jena.query.ResultSet rs=qe.execSelect();
		 List<String>Stations=new ArrayList<String>();
		 Travel t=null;
		 if(rs==null)
			 return null;
		 else {
			 
		 
			 while(rs.hasNext())
			 {
				 
				 QuerySolution qs=rs.nextSolution();
				 String travel=qs.getResource("t").getLocalName();
				 String MoyenTransport=qs.getResource("m").getLocalName();
				 if(qs.getResource("s")!=null)
					 Stations.add(qs.getResource("s").getLocalName());
				 //System.out.println(travel);
				 double prix=Double.valueOf(qs.getLiteral("p").getDouble());
				 double emissionDeGaz=Double.valueOf(qs.getLiteral("g").getDouble());
				 results.putIfAbsent(travel, new Travel(travel, prix, emissionDeGaz));
				 results.get(travel).addTransport(MoyenTransport);
				 
				
			 }
			 
			 for (Map.Entry<String,Travel> entry : results.entrySet()) {
			        Travel value = entry.getValue();
			        String key = entry.getKey();
			        
			      
			        
			        for(String s:value.getTransport_list())
			        	System.out.println(s);
			        
			        System.out.println("----------------------------------");
			 }
			
			 return results;
		}
		 
		
	
	}
	
	public double get_ratio_uber()
	{
		
		
		org.apache.jena.query.Query q=QueryFactory.create(this.request+"SELECT  (COUNT(?y) as ?x)\n"+
				"WHERE { {?y rdf:type :Uber}  UNION {?y rdf:type :Taxis}}GROUP BY ?y");
		
		
		 QueryExecution qe=QueryExecutionFactory.create(q,m1);
		 org.apache.jena.query.ResultSet rs=qe.execSelect();
		 List<Integer>results=new ArrayList<Integer>();
		
		
		 
			 while( rs.hasNext())
			 {
				 QuerySolution qs=rs.nextSolution();
				 results.add(Integer.valueOf(qs.getLiteral("x").getInt()));
				 
			 }
			 
			 for(Integer i : results)
				 this.num_uber+=i;
			 
			 
		
		
		 q=QueryFactory.create(request+"SELECT (COUNT(?z) as ?y)\n" + 
					"WHERE{?x :constitué ?z}"
			);
		
		 qe=QueryExecutionFactory.create(q,m1);
		 rs=qe.execSelect();
		 results=new ArrayList<Integer>();
		 while( rs.hasNext())
		 {
			 QuerySolution qs=rs.nextSolution();
			 int z=(qs.getLiteral("y").getInt());
			 results.add(Integer.valueOf(qs.getLiteral("y").getInt()));
			 
		 }
		 
		 
		 for(Integer i : results)
			 this.num_other+=i;
		 
		 num_other=num_other-num_uber;
		 System.out.println(num_other+" uber");
		 System.out.println(num_uber+ " other");
	
		double rez=num_other/num_uber;
		return rez;
		
	}
}