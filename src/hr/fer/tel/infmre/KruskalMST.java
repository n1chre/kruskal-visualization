package hr.fer.tel.infmre;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementacija Kruskal algoritma za nalaženje najmanjeg razapinjućeg stabla u grafu.
 */
public class KruskalMST {

	private double weight;
	private final List<Edge> mst;

	KruskalMST(Graph g) {

		mst = new LinkedList<>();
		List<Edge> edges = new ArrayList<>(g.getEdges());
		edges.sort(null);
		UnionFind uf = new UnionFind(g.getV());

		// run greedy algorithm
		for (Edge e : edges) {
			int u = e.getEither();
			int v = e.getOther(u);

			if (!uf.areConnected(u, v)) {
				uf.union(u, v);
				mst.add(e);
				weight += e.getWeight();
			}
		}

	}

	public double getWeight() {
		return weight;
	}

	public Iterable<Edge> getMST() {
		return mst;
	}
}
