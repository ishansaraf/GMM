USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[updateShop]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[updateShop]
	(@shopName varchar(30),
	@merUID uniqueidentifier,
	@closed bit)
as

declare @shopID int
select @shopID = ShopID from Storefront where [Name] = @shopName and MerchantUID = @merUID
--check if shop exists
if @shopID is NULL
begin
	print 'the shop does not exist'
	return 1
end

update Storefront
set Closed = @closed
where [Name] = @shopName and MerchantUID = @merUID

return 0
GO
