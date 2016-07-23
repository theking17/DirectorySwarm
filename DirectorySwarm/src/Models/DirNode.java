package Models;

import java.util.ArrayList;
import java.util.List;

public class DirNode<T> {

	Integer id;
	T value;

	boolean isDir;
	List< DirNode<T> > edges;
	DirNode<T> lastNode;
	
	public DirNode(Integer id, T value, boolean isFile, DirNode<T> lastNode) {
		super();
		this.id = id;
		this.value = value;
		this.isDir = isFile;
		this.edges = new ArrayList<DirNode<T>>();
		this.lastNode = lastNode;
	} 
	
	public DirNode(Integer id, T value) {
		super();
		this.id = id;
		this.value = value;
		this.edges = new ArrayList<DirNode<T>>();
		this.isDir = false;
		this.lastNode = null;
	} 

	public DirNode<T> getLastNode() {
		return lastNode;
	}

	public void setLastNode(DirNode<T> lastNode) {
		this.lastNode = lastNode;
	}
	
	public void addBidirectionalNode(DirNode<T> n){		
		edges.add(n);
		n.getEdges().add(this);
}
	public void addNode(DirNode<T> n){		
		edges.add(n);
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public T getValue() {
		return value;
	}
	public boolean isDir() {
		return isDir;
	}

	public void setDir(boolean isFile) {
		this.isDir = isFile;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public void setEdges(List<DirNode<T>> edges) {
		this.edges = edges;
	}

	public List< DirNode<T> > getEdges(){
		return edges;
	}
}
