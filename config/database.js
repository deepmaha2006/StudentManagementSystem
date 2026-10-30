const mongoose = require("mongoose");

let cached = global.mongoose;
if (!cached) {
    cached = global.mongoose = { conn: null, promise: null };
}

const connectDB = async () => {
    if (cached.conn) return cached.conn;

    if (!cached.promise) {
        cached.promise = mongoose.connect(process.env.MONGODB_URI);
    }

    try {
        cached.conn = await cached.promise;
        console.log("MongoDB Atlas Connected Successfully");
    } catch (error) {
        cached.promise = null;
        console.error("MongoDB Connection Error:", error.message);
        process.exit(1);
    }

    return cached.conn;
};

module.exports = connectDB;
