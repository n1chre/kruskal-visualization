package hr.fer.tel.infmre;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fhrenic on 07/12/2016.
 */
class GUI {

	private hr.fer.tel.infmre.struct.Graph G;
	private ArrayList<hr.fer.tel.infmre.struct.Edge> edges;

	private final Graph graph;
	private final Map<hr.fer.tel.infmre.struct.Edge, Edge> mapper = new HashMap<>();
	private int index;
	private final int n;

	GUI(hr.fer.tel.infmre.struct.Graph g, Iterable<hr.fer.tel.infmre.struct.Edge> edges) {
		G = g;

		this.edges = new ArrayList<>();
		edges.forEach(this.edges::add);

		index = -1; // none marked
		n = this.edges.size();

		graph = createDefaultGraph();
		fillGraph();
	}

	private void fillGraph() {
		int edgeIndex = 0;

		for (hr.fer.tel.infmre.struct.Edge e : G.getEdges()) {
			int u = e.getEither();
			int v = e.getOther(u);

			String uid = Integer.toString(u);
			String vid = Integer.toString(v);

			graph.addEdge("" + edgeIndex, uid, vid);
			graph.getNode(uid).addAttribute("ui.label", u);
			graph.getNode(vid).addAttribute("ui.label", v);
			Edge ed = graph.getEdge("" + edgeIndex);

			ed.addAttribute("ui.label", e.getWeight());
			ed.addAttribute("layout.weight", e.getWeight());

			mapper.put(e, ed);

			edgeIndex++;
		}
	}

	void display() {

		Viewer v = graph.display();


		v.getDefaultView().addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// ignore
			}

			@Override
			public void keyPressed(KeyEvent e) {
				GUI g = GUI.this;
				switch (e.getKeyCode()) {
					case 78: // n -> next
						if (g.index == g.n - 1) {
							return;
						}
						g.index++;
						markEdge(true);
						break;
					case 80: // p -> previous
						if (g.index == -1) {
							return;
						}
						markEdge(false);
						g.index--;
						break;
					case 81: // q -> quit
						System.exit(0);
						break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// ignore
			}
		});

	}

	private void markEdge(boolean mark) {
		String color = mark ? "red" : "black";
		mapper.get(edges.get(index))
				.setAttribute("ui.style", "fill-color: " + color + ";");
	}

	private Graph createDefaultGraph() {
		Graph graph = new SingleGraph("Kruskal graph");
		graph.setStrict(false);
		graph.setAutoCreate(true);
		graph.setAttribute("ui.antialias");
		String css = "node{size:15px;fill-color:green;}edge{text-alignment:along;text-size:20;}";
		graph.addAttribute("ui.stylesheet", css);
		return graph;
	}
}
