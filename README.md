# TEVS_ABSCHLUSSABGABE_TEAM_ANDI

# Requirements
  - docker version >= 24.0.6
  - docker compose version >= v2.23.0

# How to run
  - docker compose up --build

# Set Status:

  - If the user doesn't exist, a new user will be added with the given status
  - If the user exits with the same name you are setting the status with, then the status of the existing user will be updated
  - If the status is "off", the user will be deleted
