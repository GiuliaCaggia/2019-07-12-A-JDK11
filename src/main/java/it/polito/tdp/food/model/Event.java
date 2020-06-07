package it.polito.tdp.food.model;

public class Event implements Comparable<Event> {

	private Double tempo;
	private EventType type;
	private Food food;
	private Stazione stazione;

	public enum EventType {

		PREPARAZIONE_CIBO, FINE_PREPARAZIONE
	}

	public Event(Double tempo, EventType type, Food food, Stazione stazione) {
		this.tempo = tempo;
		this.type = type;
		this.food = food;
		this.stazione = stazione;
	}

	public Double getTempo() {
		return tempo;
	}

	public void setTempo(Double tempo) {
		this.tempo = tempo;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}
	
	

	public Stazione getStazione() {
		return stazione;
	}

	public void setStazione(Stazione stazione) {
		this.stazione = stazione;
	}

	@Override
	public int compareTo(Event o) {
		return this.tempo.compareTo(o.tempo);
	}

}
