package application;

public class AVL<T1 extends Comparable<T1>, T2> {

	TNode<T1, T2> root;


	public TNode<T1, T2> rotateRight(TNode<T1, T2> nodeN) {
		TNode<T1, T2> nodeC = nodeN.getLeft();
		nodeN.setLeft(nodeC.getRight());
		nodeC.setRight(nodeN);
		return nodeC;
	}

	public TNode<T1, T2> rotateLeft(TNode<T1, T2> nodeN) {
		TNode<T1, T2> nodeC = nodeN.getRight();
		nodeN.setRight(nodeC.getLeft());
		nodeC.setLeft(nodeN);
		return nodeC;
	}

	public TNode<T1, T2> rotateRightLeft(TNode<T1, T2> nodeN) {
		TNode<T1, T2> nodeC = nodeN.getRight();
		nodeN.setRight(rotateRight(nodeC));
		return rotateLeft(nodeN);
	}

	public TNode<T1, T2> rotateLeftRight(TNode<T1, T2> nodeN) {
		TNode<T1, T2> nodeC = nodeN.getLeft();
		nodeN.setLeft(rotateLeft(nodeC));
		return rotateRight(nodeN);
	}

	private TNode<T1, T2> rebalance(TNode<T1, T2> nodeN) {
		int diff = getHeightDifference(nodeN);
		if (diff > 1) { // addition was in node's left subtree
			if (getHeightDifference(nodeN.getLeft()) > 0)
				nodeN = rotateRight(nodeN);
			else
				nodeN = rotateLeftRight(nodeN);
		} else if (diff < -1) { // addition was in node's right subtree
			if (getHeightDifference(nodeN.getRight()) < 0)
				nodeN = rotateLeft(nodeN);
			else
				nodeN = rotateRightLeft(nodeN);
		}
		return nodeN;
	}

	public void insert(T1 label, T2 data) {
		if (isEmpty()) {
			root = new TNode<>(label, data);
		} else {
			TNode<T1, T2> rootNode = root;
			addEntry(label, data, rootNode);
			root = rebalance(rootNode);
		}
	}

	private void addEntry(T1 label, T2 data, TNode<T1, T2> rootNode) {
		assert rootNode != null;
		if (label.compareTo((T1) rootNode.getLabel()) < 0) { // right into left subtree
			if (rootNode.hasLeft()) {
				TNode<T1, T2> leftChild = rootNode.getLeft();
				addEntry(label, data, leftChild);
				rootNode.setLeft(rebalance(leftChild));
			} else
				rootNode.setLeft(new TNode<T1, T2>(label, data));
		} else { // right into right subtree
			if (rootNode.hasRight()) {
				TNode<T1, T2> rightChild = rootNode.getRight();
				addEntry(label, data, rightChild);
				rootNode.setRight(rebalance(rightChild));
			} else
				rootNode.setRight(new TNode<T1, T2>(label, data));
		}
	}

	private int getHeightDifference(TNode<T1, T2> node) {
		if (node == null)
			return 0;

		int right = height(node.getRight());
		int left = height(node.getLeft());

		return left - right;

	}

	public int height() {
		return height(root);
	}

	protected int height(TNode<T1, T2> node) {
		if (node == null)
			return 0;

		if (node.isLeaf())
			return 1;

		if (node.hasLeft() && node.hasRight())
			return max(height(node.getRight()), height(node.getLeft())) + 1;

		else if (node.hasLeft())
			return height(node.getLeft()) + 1;

		else
			return height(node.getRight()) + 1;

	}

	private int max(int a, int b) {
		return a > b ? a : b;
	}

	public boolean isEmpty() {
		return root == null;
	}

	private TNode<T1, T2> deleteNode(T1 label) {
		TNode<T1, T2> parent = root;
		TNode<T1, T2> curr = root;
		boolean isLeft = false;

		if (root == null) // tree is empty
			return null;

		while (curr != null && !curr.getLabel().equals(label)) {
			parent = curr;
			if (label.compareTo(curr.getLabel()) > 0) {
				curr = curr.getRight();
				isLeft = false;
			}

			else if (label.compareTo(curr.getLabel()) < 0) {
				curr = curr.getLeft();
				isLeft = true;
			}
		}

		if (curr == null) // node to be deleted not found
			return null;

		else if (curr.isLeaf()) { // node to be deleted is a leaf
			if (curr == root)
				root = null;

			if (isLeft)
				parent.setLeft(null);

			else
				parent.setRight(null);
		}

		// if the node to be deleted has only one child
		else if (curr.hasRight() && !curr.hasLeft()) { // if the node to be deleted has only right child
			if (curr == root)
				root = curr.getRight();
			else if (isLeft)
				parent.setLeft(curr.getRight());
			else
				parent.setRight(curr.getRight());

		}

		else if (curr.hasLeft() && !curr.hasRight()) { // if the node to be deleted has only the left child
			if (curr == root)
				root = curr.getLeft();
			else if (isLeft)
				parent.setLeft(curr.getLeft());
			else
				parent.setRight(curr.getLeft());
		}

		else { // if the node to be deleted has two children
			TNode<T1, T2> successorParent = getSuccessorParent(curr);
			TNode<T1, T2> successor;

			if (successorParent != curr) { // successor != curr.getRight()
				successor = successorParent.getLeft();
				successorParent.setLeft(successor.getRight());
			} else // successor = curr.getRight()
				successor = successorParent.getRight();

			if (curr == root) {
				root = successor;
			}

			else if (isLeft) {
				parent.setLeft(successor);
			}

			else {
				parent.setRight(successor);
			}
			successor.setLeft(curr.getLeft());
			if (successor != curr.getRight()) {
				successor.setRight(curr.getRight());
				return successorParent;
			} else
				return successor;

		}

		return parent;
	}

	private TNode<T1, T2> getSuccessorParent(TNode<T1, T2> node) {
		TNode<T1, T2> parent = node;
		TNode<T1, T2> curr = node.getRight();

		while (curr.getLeft() != null) {
			parent = curr;
			curr = curr.getLeft();
		}
		return parent;
	}

	public TNode<T1, T2> delete(T1 label) {
		TNode<T1, T2> temp = deleteNode(label);
		System.out.println(temp);
		if (temp != null) {
			TNode<T1, T2> rootNode = root;
			rebalanceB(rootNode, temp);
			root = rebalance(rootNode);
		}
		return temp;
	}

	private void rebalanceB(TNode<T1, T2> rootNode, TNode<T1, T2> temp) {
		if (temp == rootNode)
			return;
		if (temp.getLabel().compareTo(rootNode.getLabel()) < 0) {
//			System.out.println(rootNode);
			rebalanceB(rootNode.getLeft(), temp);
			rootNode.setLeft(rebalance(rootNode.getLeft()));
		} else if (temp.getLabel().compareTo(rootNode.getLabel()) > 0) {
			rebalanceB(rootNode.getRight(), temp);
			rootNode.setRight(rebalance(rootNode.getRight()));
		}
	}

	public String levelOrderTraverse() {
		Queue<TNode<T1, T2>> queue = new Queue<>();

		String s = "";

		if (root == null)
			return s;

		queue.enqueue(root);
		while (!queue.isEmpty()) {
			Node<TNode<T1, T2>> node = queue.dequeue();

			if (node.getData().hasLeft())
				queue.enqueue(node.getData().getLeft());

			if (node.getData().hasRight())
				queue.enqueue(node.getData().getRight());

			s = s + "\n" + node.getData().getLabel() + ":\n" + node.getData().getData() + "\n";
		}
		return s;
	}

	public TNode<T1, T2> search(T1 label) {
		return search(root, label);
	}

	private TNode<T1, T2> search(TNode<T1, T2> node, T1 label) {
		if (node != null)
			if (node.getLabel().compareTo(label) == 0)
				return node;
			else if (node.getLabel().compareTo(label) > 0)
				return search(node.getLeft(), label);
			else if (node.getLabel().compareTo(label) < 0)
				if (node.getRight() != null)
					return search(node.getRight(), label);

		return null;
	}

	public TNode<T1, T2> getRoot() {
		return root;
	}

	@Override
	public String toString() {
		return levelOrderTraverse();
	}

}
