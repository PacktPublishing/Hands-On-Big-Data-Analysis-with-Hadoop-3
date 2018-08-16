SELECT upper(name), sales_cost FROM products;

SELECT name FROM products WHERE name = 'stone of jordan';

SELECT category, count(1) FROM products GROUP BY category;

SELECT category, count(1) AS cnt FROM products GROUP BY category HAVING cnt > 10;

# Number of Records
SELECT COUNT(1) FROM www_access;

# Number of Unique IPs
SELECT COUNT(1) FROM ( \
  SELECT DISTINCT ip FROM www_access \
) t;

# Number of Unique IPs that Accessed the Top Page
SELECT COUNT(distinct ip) FROM www_access \
  WHERE url='/';

# Number of Accesses per Unique IP
SELECT ip, COUNT(1) FROM www_access \
  GROUP BY ip LIMIT 30;

# Unique IPs Sorted by Number of Accesses
SELECT ip, COUNT(1) AS cnt FROM www_access \
  GROUP BY ip
  ORDER BY cnt DESC LIMIT 30;

# Number of Accesses After a Certain Time
SELECT COUNT(1) FROM www_access \
  WHERE TD_TIME_RANGE(time, "2011-08-19", NULL, "PDT")

# Number of Accesses Each Day
SELECT \
  TD_TIME_FORMAT(time, "yyyy-MM-dd", "PDT") AS day, \
  COUNT(1) AS cnt \
FROM www_access \
GROUP BY TD_TIME_FORMAT(time, "yyyy-MM-dd", "PDT")
