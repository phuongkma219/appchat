
if ( global.redisClient  == null) {
const redis = require("redis");
const client = redis.createClient({
  host: '103.226.248.62',
  port: 6379,
  password: 'koolsoft'
});

client.on("error", function(error) {
  console.error(error);
});

global.redisClient = client;
}
module.exports = global.redisClient 