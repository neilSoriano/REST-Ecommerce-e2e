package files;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetails;
import pojo.Orders;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;


public class ECommerceAPITest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// SSL Certification can be bypassed using:
//		given().relaxedHTTPSValidation()
		
		// Login Call 
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.setContentType(ContentType.JSON).build();
		
		// Serialization
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("neilsoriano.ns27@gmail.com");
		loginRequest.setUserPassword("$hettY123");
		
		RequestSpecification reqLogin = given().log().all().spec(req).body(loginRequest);
		
		LoginResponse loginResponse = reqLogin.when().post("/api/ecom/auth/login")
		.then().log().all().extract().response().as(LoginResponse.class);
		
//		System.out.println(loginResponse.getToken());
//		System.out.println(loginResponse.getUserId());
		
		// Deserialization
		String token = loginResponse.getToken();
		String userId = loginResponse.getUserId();
		
		// Add Product Call
		RequestSpecification addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).build();
		
		// Use param() to add form-data from payload
		// Use multipart(key, new File(file_path)) when sending attachments 
		RequestSpecification reqAddProduct = given().log().all().spec(addProductBaseReq).param("productName", "Wave Runner")
		.param("productAddedBy", userId).param("productCategory", "fashion")
		.param("productSubCategory", "shirts").param("productPrice", "11500")
		.param("productDescription", "Adidas Yeezy").param("productFor", "men")
		.multiPart("productImage", new File("/Users/neilsoriano/Downloads/adidas-yeezy-boost-700-v1-wave-runner-b75571_1.jpg"));
		
		String addProductResponse = reqAddProduct.when().post("/api/ecom/product/add-product")
		.then().log().all().extract().response().asString();
		
		JsonPath js = new JsonPath(addProductResponse);
		String productId = js.get("productId");
		
		
		// Create Order
		RequestSpecification createOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).setContentType(ContentType.JSON).build();
		
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setCountry("United States");
		orderDetails.setProductOrderedId(productId);
		
		List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails> ();
		orderDetailsList.add(orderDetails);
		
		Orders orders = new Orders();
		orders.setOrders(orderDetailsList);
		
		RequestSpecification createOrderReq = given().log().all().spec(createOrderBaseReq).body(orders);
		
		String orderResponse = createOrderReq.when().post("/api/ecom/order/create-order")
		.then().log().all().extract().response().asString();
		
		System.out.println(orderResponse);
		
		
		// Delete Product
		RequestSpecification deleteProdBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).setContentType(ContentType.JSON).build();	
		
		RequestSpecification deleteProdReq = given().log().all().spec(deleteProdBaseReq).pathParam("productId", productId);
		
		String deleteProdResponse = deleteProdReq.when().delete("api/ecom/product/delete-product/{productId}")
		.then().log().all().extract().response().asString();
		
		JsonPath js1 = new JsonPath(deleteProdResponse);
		
		Assert.assertEquals("Product Deleted Successfully", js1.get("message"));
		
		
		
		
		
		
		
		
		
	}

}
