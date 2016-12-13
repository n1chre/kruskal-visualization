package hr.fer.tel.infmre;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementacija Kruskal algoritma za nalaženje najmanjeg razapinjućeg stabla u grafu.
 */
class KruskalMST {

	/**
	 * Lista grana koje predstavljaju najmanje razapinjuće stablo danog grafa
	 */
	private final List<Edge> mst;

	/**
	 * Pokreće Kruskal algoritam na danom grafu
	 *
	 * @param g graf
	 */
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
			}
		}
	}

	/**
	 * @return grane najmanjeg razapinjućeg stabla
	 */
	Iterable<Edge> getMST() {
		return mst;
	}
}
