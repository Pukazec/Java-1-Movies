USE master
GO
DROP DATABASE IF EXISTS Java1
GO
CREATE DATABASE Java1;
GO
USE Java1

---------------------------------------------------------Tables----------------------------------------------

CREATE TABLE Users (
	IdUser INT PRIMARY KEY IDENTITY,
	UserName NVARCHAR(250) NOT NULL,
	Password NVARCHAR(250) NOT NULL,
	Role NVARCHAR(250) NOT NULL,
	CONSTRAINT UQ_UserName UNIQUE (UserName)
)

CREATE TABLE Person(
	IdPerson INT PRIMARY KEY IDENTITY,
	Name NVARCHAR(250) NOT NULL,
	CONSTRAINT UQ_Name UNIQUE (Name)
)

CREATE TABLE Movie(
	IdMovie INT PRIMARY KEY IDENTITY,
	Title NVARCHAR(250) NOT NULL,
	Description NVARCHAR(250) NOT NULL,
	OriginalName NVARCHAR(250) NOT NULL,
	Link NVARCHAR(250) NOT NULL,
	DirectorId INT FOREIGN KEY REFERENCES Person (IdPerson),
	Length INT NOT NULL,
	Genre NVARCHAR(500) NOT NULL,
	PicturePath NVARCHAR(250) NOT NULL,
	InCinemaFrom NVARCHAR(250) NOT NULL,
	Published NVARCHAR(250) NOT NULL,
)

CREATE TABLE Actor(
	IdActor INT PRIMARY KEY IDENTITY,
	MovieId INT NOT NULL FOREIGN KEY REFERENCES Movie(IdMovie),
	PersonId INT NOT NULL FOREIGN KEY REFERENCES Person(IdPerson)
)

GO

--------------------------------------------------------Users------------------------------------------------
CREATE OR ALTER PROC createUser
	@UserName NVARCHAR(250),
	@Password NVARCHAR(250),
	@Role NVARCHAR(250)
AS
BEGIN
	INSERT INTO Users (UserName, Password, Role)
	VALUES(@UserName, @Password, @Role)
END
GO

CREATE OR ALTER PROC selectUser
	@UserName NVARCHAR(250),
	@Password NVARCHAR(250)
AS
BEGIN
	SELECT *
	FROM Users
	WHERE UserName = @UserName
		  AND Password = @Password
END
GO

CREATE OR ALTER PROC selectUsers
AS
BEGIN
	SELECT *
	FROM Users
END
GO

INSERT INTO Users (UserName, Password, Role)
VALUES('admin', '123', 'ADMIN')
GO

--------------------------------------------------------Movie------------------------------------------------

CREATE OR ALTER PROC createMovie
	@Title NVARCHAR(250),
	@Description NVARCHAR(250),
	@OriginalName NVARCHAR(250),
	@Link NVARCHAR(250),
	@Director INT,
	@Length INT,
    @Genre NVARCHAR(250),
	@PicturePath NVARCHAR(250),
	@InCinemaFrom NVARCHAR(250),
	@Published NVARCHAR(250),
	@IdMovie INT OUTPUT
AS
BEGIN
	INSERT INTO Movie(Title, Description, OriginalName, Link, DirectorId, Length, Genre, PicturePath, InCinemaFrom, Published)
	VALUES(@Title, @Description, @OriginalName, @Link, @Director, @Length, @Genre, @PicturePath, @InCinemaFrom, @Published)
	SET @IdMovie = SCOPE_IDENTITY()
END
GO

CREATE OR ALTER PROC updateMovie
	@IdMovie INT,
	@Title NVARCHAR(250),
	@Description NVARCHAR(MAX),
	@OriginalName NVARCHAR(250),
	@Link NVARCHAR(250),
	@Director INT,
	@Length INT,
    @Genre NVARCHAR(250),
	@PicturePath NVARCHAR(250),
	@InCinemaFrom NVARCHAR(250),
	@Published NVARCHAR(250)
AS
BEGIN
	UPDATE Movie
	SET Title = @Title,
		Description = @Description,
		OriginalName = @OriginalName,
		Link = @Link,
		DirectorId = @Director,
		Length = @Length,
		Genre = @Genre,
		PicturePath = @PicturePath,
		InCinemaFrom = @InCinemaFrom,
		Published = @Published
	WHERE IdMovie = @IdMovie
END
GO

CREATE OR ALTER PROC deleteMovie
	@IdMovie INT
AS
BEGIN
	DELETE Actor WHERE MovieId = @IdMovie
	DELETE Movie WHERE IdMovie = @IdMovie
END
GO

CREATE OR ALTER PROC deleteMovies
AS
BEGIN
	DELETE Actor
	DELETE Movie
	DELETE Person
END
GO

CREATE OR ALTER PROC selectMovie
	@Idmovie INT
AS
BEGIN
	SELECT M.IdMovie, M.Title, M.Description, M.OriginalName, M.Link, M.DirectorId, P.Name, M.Length, M.Genre, M.PicturePath, M.InCinemaFrom, M.Published
	FROM Movie AS M
	INNER JOIN Person AS P ON P.IdPerson = M.DirectorId
	WHERE M.IdMovie = @Idmovie
END
GO

CREATE OR ALTER PROC selectMovies
AS
BEGIN
	SELECT M.IdMovie, M.Title, M.Description, M.OriginalName, M.Link, M.DirectorId, P.Name, M.Length, M.Genre, M.PicturePath, M.InCinemaFrom, M.Published
	FROM Movie AS M
	INNER JOIN Person AS P ON P.IdPerson = M.DirectorId
END
GO

--------------------------------------------------------Person------------------------------------------------

CREATE OR ALTER PROC createPerson
	@Name NVARCHAR(250),
	@IdPerson INT OUTPUT
AS
BEGIN
	IF NOT EXISTS(SELECT * FROM Person WHERE Name = @Name)
	BEGIN
		INSERT INTO Person (Name)
		VALUES(@Name)
		SET @IdPerson = SCOPE_IDENTITY()
	END
	ELSE
	BEGIN
		SELECT @IdPerson = IdPerson FROM Person WHERE Name = @Name
	END
END
GO

CREATE OR ALTER PROC updatePerson
	@IdPerson INT,
	@Name NVARCHAR(250)
AS
BEGIN
	UPDATE Person
	SET Name = @Name
	WHERE IdPerson = @IdPerson
END
GO

CREATE OR ALTER PROC deletePerson
	@IdPerson INT
AS
BEGIN
	DELETE Person WHERE IdPerson = @IdPerson
END
GO

CREATE OR ALTER PROC selectPerson
	@IdPerson INT
AS
BEGIN
	SELECT *
	FROM Person
	WHERE IdPerson = @IdPerson
END
GO

CREATE OR ALTER PROC selectPersons
AS
BEGIN 
	SELECT *
	FROM Person
END
GO

--------------------------------------------------------Actor------------------------------------------------

CREATE OR ALTER PROC createActor
	@IdPerson INT,
	@IdMovie INT,
	@IdActor INT OUTPUT
AS
BEGIN
	INSERT INTO Actor(MovieId, PersonId)
	VALUES(@IdMovie, @IdPerson)
	SET @IdActor = SCOPE_IDENTITY()
END
GO

CREATE OR ALTER PROC deleteActor
	@IdMovie INT
AS
BEGIN
	DELETE Actor
	WHERE MovieId = @IdMovie
END
GO

CREATE OR ALTER PROC selectActors
	@IdMovie INT
AS
BEGIN
	SELECT P.IdPerson, P.Name AS Name
	FROM Actor AS A
	INNER JOIN Person AS P ON A.PersonId = P.IdPerson
	WHERE MovieId = @IdMovie
END
GO