USE [GMM]
GO
/****** Object:  Table [dbo].[ItemsByStorefront]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ItemsByStorefront](
	[ShopID] [int] NOT NULL,
	[ItemID] [int] NOT NULL,
	[Quantity] [int] NOT NULL,
	[UnitPrice] [decimal](18, 4) NOT NULL,
	[Discontinued] [bit] NOT NULL,
 CONSTRAINT [ShopID_ItemID_Index] PRIMARY KEY CLUSTERED 
(
	[ShopID] ASC,
	[ItemID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
ALTER TABLE [dbo].[ItemsByStorefront] ADD  CONSTRAINT [DF__ItemsBySt__Disco__6D9742D9]  DEFAULT ((0)) FOR [Discontinued]
GO
ALTER TABLE [dbo].[ItemsByStorefront]  WITH CHECK ADD  CONSTRAINT [FK_ItemsByStorefront_Item] FOREIGN KEY([ItemID])
REFERENCES [dbo].[Item] ([ItemID])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[ItemsByStorefront] CHECK CONSTRAINT [FK_ItemsByStorefront_Item]
GO
ALTER TABLE [dbo].[ItemsByStorefront]  WITH CHECK ADD  CONSTRAINT [FK_ItemsByStorefront_Storefront] FOREIGN KEY([ShopID])
REFERENCES [dbo].[Storefront] ([ShopID])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[ItemsByStorefront] CHECK CONSTRAINT [FK_ItemsByStorefront_Storefront]
GO
ALTER TABLE [dbo].[ItemsByStorefront]  WITH CHECK ADD  CONSTRAINT [CHK_Quantity] CHECK  (([Quantity]>=(0)))
GO
ALTER TABLE [dbo].[ItemsByStorefront] CHECK CONSTRAINT [CHK_Quantity]
GO
ALTER TABLE [dbo].[ItemsByStorefront]  WITH CHECK ADD  CONSTRAINT [CHK_UnitPrice] CHECK  (([UnitPrice]>=(0)))
GO
ALTER TABLE [dbo].[ItemsByStorefront] CHECK CONSTRAINT [CHK_UnitPrice]
GO
