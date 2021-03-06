USE [GMM]
GO
/****** Object:  Table [dbo].[Storefront]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Storefront](
	[ShopID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](30) NOT NULL,
	[Funds] [decimal](18, 4) NOT NULL,
	[MerchantUID] [uniqueidentifier] NOT NULL,
	[ServerID] [int] NOT NULL,
	[LocationX] [decimal](18, 4) NOT NULL,
	[LocationY] [decimal](18, 4) NOT NULL,
	[Closed] [bit] NOT NULL,
 CONSTRAINT [ShopID_Index] PRIMARY KEY CLUSTERED 
(
	[ShopID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [ShopName_MerUID_Index] UNIQUE NONCLUSTERED 
(
	[Name] ASC,
	[MerchantUID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
ALTER TABLE [dbo].[Storefront] ADD  DEFAULT ((0)) FOR [Closed]
GO
