package hr.fer.tel.infmre.struct;

/**
 * Ova klasa predstavlja granu u neusmjerenom grafu.
 * Čvorovi se predstavljaju cijelim nenegativnim brojevima.
 * Težina grafa mozže biti nenegativan cijeli broj.
 */
public class Edge implements Comparable<Edge> {

	/**
	 * Jedan čvor ove grane
	 */
	private final int u;

	/**
	 * Drugi čvor ove grane
	 */
	private final int v;

	/**
	 * Težina grane
	 */
	private final int w;

	/**
	 * Stvara novu granu sa zadanim čvorovima i težinom
	 *
	 * @param u prvi čvor
	 * @param v drugi čvor
	 * @param w težina grane
	 */
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

	/**
	 * Usporeduje dvije grane po njihovim težinama.
	 *
	 * @param e grana s kojom se usporeduje
	 * @return -1, 0 ili 1 ovisno ako je ova grana manja, jednaka ili veća od druge grane
	 */
	@Override
	public int compareTo(Edge e) {
		return Integer.compare(w, e.w);
	}

	/**
	 * Usporeduje jesu li dvije grane jednake.
	 *
	 * @param o druga grana
	 * @return true ako su jednake, false inače
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Edge)) {
			return false;
		}

		Edge edge = (Edge) o;

		return u == edge.u && v == edge.v && w == edge.w;
	}

	/**
	 * Računa sažetak za ovu granu
	 *
	 * @return sažetak
	 */
	@Override
	public int hashCode() {
		int result = u;
		result = 31 * result + v;
		result = 31 * result + w;
		return result;
	}

	/**
	 * Vraća bilo koji od čvorova koji su spojeni ovom granom
	 *
	 * @return neki čvor
	 */
	public int getEither() {
		return u;
	}

	/**
	 * Za dani čvor na ovoj grani, vraća onaj drugi.
	 *
	 * @param x neki od čvorova na grani
	 * @return drugi čvor
	 */
	public int getOther(int x) {
		if (u == x) {
			return v;
		}
		if (v == x) {
			return u;
		}
		throw new IllegalArgumentException("Edge doesn't contain given vertex " + x);
	}

	/**
	 * Vraća težinu grane
	 *
	 * @return težina
	 */
	public int getWeight() {
		return w;
	}

	/**
	 * Vraća tekstualnu reprezentaciju ove grane
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return String.format("%d-%d [%d]", u, v, w);
	}
}
