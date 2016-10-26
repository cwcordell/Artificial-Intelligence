package edu.uab.cis.learning.decisiontree;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DecisionTreeTest {

  @Test(timeout = 10000)
  public void testMostFrequentClass() {
    // assemble training data
    List<LabeledFeatures<Integer, Integer, String>> trainingData = new ArrayList<>();
    trainingData.add(LabeledFeatures.ofStrings(0));
    trainingData.add(LabeledFeatures.ofStrings(1));
    trainingData.add(LabeledFeatures.ofStrings(1));
    // train the classifier
    DecisionTree<Integer, Integer, String> classifier = new DecisionTree<>(trainingData);
    // test that the classifier returns the most frequent class
    Features<Integer, String> testDatum = Features.of();
    Assert.assertEquals(Integer.valueOf(1), classifier.classify(testDatum));
  }

  @Test(timeout = 10000)
  public void testFullyPredictiveFeature() {
    // assemble training data
    List<LabeledFeatures<String, Integer, Integer>> trainingData = new ArrayList<>();
    trainingData.add(LabeledFeatures.ofIntegers("A", 0, 0));
    trainingData.add(LabeledFeatures.ofIntegers("A", 1, 0));
    trainingData.add(LabeledFeatures.ofIntegers("B", 0, 1));
    // train the classifier
    DecisionTree<String, Integer, Integer> classifier = new DecisionTree<>(trainingData);
    // test that the classifier split on the second feature
    Assert.assertEquals("B", classifier.classify(Features.of(1, 1)));
  }

  @Test(timeout = 10000)
  public void testFullyPredictiveFeature2() {
    // assemble training data
    List<LabeledFeatures<String, Integer, Integer>> trainingData = new ArrayList<>();
    trainingData.add(LabeledFeatures.ofIntegers("A", 0, 0, 3, 2));
    trainingData.add(LabeledFeatures.ofIntegers("A", 0, 0, 2, 5));
    trainingData.add(LabeledFeatures.ofIntegers("B", 0, 1, 1, 3));
    trainingData.add(LabeledFeatures.ofIntegers("C", 0, 2, 2, 5));
    // train the classifier
    DecisionTree<String, Integer, Integer> classifier = new DecisionTree<>(trainingData);
    // test that the classifier split on the second feature
    Assert.assertEquals("C", classifier.classify(Features.of(1, 2)));
  }

  @Test(timeout = 10000)
  public void testFullyPredictiveFeature3() {
    // assemble training data
    List<LabeledFeatures<String, Integer, Integer>> trainingData = new ArrayList<>();
    trainingData.add(LabeledFeatures.ofIntegers("A", 3, 1, 3, 2));
    trainingData.add(LabeledFeatures.ofIntegers("A", 0, 0, 2, 5));
    trainingData.add(LabeledFeatures.ofIntegers("B", 0, 1, 1, 3));
    trainingData.add(LabeledFeatures.ofIntegers("C", 0, 2, 2, 5));
    trainingData.add(LabeledFeatures.ofIntegers("C", 2, 1, 3, 4));
    // train the classifier
    DecisionTree<String, Integer, Integer> classifier = new DecisionTree<>(trainingData);
    // test that the classifier split on the second feature
    Assert.assertEquals("C", classifier.classify(Features.of(1,2,1,5,2)));
  }

  @Test(timeout = 10000)
  public void testNonPredictiveFeature() {
    // assemble training data
    List<LabeledFeatures<String, Integer, Integer>> trainingData = new ArrayList<>();
    trainingData.add(LabeledFeatures.ofIntegers("A", 3, 1, 3, 2));
    trainingData.add(LabeledFeatures.ofIntegers("D", 0, 0, 2, 5));
    trainingData.add(LabeledFeatures.ofIntegers("B", 0, 1, 1, 3));
    trainingData.add(LabeledFeatures.ofIntegers("D", 0, 2, 2, 5));
    trainingData.add(LabeledFeatures.ofIntegers("C", 2, 1, 3, 4));
    trainingData.add(LabeledFeatures.ofIntegers("E", 5, 0, 3, 1));
    trainingData.add(LabeledFeatures.ofIntegers("F", 4, 5, 1, 1));
    // train the classifier
    DecisionTree<String, Integer, Integer> classifier = new DecisionTree<>(trainingData);
    // test that the classifier split on the second feature
    Assert.assertEquals("F", classifier.classify(Features.of(4,9,8,1,9)));
  }

  @Test(timeout = 10000)
  public void testNonPredictiveFeature2() {
    // assemble training data
    List<LabeledFeatures<String, Integer, Integer>> trainingData = new ArrayList<>();
    trainingData.add(LabeledFeatures.ofIntegers("A", 3, 1, 3, 2, 2, 1, 3, 4));
    trainingData.add(LabeledFeatures.ofIntegers("D", 0, 0, 2, 5, 3, 1, 3, 2));
    trainingData.add(LabeledFeatures.ofIntegers("B", 0, 1, 1, 3, 9, 2, 5, 0));
    trainingData.add(LabeledFeatures.ofIntegers("D", 9, 2, 2, 3, 9, 2, 5, 0));
    trainingData.add(LabeledFeatures.ofIntegers("C", 2, 1, 3, 4, 4, 9, 5, 1));
    trainingData.add(LabeledFeatures.ofIntegers("E", 5, 0, 3, 1, 0, 0, 2, 5));
    trainingData.add(LabeledFeatures.ofIntegers("F", 4, 5, 1, 1, 0, 1, 1, 3));
    trainingData.add(LabeledFeatures.ofIntegers("F", 9, 2, 5, 0, 0, 2, 2, 3));
    trainingData.add(LabeledFeatures.ofIntegers("E", 9, 2, 5, 0, 0, 2, 2, 3));
    trainingData.add(LabeledFeatures.ofIntegers("G", 6, 3, 7, 0, 4, 9, 5, 1));
    trainingData.add(LabeledFeatures.ofIntegers("F", 2, 1, 5, 3, 5, 0, 3, 1));
    trainingData.add(LabeledFeatures.ofIntegers("A", 4, 0, 5, 1, 0, 2, 2, 3));
    trainingData.add(LabeledFeatures.ofIntegers("I", 4, 0, 5, 1, 0, 2, 2, 3));
    trainingData.add(LabeledFeatures.ofIntegers("H", 9, 9, 7, 0, 0, 6, 9, 0));
    trainingData.add(LabeledFeatures.ofIntegers("I", 3, 7, 1, 0, 3, 6, 9, 0));
    trainingData.add(LabeledFeatures.ofIntegers("J", 3, 7, 2, 0, 0, 6, 9, 0));
    trainingData.add(LabeledFeatures.ofIntegers("H", 2, 4, 11, 0, 0, 6, 9, 0));
    trainingData.add(LabeledFeatures.ofIntegers("E", 6, 8, 5, 1, 1, 2, 3, 7));
    trainingData.add(LabeledFeatures.ofIntegers("C", 6, 8, 2, 1, 1, 2, 3, 7));
    trainingData.add(LabeledFeatures.ofIntegers("C", 1, 9, 4, 6, 4, 3, 4, 3));
    // train the classifier
    DecisionTree<String, Integer, Integer> classifier = new DecisionTree<>(trainingData);
    // test that the classifier split on the second feature
    Assert.assertEquals("C", classifier.classify(Features.of(0,9,8,1,9)));
  }
}
