import requests

url = 'http://localhost:8083/forecast/logs/'
headers = {'Content-type': 'application/text', 'Accept': 'text/plain'}
password = "#Fasten2019!"

req = requests.get(url,headers=headers,params=password)

print(req.url)