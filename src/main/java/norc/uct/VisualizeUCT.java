package norc.uct;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.LinkedList;

import norc.State;
import norc.uct.UCTNodes.*;


/**
 * Visualize UCT planning tree. You must have graphviz installed with the
 * dot executable located in /usr/bin/dot.
 * @author Jeshua Bratman
 */
public class VisualizeUCT {

	public static void vis(UCT uct) {
		UCTStateNode root = uct.root;
		State rootState = uct.rootState;
		File temp;
		BufferedWriter out;
		try {
			temp = File.createTempFile("disp", ".dot");
			out = new BufferedWriter(new FileWriter(temp));
			// create the graph
			String digraph = "digraph dispgraph { \n";
			Object state;
			LinkedList<Object> queue = new LinkedList<Object>();
			queue.add(root);
			digraph += "\""+root.toString() + "\" [label=\""+rootState.toString()+"\",shape=\"box\"]\n";
			HashSet<UCTActionNode> adrawn = new HashSet<UCTActionNode>();
			HashSet<UCTStateNode> sdrawn = new HashSet<UCTStateNode>();

			while (!queue.isEmpty()) {
				state = queue.poll();
				if (state instanceof UCTStateNode) {
					UCTStateNode st = ((UCTStateNode) state);
					final UCTActionNode[] children = st.children;

					for (int i = 0; i < children.length; i++) {	
						digraph += "\""+children[i].toString() 
								+ "\" [label=\""+String.format("a:%d Q=%.3f",i,st.Q[i])+"\",shape=\"triangle\"]\n";
					}System.out.println("State "+ st.state + " at depth "+ st.depth + " Has "+children.length+"children.");
					for (int i = 0; i < children.length; i++) {
						if(!sdrawn.contains(st)){
							digraph += "\"" + state.toString() + "\" -> \""
									+ children[i].toString()+"\" "							
									+" [label=\"#"+st.saCounts[i]+"\"]\n";
						}
						queue.add(children[i]);						
					}
					if(!sdrawn.contains(st)){ sdrawn.add(st);}
				} else { //uct action node
					UCTActionNode st = ((UCTActionNode) state);
					final UCTStateNode[] children = st.childNodes;					
					for (int i = 0; i < st.currBranches; i++) {
						if(!adrawn.contains(st)){
							digraph += "\""+children[i].toString() + "\" [label=\"s: "+st.childStates[i].toString()+"\",shape=\"box\"]\n";
							digraph += "\"" + state.toString() + "\" -> \""
									+ children[i].toString()
									+ "\"[label=\""
									+ children[i].sCount + "\"]\n";
						}
						queue.add(children[i]);
					}
					if(!adrawn.contains(st)){ adrawn.add(st);}
				}
			}
			digraph += "}\n";

			//System.out.println(digraph);
			out.write(digraph);
			
			// display the graph
			out.flush();
			String tmp_filename = temp.toString();
			String command = "/usr/bin/dot -Tps " + tmp_filename + " -o "
					+ tmp_filename + ".ps";
			Process p = Runtime.getRuntime().exec(command);

			// wait for dotty to close
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			command = "evince " + tmp_filename + ".ps";
			p = Runtime.getRuntime().exec(command);

			try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			temp.delete();
			out.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
}
