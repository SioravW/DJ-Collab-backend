from locust import HttpUser, task, between

import logging
from locust import events
import requests

payload = {'username':'sioravw@gmail.com','password':'boe123','client_id':'djcollab-vue','grant_type':'password'}
response = requests.post(
    "http://keycloak.siora.nl/auth/realms/djcollab-realm/protocol/openid-connect/token",
    data=payload
).json()
print(response)
auth_token = response['access_token']

@events.quitting.add_listener
def _(environment, **kw):
    if environment.stats.total.fail_ratio > 0.01:
        logging.error("Test failed due to failure ratio > 1%")
        environment.process_exit_code = 1
    elif environment.stats.total.avg_response_time > 200:
        logging.error("Test failed due to average response time ratio > 200 ms")
        environment.process_exit_code = 1
    elif environment.stats.total.get_response_time_percentile(0.95) > 800:
        logging.error("Test failed due to 95th percentile response time > 800 ms")
        environment.process_exit_code = 1
    else:
        environment.process_exit_code = 0


class HelloWorldUser(HttpUser):
    wait_time = between(1, 5)

    def on_start(self):
        self.client.headers = {"Authorization": "Bearer " + auth_token}

    @task(3)
    def get_playlist(self):
        self.client.get("/user")

    @task
    def add_playlist(self):
         response = self.client.post("/user", json={"username": "user", "password": "userpass"})
         if response.status_code == 200:
            userId = response.json()['id']
            self.client.put(f"/user/{userId}", json={"externalId": userId, "username": "nieuweUsername", "password": "ookNieuw"}, name="/user/id")
            self.client.get(f"/user/{userId}", name="/user/id")
            self.client.delete(f"/user/{userId}", name="/user/id")
