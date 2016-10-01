package Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

import Models.DirNode;
import Models.DirectorySwarm;

public class SwarmMenu {

	private JFrame frmSwarmDirectory;
	private JPanel panelOne;
	private JTextField textField;
	private String mainNodeName = "Target Directory";
	private Path defaultPath = FileSystems.getDefault().getPath(Paths.get("").toAbsolutePath().toString()+"\\src\\SwarmThis.txt");
 	private JCheckBox[] options;
	private JLabel warning;
	private JTextArea textArea;
	
public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwarmMenu window = new SwarmMenu();
					window.frmSwarmDirectory.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SwarmMenu() {
		initialize();
	}

	private void initialize() {
		frmSwarmDirectory = new JFrame();
		frmSwarmDirectory.setTitle("DirectorySwarm ");
		frmSwarmDirectory.setBounds(100, 100, 527, 458);
		frmSwarmDirectory.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSwarmDirectory.getContentPane().setLayout(null);
		
		JScrollPane scrollPaneOne = new JScrollPane();
		scrollPaneOne.setBounds(10, 11, 210, 239);
		frmSwarmDirectory.getContentPane().add(scrollPaneOne);
		
		panelOne = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelOne.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		scrollPaneOne.setViewportView(panelOne);
		
		JLabel lblSourceDirectory = new JLabel("Target Directory:");
		lblSourceDirectory.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblSourceDirectory.setBounds(230, 23, 127, 22);
		frmSwarmDirectory.getContentPane().add(lblSourceDirectory);
		
		textField = new JTextField();
		textField.setBounds(230, 47, 162, 20);
		frmSwarmDirectory.getContentPane().add(textField);
		textField.setColumns(10);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(230, 105, 271, 84);
		frmSwarmDirectory.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Options", null, panel, null);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JCheckBox chkDeleteFiles = new JCheckBox("Delete files that already exist");
		options = new JCheckBox[2];
		options[0] = chkDeleteFiles;
		panel.add(chkDeleteFiles);
		
		JCheckBox chkBreakDirectoryStructure = new JCheckBox("Break directory structure for the created files");
		options[1] = chkBreakDirectoryStructure;
		panel.add(chkBreakDirectoryStructure);
		
		JButton btnSelecionar = new JButton("Select...");
		btnSelecionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser file = new JFileChooser();
				file.setCurrentDirectory(new File(System.getProperty("user.home")));
				file.setFileSelectionMode(file.DIRECTORIES_ONLY);
				file.showOpenDialog(frmSwarmDirectory);
				textField.setText( file.getSelectedFile().getPath() );
			}
		});
		btnSelecionar.setBounds(400, 46, 101, 23);
		frmSwarmDirectory.getContentPane().add(btnSelecionar);
		
		JButton btnSwarm = new JButton("Swarm");
		btnSwarm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if(!textField.getText().isEmpty()){
					DirNode<String> dirNode = DirectorySwarm.getDirStructure();
					DirectorySwarm.SwarmSearchAndCreateDirs(textField.getText()+"\\",dirNode, options);
				}else warning.setText("No Target.");
			}
		});
		btnSwarm.setBounds(415, 220, 86, 30);
		frmSwarmDirectory.getContentPane().add(btnSwarm);
		
		warning = new JLabel("");
		warning.setForeground(Color.RED);
		warning.setFont(new Font("Tahoma", Font.PLAIN, 12));
		warning.setBounds(230, 72, 127, 22);
		frmSwarmDirectory.getContentPane().add(warning);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 283, 464, 113);
		frmSwarmDirectory.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Console", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 261, 491, 147);
		frmSwarmDirectory.getContentPane().add(panel_1);
		
		JButton btnCompile = new JButton("Compile");
		btnCompile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {										
					FileWriter fw = new     FileWriter(defaultPath.toString());
					BufferedWriter bw = new BufferedWriter(fw);

					bw.write(textArea.getText());
					bw.flush();
				}catch(Exception ex){
					
				}
				JTreeInitializeRepaint();
			}
		});
		btnCompile.setBounds(311, 220, 95, 30);
		frmSwarmDirectory.getContentPane().add(btnCompile);
		JTreeInitializeRepaint();
	}

	private void JTreeInitializeRepaint(){
		
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(mainNodeName);
		DefaultMutableTreeNode[] treeRef = new DefaultMutableTreeNode[1];
		treeRef[0] = treeNode;						
		JTree tree;

		DirectorySwarm s = new DirectorySwarm();

			treeNode = s.LoadTreeNode(mainNodeName);
			tree = new JTree();
			tree.setModel(new DefaultTreeModel(treeNode));
			tree.setCellRenderer(new TreeCellRenderer(){

				private JLabel label;
				private Object o;
				public Component getTreeCellRendererComponent(JTree arg0,
						Object arg1, boolean arg2, boolean arg3, boolean arg4,
						int arg5, boolean arg6) {
					label = new JLabel();	
					o = ((DefaultMutableTreeNode) arg1).getUserObject();
					
		            if (o instanceof DirNode) {
		            	DirNode<String> node = (DirNode<String>) o;
		                if (node.isDir() == true) {
		            		label.setIcon(UIManager.getIcon("Tree.openIcon"));                
		                }else 
		            		label.setIcon(UIManager.getIcon("Tree.leafIcon"));                
		                label.setText(node.getValue());
		                
		            } else {
		                label.setIcon(null);
		                label.setText("" + arg1);
		            }
					Path path = defaultPath;
					ArrayList<String> lines=null;
					try {
						lines = (ArrayList<String>) Files.readAllLines(path);
					} catch (IOException e) {
						e.printStackTrace();
					}				
					String str="";
					for(String s : lines){
						str += s+"\n";
					}
					textArea.setText(str);
		            return label;				
		       }
				
			});
			panelOne.removeAll();
			panelOne.add(tree);			
		
		panelOne.repaint();
		panelOne.revalidate();
		frmSwarmDirectory.repaint();
		frmSwarmDirectory.revalidate();

	}
}
