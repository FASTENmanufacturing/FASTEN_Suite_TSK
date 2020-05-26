library('httr')
library('dplyr')
library('jsonlite')

local <- 'http://127.0.0.1:1026/allocate'
aws <- 'http://ec2-54-233-213-104.sa-east-1.compute.amazonaws.com:8001/allocate'

POST(url = local, encode = 'json',
	body = list(origin = 'Curitiba',
		    quantity = 1,
		    part = 'Button')
	) %>%
	content %>%
	toJSON %>%
	fromJSON %>%
	print

# curl -X POST "http://ec2-54-233-213-104.sa-east-1.compute.amazonaws.com:8001/allocate?origin=Curitiba&quantity=1&part=Button" -H  "accept: application/json"
