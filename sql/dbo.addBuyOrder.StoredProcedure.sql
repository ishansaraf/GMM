USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[addBuyOrder]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO





CREATE PROCEDURE [dbo].[addBuyOrder]
	(@player varchar(30),
	 @quantity int,
	 @item varchar(30),
	 @shop varchar(30),
	 @merUID uniqueidentifier)
AS

--get the correct values
DECLARE @playerID int
DECLARE @itemID int
declare @disc bit
declare @closed bit
DECLARE @shopID int
DECLARE @funds decimal(18,4)
DECLARE @unitPrice decimal(18,4)


SELECT @playerID = PlayerID FROM Player WHERE UserName = @player
SELECT @itemID = ItemID FROM Item WHERE [Name] = @item
SELECT @shopID = ShopID, @funds = Funds, @closed = Closed FROM Storefront WHERE [Name] = @shop and MerchantUID = @merUID
select @unitPrice = UnitPrice from ItemsByStorefront where ShopID = @shopID and ItemID = @itemID
select @disc = Discontinued from ItemsByStorefront where ItemID = @itemID and ShopID = @shopID

IF @playerID IS NULL
	BEGIN
		PRINT 'the player name parsed is invalid'
		RETURN 1;
	END
IF @itemID IS NULL
	BEGIN
		PRINT 'the item name parsed is invalid'
		RETURN 2;
	END
IF @shopID IS NULL
	BEGIN
		PRINT 'the storefront name parsed is invalid'
		RETURN 3;
	END
IF @unitPrice IS NULL
	BEGIN
		PRINT 'the unit price of the item name parsed could not be calculated'
		RETURN 4;
	END
--check if item is discontinued from store
if @disc = 1
begin
	print 'cannot purchase discontinued items'
	return 5
end
--check if shop is closed
if @closed = 1
begin
	print 'cannot purchase from closed shops'
	return 6
end
--check if enough quantity in store
declare @itemQ int
set @itemQ = (select Quantity from ItemsByStorefront where ShopID=@shopID and ItemID=@itemID)
if @itemQ < @quantity
begin
	print 'not enough item in store for order'
	return 7
end

--everything is valid so insert
INSERT INTO BuyOrder(UnitPrice, Quantity, DateTime, PlayerID, ShopID, ItemID)
VALUES (@unitPrice, @quantity, GETDATE(), @playerID, @shopID, @itemID)

--Checks for error, taken from CSSE333 Lab 7 - Stored Procedures
DECLARE @Status SMALLINT
SET @Status = @@ERROR
IF @Status != 0
	BEGIN
		PRINT 'An Error occurred during the addition of the storefront. The error code is: ' +CONVERT(varchar(30), @Status)
		RETURN(@Status)
	END

--Insert successful, return 0
RETURN 0

GO
