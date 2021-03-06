USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getLastNRestocks]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[getLastNRestocks] 
	(@shopName varchar(30),
	@merUID uniqueidentifier,
	@number int)
as

--get shopID
declare @shopID int
select @shopID=ShopID from Storefront where [Name]=@shopName and MerchantUID=@merUID
--get last ten orders
select top (@number) Item.[Name] as Item, so.Quantity, s.[Name] as Supplier, so.[DateTime] as [Order Time]
from (select * from StockOrder where ShopID=@shopID) as so join Item on so.ItemID = Item.ItemID
	join Supplier s on so.SupplierID = s.SupplierID
order by StockingID desc


GO
