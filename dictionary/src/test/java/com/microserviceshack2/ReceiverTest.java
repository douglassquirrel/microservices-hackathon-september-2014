package com.microserviceshack2;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.microserviceshack2.dictionary.Receiver;

public class ReceiverTest {
	@Test
	public void testColumns() {
		String row1 = "abcde";
		String row2 = "fghij";

		Receiver receiver = new Receiver();

		List<String> rows = new ArrayList<String>();
		rows.add(row1);
		rows.add(row2);
		List<String> columns = receiver.getColumns(rows);

		assertEquals(5, columns.size());
		assertEquals("af", columns.get(0));
		assertEquals("bg", columns.get(1));
		assertEquals("ch", columns.get(2));
		assertEquals("di", columns.get(3));
		assertEquals("ej", columns.get(4));
	}
}
