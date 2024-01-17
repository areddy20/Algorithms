//***************************************************************
// TODO: Nothing, all done. You may read this if you'd like,
// but you may not need to.
//***************************************************************


import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.util.EdgeType;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;

import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import java.util.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.BorderLayout;

import java.awt.geom.Ellipse2D;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *  GUI for forest interactions.
 *  
 *  @author Katherine (Raven) Russell
 */
class SimGUI {
	/**
	 *  The default animation speed for "play".
	 */
	private static final int DEFAULT_ANIMATION_SPEED = 800;
	
	/**
	 *  The set animation speed.
	 */
	private int animationSpeed;
	
	/**
	 *  Frame for the GUI.
	 */
	private JFrame frame;
	
	/**
	 *  Current algorithm simulation.
	 */
	private HuffmanAlg alg = null;
	
	/**
	 *  The panel containing the forest display.
	 */
	private Forest<HuffmanTreeNode, TreeEdge> forest = null;
	
	/**
	 * The current tree layout
	 */
	private TreeLayout<HuffmanTreeNode, TreeEdge> currentLayout = null;
	
	/**
	 *  The panel containing the forest display.
	 */
	private VisualizationViewer<HuffmanTreeNode, TreeEdge> visServer = null;
	
	/**
	 *  Model for mouse.
	 */
	private ModalGraphMouse gm;
	
	/**
	 *  The panel containing the step, reset, and play buttons.
	 */
	private JPanel buttonPanel = null;
	
	/**
	 *  The panel containing the information the algorithm would like to
	 *  display on the right side of the visualization.
	 */
	private JPanel sidePanel = null;
	
	/**
	 *  The area for displaying the binary (uncompressed).
	 */
	protected JTextArea outTextUncomp = null;
	
	/**
	 *  The area for displaying the binary (compressed).
	 */
	protected JTextArea outTextComp = null;
	
	/**
	 *  The panel containing the information the algorithm would like to
	 *  display on the top side of the visualization.
	 */
	private JPanel topPanel = null;
	
	/**
	 *  The area for typing in the top panel.
	 */
	protected JTextArea textArea = null;
	
	/**
	 *  Whether or not a simulation is currently playing with
	 *  the play button (i.e. automatically playing).
	 */
	private boolean playing = false;
	
	/**
	 *  The text to load into the GUI from the command line.
	 */
	private String defaultText = null;
	
	/**
	 *  Load up the GUI.
	 */
	public SimGUI(String defaultText, int animationSpeed) {
		frame = new JFrame("Algorithm Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 700);
		frame.getContentPane().setLayout(new BorderLayout(0,0));
		
		this.defaultText = defaultText;
		this.animationSpeed = (animationSpeed < 100) ? 100 : animationSpeed;
		
		resetAlg();
		makeMenu(); //needs to go after so gm is set
		
		frame.setVisible(true);
	}
	
	/**
	 *  Makes the menu for the simulation.
	 */
	public void makeMenu() {
		frame.setJMenuBar(null);
		JMenuBar menuBar = new JMenuBar();
		
		//exit option
		JMenu simMenu = new JMenu("Simulation");
		simMenu.setPreferredSize(new Dimension(80,20)); // Change the size 
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		simMenu.add(exit);
		menuBar.add(simMenu);
		
		frame.setJMenuBar(menuBar);
	}
	
	/**
	 *  Makes the forest components.
	 */
	public void makeForestPanel() {
		if(alg == null) return;
		if(visServer != null) frame.remove(visServer);
		
		currentLayout = new TreeLayout<>(forest);
		//layout.setSize(new Dimension(frame.getWidth()-200,frame.getHeight()-80));
		visServer = new VisualizationViewer<>(currentLayout);
		visServer.setPreferredSize(new Dimension(frame.getWidth()-150,frame.getHeight()-30));
		
		visServer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		RenderContext<HuffmanTreeNode, TreeEdge> context = visServer.getRenderContext();
		
		//label edges with toString()
		context.setEdgeLabelTransformer(
			new Transformer<TreeEdge,String>(){
				public String transform(TreeEdge e) {
					return e.toString();
				}
			}
		);
		
		//color arrows with edge color
		context.setArrowFillPaintTransformer(
			new Transformer<TreeEdge,Paint>(){
				public Paint transform(TreeEdge e) {
					return e.getColor();
				}
			}
		);
		
		//color arrow outlines with edge color
		context.setArrowDrawPaintTransformer(
			new Transformer<TreeEdge,Paint>(){
				public Paint transform(TreeEdge e) {
					return e.getColor();
				}
			}
		);
		
		//color lines with edge color
		context.setEdgeDrawPaintTransformer(
			new Transformer<TreeEdge,Paint>(){
				public Paint transform(TreeEdge e) {
					return e.getColor();
				}
			}
		);
		
		//set edge line stroke to bolder
		context.setEdgeStrokeTransformer(
			new Transformer<TreeEdge,Stroke>(){
				public Stroke transform(TreeEdge e) {
					return new BasicStroke(3);
				}
			}
		);
		
		//move edge labels off the lines
		context.setLabelOffset(-10);
		
		//make nodes bigger and oval
		context.setVertexShapeTransformer(
			new Transformer<HuffmanTreeNode,Shape>(){
				public Shape transform(HuffmanTreeNode v) {
					int s = 80;
					return new Ellipse2D.Double(-s/2.0, -s/4.0, s, s/2);
				}
			}
		);
		
		//label vertices with toString()
		context.setVertexLabelTransformer(
			new Transformer<HuffmanTreeNode,String>(){
				public String transform(HuffmanTreeNode v) {
					return v.toString();
				}
			}
		);
		
		//color vertices with node color
		context.setVertexFillPaintTransformer(
			new Transformer<HuffmanTreeNode,Paint>(){
				public Paint transform(HuffmanTreeNode v) {
					return v.getColor();
				}
			}
		);
		
		//make nodes bigger
		context.setVertexFontTransformer(
			new Transformer<HuffmanTreeNode,Font>(){
				public Font transform(HuffmanTreeNode v) {
					return new Font("Serif",Font.PLAIN,14);
				}
			}
		);
		
		//Add user interactions
		gm = new DefaultModalGraphMouse();
		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		visServer.setGraphMouse(gm);
		
		//frame.add(visServer, 0);
		frame.add(visServer, BorderLayout.CENTER);
		frame.revalidate();
	}
	
	/**
	 *  Makes the graph components.
	 */
	public void makeAlgPanels() {
		if(alg == null) return;
		
		makeTopPanel();
		makeSidePanel();
	}
	
	/**
	 *  Makes the panel containing the step, reset, and play buttons.
	 */
	public void makeBottomButtons() {
		if(alg == null) return;
		if(buttonPanel != null) frame.remove(buttonPanel);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		
		//step button
		JButton step = new JButton("Step");
		step.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				step();
			}
		});
		buttonPanel.add(step);
		
		//reset button
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				resetAlg();
			}
		});
		buttonPanel.add(reset);
		
		//play button
		JButton play = new JButton("Play");
		play.addActionListener(new ActionListener() {
			private void toggle() {
				//toggle playing and not playing
				playing = !playing;
				buttonPanel.getComponent(0).setEnabled(!playing);
				buttonPanel.getComponent(1).setEnabled(!playing);
				((JButton)buttonPanel.getComponent(2)).setText((playing ? "Stop" : "Play"));
			}
			
			public void actionPerformed(ActionEvent event) {
				toggle();
				
				//if playing, kick off a timer
				if(playing) {
					new javax.swing.Timer(animationSpeed, new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							//someone hit the stop button
							if(!playing) {
								((javax.swing.Timer)event.getSource()).stop();
								return;
							}
							else {
								if(!step()) toggle();
							}
						}
					}).start();
				}
			}
		});
		buttonPanel.add(play);
		
		//frame.add(buttonPanel, 1);
		
		frame.add(buttonPanel, BorderLayout.PAGE_END);
		frame.revalidate();
	}
	
	/**
	 *  Calls the step button on the simulation and updates
	 *  the GUI to display the result.
	 *  
	 *  @return whether or not the simulation was able to step
	 */
	public boolean step() {
		//if we're in counting mode, we need to set (and highlight
		//in the GUI) the next character from the text box in the GUI
		int textSize = textArea.getText().length();
		Character currentChar = null;
		
		//we haven't started the algorithm
		if(!alg.isStarted()) {
			//grey out the text box
			textArea.setEditable(false);
			textArea.setBackground(Color.LIGHT_GRAY);
			
			//there is some text to work on
			if(textSize > 0) {
				//nned to request focus before trying to highlight
				textArea.requestFocus();
				
				//highlight the start
				textArea.select(0, 1);
				
				//and get that character
				currentChar = textArea.getSelectedText().charAt(0);
			}
		}
		//some text is selected already
		else if(textArea.getSelectedText() != null) {
			//figure out where we are
			int selectionEnd = textArea.getSelectionEnd();
			
			//this is needed before calling select() for a new area
			textArea.requestFocus();
			
			//there is more text to read
			if(textSize > selectionEnd) {
				//select the next character
				textArea.select(selectionEnd, selectionEnd+1);
				
				//and save it for later
				currentChar = textArea.getSelectedText().charAt(0);
			} 
			//we're out of text
			else {
				//reset some things
				textArea.select(0,0);
			}
		}
		//otherwise we have no new characters, and the algorithm
		//should have the default (null) character
		
		alg.setCurrentChar(currentChar);
		
		//actually step the algorithm
		boolean ret = alg.step();
		
		//animate any changes to the tree
		TreeLayout<HuffmanTreeNode, TreeEdge> newLayout = new TreeLayout<>(forest, 100, 100);
		
		LayoutTransition<HuffmanTreeNode,TreeEdge> transition = new LayoutTransition<>(visServer, currentLayout, newLayout);
		Animator animator = new Animator(transition);
		animator.start();
		visServer.getRenderContext().getMultiLayerTransformer().setToIdentity();
		
		visServer.repaint();
		//sidePanel.repaint();
		//topPanel.repaint();
		
		currentLayout = newLayout;
		
		//we're done with the algorithm, print out the encoding of the text
		if(!ret) {
			//unencoded text
			String text = this.textArea.getText();
			StringBuilder sb = new StringBuilder("ASCII Text:\n");
			for(int i = 0; i < text.length(); i++) {
				sb.append(Integer.toBinaryString((int)text.charAt(i)));
			}
			this.outTextUncomp.setText(sb.toString());
			this.outTextUncomp.setCaretPosition(0);
			
			//encoded text
			HashMap<Character,String> encoding = new HashMap<>();
			for(HuffmanTreeNode n : this.forest.getVertices()) {
				if(n.getLetter() != null && n.getEncoding() != null) {
					encoding.put(n.getLetter(), n.getEncoding());
				}
			}
			
			if(encoding.size() == 0) {
				this.outTextComp.setText("Encodings not set. This is ok if you haven't finished the project.");
			}
			else {
				sb = new StringBuilder("Compressed:\n");
				for(int i = 0; i < text.length(); i++) {
					sb.append(encoding.getOrDefault(text.charAt(i),""));
				}
				this.outTextComp.setText(sb.toString());
			}
			this.outTextComp.setCaretPosition(0);
		}
		
		return ret;
	}
	
	/**
	 *  Generates a new forest, resetting all the appropriate static variables
	 *  for nodes and edges.
	 */
	public void genForest() {
		this.forest = new OrderedDelegateForest<HuffmanTreeNode, TreeEdge>();
	}
	
	/**
	 *  Load a new simulation.
	 */
	public void resetAlg() {
		if(alg == null) alg = new HuffmanAlg();
		
		genForest();
		alg.reset(forest);
		
		makeForestPanel();
		makeMenu();
		makeBottomButtons();
		makeAlgPanels();
	}
	
	/**
	 *  What the algorithm would like to display on the top.
	 */
	public void makeTopPanel() {
		//we're resetting the top panel
		if(topPanel != null) {
			this.textArea.setEditable(true);
			this.textArea.setBackground(Color.WHITE);
			this.textArea.setCaretPosition(this.textArea.getText().length());
		}
		//we're making the top panel
		else {
			int width = frame.getWidth();
			int height = 100;
				
			this.topPanel = new JPanel();
			this.topPanel.setLayout(new BorderLayout());
			
			this.textArea = new JTextArea();
			this.textArea.setEditable(true);
			this.textArea.setBackground(Color.WHITE);
			this.textArea.setSelectionColor(Color.RED);
			this.textArea.setFont(new Font("Serif",Font.PLAIN,20));
			this.textArea.setText(this.defaultText);
			JScrollPane scrollPane = new JScrollPane(this.textArea);
			
			this.topPanel.setPreferredSize(new Dimension(width,height));
			this.topPanel.add(scrollPane);
			
			frame.add(topPanel, BorderLayout.PAGE_START);
			topPanel.setVisible(true);
			frame.revalidate();
		}
	}
	
	/**
	 *  What the algorithm would like to display on the right.
	 */
	public void makeSidePanel() {
		//we're resetting the side panel
		if(this.sidePanel != null) {
			this.outTextUncomp.setText("");
			this.outTextComp.setText("");
		}
		//we're making the side panel
		else {
			int width = 150;
			int height = frame.getHeight();
			
			this.sidePanel = new JPanel();
			this.sidePanel.setLayout(new GridLayout(2, 1));
			this.sidePanel.setPreferredSize(new Dimension(width,height));
			
			this.outTextUncomp = new JTextArea();
			this.outTextUncomp.setEditable(false);
			this.outTextUncomp.setBackground(Color.LIGHT_GRAY);
			this.outTextUncomp.setLineWrap(true);
			JScrollPane scrollPane = new JScrollPane(this.outTextUncomp);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			this.sidePanel.add(scrollPane);
			
			this.outTextComp = new JTextArea();
			this.outTextComp.setEditable(false);
			this.outTextComp.setBackground(Color.LIGHT_GRAY);
			this.outTextComp.setLineWrap(true);
			scrollPane = new JScrollPane(this.outTextComp);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			this.sidePanel.add(scrollPane);
			
			frame.add(this.sidePanel, BorderLayout.LINE_END);
			this.sidePanel.setVisible(true);
			frame.revalidate();
		}
	}
	
	/**
	 *  A main method to run the simulation with GUI.
	 *  
	 *  @param args [0] = the seed for the alg's random number generator
	 */
	public static void main(String[] args) {
		String callInstructions = "Call with one of the following commands:\njava SIMGui\njava SIMGui [string]";
		
		if(args.length == 0) {
			new SimGUI("", SimGUI.DEFAULT_ANIMATION_SPEED);
		}
		else if(args.length == 1) {
			new SimGUI(args[0], SimGUI.DEFAULT_ANIMATION_SPEED);
		}
		else if(args.length == 2) {
			try {
				new SimGUI(args[0], Integer.parseInt(args[1]));
			}
			catch(NumberFormatException e) {
				System.out.println("Second argument must be an integer.\n" + callInstructions);
			}
		}
		else {
			System.out.println(callInstructions);
		}
	}
}
