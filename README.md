# Secret Gift Exchange
## REST endpoints:
### GET /gift_exchange
     Show the most recent gift exchange
### POST /gift_exchange/shuffle
     Shuffle the gift exchange and return the results of the shuffle
### GET /members
     List the family members
### POST /members
     Add a new family members

## To run
```gradle bootRun```

## To run the tests
```gradle test```



## Future improvements
1. Add more unit tests
2. migrate data store to RDBMS
3. member verification when a gift is shuffled
4. Address concurrent issue when many family member access the application
