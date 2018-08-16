pig -x local

movies = LOAD '/Users/tomasz.lelek/intellij_workspace/packtemr/src/main/resources/movies_data.csv' USING PigStorage(',') as (id,name,year,rating,duration);
DUMP movies;
movies_greater_than_four = FILTER movies BY (float)rating>4.0;
store movies_greater_than_four into '/Users/tomasz.lelek/intellij_workspace/packtemr/src/main/resources/movies_greater_than_four';