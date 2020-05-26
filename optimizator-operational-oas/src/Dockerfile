FROM rocker/r-ver:3.6.3

RUN apt-get update -qq && apt-get install -y \
	libssl-dev \
	libcurl4-gnutls-dev \
	libxml2-dev

RUN R -e "install.packages(c('plumber', 'dplyr', 'httr', 'jsonlite'))"

RUN mkdir -p app && mkdir -p app/logs

COPY callAPI.R plumber.R library.R allocate.R getData.R travelTime.R /app/

EXPOSE 1026

WORKDIR /app

ENTRYPOINT ["R", "-e", "source('callAPI.R')"]
