USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getSupplierList]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE procedure [dbo].[getSupplierList]
AS
SELECT [Name] FROM Supplier;

GO
