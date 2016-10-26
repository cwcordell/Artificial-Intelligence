package edu.uab.cis.probability.ngram;


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import static com.google.common.collect.Lists.charactersOf;
import java.util.List;

/**
 * A probabilistic n-gram language model.
 * 
 * @param <T>
 *          The type of items in the sequences over which the language model
 *          estimates probabilities.
 */
public class NgramLanguageModel<T> {

  enum Smoothing {
    /**
     * Do not apply smoothing. An n-gram w<sub>1</sub>,...,w<sub>n</sub> will
     * have its joint probability P(w<sub>1</sub>,...,w<sub>n</sub>) estimated
     * as #(w<sub>1</sub>,...,w<sub>n</sub>) / N, where N indicates the total
     * number of all 1-grams observed during training.
     * 
     * Note that we have defined only the joint probability of an n-gram here.
     * Deriving the conditional probability from the definition above is left as
     * an exercise.
     */
    NONE,

    /**
     * Apply Laplace smoothing. An n-gram w<sub>1</sub>,...,w<sub>n</sub> will
     * have its conditional probability
     * P(w<sub>n</sub>|w<sub>1</sub>,...,w<sub>n-1</sub>) estimated as (1 +
     * #(w<sub>1</sub>,...,w<sub>n</sub>)) / (V +
     * #(w<sub>1</sub>,...,w<sub>n-1</sub>)), where # indicates the number of
     * times an n-gram was observed during training and V indicates the number
     * of <em>unique</em> 1-grams observed during training.
     * 
     * Note that Laplace smoothing defines only the conditional probability of
     * an n-gram, not the joint probability.
     */
    LAPLACE
  }

  enum Representation {
    /**
     * Calculate probabilities in the normal range, [0,1].
     */
    PROBABILITY,
    /**
     * Calculate log-probabilities instead of probabilities. In every case where
     * probabilities would have been multiplied, take advantage of the fact that
     * log(P(x)*P(y)) = log(P(x)) + log(P(y)) and add log-probabilities instead.
     * This will improve efficiency since addition is faster than
     * multiplication, and will avoid some numerical underflow problems that
     * occur when taking the product of many small probabilities close to zero.
     */
    LOG_PROBABILITY
  }

  private int n;
  private Node<T> root = new Node<T>();
  private Representation rep;
  private Smoothing smooth;
  private LinkedList<T> nodes;

  /**
   * Creates an n-gram language model.
   * 
   * @param n
   *          The number of items in an n-gram.
   * @param representation
   *          The type of representation to use for probabilities.
   * @param smoothing
   *          The type of smoothing to apply when estimating probabilities.
   */
  public NgramLanguageModel(int n, Representation representation, Smoothing smoothing)
  {
    this.n = n;
    rep = representation;
    smooth = smoothing;
  }

  /**
   * Trains the language model with the n-grams from a sequence of items.
   * 
   * This typically involves collecting counts of n-grams that occurred in the
   * sequence.
   * 
   * @param sequence
   *          The sequence on which the model should be trained.
   */
  public void train(List<T> sequence)
  {
	  for(int a = 1; a <= n; ++a)
	  {
		  for(int i = a; i <= sequence.size(); ++i)
		  {
			  root.add(sequence.subList(i-a, i)); 
		  }
	  }
  }

  /**
   * Return the estimated n-gram probability of the sequence:
   * 
   * P(w<sub>0</sub>,...,w<sub>k</sub>) = ∏<sub>i=0,...,k</sub>
   * P(w<sub>i</sub>|w<sub>i-n+1</sub>, w<sub>i-n+2</sub>, ..., w<sub>i-1</sub>)
   * 
   * For example, a 3-gram language model would calculate the probability of the
   * sequence [A,B,B,C,A] as:
   * 
   * P(A,B,B,C,A) = P(A)*P(B|A)*P(B|A,B)*P(C|B,B)*P(A|B,C)
   * 
   * The exact calculation of the conditional probabilities in this expression
   * depends on the smoothing method. See {@link Smoothing}.
   * 
   * The result is in the range [0,1] with {@link Representation#PROBABILITY}
   * and in the range (-∞,0] with {@link Representation#LOG_PROBABILITY}.
   * 
   * @param sequence
   *          The sequence of items whose probability is to be estimated.
   * @return The estimated probability of the sequence.
   */
  public double probability(List<T> sequence)
  {
	  int[][] prob = new int[sequence.size()][2];
	  int start = 0;
	  for(int i = 1; i <= sequence.size(); ++i)
	  {
		  start = (i - n) >= 0 ? i - n : 0;
		  prob[i-1] = root.getProbabilityData(sequence.subList(start, i));
	  }
	  
	  if(smooth == Smoothing.LAPLACE)
		  applyLaplaceSmoothing(prob);

	  if(rep == Representation.LOG_PROBABILITY)
		  return calcLogProbability(prob);
	  else
		  return calcProbability(prob);
  }
  
  // additional methods
  public void applyLaplaceSmoothing(int[][] prob)
  {
	  int v = root.getV();
	  for(int[] i : prob)
	  {
		  i[0] += 1;
		  i[1] += v;
	  }
  }
  
  public double calcLogProbability(int[][] prob)
  {
	  double p = 0.0;
	  for(int[] i : prob)
		  p += Math.log(i[0]/(double)i[1]);
	  
	  return p;
  }
  
  public double calcProbability(int[][] prob)
  {
	  double p = 1;
	  for(int[] i : prob)
		  p *= i[0]/(double)i[1];
	  return p;
  }
  
  // Node stores data about gram sequences
  class Node<S>
  {
    private LinkedList<Node<S>> nodes = new LinkedList<>();
    private Node<S> parent = null;
    private int count = 0;
    private S value;
    
    public Node()
    {
    	nodes = new LinkedList<>();
    }
    
    public Node(S val, Node<S> par)
    {
    	nodes = new LinkedList<>();
    	value = val;
    	parent = par;
    }
    
    public void inc()
    {
    	++count;
    }
    
    public S getValue()
    {
    	return value;
    }
    
    public int getCount()
    {
    	return count;
    }
    
    public int getV()
    {
    	return nodes.size();
    }
    
    // returns an int array containing the count and the prefix (base) count
    public int[] getProbabilityData(List<S> list)
    {
    	if(list.isEmpty())
    		return getProbabilityData();
    	
    	Node<S> node = search(list.get(0));
    	if(node != null)
    		return node.getProbabilityData(list.subList(1,list.size()));
    	
    	node = new Node<S>(list.get(0), this);
		nodes.add(node);
		return node.getProbabilityData(list.subList(1,list.size()));
    }
    
    private int[] getProbabilityData()
    {
    	return new int[]{getCount(), parent.getCount()};
    }

    private void add(List<S> list, boolean inc)
    {
    	if(list.size() == 1 && parent == null && inc) inc();
    	if(list.size() == 0 && inc)
    	{
    		inc();
    	}
    	else
    	{
	    	Node<S> node = search(list.get(0));
	    	if(list.size() == 1 && node == null)
	    	{
	    		node = new Node<S>(list.get(0), this);
	    		nodes.add(node);
	    		node.add(list.subList(1,list.size()));
	    	}
	    	else
	    		node.add(list.subList(1,list.size()));
    	}
    }
    
    private void add(List<S> list)
    {
    	add(list, true);
    }
    
    private Node<S> search(S val)
    {
    	Iterator<Node<S>> iter = nodes.iterator();
    	Node<S> node;
    	while(iter.hasNext())
    	{
    		node = iter.next();
    		if(val.equals(node.getValue())) return node;
    	}
    	return null;
    }
    
    private Node<S> search(List<S> list)
    {
    	if(list.isEmpty())
    		return this;
    	
    	Node<S> node = search(list.get(0));
    	if(!list.isEmpty() && node != null)
    		return node.search(list.subList(1,list.size()));
    	
    	return null;
    }
    
    public String getSequence(List<S> list)
    {
    	Node<S> node = search(list);
    	if(node != null)
    		return node.getSequence();
    	return "Not Found";
    }
    
    public String getSequence()
    {
    	if(parent != null)
    		return parent.getSequence() + getValue();
    	return "";
    }
    
    public void printMap()
    {
    	if(parent != null)
    		System.out.println(getSequence() + ", Count: " + getCount() + 
    				", Parent V: " + parent.getV() + ", V: " + getV());
    	
    	Iterator<Node<S>> iter = nodes.iterator();
    	while(iter.hasNext())
    	{
    		iter.next().printMap();
    	}
    }
    
    public void printMap(String seq, String data)
    {
    	if(parent != null) seq = seq + value;
    	if(getV() == 0) System.out.println("Sequence: " + seq);
    	else
    	{
	    	Iterator<Node<S>> iter = nodes.iterator();
	    	while(iter.hasNext())
	    	{
	    		iter.next().printMap(seq,data);
	    	}
    	}
    }
    
    public String toString()
    {
    	return getSequence() + ", " + getCount() + "/" + parent.getCount();
    }
  }
}
