USE [GMM]
GO
/****** Object:  Table [dbo].[Player]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Player](
	[PlayerID] [int] IDENTITY(1,1) NOT NULL,
	[ServerID] [int] NOT NULL,
	[Username] [varchar](30) NOT NULL,
 CONSTRAINT [PlayerID_Index] PRIMARY KEY CLUSTERED 
(
	[PlayerID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [PlayerName_Index]    Script Date: 5/19/2017 12:54:42 AM ******/
CREATE UNIQUE NONCLUSTERED INDEX [PlayerName_Index] ON [dbo].[Player]
(
	[Username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Player]  WITH CHECK ADD  CONSTRAINT [FK__Player__ServerID__2EA5EC27] FOREIGN KEY([ServerID])
REFERENCES [dbo].[Server] ([ServerID])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[Player] CHECK CONSTRAINT [FK__Player__ServerID__2EA5EC27]
GO
