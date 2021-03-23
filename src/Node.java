/*
    Generic Node class that allows us to implement it in
    a 2D linked grid

    Author: Erik Rodriguez
 */
public class Node<T>
{
    private Node<T> up;
    private Node<T> down;
    private Node<T> left;
    private Node<T> right;
    private T payload;

    Node() {} // default constructor
    Node(T payload) {this.payload = payload ;} // overloaded constructor

    public T getPayload() { return payload; } // Getters
    public Node<T> getDown() { return down; }
    public Node<T> getLeft() { return left; }
    public Node<T> getRight() { return right; }
    public Node<T> getUp() { return up; }


    public void setPayload(T payload)
    {
        this.payload = payload;
    } // Setters
    public void setRight(Node<T> right) { this.right = right; }
    public void setUp(Node<T> up) { this.up = up; }
    public void setLeft(Node<T> left) { this.left = left; }
    public void setDown(Node<T> down) { this.down = down; }
}
