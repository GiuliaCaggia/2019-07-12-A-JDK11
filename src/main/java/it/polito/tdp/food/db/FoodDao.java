package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public void listAllFoods(Map<Integer, Food> idMap) {
		String sql = "SELECT * FROM food";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					if (!idMap.containsKey(res.getInt("food_code"))) {
						Food f = new Food(res.getInt("food_code"), res.getString("display_name"));
						idMap.put(f.getFood_code(), f);
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<Condiment> listAllCondiments() {
		String sql = "SELECT * FROM condiment";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Condiment> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"), res.getString("display_name"),
							res.getDouble("condiment_calories"), res.getDouble("condiment_saturated_fats")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Portion> listAllPortions() {
		String sql = "SELECT * FROM portion";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Portion> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"), res.getDouble("portion_amount"),
							res.getString("portion_display_name"), res.getDouble("calories"),
							res.getDouble("saturated_fats"), res.getInt("food_code")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Food> getFoodPortion(Integer portion_id) {

		String sql = "SELECT food.food_code as f, food.display_name as d, count(distinct portion.portion_id) as cnt FROM `portion`, food  WHERE food.food_code = portion.food_code group by food.food_code HAVING cnt = ? ORDER BY food.display_name ASC" ;

		List<Food> cibi = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, portion_id);

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					Food f = new Food(res.getInt("f"),res.getString("d"));
					cibi.add(f);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return cibi;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<CoppiaCibi> getCoppie(){
		
		String sql = "SELECT f1.food_code as f1, f2.food_code as f2, AVG(c1.condiment_calories) as peso FROM condiment c1, condiment c2, food_condiment f1, food_condiment f2 WHERE f1.food_code > f2.food_code AND c1.condiment_code = f1.condiment_code AND c2.condiment_code = f2.condiment_code AND c1.condiment_code = c2.condiment_code GROUP BY f1.food_code, f2.food_code";
		List<CoppiaCibi> coppie = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);


			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					CoppiaCibi c = new CoppiaCibi(res.getInt("f1"),res.getInt("f2"),res.getDouble("peso"));
					coppie.add(c);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return coppie;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
