/**
 * PopMember.java
 * 
 * This class represents a member of the population. Each member has 
 * a fitness, an ordering for how to pack the bins, and a mutate method.
 * 
 * @author Brian Clee
 * @version 3/1/2013
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class PopMember 
{
	//fields
	private double fitness;
	private double[] order;
	private Random r;
	
	public PopMember()
	{
		r = new Random();
	}
	
	public void setFitness(double fitness)
	{
		this.fitness = fitness;
	}
	
	public void setOrder(double[] order)
	{
		this.order = order;
	}
	
	public double getFitness()
	{
		return fitness;
	}
	
	public double[] getOrder()
	{
		return order;
	}
	
	public void evaluate(double binCap)
	{
		double cost = 0;
		ArrayList<Double> bins = new ArrayList<Double>();
		bins.add(0.0);
		
		for(int i = 0; i < order.length; i++)
		{
			int tempIndex = 0;
			boolean placed = false;
			while(!placed)
			{
				double binWeight = bins.get(tempIndex);
				if(binWeight + order[i] <= binCap)
				{
					bins.set(tempIndex, binWeight + order[i]);
					placed = true;
				}
				else
				{
					if(bins.size()-1 == tempIndex)
					{
						bins.add(0.0);
					}
					tempIndex++;
				}
			}
		}
		
		cost = bins.size();
		fitness = cost;
	}
	
	public void mutate()
	{
        Arrays.sort(order);
        for(int i = 0; i < order.length/2; i++)
        {
            int j = order.length - (i+1);
            double temp = order[i];
            order[i] = order[j];
            order[j] = temp;
        }

		boolean mutated = false;
        int count = 0;
		while(!mutated)
		{
			int index1 = r.nextInt(order.length);
			int index2 = r.nextInt(order.length);
			
			if(index1 != index2)
			{
				count++;
				double temp = order[index2];
				order[index2] = order[index1];
				order[index1] = temp;
                if(count == order.length/4)
                {
                    mutated = true;
                }
			}
		}
	}
}
