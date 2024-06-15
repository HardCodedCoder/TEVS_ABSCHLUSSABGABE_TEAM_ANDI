# TEVS_ABSCHLUSSABGABE_TEAM_ANDI

# Requirements
  - docker version >= 24.0.6
  - docker compose version >= v2.23.0

# How to run
  - docker compose up --build
  
  - open "http://localhost:8082" in your browser (Client Application)
  - Set or Get status of users
  
  - Server 1 is running on "http://localhost:8081"
  - Server 2 is running on "http://localhost:8083"
  - Service Registry (Eureka) is running on "http://localhost:8761"
  - API Gateway is running on "http://localhost:8080"
  
# Set Status:

  - If the user doesn't exist, a new user will be added with the given status
  - If the user exits with the same name you are setting the status with, then the status of the existing user will be updated
  - If the status is "off", the user will be deleted
