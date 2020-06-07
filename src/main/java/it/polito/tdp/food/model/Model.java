package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	/*
	 * 1) si permetta all'utente di inserire un vaolre numero intero che identifica
	 * il numero di porzioni diverse (portion_id) con cui quel cibo è rappresentato,
	 * Ad esempio se Porzioni = 1, si considerino solo i cibi che siano rpesenti con
	 * una sola porzione. se Porzioni uguale a 3, si considerino i cibi preseti con
	 * 1 2 o 3 porzioni. 
	 */
	
	FoodDao dao;
	List<Food> cibi;
	Map<Integer, Food> idMap;
	Graph<Food, DefaultWeightedEdge> grafo;
	List<CoppiaCibi> coppie;

	public Model() {
		dao = new FoodDao();
		idMap = new HashMap<>();
		dao.listAllFoods(idMap);

	}

	public List<Food> getFoodPortion(Integer portion_id) {
		return cibi = dao.getFoodPortion(portion_id);
	}

	public void creaGrafo() {
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		
		// aggiungo i vertici
		for (Food f : cibi)
			grafo.addVertex(f);

		// aggiungo gli archi
		// se due archi hanno almeno un ingrediente in comune, allora esisterà un arco
		// tra di essi
		// e il peso sarà il valore medio del campo condiment_calories di tutti gli
		// ingredienti in comune
		coppie = dao.getCoppie();
		for (CoppiaCibi c : coppie) {
			if (c.getPeso() != 0 && grafo.containsVertex(idMap.get(c.getF1()))
					&& grafo.containsVertex(idMap.get(c.getF2()))) {
				Graphs.addEdgeWithVertices(grafo, idMap.get(c.getF1()), idMap.get(c.getF2()), c.getPeso());
			}
		}

	}

	public List<CoppiaCibi> getAdiacenti(Food selezionato) {

		List<CoppiaCibi> risultato = new ArrayList<>();

		List<Food> vicini = Graphs.neighborListOf(this.grafo, selezionato);

		for (Food v : vicini) {
			Double calorie = this.grafo.getEdgeWeight(this.grafo.getEdge(selezionato, v));
			risultato.add(new CoppiaCibi(selezionato.getFood_code(), v.getFood_code(), calorie));
		}

		Collections.sort(risultato);
		return risultato;

	}

	public Integer nArchi() {
		return grafo.edgeSet().size();
	}

	public Integer nVertici() {
		return grafo.vertexSet().size();
	}

	public Map<Integer, Food> getIdMap() {
		return idMap;
	}

	public Graph<Food, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public String simula(Food cibo, int k) {
		Simulator sim = new Simulator(this.grafo,this, this.getIdMap());
		sim.setK(k);
		sim.init(cibo);
		sim.run();
		
		String result = String.format("Preparati %d cibi in %f minuti", sim.getNumTotCibi(),sim.getTempoPreparazione());
		
		return result;
		
	}

	public static void main(String[] args) {
		Model model = new Model();

		List<Food> cibi = model.getFoodPortion(1000);

		Map<Integer, Food> idMap = model.getIdMap();
		
		model.creaGrafo();

		System.out.println(model.grafo.vertexSet());
	}

}
