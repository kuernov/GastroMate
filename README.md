Gastromate
==========

Gastromate is an application designed to support restaurant management, including order handling, ingredient management, sales reporting, and forecasting ingredient demands.

Features
--------

- **Order Management**:
    - View a paginated list of orders.
    - Filter orders by date range.
    - Display order details, including menu items and total cost.

- **Ingredient Management**:
    - Add, edit, and delete ingredients.
    - Log inventory changes (add, subtract, or adjust stock).
    - Group ingredients and calculate their total quantity.

- **Menu and Filters**:
    - Filter menu items by ingredients, categories, pizza sizes, and price ranges.

- **Sales Reports**:
    - Generate sales reports by category, days of the week, hours, and other metrics.

- **Demand Forecasting**:
    - Automatically forecast ingredient demand using ARIMA/SARIMA models.
    - Use historical data to predict future needs.

Technologies
------------

### Backend
- **Spring Boot**: Handles business logic and REST API.
- **PostgreSQL**: Relational database.
- **Gradle**: Build system.
- **Liquibase**: Database schema version control.

### Frontend
- **React**: User interface.
- **Ant Design**: UI components library.

### Forecasting
- **Python**: Flask for ARIMA/SARIMA models.

### Containerization
- **Docker**: Runs backend, frontend, Flask, and database in containers.

System Requirements
-------------------

- **Docker**: 24+

Installation
------------

### Running with Docker (recommended):

1. **Clone the repository**:
   ```
   git clone https://github.com/kuernov/gastromate.git
   cd gastromate
   ```

2. **Start the application**:
    - Run the following commands to start all services (backend, frontend, and database):
      ```
      docker-compose up db
      ./gradlew build
      docker-compose up --build
      ```
    - This will start:
        - Backend: `http://localhost:8080`
        - Frontend: `http://localhost:3000`
        - Flask: `http://localhost:5000`
        - PostgreSQL database.

3. **Verify the services**:
    - Ensure that all containers are running:
      ```
      docker ps
      ```
    - Access the application in your browser at `http://localhost:3000`.


Project Structure
-----------------

```
src/main/java/com/yourcompany/gastromate
├── config             # Application configurations (CORS, security, etc.)
├── controller         # REST API controllers
├── dto                # Data transfer objects
├── data               # JPA entities
├── repository         # Database repositories
├── service            # Business logic
├── prediction			# Service and controller for prediction module
├── mapper             # Mapping entities to DTOs
├── exception          # Exception handling and custom errors
├── util               # Utility classes and helpers
```
