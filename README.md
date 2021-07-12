# Trade Allocation

## Introduction
Mary is a very successful portfolio manager specializing in technology stocks. Because she is so successful in selecting winners, she is now trading for several clients each with their own separate account. Each client will have a different amount of money invested with Mary (E.g. investor John has $50,000 while investor Sarah has $150,000 in her account). Furthermore each client will have different risk appetite for their portfolio (for example, John likes risk and wants 70% of his portfolio to be comprised of highly volatile stocks, while risk-adverse Sarah only wants 20% of her portfolio to be allocated to high risk stocks).

Whenever Mary buys a new stock (for example 100 shares of GOOGLE), she cannot evenly assign 50 shares to John's account and 50 shares to Sarah's account. Mary must split those shares amongst all her client's accounts in accordance with the size of their account while conforming to her client's risk preferences. Mary has been doing all the calculations in Excel spreadsheets and has asked you to automate the process by writing a Java program to implement the rules for allocating her trades.

## How to test
**Input Files**: TradeAlloc/src/test/resources/input/ 
* trades.csv 
* capital.csv
* holdings.csv 
* targets.csv 

**Output File**: TradeAlloc/src/test/resources/output/
* allocations.csv

Update the test data based on different use-case in the input files. Run method **test_calcTradeAllocationSuccess** in test class **AllocationServiceTest**. 

**Test execution Steps**:
* Load data from input files to in memory H2 DB
* Calls **calcTradeAllocation** method in class **AllocationService**
* Writes List<Allocation> in allocations.csv file



