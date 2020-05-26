source("library.R")
source("allocate.R")

DATABASE_OAS_URL <- Sys.getenv("DATABASE_OAS_URL")
ORS_API_KEY <- Sys.getenv("ORS_API_KEY")

API <- plumb("plumber.R")
API$run(host = '0.0.0.0', 
        port = 1026,
        swagger = T)
