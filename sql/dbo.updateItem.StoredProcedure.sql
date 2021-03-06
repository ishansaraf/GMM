USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[updateItem]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[updateItem]
	(@item varchar(30),
	@shop varchar(30),
	@merUID uniqueidentifier,
	@price decimal(18, 4) = NULL,
	@disc bit = NULL)
as

if @price < 0
return 2

declare @itemID int
declare @shopID int
set @itemID = (select ItemID from Item where [Name] = @item)
set @shopID = (select ShopID from Storefront where [Name] = @shop and MerchantUID = @merUID)
--check if input is valid
if @itemID is NULL or @shopID is NULL
begin
	print 'invalid item or shop name'
	return 1
end
--update for values specified
if @price is not NULL
begin
	update ItemsByStorefront
	set UnitPrice = @price
	where ShopID = @shopID and ItemID = @itemID
	return 0
end

if @disc is not NULL
begin
	update ItemsByStorefront
	set Discontinued = @disc
	where ShopID = @shopID and ItemID = @itemID
	return 0
end
GO
