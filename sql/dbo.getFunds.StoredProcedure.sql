USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getFunds]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create procedure [dbo].[getFunds]
	(@shopName varchar(30),
	@merUID uniqueidentifier,
	@funds decimal(18, 4) output)
as
set @funds = (select Funds from Storefront where [Name]=@shopName and MerchantUID=@merUID)
return 0
GO
