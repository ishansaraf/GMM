USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getShopStats]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[getShopStats]
	(@shopName varchar(30),
	@merUID uniqueidentifier)
as

declare @shopID int
select @shopID = ShopID from Storefront where [Name] = @shopName and MerchantUID = @merUID

if @shopID is NULL
begin
	print 'Cannot find the shop specified.'
	return 1
end

select [Name], s.QSold, t.STotal, u.QNotSold
from (select ItemID from ItemsByStorefront where ShopID = @shopID) as l left join
	(select * from QuantitySold where ShopID = @shopID) as s on l.ItemID = s.ItemID left join 
	(select * from QuantityUnfulfilled where ShopID = @shopID) as u on l.ItemID = u.ItemID left join
	(select ItemID, [Name] from Item) as i on l.ItemID = i.ItemID left join
	(select * from QuantityStocked where ShopID = @shopID) as t on l.ItemID = t.ItemID
return 0

GO
