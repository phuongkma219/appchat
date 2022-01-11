const UserModel = require('../models/user')

const { hash } = require('../utils/bcrypt')

const getByAccount = (account) => UserModel.findOne({ account })

const createUser = (data) => {
  const { id, account, password, act_user, user_type } = data
  return UserModel.create({
    id,
    account,
    password: hash(password),
    // act_user,
    user_type,
  })
}

module.exports = {
  getByAccount,
  createUser,
}
