package lab5;

import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MasterSlaveAlg {

    private static double best_fit;
    private static long run_time;

    public static void main(String[] args) {
        for (int complexity = 1; complexity <= 5; complexity++) {
//            System.out.println("Complexity " + (complexity));
            int N = 51;
            double[] fitness = new double[N];
            long[] time = new long[N];
            for (int i = 0; i < N; i++) {
                best_fit = 0;
                run_time = 0;
                run(complexity, false);
                fitness[i] = best_fit;
                time[i] = run_time;
            }

//            System.out.printf("Среднее время выполнения в секундах: %f%n", Arrays.stream(time).average().getAsDouble() / 1000);
//            System.out.printf("Средний резутьтат: %f%n", Arrays.stream(fitness).average().getAsDouble());
            System.out.printf("%f %f%n", Arrays.stream(time).average().getAsDouble() / 1000, Arrays.stream(fitness).average().getAsDouble());

        }
    }

    public static void run(int complexity, boolean singleThread) {
        int dimension = 50; // dimension of problem
//        int complexity = 1; // fitness estimation time multiplicator
        int populationSize = 100; // size of population
        int generations = 1000; // number of generations

        Random random = new Random(); // random

        CandidateFactory<double[]> factory = new MyFactory(dimension); // generation of solutions

        ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
        operators.add(new MyCrossover()); // Crossover
        operators.add(new MyMutation()); // Mutation
        EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

        SelectionStrategy<Object> selection = new RouletteWheelSelection(); // Selection operator

        FitnessEvaluator<double[]> evaluator = new MultiFitnessFunction(dimension, complexity); // Fitness function

        AbstractEvolutionEngine<double[]> algorithm = new SteadyStateEvolutionEngine<double[]>(
                factory, pipeline, evaluator, selection, populationSize, false, random);

        algorithm.setSingleThreaded(singleThread);

        algorithm.addEvolutionObserver(new EvolutionObserver() {
            public void populationUpdate(PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
//                System.out.println("Generation " + populationData.getGenerationNumber() + ": " + bestFit);
//                System.out.println("\tBest solution = " + Arrays.toString((double[]) populationData.getBestCandidate()));
                if (bestFit > best_fit)
                    best_fit = bestFit;
            }
        });

        TerminationCondition terminate = new GenerationCount(generations);
        long start = System.currentTimeMillis();
        algorithm.evolve(populationSize, 1, terminate);
        long end = System.currentTimeMillis();
        run_time = end - start;
    }
}
