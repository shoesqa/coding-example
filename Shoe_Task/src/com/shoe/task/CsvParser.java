package com.shoe.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 
 * @author Rajendra kumar tambabattula.
 * CsvParser will parse the content from csv file and store it in list.
 *
 */
public class CsvParser {

	public static void main(String[] args) throws IOException {
		System.out.println("Test results analysis started.");

		// Creating list 
		List<CsvColumn> list = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/com/shoe/task/jmeter_results.csv"))));
				
				) {
			//Adding all rows from csv file to list.
			list = reader.lines().skip(1).map(line -> {
				String s[] = line.split(",");
				CsvColumn row = new CsvColumn();
				row.setTimeStamp(new Date(new Timestamp(Long.parseLong(s[0])).getTime()));
				row.setElapsed(Double.parseDouble(s[1])/1000);
				row.setThreadName(s[2]);
				return row;
			}).collect(Collectors.toList());
		}
				
		Collections.sort(list,new Comparator<CsvColumn>() {
			public int compare(CsvColumn c1, CsvColumn c2) {
				return c1.getTimeStamp().compareTo(c2.getTimeStamp());
			}
		});
		
		// get the no of queries.
		int noOfQueries = list.size();
		
		// Get the job start time and end time from list which is in ascending order.
		Date jobStartTime = list.get(0).getTimeStamp();
		Date jobEndTime = list.get(noOfQueries-1).getTimeStamp();
				
		//Sort the list by elapsed time
		Collections.sort(list, new Comparator<CsvColumn>() {
			public int compare(CsvColumn c1, CsvColumn c2) {
				return Double.compare(c1.getElapsed(), c2.getElapsed());
			}
		});
		
		// Get the shortest elapsed time and longest elapsed time from the list which is in sorted order by elapsed time.
		double shortestElapsedTime = list.get(0).getElapsed();
		double longestElapsedTime = list.get(noOfQueries-1).getElapsed();
		double totalElapsedTime = list.stream().map(CsvColumn::getElapsed).collect(Collectors.summingDouble(i->i));		
		double  averageElapseTime = totalElapsedTime/noOfQueries;
		double qps = (double) noOfQueries/totalElapsedTime;
		
		//Predicate for filter the list by by Cart Group (Add to cart, Full purchase, Add item and login)
		Predicate<CsvColumn> isCart = row -> {
			String threadName = row.getThreadName();
			return threadName.contains("Add to cart") || threadName.contains("Full purchase")
					|| threadName.contains("Add item and login");
		};
		
		//Predicate for filter the list by by Category Group (Category Concept CSV, Category Shops CSV, Category Thumbs CSV)
		Predicate<CsvColumn> isCategory = row -> {
			String threadName = row.getThreadName();
			return threadName.contains("Category Concept CSV") || threadName.contains("Category Shops CSV")
					|| threadName.contains("Category Thumbs CSV");
		};
		
		//Predicate for filter the list by by Search Functionality Group (Search Functionality)
		Predicate<CsvColumn> isSearch = row -> {
			String threadName = row.getThreadName();
			return threadName.contains("Search Functionality");
		};
		
		//Predicate for filter the list by by Product Page Group (Product CSV)
		Predicate<CsvColumn> isProductPage = row -> {
			String threadName = row.getThreadName();
			return threadName.contains("Product CSV");
		};
		
		//Predicate for filter the list by by Marketing Group (Marketing)
		Predicate<CsvColumn> isMarketingPage = row -> {
			String threadName = row.getThreadName();
			return threadName.contains("Marketing");
		};
		
		//Predicate for filter the list by by Coupon code Group (Coupon code)
		Predicate<CsvColumn> isCouponCode = row -> {
			String threadName = row.getThreadName();
			return threadName.contains("Coupon code");
		};
		
		//Predicate for filter the list by by Info Page Group (Info CSV)
		Predicate<CsvColumn> isInfoCsv = row -> {
			String threadName = row.getThreadName();
			return threadName.contains("Info CSV");
		};
		
		//Predicate for filter the list by by Login Group (Login and Wishlist)
		Predicate<CsvColumn> isLogin = row -> {
			String threadName = row.getThreadName();
			return threadName.contains("Login and Wishlist");
		};
		
		//Predicate for filter the list by by Registration Group (Registration)
		Predicate<CsvColumn> isRegistration = row -> {
			String threadName = row.getThreadName();
			return threadName.contains("Registration");
		};
		
		/**
		 * Create lists by applying predicates
		 */
		List<CsvColumn> cartList = list.stream().filter(isCart).collect(Collectors.<CsvColumn>toList());
		
		List<CsvColumn> categoryList = list.stream().filter(isCategory).collect(Collectors.toList());
		
		List<CsvColumn> searchList = list.stream().filter(isSearch).collect(Collectors.toList());
		
		List<CsvColumn> productPageList = list.stream().filter(isProductPage).collect(Collectors.toList());
		
		List<CsvColumn> marketingPageList = list.stream().filter(isMarketingPage).collect(Collectors.toList());
		
		List<CsvColumn> couponList = list.stream().filter(isCouponCode).collect(Collectors.toList());
		
		List<CsvColumn> infoCsvList = list.stream().filter(isInfoCsv).collect(Collectors.toList());
		
		List<CsvColumn> loginList = list.stream().filter(isLogin).collect(Collectors.toList());
		
		List<CsvColumn> registrationList = list.stream().filter(isRegistration).collect(Collectors.toList());

		// Get No of queries per second from sub lists.
		double nofOfQueriesPerSecondForSearchGroup = (double) searchList.size()/searchList.stream().mapToDouble(r -> r.getElapsed()).sum();
		double nofOfQueriesPerSecondForCartGroup = (double) cartList.size()/cartList.stream().mapToDouble(r -> r.getElapsed()).sum();
		double nofOfQueriesPerSecondForCategoryGroup = (double) categoryList.size()/categoryList.stream().mapToDouble(r -> r.getElapsed()).sum();
		double nofOfQueriesPerSecondForProductPageGroup = (double) productPageList.size()/productPageList.stream().mapToDouble(r -> r.getElapsed()).sum();
		double nofOfQueriesPerSecondForMarketingPageGroup = (double) marketingPageList.size()/marketingPageList.stream().mapToDouble(r -> r.getElapsed()).sum();
		double nofOfQueriesPerSecondForCouponCodeGroup = (double) couponList.size()/couponList.stream().mapToDouble(r -> r.getElapsed()).sum();
		double nofOfQueriesPerSecondForInfoPageGroup = (double) infoCsvList.size()/infoCsvList.stream().mapToDouble(r -> r.getElapsed()).sum();
		double nofOfQueriesPerSecondForLoginGroup = (double) loginList.size()/loginList.stream().mapToDouble(r -> r.getElapsed()).sum();
		double nofOfQueriesPerSecondForRegistrationGroup = (double) registrationList.size()/registrationList.stream().mapToDouble(r -> r.getElapsed()).sum();
		
		// Create csv file in src/com/shoe/task folder and add results to it.
		FileWriter fileWriter = new FileWriter(new File("src/com/shoe/task/test_analysis.csv"));
		String HEADER = "Start Time, End Time, No Of Queries, Total time (in sec), Longest Elapse time (in sec), Shortest elapse time (in sec), Average Elapse Time(in sec), QPS, QPS for Cart,"
				+ "QPS for Category, QPS for Search, QPS for Product page, QPS for Marketing page, QPS for Coupon code, QPS for Info page, QPS for Login, QPS for Registration";
		fileWriter.append(HEADER);
		fileWriter.append("\n");
		fileWriter.append(jobStartTime.toString());
		fileWriter.append(",");
		fileWriter.append(jobEndTime.toString());
		fileWriter.append(",");
		fileWriter.append(Integer.toString(noOfQueries));
		fileWriter.append(",");
		fileWriter.append(Double.toString(totalElapsedTime));
		fileWriter.append(",");
		fileWriter.append(Double.toString(longestElapsedTime));
		fileWriter.append(",");
		fileWriter.append(Double.toString(shortestElapsedTime));
		fileWriter.append(",");
		fileWriter.append(Double.toString(averageElapseTime));
		fileWriter.append(",");
		fileWriter.append(Double.toString(qps));
		fileWriter.append(",");
		fileWriter.append(Double.toString(nofOfQueriesPerSecondForCartGroup));
		fileWriter.append(",");
		fileWriter.append(Double.toString(nofOfQueriesPerSecondForCategoryGroup));
		fileWriter.append(",");
		fileWriter.append(Double.toString(nofOfQueriesPerSecondForSearchGroup));
		fileWriter.append(",");
		fileWriter.append(Double.toString(nofOfQueriesPerSecondForProductPageGroup));
		fileWriter.append(",");
		fileWriter.append(Double.toString(nofOfQueriesPerSecondForMarketingPageGroup));
		fileWriter.append(",");
		fileWriter.append(Double.toString(nofOfQueriesPerSecondForCouponCodeGroup));
		fileWriter.append(",");
		fileWriter.append(Double.toString(nofOfQueriesPerSecondForInfoPageGroup));
		fileWriter.append(",");
		fileWriter.append(Double.toString(nofOfQueriesPerSecondForLoginGroup));
		fileWriter.append(",");
		fileWriter.append(Double.toString(nofOfQueriesPerSecondForRegistrationGroup));
		fileWriter.append(",");
		
		// Flush and close the filewriter.
		fileWriter.flush();
		fileWriter.close();
		System.out.println("Test results analysis completed and results are written to test_analysis.csv file and saved in src/com/shoe/task folder.");
		
	}
}
