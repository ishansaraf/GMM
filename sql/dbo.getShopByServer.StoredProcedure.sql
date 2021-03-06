USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getShopByServer]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[getShopByServer](
	@sname varchar(25),
	@mID uniqueidentifier)
AS
DECLARE @sID int
SET @sID = (SELECT ServerID FROM Server WHERE Name = @sname)

SELECT Name FROM Storefront WHERE ServerID = @sID AND MerchantUID = @mID
ORDER BY Closed ASC, Name ASC


GO
