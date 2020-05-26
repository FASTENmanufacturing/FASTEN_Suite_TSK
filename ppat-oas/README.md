# Predictive Prescriptive and Analytic Tool by FASTEN

This directory contains all Python code of the Predictive Prescriptive and Analytic Tool (PPAT) 
developed during the **FASTEN** project for ThyssenKrupp use case. 
A brief description of each package is provided below. 
The application is initialized passing the "start.py" file in the root directory (/ppat) to the Python interpreter.

- The [ws](./ws) package

The ws package refers to Web Service and is the entry point of the application. It contains modules to handle HTTP requests, to manage the forecast models, and to define the input and output entities for the tool.

Main modules used: [Flask](https://palletsprojects.com/p/flask/), 
[Statsmodels](https://www.statsmodels.org/stable/index.html), and 
[Keras](https://keras.io/).

- The [utils](./utils) package

Auxiliary functions to support the operations performed by the application. For example input and output data processing, error metrics definitions, and others.

Main modules used: [Pandas](https://pandas.pydata.org/) and 
[NumPy](https://numpy.org/).

- The [ml_forecast](./ml_forecast) package

This package contains the modules with the Machine Learning (ML) models available in the application. The approaches include ANN variations and ensemble systems.

Main modules used: [Keras](https://keras.io/) and [ELM](https://elm.readthedocs.io/en/latest/).

- The [intermittent](./intermittent) package

Includes a standard approach to deal with intermittent time series (Croston's method) and a "naive" parameter optimization method. It was built using [Statsmodels](https://www.statsmodels.org/stable/index.html) tools.

- The [logs](./logs) folder

It contains the log with the purpose of debugging the application. Each request which generates an error is stored in the file named "ppat.log".

- The [dataset](./dataset) folder

It contains the files in Microsoft Excel format sent by ThyssenKrupp colleagues and other data used to test the application.

- The [docs](./docs) folder

Other files used to document/deploy the application. Files as UML diagrams, The Dockerfile, and Python requirements are available in this folder.

The other folders contain temporary files/data and could be removed on the next commits.