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

import it.polito.tdp.food.db.CoppiaCibi;
import it.polito.tdp.food.db.FoodDao;

public class Model {
	/*
	 * 1) si permetta all'utente di inserire un vaolre numero intero che identifica
	 * il numero di porzioni diverse (portion_id) con cui quel cibo è rappresentato,
	 * Ad esempio se Porzioni = 1, si considerino solo i cibi che siano rpesenti con
	 * una sola porzione. se Porzioni uguale a 3, si considerino i cibi preseti con
	 * 1 2 o 3 porzioni.
	 * 
	 * 
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
			if (grafo.containsVertex(idMap.get(c.getF1())) && grafo.containsVertex(idMap.get(c.getF2()))) {
				Graphs.addEdgeWithVertices(grafo, idMap.get(c.getF1()), idMap.get(c.getF2()), c.getPeso());
			}
		}

	}

	public List<CoppiaCibi> getAdiacenti(Food selezionato) {

		List<Food> adiacenti = new ArrayList<>(cibi);
		List<CoppiaCibi> massimeCalorie = new ArrayList<>();
		List<CoppiaCibi> risultato = new ArrayList<>();

		for (Food food : grafo.vertexSet()) {
			if (grafo.containsVertex(selezionato) && grafo.containsEdge(grafo.getEdge(selezionato, food))) {
				adiacenti = Graphs.neighborListOf(grafo, selezionato);

				for (Food f : adiacenti) {
					massimeCalorie.add(new CoppiaCibi(selezionato.getFood_code(), f.getFood_code(),
							(int) grafo.getEdgeWeight(grafo.getEdge(selezionato, f))));

				}

			}
		}

		Collections.sort(massimeCalorie);

		for (int i = 0; i < 5; i++) {
			risultato.add(massimeCalorie.get(i));
		}

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

	public static void main(String[] args) {
		Model model = new Model();

		model.getFoodPortion(1000);

		model.creaGrafo();

		System.out.println(model.getAdiacenti(new Food(23559, "Ground beef (95% lean)")));
	}

}
