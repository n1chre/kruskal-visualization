package hr.fer.tel.infmre.struct;

/**
 * Created by fhrenic on 06/12/2016.
 */
public class Edge implements Comparable<Edge> {

	private final int u;
	private final int v;
	private final int w;

	Edge(int u, int v, int w) {
		if (u < 0) {
			throw new IllegalArgumentException("vertex can't be negative");
		}
		if (v < 0) {
			throw new IllegalArgumentException("vertex can't be negative");
		}
		this.u = u;
		this.v = v;
		this.w = w;
	}

	@Override
	public int compareTo(Edge e) {
		return Integer.compare(w, e.w);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Edge)) {
			return false;
		}

		Edge edge = (Edge) o;

		if (u != edge.u) {
			return false;
		}
		if (v != edge.v) {
			return false;
		}
		return w == edge.w;
	}

	@Override
	public int hashCode() {
		int result = u;
		result = 31 * result + v;
		result = 31 * result + w;
		return result;
	}

	public int getEither() {
		return u;
	}

	public int getOther(int x) {
		if (u == x) {
			return v;
		}
		if (v == x) {
			return u;
		}
		throw new IllegalArgumentException("Edge doesn't contain given vertex " + x);
	}

	public int getWeight() {
		return w;
	}

	@Override
	public String toString() {
		return String.format("%d-%d [%d]", u, v, w);
	}
}
