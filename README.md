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

## Implementation

###Start From:
* Method Name: **calcTradeAllocation**
* Class Name: **AllocationService**

###Steps:
1. Get all the trades from Trades Table: List<Trade> tradeList
2. createAllocationMetric(tradeList) and pass tradeList from step 1 
    - Iterate over tradeList
    - Get filtered Capitals list where stock value in holding and target equals stock value in trade.
      **Assumption Note**: For a given account and stock in capital.csv: there will be (only one value) in holdings.csv and target.csv file 
        - Calculate *TargetMarketValue* and save it in Capital entity
    - Calculate Total_Quantity of given stock across account. This required for calculating ALL_IN_POSITION value.
    - Calculate Total_Target_Value by summing the targetMarketValue stored in each Capital entity from step 2.2
    - At this point we have everything required to Allocation_Math. Call **calcUsingAllocationMath** method and pass required datasets
3. create Allocation data using AllocationMetric crested from Step 2
   - Create Map with Key as Stock and value as List<AllocationMetric>. We are doing this step so that when we iterate over map all the values in AllocationMetric will belong to same stock
   - Iterate over map created from Step 1
   - Sort the AllocationMetric list in ascending order using SuggestedTradeAllocation column so that we start with smallest
   - Iterate over AllocationMetric list to determine actual shares to be attributed
      - Check for error condition - if yes then set quantity as Zero and set hasErrorCondition == true else Use Math.round function to round **SuggestedTradeAllocation* and save it as allocation quantity
      - if there is error condition then check if there are any older allocation entries for same stock. If yes then set quantity value for all of them to be Zero
      - Also calculate remainingQuantity by subtracting total(for first time)/remaining shares and quantity. If iterator does not have next value then use remainingQuantity as quantity value for allocation
   
   
## Package Structure
- ### domain
    - **Allocation.java**
      
    | Account | Stock | Quantity |
    |---------|-------|-----|
    | John | GOOGLE |  +41 |
    | Sarah | GOOGLE |  +59 |
    | John | APPLE |  0 |
    | Sarah | APPLE |  0 |
  
    - **AllocationMetric.java**
      
    | Account | Stock | Type | QuantityHeld | targetMarketValue | maxShares | allInPosition | suggestedFinalPosition | suggestedTradeAllocation|
    |---------|-------|------|--------------|-------------------|-----------|---------------|------------------------|-------------------------|
    | John | GOOGLE | BUY | 50 | 2000 | 100 | 160 | 91.43 | 41.43 |

- **model** (DB entities with relationships)
- **repository** (Spring JPA repositories for interacting with DB)
- **service**

   


