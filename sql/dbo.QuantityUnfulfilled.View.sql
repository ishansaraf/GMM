USE [GMM]
GO
/****** Object:  View [dbo].[QuantityUnfulfilled]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE view [dbo].[QuantityUnfulfilled]
as
select ShopID, ItemID, sum(Quantity) as QNotSold from BuyOrder where Completed = 0 group by ItemID, ShopID

GO
