const ENV = require('./utils/env')
const express = require('express')
const app = express()

require('./start/db')()
require('./start/redis')


const PORT = ENV.get('PORT', 5000)

app.use(express.json())
app.use(express.urlencoded({ extended: false }))

app.use(express.static('public'))

require('./routes')(app)

app.listen(PORT, function () {
  console.log('Server listening on port ' + PORT)
})

module.exports = app
