# Mini CAS (Credit Approval System)

A **classic enterprise Java web application** implementing a **Maker–Checker workflow** for loan processing.  
Built using **Servlets, JSP/JSTL, JDBC, and Oracle DB**, the system mirrors real-world **banking approval systems** without using Spring or Hibernate.

This version represents the **pre-Hibernate architecture**, focused on **explicit control, transparency, and traditional Java EE patterns**.


## 📌 Key Objectives

- Implement a **Maker–Checker approval flow** for loan applications
- Maintain **temporary vs approved data separation** (TEMP & MASTER tables)
- Follow **banking-grade validation and audit patterns**
- Avoid frameworks to demonstrate **core Java EE mastery**


## 🧱 Architecture Overview

```
Browser (JSP)
   ↓
Servlet Controllers
   ↓
Service Layer (Business Logic)
   ↓
DAO Layer (JDBC)
   ↓
Oracle Database
```

### Design Principles

- Clear **separation of concerns**
- Explicit **transaction boundaries**
- No hidden magic (no ORM, no DI container)
- Deterministic SQL behavior


## 🧑‍💼 User Roles

| Role    | Responsibilities |
|--------|------------------|
| **Maker** | Create, modify, delete loan applications |
| **Checker** | Approve or reject loan applications |


## 🔄 Maker–Checker Workflow (Core Feature)

### Dual Table Strategy

| Table | Purpose |
|-----|--------|
| `LOAN_APPLICATION_TEMP` | Pending / modified / deletion requests |
| `LOAN_APPLICATION_MASTER` | Authorized (approved) loans |

### Workflow States

- `NEW`
- `PENDING_MODIFICATION`
- `PENDING_DELETION`
- `AUTHORIZED`
- `REJECTED_*`

This mirrors **real banking systems**, where no change directly affects master data without approval.


## 🗂️ Project Structure

```
src/
 ├── controller/          # Servlets (request handling)
 ├── service/             # Business logic
 │    ├── maker/
 │    └── checker/
 ├── dao/
 │    └── impl/            # JDBC implementations
 ├── entity/              # Plain Java domain models
 ├── util/                # Utility classes
 ├── filter/              # Authentication & Remember-Me filters
 └── exception/           # Custom business exceptions
```


## 🧾 Core Modules

### 1️⃣ Authentication

- Login with role (Maker / Checker)
- Remember-Me support via cookies
- Session-based authorization
- Filter-driven access control

### 2️⃣ Customer Management

- Capture personal details
- Address details
- Multiple education records
- One highest degree enforced

### 3️⃣ Loan Application Management

- Create loan applications
- Modify existing loans
- Delete requests (soft delete via TEMP)
- Search loans by application number

### 4️⃣ Approval Processing (Checker)

- Approve new loans
- Approve modifications
- Approve deletions
- Reject operations with proper status handling


## 🛠️ Technology Stack (Pre-Hibernate)

| Layer | Technology |
|----|-----------|
| Frontend | JSP, JSTL, Bootstrap |
| Backend | Java Servlets |
| Persistence | JDBC |
| Database | Oracle |
| Logging | Log4j2 |
| Build Tool | Maven |
| Server | Apache Tomcat |


## 📄 Database Design Highlights

- Normalized schema
- Explicit foreign keys
- Separate TEMP & MASTER tables
- Audit-friendly design
- No cascade side effects


## 🔐 Security Considerations

- Role-based access control
- Filter-based route protection
- Session fixation prevention
- Controlled form submissions
- Explicit validation (server-side)


## 🧪 Validation Strategy

- Null & empty checks
- Numeric range validation
- Enum safety via constants
- Business rule enforcement in services
- No client-side trust


## 📜 Logging & Monitoring

- Log4j2 based logging
- DAO-level SQL error logs
- Service-level business flow logs
- Filter-level authentication logs


## 🚀 How to Run

### Prerequisites

- JDK 17+
- Apache Tomcat 9+
- Oracle Database
- Maven 3.9+

### Steps

```bash
mvn clean package
```

Deploy the generated `mini-cas.war` to Tomcat.

Access:

```
http://localhost:8080/mini-cas
```


## 🧭 Why This Architecture Matters

This version intentionally avoids Hibernate/Spring to:

- Demonstrate **core Java EE fundamentals**
- Provide **full control over SQL**
- Reflect **legacy + enterprise banking systems**
- Make migration paths explicit and safe


## 🔄 Next Phase (Planned)

- Hibernate ORM integration
- Session-per-request model
- Removal of TEMP/MASTER duplication
- Aggregate-based domain modeling
- Transactional consistency improvements


## 🏦 Real-World Relevance

This application closely resembles:

- Bank loan approval systems
- Government verification workflows
- Compliance-driven enterprise software
- Legacy modernization projects


## 📌 Status

**Stable – Pre-Hibernate Baseline**  
Used as a reference implementation before ORM adoption.
