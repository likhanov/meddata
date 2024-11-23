## Med Data service

### How to run
```bash
mvn spring-boot:run
```
### How to test
1. Upload the data file
```bash
curl --request POST \
   --url http://localhost:8080/health-condition/upload \
   --header 'content-type: multipart/form-data' \
   --form file=@./exercise.csv
```
Expected result:
```bash
CSV data successfully loaded.
```
1. Fetch all data
```bash
curl --request GET \
  --url http://localhost:8080/health-condition
```
Expected result: page with data
1. Fetch by code
```bash
curl --request GET \
  --url http://localhost:8080/health-condition/code/271636001
```
Expected result: data rows with code 271636001
1. Delete all data
```bash
curl --request DELETE \
  --url http://localhost:8080/health-condition
```
Expected result: all data should be deleted

