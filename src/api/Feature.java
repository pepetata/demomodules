package api;

import java.util.ArrayList;

public class Feature {
    private String featureIdentifier;

    private String featureName;
    private String featureDescription;
    private String refId1;
    private String refId2;

    public Feature() {
    }
    
    public Feature(String featureIdentifier, String featureName, String featureDescription, 
        String refId1, String refId2) {
        this.featureIdentifier = featureIdentifier;
        this.featureName = featureName;
        this.featureDescription = featureDescription;
        this.refId1 = refId1;
        this.refId2 = refId2;
    }

    public Feature getByIdentifier(ArrayList<Feature> featureList, String id) {
        for(Feature f : featureList) {
            if(f.getFeatureIdentifier().equals(id) ){
                //   System.out.println("-------Value: " + f.getFeatureName());
                  return f;
            }
        }
        return null;
    }

    public String getFeatureIdentifier() {
        return featureIdentifier;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getFeatureDescription() {
        return featureDescription;
    }

    public String getRefId1() {
        return refId1;
    }

    public String getRefId2() {
        return refId2;
    }

    public void setFeatureIdentifier(String featureIdentifier) {
        this.featureIdentifier = featureIdentifier;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public void setFeatureDescription(String featureDescription) {
        this.featureDescription = featureDescription;
    }

    public void setRefId1(String refId1) {
        this.refId1 = refId1;
    }

    public void setRefId2(String refId2) {
        this.refId2 = refId2;
    }

}
