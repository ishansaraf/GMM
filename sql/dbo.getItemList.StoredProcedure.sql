USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getItemList]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE procedure [dbo].[getItemList]
AS
SELECT [Name], BaseValue FROM Item;

GO
