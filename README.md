# DiscountCodeMicroService
Discount Code Micro Service

Web URL : http://localhost:8080
H2 Memory Database URL : http://localhost:8080/h2
Swagger UI URL : http://localhost:8080/swagger-ui.html

Java Documentation : java docs are created in target/apidocs folder and also bundled as DiscountCodeMicroService-1.0-SNAPSHOT-javadoc.jar

Test case coverage:
-------------------
DiscountControllerDataSetupTest : Helps to setup the Test data for creating Special Offers, Recipients and Positive Tests for generating the Discount codes for all recipients with available Special offers created.
DiscountControllerTest : Helps to cover all positive and negative tests by validating the Response codes and also redeems the discount code. Delete tests are also covered for deleting recipients and Special offers.

Validate Discount Code Negative Test Scenarios:
--------------------------------------
1.) validateAndGetDiscountCode_already_mapped() : Test scenario that validates the recipient by mapping again the same offer. 
Response status returned is 
{"status":"ERROR","errorCode":"DMSERR103","errorMessage":"Recipient is already mapped to this Special Offer"}

2.) validateAndGetDiscountCode_null_email() : Test scenario that validates the null recipient by mapping offer. 
Response status returned is 
{"status":"ERROR","errorCode":"DMSERR108","errorMessage":"Input Request Invalid"}

3.) validateAndGetDiscountCode_null_offer() : Test scenario that validates the valid recipient by null mapping offer. 
Response status returned is 
{"status":"ERROR","errorCode":"DMSERR108","errorMessage":"Input Request Invalid"}

4.) validateAndGetDiscountCode_null_request() : Test scenario that validates the null recipient and null mapping offer. 
Response status returned is 
{"status":"ERROR","errorCode":"DMSERR108","errorMessage":"Input Request Invalid"}

5.) validateAndGetDiscountCode_no_recipient() : Test scenario that validates the invalid recipient and valid mapping offer. 
Response status returned is 
{"status":"ERROR","errorCode":"DMSERR101","errorMessage":"Recipient does not exist for the provided Email"}

6.) validateAndGetDiscountCode_no_offer() : Test scenario that validates the valid recipient and invalid mapping offer. 
Response status returned is 
{"status":"ERROR","errorCode":"DMSERR102","errorMessage":"Special Offer does not exist for provided Special Offer name"}


Redeem Discount Code Test Scenarios:
------------------------------------
1.) b_redeemDiscountCode() : Test scenario that validates the Valid recipient and the valid generated Discount code. 
Response status returned is 
{"responseStatus":{"status":"SUCCESS","errorCode":null,"errorMessage":null},"discountPercent":10.0}

2.) d_redeemDiscountCode_invalid_discount_code() : Test scenario that validates for the invalid Discount code. 
Response status returned is 
{"responseStatus":{"ERROR","errorCode":"DMSERR105","errorMessage":"Discount Code is not mapped to this User"},"discountPercent":0.0}

3.) e_redeemDiscountCode_invalid_email() : Test scenario that validates the invalid recipient. 
Response status returned is 
{"responseStatus":{"ERROR","errorCode":"DMSERR101","errorMessage":"Recipient does not exist for the provided Email"},"discountPercent":0.0}

4.) c_redeemDiscountCode_already_used() : Test scenario that validates the invalid recipient. 
Response status returned is 
{"responseStatus":{"ERROR","errorCode":"DMSERR104","errorMessage":"Discount Code is already been used"},"discountPercent":0.0}

5.) f_redeemDiscountCode_expired_discount_code() : Test scenario that validates the valid recipient with an expired discount code
Response status returned is 
{"responseStatus":{"ERROR","errorCode":"DMSERR106","errorMessage":"Discount Code is Expired"},"discountPercent":0.0}




