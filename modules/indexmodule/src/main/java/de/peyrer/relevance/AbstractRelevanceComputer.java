package de.peyrer.relevance;

import de.peyrer.repository.ArgumentRepository;

import java.util.Arrays;
import java.util.Map;

public abstract class AbstractRelevanceComputer implements IRelevanceComputer
{
    protected ArgumentRepository argumentRepository;

    protected AbstractRelevanceComputer()
    {
        this.argumentRepository = new ArgumentRepository();
    }

    protected Map<String, Double> normalizeRelevance(Map<String, Double> relevanceMap, int base)
    {
        double sum = 0;
        for (Map.Entry<String, Double> relevance : relevanceMap.entrySet()){
            sum += relevance.getValue();
        }

        double normalize = sum/base;

        for (Map.Entry<String, Double> relevance : relevanceMap.entrySet()){
            relevance.setValue(relevance.getValue() / normalize);
        }

        return relevanceMap;
    }

    protected Map<String, Double> normalizeRelevanceMaximum(Map<String, Double> relevanceMap)
    {
        double sum = 0;
        int count = 0;
        for (Map.Entry<String, Double> relevance : relevanceMap.entrySet()){
            count++;
            sum += relevance.getValue();
        }

        double normalize = sum/count;

        for (Map.Entry<String, Double> relevance : relevanceMap.entrySet()){
            double newValue = relevance.getValue() / normalize;
            newValue = newValue <= 10 ? newValue : 10;

            relevance.setValue(newValue);
        }

        return relevanceMap;
    }

    protected void logRelevanceData(Map<String, Double> relevanceMap)
    {
        double sum = 0;
        int counter = 0;
        for (Map.Entry<String, Double> relevance : relevanceMap.entrySet()){
            counter++;
            sum += relevance.getValue();
        }

        System.out.println("Average relevance: " + sum/counter);

        Double[] medianArray = relevanceMap.values().toArray(new Double[0]);
        Arrays.sort(medianArray);
        double medianRelevance = medianArray[medianArray.length/2];

        System.out.println("Median relevance: " + medianRelevance);

        Double current = 0d;
        int count = 0;
        int other = 0;
        int otherCount = 0;
        for(Double relevance : medianArray){
            if(relevance.equals(current)){
                count++;
            }else{
                if (count > 10 || current == 0) {
                    System.out.println("Relevance " + current + " occurs " + count + " times.");
                } else {
                    other++;
                    otherCount += count;
                }
                current = relevance;
                count = 1;
            }
        }
        System.out.println("Maximum relevance " + current + " occurs " + count + " times.");
        System.out.println(other + " other relevances occur altogether " + otherCount + " times.");
    }

    protected void saveRelevance(Map<String,Double> relevanceMap)
    {
        System.out.println("Saving of relevance started at : " + java.time.ZonedDateTime.now());
        int count = 0;
        for (Map.Entry<String, Double> rvMap : relevanceMap.entrySet()) {
            argumentRepository.updateRelevance(rvMap.getKey(), rvMap.getValue());

            count++;
            if (count % 1000 == 0) {
                System.out.println("Progress: Relevance of " + count + " arguments saved at: " + java.time.ZonedDateTime.now());
            }
        }
        System.out.println("Saving of relevance ended at : " + java.time.ZonedDateTime.now());
    }
}
