package hr.fer.tel.infmre;

import hr.fer.tel.infmre.struct.Edge;
import hr.fer.tel.infmre.struct.Graph;
import hr.fer.tel.infmre.struct.UnionFind;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by fhrenic on 06/12/2016.
 */
public class KruskalMST {

	private double weight;
	private final List<Edge> mst;

	public KruskalMST(Graph g) {

		mst = new LinkedList<>();
		Set<Edge> edges = new TreeSet<>(g.getEdges());
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
