# Assignment: Agents: Blackjack

The goal of this assignment is to introduce you to intelligent agents, and at the same time, get you familiar with [Git](http://git-scm.com/) and [Maven](http://maven.apache.org/), which will be used for all the assignments in this course.

## Prepare for Git and Maven use

1. If it is not already installed, [install Git on your local machine](http://git-scm.com/book/en/Getting-Started-Installing-Git).

2. If it is not already installed, [install Maven on your local machine](http://maven.apache.org/download.cgi).

3. Start reading the [Git book](http://git-scm.com/book). Minimally, read Chapter 1 (Getting Started), which will teach you basic version control concepts, and Chapter 2 (Git Basics), which will teach you the most common commands.

4. Read the [Maven Getting Started Guide](http://maven.apache.org/guides/getting-started/), which will teach you how to compile and test code using Maven.

## Get a copy of the assignment template

1. Sign into https://git.cis.uab.edu using your CIS credentials (not your UAB credentials).

2. Set up SSH access for your account. Go to https://git.cis.uab.edu/profile/keys and follow the instructions there to add an SSH key.

3. Fork the repository for the current assignment, https://git.cis.uab.edu/cs-460/agents-blackjack, by following the link, and clicking on the "Fork" button on the right hand side of the "CS 460 / Agents - Blackjack" page.

4. Give your instructor access to your fork. **I cannot grade your assignment unless you complete this step.** Click on `Settings`, then `Members`, then `New Project Member`. In the `People` box, search for and add the user ``bethard``. Under ``Project Access``, select ``Reporter``. Then click ``Add users``.

5. Copy the code from your fork at `git.cis.uab.edu` to your local machine so that you can start editing it. Run the following command, replacing `<url>` with the URL displayed at the top of your fork's webpage (which looks something like `git@git.cis.uab.edu:.../agents-blackjack.git`):

        git clone <url>

6. You should see that a directory named `agents-blackjack` has been created and populated. Change to that directory:

        cd agents-blackjack

## Compile and test the code

1.  Compile the code. Run the following command:

        mvn clean compile

    Everything should compile and you should see a message like:

        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------

2.  Test the code. Run the following command:

        mvn clean test

    The tests should fail, and you should see a message like:

        Tests in error: 
          testTenKing(edu.uab.cis.agents.blackjack.DealerAgentTest)
          testSixJack(edu.uab.cis.agents.blackjack.DealerAgentTest)
        
        Tests run: 2, Failures: 0, Errors: 2, Skipped: 0
        
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD FAILURE
        [INFO] ------------------------------------------------------------------------

    Note the `clean`, which ensures that Maven alone (not your development environment) is compiling your code.

## Implement your part of the code

If you look closely at the test failures, you will see that the problem is that `edu.uab.cis.agents.blackjack.DealerAgent.act` is throwing a `java.lang.UnsupportedOperationException`.

Your task is to correctly implement `edu.uab.cis.agents.blackjack.DealerAgent.act` by modifying `src/main/java/edu/uab/cis/agents/blackjack/DealerAgent.java`. Your agent should implement the following rules:

* If the total points represented by the cards are:
    * 16 or less, `act` should return `Action.HIT`
    * Between 17 and 22, `act` should return `Action.STAND`
    * 22 or more, `act` should return `Action.BUST`.
* In calculating the total points, the order of the cards does not matter, only the point values.
    * The numeric cards always count for the equivalent number of points (TWO=2, THREE=3, ..., TEN=10).
    * JACK, QUEEN and KING always count for 10 points.
    * Each ACE counts for 11 points, unless it would cause the total points to be 22 or more, in which case it counts for 1 point.

You may modify only `src/main/java/edu/uab/cis/agents/blackjack/DealerAgent.java` and `src/test/java/edu/uab/cis/agents/blackjack/DealerAgentTest.java`. **Do not modify any other files.** If you need to declare any additional classes, please do so in one of these files.

## Test your code

1.  Re-run the tests:

        mvn clean test

    You should now see a message like:

        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------

    Your code is now passing the tests that were given to you. This is a good sign, but note that **a successful `mvn test` does not guarantee you full credit on an assignment**. We will run extra tests on your code when grading it. For example, the tests you have been given do not test your handling of ACE cards.

## Save your changes while you work

1. As you make changes to the code, you should regularly save them to the Git repository. First, add any files with changes to the Git staging area. For this assignment, that will typically mean running something like the following command:

        git add src/main/java/edu/uab/cis/agents/blackjack/DealerAgent.java

2.  Then commit all the staged changes to your local Git clone. Run the following command, providing an informative commit message:

        git commit -m "...your comment about what you changed in the code..."

3.  Push your local commits to your remote Git repository on `git.cis.uab.edu`. Run the following command:

        git push

4.  Verify that your changes have been pushed. Go to `https://git.cis.uab.edu/.../agents-blackjack`, where `...` is your CIS username. You should see your newest commit displayed near the top of the page.

5. You should repeat this process regularly while you edit your code to make sure that your in-progress work is always backed up.

## Submit your assignment

1.  To submit your assignment, just make sure that you have pushed all of your changes to `git.cis.uab.edu`.

2.  I will inspect the date of your last push to your `git.cis.uab.edu` repository. If it is after the deadline, your submission will be marked as late. So please **do not push changes to `git.cis.uab.edu` after the assignment deadline** unless you intend to submit a late assignment.
