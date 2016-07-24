package Models;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JCheckBox;
import javax.swing.tree.DefaultMutableTreeNode;

public class DirectorySwarm {

	private final String SOURCE_PATH = Paths.get("").toAbsolutePath().toString()+"\\src\\";
	private static int id = 100;
	private static DirNode<String> dirStructure;
	
	public DefaultMutableTreeNode LoadTreeNode(String nodeName){
		boolean flag=false;
		Path path = null;

			path = FileSystems.getDefault().getPath(SOURCE_PATH+"SwarmThis.txt");
			if(Files.isReadable(path)){flag=true;}
			else{
				Scanner s = new Scanner(System.in);
				path = FileSystems.getDefault().getPath(s.next());	
				s.close();
				if(Files.isReadable(path)){
					flag=true;
				}
			}
		if(flag){
				try {
					ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(path);				
					for(String line : lines){
						String[] aux = line.split("\t");
						line = "";
						for(String newLine : aux){if(newLine.equals(""))line += "..\\";else line += newLine;}
					}
					ArrayList[] linesRef = (ArrayList[]) Array.newInstance(lines.getClass(), 1);
					linesRef[0]=lines;
					dirStructure = new DirNode<String>(this.id,nodeName);
					DirNode[] dirRef = (DirNode[]) Array.newInstance(dirStructure.getClass(), 1);
					dirRef[0] = dirStructure;					
					
					DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(nodeName);
					DefaultMutableTreeNode[] treeRef = new DefaultMutableTreeNode[1];
					treeRef[0] = treeNode;		
					
					SwarmDirsToNodes(dirRef,linesRef);
					theRecursiveSwarm(dirStructure, treeRef);
					return treeNode;
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
	private void theRecursiveSwarm(DirNode<String> graph,	DefaultMutableTreeNode[] treeNode){
		
		for(DirNode<String> node : graph.getEdges()){
												
			DefaultMutableTreeNode aux = new DefaultMutableTreeNode(node);
			
			DefaultMutableTreeNode[] auxRef = new DefaultMutableTreeNode[1];
			auxRef[0] = aux;
			if(node.getEdges().size()>0)theRecursiveSwarm(node,auxRef);
			treeNode[0].add(auxRef[0]);
		}
		System.out.println();

	}

	private void SwarmDirsToNodes(DirNode<String>[] tree,ArrayList<String>[] directories){

		DirNode[] lastRef = (DirNode[]) Array.newInstance(tree[0].getClass(), 1);
		ArrayList<String> dirAux = new ArrayList<String>();
		ArrayList[] dirRef = (ArrayList[]) Array.newInstance(dirAux.getClass(), 1);
		dirRef[0]=dirAux;
		Iterator<?> it = directories[0].iterator();
		boolean lastIterationFlag = false;
		String line = "";
		while(it.hasNext()){
			if(lastIterationFlag){
				lastIterationFlag = false;
			}
			else line = it.next().toString();
			
			if(line.startsWith("\t")){
				while(true){
					if(line.startsWith("\t")){
						dirAux.add(line.split("\t",2)[1]);
						if(it.hasNext()){
							line=it.next().toString();
							lastIterationFlag = true;
						}else break;
					}
					else break;
				}
				
				SwarmDirsToNodes(lastRef,dirRef);
				
				dirAux.clear();

			}
			else {
				
				if(line.contains("\\")){
					DirNode<String> aux = new DirNode<String>(
							++id,
							line,
							true,
							null
					);
					lastRef[0] = aux;
					tree[0].addNode(aux);	//graph.isEmpty()? null:graph.get(id-151)
				}
				
				else {
					tree[0].addNode(new DirNode<String>(++id,line));
				}

			} 

		}
		
	}
	
	public static void SwarmSearchAndCreateDirs(String DirAbsPath, DirNode<String> currentDir,JCheckBox[] options){
		Path path;	
		if(currentDir != null){
			for(DirNode<String> node : currentDir.getEdges()){
				String auxString = DirAbsPath;
;
				if(node.isDir()){
					if(!options[1].isSelected()){
						auxString+=node.getValue();
						path = FileSystems.getDefault().getPath(auxString);
					}else path = FileSystems.getDefault().getPath(auxString+node.getValue());
					
					try{Files.createDirectory(path);}catch(FileAlreadyExistsException ex){} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}else{				
					try{
						path = FileSystems.getDefault().getPath(auxString+node.getValue());
						if(options[0].isSelected())Files.deleteIfExists(path);
						Files.createFile(path);
					}catch(FileAlreadyExistsException ex){} catch (IOException e) {
						System.out.println(e.getMessage());
					}

				}

				if(!node.getEdges().isEmpty()){
					if(options[1].isSelected()){
						SwarmSearchAndCreateDirs(DirAbsPath, node, options);
					}else{ 
						SwarmSearchAndCreateDirs(auxString, node, options);
					}
				}
			}
		}else System.out.println("ERROR. NullPointerException.");		    
				
	}
	
	public static DirNode<String> getDirStructure() {
		return dirStructure;
	}

}
