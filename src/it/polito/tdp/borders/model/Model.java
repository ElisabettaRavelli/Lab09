package it.polito.tdp.borders.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	//la mappa continene come chiave il codice del country e come valori gli oggetti country
	private Map<Integer, Country> cMap;
	private Graph<Country,DefaultEdge> grafo;
	
	public Model() {
		grafo = new SimpleGraph<>(DefaultEdge.class);
		cMap = new HashMap<Integer, Country>();
	}
	
	public void creaGrafo(int anno) {
		
		//AGGIUGO I VERTICI
		BordersDAO dao = new BordersDAO();
		dao.getCountries(anno, cMap); //popolo la mappa
		Graphs.addAllVertices(grafo, cMap.values());
		System.out.println("Vertici: "+grafo.vertexSet().size());
		
		//AGGIUNGO GLI ARCHI
		List<Border> border = dao.getCountryPairs(anno);
		for(Border b: border) {
			grafo.addEdge(cMap.get(b.getState1no()), cMap.get(b.getState2no()));
		}
		System.out.println("Archi: "+grafo.edgeSet().size());
		
	}
	
	//Metodo che mi permette di avere una lista di oggetti (CountruAndNumber) composti 
	//da un country (vertice del grafo) e il suo grado
	public List<CountryAndNumber> getCountryAndNumber() {
		List<CountryAndNumber> list = new ArrayList<>();
		for(Country tmp: grafo.vertexSet()) {
			CountryAndNumber cn = new CountryAndNumber(tmp, grafo.degreeOf(tmp));
			list.add(cn);
		}
		return list;
	}

	public List<Country> getCountries() {
		//Metodo per passare da set a list
		List<Country> c = grafo.vertexSet().stream().collect(Collectors.toList());
		return c;
		
	}
	//Metodo che calcola le componenti connesse del grafo
	public int calcolaCC() {
		ConnectivityInspector<Country, DefaultEdge> ci = new ConnectivityInspector<Country, DefaultEdge>(grafo);
		return ci.connectedSets().size();
	}
	//Metodo per trovare tutti i vertici raggiungibili da source
	public List<Country> trovaRaggiungibili(Country source){
		
		List<Country> result = new ArrayList<>();
		GraphIterator<Country, DefaultEdge> it = new DepthFirstIterator<Country, DefaultEdge>(grafo, source);
		while(it.hasNext()) {
			result.add(it.next());
		}
		
		return result;
	}
	
	public Country getCountry(String s) {
		for(Country tmp: cMap.values()) {
			if(tmp.getAbb().equals(s)) {
				return tmp;
			}
		}
		return null;
	}
}

