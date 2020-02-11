package com.TransportOws;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Travel {
	private String id;
	private String date_Depart;
	private String date_arrive;
	private double prix;
	private double EmissionDeGaz;
	private List<String>transport_list;
	
	public Travel(String id, double prix,double emissionDeGaz) {
		super();
		this.id=id;
		this.date_Depart = date_Depart;
		this.date_arrive = date_arrive;
		this.prix = prix;
		EmissionDeGaz = emissionDeGaz;
		this.transport_list = new ArrayList<String>();
	}
	
	public void addTransport(String s)
	{
		if(!transport_list.contains(s))
			this.transport_list.add(s);
	}

	/*public String getDepart() {
		return Depart;
	}

	public void setDepart(String depart) {
		Depart = depart;
	}

	public String getDestination() {
		return Destination;
	}

	public void setDestination(String destination) {
		Destination = destination;
	}
*/
	public String getDate_Depart() {
		return date_Depart;
	}

	public void setDate_Depart(String date_Depart) {
		this.date_Depart = date_Depart;
	}

	public String getDate_arrive() {
		return date_arrive;
	}

	public void setDate_arrive(String date_arrive) {
		this.date_arrive = date_arrive;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public double getEmissionDeGaz() {
		return EmissionDeGaz;
	}

	public void setEmissionDeGaz(double emissionDeGaz) {
		EmissionDeGaz = emissionDeGaz;
	}

	public List<String> getTransport_list() {
		return transport_list;
	}

	public void setTransport_list(List<String> transport_list) {
		this.transport_list = transport_list;
	}
	
	
	

}
