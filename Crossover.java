/**
 * Crossover.java
 * 
 * This class provides a crossover that takes two PopMembers and crosses them
 * to produce a new PopMember child.
 * 
 * @author Brian Clee
 * @version 3/1/2013
 */

import java.util.ArrayList;
import java.util.Random;

public class Crossover 
{
	//fields
	private double[] momOrder;
	private double[] dadOrder;
	private Random r;
	
	public Crossover()
	{
		r = new Random();
	}
	
	public PopMember cross(PopMember mom, PopMember dad, double binCap)
	{
		momOrder = mom.getOrder();
		dadOrder = dad.getOrder();
		
		PopMember child = new PopMember();
		double[] childOrder;
		
		childOrder = createPath();
		child.setOrder(childOrder);
		child.evaluate(binCap);
		
		return child;
	}
	
	public double[] createPath()
	{
		double[] path = new double[momOrder.length];
		
		// Determine swath
		int swathLength = momOrder.length/3;
		int position1 = r.nextInt(momOrder.length - swathLength);
		int position2 = position1 +  swathLength;
		
		ArrayList<Double> remainingSwath = new ArrayList<Double>();
		for(int i = 0; i < momOrder.length; i++)
		{
			remainingSwath.add(momOrder[i]);
		}
		
		// Copy swath from parent1 to child
		ArrayList<Double> momSwathItems = new ArrayList<Double>();
		for(int i = position1; i <= position2; i++)
		{
			double element = momOrder[i];
			path[i] = element;
			momSwathItems.add(element);
			
			int remIndex = remainingSwath.indexOf(element);
			remainingSwath.remove(remIndex);
		}
		
		for(int i = 0; i < path.length; i++)
		{
			if(i < position1 || i > position2)
			{
				double element = dadOrder[i];
				if(!momSwathItems.contains(element) && remainingSwath.contains(element))
				{
					path[i] = element;
					int remIndex = remainingSwath.indexOf(element);
					remainingSwath.remove(remIndex);
				}
				else 
				{
					path[i] = -1;
				}
			}
		}
		
		for(int i = 0; i < path.length; i++)
		{
			if(path[i] == -1)
			{
				path[i] = remainingSwath.get(remainingSwath.size()-1);
				remainingSwath.remove(remainingSwath.size()-1);
			}
		}
		
		return path;
	}
	
	/**
	public PopMember cross(PopMember mom, PopMember dad, int binCap)
	{
		momOrder = mom.getOrder();
		dadOrder = dad.getOrder();
		
		PopMember child = new PopMember();
		int[] childOrder;
		
		int[][] momAdjMatrix = fillAdjMatrix(momOrder);
		int[][] dadAdjMatrix = fillAdjMatrix(dadOrder);
		
		ArrayList<ArrayList<Integer>> neighborLists = createUnionMatrix(momAdjMatrix, dadAdjMatrix);
		
		childOrder = createPath(neighborLists);
		child.setOrder(childOrder);
		child.evaluate(binCap);
		return child;
	}
	
	public int[][] fillAdjMatrix(int[] source)
	{
		int[][] adjMatrix = new int[source.length][2];
		
		for(int i = 0; i < source.length; i++)
		{
			int index = 0;
			boolean found = false;
			while(!found)
			{
				if(source[index] == i)
				{
					found = true;
				}
				else 
				{
					index++;
				}
			}
			
			//i = each node in order
			//index = that nodes place in the list
			int leftValue;
			int rightValue;
			if(index == 0)
			{
				leftValue = source[source.length];
				rightValue = source[index+1];
			}
			else if(index == source.length-1)
			{
				leftValue = source[index-1];
				rightValue = source[0];
			}
			else
			{
				leftValue = source[index-1];
				rightValue = source[index+1];
			}
			adjMatrix[i][0] = leftValue;
			adjMatrix[i][1] = rightValue;
		}
		
		return adjMatrix;
	}
	
	public ArrayList<ArrayList<Integer>> createUnionMatrix(int[][] source1, int[][] source2)
	{
		//ArrayList<Integer>[] unionMatrix = new ArrayList[source1.length];
		ArrayList<ArrayList<Integer>> unionMatrix = new ArrayList<ArrayList<Integer>>(source1.length);
		
		for(int i = 0; i < unionMatrix.size(); i++)
		{
			ArrayList<Integer> currentList = unionMatrix.get(i);
			currentList = new ArrayList<Integer>();
			int[] neighbors = new int[] {source1[i][0], source1[i][1], source2[i][0], source2[i][1]};
			Arrays.sort(neighbors);
			
			currentList.add(neighbors[0]);
			for(int j = 1; i < neighbors.length; j++)
			{
				if(!currentList.contains(neighbors[j]))
				{
					currentList.add(neighbors[j]);
				}
			}
			unionMatrix.set(i, currentList);
		}
		
		return unionMatrix;
	}
	
	public int[] createPath(ArrayList<ArrayList<Integer>> neighborList)
	{
		int[] childOrder = new int[momOrder.length];
		int nextNode = 0;
		if(r.nextBoolean())
		{
			nextNode = momOrder[0];
		}
		else
		{
			nextNode = dadOrder[0];
		}
		
		for(int i = 0; i < momOrder.length; i++)
		{
			childOrder[i] = nextNode;
			
			//remove n from all neighbor lists
			for(ArrayList<Integer> sublist : neighborList)
			{
				if(sublist.contains(nextNode))
				{
					sublist.remove(nextNode);
				}
			}
			
			//if n's neighbor list is non-empty
			//N* is the neighbor of N with the fewest neighbors in ITS list
			if(!neighborList.get(nextNode).isEmpty())
			{
				int fewestNode = neighborList.get(nextNode).get(0);
				int fewestSize = neighborList.get(fewestNode).size();
				for(int j = 1; i < neighborList.get(nextNode).size(); j++)
				{
					int currentNode = neighborList.get(nextNode).get(j);
					int currentSize = neighborList.get(currentNode).size();
					if(currentSize < fewestSize)
					{
						fewestNode = currentNode;
						fewestSize = currentSize;
					}
				}
				nextNode = fewestNode;
			}
			//N* is a randomly chosen node not in childOrder
			else
			{
				boolean placed = false;
				while(!placed)
				{
					int randomNode = momOrder[r.nextInt(momOrder.length)];
				
					boolean found = false;
					for(int node : childOrder)
					{
						if(node == randomNode)
						{
							found = true;
						}
					}
					if(!found)
					{
						placed = true;
						nextNode = randomNode;
					}
				}
			}
		}
		
		return childOrder;
	} */
}
