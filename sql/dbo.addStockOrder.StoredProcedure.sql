USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[addStockOrder]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[addStockOrder]
	(@shopName varchar(30),
	@supplierName varchar(30),
	@itemName varchar(30),
	@quantity int,
	@merUID uniqueidentifier)
as

declare @shopID int
declare @bv decimal(18, 4)
declare @supplierID int
declare @itemID int
declare @serverID int
declare @funds decimal(18, 4)
declare @discount decimal(18, 4)

set @itemID = (select ItemID from Item where [Name]=@itemName)
select @shopID=ShopID, @serverID=ServerID from Storefront where [Name]=@shopName and MerchantUID=@merUID
select @supplierID = SupplierID, @discount = Discount from Supplier where [Name]=@supplierName and ServerID=@serverID
set @bv = (select BaseValue from Item where ItemID = @itemID)
set @funds = (select Funds from Storefront where ShopID = @shopID)
--if either of item, shop, or supplier is invalid, print error and return 1
if (@itemID is NULL or @shopID is NULL or @supplierID is NULL)
begin
	print 'Invalid inputs.'
	return 1
end
--if not enough funds in store, print error and return 2
if ((@funds - (1 - @discount) * @bv * @quantity) < 0)
begin
	print 'Insufficient funds.'
	return 2
end
--else insert into StockOrder table
--if item not yet in shop, insert into ItemsByStorefront table
declare @check bit
exec isItemInShop @itemID, @shopID, @check output
if @check = 0
begin
	insert into ItemsByStorefront (ShopID, ItemID, Quantity, UnitPrice)
	values(@shopID, @itemID, 0, @bv * 1.1)
end
insert into StockOrder (ItemID, Quantity, DateTime, ShopID, SupplierID)
values(@itemID, @quantity, GETDATE(), @shopID, @supplierID)
return 0
GO
