package edu.uab.cis.learning.decisiontree;

import com.google.common.collect.Maps;

import java.util.*;

/**
 * A decision tree classifier.
 * 
 * @param <LABEL>
 *          The type of label that the classifier predicts.
 * @param <FEATURE_NAME>
 *          The type used for feature names.
 * @param <FEATURE_VALUE>
 *          The type used for feature values.
 */
public class DecisionTree<LABEL, FEATURE_NAME, FEATURE_VALUE> {
  Map<FEATURE_VALUE, DecisionTree<LABEL, FEATURE_NAME, FEATURE_VALUE>> decisionTreeMap = new HashMap<>();
  Map<LABEL, Integer> labelCounts = new HashMap();
  FEATURE_NAME name = null;

  /**
   * Trains a decision tree classifier on the given training examples.
   *
   * <ol>
   * <li>If all examples have the same label, a leaf node is created.</li>
   * <li>If no features are remaining, a leaf node is created.</li>
   * <li>Otherwise, the feature F with the highest information gain is
   * identified. A branch node is created where for each possible value V of
   * feature F:
   * <ol>
   * <li>The subset of examples where F=V is selected.</li>
   * <li>A decision (sub)tree is recursively created for the selected examples.
   * None of these subtrees nor their descendants are allowed to branch again on
   * feature F.</li>
   * </ol>
   * </li>
   * </ol>
   *
   * @param trainingData
   *          The training examples, where each example is a set of features and
   *          the label that should be predicted for those features.
   */
  public DecisionTree(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData)
  {
    if(trainingData != null && !trainingData.isEmpty()) {
      labelCount(trainingData);

      if (labelCounts.size() > 1) {
        double labelEntropy = labelEntropy(trainingData);
        FeatureAnalyzer fa = new FeatureAnalyzer(trainingData);
        Feature<LABEL, FEATURE_NAME, FEATURE_VALUE> splitOnFeature = fa.getFeatureToSplitOn(labelEntropy);
        if(splitOnFeature != null) {
          name = splitOnFeature.getName();
          split(splitOnFeature, trainingData);
        }
      }
    }
  }

  /**
   * Predicts a label given a set of features.
   *
   * <ol>
   * <li>For a leaf node where all examples have the same label, that label is
   * returned.</li>
   * <li>For a leaf node where the examples have more than one label, the most
   * frequent label is returned.</li>
   * <li>For a branch node based on a feature F, E is inspected to determine the
   * value V that it has for feature F.
   * <ol>
   * <li>If the branch node has a subtree for V, then example E is recursively
   * classified using the subtree.</li>
   * <li>If the branch node does not have a subtree for V, then the most
   * frequent label for the examples at the branch node is returned.</li>
   * </ol>
   * <li>
   * </ol>
   *
   * @param features
   *          The features for which a label is to be predicted.
   * @return The predicted label.
   */
  public LABEL classify(Features<FEATURE_NAME, FEATURE_VALUE> features)
  {
    if(name == null || features == null || features.getFeatureNames().isEmpty() || decisionTreeMap == null || decisionTreeMap.isEmpty())
      return majorityLabel();

    FEATURE_VALUE value = features.getFeatureValue(name);
    if(value == null) return majorityLabel();

    DecisionTree<LABEL, FEATURE_NAME, FEATURE_VALUE> dtv = decisionTreeMap.get(value);
    if(dtv != null) return dtv.classify(features);
    return majorityLabel();
  }

  private LABEL majorityLabel() {
    LABEL label = null;
    int count = -1;
    LABEL testLabel;
    Iterator<LABEL> iter = labelCounts.keySet().iterator();
    while(iter.hasNext()) {
      testLabel = iter.next();
      if(labelCounts.get(testLabel) > count) {
        count = labelCounts.get(testLabel);
        label = testLabel;
      }
    }
    return label;
  }

  public FEATURE_NAME getName() {
    return name;
  }

  private void labelCount(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData) {
    LABEL label;
    Iterator<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> iter = trainingData.iterator();
    while(iter.hasNext()) {
      int val = 1;
      label = iter.next().getLabel();
      if(labelCounts.containsKey(label)) val = labelCounts.get(label) + 1;
      labelCounts.put(label, val);
    }
  }

  private double labelEntropy(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData) {
    double p;
    int logBase = 2;
    double entropy = 0.0;
    Iterator<Integer> iter = labelCounts.values().iterator();
    while(iter.hasNext()) {
      p = iter.next()/(double)trainingData.size();
      entropy += (-1) * p * Math.log(p)/Math.log(logBase);
    }
    return entropy;
  }

  private void split(Feature<LABEL, FEATURE_NAME, FEATURE_VALUE> feature,
                     Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData) {
    Iterator<Value<LABEL, FEATURE_VALUE>> viter = feature.getValues().iterator();
    LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> labeledFeatures;
    FEATURE_VALUE value;

    while(viter.hasNext()) {
      value = viter.next().getValue();
      List<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> list = new ArrayList<>();
      Iterator<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> iter = trainingData.iterator();

      while(iter.hasNext()) {
        labeledFeatures = iter.next();

        if(labeledFeatures.getFeatureValue(name).equals(value)) {
          Iterator<FEATURE_NAME> fni = labeledFeatures.getFeatureNames().iterator();
          FEATURE_NAME fn;
          Map<FEATURE_NAME, FEATURE_VALUE> newMap = new HashMap();
          while(fni.hasNext()) {
            fn = fni.next();
            if(!fn.equals(name)) newMap.put(fn, labeledFeatures.getFeatureValue(fn));
          }
          list.add(new LabeledFeatures(labeledFeatures.getLabel(),newMap));
        }

      }
      if(list != null) decisionTreeMap.put(value ,new DecisionTree(list));
    }
  }



  public class FeatureAnalyzer<LABEL, FEATURE_NAME, FEATURE_VALUE> {
    List<Feature<LABEL, FEATURE_NAME, FEATURE_VALUE>> features = new LinkedList<>();

    public FeatureAnalyzer(Collection<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> trainingData) {
      if(trainingData.size() > 0) {
        Iterator<LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE>> iter = trainingData.iterator();
        LabeledFeatures<LABEL, FEATURE_NAME, FEATURE_VALUE> labeledFeatures;
        FEATURE_NAME featureName;
        Feature<LABEL, FEATURE_NAME, FEATURE_VALUE> feature;

        while (iter.hasNext()) {
          labeledFeatures = iter.next();
          Iterator<FEATURE_NAME> fni = labeledFeatures.getFeatureNames().iterator();

          while (fni.hasNext()) {
            featureName = fni.next();
            feature = findFeature(featureName);
            if (feature == null) {
              feature = new Feature(featureName);
              features.add(feature);
            }

            feature.addValue(labeledFeatures.getLabel(), labeledFeatures.getFeatureValue(featureName));
          }
        }
      }
    }

    public Feature<LABEL, FEATURE_NAME, FEATURE_VALUE> findFeature(FEATURE_NAME name) {
      Feature<LABEL, FEATURE_NAME, FEATURE_VALUE> feature;
      Iterator<Feature<LABEL, FEATURE_NAME, FEATURE_VALUE>> iter = features.iterator();

      while(iter.hasNext()) {
        feature = iter.next();
        if(feature.getName().equals(name)) return feature;
      }
      return null;
    }

    public void printData() {
      Iterator<Feature<LABEL, FEATURE_NAME, FEATURE_VALUE>> iter = features.iterator();
      while(iter.hasNext()) {
        iter.next().printData();
      }
    }

    private Feature<LABEL, FEATURE_NAME, FEATURE_VALUE> getFeatureToSplitOn(double labelEntropy) {
      Iterator<Feature<LABEL, FEATURE_NAME, FEATURE_VALUE>> iter = features.iterator();
      Feature<LABEL, FEATURE_NAME, FEATURE_VALUE> splitOnFeature = null;
      Feature<LABEL, FEATURE_NAME, FEATURE_VALUE> feature;
      double entropy;
      double ig = -1;

      while(iter.hasNext()) {
        feature = iter.next();
        entropy = feature.entropy();
        if(labelEntropy - entropy > ig) {
          splitOnFeature = feature;
          ig = labelEntropy - entropy;
        }
      }
      return splitOnFeature;
    }
  }

  public class Feature<LABEL, FEATURE_NAME, FEATURE_VALUE> {
    private FEATURE_NAME featureName;
    private List<Value<LABEL, FEATURE_VALUE>> values = new LinkedList<>();

    public Feature(FEATURE_NAME fn) {
      featureName = fn;
    }

    public FEATURE_NAME getName() {
      return featureName;
    }

    public List<Value<LABEL, FEATURE_VALUE>> getValues() {
      return values;
    }

    public void addValue(LABEL label, FEATURE_VALUE name) {
      Value value = findValue(name);
      if(value == null) {
        value = new Value(name);
        values.add(value);
      }
      value.addLabel(label);
    }

    public Value<LABEL, FEATURE_VALUE> findValue(FEATURE_VALUE name) {
      Value<LABEL, FEATURE_VALUE> value;
      Iterator<Value<LABEL, FEATURE_VALUE>> iter = values.iterator();

      while(iter.hasNext()) {
        value = iter.next();
        if(value.getValue().equals(name)) return value;
      }
      return null;
    }

    private double entropy() {
      Iterator<Value<LABEL, FEATURE_VALUE>> iter = values.iterator();
      Value<LABEL, FEATURE_VALUE> value;
      int totalCount = totalCount();
      double sum = 0.0;

      while(iter.hasNext()) {
        value = iter.next();
        sum += value.totalCount() * value.entropy();
      }
      return 1/(double)totalCount * sum;
    }

    public int totalCount() {
      int sum = 0;
      Iterator<Value<LABEL, FEATURE_VALUE>> iter = values.iterator();
      while(iter.hasNext()) {
        sum += iter.next().totalCount();
      }
      return sum;
    }

    public void printData() {
      System.out.println("Feature: " + featureName);
      Iterator<Value<LABEL, FEATURE_VALUE>> iter = values.iterator();
      Value<LABEL, FEATURE_VALUE> value;

      while(iter.hasNext()) {
        value = iter.next();
        value.printData();
      }
      System.out.println();
    }

  }
  public class Value<LABEL, FEATURE_VALUE> {
    private FEATURE_VALUE value;
    private Map<LABEL, Integer> labelCounts = new HashMap();

    public Value(FEATURE_VALUE name) {
      value = name;
    }

    public FEATURE_VALUE getValue() {
      return value;
    }

    public void addLabel(LABEL label) {
      int count = 0;
      if(labelCounts.containsKey(label)) count = labelCounts.get(label);
      labelCounts.put(label, count + 1);
    }

    private double entropy() {
      double p;
      int x;
      int logBase = 2;
      double entropy = 0.0;
      LABEL label;
      Iterator<LABEL> iter = labelCounts.keySet().iterator();

      while(iter.hasNext()) {
        label = iter.next();
        x = labelCounts.get(label);
        p = x/(double)totalCount();
        entropy += (-1) * p * Math.log(p)/Math.log(logBase);
      }
      return entropy;
    }

    public int totalCount() {
      int sum = 0;
      Iterator<Integer> iter = labelCounts.values().iterator();
      while(iter.hasNext()) {
        sum += iter.next();
      }
      return sum;
    }

    public void printData() {
      System.out.print(value + ": ");
      Iterator<LABEL> iter = labelCounts.keySet().iterator();
      LABEL label;
      while(iter.hasNext()) {
        label = iter.next();
        System.out.print("(" + label + ", " + labelCounts.get(label) + ") ");
      }
      System.out.println();
    }
  }

  public void printArray(FEATURE_NAME[] array) {
    for(FEATURE_NAME n : array) {
      System.out.print(n + ", ");
    }
  }
}
