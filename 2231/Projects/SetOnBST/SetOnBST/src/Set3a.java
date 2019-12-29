import java.util.Iterator;

import components.binarytree.BinaryTree;
import components.binarytree.BinaryTree1;
import components.set.Set;
import components.set.SetSecondary;

/**
 * {@code Set} represented as a {@code BinaryTree} (maintained as a binary
 * search tree) of elements with implementations of primary methods.
 *
 * @param <T>
 *            type of {@code Set} elements
 * @mathdefinitions <pre>
 * IS_BST(
 *   tree: binary tree of T
 *  ): boolean satisfies
 *  [tree satisfies the binary search tree properties as described in the
 *   slides with the ordering reported by compareTo for T, including that
 *   it has no duplicate labels]
 * </pre>
 * @convention IS_BST($this.tree)
 * @correspondence this = labels($this.tree)
 *
 * @author Chris Tuttle.219, Ryan Shaffer.555
 *
 */
public class Set3a<T extends Comparable<T>> extends SetSecondary<T> {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Elements included in {@code this}.
     */
    private BinaryTree<T> tree;

    /**
     * Returns whether {@code x} is in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be searched for
     * @return true if t contains x, false otherwise
     * @requires IS_BST(t)
     * @ensures isInTree = (x is in labels(t))
     */
    private static <T extends Comparable<T>> boolean isInTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";

        boolean found = false;

        /*
         * Disassemble the tree.
         */
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();

        /*
         * Check if x is equal to the root, and/or recursively check each
         * subtree. Change boolean accordingly. Then, reassemble the tree.
         */
        if (t.size() > 0) {
            T root = t.disassemble(left, right);
            if (root.equals(x)) {
                found = true;
            } else if (root.compareTo(x) < 0) {
                found = isInTree(right, x);
            } else if (root.compareTo(x) > 0) {
                found = isInTree(left, x);
            }
            t.assemble(root, left, right);
        }
        return found;
    }

    /**
     * Inserts {@code x} in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be inserted
     * @aliases reference {@code x}
     * @updates t
     * @requires IS_BST(t) and x is not in labels(t)
     * @ensures IS_BST(t) and labels(t) = labels(#t) union {x}
     */
    private static <T extends Comparable<T>> void insertInTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";

        /*
         * If the tree is empty, assemble it using x as the root and with two
         * children.
         */
        if (t.size() == 0) {
            BinaryTree<T> emptyLeft = t.newInstance();
            BinaryTree<T> emptyRight = t.newInstance();
            t.assemble(x, emptyLeft, emptyRight);
        } else {
            /*
             * Otherwise, disassemble the tree and recursively look through it
             * by comparing a root to x. Insert it into the proper position, and
             * then reassemble the tree.
             */
            BinaryTree<T> left = t.newInstance();
            BinaryTree<T> right = t.newInstance();
            T root = t.disassemble(left, right);
            if (x.compareTo(root) < 0) {
                insertInTree(left, x);
            } else {
                insertInTree(right, x);
            }
            t.assemble(root, left, right);
        }
    }

    /**
     * Removes and returns the smallest (left-most) label in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} from which to remove the label
     * @return the smallest label in the given {@code BinaryTree}
     * @updates t
     * @requires IS_BST(t) and |t| > 0
     * @ensures <pre>
     * IS_BST(t)  and  removeSmallest = [the smallest label in #t]  and
     *  labels(t) = labels(#t) \ {removeSmallest}
     * </pre>
     */
    private static <T> T removeSmallest(BinaryTree<T> t) {
        assert t != null : "Violation of: t is not null";

        /*
         * Initialize a node to return and disassemble the tree.
         */
        T smallest;
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();
        T root = t.disassemble(left, right);

        /*
         * If the left tree is non-empty, recursively move down the tree and
         * remove the smallest (the left-most node). Then, reassemble the tree.
         */
        if (left.size() > 0) {
            smallest = removeSmallest(left);
            t.assemble(root, left, right);
        } else {
            /*
             * Otherwise, if the left tree is empty, remove the root. Make the
             * right tree the new tree.
             */
            smallest = root;
            t.transferFrom(right);
        }
        return smallest;
    }

    /**
     * Finds label {@code x} in {@code t}, removes it from {@code t}, and
     * returns it.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} from which to remove label {@code x}
     * @param x
     *            the label to be removed
     * @return the removed label
     * @updates t
     * @requires IS_BST(t) and x is in labels(t)
     * @ensures <pre>
     * IS_BST(t)  and  removeFromTree = x  and
     *  labels(t) = labels(#t) \ {x}
     * </pre>
     */
    private static <T extends Comparable<T>> T removeFromTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";

        /*
         * Initialize the node to remove as the root instead of null, since it
         * is known to be in the tree.
         */
        T removed = t.root();
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();

        /*
         * If the root is not what is being removed, disassemble the tree.
         * Compare x to the root and recursively move through the left or right
         * tree, depending on the result. Then, reassemble tree.
         */
        if (!t.root().equals(x)) {
            T temp = t.disassemble(left, right);
            if (temp.compareTo(x) < 0) {
                removed = removeFromTree(right, x);
            } else {
                removed = removeFromTree(left, x);
            }
            t.assemble(temp, left, right);

        } else {
            /*
             * Otherwise, the root is what is being removed. Disassemble the
             * tree. There are 3 possible cases. If the left tree is empty,
             * transfer the right tree into the new tree. If the right tree is
             * empty, transfer the left tree into the new tree. If both are
             * non-empty, reassemble the tree using the smallest value from the
             * right tree as the new root (since it is the next-largest value
             * after the root).
             */
            t.disassemble(left, right);
            if (left.size() == 0 && right.size() > 0) {
                t.transferFrom(right);
            } else if (left.size() > 0 && right.size() == 0) {
                t.transferFrom(left);
            } else if (right.size() > 0 && left.size() > 0) {
                t.assemble(removeSmallest(right), left, right);
            }
        }
        return removed;
    }

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        this.tree = new BinaryTree1<T>();

    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Set3a() {
        this.createNewRep();

    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @SuppressWarnings("unchecked")
    @Override
    public final Set<T> newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(Set<T> source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof Set3a<?> : ""
                + "Violation of: source is of dynamic type Set3<?>";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case: source must be of dynamic type Set3a<?>, and
         * the ? must be T or the call would not have compiled.
         */
        Set3a<T> localSource = (Set3a<T>) source;
        this.tree = localSource.tree;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void add(T x) {
        assert x != null : "Violation of: x is not null";
        assert !this.contains(x) : "Violation of: x is not in this";

        /*
         * Use insertInTree to add.
         */
        insertInTree(this.tree, x);

    }

    @Override
    public final T remove(T x) {
        assert x != null : "Violation of: x is not null";
        assert this.contains(x) : "Violation of: x is in this";

        /*
         * Use removeFromTree to return the desired value.
         */
        return removeFromTree(this.tree, x);
    }

    @Override
    public final T removeAny() {
        assert this.size() > 0 : "Violation of: this /= empty_set";

        /*
         * Use removeSmallest since what is removed doesn't matter.
         */
        return removeSmallest(this.tree);
    }

    @Override
    public final boolean contains(T x) {
        assert x != null : "Violation of: x is not null";

        /*
         * Use isInTree to check.
         */
        return isInTree(this.tree, x);
    }

    @Override
    public final int size() {

        /*
         * Use the size() kernel method for binary trees.
         */
        return this.tree.size();
    }

    @Override
    public final Iterator<T> iterator() {
        return this.tree.iterator();
    }

}
