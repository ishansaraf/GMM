USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getLastNOrders]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[getLastNOrders] 
	(@shopName varchar(30),
	@merUID uniqueidentifier,
	@number int)
as

--get shopID
declare @shopID int
select @shopID=ShopID from Storefront where [Name]=@shopName and MerchantUID=@merUID
--get last ten orders
select top (@number) Item.[Name] as Item, bo.Quantity, p.UserName as Player, bo.DateTime as [Order Time]
from (select * from BuyOrder where ShopID=@shopID) as bo join Item on bo.ItemID = Item.ItemID
	join Player p on bo.PlayerID = p.PlayerID
order by OrderID desc


GO
