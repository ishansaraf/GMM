USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getPlayerNameByID]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE procedure [dbo].[getPlayerNameByID] (
	@playerID int)
AS
SELECT UserName FROM Player WHERE PlayerID = @playerID;

GO
