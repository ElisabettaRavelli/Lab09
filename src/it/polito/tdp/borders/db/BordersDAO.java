package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public List<Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	//METODO PER OTTENERE I VERTICI DA INSERIRE NEL GRAFO
	public List<Country> getCountries(int anno, Map<Integer, Country> cMap) {

		String sql = "select * from country " + 
				"where CCode in ( " + 
				"select state1no " + 
				"from contiguity " + 
				"where year<=? and conttype=1)" ;
		
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				//controllo che non sia già presente nella mappa il Country
				 if(cMap.get(rs.getInt("ccode"))==null) {
					 //se non c'è creo l'oggetto country e lo inserisco nella mappa e nella lista
					 Country country = new Country(rs.getInt("ccode"), rs.getString("stateAbb"), rs.getString("stateNme"));
					 
					 cMap.put(country.getCode(), country);
					 result.add(country);
				 }else { //se è già presente nella mappa lo inserisco solo nella lista prendendolo dalla mappa
					 result.add(cMap.get(rs.getInt("ccode")));
				 }
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	//METODO CHE MI RESTITUISCE GLI ARCHI DEL GRAFO
	public List<Border> getCountryPairs(int anno) {

		String sql = "select state1no, state2no " + 
				"from contiguity " + 
				"where state1no < state2no " + 
				"and conttype = 1 " + 
				"and year<= ? " ;
		List<Border> result = new ArrayList<Border>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				 
					 Border border = new Border(rs.getInt("state1no"),rs.getInt("state2no") );
					 result.add(border);
				 
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
