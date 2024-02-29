package lab5;

import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.islands.IslandEvolution;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;
import org.uncommons.watchmaker.framework.islands.Migration;
import org.uncommons.watchmaker.framework.islands.RingMigration;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class IslandsAlg {

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
                run(complexity);
                fitness[i] = best_fit;
                time[i] = run_time;
            }

//            System.out.printf("Среднее время выполнения в секундах: %f%n", Arrays.stream(time).average().getAsDouble() / 1000);
//            System.out.printf("Средний резутьтат: %f%n", Arrays.stream(fitness).average().getAsDouble());
            System.out.printf("%f %f%n", Arrays.stream(time).average().getAsDouble() / 1000, Arrays.stream(fitness).average().getAsDouble());

        }
    }

    public static void run(int complexity) {
        int dimension = 50; // dimension of problem
//        int complexity = 1; // fitness estimation time multiplicator
        int islandCount = 5;
        int islandPopulationSize = 20;
        int populationSize = 100 / islandCount; // size of population
        int epochLength = 50;
        int generations = 1000 / epochLength; // number of generations

        Random random = new Random(); // random

        CandidateFactory<double[]> factory = new MyFactory(dimension); // generation of solutions

        ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
        operators.add(new MyCrossover()); // Crossover
        operators.add(new MyMutation()); // Mutation
        EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

        SelectionStrategy<Object> selection = new RouletteWheelSelection(); // Selection operator

        FitnessEvaluator<double[]> evaluator = new MultiFitnessFunction(dimension, complexity); // Fitness function
        RingMigration migration = new RingMigration();
        IslandEvolution<double[]> island_model = new IslandEvolution<>(islandCount, migration, factory,
                pipeline, evaluator, selection, random); // your model;

        island_model.addEvolutionObserver(new IslandEvolutionObserver() {
            public void populationUpdate(PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
//                System.out.println("Epoch " + populationData.getGenerationNumber() + ": " + bestFit);
//                System.out.println("\tEpoch best solution = " + Arrays.toString((double[]) populationData.getBestCandidate()));
            }

            public void islandPopulationUpdate(int i, PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
//                System.out.println("Island " + i);
//                System.out.println("\tGeneration " + populationData.getGenerationNumber() + ": " + bestFit);
//                System.out.println("\tBest solution = " + Arrays.toString((double[]) populationData.getBestCandidate()));
                if (bestFit > best_fit)
                    best_fit = bestFit;
            }
        });

        TerminationCondition terminate = new GenerationCount(generations);
        long start = System.currentTimeMillis();
        island_model.evolve(populationSize, 1, epochLength, 2, terminate);
        long end = System.currentTimeMillis();
        run_time = end - start;
    }
}
