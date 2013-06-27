package com.myjavasolutions.java7.tutorial;

import java.util.concurrent.RecursiveTask;

public class MyExpensiveTask extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 1L;

	public MyExpensiveTask() {
	}

	@Override
	protected Integer compute() {

		// Add your task implementation here
		return 1;
	}
}
