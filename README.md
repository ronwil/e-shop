## E-Shop app

This is a Spring Boot API for managing products and shopping carts in an e-commerce shop.

Features:

Product: Contains code related to a Product
    List all products
    List one product
    Create a new product
    Delete an existing product

Products: Contains code related to products array
    1:M relationship with Product.
    1:M relationship with Shopping cart.

Shopping Carts:
    Create a shopping cart
    List all shopping carts
    Modify a shopping cart
    Checkout a shopping cart

Technology Stack:
    Spring Boot
    Database - I used Postgres
    Postman - API testing tool

src/main/java: Contains your application's Java source code.
com/online/eshop: Package name.

Products folder contains all the code related to each product and list of products - Repository, service, entity and DTO classes.
Similarly Shopping cart folder contains all the code related to each the cart - Repository, service, entity and DTO classes.

pom.xml: The project's dependencies configuration.


## TESTS

1. ProductControllerTest
    - Find a product by id.
    - Product name cannot exceed 200 characters.
    - Product name cannot be empty.
    - Restricted set of labels
    - Product name already exists.
    - Delete a product by id.

2. ShoppingCartTest
    - Create a cart.
    - Update cart with product.
    - Cannot update a checked out cart.
    - Check out api with total price

## Getting Started:

- Unzip the folder and open the folder in an editor of your choice - I used VS code.
- Install the required dependencies (Maven).
- Configure your database connection details in application.properties.
- Refer Local_DB.session.sql file to create all the necessary tables.
- Run the application using mvn spring-boot:run.
- Access the API endpoints and documentation as described above.
