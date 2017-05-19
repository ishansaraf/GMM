USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[getMerchantUID]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[getMerchantUID]
	(@username char(20),
	@uid char(36) OUTPUT)
as
SELECT @uid=cast(MerchantUID as char(36)) FROM Merchant where Username=@username
GO
