package hr.fer.tel.infmre.struct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by fhrenic on 06/12/2016.
 */
public class Graph {

	public static void main(String[] args) {
		String line = "41, 22, 33, d";
	}

	private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

	private final int V;
	private List<Edge> edges = new LinkedList<>();

	public static Graph fromStream(InputStream stream) {

		Graph G;
		Matcher m;

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {

			String line = reader.readLine();
			m = NUMBER_PATTERN.matcher(line);
			if (!m.find()) {
				throw new IllegalArgumentException("First line must contain the number of vertices");
			}
			final int V = Integer.parseInt(m.group());

			G = new Graph(V);

			line = reader.readLine();
			while (line != null) {
				m = NUMBER_PATTERN.matcher(line);

				int[] arr = new int[3];
				int idx = 0;
				while (m.find() && idx < 3) {
					arr[idx++] = Integer.parseInt(m.group());
				}
				if (idx != 3) {
					throw new IllegalArgumentException("Too few values in line");
				}

				G.addEdge(arr[0], arr[1], arr[2]);
				line = reader.readLine();
			}


		} catch (IOException ex) {
			System.err.println("Error while reading graph");
			throw new IllegalArgumentException(ex);
		}

		return G;
	}

	/**
	 * Number of nodes. Nodes can be from 0 to V-1
	 *
	 * @param V number of nodes
	 */
	public Graph(int V) {
		this.V = V;
		edges = new LinkedList<>();
	}

	public Collection<Edge> getEdges() {
		return edges;
	}

	public void addEdge(int u, int v, int w) {
		validateVertex(u);
		validateVertex(v);
		edges.add(new Edge(Math.min(u, v), Math.max(u, v), w));
	}

	public int getV() {
		return V;
	}

	@Override
	public String toString() {
		return edges.stream().map(Edge::toString).collect(Collectors.joining("\n"));
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= V) {
			throw new IllegalArgumentException("Vertex index out of bounds, should be between 0 and " + (V - 1));
		}
	}
}
