/**
 * Population.java
 * 
 * This class handles the population of PopMembers and provides a way 
 * to insert a new PopMember into the population.
 * 
 * @author Brian Clee
 * @version 3/1/2013
 */

import java.util.ArrayList;
import java.util.Random;

public class Population 
{
	//fields
	private ArrayList<PopMember> popList;
	private int popSize;
	private Random r;
	
	public Population(int popSize)
	{
		popList = new ArrayList<PopMember>(popSize);
		this.popSize = popSize;
		r = new Random();
	}
	
	public void insert(PopMember child)
	{
		double currentFitness;
		PopMember currentPerson;
		int prevIndex;
		
		popList.add(child);
		
		for(int i = 1; i < popList.size(); i++)
		{
			currentFitness = popList.get(i).getFitness();
			currentPerson = popList.get(i);
			prevIndex = i - 1;
			
			while(prevIndex >= 0 && popList.get(prevIndex).getFitness() > currentFitness)
			{
				popList.set(prevIndex+1, popList.get(prevIndex));
				prevIndex--;
			}
			
			popList.set(prevIndex+1, currentPerson);
		}
		
		if(popList.size() > popSize)
		{
			//Should remove the extra person at the end, to keep a fixed size pop
			popList.remove(popList.size()-1);
		}
	}
	
	public PopMember selectParent()
	{
		//Kevin Holway select
		int x = r.nextInt(popSize);
		int y = r.nextInt(popSize-1);
		int z = y;
		
		if(x <= y)
		{
			z = x;
		}
		return popList.get(z);
	}
	
	public ArrayList<PopMember> getPopulation()
	{
		return popList;
	}
}
