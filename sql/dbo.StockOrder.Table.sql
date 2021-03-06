USE [GMM]
GO
/****** Object:  Table [dbo].[StockOrder]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[StockOrder](
	[StockingID] [int] IDENTITY(1,1) NOT NULL,
	[ItemID] [int] NOT NULL,
	[Quantity] [int] NOT NULL,
	[DateTime] [datetime] NOT NULL,
	[ShopID] [int] NOT NULL,
	[SupplierID] [int] NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Index [StockingID_Index]    Script Date: 5/19/2017 12:54:42 AM ******/
CREATE CLUSTERED INDEX [StockingID_Index] ON [dbo].[StockOrder]
(
	[StockingID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[StockOrder]  WITH CHECK ADD  CONSTRAINT [FK__StockOrde__ItemI__36470DEF] FOREIGN KEY([ItemID])
REFERENCES [dbo].[Item] ([ItemID])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[StockOrder] CHECK CONSTRAINT [FK__StockOrde__ItemI__36470DEF]
GO
ALTER TABLE [dbo].[StockOrder]  WITH CHECK ADD  CONSTRAINT [FK__StockOrde__ShopI__373B3228] FOREIGN KEY([ShopID])
REFERENCES [dbo].[Storefront] ([ShopID])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[StockOrder] CHECK CONSTRAINT [FK__StockOrde__ShopI__373B3228]
GO
ALTER TABLE [dbo].[StockOrder]  WITH CHECK ADD  CONSTRAINT [FK__StockOrde__Suppl__382F5661] FOREIGN KEY([SupplierID])
REFERENCES [dbo].[Supplier] ([SupplierID])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[StockOrder] CHECK CONSTRAINT [FK__StockOrde__Suppl__382F5661]
GO
ALTER TABLE [dbo].[StockOrder]  WITH CHECK ADD  CONSTRAINT [CHK_Stock_Quantity] CHECK  (([Quantity]>(0)))
GO
ALTER TABLE [dbo].[StockOrder] CHECK CONSTRAINT [CHK_Stock_Quantity]
GO
