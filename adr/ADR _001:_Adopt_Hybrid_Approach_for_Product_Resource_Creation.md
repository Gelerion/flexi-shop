# ADR 001: Adopt Hybrid Approach for Product Resource Creation

**Date:** 2024-10-04  
**Status:** Accepted  
**Context:** RESTful API design for product resource with multiple associations  
**Decision Makers:** Gelerion
**Technical Story:** Designing an efficient and maintainable API for product management

---

## Context

We are developing a RESTful API for a product management system. Each product has multiple associations, including categories, tags, images, and specifications. The product resource interacts with several database tables representing these associations. We needed to decide on the API design approach for creating and managing products and their associated resources.

Two primary options were considered:

- **Single Composite Endpoint:**
  - Use one endpoint (`POST /products`) to handle the creation of a product along with all its associations in a single request.
  - The server validates and creates associated resources if they do not exist.
  
- **Separate Endpoints for Each Resource:**
  - Use individual endpoints for the product and each associated resource.
  - Clients create a basic product and then use dedicated endpoints to add categories, tags, images, and specifications.

Our goal was to choose an approach that balances client simplicity, adherence to RESTful principles, performance, and maintainability.

---

## Decision

We decided to adopt a hybrid approach:

### Composite Endpoint for Product Creation:

- Provide a `POST /products` endpoint that allows clients to create a new product along with its associations in a single request.
- This endpoint handles the creation of the product and associated resources atomically, ensuring data consistency.

### Separate Endpoints for Managing Associations Post-Creation:

- Offer dedicated endpoints for updating and managing associations after the product has been created.  
  **Examples:**
  - `POST /products/{productId}/categories`
  - `POST /products/{productId}/tags`
  - `POST /products/{productId}/images`
  - `POST /products/{productId}/specifications`
- This allows clients to modify specific aspects of a product without affecting the entire resource.

---

## Rationale

The hybrid approach combines the advantages of both options while mitigating their drawbacks:

- **Client Simplicity During Creation:**  
  Clients can create a complete product with all associations in a single API call, reducing client-side complexity and the need for multiple HTTP requests during initial creation.

- **Adherence to RESTful Principles:**  
  By using separate endpoints for managing associations post-creation, we maintain a resource-oriented architecture. Each resource can be managed independently, promoting modularity and scalability.

- **Performance Optimization:**  
  Combining creation into one request minimizes latency and network overhead, improving performance, especially in environments with high latency or limited bandwidth.

- **Atomic Operations and Data Consistency:**  
  The composite creation endpoint handles transactions atomically, ensuring that all associated resources are created together. This prevents partial data creation and maintains database integrity.

- **Maintainability and Extensibility:**  
  The modular design allows for easier maintenance and future enhancements. Changes to one resource type (e.g., tags) have minimal impact on others.

---

## Consequences

### Positive Outcomes:

- **Enhanced Client Experience:**  
  Simplifies the product creation process for clients and reduces the likelihood of errors caused by multiple dependent requests.

- **Scalable Architecture:**  
  The API design is scalable and can accommodate future requirements. New associations or resources can be added with minimal disruption.

- **Balanced RESTful Compliance:**  
  Maintains a balance between practical client needs and RESTful design principles, encouraging proper resource management and clear API structure.

### Potential Trade-offs:

- **Increased Server-Side Complexity:**  
  The server must handle more complex logic during the product creation process, requiring robust validation and error handling mechanisms.

- **Partial Deviation from Strict REST Principles:**  
  The composite endpoint for creation slightly deviates from the ideal of one resource per endpoint. However, this is mitigated by the benefits in client simplicity and performance.

- **Transaction Management Overhead:**  
  Implementing atomic transactions for the composite creation may introduce additional overhead, which needs careful handling to ensure system performance is not adversely affected.

---

## Alternatives Considered

- **Single Composite Endpoint Only:**
  - **Pros:** Simplifies client interactions; ensures atomicity.
  - **Cons:** Increases server complexity; violates RESTful principles; less modular.

- **Separate Endpoints Only:**
  - **Pros:** Adheres strictly to REST; modular and maintainable.
  - **Cons:** Increases client complexity; multiple requests may affect performance; harder to ensure atomicity.

We concluded that neither alternative fully met our requirements when considered alone.

---

## Implementation Plan

1. **Develop the Composite Creation Endpoint:**
   - Design the `POST /products` endpoint to accept a comprehensive product resource.
   - Implement server-side logic to handle validation, creation, and transaction management.

2. **Create Separate Endpoints for Associations:**
   - Implement endpoints for managing categories, tags, images, and specifications.
   - Ensure each endpoint follows RESTful conventions and includes proper validation.

3. **Robust Validation and Error Handling:**
   - Implement detailed validation for all input data.
   - Provide clear and informative error messages to clients.

4. **Documentation and OpenAPI Specification:**
   - Generate and maintain an OpenAPI Specification for the API.
   - Include examples and usage guidelines to assist client developers.

5. **Testing and Quality Assurance:**
   - Perform thorough testing to ensure atomicity, data integrity, and performance.
   - Include unit, integration, and end-to-end tests for all endpoints.

---

## Outcome Measurement

- **Client Adoption and Feedback:**  
  Monitor client usage patterns and gather feedback on the API's usability. Adjust the API based on client needs and pain points.

- **Performance Metrics:**  
  Track API response times and error rates. Optimize server performance as needed.

- **Maintenance and Scalability:**  
  Assess the ease of maintaining and extending the API over time. Ensure the architecture supports future growth and changes.

---

## References

- **OpenAPI Specification:**  
  The API design follows the OpenAPI Specification version 3.0.  
  See the attached OpenAPI YAML file for detailed endpoint definitions.

- **RESTful API Design Principles:**  
  Fielding, R. T. (2000). *Architectural Styles and the Design of Network-based Software Architectures*.

---

**Decision accepted on:** 2024-10-04  
**Review Date:** [Set a future date for reviewing the decision, if applicable]
