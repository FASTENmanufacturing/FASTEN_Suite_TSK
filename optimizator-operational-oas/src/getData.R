# Travel time

source("travelTime.R")
api_key <- ORS_API_KEY

timeTravel <- getTimeTravel(longitude, latitude, api_key)

# Production time

timeProduction <-
  GET(paste0(DATABASE_OAS_URL, "/processingPart"),
      encode = 'json') %>%
  content %>%
  toJSON %>%
  fromJSON

timeProduction <- flatten(timeProduction) %>% 
  filter(part.name == part)

timeProduction <- data.frame(SRAM = timeProduction$sram.code %>% unlist,
                             part = timeProduction$part.name %>% unlist,
                             time = timeProduction$averagePrintTime %>% unlist,
                             
                             stringsAsFactors = FALSE)

timeProd <- c()

for (i in 1:nrow(timeTravel)){
  
  timeProd <- c(timeProd, filter(timeProduction, SRAM == timeTravel[i,1])[[3]])
  
}; rm(i)



# Queue time

timeLeft <- c()

for (l in 1:nrow(srams)){
  
  if (srams$printTimeLeft[[l]] %>% unlist %>% is.null() == TRUE) {
    
    timeLeft <- c(timeLeft, 0)
    
  } else {
    
    timeLeft <- c(timeLeft, srams$printTimeLeft[[l]] %>% unlist)
    
  }
}; rm(l)
