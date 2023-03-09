### Description
Scooters online-shop with frontend on Typescript + ReactJS and backend on Java Spring Framework 3.

This project was created by [Art3m1y](https://github.com/Art3m1y) and [ShyHarvey](https://github.com/ShyHarvey).
### Implemented features:
+ JWT authorization 
+ Registration
+ Addition of new products with admin panel
+ Deleting and editing of existing products with admin panel
+ Product rating + reviews on products
+ Search products by name
+ Shopping cart, that is synchronized with the server and linked to the account
+ Local favorites
+ Light and dark themes
+ Vercel analytics
### Technologies
__Frontend__:
+ TypeScript
+ ReactJS 18
+ Redux Toolkit
+ Material UI
+ React - hook - form
+ Zod
+ React Router v6
+ Lazy Loading (React.lazy() and Suspense)
+ Axios + Fetch
+ React Hooks

__Backend__:
+ Java
+ Spring framework 3
+ Hibernate
+ PostgreSQL
+ JUnit 5
+ JWT
### Start application
+ Clone repository from Github:
```https://github.com/Art3m1y/scooters-online-store.git```
+ For run frontend part of project:
    + Setup Node Package Manager
    + Download all dependencies for project (write this command only from frontend path)
    ```npm install```
    + Run frontend part of project (write this command only from frontend path)
    ```npm start```
+ For run backend part of project     :
    + Setup Java Development Kit
    + Compile and package the application to an executable JAR
    ```mvn package```
    + Run backend part of project (executable JAR)
    ```java -jar scooters-shop-project.jar```

 _If you have followed all the steps, the frontend will be available at http://<host>:3000 and the backend will be available at http://<host>:8080_
### Backend endpoints
All endpoints and description to this endpoints,  you can find by starting the project and opening the /docs endpoint (this is an endpoint, that generated by swagger).
### SQL statements script for correct project work
You can find SQL statements script for correct project work on this path: ./backend/src/main/resources/script.sql
    
    
