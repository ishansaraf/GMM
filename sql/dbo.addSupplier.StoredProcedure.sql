USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[addSupplier]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO




CREATE PROCEDURE [dbo].[addSupplier]
	(@name varchar(30),
	 @serverName varchar(30),
	 @locationX decimal,
	 @locationY decimal,
	 @discount decimal = 0)
AS

declare @serverID int
set @serverID = (select ServerID from [Server] where [Name] = @serverName)
--Checks if storefront already exists, if so, throws error
IF EXISTS (SELECT * FROM Supplier WHERE Name = @name AND ServerID = @serverID)
	BEGIN
		PRINT 'Supplier already exists, please enter new details.'
		RETURN 1
	END
ELSE
	BEGIN
		--Insert given values into Supplier table
		--No need for SupplierID, is auto-generated with identity
		INSERT INTO Supplier([Name], ServerID, LocationX, LocationY, Discount)
		VALUES (@name, @serverID, @locationX, @locationY, @discount)

		--Checks for error, taken from CSSE333 Lab 7 - Stored Procedures
		DECLARE @Status SMALLINT
		SET @Status = @@ERROR
		IF @Status != 0
			BEGIN
				PRINT 'An Error occurred during the addition of the storefront. The error code is: ' +CONVERT(varchar(30), @Status)
			
				RETURN(@Status)
			END

		--Insert successful, return 0
		RETURN 0
	END


GO
