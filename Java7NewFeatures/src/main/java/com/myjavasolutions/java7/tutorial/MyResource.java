package com.myjavasolutions.java7.tutorial;

/*
 * This class is for demonstrating Java 7's suppressed exceptions
 */
public class MyResource implements AutoCloseable {

	/*
	 * overriding close method and generating some exception
	 * 
	 * (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		throw new UnsupportedOperationException("Not supported");
	}

	public void doSomething() {
		throw new RuntimeException("Some error occured while processing your request");
	}
}
