package edu.uab.cis.search.maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.uab.cis.search.maze.Solver.SNode;

public class SolverTest
{
	@Test(timeout = 10000)
	  public void testStartGoalEqual() {
		Square start = new Square(1,1);
		Square goal = new Square(1,2);
		Set<Square> obstacles = Sets.newHashSet(new Square(1, 3), new Square(2, 2));
		Maze maze = new Maze(10, 10, start, goal, obstacles);
		Solver solver = new Solver(maze);
		Solver.SNode startNode = solver.new SNode(start, goal);
		Solver.SNode goalNode = solver.new SNode(1, 2, startNode, goal);

		Assert.assertEquals(start, startNode);
	   Assert.assertEquals(goal, goalNode);
	  }
	
	@Test(timeout = 10000)
	public void testNoObstacles()
	{
    // @formatter:off
    String mazeString = 
      "###############\n" +
      "#S           G#\n" +
      "#             #\n" +
      "###############\n";
    // @formatter:on
    Maze maze = new Maze(2, 13, new Square(0, 0), new Square(0, 12), Sets.<Square> newHashSet());
    Assert.assertEquals("The Maze does not match!", mazeString, maze.toString());

    // solve the maze
    Solver solver = new Solver(maze);

    // the solution should just go left to right
    List<Square> path = solver.getPathFromStartToGoal();
    List<Square> expectedPath = new ArrayList<>();
    for (int column = 0; column <= 12; ++column) {
      expectedPath.add(new Square(0, column));
    }
    Assert.assertEquals("The Path does not match!", expectedPath, path);
    
    // no additional squares should be explored
    Set<Square> explored = solver.getExploredSquares();
    Set<Square> expectedExplored = Sets.newHashSet(expectedPath);
    
    Assert.assertEquals("The Explored Squares does not match!", expectedExplored, explored);
  }

  @Test(timeout = 10000)
  public void testObstacles()
  {
    // @formatter:off
    String mazeString = 
      "######\n" +
      "#    #\n" +
      "# # G#\n" +
      "#S # #\n" +
      "######\n";
    // @formatter:on
    Set<Square> obstacles = Sets.newHashSet(new Square(1, 1), new Square(2, 2));
    Maze maze = new Maze(3, 4, new Square(2, 0), new Square(1, 3), obstacles);
    Assert.assertEquals("The Maze does not match!", mazeString, maze.toString());

    // solve the maze
    Solver solver = new Solver(maze);

    // the solution should go up around the obstacles
    List<Square> path = solver.getPathFromStartToGoal();
    List<Square> expectedPath = Lists.newArrayList(
            new Square(2, 0),
            new Square(1, 0),
            new Square(0, 0),
            new Square(0, 1),
            new Square(0, 2),
            new Square(0, 3),
            new Square(1, 3));
    Assert.assertEquals("The Path does not match!", expectedPath, path);

    // the square to the right of the start should also be explored
    Set<Square> explored = solver.getExploredSquares();
    Set<Square> expectedExplored = Sets.newHashSet(new Square(2, 1));
    expectedExplored.addAll(expectedPath);
    Assert.assertEquals("The Explored Squares does not match!", expectedExplored, explored);
  }
  
	@Test(timeout = 10000)
	public void testStartBlocked()
	{
	    // @formatter:off
	    String mazeString = 
	      "######\n" +
	      "#    #\n" +
	      "### G#\n" +
	      "#S # #\n" +
	      "######\n";
	    // @formatter:on
	    Set<Square> obstacles = Sets.newHashSet(new Square(1, 0), new Square(1, 1), new Square(2, 2));
	    Maze maze = new Maze(3, 4, new Square(2, 0), new Square(1, 3), obstacles);
	    Assert.assertEquals("The Maze does not match!", mazeString, maze.toString());

	    // solve the maze
	    Solver solver = new Solver(maze);

	    // no path exists
	    List<Square> path = solver.getPathFromStartToGoal();
	    List<Square> expectedPath = Lists.newArrayList();
	    Assert.assertEquals("The Path does not match!", expectedPath, path);

	    // the square to the right of the start should also be explored
	    Set<Square> explored = solver.getExploredSquares();
	    Set<Square> expectedExplored = Sets.newHashSet(new Square(2, 1), new Square(2, 0));
	    expectedExplored.addAll(expectedPath);
	    Assert.assertEquals("The Explored Squares does not match!", expectedExplored, explored);
	  }
	
	// produces the same result as testStartBlocked but tested to be complete
	@Test(timeout = 10000)
	public void testGoalBlocked()
	{
	    // @formatter:off
	    String mazeString = 
	      "#######\n" +
	      "#  #  #\n" +
	      "#  # G#\n" +
	      "#S #  #\n" +
	      "#######\n"; 
	    // @formatter:on
	    Set<Square> obstacles = Sets.newHashSet(new Square(2, 2), new Square(1, 2), new Square(0, 2));
	    Maze maze = new Maze(3, 5, new Square(2, 0), new Square(1, 4), obstacles);
	    Assert.assertEquals("The Maze does not match!", mazeString, maze.toString());

	    // solve the maze
	    Solver solver = new Solver(maze);

	    // no path exists
	    List<Square> path = solver.getPathFromStartToGoal();
	    List<Square> expectedPath = Lists.newArrayList();
	    Assert.assertEquals("The Path does not match!", expectedPath, path);

	    // the squares on the left side should be explored
	    Set<Square> explored = solver.getExploredSquares();
	    Set<Square> expectedExplored = Sets.newHashSet(
	   		 new Square(0, 0), 
	   		 new Square(0, 1), 
	   		 new Square(1, 0), 
	   		 new Square(1, 1), 
	   		 new Square(2, 0), 
	   		 new Square(2, 1));
	    expectedExplored.addAll(expectedPath);
	    Assert.assertEquals("The Explored Squares does not match!", expectedExplored, explored);
	  }
	
	@Test(timeout = 10000)
	public void testGoalIsBottomLeft()
	{
	    // @formatter:off
	    String mazeString = 
	      "#######\n" +
	      "#     #\n" +
	      "#  # S#\n" +
	      "#G #  #\n" +
	      "#######\n";
	    
	    	//"#######\n" +
	      //"#7777 #\n" +
	      //"#7 #5S#\n" +
	      //"#G #55#\n" +
	      //"#######\n"; 
	    
	    // @formatter:on
	    Set<Square> obstacles = Sets.newHashSet(new Square(2, 2), new Square(1, 2));
	    Maze maze = new Maze(3, 5, new Square(1, 4), new Square(2, 0), obstacles);
	    Assert.assertEquals("The Maze does not match!", mazeString, maze.toString());

	    // solve the maze
	    Solver solver = new Solver(maze);

	    // the solution should go up around the obstacles
	    List<Square> path = solver.getPathFromStartToGoal();
	    List<Square> expectedPath = Lists.newArrayList(
	   		 new Square(1, 4),
	   		 new Square(1, 3),
	   		 new Square(0, 3),
	   		 new Square(0, 2),
	   		 new Square(0, 1),
	   		 new Square(0, 0),
	   		 new Square(1, 0),
	   		 new Square(2, 0)
	   		 );
	    Assert.assertEquals("The Path does not match!", expectedPath, path);

	    // the squares below and below-left of start should also be explored
	    Set<Square> explored = solver.getExploredSquares();
	    Set<Square> expectedExplored = Sets.newHashSet(
	   		 new Square(2, 3), 
	   		 new Square(2, 4));
	    expectedExplored.addAll(expectedPath);
	    Assert.assertEquals("The Explored Squares does not match!", expectedExplored, explored);
	  }
	
	@Test(timeout = 10000)
	public void testNoObstaclesVertical()
	{
    // @formatter:off
    String mazeString = 
      "###############\n" +
      "#S            #\n" +
      "#             #\n" +
      "#             #\n" +
      "#             #\n" +
      "#G            #\n" +
      "###############\n";
    // @formatter:on
    Maze maze = new Maze(5, 13, new Square(0, 0), new Square(4, 0), Sets.<Square> newHashSet());
    Assert.assertEquals("The Maze does not match!", mazeString, maze.toString());

    // solve the maze
    Solver solver = new Solver(maze);

    // the solution should just go straight down
    List<Square> path = solver.getPathFromStartToGoal();
    List<Square> expectedPath = new ArrayList<>();
    for (int row = 0; row <= 4; ++row) {
      expectedPath.add(new Square(row, 0));
    }
    Assert.assertEquals("The Path does not match!", expectedPath, path);
    
    // no additional squares should be explored
    Set<Square> explored = solver.getExploredSquares();
    Set<Square> expectedExplored = Sets.newHashSet(expectedPath);
    
    Assert.assertEquals("The Explored Squares does not match!", expectedExplored, explored);
  }
	
	@Test(timeout = 10000)
	public void testStartIsGoal()
	{
    // @formatter:off
    String mazeString = 
      "###############\n" +
      "#             #\n" +
      "#             #\n" +
      "#      S      #\n" +
      "#             #\n" +
      "#             #\n" +
      "###############\n";
    // @formatter:on
    Maze maze = new Maze(5, 13, new Square(2, 6), new Square(2, 6), Sets.<Square> newHashSet());
    Assert.assertEquals("The Maze does not match!", mazeString, maze.toString());

    // solve the maze
    Solver solver = new Solver(maze);

    // the solution is one square, start/goal
    List<Square> path = solver.getPathFromStartToGoal();
    List<Square> expectedPath = Lists.newArrayList(new Square(2, 6));
    Assert.assertEquals("The Path does not match!", expectedPath, path);
    
    // no additional squares should be explored
    Set<Square> explored = solver.getExploredSquares();
    Set<Square> expectedExplored = Sets.newHashSet(expectedPath);
    
    Assert.assertEquals("The Explored Squares does not match!", expectedExplored, explored);
  }
}
