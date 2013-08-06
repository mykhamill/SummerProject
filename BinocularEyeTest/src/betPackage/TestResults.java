package betPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TestResults {
	private ArrayList<TestResult> listOfResults = new ArrayList<>();
	
	public TestResults addResult (TestResult t) {
		listOfResults.add(t);
		return this;
	}
	
	public void saveResults (String fname) {
		PrintWriter out = null;
		File f = new File(fname + ".csv");
		int i = 1;
		while (f.exists()) {
			String fname1 = fname + "_" + i + ".csv";
			f = new File(fname1);
			i++;
		}
		
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (out != null) {
			for (TestResult t : listOfResults.toArray(new TestResult[0])) {
				out.println(t.toString());
			}
			out.close();
		}
	}
}
