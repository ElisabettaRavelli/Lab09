package it.polito.tdp.borders.model;

public class Country {
	private int code;
	private String abb;
	private String nome;
	
	public Country(int code, String abb, String nome) {
		super();
		this.code = code;
		this.abb = abb;
		this.nome = nome;
	}
	public int getCode() {
		return code;
	}
	public String getAbb() {
		return abb;
	}
	public String getNome() {
		return nome;
	}
	
	
	
}
