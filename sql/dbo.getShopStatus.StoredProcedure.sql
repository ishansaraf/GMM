USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getShopStatus]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[getShopStatus]
	(@shopName varchar(30),
	@merUID uniqueidentifier,
	@closed bit output)
as

declare @shopID int
select @shopID = ShopID from Storefront where [Name] = @shopName and MerchantUID = @merUID
--check if shop exists
if @shopID is NULL
begin
	print 'the shop does not exist'
	return 1
end

select @closed=Closed from Storefront where ShopID = @shopID

return 0
GO
