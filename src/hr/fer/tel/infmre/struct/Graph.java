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
 * Klasa predstavlja neusmjereni graf.
 * Čvorovi se označavaju nenegativnim brojevima.
 */
public class Graph {

	/**
	 * Uzorak za pronalaženje brojeva.
	 */
	private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

	/**
	 * Broj čvorova u grafu.
	 */
	private final int V;

	/**
	 * Lista grana koje se nalaze u grafu
	 */
	private final List<Edge> edges;

	/**
	 * Učitava graf iz danog toka podataka.
	 * Prva linija treba sadržavati (minimalno) jedan broj koji predstavlja broj čvorova u grafu.
	 * Sve ostale linije trebaju sadržavati (minimalno) tri broja, koji predstavljaju 2 čvora koje
	 * grana spaja i njezinu tezinu.
	 *
	 * @param stream tok podataka
	 * @return učitani graf
	 */
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
	 * Stvara prazan graf sa zadanim brojem čvorova. Čvorovi mogu biti u rasponu [0,V-1]
	 *
	 * @param V broj čvorova
	 */
	private Graph(int V) {
		this.V = V;
		edges = new LinkedList<>();
	}

	/**
	 * Vraća kolekciju svih grana u ovom grafu
	 *
	 * @return sve grane
	 */
	public Collection<Edge> getEdges() {
		return edges;
	}

	/**
	 * Dodaje novu granu u graf sa zadanim čvorovima i težinom
	 *
	 * @param u prvi čvor
	 * @param v drugi čvor
	 * @param w težina
	 */
	private void addEdge(int u, int v, int w) {
		validateVertex(u);
		validateVertex(v);
		if (w < 0) {
			throw new IllegalArgumentException("Weight can't be negative");
		}
		edges.add(new Edge(Math.min(u, v), Math.max(u, v), w));
	}

	/**
	 * Vraća broj čvorova u grafu
	 *
	 * @return broj čvorova
	 */
	public int getV() {
		return V;
	}

	/**
	 * @return tekstualna reprezentacija grafa
	 */
	@Override
	public String toString() {
		return edges.stream().map(Edge::toString).collect(Collectors.joining("\n"));
	}

	/**
	 * Ako je čvor negativan ili veći od maksimalnog broja, baca iznimku.
	 *
	 * @param v čvor
	 */
	private void validateVertex(int v) {
		if (v < 0 || v >= V) {
			throw new IllegalArgumentException("Vertex index out of bounds, should be between 0 and " + (V - 1));
		}
	}
}
