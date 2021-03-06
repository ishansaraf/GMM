USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getShopItems]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[getShopItems] 
	(@shopName varchar(30),
	@merUID uniqueidentifier)
as

--get shopID
declare @shopID int
select @shopID=ShopID from Storefront where [Name]=@shopName and MerchantUID=@merUID
--get items in shop
select [Name] as Item, Quantity, UnitPrice as [Unit Price], [Description], [Discontinued]
from (select * from ItemsByStorefront where ShopID=@shopID) as i join Item t on i.ItemID=t.ItemID
where ShopID=@shopID
order by Discontinued asc, [Name] asc


GO
