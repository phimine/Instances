public class RBNode<T extends Comparable<T>> {
    public void setKey(T key) {
        this.key = key;
    }

    private T key;
    private boolean red;
    private RBNode<T> left;
    private RBNode<T> right;
    private RBNode<T> parent;

    public RBNode(T key, boolean red, RBNode<T> left, RBNode<T> right, RBNode<T> parent) {
        this.key = key;
        this.red = red;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }

    public T getKey() {
        return key;
    }

    public boolean isRed() {
        return red;
    }

    public void setRed(boolean red) {
        this.red = red;
    }

    public void setRed(){
        this.setRed(true);
    }

    public void setBlack(){
        this.setRed(false);
    }

    public RBNode<T> getLeft() {
        return left;
    }

    public void setLeft(RBNode<T> left) {
        this.left = left;
    }

    public RBNode<T> getRight() {
        return right;
    }

    public void setRight(RBNode<T> right) {
        this.right = right;
    }

    public RBNode<T> getParent() {
        return parent;
    }

    public void setParent(RBNode<T> parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "RBNode{" +
                "key=" + key +
                ", red=" + red +
                ", left=" + (null == left ? null : left.getKey()) +
                ", right=" + (null == right ? null : right.getKey()) +
                ", parent=" + (null == parent ? null : parent.getKey()) +
                '}';
    }
}
