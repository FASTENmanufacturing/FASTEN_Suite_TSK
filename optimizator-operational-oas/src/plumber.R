library(plumber)

# Load static data_base
#* @apiTitle Allocate Production Order

#* allocate production order
#* @param part The type of spare part (ex.: Button, Dosing chamber, Grid Air Conditioning, Support for escalator, Home Lift frame)
#* @param quantity The quantity to be allocate
#* @param latitude The delivery latitude location
#* @param longitude The delivery longitude location
#* @param orderID Production Order Indentification
#* @serializer unboxedJSON
#* @post /allocate

function(latitude, longitude, orderID, part, quantity){

  allocate(part      = part,
           quantity  = quantity,
           latitude  = latitude,
           longitude = longitude,
           orderID   = orderID)

}
