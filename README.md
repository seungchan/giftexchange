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

## To run locally
```gradle bootRun```

## To run the tests
```gradle test```

## To build and run Docker image
```
gradle build
docker build -t giftexchange .
docker run -p 8080:8080 giftexchange
```


## Sample http requests
```
curl -X POST --header "Content-Type: application/json" --data '{
  "fullname": "John Adams"
}' http://localhost:8080/members

curl -X POST --header "Content-Type: application/json" --data '{
  "fullname": "Jamie Adams"
}' http://localhost:8080/members

curl -X GET http://localhost:8080/members

Replace giverid and receiverid below from the result of previous GET request.

curl -X POST --header "Content-Type: application/json" --data '{
  "giverid": "8b268718-030d-4c1d-b282-c07f14bb7940",
  "receiverid": "ff691245-7ccd-4e9d-8c4f-cbd03e603ac2",
  "time": "2023-01-30T03:15:21.721704Z"
}' http://localhost:8080/gift_exchange/shuffle

curl -X GET http://localhost:8080/gift_exchange

```

## Future improvements
1. Add more unit tests
2. migrate data store to RDBMS
3. member verification when a gift is shuffled
4. Address concurrent issue when many family member access the application
