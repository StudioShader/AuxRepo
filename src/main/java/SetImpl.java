import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicMarkableReference;
import com.sun.tools.javac.util.Pair;

public class SetImpl<T extends Comparable<T>> implements Set<T> {

    class Node{
        T data;
        AtomicMarkableReference<Node> next;

        public Node(T data) {
            this.data = data;
            this.next = new AtomicMarkableReference<>(null, false);
        }
    }

    public Node head = null;
    public Node tail = null;

    public SetImpl() {
        this.head = new Node(null);
        this.tail = new Node(null);
    }

    @Override
    public boolean add(T value) {
        Node pred, curr;
        boolean trying = true;
        while (trying) {
            Node findKey = get_by_key(value);
            pred = findKey;
            curr = findKey.next.getReference();

            if (curr != null && curr.data.compareTo(value) == 0) {
                return false;
            } else if (curr != null && curr.data.compareTo(value) < 0){
                continue;
            } else {
                Node node = new Node(value);
                node.next.set(curr, false);

                if (pred.next.compareAndSet(curr, node, false, false)) {
                    return true;
                }else{
                    continue;
                }
            }
        }
        return false;
    }

    private Node get_by_key(T key) {
        Node pred;
        Node curr;
        Node aux;
        boolean trying = true;
        while (trying) {
            pred = head;
            curr = pred.next.getReference();
            if (curr == null) {
                return pred;
            }
            inner: while (true) {
                aux = curr.next.getReference();
                boolean[] mark = new boolean[1];
                curr.next.get(mark);
                if (mark[0]) {
                    if (!pred.next.compareAndSet(curr, aux, false, false))
                        break inner;
                    curr = aux;
                } else {
                    if (curr.data.compareTo(key) >= 0) return pred;
                    pred = curr;
                    curr = aux;
                    if (curr == null) {
                        return pred;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean remove(T value) {
        Node pred;
        Node curr;
        Node aux;
        boolean trying = true;
        while (trying) {
            Node findKey = get_by_key(value);
            pred = findKey;
            curr = findKey.next.getReference();
            if (curr == null || curr.data.compareTo(value) != 0) {
                return false;
            } else {
                if (!curr.next.compareAndSet(curr.next.getReference(), curr.next.getReference(), false, true) || !pred.next.compareAndSet(pred.next.getReference(), pred.next.getReference(), false, true)) {
                    continue;
                }else{
                    aux = curr.next.getReference();
                    pred.next.compareAndSet(curr, aux, true, false);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean contains(T value) {
        Node curr = head.next.getReference();
        while (curr != null && curr.data.compareTo(value) < 0) {
            curr = curr.next.getReference();
        }
        return curr != null && curr.data.compareTo(value) == 0 && !curr.next.isMarked();
    }

    @Override
    public boolean isEmpty() {
        return head.next.getReference() == null;
    }

    private List getSnapshot() {
        List<Node> nodeSnapshot = new LinkedList<>();
        while(true) {
            Node current = head;
            while (current != null) {
                if (!current.next.isMarked()) {
                    nodeSnapshot.add(current);
                }
                current = current.next.getReference();
            }
            Iterator<Node> snapshotIterator = nodeSnapshot.iterator();
            current = head;
            while (current != null) {
                if (!current.next.isMarked()) {
                    try {
                        Node currentSnapshotNode = snapshotIterator.next();
                        if (currentSnapshotNode != current)
                            break;
                    }
                    catch (NoSuchElementException e) {
                        break;
                    }
                }
                current = current.next.getReference();
            }
            if(current != null || snapshotIterator.hasNext()) {
                nodeSnapshot.clear();
                continue;
            }
            break;
        }
        List<T> snapshot = new LinkedList<>();
        boolean first = false;
        for(Node node : nodeSnapshot) {
            if(first) {
                snapshot.add(node.data);
            }
            first = true;
        }
        return snapshot;
    }

    @Override
    public Iterator<T> iterator() {
        return getSnapshot().iterator();
    }
}
