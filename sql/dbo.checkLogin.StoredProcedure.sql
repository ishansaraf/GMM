USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[checkLogin]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[checkLogin]
	(@username char(20),
	@passHash nvarchar(max) OUTPUT)
as

if (@username not in (select Username from Merchant))
begin
	print 'The username or password is invalid.' + @username
	return 1
end
select @passHash=PasswordHash from Merchant where Username=@username

return 0
GO
