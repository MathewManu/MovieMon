CREATE TABLE MOVIE
(
ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
MOVIENAME VARCHAR(100) NOT NULL,
MOVIEFILENAME VARCHAR(100) NOT NULL,
FILESIZE INTEGER NOT NULL,
FILELOCATION VARCHAR(100) NOT NULL,
FILEPROPERTIESID INTEGER,
PRIMARY KEY (ID)
);