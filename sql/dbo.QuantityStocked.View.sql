USE [GMM]
GO
/****** Object:  View [dbo].[QuantityStocked]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create view [dbo].[QuantityStocked]
as
select ShopID, ItemID, sum(Quantity) as STotal from StockOrder group by ItemID, ShopID
GO
