USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getServerList]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE procedure [dbo].[getServerList]
AS
SELECT [Name] FROM Server;

GO
