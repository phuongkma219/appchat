const bcrypt = require('bcrypt')

const saltRounds = 10
const salt = bcrypt.genSaltSync(saltRounds)

const hash = (password) => bcrypt.hashSync(password, salt)

const compare = (password, passwordEncrypted) => bcrypt.compareSync(password, passwordEncrypted)

module.exports = { hash, compare }
