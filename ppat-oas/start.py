# -*- coding: utf-8 -*-

import os
from waitress import serve
from ws import appPredictions

def startServer():
    serve(appPredictions.flask_app, host='0.0.0.0', port=8083)

# For the log and the relative imports
abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)

# Initialize application
startServer()