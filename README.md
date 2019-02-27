# Payment API

This API aims to deal with payments. It allows to:
* List all payments
* Create a payment
* Get a given payment by ID
* Update a given payment
* Delete a payment

## Getting started

In order to start the application, simply run ``mvn spring-boot:run``

The application will start on port **9000** (could be changed in the configuration file).

## API Documentation

Access the Swagger UI documentation here: http://localhost:9000/swagger-ui.html 
You can even launch a bunch of test from there.

## Service health and monitoring    

Access the health endpoint here: http://localhost:9000/actuator/health
Further configuration is needed in order to provide metrics etc.



