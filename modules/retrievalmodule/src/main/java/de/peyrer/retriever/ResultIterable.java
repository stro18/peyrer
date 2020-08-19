package de.peyrer.retriever;

import java.util.Iterator;

import de.peyrer.model.Argument;

public class ResultIterable implements Iterable<Argument> {
	private Iterator<Argument> iterator;
	
	public ResultIterable(Iterator<Argument> iterator) {
		this.iterator = iterator;
	}

	@Override
	public Iterator<Argument> iterator() {
		return this.iterator;
	}
}
