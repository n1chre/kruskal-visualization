package hr.fer.tel.infmre.struct;

/**
 * Created by fhrenic on 06/12/2016.
 */
public class Edge implements Comparable<Edge> {

	private final int u;
	private final int v;
	private double w;

	public Edge(int u, int v, double w) {
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
		return Double.compare(w, e.w);
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
		return Double.compare(edge.w, w) == 0;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = u;
		result = 31 * result + v;
		temp = Double.doubleToLongBits(w);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
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

	public double getWeight() {
		return w;
	}
}
