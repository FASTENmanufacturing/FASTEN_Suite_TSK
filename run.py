#!/usr/bin/env python
"""This script spins up the full fasten WP4 core infrastructure.
   'docker-compose up' is not sufficient because of missing hooks
   for initialisation after booting."""
import sys
import time
from subprocess import call


print("Starting WP4 core ...")

print("- shutting down existing WP4 services ...\n")
call(["docker-compose", "down"])
time.sleep(2)

print("Starting Grafana...")
call(["docker-compose", "up", "-d", "grafana"])

print("Starting PostgreSQL...")
call(["docker-compose", "up", "-d", "postgresdb"])

#print("Starting pgAdmin...")
#call(["docker-compose", "up", "-d", "pgadmin"])

print("Starting database-oas...")
call(["docker-compose", "up", "-d", "database-oas"])

print("Starting email-oas...")
call(["docker-compose", "up", "-d", "email-oas"])

print("Starting iot-kafka-oas...")
call(["docker-compose", "up", "-d", "iot-kafka-oas"])

print("Starting iot-fiware-oas...")
call(["docker-compose", "up", "-d", "iot-fiware-oas"])

print("Starting fpsot-oas...")
call(["docker-compose", "up", "-d", "fpsot-oas"])

print("Starting optimizator-operational...")
call(["docker-compose", "up", "-d", "optimizator-operational"])

print("Starting probability-distribution...")
call(["docker-compose", "up", "-d", "probability-distribution"])

print("Starting ppa...")
call(["docker-compose", "up", "-d", "ppa"])

print("Starting fpsot-frontend...")
call(["docker-compose", "up", "-d", "fpsot-frontend"])

print("")
print("--------------------------------")
print(" WP4 core started successfully.")
print("--------------------------------")
print("")
