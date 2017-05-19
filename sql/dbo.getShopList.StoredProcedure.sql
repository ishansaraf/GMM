USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getShopList]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE procedure [dbo].[getShopList](
	@merUID uniqueidentifier)
AS
SELECT [Name] FROM Storefront WHERE @merUID = MerchantUID
order by Closed asc, [Name] asc

GO
