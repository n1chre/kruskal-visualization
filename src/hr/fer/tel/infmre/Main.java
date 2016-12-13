package hr.fer.tel.infmre;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * Glavna klasa koja je zaslužna za:
 * - ucitavanje grafa
 * - pronalazenje najmanjeg razapinjućeg stabla uporabom Kruskal algoritma
 * - prikaza grafa, postupka i rješenja
 */
class Main extends JFrame {

	private static final long serialVersionUID = -804177406404724792L;

	/**
	 * Ulazna metoda u program. Može primiti datoteku u kojoj je zapisan graf preko argumenata.
	 * Ako tih argumenata nema onda će čitati graf sa standardnog ulaza.
	 *
	 * @param args može sadržavati putanju do datoteke
	 */
	public static void main(String[] args) {

		InputStream stream;
		if (args.length > 0) {
			try {
				Path p = Paths.get(args[0]);
				stream = Files.newInputStream(p);
			} catch (IOException ex) {
				stream = System.in;
			}
		} else {
			stream = System.in;
		}

		// read graph
		hr.fer.tel.infmre.Graph G;
		try {
			G = hr.fer.tel.infmre.Graph.fromStream(stream);
		} catch (Exception ex) {
			System.err.println("Error while reading graph: " + ex.getMessage());
			return;
		}

		// calculate mst
		KruskalMST mst = new KruskalMST(G);

		Main g = new Main(G.getEdges(), mst.getMST());
		SwingUtilities.invokeLater(g::display);
	}

	/**
	 * Labele za ispis grana
	 */
	private final List<JLabel> labels;

	/**
	 * Viewer koji sadrži prikaz grafa
	 */
	private Viewer viewer;

	private final Graph graph;

	/**
	 * Lista svih grana
	 */
	private final List<hr.fer.tel.infmre.Edge> allEdges;

	/**
	 * Lista grana koje su dio najmanjeg razapinjućeg stabla grafa
	 */
	private final Set<hr.fer.tel.infmre.Edge> mstEdges;

	/**
	 * Mapira granu iz reprezentacije grafa u granu koja se iscrtava na ekranu
	 */
	private final Map<hr.fer.tel.infmre.Edge, Edge> mapper;

	/**
	 * Trenutni index grane koja se prikazuje
	 */
	private int index;

	/**
	 * Ukupan broj grana
	 */
	private final int n;

	/**
	 * Stvara novu glavnu klasu s danim granam
	 *
	 * @param allEdges sve grane
	 * @param mstEdges grane koje spadaju u najmanje razapinjuće stablo
	 */
	private Main(Iterable<hr.fer.tel.infmre.Edge> allEdges, Iterable<hr.fer.tel.infmre.Edge> mstEdges) {

		labels = new ArrayList<>();

		this.allEdges = new ArrayList<>();
		allEdges.forEach(this.allEdges::add);
		this.allEdges.sort(null);

		this.mstEdges = new HashSet<>();
		mstEdges.forEach(this.mstEdges::add);

		mapper = new HashMap<>();

		index = -1; // none marked
		n = this.allEdges.size();

		graph = createDefaultGraph();
		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
	}

	/**
	 * Prikaži graf i cijelo sučelje
	 */
	private void display() {

		// create default pane
		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// create right panel for edge labels and buttons
		JPanel edgePanel = new JPanel();
		edgePanel.setLayout(new BorderLayout());
		JButton prev = new JButton(new AbstractAction("Prethodni") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (index == -1) {
					return;
				}
				markEdge(false); // un mark
				--index;
			}
		});
		JButton next = new JButton(new AbstractAction("Sljedeći") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (index == n - 1) {
					return;
				}
				++index;
				markEdge(true); // mark
			}
		});
		JPanel btns = new JPanel(new GridLayout(1, 2));
		btns.add(prev);
		btns.add(next);
		edgePanel.add(btns, BorderLayout.SOUTH);

		// create labels
		JPanel panel = new JPanel(new GridLayout(n, 1));
		for (hr.fer.tel.infmre.Edge e : allEdges) {
			JLabel l = new JLabel(e.toString());
			labels.add(l);
			panel.add(l);
		}
		edgePanel.add(new JScrollPane(panel), BorderLayout.NORTH);
		contentPane.add(edgePanel, BorderLayout.EAST);

		fillGraph(); // fill entire graph

		viewer.enableAutoLayout();
		JPanel view = viewer.addDefaultView(false);

		// finish setup
		contentPane.add(view, BorderLayout.CENTER);
		setTitle("Kruskal mrežni algoritam");
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * Označi ili odznači neku granu
	 *
	 * @param mark ako je true, onda se grana označi
	 */
	private void markEdge(boolean mark) {
		hr.fer.tel.infmre.Edge edge = allEdges.get(index);

		String color, size;
		Color jcolor;

		if (mark) {
			if (mstEdges.contains(edge)) {
				jcolor = Color.green;
				color = "green";
			} else {
				jcolor = Color.red;
				color = "red";
			}
			size = "3";
		} else {
			jcolor = Color.black;
			color = "black";
			size = "1";
		}

		JLabel label = labels.get(index);
		label.setForeground(jcolor);

		Edge e = mapper.get(edge);
		e.setAttribute("ui.style", "fill-color: " + color + ";");
		e.setAttribute("ui.style", "size: " + size + "px;");
	}

	/**
	 * Popuni graf koji će se prikazati sa svim granama
	 */
	private void fillGraph() {
		int edgeIndex = 0;

		for (hr.fer.tel.infmre.Edge e : allEdges) {
			int u = e.getEither();
			int v = e.getOther(u);

			String uid = Integer.toString(u);
			String vid = Integer.toString(v);
			String eid = Integer.toString(++edgeIndex);

			Edge ed = graph.addEdge(eid, uid, vid);
			graph.getNode(uid).addAttribute("ui.label", u);
			graph.getNode(vid).addAttribute("ui.label", v);

			ed.addAttribute("ui.label", e.getWeight());
			ed.addAttribute("layout.weight", e.getWeight());

			mapper.put(e, ed);
		}
	}

	/**
	 * Stvori početni prazni graf za prikaz
	 *
	 * @return stvoreni graf
	 */
	private Graph createDefaultGraph() {
		Graph graph = new SingleGraph("Kruskal graph");
		graph.setStrict(false);
		graph.setAutoCreate(true);
		graph.setAttribute("ui.antialias");
		String css = "node{size:15px;fill-color:green;}edge{text-alignment:along;text-size:20;size-mode:dyn-size;}";
		graph.addAttribute("ui.stylesheet", css);
		return graph;
	}
}
