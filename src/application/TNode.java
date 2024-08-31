package application;

public class TNode<T1 extends Comparable<T1>, T2> {
	
	private T1 label; 
	private T2 data; 
	private TNode<T1, T2> left; 
	private TNode<T1, T2> right;
	
	public TNode(T1 label, T2 data) {
		this.label = label; 
		this.data = data;
	}

	
	public T1 getLabel() {
		return label;
	}

	public void setLabel(T1 label) {
		this.label = label;
	}

	public T2 getData() {
		return data;
	}

	public void setData(T2 data) {
		this.data = data;
	}

	public TNode<T1, T2> getLeft() {
		return left;
	}

	public void setLeft(TNode<T1, T2> left) {
		this.left = left;
	}

	public TNode<T1, T2> getRight() {
		return right;
	}

	public void setRight(TNode<T1, T2> right) {
		this.right = right;
	}
	
	public boolean hasRight(){
		return this.right != null; 
	}
	
	public boolean hasLeft() {
		return this.left != null; 
	}
	
	public boolean isLeaf() {
		return this.right == null && this.left == null; 
	}

	@Override
	public String toString() {
		return "Node= " + label + ", data: " + data;
	}
}
