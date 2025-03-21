// mongo-init.js
print("Creating 'oddk' database...");

db = db.getSiblingDB('oddk');
db.createCollection('test_collection');
db.test_collection.insertOne({ message: "Database initialized!" });

print("Database 'oddk' initialized successfully.");
