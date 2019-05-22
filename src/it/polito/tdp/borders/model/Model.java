package it.polito.tdp.borders.model;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Map<Integer, Country> cMap;
	private Graph<Country,DefaultEdge> grafo;
	
	public Model() {
		grafo = new SimpleGraph<>(DefaultEdge.class);
		cMap = new HashMap<Integer, Country>();
	}
	
	public void creaGrafo(int anno) {
		
		//AGGIUGO I VERTICI
		BordersDAO dao = new BordersDAO();
		dao.getCountries(anno, cMap);
		Graphs.addAllVertices(grafo, cMap.values());
		
		//AGGIUNGO GLI ARCHI
		List<Border> border = dao.getCountryPairs(anno);
		for(Border b: border) {
			Graphs.addEdge(grafo, cMap.get(b.getState1no()), cMap.get(b.getState2no()), 0);
		}
		
	}
}
