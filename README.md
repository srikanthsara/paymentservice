# paymentservice
paymentservice


### Start Kafka
### Create Topics as menioned in below

docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic payment-success-topic
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic payment-failed-topic
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic order-created-topic

docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create --topic order-created-topic
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create --topic payment-success-topic
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create --topic payment-failed-topic
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list


### Go to http://localhost:8763/cartservice/swagger-ui/index.html#/cart-controller/addToCart

POST /cart/add

{
		"customerId": "CUST1004",
		"customerName": "Srikanth",
		"productId": "GROC1004",
		"productName": "India Gate Basmati Rice 25KG",
		"category": "Grocery",
		"brand": "India Gate",
		"quantity": 2,
		"unitPrice": 2450
}

Response data 

{
	  "cartId": 5,
	  "customerId": "CUST1004",
	  "customerName": "Srikanth",
	  "totalAmount": 4900,
	  "totalItems": 1,
	  "createdAt": "2026-05-26T23:11:01.6669106",
	  "cartItems": [
		{
		  "cartItemId": 9,
		  "productId": "GROC1004",
		  "productName": "India Gate Basmati Rice 25KG",
		  "category": "Grocery",
		  "brand": "India Gate",
		  "quantity": 2,
		  "unitPrice": 2450,
		  "totalPrice": 4900
		}
	  ]
}


### Go to Order service : http://localhost:8764/orderservice/swagger-ui/index.html#/order-controller/checkout

#### Request Data
{
	  "customerId": "CUST1004",
	  "customerName": "Srikanth",
	  "customerEmail": "srikanth@gmail.com"
}


###Response Data:

{
	  "orderId": 14,
	  "customerId": "CUST1004",
	  "customerName": "Srikanth",
	  "customerEmail": "srikanth@gmail.com",
	  "subTotal": 4900,
	  "gstAmount": 882,
	  "shippingCharges": 100,
	  "discountAmount": 200,
	  "totalAmount": 5682,
	  "paymentStatus": "PENDING",
	  "inventoryStatus": "PENDING",
	  "orderStatus": "ORDER_CREATED",
	  "createdAt": "2026-05-26T23:11:50.1607087",
	  "orderItems": [
		{
		  "orderItemId": 12,
		  "productId": "GROC1004",
		  "productName": "India Gate Basmati Rice 25KG",
		  "quantity": 2,
		  "unitPrice": 2450,
		  "totalPrice": 4900
		}
	  ]
}


###Go to PaymentServices http://localhost:8765/paymentservice/swagger-ui/index.html
## see logs "Payment processed successfully",..so payment services succesful

>psql -U postgres -p 5433
>\c payment_db
payment_db=# SELECT * FROM payment_transaction;
 amount |         created_at         | order_id | payment_id | customer_id | failure_reason | payment_provider | payment_status | payment_type |            transaction_id
--------+----------------------------+----------+------------+-------------+----------------+------------------+----------------+--------------+--------------------------------------
   5682 | 2026-05-26 22:12:25.225889 |       11 |          1 | CUST1001    |                | GooglePay        | SUCCESS        | UPI          | 609fc73e-33af-4385-ac38-8c52019d9d73
   5682 | 2026-05-26 22:12:25.432245 |       12 |          2 | CUST1002    |                | GooglePay        | SUCCESS        | UPI          | f6901873-91b4-40dc-b878-eadf19666fa3
   5682 | 2026-05-26 22:58:35.648241 |       13 |          3 | CUST1003    |                | GooglePay        | SUCCESS        | UPI          | cc0f52b9-f24b-4968-939d-0d5ce5eca310
(3 rows)


\c order_db

SELECT order_id,payment_status,order_status FROM order_master WHERE order_id = 22;


docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic payment-success-topic 	--from-beginning

###Sonar scan###
mvn sonar:sonar "-Dsonar.host.url=http://localhost:9000" "-Dsonar.token=squ_ab420683f01090fa92c6c618ee78bd57a91e0ce7"