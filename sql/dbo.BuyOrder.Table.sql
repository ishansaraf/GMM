USE [GMM]
GO
/****** Object:  Table [dbo].[BuyOrder]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BuyOrder](
	[OrderID] [int] IDENTITY(1,1) NOT NULL,
	[UnitPrice] [decimal](18, 4) NOT NULL,
	[Quantity] [int] NOT NULL,
	[DateTime] [datetime] NOT NULL,
	[PlayerID] [int] NOT NULL,
	[ShopID] [int] NOT NULL,
	[ItemID] [int] NOT NULL,
	[Completed] [bit] NULL,
 CONSTRAINT [OrderID_Index] PRIMARY KEY CLUSTERED 
(
	[OrderID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
ALTER TABLE [dbo].[BuyOrder] ADD  DEFAULT ((1)) FOR [Completed]
GO
ALTER TABLE [dbo].[BuyOrder]  WITH CHECK ADD  CONSTRAINT [FK__BuyOrder__ItemID__3CF40B7E] FOREIGN KEY([ItemID])
REFERENCES [dbo].[Item] ([ItemID])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[BuyOrder] CHECK CONSTRAINT [FK__BuyOrder__ItemID__3CF40B7E]
GO
ALTER TABLE [dbo].[BuyOrder]  WITH CHECK ADD  CONSTRAINT [FK__BuyOrder__Player__3B0BC30C] FOREIGN KEY([PlayerID])
REFERENCES [dbo].[Player] ([PlayerID])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[BuyOrder] CHECK CONSTRAINT [FK__BuyOrder__Player__3B0BC30C]
GO
ALTER TABLE [dbo].[BuyOrder]  WITH CHECK ADD  CONSTRAINT [FK__BuyOrder__ShopID__3BFFE745] FOREIGN KEY([ShopID])
REFERENCES [dbo].[Storefront] ([ShopID])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[BuyOrder] CHECK CONSTRAINT [FK__BuyOrder__ShopID__3BFFE745]
GO
ALTER TABLE [dbo].[BuyOrder]  WITH CHECK ADD  CONSTRAINT [CHK_Buy_Quantity] CHECK  (([Quantity]>(0)))
GO
ALTER TABLE [dbo].[BuyOrder] CHECK CONSTRAINT [CHK_Buy_Quantity]
GO
