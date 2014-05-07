/**
 * Driver.java
 * 
 * This class drives the BinPacking genetic algrothim as established throughout
 * this class, and the Crossover, PopMember, and Population classes.
 * 
 * @author Brian Clee
 * @version 3/1/2013
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;

public class Driver 
{
	private Population pop;
	private Crossover cross;
	private double mutationRate;
	private int extraPad;
	private int numRuns;
	private int popSize;
	private String fileName;
	private BufferedReader input;
	private double[] binItems;
	private double binCap;
	private int numItems;
	private Random r;
	private int bestKnown;
	
	public Driver()
	{
		input = new BufferedReader(new InputStreamReader(System.in));
		r = new Random();
		cross = new Crossover();
	}
	
	public static void main(String[] args)
	{
		try
		{
			Driver binPack = new Driver();
			binPack.gatherParameters();
			binPack.readData();
			binPack.run();
			binPack.firstFit();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	public void gatherParameters() throws IOException
	{
		String s;
		
		System.out.print("File name (include extension): ");
		s = input.readLine();
		fileName = s;
		
		System.out.print("\nPopulation size: ");
		s = input.readLine();
		popSize = Integer.parseInt(s);
		
		System.out.print("\nExtra pad size: ");
		s = input.readLine();
		extraPad = Integer.parseInt(s);
		
		System.out.print("\nNumber of runs: ");
		s = input.readLine();
		numRuns = Integer.parseInt(s);
		
		System.out.print("\nMutation rate (as a decimal i.e 12% = .12): ");
		s = input.readLine();
		mutationRate = Double.parseDouble(s);
	}
	
	public void readData() throws IOException
	{
		BufferedReader fileIn = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		String s;
		String[] sArr;
		
		s = fileIn.readLine();
		sArr = s.split(" ");
		
		binCap = Double.parseDouble(sArr[0]);
		numItems = Integer.parseInt(sArr[1]);
		binItems = new double[numItems];
		bestKnown = Integer.parseInt(sArr[2]);
		
		for(int i = 0; i < numItems; i++)
		{
			s = fileIn.readLine();
			binItems[i] = Double.parseDouble(s);
		}
	}
	
	public void initializePop()
	{
		pop = new Population(popSize);
		for(int i = 0; i < popSize; i++)
		{
			PopMember p = new PopMember();
			double[] order = permuteOrder();
			p.setOrder(order);
			p.evaluate(binCap);
			pop.insert(p);
		}
		
		for(int i = 0; i < extraPad; i++)
		{
			PopMember p = new PopMember();
			double[] order = permuteOrder();
			p.setOrder(order);
			p.evaluate(binCap);
			pop.insert(p);
		}
	}
	
	public void run()
	{
		ArrayList<PopMember> bestResults = new ArrayList<PopMember>();
		
		for(int i = 0; i < numRuns; i++)
		{
			initializePop();
			
			for(int j = 0; i < bestResults.size(); j++)
			{
				pop.insert(bestResults.get(j));
			}
			
			int generation = 0;
			do
			{
				generation++;
				PopMember mom;
				PopMember dad;
				do
				{
					//ArrayList<PopMember> workingPop = pop.getPopulation();
					//mom = workingPop.get(workingPop.size()/4);
					mom = pop.selectParent();
                    dad = pop.selectParent();

				} while(pop.getPopulation().indexOf(mom) == pop.getPopulation().indexOf(dad));
				
				PopMember child = cross.cross(mom, dad, binCap);
				//mutation
				if(mutationRate - r.nextDouble() > 0)
				{
					child.mutate();
                    child.evaluate(binCap);
				}
				pop.insert(child);
				
				/*if(pop.getPopulation().indexOf(child) == 0)
				{
					report(generation, i, child);
				}*/
			} while(pop.getPopulation().get(popSize-1).getFitness() - pop.getPopulation().get(0).getFitness() > 1.0);
			
			PopMember best = pop.getPopulation().get(0);
		    bestResults.add(best);
		    report(generation, i, best);
		}
		
		PopMember bestResult = bestResults.get(0);
		for(int i = 1; i < numRuns; i++)
		{
			if(bestResults.get(i).getFitness() < bestResult.getFitness())
			{
				bestResult = bestResults.get(i);
			}
		}
		
		report(-1, numRuns, bestResult);
	}
	
	public double[] permuteOrder()
	{
		double[] permutation = new double[numItems];
		int choice;
		
		for(int i = 0; i < permutation.length; i++)
		{
			permutation[i] = binItems[i];
		}
		
		for(int i = permutation.length-1; i >= 0; i--)
		{
			choice = r.nextInt(i+1);
			double temp = permutation[choice];
			permutation[choice] = permutation[i];
			permutation[i] = temp;
		}
		
		return permutation;
	}
	
	public void report(int generation, int numRun, PopMember p)
	{
		System.out.println("Solution of Generation " + generation + ", Run " + numRun);
		double[] order = p.getOrder();
		double fitness = p.getFitness();
		
		System.out.println("Order: ");
		for(int i = 0; i < order.length; i++)
		{
			if(i % 16 == 0)
			{
				System.out.print("\n" + order[i] + ", ");
			}
			else
			{
				System.out.print(order[i] + ", ");
			}
		}
		System.out.println("\nTotal Bins Used: " + fitness + "\n");
	}
	
	public void firstFit()
	{
		System.out.println("Best Known Result: " + bestKnown);
		System.out.print("First Fit Decreasing Result: ");
		double[] descending = binItems;
        Arrays.sort(descending);
        
        for(int i = 0; i < descending.length/2; i++)
        {
            int j = descending.length-(i+1);
            //Swap first and last
            double temp = descending[i];
            descending[i] = descending[j];
            descending[j] = temp;
        }

        PopMember ffd = new PopMember();
		ffd.setOrder(descending);
		ffd.evaluate(binCap);
		System.out.print(ffd.getFitness() + "\n");
	}
}
