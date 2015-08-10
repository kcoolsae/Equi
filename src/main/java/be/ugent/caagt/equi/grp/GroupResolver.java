package be.ugent.caagt.equi.grp;

import be.ugent.caagt.perm.Perm;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Resolves groups. Returns a combinatorial group that corresponds to a given set of permutations;
 */
public class GroupResolver {

    private Perm[] permutations;

    private int[] orders;

    private int[] signature;

    private int degree;

    public GroupResolver(Perm[] permutations, int degree) {
        this.permutations = permutations;
        this.degree = degree;

        // compute permutation orders
        this.orders = new int[permutations.length];
        for (int i = 0; i < permutations.length; i++) {
            orders[i] = permutations[i].order();
        }
        // compute order signature
        SortedMap<Integer, Integer> map = new TreeMap<>();
        for (int order : orders) {
            if (map.containsKey(order)) {
                map.put(order, map.get(order) + 1);
            } else {
                map.put(order, 1);
            }
        }

        this.signature = new int[map.size() * 2];
        int pos = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            signature[pos] = entry.getKey();
            pos++;
            signature[pos] = entry.getValue();
            pos++;
        }
    }

    /**
     * Is this an involution that commutes with every element of the group?
     */
    private boolean isCentralInvolution(Perm g) {
        for (Perm perm : permutations) {
            if (!perm.commutesWith(g)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find an element of given order among the given permutations.
     */
    private Perm elementOfOrder(int order) {
        for (int i = 0; i < permutations.length; i++) {
            if (orders[i] == order) {
                return permutations[i];
            }
        }
        throw new IllegalStateException("Required group element not found");
    }

    /**
     * Find an element p of order n such that q.p has order m
     */
    private Perm elementOfOrder(int n, Perm q, int m) {
        for (int i = 0; i < permutations.length; i++) {
            Perm p = permutations[i];
            if (orders[i] == n && q.mul(p).order() == m) {
                return p;
            }
        }
        throw new IllegalStateException("Required group element not found");
    }

    /**
     * Try to resolve the given list of permutations as a cyclic group
     */
    private CombinatorialGroup resolveCyclic() {
        if (signature[signature.length - 2] == permutations.length) {
            return new Cyclic(degree, elementOfOrder(permutations.length));
        } else {
            return null;
        }
    }

    /**
     * Try to resolve the given list of permutations as a doubled cyclic group
     */
    private CombinatorialGroup resolveDoubleCyclic() {
        if (permutations.length % 4 != 0) {
            return null;  // cyclic should not be recognized as double cyclic
        } else {
            int halfOrder = permutations.length / 2;
            if (signature[signature.length - 2] == halfOrder && signature[3] == 3) {
                Perm g = elementOfOrder(halfOrder);
                Perm invol = elementOfOrder(2, g.pow(halfOrder / 2), 2);
                return new DoubleCyclic(degree, g, invol);
            } else {
                return null;
            }
        }
    }

    /**
     * Try to resolve the given list of permutations as a dihedral group
     */
    private CombinatorialGroup resolveDihedral() {
        int halfOrder = permutations.length / 2;
        if (signature[signature.length - 2] == halfOrder && signature[3] == halfOrder + (1 - halfOrder % 2)) {
            Perm g = elementOfOrder(halfOrder);
            Perm m = elementOfOrder(2, g, 2);
            return new Dihedral(degree, g, m);
        } else {
            return null;
        }
    }

    /**
     * Try to resolve the given list of permutations as a doubled dihedral group
     */
    private CombinatorialGroup resolveDoubleDihedral() {
        if (permutations.length % 8 != 0) {
            return null;
        } else {
            int quarterOrder = permutations.length / 4;
            if (signature[signature.length - 2] == quarterOrder && signature[3] == 2 * quarterOrder + 1 + 2 * (1 - quarterOrder % 2)) {
                Perm g = elementOfOrder(quarterOrder);
                Perm h = g.pow(quarterOrder / 2); // involution that belongs to the cyclic subgroup
                int pos = 1;
                while (orders[pos] != 2 || permutations[pos].equals(h) || !isCentralInvolution(permutations[pos])) {
                    pos++;
                }
                Perm g2 = permutations[pos];
                pos = 1;
                while (orders[pos] != 2 || isCentralInvolution(permutations[pos])) {
                    pos++;
                }
                Perm g3 = permutations[pos];
                return new DoubleDihedral(degree, g, g3, g2);
            } else {
                return null;
            }
        }
    }

    /**
     * Return the combinatorial group that corresponds to the given list of permutations
     */
    public CombinatorialGroup resolve() {

        if (permutations.length == 1) {
            return new Trivial(degree);
        }

        // first the infinite series
        CombinatorialGroup group = resolveCyclic();
        if (group == null) {
            group = resolveDihedral();
        }
        if (permutations.length == 4 && signature[3] == 3) {
            // special case for double cyclic
            group = new Squared2(degree, permutations[1], permutations[2]);
        }
        if (group == null) {
            group = resolveDoubleCyclic();
        }
        if (permutations.length == 8 && signature[3] == 7) {
            // special case for double dihedral
            Perm a = permutations[1];
            Perm b = permutations[2];
            Perm ab = a.mul(b);
            int pos = 3;
            while (!permutations[pos].equals(ab)) {
                pos++;
            }
            group = new Cubed2(degree, a, b, permutations[pos]);
        }
        if (group == null) {
            group = resolveDoubleDihedral();
        }
        if (group == null) {
            // now the remaining cases
            switch (permutations.length) {
                case 120: {
                    Perm g5i = elementOfOrder(10);
                    Perm g3 = elementOfOrder(3, g5i.pow(6), 2);
                    group = new DoubleAlt5(degree, g5i, g3);
                    break;
                }
                case 60: {
                    Perm g5 = elementOfOrder(5);
                    Perm g3 = elementOfOrder(3, g5, 2);
                    group = new Alt5(degree, g5, g3);
                    break;
                }
                case 48: {
                    Perm g3i = elementOfOrder(6);
                    Perm g4 = elementOfOrder(4, g3i.pow(4), 2);
                    group = new DoubleSym4(degree, g3i, g4);
                    break;
                }
                case 24:
                    if (signature[3] == 7) {
                        Perm g3i = elementOfOrder(6);
                        Perm g2 = elementOfOrder(2, g3i.pow(3), 6);
                        group = new DoubleAlt4(degree, g3i, g2);
                    } else { //if (signature[3] == 9)
                        Perm g3 = elementOfOrder(3);
                        Perm g4 = elementOfOrder(4, g3, 2);
                        group = new Sym4(degree, g3, g4);
                    }
                    break;
                case 12:
                    if (signature[3] == 3 && signature[5] == 8) {
                        Perm g3 = elementOfOrder(3);
                        Perm g2 = elementOfOrder(2);
                        group = new Alt4(degree, g3, g2);
                    }
                    break;
            }
        }
        return group;

    }

}
