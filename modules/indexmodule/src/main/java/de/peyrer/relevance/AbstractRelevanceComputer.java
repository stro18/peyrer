package de.peyrer.relevance;

import de.peyrer.repository.ArgumentRepository;

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
