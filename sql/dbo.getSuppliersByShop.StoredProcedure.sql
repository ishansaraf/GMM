USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getSuppliersByShop]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[getSuppliersByShop]
	(@shopName varchar(30),
	@merUID uniqueidentifier)
as
declare @serverID int
declare @shopX decimal(18, 4)
declare @shopY decimal(18, 4)

select @shopX = LocationX, @shopY = LocationY, @serverID = ServerID from Storefront where [Name] = @shopName and MerchantUID = @merUID
--check if input is valid
if @serverID is NULL
begin
	print 'cannot find appropriate shop'
	return 1
end
--retrieve supplier list
select [Name], Discount, ((LocationX - @shopX) * (LocationX - @shopX) + (LocationY - @shopY) * (LocationY - @shopY)) as DistanceSq
from Supplier
where ServerID = @serverID
order by Discount desc, DistanceSq asc
return 0
GO
