USE [GMM]
GO
/****** Object:  Table [dbo].[Merchant]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Merchant](
	[MerchantUID] [uniqueidentifier] NOT NULL,
	[Username] [varchar](30) NOT NULL,
	[PasswordHash] [varchar](max) NOT NULL,
	[Email] [varchar](50) NOT NULL,
 CONSTRAINT [MerchantUID_Index] PRIMARY KEY CLUSTERED 
(
	[MerchantUID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [MerchantName_Index] UNIQUE NONCLUSTERED 
(
	[Username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
ALTER TABLE [dbo].[Merchant] ADD  DEFAULT (newid()) FOR [MerchantUID]
GO
