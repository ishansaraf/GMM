USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getPlayerList]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE procedure [dbo].[getPlayerList]
AS
SELECT PlayerID FROM Player;

GO
