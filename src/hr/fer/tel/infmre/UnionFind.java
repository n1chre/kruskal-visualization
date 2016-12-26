package hr.fer.tel.infmre;

/**
 * Implementacija UnionFind strukture sa skraćivanjem puta i težinskim spajanjem.
 * Union/Find funkcije imaju složenost O(log*N) gdje je N broj čvorova u strukturi,
 * a log* funkcija koja je manja od 5 za sve normalne vrijednosti N (N manji od 2^65536).
 */
class UnionFind {

	/**
	 * ID čvora kojem pripada čvor na i-tom mjestu
	 */
	private final int[] id;

	/**
	 * Veličina stabla ispod čvora na i-tom mjestu
	 */
	private final int[] size;

	/**
	 * Stvara novu strukturu od n čvorova.
	 *
	 * @param n broj čvorova
	 */
	UnionFind(int n) {
		id = new int[n];
		size = new int[n];
		for (int i = 0; i < n; i++) {
			id[i] = i;
			size[i] = 1;
		}
	}

	/**
	 * @param x prvi čvor
	 * @param y drugi čvor
	 * @return true ako su čvorovi spojeni, false inače
	 */
	boolean areConnected(int x, int y) {
		return find(x) == find(y);
	}

	/**
	 * Spaja 2 čvora ako nisu spojeni
	 *
	 * @param x prvi čvor
	 * @param y drugi čvor
	 */
	void union(int x, int y) {
		int rootX = find(x);
		int rootY = find(y);

		if (rootX == rootY) {
			return;
		}

		// smaller root points to larger one
		if (size[rootX] < size[rootY]) {
			id[rootX] = rootY;
			size[rootY] += size[rootX];
		} else {
			id[rootY] = rootX;
			size[rootX] += size[rootY];
		}
	}

	/**
	 * Nalazi korijen stabla u kojem se nalazi dani čvora te usput radi skraćivanje puta.
	 *
	 * @param x čvor
	 * @return korijen čvora
	 */
	private int find(int x) {
		int n = id.length;
		if (x < 0 || x >= n) {
			throw new IndexOutOfBoundsException("index " + x + " not between 0 and " + (n - 1));
		}

		while (x != id[x]) {
			id[x] = id[id[x]]; // path compression: makes tree almost flat
			x = id[x];
		}

		return x;
	}

}
