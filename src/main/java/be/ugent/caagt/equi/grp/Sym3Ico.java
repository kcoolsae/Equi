package be.ugent.caagt.equi.grp;

import be.ugent.caagt.perm.Perm;

import java.util.Arrays;
import java.util.Collections;

/**
 * Symmetric group of order 3 with a special orientation useful as a subgroup of an icosahedral group
 */

public class Sym3Ico extends AbstractCombinatorialGroup {

    private Perm g3;

    private Perm g2prime;

    public Sym3Ico(int degree, Perm g3, Perm g2prime) {
        super(6, degree);
        this.g3 = g3;
        this.g2prime = g2prime;
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
                        new ExtendedPerm(g2prime, PointGroupElement.REFLECT_PHI)
                )),
                new CombinedGroup("C3v", order, degree, Arrays.asList(
                        new ExtendedPerm(g3, PointGroupElement.ROT_3),
                        new ExtendedPerm(g2prime, PointGroupElement.REFLECT_PHI.minus())
                ))
        );
    }

    @Override
    public String toString() {
        return "Sym(3)";
    }

}
