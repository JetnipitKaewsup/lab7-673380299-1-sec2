# Game Management System

ระบบจัดการข้อมูลเกม พัฒนาด้วย **Spring Boot** โดยใช้สถาปัตยกรรมแบบ Layered Architecture แบ่งหน้าที่ของระบบออกเป็น Entity, Repository, Service และ Controller เพื่อให้โค้ดมีโครงสร้างชัดเจนและง่ายต่อการดูแล

ระบบรองรับการจัดการข้อมูลเกมแบบ CRUD และมีการใช้ **Strategy Pattern** สำหรับคำนวณราคาหลังส่วนลด เช่น ส่วนลดนักเรียนและส่วนลดเทศกาล

---
## รายงานอภิปราย
[Lab 7 Software Design 673380299-1 Section 2.pdf](https://github.com/JetnipitKaewsup/lab7-673380299-1-sec2/blob/2219f8556a3a0c183edae60c1fc4f8871bb6ff35/Lab%207%20Software%20Design%20673380299-1.pdf)

## Technologies

* Java
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Thymeleaf
* Maven

---

## Features

* แสดงรายการเกมทั้งหมด
* แสดงรายละเอียดเกมตาม ID
* เพิ่มข้อมูลเกม
* แก้ไขข้อมูลเกม
* ลบข้อมูลเกม
* คำนวณราคาหลังส่วนลด
* รองรับส่วนลดหลายประเภทด้วย Strategy Pattern
* เชื่อมต่อข้อมูลกับ PostgreSQL

---

## Project Structure

```text
src/
└── main/
    ├── java/com/example/demo/
    │   │
    │   ├── model/
    │   │   └── Game.java
    │   │
    │   ├── repository/
    │   │   └── GameRepository.java
    │   │
    │   ├── service/
    │   │   └── GameService.java
    │   │
    │   ├── controller/
    │   │   └── GameController.java
    │   │
    │   └── strategy/
    │       ├── DiscountStrategy.java
    │       ├── DiscountContext.java
    │       ├── NoDiscountStrategy.java
    │       ├── StudentDiscountStrategy.java
    │       └── SeasonalSaleStrategy.java
    │
    └── resources/
        ├── templates/
        │   └── games/
        │       └── ...
        │
        └── application.properties
```

---

## Architecture

ระบบแบ่งออกเป็น Layer หลักดังนี้

```text
Browser
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Repository
   │
   ▼
PostgreSQL
```

### Entity

ไฟล์ `Game.java`

ทำหน้าที่เป็น Entity สำหรับแทนข้อมูลเกมและ Mapping กับตาราง `lab7demo` ใน PostgreSQL โดยมีข้อมูล เช่น

* ID
* ชื่อเกม
* ประเภทเกม
* Platform
* Rating
* วันที่วางจำหน่าย
* ราคา
* ประเภทส่วนลด
* ราคาสุทธิ

### Repository

ไฟล์ `GameRepository.java`

ทำหน้าที่จัดการการเข้าถึงข้อมูล โดยใช้ `JpaRepository` ของ Spring Data JPA ซึ่งช่วยให้สามารถดำเนินการ CRUD กับฐานข้อมูลได้โดยไม่ต้องเขียน SQL สำหรับการทำงานพื้นฐานเอง

### Service

ไฟล์ `GameService.java`

ทำหน้าที่จัดการ Business Logic และเป็นตัวกลางระหว่าง Controller กับ Repository โดยมีคำสั่งหลัก ได้แก่

```text
getAllGames()
getGameById(Long id)
addGame(Game game)
updateGame(Long id, Game game)
deleteGame(Long id)
```

`GameService` ใช้ **Constructor Injection** เพื่อรับ `GameRepository` จาก Spring

```java
public GameService(GameRepository repository) {
    this.repository = repository;
}
```

### Controller

ไฟล์ `GameController.java`

ทำหน้าที่รับ HTTP Request จาก Browser และเรียกใช้ `GameService` สำหรับการทำงานต่าง ๆ เช่น แสดง เพิ่ม แก้ไข และลบข้อมูลเกม

Controller ใช้ **Constructor Injection** เพื่อรับ `GameService`

```java
public GameController(GameService gameService) {
    this.gameService = gameService;
}
```

---

## Strategy Pattern

ระบบใช้ **Strategy Pattern** สำหรับแยกวิธีการคำนวณส่วนลดแต่ละประเภทออกจากกัน

```text
DiscountStrategy
       │
       ├── NoDiscountStrategy
       ├── StudentDiscountStrategy
       └── SeasonalSaleStrategy
```

### DiscountStrategy

Interface ที่กำหนดรูปแบบมาตรฐานสำหรับการคำนวณส่วนลด

```java
public interface DiscountStrategy {
    double calculate(double price);
}
```

### NoDiscountStrategy

ใช้เมื่อเกมไม่มีส่วนลด และคืนราคาปกติ

### StudentDiscountStrategy

ใช้สำหรับส่วนลดนักเรียน

### SeasonalSaleStrategy

ใช้สำหรับส่วนลดในช่วงเทศกาล

### DiscountContext

ทำหน้าที่เลือก Strategy ที่เหมาะสมตามประเภทส่วนลด

การออกแบบลักษณะนี้ช่วยสนับสนุน **Open/Closed Principle (OCP)** เพราะสามารถเพิ่มรูปแบบส่วนลดใหม่ได้โดยสร้าง Strategy ใหม่ โดยไม่จำเป็นต้องแก้ไข Strategy เดิม

---

## Database

ระบบใช้ **PostgreSQL** เป็นฐานข้อมูล และ Entity `Game` ถูก Mapping กับตาราง

```text
lab7demo
```

ตัวอย่างโครงสร้างข้อมูลที่เกี่ยวข้อง:

```text
Game
├── id
├── title
├── genre
├── platform
├── rating
├── releaseDate
├── price
├── discountType
└── finalPrice
```

สามารถตรวจสอบข้อมูลใน PostgreSQL ผ่าน **pgAdmin 4** โดยเลือก

```text
Databases
└── Schemas
    └── public
        └── Tables
            └── lab7demo
```

หรือใช้ SQL

```sql
SELECT * FROM lab7demo;
```

---

## Application Flow

เมื่อผู้ใช้ส่ง Request จาก Browser ระบบจะทำงานตามลำดับ

```text
Browser
   ↓
GameController
   ↓
GameService
   ↓
GameRepository
   ↓
JPA / Hibernate
   ↓
PostgreSQL
```

เมื่อมีการคำนวณส่วนลด:

```text
Game
   ↓
DiscountContext
   ↓
DiscountStrategy
   ├── NoDiscountStrategy
   ├── StudentDiscountStrategy
   └── SeasonalSaleStrategy
   ↓
Final Price
```

จากนั้นข้อมูลจะถูกส่งกลับไปยัง Controller และแสดงผลผ่าน Thymeleaf

```text
PostgreSQL
   ↓
Repository
   ↓
Service
   ↓
Controller
   ↓
Thymeleaf
   ↓
Browser
```

---

## Dependency Injection

ระบบใช้ **Dependency Injection** เพื่อให้ Spring เป็นผู้จัดการ Dependency ระหว่างคลาสต่าง ๆ โดยใช้ **Constructor Injection** เป็นหลักใน Service และ Controller

```text
GameController
      │
      │ GameService
      ▼
GameService
      │
      │ GameRepository
      ▼
GameRepository
```

การออกแบบนี้ช่วยลดการสร้าง Object ด้วย `new` ภายในแต่ละคลาส และช่วยให้ระบบมี **Low Coupling** รวมถึงทำให้แต่ละคลาสมีหน้าที่เฉพาะของตัวเองหรือ **High Cohesion**

---

## Design Principles

โปรเจกต์นี้นำหลักการออกแบบซอฟต์แวร์มาประยุกต์ใช้ ได้แก่

* **Single Responsibility Principle (SRP)** — แต่ละ Layer มีหน้าที่เฉพาะ
* **Open/Closed Principle (OCP)** — Strategy Pattern ช่วยให้เพิ่มรูปแบบส่วนลดใหม่ได้
* **Dependency Inversion Principle (DIP)** — ใช้ Dependency Injection เพื่อลดการผูกติดกับการสร้าง Object โดยตรง
* **High Cohesion** — แต่ละคลาสรวมความรับผิดชอบที่เกี่ยวข้องกัน
* **Low Coupling** — ลดการพึ่งพาโดยตรงระหว่าง Layer
* **Strategy Pattern** — แยก Algorithm การคำนวณส่วนลดออกเป็นแต่ละ Strategy

---

## How to Run

### 1. Clone Project

```bash
git clone <repository-url>
cd <project-folder>
```

### 2. ตั้งค่า PostgreSQL

สร้าง Database ใน PostgreSQL และกำหนดข้อมูลการเชื่อมต่อใน

```text
src/main/resources/application.properties
```

ตัวอย่าง:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/<database-name>
spring.datasource.username=<username>
spring.datasource.password=<password>

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Run Application

ใช้ Maven:

```bash
mvn spring-boot:run
```

หรือเปิดโปรเจกต์ผ่าน IDE แล้ว Run Spring Boot Application

### 4. เปิดระบบ

เปิด Browser และเข้า

```text
http://localhost:8080/games
```

---

## Summary

โปรเจกต์นี้เป็นระบบจัดการข้อมูลเกมที่ใช้ **Spring Boot + Spring Data JPA + PostgreSQL + Thymeleaf** โดยแบ่งโครงสร้างออกเป็น Controller, Service, Repository และ Entity เพื่อแยกความรับผิดชอบของแต่ละส่วน นอกจากนี้ยังใช้ **Strategy Pattern** เพื่อจัดการการคำนวณส่วนลดหลายรูปแบบ ทำให้สามารถเพิ่มรูปแบบส่วนลดใหม่ได้ง่ายและสอดคล้องกับหลัก OCP รวมถึงใช้ Constructor Injection เพื่อจัดการ Dependency และช่วยให้ระบบมี Low Coupling และ High Cohesion
