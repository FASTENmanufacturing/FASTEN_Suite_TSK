# -*- coding: utf-8 -*-
'''
Created on 24 de jun de 2019
Author: 	1. Flavio Filho (Jun-Dec 2019)    
			2. Saymon G. Bandeira (Jun 2019-Today) 
			
Contact:    1. flaviofilho2908@gmail.com
			2. saymongb@gmail.com
Summary:    
			
Note:       
Fix:
Status:        
'''

#import sys,os,string
from flask import Flask, request
from flask_restplus import Api, Resource, reqparse, inputs
from ws.objects import *
from ws.study import ForecastStudy
from utils import ws_util as wsUt
import logging
from logging.handlers import RotatingFileHandler

# Creating an Api Flask instance
flask_app = Flask(__name__)

# Flask parameter setup
flask_app.config.setdefault('RESTPLUS_MASK_SWAGGER', False)
flask_app.config['PREFERRED_URL_SCHEME'] = 'http'

# Logging system
logging.basicConfig(filename='logs/ppat.log',
				level=logging.ERROR,
				format='%(asctime)s %(levelname)s %(name)s %(threadName)s : %(message)s')

# Creating a instance of API
app = Api(app = flask_app, 
		  version = "1.0", 
		  title = "Predictions API by FASTEN", 
		  description = "Part of Predictive Prescriptive and Analytic Tool.",
		  contact_url = "http://www.fastenmanufacturing.eu/")

# Namespace setup
name_space = app.namespace('forecast', description='Provides several methods for time series forecasting.')

# Creating instance of class which will be used
# to handle input and output data.
documentation = documentationModel(app)
modelForecastingStudy = documentation.getmodelForecastingStudy()
modelsResult = documentation.getmodelsResult()
modelStudyResults = documentation.getmodelStudyResults()
studyList = documentation.getForecastStudyList()

# Adding a route for methods calls
# This route will be used for single a forecast study
@name_space.route("/single/")
class MainClass(Resource):
    
    parser = reqparse.RequestParser()
    parser.add_argument('url', type=inputs.URL(schemes=['http']))
    
    # Type of data model returned by this method
    @app.marshal_with(modelStudyResults)
    # Response code definition
    @app.doc(responses={200: 'OK. Processing your request.',
					400: 'Invalid Argument.',
					500: 'Internal Server Error.' })
    # Type of data model for input 
    @app.expect(modelForecastingStudy)	
    
    def post(self):
        
        try:
            
            studyData = wsUt.WebServiceTools.decode(request)
            
            # Create an instance and perform a forecast study
            F = ForecastStudy(studyData['models'],
							studyData['demands'],
							studyData['horizon'],
							studyData['part'],
							studyData['remoteStation'],
							studyData['frequency'])
            
            res = F.runStudy()
            
            return res   
              
        except (ErrorJson,StudyException,TypeError,ValueError) as e:
            #wsUt.WebServiceTools.createLOG(request.json)
            flask_app.logger.error(request.json)
            name_space.abort(400, e.args[0], status = "Invalid Argument.", statusCode = "400")
            
        except Exception as e:
        	#wsUt.WebServiceTools.createLOG(request.json)
        	#flask_app.logger.error(request.text.encode('ascii','ignore'))
        	flask_app.logger.error(request.json)
            
        	name_space.abort(500, "There is an error in the application."+ 
							" Your request will be stored for analysis.",
							status = "Internal Server Error.", statusCode = "500")
 
# This route will be used to perform multiple forecast study
# integrated with tactical optimization.            
@name_space.route("/multi/")
class Multi(Resource):
	
	parser = reqparse.RequestParser()
	parser.add_argument('url', type=inputs.URL(schemes=['http']))
	
	# Type of data model returned by this method
	@app.marshal_with(studyList)
	# Response code definition
	@app.doc(responses={
		200: 'OK. Processing your request.',
		400: 'Invalid Argument.',
		500: 'Internal Server Error.' })
	# Type of data model for input
	@app.expect(studyList)	
	
	def post(self):
		
		try:
			
			forecastStudyList = wsUt.WebServiceTools.decodeForecastStudyList(request)
			res = wsUt.WebServiceTools.createResultList(forecastStudyList,request)
			return res
		
		except (TypeError,ErrorJson) as e:
			flask_app.logger.error(request.json)
			name_space.abort(400, e.args[0], status = "Invalid Argument.", statusCode = "400")
		except Exception as e:
			flask_app.logger.error(request.json)
			name_space.abort(500, e.args[0], status = "Internal Server Error.", statusCode = "500")
			