from pyspark.sql.functions import *

from pyspark.sql.window import Window
import os

try:
    readConfigRentals = {
        "spark.cosmos.accountEndpoint": os.environ.get("DB_CONNECTION_URL"),
        "spark.cosmos.accountKey": os.environ.get("DB_KEY"),
        "spark.cosmos.database": os.environ.get("DB_NAME"),
        "spark.cosmos.container": "rental",
        "spark.cosmos.read.customQuery": "SELECT * FROM rentals",
    }

    rentals = spark.read.format("cosmos.oltp").options(**readConfigRentals) \
        .option("spark.cosmos.read.inferSchema.enabled", "true") \
        .load()
    
    groupedRentals = rentals.groupBy("houseID").agg(collect_set("tenantID").alias("tenants"))


    suggestedHouses = groupedRentals.alias("r1").crossJoin(groupedRentals.alias("r2")) \
        .where("r1.houseID != r2.houseID")

    suggestedHouses = suggestedHouses.withColumn("commonTenants", array_intersect("r1.tenants", "r2.tenants")) \
        .withColumn("score", size("commonTenants")) 

    suggestedHouses = suggestedHouses.groupBy("r1.houseID").agg(collect_list(struct("r2.houseID", "score")).alias("suggestedHouses")) \
        .withColumn("suggestedHouses", sort_array("suggestedHouses", False)) \
        .withColumn("suggestedHouses", slice("suggestedHouses", 1, 5)) \
        .withColumn("suggestedHouses", filter("suggestedHouses", lambda x: x.score > 0)) \
        .withColumn("suggestedHouses", col("suggestedHouses.houseID")) \
        .withColumnRenamed("r1.houseID", "houseID") \
        .select("houseID", "suggestedHouses") \
        .orderBy("houseID") 

    suggestedHouses = suggestedHouses.withColumn("id", col("houseID"))

    writeConfig = {
        "spark.cosmos.accountEndpoint": os.environ.get("DB_CONNECTION_URL"),
        "spark.cosmos.accountKey": os.environ.get("DB_KEY"),
        "spark.cosmos.database": os.environ.get("DB_NAME"),
        "spark.cosmos.container": "housesSuggestions",
    }

    suggestedHouses.write.format("cosmos.oltp").options(**writeConfig) \
        .mode("append") \
        .save()

except Exception as e:
    print(e)