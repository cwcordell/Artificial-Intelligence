package edu.uab.cis.search.maze;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

public class MazeTest {

  @Test
  public void test() {
    // @formatter:off
    String mazeString = 
      "#######\n" +
      "#    G#\n" +
      "#  #  #\n" +
      "# S # #\n" +
      "#######\n";
    // @formatter:on
    Set<Square> obstacles = Sets.newHashSet(new Square(1, 2), new Square(2, 3));
    Maze maze = new Maze(3, 5, new Square(2, 1), new Square(0, 4), obstacles);
    Assert.assertEquals(mazeString, maze.toString());
    Assert.assertEquals(new Square(2, 1), maze.getStart());
    Assert.assertEquals(new Square(0, 4), maze.getGoal());
    for (Square square : obstacles) {
      Assert.assertTrue(maze.isBlocked(square));
    }
    Assert.assertTrue(maze.isBlocked(new Square(-1, 0)));
    Assert.assertTrue(maze.isBlocked(new Square(0, -1)));
    Assert.assertTrue(maze.isBlocked(new Square(0, 6)));
    Assert.assertTrue(maze.isBlocked(new Square(7, 0)));
  }
}
