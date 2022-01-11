const Auth = require('./auth')
const User = require('./user')
const Course = require('./course')
const Subject = require('./subject')
const Attendance = require('./attendance')
const { checkAuth } = require('../middleware/auth')

module.exports = (app) => {
  app.use('/auth', Auth)
  app.use(checkAuth)
  app.use('/user', User)
  app.use('/course', Course)
  app.use('/subject', Subject)
  app.use('/attendance', Attendance)
  app.use((req, res) => res.status(404).send('Unable to find the requested resource!'))
}
