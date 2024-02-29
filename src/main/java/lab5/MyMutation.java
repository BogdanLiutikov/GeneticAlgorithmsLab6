package lab5;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.List;
import java.util.Random;

public class MyMutation implements EvolutionaryOperator<double[]> {
    public List<double[]> apply(List<double[]> population, Random random) {
        for (double[] candidate :
                population) {
            double candidateProbability = random.nextDouble();
            if (candidateProbability < 1) {
                for (int i = 0; i < candidate.length; i++) {
                    double geneProbability = random.nextDouble();
                    if (geneProbability < 1. / candidate.length) {
                        candidate[i] = Math.max(-5, Math.min(5, candidate[i] + random.nextDouble() * 2 - 1));
                    }
                }
            }
        }
        return population;
    }
}
