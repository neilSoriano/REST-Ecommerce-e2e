# REST-Ecommerce-e2e

End to End Ecommerce API Test
Tested Ecommerce website API calls like creating a product, adding that product, submitting an order and deleting the product at the end. API calls were tested in Postman prior for visual representation fo data being parsed into the REST Assured code.

## Implementing POJO Classes
Using POJO classes in Java allowed for easier usability and readbility of the program. 
- LoginRequest
  - Used for Serailization of request with setting user email and password for authentication upon Login
- LoginResponse
  - Utilized Deserizalization of response to parse authentication token and user ID to be used for subsequent API calls
- Orders
  - Necessary for passing in <List> of OrderDetails 
- OrderDetails
  - Necessary for setting required "country" field during checkout and parsing order ID
  - Object should be in <List> form for Orders POJO class to accept

## Other Features
- Building RequestSpecBuilders(), holding authentication token along with base API request specs
- Using multiPart() method for passing in an image when product is added into the shop
- JsonPath objects created for quick parsing of strings
- Serialization and Deserialization
