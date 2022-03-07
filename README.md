# Transactions routine

Transactions routine REST API

## Built With

* Java 11 - The programming language used
* Spring Boot 2.6.4 - The web framework used
* Database H2 (In-Memory) - The in-memory database
* Lombok - Lombok is used to reduce boilerplate code for model/data objects,
* Maven 3.x - Dependency Management
* Spring doc openapi - API Documentation (spring-boot and swagger-ui)

## Steps to run the application

**1. Clone the repository**

```bash
git clone https://github.com/shridhar-hitnalli/transactions-routine.git
```

**2. To start the application in docker -> Execute the script**
```bash
/bin/bash /path/to/transaction-routine/entrypoint.sh
```

**3. To run the application using maven**
```bash
./mvnw spring-boot:run
```
The app will start running at <http://localhost:8080>

**3. To access the swagger documentation**
http://localhost:8080/swagger-ui/index.html

**4. To run the tests**
```bash
 ./mvnw clean test
```

## Rest APIs

The app defines following APIs.

### Account

| Method | Url            | Decription                | Sample Valid Request Body | 
|--------|----------------|---------------------------|---------------------------|
| POST   | /accounts      | Create account            | [JSON](#accountcreate)    |
| GET    | /accounts/{id} | Get accound by account id | /accounts/1               |

### Transaction

| Method | Url                               | Description                                                                           | Sample Valid Request Body  |
|--------|-----------------------------------|---------------------------------------------------------------------------------------|----------------------------|
| POST   | /transactions                     | Create a transaction                                                                  | [JSON](#transactioncreate) |


Test them using postman or any other rest client.

## Sample Valid JSON Request

##### <a id="accountcreate">Create a new account-> /accounts</a>
```json
{
  "documentNumber" : "1321212332"
}
```

##### <a id="transactioncreate">Create a new transaction-> /transactions</a>
```json
{
  "accountId" : 1,
  "operationTypeId" : 1,
  "amount" : -23.00
}
```
