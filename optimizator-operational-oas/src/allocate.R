allocate <- function(latitude, longitude, orderID, part, quantity){
  
  time.ini <- Sys.time()
  
  # Log
  input <- paste('ID:', orderID, 'part:', quantity, part, 'local:', latitude, longitude)
  logN <- format(Sys.time(), '_d[%d_%m_%Y]t[%H_%M_%S]')
  file <- paste0('logs/', orderID, logN, '.log')
  write(input, file = file)
  sink(file = file, append = T, split = T)
  
  latitude  <<- latitude  %>% as.numeric()
  longitude <<- longitude %>% as.numeric()
  part      <<- part
  
  suppressWarnings(source("getData.R"))
  
  QT <- timeLeft
  TT <- timeTravel
  PT <- timeProd/60
  
  result <- 
    cbind(TT[1],
          queueTime = QT,
          timeProduction = PT,
          (TT[2]/60) + 60,
          totalTime = QT + PT + ((TT[[2]]/60) + 60)) %>% 
    as_tibble %>% arrange(totalTime)
  
  result <- filter(result, !is.na(totalTime))
  
  result <- 
    list(orderID = orderID,
         optimizationResult = result[1:5,])
  
  print(result)
  
  print(paste0('Tempo de execucao: ', Sys.time() - time.ini))
  
  sink()
  return(result)
  
}

#allocate(part = "Button",
#         quantity = 3,
#         longitude = -51.1795,
#         latitude = -30.0991,
#         orderID = "2ad1ee0d-ee75-4dab-a27c-e356be6776ee")
