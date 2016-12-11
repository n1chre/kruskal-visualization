package hr.fer.tel.infmre;

import hr.fer.tel.infmre.struct.Edge;
import hr.fer.tel.infmre.struct.Graph;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) {

		InputStream stream;
		if (args.length > 0) {
			try {
				Path p = Paths.get(args[0]);
				stream = Files.newInputStream(p);
			} catch (IOException ex) {
				System.err.println("File " + args[0] + " doesn't exist");
				return;
			}
		} else {
			stream = System.in;
		}

		// input graph
		Graph G;
		try {
			G = Graph.fromStream(stream);
		} catch (Exception ex) {
			System.err.println("Error while reading graph: " + ex.getMessage());
			return;
		}

		// calculate mst
		KruskalMST mst = new KruskalMST(G);
		Iterable<Edge> edges = mst.getMST();

		GUI gui = new GUI(G, edges);
		gui.display();
	}

}
