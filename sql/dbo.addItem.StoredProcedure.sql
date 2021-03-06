USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[addItem]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[addItem]
	(@name varchar(30),
	 @basevalue decimal(18, 4),
	 @desc varchar(MAX) = '')
AS

--Checks if Item already exists, if so, throws error
IF EXISTS (SELECT * FROM Item WHERE Name = @name)
	BEGIN
		PRINT 'Item already exists, please enter new details.'
		RETURN 1
	END
else if @basevalue < 0
	begin
		print 'Base value must be greater than or equal to 0'
		return 2
	end
ELSE
	BEGIN
		--Insert given values into Item table
		--No need for ItemID, auto-generated with identity
		INSERT INTO Item(Name, Description, BaseValue)
		VALUES (@name, @desc, @basevalue)

		--Checks for error, taken from CSSE333 Lab07 - Stored Procedures
		DECLARE @Status SMALLINT
		SET @Status = @@ERROR
		IF @Status != 0
			BEGIN
				PRINT 'An error occurred during addition of the item. The error code is: ' + CONVERT(varchar(30), @Status)
				RETURN @Status
			END
		--Insert successful, return
		RETURN 0
	END
GO
