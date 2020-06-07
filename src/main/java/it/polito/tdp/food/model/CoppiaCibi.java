package it.polito.tdp.food.model;

public class CoppiaCibi implements Comparable<CoppiaCibi>{

	private Integer f1;
	private Integer f2;
	private Double peso;

	public CoppiaCibi(Integer f1, Integer f2, Double peso) {
		this.f1 = f1;
		this.f2 = f2;
		this.peso = peso;
	}

	public Integer getF1() {
		return f1;
	}

	public void setF1(Integer f1) {
		this.f1 = f1;
	}

	public Integer getF2() {
		return f2;
	}

	public void setF2(Integer f2) {
		this.f2 = f2;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(CoppiaCibi o) {
		return -this.peso.compareTo(o.peso);
	}

	@Override
	public String toString() {
		return "CoppiaCibi [f1=" + f1 + ", f2=" + f2 + ", peso=" + peso + "]";
	}
	
	

}
