# Project Guidelines

## 1. Layer Architecture

## 2. Naming Conventions Per Layer

## 3. Use ADR for decisions (API-First/DB-First,..)

## Repository Layer

### Entity Retrieval: Optional vs. Null

#### Do:
- Use `Optional<Entity>` to handle potentially missing records.

#### Avoid:
- Returning `null` from the repository and relying on manual `null` checks.
- Throwing exceptions from the repository layer for flow control.

#### Why?
Returning an `Optional<Entity>` is considered best practice because it forces the caller to explicitly handle the case where the entity might not be present. It encourages safe handling of potential `null` values, leading to more readable and robust code.

#### Example:
```java
// Repository Layer
Optional<Employee> findById(Long id);

// Service Layer
public Employee getEmployeeById(Long id) {
    return employeeRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
}
```