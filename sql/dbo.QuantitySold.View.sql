USE [GMM]
GO
/****** Object:  View [dbo].[QuantitySold]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[QuantitySold]
as
select ShopID, ItemID, sum(Quantity) as QSold from BuyOrder where Completed = 1 group by ItemID, ShopID
GO
