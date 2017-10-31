/**
 * An immutable list interface
 * Designed for illustrating reasoning about immutable types
 * <p>
 * Copyright 2007 Daniel Jackson and MIT
 */
package immutable;

import java.util.Iterator;

public interface ImList<E> extends Iterable<E> {
    /**
     * @param e element to add
     * @return [e, e_0, ..., e_n] where this list = [e_0,...,e_n]
     * @requires e != null
     */
    public ImList<E> add(E e);

    /**
     * Get first element of this list.
     *
     * @return e_0 where this list = [e_0,...,e_n]
     * @requires this list is nonempty
     */
    public E first();

    /**
     * Get second element of this list.
     *
     * @return e_1 where this list = [e_0, e_1,...,e_n]
     * @requires this list has at least 2 elements
     */
    public E second();

    /**
     * Get list of all elements of this list except for the first.
     *
     * @return [e_1, ..., e_n] where this list = [e_0,...,e_n]
     * @requires this list is nonempty
     */
    public ImList<E> rest();

    /**
     * Remove the first occurrence of an element from the list, if present.
     *
     * @return [e0, .., e_{i-1], e_{i+1},..,e_n] where i is the minimum index such
     * that e_i.equals(e); if no such i, then returns [e_0,..,e_n]
     * unchanged.
     * @requires e != null
     */
    public ImList<E> remove(E e);

    /**
     * @return exists i such that e_i.equals(e) where e_i is ith element of this
     * @requires e != null
     */
    public boolean contains(E e);

    /**
     * @return number of elements in this
     */
    public int size();

    /**
     * @return true if this contains no elements
     */
    public boolean isEmpty();

    /**
     * see Iterable.iterator()
     */
    public Iterator<E> iterator();

}
