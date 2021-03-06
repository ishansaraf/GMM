USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[isValidItem]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[isValidItem]
	(@itemName varchar(30))
AS
RETURN CASE WHEN EXISTS (
    SELECT 1
    FROM Item
    WHERE [Name] = @itemName
)
THEN CAST(1 AS BIT)
ELSE CAST(0 AS BIT) END

GO
