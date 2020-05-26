getTimeTravel <- function(longitude, latitude, api_key){
  
  srams <- 
    GET(paste0(DATABASE_OAS_URL, "/SRAM"),
        encode = 'json') %>% 
    content %>% 
    toJSON %>% 
    fromJSON
  
  srams <- flatten(srams)
  
  srams <- 
    filter(srams, status == 'Online',
           processStatus == 'In Operation' | processStatus == 'Standby')
  
  srams <<- srams
  
  srams <- 
    data.frame(SRAM = unlist(srams$code),
               remoteStations = unlist(srams$remoteStation.name),
               latitude = unlist(srams$remoteStation.address.latitude),
               longitude = unlist(srams$remoteStation.address.longitude),
               
               stringsAsFactors = FALSE)
  
  locations <- vector("list", nrow(srams))
  
  for (n in 1:nrow(srams)){
    
    locations[[n]] <- c(srams$longitude[[n]], srams$latitude[[n]])
    
  }
  
  y <- c(longitude, latitude)
  
  locations <- c(list(y), locations)
  
  # API
  
  matrix <- 
    POST("https://api.openrouteservice.org/v2/matrix/driving-car",
         add_headers(Authorization = api_key),
         body = list(locations = locations,
                     destinations = list(0)),
         encode = "json") %>% 
    content %>% 
    toJSON %>% 
    fromJSON
  
  return(
    
    # Results
    
    data.frame(

      SRAM = c('origin', unlist(srams[['SRAM']])),
      timeTravel = matrix$durations %>% as.character() %>% as.numeric(),
      stringsAsFactors = F
      
    )[-1,]
    
  )
  
}
