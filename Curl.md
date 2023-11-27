curl --location 'http://localhost/topjava/rest/meals/100004'

curl --location --request DELETE 'http://localhost/topjava/rest/meals/100004'

curl --location 'http://localhost/topjava/rest/meals'

curl --location --request PUT 'http://localhost/topjava/rest/meals/100009' \
--header 'Content-Type: application/json' \
--data '{
"id": 100009,
"dateTime": "2020-01-31T20:00:00",
"description": "Ужин",
"calories": 1010
}'

curl --location 'http://localhost/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--data '{
"dateTime": "2020-03-15T20:00:00",
"description": "Ужин",
"calories": 1010
}'

curl
--location 'http://localhost/topjava/rest/meals/filter?startDate=2018-05-10&startTime=11%3A22&endDate=2020-01-30&endTime=23%3A22'

