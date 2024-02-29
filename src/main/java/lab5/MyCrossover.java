package lab5;

import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyCrossover extends AbstractCrossover<double[]> {
    protected MyCrossover() {
        super(1);
    }

    protected List<double[]> mate(double[] p1, double[] p2, int i, Random random) {
        ArrayList<double[]> children = new ArrayList<>();

        int dim = p1.length;
        double[] c1 = p1.clone();
        double[] c2 = p2.clone();
        double alpha = 0.05;
        int index = random.nextInt(dim);
        for (int j = index; j < dim; j++) {
            c1[j] = (1 - alpha) * p1[j] + alpha * p2[j];
            c2[j] = alpha * p1[j] + (1 - alpha) * p2[j];
        }

        children.add(c1);
        children.add(c2);
        return children;
    }
}
