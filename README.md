# 🚚 Shipping Charge Estimator — Jumbotail Assignment

A Spring Boot REST API that calculates shipping charges for a B2B e-commerce marketplace serving Kirana stores across India.

---

## 🏗️ Tech Stack

| Layer       | Technology        |
|-------------|-------------------|
| Framework   | Spring Boot 4.0.3 |
| Language    | Java 25           |
| Database    | H2 (In-Memory)    |
| ORM         | Spring Data JPA   |
| Boilerplate | Lombok            |
| Testing     | JUnit 5 + Mockito |

---

## 📐 Architecture
Request → Controller → Service → Repository → H2 Database
↘ Utils (Haversine, TransportMode, DeliverySpeed)

**Layered Architecture:**
- **Controller** — Receives HTTP requests, validates input, returns responses
- **Service** — All business logic lives here
- **Repository** — Data access via Spring Data JPA (zero SQL written)
- **Utils** — Pure stateless calculation helpers
- **DTOs** — Clean API contracts, DB internals never exposed
- **GlobalExceptionHandler** — All errors handled in one place

---

## 🗂️ Entity Model
Seller ──< Product     (One seller has many products)
Warehouse              (Independent — located across India)
Customer               (Kirana stores with GPS coordinates)

---

## ⚙️ How Shipping Is Calculated

### Step 1 — Find Nearest Warehouse
Seller drops product at the nearest warehouse. Distance is calculated using the **Haversine Formula** which accounts for Earth's curvature to give accurate GPS-based distances.

### Step 2 — Select Transport Mode
| Distance     | Mode      | Rate          |
|--------------|-----------|---------------|
| 500 km+      | Aeroplane | ₹1 / km / kg |
| 100 – 499 km | Truck     | ₹2 / km / kg |
| 0 – 99 km    | Mini Van  | ₹3 / km / kg |

### Step 3 — Apply Delivery Speed
| Speed    | Charge Formula                                           |
|----------|----------------------------------------------------------|
| Standard | ₹10 flat + base shipping charge                          |
| Express  | ₹10 flat + ₹1.2/kg express charge + base shipping charge |

---

## 🚀 Running The Project

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps
```bash
# 1. Clone the repo
git clone https://github.com/Abhxay/jumbotail-shipping-estimator.git

# 2. Navigate into project
cd jumbotail-shipping-estimator

# 3. Run the app
mvn spring-boot:run
```

App starts at: `http://localhost:8080`

### View The Database (H2 Console)
Visit `http://localhost:8080/h2-console`
JDBC URL:  jdbc:h2:mem:shippingdb
Username:  sa
Password:  (leave empty)

---

## 📡 API Reference

### API 1 — Get Nearest Warehouse for a Seller
GET /api/v1/warehouse/nearest?sellerId={id}&productId={id}

**Sample Request:**
GET /api/v1/warehouse/nearest?sellerId=1&productId=1

**Sample Response:**
```json
{
  "warehouseId": 1,
  "warehouseName": "BLR_Warehouse",
  "latitude": 12.99999,
  "longitude": 77.92327,
  "city": "Bangalore",
  "state": "Karnataka"
}
```

> **Design Note:** Nearest warehouse is determined by the seller's GPS coordinates using the Haversine Formula. `productId` is accepted per API contract but warehouse selection depends on seller location only.

---

### API 2 — Get Shipping Charge from Warehouse to Customer
GET /api/v1/shipping-charge?warehouseId={id}&customerId={id}&productId={id}&deliverySpeed={speed}

**Sample Request:**
GET /api/v1/shipping-charge?warehouseId=1&customerId=1&productId=1&deliverySpeed=standard

**Sample Response:**
```json
{
  "shippingCharge": 520.50,
  "transportMode": "TRUCK",
  "distanceKm": 254.43,
  "deliverySpeed": "standard",
  "nearestWarehouse": {
    "warehouseId": 1,
    "warehouseName": "BLR_Warehouse",
    "latitude": 12.99999,
    "longitude": 77.92327,
    "city": "Bangalore",
    "state": "Karnataka"
  }
}
```

> **Design Note:** `productId` is required beyond the original spec because product weight is essential to calculate shipping charge accurately.

---

### API 3 — Full Shipping Charge Calculation
POST /api/v1/shipping-charge/calculate
Content-Type: application/json

**Sample Request Body:**
```json
{
  "sellerId": 1,
  "customerId": 1,
  "productId": 2,
  "deliverySpeed": "express"
}
```

**Sample Response:**
```json
{
  "shippingCharge": 5098.60,
  "transportMode": "AEROPLANE",
  "distanceKm": 498.43,
  "deliverySpeed": "express",
  "nearestWarehouse": {
    "warehouseId": 1,
    "warehouseName": "BLR_Warehouse",
    "latitude": 12.99999,
    "longitude": 77.92327,
    "city": "Bangalore",
    "state": "Karnataka"
  }
}
```

---

## ❌ Error Handling

All errors return a consistent JSON structure:
```json
{
  "timestamp": "2026-03-15T10:30:00",
  "status": 404,
  "error": "Seller not found with id: 999"
}
```

| Scenario                    | HTTP Status |
|-----------------------------|-------------|
| Seller / Customer not found | 404         |
| Missing request parameter   | 400         |
| Invalid delivery speed      | 400         |
| Validation failure          | 400         |
| Unexpected server error     | 500         |

---

## 🧪 API Testing

### Postman Collection
All APIs are documented and testable via the public Postman collection:

[▶ Run in Postman](https://www.postman.com/abhay-8701139/workspace/postman-api-fundamentals-student-expert/collection/46299576-b0ad497c-d0f0-4899-a2b7-c872a2feffd1?action=share&creator=46299576)

### Seed Data Reference
Use these IDs when testing:

| Entity    | ID | Name                |
|-----------|----|---------------------|
| Seller    | 1  | Nestle Seller       |
| Seller    | 2  | Rice Seller         |
| Seller    | 3  | Sugar Seller        |
| Customer  | 1  | Shree Kirana Store  |
| Customer  | 2  | Andheri Mini Mart   |
| Product   | 1  | Maggie 500g (0.5kg) |
| Product   | 2  | Rice Bag 10kg       |
| Product   | 3  | Sugar Bag 25kg      |
| Warehouse | 1  | BLR_Warehouse       |
| Warehouse | 2  | MUMB_Warehouse      |

### Running Unit Tests
```bash
mvn test
```
Tests run: 13, Failures: 0, Errors: 0
✅ WarehouseServiceTest  (4 tests)
✅ HaversineUtilTest     (9 tests)

---

## 📦 Seed Data (Preloaded on Startup)

| Type      | Records                                                             |
|-----------|---------------------------------------------------------------------|
| Warehouse | BLR_Warehouse (Bangalore), MUMB_Warehouse (Mumbai)                  |
| Seller    | Nestle Seller (Chennai), Rice Seller (Mumbai), Sugar Seller (Delhi) |
| Customer  | Shree Kirana Store (Faridabad), Andheri Mini Mart (Mumbai)          |
| Product   | Maggie 500g (0.5kg), Rice Bag 10kg, Sugar Bag 25kg                  |

---

## 👤 Abhay Thakur
Built as part of the Jumbotail Backend Engineering Assignment.