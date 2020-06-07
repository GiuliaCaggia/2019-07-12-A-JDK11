package it.polito.tdp.food.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;
import it.polito.tdp.food.model.Food.StatoPreparazione;

public class Simulator {

	private PriorityQueue<Event> queue;
	private Model model;
	private Map<Integer, Food> idMap;

	// stato del sistema
	List<Food> food;
	List<Stazione> stazioni;
	private Graph<Food, DefaultWeightedEdge> grafo;

	// parametri della simulazione
	private int k = 5; // stazioni di lavoro --> inserito dall'utente (set)

	// output da ritornare
	private int numTotCibi;
	private Double tempoPreparazione;

	public Simulator(Graph<Food, DefaultWeightedEdge> grafo, Model model, Map<Integer, Food> idMap) {
		this.grafo = grafo;
		this.model = model;
		this.idMap = idMap;
	}

	public void init(Food f) {
		// la simulazione avvia partendo dal cibo selezionato dall'utente -> f

		

		queue = new PriorityQueue<>();

		food = new ArrayList<>(grafo.vertexSet()); // creo una lista di cibi contenente i vertici del grafo

		for (Food cibi : this.food) {
			cibi.setPreparazione(StatoPreparazione.DA_PREPARARE);
		}
		
		this.stazioni = new ArrayList<>();

		// creo tante stazioni iniziali quanto è il k definito con libera settato a true
		// e nessun cibo definito
		for (int i = 0; i < k; i++) {
			stazioni.add(new Stazione(true, null));
		}

		this.numTotCibi = 0;
		this.tempoPreparazione = 0.0;

		food.add(f); // aggiungo alla mia lista di food in preparazione il cibo selezionato

		List<CoppiaCibi> vicini = model.getAdiacenti(f);

		// ottengo tutti i vicini del food da cui sto partendo

		// devo impostare la durata di preparazione del cibo recuperando il peso
		// dell'arco tra i due cibi
		// List<Duration> durate = new ArrayList<>();

		for (int i = 0; i <= k && i < vicini.size(); i++) {

			this.stazioni.get(i).setLibera(false); // occupo la stazione
			this.stazioni.get(i).setFood(idMap.get(vicini.get(i).getF1()));
			DefaultWeightedEdge edge = grafo.getEdge(f, idMap.get(vicini.get(i).getF1()));

			Event e = new Event(vicini.get(i).getPeso(), EventType.FINE_PREPARAZIONE, idMap.get(vicini.get(i).getF1()),
					stazioni.get(i));
			queue.add(e);

			this.numTotCibi++;
			this.tempoPreparazione = e.getTempo();

		}

	}

	public void run() {
		while (!queue.isEmpty()) {
			Event e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {

		// analizzo tutti i tipi di eventi

		switch (e.getType()) {

		case PREPARAZIONE_CIBO:

			List<CoppiaCibi> vicini = model.getAdiacenti(e.getFood());

			CoppiaCibi prossimo = null;

			for (CoppiaCibi vicino : vicini) {
				if (idMap.get(vicino.getF1()).getPreparazione() == StatoPreparazione.DA_PREPARARE) {
					prossimo = vicino;
					break;
				}
			}

			if (prossimo != null) {
				idMap.get(prossimo.getF1()).setPreparazione(StatoPreparazione.IN_CORSO);
				e.getStazione().setLibera(false);
				e.getStazione().setFood(idMap.get(prossimo.getF1()));
			}

			Event e2 = new Event(e.getTempo() + prossimo.getPeso(), EventType.FINE_PREPARAZIONE,
					idMap.get(prossimo.getF1()), e.getStazione());
			queue.add(e2);

			break;

		case FINE_PREPARAZIONE:

			// ho terminato la preparazione di un cibo e devo aggiornare lo stato del mondo
			this.numTotCibi++;
			this.tempoPreparazione = e.getTempo();
			e.getStazione().setLibera(true);
			e.getFood().setPreparazione(StatoPreparazione.PREPARATO);

			queue.add(new Event(e.getTempo(), EventType.PREPARAZIONE_CIBO, e.getFood(), e.getStazione()));
			break;
		}
	}

	public void setK(int k) {
		this.k = k; // tra 1 e 10
	}

	public int getNumTotCibi() {
		return numTotCibi;
	}

	public Double getTempoPreparazione() {
		return tempoPreparazione;
	}

	public static void main(String[] args) {
		Model model = new Model();
		model.creaGrafo();

		Simulator sim = new Simulator(model.getGrafo(), model, model.getIdMap());
		sim.setK(3);
		sim.init(new Food(23559, "Ground beef (95% lean)"));
		sim.run();

		System.out.println("** STATISTICHE **");
		System.out.println("Numero cibi preparati: " + sim.getNumTotCibi());
		System.out.println("Tempo impiegato " + sim.getTempoPreparazione());

	}

}
