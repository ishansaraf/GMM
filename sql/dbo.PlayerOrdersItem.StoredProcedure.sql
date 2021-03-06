USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[PlayerOrdersItem]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO



CREATE PROCEDURE [dbo].[PlayerOrdersItem]
	(@playerID int,
	 @merUID uniqueidentifier,
	 @playerName varchar(30) OUTPUT,
	 @toBuy int OUTPUT,
	 @itemName varchar(30) OUTPUT, 
	 @shopName varchar(30) OUTPUT)
AS
--choose a shop
DECLARE @shopID int


SELECT TOP 1 @shopID = ShopID, @shopName = [Name]
FROM Storefront
WHERE MerchantUID = @merUID and Closed = 0
ORDER BY NEWID()

--choose an item at that shop:
DECLARE @itemID int
DECLARE @quantity int
DECLARE @unitPrice int
SELECT TOP 1 @itemID = ItemID, @quantity = Quantity, @unitPrice = UnitPrice FROM ItemsByStorefront WHERE ShopID = @shopID and Discontinued = 0 ORDER BY NEWID()

--get the player's username
SELECT @playerName = UserName FROM Player WHERE PlayerID = @playerID

IF (@itemID IS NULL)
	BEGIN
		PRINT 'the shop has no items'
		RETURN 2
	END
--choose how many to buy
SET @toBuy = ceiling(10*RAND());


--get the Item's name
SELECT @itemName = [Name] From Item WHERE ItemID = @itemID

--Checks for error, taken from CSSE333 Lab 7 - Stored Procedures
DECLARE @Status SMALLINT
SET @Status = @@ERROR
IF @Status != 0
	BEGIN
		PRINT 'An Error occurred during the addition of the storefront. The error code is: ' +CONVERT(varchar(30), @Status)
		RETURN(@Status)
	END

--return an error code with the info if the shop doesn't have enough stock
IF @toBuy > @quantity
	BEGIN
		PRINT 'player wanted to buy more than are in stock'
		INSERT INTO BuyOrder(UnitPrice, Quantity, DateTime, PlayerID, ShopID, ItemID, Completed)
		VALUES (@unitPrice, @toBuy, GETDATE(), @playerID, @shopID, @itemID, 0)
		RETURN 1;
	END
ELSE
	BEGIN
		RETURN 0;
	END





GO
