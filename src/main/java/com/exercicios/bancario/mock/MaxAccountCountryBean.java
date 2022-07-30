package com.exercicios.bancario.mock;

/**
 * <p>
 * Classe criada para armazenar o país e seu cliente com o maior saldo em contas
 * bancárias
 * </p>
 * 
 * @author Sávio
 */
public class MaxAccountCountryBean {

	private static final long serialVersionUID = 827234455513657974L;
	String country;
	Double maxValueAccount;

	public MaxAccountCountryBean() {
		this.country = "";
		this.maxValueAccount = 0d;
	}
	
	public MaxAccountCountryBean(String country, Double maxValueAccount) {
		this.country = country;
		this.maxValueAccount = maxValueAccount;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Double getMaxValueAccount() {
		return maxValueAccount;
	}

	public void setMaxValueAccount(Double maxValueAccount) {
		this.maxValueAccount = maxValueAccount;
	}

	@Override
	public String toString() {
		return "MaxAccountCountryVO [country=" + country + ", maxValueAccount=" + maxValueAccount + "]";
	}
}
