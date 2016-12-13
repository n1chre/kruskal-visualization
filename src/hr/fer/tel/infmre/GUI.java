package hr.fer.tel.infmre;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

class GUI extends JFrame {

	private static final long serialVersionUID = -804177406404724792L;
	private final List<JLabel> labels;
	private Viewer viewer;

	private final Graph graph;
	private final List<hr.fer.tel.infmre.Edge> allEdges;
	private final Set<hr.fer.tel.infmre.Edge> mstEdges;
	private final Map<hr.fer.tel.infmre.Edge, Edge> mapper = new HashMap<>();
	private int index;
	private final int n;

	GUI(Iterable<hr.fer.tel.infmre.Edge> allEdges, Iterable<hr.fer.tel.infmre.Edge> mstEdges) {

		labels = new ArrayList<>();

		this.allEdges = new ArrayList<>();
		allEdges.forEach(this.allEdges::add);
		this.allEdges.sort(null);

		this.mstEdges = new HashSet<>();
		mstEdges.forEach(this.mstEdges::add);

		index = -1; // none marked
		n = this.allEdges.size();

		graph = createDefaultGraph();

		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
	}

	void display() {

		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel edgePanel = new JPanel();
		edgePanel.setLayout(new BorderLayout());
		JButton prev = new JButton(new AbstractAction("Previous") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (index == -1) {
					return;
				}
				markEdge(false); // un mark
				--index;
			}
		});
		JButton next = new JButton(new AbstractAction("Next") {
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

		JPanel panel = new JPanel(new GridLayout(n, 1));
		for (hr.fer.tel.infmre.Edge e : allEdges) {
			JLabel l = new JLabel(e.toString());
			labels.add(l);
			panel.add(l);
		}
		edgePanel.add(new JScrollPane(panel), BorderLayout.NORTH);
		contentPane.add(edgePanel, BorderLayout.EAST);

		fillGraph();

		viewer.enableAutoLayout();
		JPanel view = viewer.addDefaultView(false);

		contentPane.add(view, BorderLayout.CENTER);
		setTitle("Kruskal example");
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private void fillGraph() {
		int edgeIndex = 0;

		for (hr.fer.tel.infmre.Edge e : allEdges) {
			int u = e.getEither();
			int v = e.getOther(u);

			String uid = Integer.toString(u);
			String vid = Integer.toString(v);
			String eid = Integer.toString(edgeIndex);

			Edge ed = graph.addEdge(eid, uid, vid);
			graph.getNode(uid).addAttribute("ui.label", u);
			graph.getNode(vid).addAttribute("ui.label", v);
			edgeIndex++;
			ed.addAttribute("ui.label", e.getWeight());
			ed.addAttribute("layout.weight", e.getWeight());
			mapper.put(e, ed);
		}
	}

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

		labels.get(index).setForeground(jcolor);

		Edge e = mapper.get(edge);
		e.setAttribute("ui.style", "fill-color: " + color + ";");
		e.setAttribute("ui.style", "size: " + size + "px;");
	}

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
