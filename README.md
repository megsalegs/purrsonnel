🐾 Purrsonnel

Purrsonnel is a full-stack cat-themed hiring platform where users can browse, bookmark, review, and request to “hire” cats based on skills and experience.

The concept is playful. The architecture is not.

Built as a capstone project, Purrsonnel demonstrates production-style backend design, relational data modeling, layered architecture, JWT authentication, and modern frontend/backend integration.

# Tech Stack
- Backend
    - Java
    - Spring Boot
    - Spring Security (JWT Authentication)
    - JDBC
    - PostgreSQL
    - RESTful API Architecture
    - Layered Architecture (Controller → Service → DAO)
    - JUnit (Unit & Integration Testing)
- Frontend
    - React
    - React Router
    - Axios
    - CSS Modules
    - Responsive Design (Grid + Flexbox)
- Dev Tools
    - IntelliJ
    - VS Code
    - Git / GitHub
    - Postman
    - pgAdmin

 # Architecture Overview
 Purrsonnel follows a strict layered architecture:

                Controller
                    ↓
                 Service
                    ↓
                   DAO
                    ↓
              PostgreSQL Database

- Backend Design Principles
- Separation of concerns across layers
- DAO pattern for database abstraction
- Explicit SQL queries (no ORM)
- Foreign key constraints to enforce referential integrity
- Transaction handling for data consistency
- Aggregate queries for ranking logic
- Role- based access control via Spring Security
- JWT- based stateless authentication

# Database Design
- Designed and normalized from scratch.
 - Core Tables:
   - users
   - cats
   - cat_image
   - review
   - hire_request
   - user_bookmark
   - cat_prior_experience
 - Key Features:
    - Foreign key constraints with cascade rules
    - Aggregate rating calculations
    - Ranking score computation
    - Many- to- many relationships (bookmarks)
    - Timestamp auditing
    - Constraint- driven validation

# Authentication & Authorization
- JWT-based authentication:
     - frontend token storage 
     - Axios interceptors for attaching Bearer tokens
     - backend signature validation via Spring Security.
- Secure login & registration
- Role-based access:
     - Anonymous users
     - Authenticated users
     - Staff role
- Protected API endpoints

# Core Features
- For All Users:
     - Browse cats
     - Search & sort
     - View cat profiles
     - See ratings and reviews
- For Authenticated Users:
     - Bookmark cats
     - Submit hire requests
     - Leave reviews after completed hires
- For Staff:
     - Manage listings (CRUD + set Featured)
     - Approve/deny hire requests
     - Moderate user-generated reviews, image uploads, and listing metadata to preserve data quality and platform integrity.

# RESTful API Design
- Endpoints follow REST conventions
- Sample:

- GET     /cats
- GET     /cats/{id}
- POST    /hire- requests
- PUT     /hire- requests/{id}
- DELETE  /bookmarks/{catId}
- POST    /auth/login

- JSON request/response bodies
- Meaningful HTTP status codes
- Centralized exception handling
- Validation and error messaging

# Frontend Implementation
- Component- based React architecture
- View- level CSS Modules
- Responsive layouts (mobile + desktop)
- Axios service abstraction layer
- Conditional rendering based on auth state
- Optimistic UI updates for bookmarking
- Dynamic image handling (local + external URLs)

# Screenshots
   # Landing Page
   /
     - Responsive React interface consuming RESTful Spring Boot endpoints.
     - Navigation and conditional rendering reflect authentication state managed via JWT.
- <img width="1623" height="969" alt="Screenshot 2026-02-13 at 5 15 47 PM" src="https://github.com/user-attachments/assets/041a3c85-7919-4434-951b-d097e0fd8057" />

     - Note: Paw animates into the viewport on a 9s loop; cat heads float with staggered nth- child delays.
- <img width="1657" height="1042" alt="Screenshot 2026-02-13 at 5 20 18 PM" src="https://github.com/user-attachments/assets/793e919f-1596-4f2c-be87-5bcd4cb3ca4a" />
    - Note: Paw animation continues; footer cat heads bounce; "Sign In" button renders transparent when unauthenticated and restores opacity with transform on hover.

    # Cat Details View
  /
      - Dynamic profile view backed by relational joins across cats, reviews, images, and prior experience tables.           - Aggregate SQL queries compute average rating and ranking score in real time.
   - This signals:
      - JOIN operations
      - Aggregate functions
- <img width="433" height="744" alt="Screenshot 2026-02-13 at 5 27 01 PM" src="https://github.com/user-attachments/assets/d9f770c9-3471-4fc3-874a-054d2f8d5e6c" />
    - Note: Profile images expand on hover; reviews collapse after two entries; staff view includes moderation action 
     buttons.
     
# Hire Requests Dashboard -  Staff
/
      - Authenticated user dashboard displaying hire request lifecycle states (pending, approved, denied). 
      - Data retrieved through protected endpoints with role-based authorization enforced via Spring Security.
   - This signals:
      - State management
      - Authorization
      - Business logic
- <img width="983" height="1053" alt="Screenshot 2026-02-13 at 5 33 43 PM" src="https://github.com/user-attachments/assets/17dd46a6-be31-4628-bbde-951ad3ceefa9" />

 # My Hire Requests 
 /
      - Authenticated dashboard displaying user-scoped hire requests and lifecycle states (pending, approved, denied).
      - Endpoints filter data serverside based on the authenticated principal and enforce role-based access control. 
      - Status transitions reflect transactional backend logic and constraint-validated relationships between users 
      and cats.
  - This signals:
      - User-scoped queries
      - Protected routes
      - Lifecycle state management
- <img width="1671" height="1017" alt="Screenshot 2026-02-13 at 5 41 21 PM" src="https://github.com/user-attachments/assets/3af47d42-c33d-4155-905c-72e9761693db" />
     - Note: Sliding paw animations reappear with staggered timing; status badges render conditionally; date ranges 
     formatted client-side; cancelled requests excluded via backend filtering.

 # Manage Photos (staff):
 /
      - Role-restricted administrative interface for managing cat image records.
      - Backend validation enforces referential integrity and single-primary-image constraints per listing.
   - This signals:
      - Administrative authorization
      - Constraint enforcement
      - Relational image handling
- <img width="1070" height="1012" alt="Screenshot 2026-02-13 at 5 35 35 PM" src="https://github.com/user-attachments/assets/80779ffa-87e0-49df-8d63-3003fac7ca07" />
    - Note: Primary image toggling updates a relational flag; deletion triggers constraint-safe removal; UI reflects 
    changes immediately after successful REST response.

    

# Testing
- JUnit tests for service layer logic
- Integration tests for DAO/database interactions
- Manual endpoint validation via Postman
