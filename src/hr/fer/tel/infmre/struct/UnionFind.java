package hr.fer.tel.infmre.struct;

/**
 * Implementation of a weighted quick union UF with path compression.
 * M union-find operations on N objects takes ~ M log*(N)
 * Created by fhrenic on 06/12/2016.
 */
public class UnionFind {

	private final int[] id;
	private final int[] size;
	private int count;

	public UnionFind(int n) {
		count = n;
		id = new int[n];
		size = new int[n];
		for (int i = 0; i < n; i++) {
			id[i] = i;
			size[i] = 1;
		}
	}

	/**
	 * Get number of disjoint components in this structure.
	 *
	 * @return number of components
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Return true if the two sites are in the same component
	 *
	 * @param x first site
	 * @param y second site
	 * @return true if they are areConnected
	 */
	public boolean areConnected(int x, int y) {
		return find(x) == find(y);
	}

	/**
	 * Connects two sites if they are disjoint.
	 *
	 * @param x first site
	 * @param y second site
	 */
	public void union(int x, int y) {
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

		--count;
	}

	/**
	 * Find root of site x using path compression
	 *
	 * @param x site
	 * @return root of x
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
