Cafeteria Management Web App with Rest API, MYSQL and SpringBoot Back End.



/// SERVER PANEL

/api
	/auth    --   authenticates with pin.    
	/products   -- returns a list of all the category-products
	/order	-- creates a new order
	/past-orders	-- list of server's past orders
	/receipt -- generates a receipt and prompts a print window
	/metrics -- returns balance of server for specific day

 ![image](https://github.com/user-attachments/assets/0da211ff-787c-4fa9-9d08-57ce7a5a6432)



/// ADMIN PANEL 


/api/register  -- creates a new staff member.

/api/admin

	/category    PUT,POST   --   Create/Update a category
	/product     PUT,POST	--   Create/Update a product
	/server-orders  -- returns a list of orders for a specific server
	/staff -- returns a list of all staff members
	/analytics - gets a list full of information about the cafeterias monthly analytics.
	/servers-balance - returns a list of all the server's balances with specific dates.
	admin/order - returns a list of all the past orders by daily sort.			


 ![image](https://github.com/user-attachments/assets/2af94233-c498-4e44-b301-4d236ec488cc)



