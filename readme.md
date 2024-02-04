# DENTAL MECHANIC

#### This is a web application designed for dental technicians to record and manage their work. The application allows users to store work records in a database, sort them by month, calculate income, and save the records to an .xlsx files.
This is my first education project. Cause my job is a dental technician, I decided to make a service for the work accounting for my job.

## Features

- Record and store works in a database
- Sort works by month for easy management and analysis
- Calculate income based on the recorded works
- Save work records to an xls file for further analysis or reporting

## Tech

The application is developed using the following technologies:

- Java 18
- Maven
- MySQL database
- Tomcat server
- Jakarta Servlet 6 libraries

## Project Structure

##### The project is divided into two modules: frontend and backend.
###### Frontend Module
The frontend module follows the MVC (Model-View-Controller) architectural pattern. It utilizes JSP (JavaServer Pages) for displaying content and servlets for handling logic processing.
###### Backend Module
The backend module consists of two sub-modules: core and webapp.
- Core Module
  The core module contains the main logic of the application. It handles the recording and storage of works, sorting works by month, and calculating income.
- Webapp Module
  The webapp module serves as the REST API for the application. It handles authentication using JWT (JSON Web Tokens) and provides servlets for interacting with the frontend module.

## Installation

To run the application, follow these steps:
- Ensure you have Java 18 installed on your system.
- Set up a MySQL database and configure the connection details in the "mysql.properties" file located in the [backend/core/src/main/resources] directory.
- Execute in MySQL file "mysql_tables.sql".
- Deploy the backend module on a Tomcat server.
- Configure the Tomcat server to use the appropriate version of Jakarta Servlet (version 6).
- Deploy the frontend module on the same Tomcat server.
- Access the application through the specified URL.

## Usage

Once the application is up and running, you can use it to record and manage your dental works. Here are some key functionalities:
- Record new works by providing details such as patient information, clinic, type of product, quantity, and optional photo(not implemented yet).
- View and sort works by month to easily track and manage your records.
- Calculate income based on the recorded works.
- Generate and save monthly reports in xls format for further analysis or reporting.

## License

**Author: Stanislav Kuprienko**
**Location: Omsk, Siberia, Russia**