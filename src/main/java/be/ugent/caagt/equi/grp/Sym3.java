package be.ugent.caagt.equi.grp;

import be.ugent.caagt.perm.Perm;

import java.util.Arrays;
import java.util.Collections;

/**
 * Symmetric group of order 3
 */

public class Sym3 extends AbstractCombinatorialGroup {

    private Perm g3;

    private Perm ri;

    public Sym3(int degree, Perm g3, Perm ri) {
        super(6, degree);
        this.g3 = g3;
        this.ri = ri;
    }

    @Override
    public Iterable<CombinatorialGroup> getSubgroups() {
        return Collections.emptyList(); // TODO
    }

    @Override
    public Iterable<CombinedGroup> getPointGroups() {
        return Arrays.asList(
                new CombinedGroup("D3", order, degree, Arrays.asList(
                        new ExtendedPerm(g3, PointGroupElement.ROT_3),
                        new ExtendedPerm(ri, PointGroupElement.REFLECT_R.minus())
                )),
                new CombinedGroup("C3v", order, degree, Arrays.asList(
                        new ExtendedPerm(g3, PointGroupElement.ROT_3),
                        new ExtendedPerm(ri, PointGroupElement.REFLECT_R)
                ))
        );
    }

    @Override
    public String toString() {
        return "Sym(3)";
    }

}
