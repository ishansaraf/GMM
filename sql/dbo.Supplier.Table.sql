USE [GMM]
GO
/****** Object:  Table [dbo].[Supplier]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Supplier](
	[SupplierID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](30) NOT NULL,
	[LocationX] [decimal](18, 4) NOT NULL,
	[LocationY] [decimal](18, 4) NOT NULL,
	[Discount] [decimal](18, 4) NULL,
	[ServerID] [int] NULL,
 CONSTRAINT [SupplierID_Index] PRIMARY KEY CLUSTERED 
(
	[SupplierID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [SupplierName_Index] UNIQUE NONCLUSTERED 
(
	[Name] ASC,
	[ServerID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
ALTER TABLE [dbo].[Supplier] ADD  DEFAULT ((0)) FOR [Discount]
GO
